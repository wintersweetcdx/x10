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
 * Nested barriers test.
 *
 * N outer activities each
 * create M inner activities that do
 * barrier syncs. Then the outer activities do a barrier sync
 * and check the results.
 *
 * @author kemal 4/2005
 */
public class ClockTest9   {

	public const N: int = 8;
	public const M: int = 8;
	val v: Rail[int]! = Rail.make[int](N, (x:int)=>0);

	public def run(): boolean = {
		finish async {
			val c: Clock = Clock.make();

			// outer barrier loop
			foreach ((i):Point(1) in 0..N-1) clocked(c) {
				foreachBody(i, c);
			}
		}
		return true;
	}

	def foreachBody(val i: int, val c: Clock): void = {
		async(here) clocked(c) finish async(here) {
			val d: Clock = Clock.make();

			// inner barrier loop
			foreach ((j):Point(1) in 0..M-1) clocked(d) {
				foreachBodyInner(i, j, d);
			}
		}
		x10.io.Console.OUT.println("#0a i = "+i);
		next;
		// at this point each val[k] must be 0
		async(here) clocked(c) finish async(here) for ((k):Point(1) in 0..N-1) chk(v(k) == 0);
		x10.io.Console.OUT.println("#0b i = "+i);
		next;
	}

	def foreachBodyInner(val i: int, val j: int, val d: Clock): void = {
		// activity i, j increments val[i] by j
		async(here) clocked(d) finish async(here) { atomic v(i) += j; }
		x10.io.Console.OUT.println("#1 i = "+i+" j = "+j);
		next;
		// val[i] must now be SUM(j = 0 to M-1)(j)
		async(here) clocked(d) finish async(here) { var tmp: int; atomic tmp = v(i); chk(tmp == M*(M-1)/2); }
		x10.io.Console.OUT.println("#2 i = "+i+" j = "+j);
		next;
		// decrement val[i] by the same amount
		async(here) clocked(d) finish async(here) { atomic v(i) -= j; }
		x10.io.Console.OUT.println("#3 i = "+i+" j = "+j);
		next;
		// val[i] should be 0 by now
	}

	public static def main(var args: Rail[String]): void = {
		new ClockTest9().run Async();
	}
}