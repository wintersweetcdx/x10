/*
 * Created on Oct 13, 2004
 */
package x10.compilergenerated;

import x10.runtime.Clock;

/**
 * This code is hand-written for now.  In the future the plan is to either
 * use generics (ClockedFinal<TYPE>) or to have the compiler generate
 * the necessary set of ClockedFinal subtypes automatically.
 * 
 * @author Christian Grothoff
 * @see ClockedFinal for more details on how this class is supposed to be used
 */
public class ClockedFinalBoolean extends ClockedFinal {

    public boolean current;
    
    public boolean next;
    
    public ClockedFinalBoolean(Clock c, boolean i) {
        super(c);
        current = next = i;
    }
    
    /* (non-Javadoc)
     * @see x10.compilergenerated.ClockedFinal#advance()
     */
    void advance() {
        current = next;
    }

}