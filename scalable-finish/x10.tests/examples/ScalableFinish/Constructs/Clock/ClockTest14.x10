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
 * Check if illegal uses of clock are raising an exception.
 * @author kemal 4/2005
 */
public class ClockTest14   {

	public def run(): boolean = {
		val c = Clock.make();
		var gotException: boolean;
		next;
		c.resume();
		c.drop();
		chk(c.dropped());
		next; // empty clock set is acceptable, next is no-op
		gotException = false;
		try {
			c.resume();
		} catch (var e: ClockUseException) {
			gotException = true;
		}
		chk(gotException);
		gotException = false;
		try {
			async clocked(c) { }
		} catch (var e: ClockUseException) {
			gotException = true;
		}
		chk(gotException);
		return true;
	}

	public static def main(var args: Rail[String]): void = {
		new ClockTest14().run ();
	}
}