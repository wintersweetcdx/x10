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

package x10.core;

import x10.rtt.RuntimeType;
import x10.rtt.Type;

// Base class for all X10 structs
public abstract class Struct implements Any {

    public Struct() {}

    public boolean equals(Object o) {
        return structEquals(o);
    }
    
    abstract public boolean structEquals(Object o);

    public static final RuntimeType<Struct> _RTT = new RuntimeType<Struct>(Struct.class);
    public RuntimeType getRTT() {return _RTT;}
    public Type<?> getParam(int i) {return null;}
}