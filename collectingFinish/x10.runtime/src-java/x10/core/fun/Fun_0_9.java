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

package x10.core.fun;

import x10.rtt.RuntimeType;
import x10.rtt.Type;

public interface Fun_0_9<T1,T2,T3,T4,T5,T6,T7,T8,T9,U> {
    U apply(T1 o1, T2 o2, T3 o3, T4 o4, T5 o5, T6 o6, T7 o7, T8 o8, T9 o9);
    Type<?> rtt_x10$lang$Fun_0_9_Z1();
    Type<?> rtt_x10$lang$Fun_0_9_Z2();
    Type<?> rtt_x10$lang$Fun_0_9_Z3();
    Type<?> rtt_x10$lang$Fun_0_9_Z4();
    Type<?> rtt_x10$lang$Fun_0_9_Z5();
    Type<?> rtt_x10$lang$Fun_0_9_Z6();
    Type<?> rtt_x10$lang$Fun_0_9_Z7();
    Type<?> rtt_x10$lang$Fun_0_9_Z8();
    Type<?> rtt_x10$lang$Fun_0_9_Z9();
    Type<?> rtt_x10$lang$Fun_0_9_U();

    public static class RTT extends RuntimeType<Fun_0_9<?,?,?,?,?,?,?,?,?,?>>{
        Type<?> T1;
        Type<?> T2;
        Type<?> T3;
        Type<?> T4;
        Type<?> T5;
        Type<?> T6;
        Type<?> T7;
        Type<?> T8;
        Type<?> T9;
        Type<?> U;

        public RTT(Type<?> T1, Type<?> T2, Type<?> T3, Type<?> T4, Type<?> T5, Type<?> T6, Type<?> T7, Type<?> T8, Type<?> T9, Type<?> U) {
            super(Fun_0_9.class);
            this.T1 = T1;
            this.T2 = T2;
            this.T3 = T3;
            this.T4 = T4;
            this.T5 = T5;
            this.T6 = T6;
            this.T7 = T7;
            this.T8 = T8;
            this.T9 = T9;
            this.U = U;
        }

        @Override
        public boolean instanceof$(Object o) {
            if (o instanceof Fun_0_9) {
                Fun_0_9<?,?,?,?,?,?,?,?,?,?> v = (Fun_0_9<?,?,?,?,?,?,?,?,?,?>) o;
                if (! v.rtt_x10$lang$Fun_0_9_U().isSubtype(U)) return false; // covariant
                if (! T1.isSubtype(v.rtt_x10$lang$Fun_0_9_Z1())) return false; // contravariant
                if (! T2.isSubtype(v.rtt_x10$lang$Fun_0_9_Z2())) return false; // contravariant
                if (! T3.isSubtype(v.rtt_x10$lang$Fun_0_9_Z3())) return false; // contravariant
                if (! T4.isSubtype(v.rtt_x10$lang$Fun_0_9_Z4())) return false; // contravariant
                if (! T5.isSubtype(v.rtt_x10$lang$Fun_0_9_Z5())) return false; // contravariant
                if (! T6.isSubtype(v.rtt_x10$lang$Fun_0_9_Z6())) return false; // contravariant
                if (! T7.isSubtype(v.rtt_x10$lang$Fun_0_9_Z7())) return false; // contravariant
                if (! T8.isSubtype(v.rtt_x10$lang$Fun_0_9_Z8())) return false; // contravarian/
                if (! T9.isSubtype(v.rtt_x10$lang$Fun_0_9_Z9())) return false; // contravariant
                return true;
            }
            return false;
        }

        @Override
        public boolean isSubtype(Type<?> o) {
            if (! super.isSubtype(o))
                return false;
            if (o instanceof Fun_0_9.RTT) {
                Fun_0_9.RTT t = (RTT) o;
                return U.isSubtype(t.U)
                        && t.T1.isSubtype(T1)
                        && t.T2.isSubtype(T2)
                        && t.T3.isSubtype(T3)
                        && t.T4.isSubtype(T4)
                        && t.T5.isSubtype(T5)
                        && t.T6.isSubtype(T6)
                        && t.T7.isSubtype(T7)
                        && t.T8.isSubtype(T8)
                        && t.T9.isSubtype(T9);
            }
            return false;
        }
    }
}