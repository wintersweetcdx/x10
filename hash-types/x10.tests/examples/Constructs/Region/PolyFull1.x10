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
 * Make a full region, examine it, observing an
 * UnboundedRegionException when attemppting to scan it
 */

class PolyFull1 extends TestRegion {

    public def run() {
        var r: Region = Region.makeFull(3);
        prUnbounded("full region", r);
        return status();
    }

    def expected() =
        "--- PolyFull1: full region\n"+
        "rank 3\n"+
        "rect true\n"+
        "zeroBased false\n"+
        "rail false\n"+
        "isConvex() true\n"+
        "size() Incomplete method.\n"+
        "region: full(3)\n"+
        "x10.array.UnboundedRegionException: axis 2 has no minimum\n";
    
    public static def main(Rail[String]) {
        new PolyFull1().execute();
    }
}
