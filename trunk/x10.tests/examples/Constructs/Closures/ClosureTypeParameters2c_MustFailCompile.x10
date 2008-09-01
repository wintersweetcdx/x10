// (C) Copyright IBM Corporation 2006
// This file is part of X10 Test. *

import harness.x10Test;


/**
 * Type parameters may be constrained by a where clause on the
 * declaration (�4.3,�9.5,�9.7,�12.5).
 */

public class ClosureTypeParameters2c_MustFailCompile extends ClosureTest {

    class V           {static val name = "V";};
    class W extends V {static val name = "W";}
    class X extends V {static val name = "X";};
    class Y extends X {static val name = "Y";};
    class Z extends X {static val name = "Z";};

    public def run(): boolean = {
        
        val c = [T](){Y<:T} => T.name;
        check("c[Z]()", c[Z](), "Z");

        return result;
    }

    public static def main(var args: Rail[String]): void = {
        new ClosureTypeParameters2c_MustFailCompile().execute();
    }
}
