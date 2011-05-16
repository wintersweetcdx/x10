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
 * @author bdlucas 8/2008
 */

public class GenericOverloading14 extends GenericTest {

    class A[T] {
        def m() = 0;
        def m(T) = 1;
    }

    class B[S] extends A[S] {
        def m(int,S) = 2;
    }

    val  b = new  B[int]();

    public def run(): boolean = {

        check(" b.m()",  b.m(), 0);
        check(" b.m(0)",  b.m(0), 1);
        check(" b.m(0,0)",  b.m(0,0), 2);

        return result;
    }


    public static def main(var args: Rail[String]): void = {
        new GenericOverloading14().run ();
    }
}