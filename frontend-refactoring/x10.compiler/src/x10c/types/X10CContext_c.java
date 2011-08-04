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

package x10c.types;

import java.util.ArrayList;
import java.util.List;

import x10.types.ClassDef;
import x10.types.TypeSystem;
import x10.types.Context;
import x10.types.Context_c;

public class X10CContext_c extends Context_c implements Context {

    private final List<ClassDef> generatedClasses = new ArrayList<ClassDef>();

    public X10CContext_c(TypeSystem ts) {
        super(ts);
    }

    public boolean isContainsGeneratedClasses(ClassDef cd) {
        return generatedClasses.contains(cd);
    }

    public void addGeneratedClasses(ClassDef cd) {
        generatedClasses.add(cd);
    }

}