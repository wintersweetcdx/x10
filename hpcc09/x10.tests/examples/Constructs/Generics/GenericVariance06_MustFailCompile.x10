// (C) Copyright IBM Corporation 2008
// This file is part of X10 Test. *

import harness.x10Test;

import x10.compiler.ArithmeticOps;

/**
 * @author bdlucas 8/2008
 */

public class GenericVariance06_MustFailCompile extends GenericTest {

    class X {}
    class Y extends X {}
    class Z extends Y {}

    class A[+T] {}

    public def run() = {
        val az:A[Z] = new A[Y](); // no
        return result;
    }

    public static def main(var args: Rail[String]): void = {
        new GenericVariance06_MustFailCompile().execute();
    }
}