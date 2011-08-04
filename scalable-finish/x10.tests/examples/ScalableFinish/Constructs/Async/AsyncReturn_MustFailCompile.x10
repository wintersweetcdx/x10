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
 * Testing returns in an async body. 
 * New semantics for X10 1.7 Cannot return from the body of an async.
 *
 * @author vj
 * updated 03/15/09
 */
public class AsyncReturn_MustFailCompile   {

	public def run(): boolean = {
		finish async {
				return;
		}
		return true;
	}

	public static def main(var args: Rail[String]): void = {
		new AsyncReturn_MustFailCompile().run ();
	}
}