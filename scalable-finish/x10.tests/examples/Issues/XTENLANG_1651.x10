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
 * @author yoav
 */
import x10.compiler.Native;
interface MyTypeName {
	global safe def myTypeName():String;
}
struct MyComplex(r:Int,i:Int) implements MyTypeName {
	@Native("c++", "x10aux::type_name(#0)")
	@Native("java", "x10.core.Ref.typeName(#0)")
    public global native safe def myTypeName():String;
}

public class XTENLANG_1651 extends x10Test {
    public static def main(Rail[String]) {
        new XTENLANG_1651().execute();
    }

    public def run(): boolean {	
        test1();
		test2();

        val i = 3;
        println(i.toString());
        println(i.typeName());
        println(i.home());
        println(i.at(here));// Ref.at(Object,int) Ref.at(Object,Object) at(int,int)
        println(i.at(this));

		return true;
    }
	
	def test1() {		
        val i = true;
        println(i.toString());
        println(i.typeName());
        println(i.home());
        println(i.at(here));
        println(i.at(this));
	}
	def test2() {		
        val i = MyComplex(4,5);
        println(i.toString());
        println(i.typeName());
        println(i.home());
        println(i.at(here));
        println(i.at(this));

		
        println(i.myTypeName());
	}

	def println(a:Any) {}
}