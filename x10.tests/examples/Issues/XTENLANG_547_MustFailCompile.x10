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

public class XTENLANG_547_MustFailCompile extends x10Test {
    static abstract class Writer {
        public abstract def write(x: Byte): void;

        public def write(buf: GlobalRef[Rail[Byte]]): void {
            val mybuf = buf as GlobalRef[Rail[Byte]]{self.home==here};
            write(mybuf, 0, mybuf().size);
        }

        public def write(buf: GlobalRef[Rail[Byte]]{self.home==here}, off: Int, len: Int) {
            for (var i: Int = off; i < off+len; i++) {
                write(buf()(i));
            }
        }
    }

    static class OutputStreamWriter extends Writer { // ERR
        public def write(x: Byte): void { }

        public def write(buf: GlobalRef[Rail[Byte]]): void {
        }

        // This should cause the compiler to issue an error.
        // OutputStreamWriter inherits def write(buf:GlobalRef[Rail[Byte]]{self.home==here}, Int, Int)
        // and its constraint erasure is identical with the method below. But a class cant have two
        // different method definitions whose constraint erasures are identical. And a method
        // can only be overridden by a method which has the same constrained type signature.
        public def write(buf:GlobalRef[Rail[Byte]], off: Int, len: Int): void {
        }
    }

    public static def main(Array[String](1)) {
        new XTENLANG_547_MustFailCompile().execute();
    }

    public def run()=true;
    public def breakit(b:GlobalRef[Rail[byte]], w:OutputStreamWriter) {
        w.write(b, 0, 0); // ERR: Semantic Error: Multiple methods match write(x10.lang.GlobalRef[x10.lang.Rail[x10.lang.Byte]]{self==b}, x10.lang.Int{self==0}, x10.lang.Int{self==0}) [method public XTENLANG_547_MustFailCompile.OutputStreamWriter.write(buf: x10.lang.GlobalRef[x10.lang.Rail[x10.lang.Byte]], off: x10.lang.Int, len: x10.lang.Int), method public XTENLANG_547_MustFailCompile.Writer.write(buf: x10.lang.GlobalRef[x10.lang.Rail[x10.lang.Byte]]{self.x10.lang.GlobalRef#home==here}, off: x10.lang.Int, len: x10.lang.Int)]
    }
}





interface I1547 {
	def m(Int):void;
}
interface I2547 {
	def m(Int{self==1}):void;
}
interface I3547 extends I1547, I2547 {} // ERR

class A547 {
	private def m(Int):void {}
}
class B547 extends A547 {
	private def m(Int{self==1}):void {}     // must be top level for it not to be an error
}
class TestInaccessible547 {
    class A {
        private def m(Int):void {}
    }
    class B extends A { // (in an inner class, even private is accessible! Yoav: wrong! test it in Java.
        private def m(Int{self==1}):void {}
    }
}
class TestAccessible547 {
    class A {
        def m(Int):void {}
    }
    class B extends A { // ERR
        def m(Int{self==1}):void {}
    }
}

class WithConstraintOk547 {
    class C(r:Int) {
        def this() { property(1); }
        def m(Point(r)):void {}
    }
    class D extends C {
        def m(Point(r)):void {}
    }
}
class WithoutConstraintOk547 {
    class C(r:Int) {
        def this() { property(1); }
        def m(Point):void {}
    }
    class D extends C {
        def m(Point):void {}
    }
}
class WithConstraint547 {
    class C(r:Int) {
        def this() { property(1); }
        def m(Point(r)):Int = 1;
    }
    class D extends C { // ERR
        def m(Point(r)):void {} // ERR
    }
}
class WithoutConstraint547 {
    class C(r:Int) {
        def this() { property(1); }
        def m(Point):Int = 1;
    }
    class D extends C { // ERR
        def m(Point):void {} // ERR
    }
}

class E547 {
   def m(x:Int) = 3;
   def m[T](x:Int) = 4;
}
class TypeParameterOverridingOk547 {
    class F {
       def m(x:Int{self==1}) = 3;
    }
    class G extends F {
       def m[T](x:Int) = 4;
    }
}
class TypeParameterOverridingErr547 {
    class F {
       def m[U](x:Int{self==1}) = 3;
    }
    class G extends F { // ERR
       def m[T](x:Int) = 4;
    }
}


class TestOverriding1 {
	class A[T] {
		def m(Int{self!=1}) {}
	}
	class B[U] extends A[A[U]] { // ERR
		def m(Int) {}
	}
}
class TestOverriding2 {
	class A[T] {
		def m(A[T]{self!=null}) {}
	}
	class B[U] extends A[A[U]] { // ERR
		def m(A[A[U]]) {}
	}
}
class TestOverriding22 {
	class A[T] {
		def m(A[T]{self!=null}) {}
	}
	class B[U] extends A[A[U]] {
		def m(A[A[A[U]]]) {}
	}
}
class TestOverriding3 {
	class A[T] {
		def m[G](A[G]{self!=null}) {}
	}
	class B[U] extends A[A[U]] { // ERR
		def m[H](A[H]) {}
	}
}


class A_static547 {
	static def m(Int{self==0}) {}
}
class B_static547 extends A_static547 { // ERR
	def m(Int) {}
}


class XTENLANG_2641 {
	class Parent {
	  def foo(x:Array[byte]) {
		Console.OUT.println("hi p");
	  }
	}

	class Child extends Parent { // ERR
	  def foo(x:Array[byte](1)) {
		Console.OUT.println("hi c");
	  }
	}
}