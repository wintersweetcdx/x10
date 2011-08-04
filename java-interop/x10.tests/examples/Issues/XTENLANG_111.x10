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

import harness.x10Test;

/**
 * @author bdlucas 10/2008
 */

class XTENLANG_111 extends x10Test {

    static class P {
        public static operator (r: Array[int]): P { throw new RuntimeException(); }
    }
    
    static class A {
        operator this(p:P): int { throw new RuntimeException(); } // FIXME: XTENLANG-1443
        operator this(p:P)=(e:int): void { throw new RuntimeException(); }
    }
        
    def foo(a:A) {
        a([1,2,3]) = 0;
    }

    public def run(): boolean {
        return true;
    }

    public static def main(Array[String](1)) {
        new XTENLANG_111().execute();
    }
}