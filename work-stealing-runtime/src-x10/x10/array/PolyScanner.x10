// (C) Copyright IBM Corporation 2006-2008.
// This file is part of X10 Language.

package x10.array;

import x10.io.*;


/**
 * Here's the general scheme for the information used in scanning,
 * illustrated for a region of rank r=4. Each axis Xi is bounded by
 * two sets of halfspaces, min[i] and max[i], obtained from the
 * region halfspaces by FME (resulting in the 0 coefficients as
 * shown). Computing the bounds for Xi requires substituting X0 up to
 * Xi-1 into each halfspace (as shown for the min[i]s) and taking
 * mins and maxes.
 *
 *           0   1   2   r-1
 * 
 * min[0]
 *           A0  0   0   0   B   X0 bounded by B / A0
 *           A0  0   0   0   B   X0 bounded by B / A0
 *           ...                 ...
 * 
 * min[1]
 *           A0  A1  0   0   B   X1 bounded by (B+A0*X0) / A1
 *           A0  A1  0   0   B   X1 bounded by (B+A0*X0) / A1
 *           ...                 ...
 * 
 * min[2]
 *           A0  A1  A2  0   B   X2 bounded by (B+A0*X0+A1*X1) / A2
 *           A0  A1  A2  0   B   X2 bounded by (B+A0*X0+A1*X1) / A2
 *           ...                 ...
 * 
 * min[3]
 *           A0  A1  A2  A3  B   X3 bounded by (B+A0*X0+A1*X1+A2*X2) / A3
 *           A0  A1  A2  A3  B   X3 bounded by (B+A0*X0+A1*X1+A2*X2) / A3
 *           ...                 ...
 *
 * In the innermost loop the bounds for X3 could be computed by
 * substituting the known values of X0 through X2 into each
 * halfspace. However, part of that computation can be pulled out of
 * the inner loop by keeping track for each halfspace in in min[k]
 * and each constraint in max[k] a set of partial sums of the form
 *
 *     minSum[0] = B
 *     minSum[1] = B+A0*X0
 *     ...
 *     minSum[k] = B+A0*X0+A1*X1+...+Ak-1*Xk-1
z *
 * (and similiarly for maxSum) and updating each partial sum
 * minSum[i+1] (and similarly for maxSum[i+1]) every time Xi changes
 * by
 *
 *     minSum[i+1] := sum[i] + Ai*Xi
 *
 * The loop bounds for Xk are then obtained by computing mins and
 * maxes over the sum[k]/Ak for the halfspaces in elim[k].
 *
 * @author bdlucas
 */

final public class PolyScanner/*(C:PolyMat, X:XformMat)*/ implements Region.Scanner {

    /*private*/ public val C: PolyMat;
    /*private*/ public val X1: ValRail[XformMat];

    /*protected*/ public val rank: int;

    private val min: Rail[VarMat];
    private val max: Rail[VarMat];
    private val minSum: Rail[VarMat];
    private val maxSum: Rail[VarMat];

    private val parFlags: Rail[boolean];
    private val min2: Rail[Rail[PolyRow]];
    private val max2: Rail[Rail[PolyRow]];

    public def this(pm: PolyMat): PolyScanner {
        this(pm, XformMat.identity(pm.rank));
    }

    public def this(var pm: PolyMat, X: XformMat) = this(pm, [X]);

    public def this(var pm: PolyMat, X1: ValRail[XformMat]) {

        pm = pm.simplifyAll();

        //property(pm, X);
        this.C = pm;
        this.X1 = X1;

        this.rank = pm.rank;

        min = Rail.makeVar[VarMat](rank);
        max = Rail.makeVar[VarMat](rank);
        minSum = Rail.makeVar[VarMat](rank);
        maxSum = Rail.makeVar[VarMat](rank);
        min2 = Rail.makeVar[Rail[PolyRow]](rank);
        max2 = Rail.makeVar[Rail[PolyRow]](rank);

        init(pm, rank-1);
        for (var k: int = rank-2; k>=0; k--) {
            pm = pm.eliminate(k+1, true);
            init(pm, k);
        }
        //printInfo(Console.OUT);

        parFlags = Rail.makeVar[boolean](rank);
    }

    final private def init(pm: PolyMat, axis: int): void {

        //pm.printInfo(Console.OUT, "pm for axis " + axis);

        // count
        var imin: int = 0;
        var imax: int = 0;
        for (r:PolyRow in pm) {
            if (r(axis)<0) imin++;
            if (r(axis)>0) imax++;
        }

        // complain if unbounded
        if (imin==0 || imax==0) {
            val m = imin==0? "minimum" : "maximum";
            val msg = "axis " + axis + " has no " + m;
            throw new UnboundedRegionException(msg);
        }

        // allocate
        min(axis) = new VarMat(imin, axis+1);
        max(axis) = new VarMat(imax, axis+1);
        minSum(axis) = new VarMat(imin, axis+1);
        maxSum(axis) = new VarMat(imax, axis+1);
        min2(axis) = Rail.makeVar[PolyRow](imin);
        max2(axis) = Rail.makeVar[PolyRow](imax);

        // fill in
        imin=0; imax=0;
        for (r:PolyRow in pm) {
            if (r(axis)<0) {
                for (var i: int = 0; i<=axis; i++)
                    min(axis)(imin)(i) = r(i);
                minSum(axis)(imin)(0) = r(rank);
                min2(axis)(imin) = r;
                imin++;
            }
            if (r(axis)>0) {
                for (var i: int = 0; i<=axis; i++)
                    max(axis)(imax)(i) = r(i);
                maxSum(axis)(imax)(0) = r(rank);
                max2(axis)(imax) = r;
                imax++;
            }
        }

        /*
        min(axis).printInfo(Console.OUT, "min for axis " + axis);
        Console.OUT.println("min2 for axis " + axis);
        for (var i:int=0; i<min2(axis).length; i++)
            min2(axis)(i).printInfo(Console.OUT, i);
        */
    }

    final public def set(axis: int, v: int): void {
        for (var k: int = axis+1; k<rank; k++)
            for (var l: int = 0; l<minSum(k).rows; l++)
                minSum(k)(l)(axis+1) = min(k)(l)(axis)*v + minSum(k)(l)(axis);
        for (var k: int = axis+1; k<rank; k++)
            for (var l: int = 0; l<maxSum(k).rows; l++)
                maxSum(k)(l)(axis+1) = max(k)(l)(axis)*v + maxSum(k)(l)(axis);
    }

    final public def min(axis: int): int {
        var result: int = Int.MIN_VALUE;
        for (var k: int = 0; k<min(axis).rows; k++) {
            val a = min(axis)(k)(axis);
            var b: int = minSum(axis)(k)(axis);
            // ax+b<=0 where a<0 => x>=ceil(-b/a)
            val m = b>0? (-b+a+1)/a : -b/a;
            if (m > result) result = m;
        }
        return result;
    }

    final public def max(axis: int): int {
        var result: int = Int.MAX_VALUE;
        for (var k: int = 0; k<max(axis).rows; k++) {
            val a = max(axis)(k)(axis);
            val b = maxSum(axis)(k)(axis);
            // ax+b<=0 where a>0 => x<=floor(-b/a)
            val m = b>0? (-b-a+1)/a : -b/a;
            if (m < result) result = m;
        }
        return result;
    }


    /**
     * odometer-style iterator for this scanner
     *
     * hasNext() computes the k that is the axis to be bumped:
     *
     * axis    0    1         k        k+1
     * now   x[0] x[1] ...  x[k]   max[k+1] ...  max[rank-1]
     * next  x[0] x[1] ...  x[k]+1 min[k+1] ...  min[rank-1]
     *
     * i.e. bump k, reset k+1 through rank-1
     * finished if k<0
     *
     * next() does the bumping and resetting
     *
     * assumes no degenerate axes, which is guaranteeed by Scanner API
     */

    final private class RailIt implements Iterator[Rail[int]] {
        
        private val rank: int = PolyScanner.this.rank;
        private val s: Region.Scanner = PolyScanner.this;

        private val x = Rail.makeVar[int](rank);
        private val min = Rail.makeVar[int](rank);
        private val max = Rail.makeVar[int](rank);

        private var k: int;

        def this() {
            min(0) = s.min(0);
            max(0) = s.max(0);
            x(0) = min(0);
            for (k=1; k<rank; k++) {
                s.set(k-1, x(k-1));
                val m = s.min(k);
                x(k) = m;
                min(k) = m;
                max(k) = s.max(k);
            }
            x(rank-1)--;
        }

        final public def hasNext(): boolean {
            k = rank-1;
            while (x(k)>=max(k))
                if (--k<0)
                    return false;
            return true;
        }

        final public def next() {
            x(k)++;
            for (k=k+1; k<rank; k++) {
                s.set(k-1, x(k-1));
                val m = s.min(k);
                x(k) = m;
                min(k) = m;
                max(k) = s.max(k);
            }
            return x;
        }

        public def remove() {}
    }

    /**
     * required by API, but less efficient b/c of allocation
     * XXX figure out how to expose
     *   1. Any/Var/ValPoint?
     *   2. hide inside iterator(body:(Point)=>void)?
     */

    final private class PointIt implements Iterator[Point(PolyScanner.this.rank)] {

        val it: RailIt;

        def this() {
            it = new RailIt();
        }

        public final def hasNext() = it.hasNext();
        public final def next(): Point(rank) = it.next() as Point(rank);
        public final def remove() = it.remove();
    }

    public def iterator(): Iterator[Point(rank)] {
        return new PointIt();
    }


    //
    // Xform support
    // XXX move to Scanner
    //

    public def this(r:Region) = this((r as PolyRegion).mat);

    /*
    public def $for(body:(p:Point)=>void) {
        for (p:Point in this)
            body(X*p);
    }
    */

    public def $for(body: (p0:int)=>void) {
        loop((p:Rail[int]) => {body(p(0));});
    }

    public def $for(body: (p0:int, p1:int)=>void) {
        loop((p:Rail[int]) => {body(p(0), p(1));});
    }

    public def $for(body: (p0:int, p1:int, p2:int)=>void) {
        loop((p:Rail[int]) => {body(p(0), p(1), p(2));});
    }

    public def loop(body:(Rail[int])=>void) {
        val p = Rail.makeVar[int](X1(0).rows);
        val q = Rail.makeVar[int](X1(0).cols);
        loop(body, p, q, 0);
    }

    public def par(axis: int) {
        parFlags(axis) = true;
    }

    public def loop(body: (Rail[int])=>void, p:Rail[int], q:Rail[int], r:int) {

        if (r<rank) {

            // lb
            val mn = this.min2(r);
            var min:int = Int.MIN_VALUE;
            for (var l:int=0; l<mn.length; l++) {
                var b:int = mn(l)(rank);
                for (var k:int=0; k<r; k++)
                    b += mn(l)(k) * q(k);
                var a:int = mn(l)(r);
                // ax+b<=0 where a<0 => x>=ceil(-b/a)
                val m = b>0? (-b+a+1)/a : -b/a;
                if (m > min) min = m;
            }

            // ub
            val mx = this.max2(r);
            var max:int = Int.MAX_VALUE;
            for (var l:int=0; l<mx.length; l++) {
                var b:int = mx(l)(rank);
                for (var k:int=0; k<r; k++)
                    b += mx(l)(k) * q(k);
                var a:int = mx(l)(r);
                // ax+b<=0 where a>0 => x<=floor(-b/a)
                val m = b>0? (-b-a+1)/a : -b/a;
                if (m < max) max = m;
            }


            if (parFlags(r)) {
                //Console.OUT.println("finish{");
                finish {
                    for (var i:int=min; i<=max; i++) {
                        q(r) = i;
                        val qq = Rail.makeVar[int](q.length, (i:nat)=>q(i));
                        async {
                            //Console.OUT.println("async{");
                            loop(body, p, qq, r+1);
                            //Console.OUT.println("}async");
                        }
                    }
                }
                //Console.OUT.println("}finish");
            } else {
                for (var i:int=min; i<=max; i++) {
                    set(r, i);
                    q(r) = i;
                    loop(body, p, q, r+1);
                }
            }
        } else {
            for (var i:int=0; i<X1(0).rows; i++) {
                var x:int = 0;
                for (var j:int=0; j<X1(0).cols; j++)
                    x += X1(0)(i)(j)*q(j);
                p(i) = x;
            }
            body(p);
        }
    }

    // XXX doesn't handle n-body scanners correctly
    public def $times(that:Xform): PolyScanner {
        if (that instanceof PolyXform) {
            val p = that as PolyXform;
            return new PolyScanner((C*p.T)||p.E, X1(0)*p.T);
        } else {
            throw new UnsupportedOperationException(this.className() + ".xform(" + that.className() + ")");
        }
    }

    // XXX makes simplifying assumptions about conformance of regions
    // - make this more general!!!
    public def $or(that:PolyScanner) {
        val x = Rail.makeVal(this.X1.length + that.X1.length, (i:nat) =>
            i<this.X1.length? this.X1(i) : that.X1(i-this.X1.length));
        return new PolyScanner(this.C, x);
    }


    //
    // debugging info
    //

    // XXX doesn't handle multi-body case
    public def printInfo(ps: Printer) {
        ps.println("PolyScanner");
        C.printInfo(ps, "  C");
        X1(0).printInfo(ps, "  X");
    }

    public def printInfo2(ps: Printer): void {
        for (var k: int = 0; k<min.length; k++) {
            ps.printf("axis %d\n", k);
            ps.printf("  min\n");
            for (var l: int = 0; l<min(k).rows; l++) {
                ps.printf("  ");
                for (var m: int = 0; m<min(k)(l).cols; m++)
                    ps.printf(" %3d", min(k)(l)(m));
                ps.printf("  sum");
                for (var m: int = 0; m<minSum(k)(l).cols; m++)
                    ps.printf(" %3d", minSum(k)(l)(m));
                ps.printf("\n");
            }
            ps.printf("  max\n");
            for (var l: int = 0; l<max(k).rows; l++) {
                ps.printf("  ");
                for (var m: int = 0; m<max(k)(l).cols; m++)
                    ps.printf(" %3d", max(k)(l)(m));
                ps.printf("  sum");
                for (var m: int = 0; m<maxSum(k)(l).cols; m++)
                    ps.printf(" %3d", maxSum(k)(l)(m));
                ps.printf("\n");
            }
        }
    }
}