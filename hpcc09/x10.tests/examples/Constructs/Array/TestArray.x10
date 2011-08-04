import x10.io.Printer;
import x10.io.StringWriter;

import x10.array.UnboundedRegionException;

import harness.x10Test;


abstract public class TestArray extends x10Test {
    
    var os: StringWriter;
    var out: Printer;
    val testName = typeName();

    def this() {
        System.setProperty("line.separator", "\n");
        try {
            os = new StringWriter();
            out = new Printer(os);
        } catch (e:Exception) {
            //e.printStackTrace();
            x10.io.Console.OUT.println(e.toString());
        }
    }

    abstract def expected():String;

    def status() {
        val got = os.toString();
        if (got.equals(expected())) {
            return true;
        } else {
            x10.io.Console.OUT.println("=== got:\n" + got);
            x10.io.Console.OUT.println("=== expected:\n" + expected());
            x10.io.Console.OUT.println("=== ");
            return false;
        }
    }

    //
    //
    //

    abstract class R {

        def this(test: String): R = {
            var r: String;
            try {
                r = run();
            } catch (e: Throwable) {
                r = e.getMessage();
            }
            pr(test + " " + r);
        }

        abstract def run(): String;

    }
            
    class Grid {

        var os: Rail[Object] = Rail.makeVar[Object](10);

        def set(i0: int, vue: double): void = {
            os(i0) = vue as Object; // XTENLANG-210
        }

        def set(i0: int, i1: int, vue: double): void = {
            if (os(i0)==null) os(i0) = new Grid();
            val grid = os(i0) as Grid;
            grid.set(i1, vue);
        }

        def set(i0: int, i1: int, i2: int, vue: double): void = {
            if (os(i0)==null) os(i0) = new Grid();
            val grid = os(i0) as Grid;
            grid.set(i1, i2, vue);
        }

        def pr(rank: int): void = {
            var min: int = os.length;
            var max: int = 0;
            for (var i: int = 0; i<os.length; i++) {
                if (os(i)!=null) {
                    if (i<min) min = i;
                    else if (i>max) max = i;
                }
            }
            for (var i: int = 0; i<os.length; i++) {
                var o: Object = os(i);
                if (o==null) {
                    if (rank==1)
                        out.print(".");
                    else if (rank==2) {
                        if (min<=i && i<=max)
                            out.print("    " + i + "\n");
                    }
                } else if (o instanceof Grid) {
                    if (rank==2)
                        out.print("    " + i + "  ");
                    else if (rank>=3) {
                        out.print("    ");
                        for (var j: int = 0; j<rank; j++)
                            out.print("-");
                        out.print(" " + i + "\n");
                    }
                    (o as Grid).pr(rank-1);
                } else {
                    // XTENLANG-34, XTENLANG-211
                    val d = (o as Box[double]) as double;
                    out.print((d as int)+"");
                }

                if (rank==1)
                    out.print(" ");
            }
            if (rank==1)
                out.print("\n");
        }
    }

    def prArray(test: String, r: Region): Array[double]{rank==r.rank} = {
        return prArray(test, r, false);
    }

    def prArray(test: String, r: Region, bump: boolean): Array[double]{rank==r.rank} = {

        val init1 = (pt: Point) => {
            var v: int = 1;
            for (var i: int = 0; i<pt.rank; i++)
                v *= pt(i);
            return v%10 as double;
        };

        val init0 = (Point) => 0.0D;

        val a = Array.make[double](r, bump? init0 : init1);
        prArray(test, a, bump);

        return a as Array[double]{rank==r.rank};
    }

    // XXX move to Place.toString
    def s(p:Place) = "place(id=" + p.id + ")";

    def prDistributed(test: String, a: Array[double]): void = {
        var ps: Rail[Place] = a.dist.places();
        for (var i: int = 0; i<ps.length; i++) {
            val p: Place = ps(i);
            finish {
                async (p) {
                    prArray(test + " at " + s(p) + " (by place)", a.$bar(p));
                    val r = a.dist.get(p);
                    prArray(test + " at " + s(p) + " (by region)", a.$bar(r));
                }
            }
        }
    }


    def prUnbounded(test: String, r: Region): void = {
        try {
            prRegion(test, r);
            var s: Region.Scanner = r.scanners().next() as Region.Scanner; // XTENLANG-55
            var i: Iterator[Point] = r.iterator();
        } catch (e: UnboundedRegionException) {
            pr(e.toString());
        }
    }


    def prRegion(test: String, r: Region): void = {

        pr("--- " + testName + ": " + test);

        new R("rank")		{def run(): String = {return "" + r.rank;}};
        new R("rect")		{def run(): String = {return "" + r.rect;}};
        new R("zeroBased")	{def run(): String = {return "" + r.zeroBased;}};
        new R("rail")		{def run(): String = {return "" + r.rail;}};

        new R("isConvex()")	{def run(): String = {return "" + r.isConvex();}};
        new R("size()")		{def run(): String = {return "" + r.size();}};

        pr("region: " + r);

    }

    def prArray(test: String, a: Array[double]): void = {
        prArray(test, a, false);
    }

    def prArray(test: String, a: Array[double], bump: boolean): void = {

        val r: Region = a.region;

        prRegion(test, r);

        // scanner api
        var grid: Grid = new Grid();
        var it: Iterator[Region.Scanner] = r.scanners();
        while (it.hasNext()) {
            var s: Region.Scanner = it.next() as Region.Scanner; // XTENLANG-55
            pr("  poly");
            if (r.rank==0) {
                pr("ERROR rank==0");
            } else if (r.rank==1) {
                val a2 = a as Array[double](1);
                var min0: int = s.min(0);
                var max0: int = s.max(0);
                for (var i0: int = min0; i0<=max0; i0++) {
                    if (bump) a2(i0) = a2(i0)+1;
                    grid.set(i0, a2(i0));
                }
            } else if (r.rank==2) {
                val a2 = a as Array[double](2);
                var min0: int = s.min(0);
                var max0: int = s.max(0);
                for (var i0: int = min0; i0<=max0; i0++) {
                    s.set(0, i0);
                    var min1: int = s.min(1);
                    var max1: int = s.max(1);
                    for (var i1: int = min1; i1<=max1; i1++) {
                        if (bump) a2(i0, i1) = a2(i0, i1) + 1;
                        grid.set(i0, i1, a2(i0,i1));
                    }
                }
            } else if (r.rank==3) {
                val a2 = a as Array[double](3);
                var min0: int = s.min(0);
                var max0: int = s.max(0);
                for (var i0: int = min0; i0<=max0; i0++) {
                    s.set(0, i0);
                    var min1: int = s.min(1);
                    var max1: int = s.max(1);
                    for (var i1: int = min1; i1<=max1; i1++) {
                        s.set(1, i1);
                        var min2: int = s.min(2);
                        var max2: int = s.max(2);
                        for (var i2: int = min2; i2<=max2; i2++) {
                            if (bump) a2(i0, i1, i2) = a2(i0, i1, i2) + 1;
                            grid.set(i0, i1, i2, a2(i0,i1,i2));
                        }
                    }
                }
            }
        }
        grid.pr(r.rank);

        pr("  iterator");
        prArray1(a, /*bump*/ false); // XXX use bump, update tests
    }

    def prArray1(a: Array[double], bump: boolean): void = {
        // iterator api
        var grid: Grid = new Grid();
        for (p:Point in a.region) {
            //var v: double = a(p as Point(a.rank));
            if (p.rank==1) {
                val a2 = a as Array[double](1);
                if (bump) a2(p(0)) = a2(p(0)) + 1;
                grid.set(p(0), a2(p(0)));
            } else if (p.rank==2) {
                val a2 = a as Array[double](2);
                if (bump) a2(p(0), p(1)) = a2(p(0), p(1)) + 1;
                grid.set(p(0), p(1), a2(p(0),p(1)));
            } else if (p.rank==3) {
                val a2 = a as Array[double](3);
                if (bump) a2(p(0), p(1), p(2)) = a2(p(0), p(1), p(2)) + 1;
                grid.set(p(0), p(1), p(2), a2(p(0),p(1),p(2)));
            }
        }
        grid.pr(a.rank);
    }


    def prPoint(test: String, p: Point): void = {
        var sum: int = 0;
        for (var i: int = 0; i<p.rank; i++)
            sum += p(i);
        pr(test + " " + p + " sum=" + sum);
    }


    def prDist(test: String, d: Dist): void = {

        pr("--- " + test + ": " + d);

        val init = (Point) => -1.0D;
        val a = Array.make[double](d.region, init);

        var ps: Rail[Place] = d.places();
        for (var i: int = 0; i<ps.length; i++) {
            var r: Region = d.get(ps(i));
            for (p:Point(r.rank) in r) {
                val q = p as Point(a.dist.region.rank);
                a(q) = a(q) + ps(i).id + 1;
            }
        }
        prArray1(a, false);
    }
        

    def pr(s: String): void = {
        out.println(s);
    }

    static def xxx(s: String): void {
        x10.io.Console.OUT.println("xxx " + s);
    }

    // substitute for [a:b,c:d]
    def r(a: int, b: int, c: int, d: int): Region(2) {
        return Region.makeRectangular([a,c], [b,d]);
    }

}