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

package dims;

public interface Pressure extends Measure { 
    @DerivedUnit(SI.pascal) const pascal: double = _, Pa: double = _;
    @DerivedUnit(101.325e3 * Pa) const atm: double = _;
    @DerivedUnit(1e5 * Pa) const bar: double = _;
    @DerivedUnit(133 * Pa) const mmHg: double = _;
    @DerivedUnit(Force.lbf / Area.inch2) const psi: double = _;
}
