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

package x10.lang;

import x10.compiler.Native;
import x10.compiler.NativeRep;

@NativeRep("java", "x10.core.Settable<#1, #2>", null, "new x10.rtt.ParameterizedType(x10.core.Settable._RTT, #2, #4)")
public interface Settable[-I,V] {
    @Native("java", "(#0).set$G(#1, #2)")
    def set(v: V, i: I): V;
}