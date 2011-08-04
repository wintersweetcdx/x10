/*
 *  This file is part of the X10 project (http://x10-lang.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2006-2010.
 */

/**
 * @author bdlucas
 */

public class ArrayMap extends TestArray {

    public const N: int = 9;

    public def run(): boolean {
	chk(Place.places.length == 4, "This test must be run with 4 places");

        val dist = Dist.makeBlock(0..N);
        prDist("dist", dist);

        pr("--- original");
        val a: DistArray[double](dist) = DistArray.make[double](dist, (p:Point)=>p(0) as double);
        for (pt:Point(1) in a) {
            val x = (future(a.dist(pt)) a(pt)).force();
            out.print(x + " ");
        }
        out.println();

        pr("--- mapped");
        val b = a.map((a:double)=>1.5*a) as DistArray[double](dist);
        for (pt:Point(1) in b) {
            val x = (future(b.dist(pt)) b(pt)).force();
            out.print(x + " ");
        }
        out.println();

        return status();
    }

    public static def main(var args: Rail[String]): void = {
        new ArrayMap().run ();
    }

    def expected() = 
        "--- dist: Dist([0..2]->0,[3..5]->1,[6..7]->2,[8..9]->3)\n" + 
        "0 0 0 1 1 1 2 2 3 3 \n" +
        "--- original\n" +
        "0.0 1.0 2.0 3.0 4.0 5.0 6.0 7.0 8.0 9.0 \n" +
        "--- mapped\n" +
        "0.0 1.5 3.0 4.5 6.0 7.5 9.0 10.5 12.0 13.5 \n";

}