// (C) Copyright IBM Corporation 2008
// This file is part of X10 Test. *

import harness.x10Test;


/**
 * As with a normal method, the closure may have a throws clause
 *
 * @author bdlucas 8/2008
 */


public class ClosureException1f extends ClosureTest {

    public def run(): boolean = {
        
        val f = () throws Exception => 1;
        check("f()", f(), 1);

        return result;
    }

    public static def main(var args: Rail[String]): void = {
        new ClosureException1f().execute();
    }
}