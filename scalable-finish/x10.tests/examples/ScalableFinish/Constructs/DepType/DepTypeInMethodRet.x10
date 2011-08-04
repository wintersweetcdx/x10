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
 * Check that a method can have a deptype argument and it is checked properly.
 *
 * @author vj
 */
public class DepTypeInMethodRet   {

    public def m(t: Boolean(true))=t;
    public def run() =m(true);
    public static def main(Rail[String]) {
	new DepTypeInMethodRet().run ();
    }
}