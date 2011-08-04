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
package x10.x10rt;

import x10.runtime.impl.java.Runtime;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * A class to contain the Java portion of message send/receive pairs.
 */
public class MessageHandlers {
		
	/*
	 * This send/receive pair is used to serialize a ()=>void closure to
	 * a remote place, which deserializes the closure object and calls eval on it.
	 * 
	 * One important use of this message pair is the non-optimized implementation of
	 * x10.lang.Runtime.runClosureAt and x10.lang.Runtime.runClosureCopyAt. 
	 */
	
    public static void runClosureAtSend(int place, int arraylen, byte[] rawBytes) {
    	runClosureAtSendImpl(place, arraylen, rawBytes);
    }
    
    private static native void runClosureAtSendImpl(int place, int arraylen, byte[] rawBytes);
    
    // Invoked from native code at receiving place
    // This function gets called by the callback thats registered to handle messages with the X10 RT implementation
    private static void runClosureAtReceive(byte[] args) {
    	try{
    		if (X10RT.VERBOSE) System.out.println("@MultiVM : runClosureAtReceive is called");
    		java.io.ByteArrayInputStream byteStream 
    			= new java.io.ByteArrayInputStream(args);
    		if (X10RT.VERBOSE) System.out.println("runClosureAtReceive: ByteArrayInputStream");
            x10.core.fun.VoidFun_0_0 actObj;
            InputStream objStream;
            if (X10JavaSerializable.CUSTOM_JAVA_SERIALIZATION) {
                objStream = new DataInputStream(byteStream);
    		    if (X10RT.VERBOSE) System.out.println("runClosureAtReceive: ObjectInputStream");
                X10JavaDeserializer deserializer = new X10JavaDeserializer((DataInputStream) objStream);
                if (x10.runtime.impl.java.Runtime.TRACE_SER_DETAIL) {
                    System.out.println("Starting deserialization ");
                }
                actObj = (x10.core.fun.VoidFun_0_0) deserializer.readRef();
                if (x10.runtime.impl.java.Runtime.TRACE_SER_DETAIL) {
                    System.out.println("Ending deserialization ");
                }
            } else if (X10JavaSerializable.CUSTOM_JAVA_SERIALIZATION_USING_REFLECTION) {
                objStream = new DataInputStream(byteStream);
    		    if (X10RT.VERBOSE) System.out.println("runClosureAtReceive: ObjectInputStream");
                X10JavaDeserializer deserializer = new X10JavaDeserializer((DataInputStream) objStream);
                if (x10.runtime.impl.java.Runtime.TRACE_SER_DETAIL) {
                    System.out.println("Starting deserialization ");
                }
                actObj = (x10.core.fun.VoidFun_0_0) deserializer.readRefUsingReflection();
                if (x10.runtime.impl.java.Runtime.TRACE_SER_DETAIL) {
                    System.out.println("Ending deserialization ");
                }
            } else {
                objStream = new java.io.ObjectInputStream(byteStream);
    		    if (X10RT.VERBOSE) System.out.println("runClosureAtReceive: ObjectInputStream");
                actObj = (x10.core.fun.VoidFun_0_0) ((ObjectInputStream)objStream).readObject();
            }
    		if (X10RT.VERBOSE) System.out.println("runClosureAtReceive: after cast and deserialization");
    		actObj.$apply();
    		if (X10RT.VERBOSE) System.out.println("runClosureAtReceive: after apply");
    		objStream.close();
    		if (X10RT.VERBOSE) System.out.println("runClosureAtReceive is done !");
    	} catch(Exception ex){
    		System.out.println("runClosureAtReceive error !!!");
    		ex.printStackTrace();
    	}
    }
    
    static native void initialize();
}