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

package x10.rtt;


import x10.core.RefI;
import x10.core.StructI;

public class Equality {
    public static boolean equalsequals(boolean a, boolean b) { return a == b; }
    public static boolean equalsequals(boolean a, Object b) { return equalsequals((Object) a, b); }
    public static int compareTo(boolean a, boolean b) { return a == b ? 0 : (b ? -1 : 1); }
    public static int compareTo(boolean a, Comparable b) { return compareTo((Comparable) a, b); }
    
    public static boolean equalsequals(byte a, byte b) { return a == b; }
    public static boolean equalsequals(byte a, Object b) { return equalsequals((Object) a, b); }
    public static int compareTo(byte a, byte b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(byte a, short b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(byte a, int b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(byte a, long b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(byte a, float b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(byte a, double b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(byte a, Comparable b) { return compareTo((Comparable) a, b); }

    public static boolean equalsequals(short a, short b) { return a == b; }
    public static boolean equalsequals(short a, Object b) { return equalsequals((Object) a, b); }
    public static int compareTo(short a, byte b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(short a, short b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(short a, int b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(short a, long b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(short a, float b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(short a, double b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(short a, Comparable b) { return compareTo((Comparable) a, b); }
    
    public static boolean equalsequals(char a, char b) { return a == b; }
    public static boolean equalsequals(char a, Object b) { return equalsequals((Object) a, b); }
    public static int compareTo(char a, char b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(char a, Comparable b) { return compareTo((Comparable) a, b); }
    
    public static boolean equalsequals(int a, int b) { return a == b; }
    public static boolean equalsequals(int a, Object b) { return equalsequals((Object) a, b); }
    public static int compareTo(int a, byte b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(int a, short b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(int a, int b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(int a, long b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(int a, float b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(int a, double b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(int a, Comparable b) { return compareTo((Comparable) a, b); }
    
    public static boolean equalsequals(long a, long b) { return a == b; }
    public static boolean equalsequals(long a, Object b) { return equalsequals((Object) a, b); }
    public static int compareTo(long a, byte b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(long a, short b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(long a, int b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(long a, long b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(long a, float b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(long a, double b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(long a, Comparable b) { return compareTo((Comparable) a, b); }
    
    public static boolean equalsequals(float a, float b) { return a == b; }
    public static boolean equalsequals(float a, Object b) { return equalsequals((Object) a, b); }
    public static int compareTo(float a, byte b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(float a, short b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(float a, int b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(float a, long b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(float a, float b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(float a, double b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(float a, Comparable b) { return compareTo((Comparable) a, b); }
    
    public static boolean equalsequals(double a, double b) { return a == b; }
    public static boolean equalsequals(double a, Object b) { return equalsequals((Object) a, b); }
    public static int compareTo(double a, byte b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(double a, short b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(double a, int b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(double a, long b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(double a, float b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(double a, double b) { return a == b ? 0 : (a < b ? -1 : 1); }
    public static int compareTo(double a, Comparable b) { return compareTo((Comparable) a, b); }
    
    public static boolean equalsequals(Object a, boolean b) { return equalsequals(a, (Object) b); }
    public static boolean equalsequals(Object a, byte b) { return equalsequals(a, (Object) b); }
    public static boolean equalsequals(Object a, short b) { return equalsequals(a, (Object) b); }
    public static boolean equalsequals(Object a, char b) { return equalsequals(a, (Object) b); }
    public static boolean equalsequals(Object a, int b) { return equalsequals(a, (Object) b); }
    public static boolean equalsequals(Object a, long b) { return equalsequals(a, (Object) b); }
    public static boolean equalsequals(Object a, float b) { return equalsequals(a, (Object) b); }
    public static boolean equalsequals(Object a, double b) { return equalsequals(a, (Object) b); }
    public static int compareTo(Comparable a, boolean b) { return compareTo(a, (Comparable) b); }
    public static int compareTo(Comparable a, byte b) { return compareTo(a, (Comparable) b); }
    public static int compareTo(Comparable a, short b) { return compareTo(a, (Comparable) b); }
    public static int compareTo(Comparable a, char b) { return compareTo(a, (Comparable) b); }
    public static int compareTo(Comparable a, int b) { return compareTo(a, (Comparable) b); }
    public static int compareTo(Comparable a, long b) { return compareTo(a, (Comparable) b); }
    public static int compareTo(Comparable a, float b) { return compareTo(a, (Comparable) b); }
    public static int compareTo(Comparable a, double b) { return compareTo(a, (Comparable) b); }

    public static boolean equalsequals(Object a, Object b) {
        // Ref equality is pointer equality.
        // This also handles "null == null" and serves as a short cut for other types.
        if (a == b) return true;
        
        // Struct equality is value equality that implys non-null.
        if (a == null || b == null) return false;

        // For boxed String object
        if (a instanceof x10.core.String) a = ((x10.core.String) a).$str;
        if (b instanceof x10.core.String) b = ((x10.core.String) b).$str;
        if (a == b) return true;

        if (a instanceof RefI || b instanceof RefI) return false;
        
        // equality of structs are follows
        if (a instanceof Boolean && b instanceof Boolean)
            return (boolean) (Boolean) a == (boolean) (Boolean) b;
        if (a instanceof Character && b instanceof Character)
            return (char) (Character) a == (char) (Character) b;
        if (a instanceof Number && b instanceof Number)
            return equalsNumbers(a, b);
        if (a instanceof StructI) return ((StructI) a)._struct_equals$O(b);
        
        return false;
    }
    public static int compareTo(Comparable a, Comparable b) { return a.compareTo(b); }
    
    private static boolean equalsNumbers(Object a, Object b) {
        if (a instanceof Double && b instanceof Double) {
            return (double) (Double) a == (double) (Double) b;
        }
        
        if (a instanceof Float && b instanceof Float) {
            return (float) (Float) a == (float) (Float) b;
        }
        
        if (a instanceof Long && b instanceof Long) {
            return (long) (Long) a == (long) (Long) b;
        }
        
        if (a instanceof Integer && b instanceof Integer) {
            return (int) (Integer) a == (int) (Integer) b;
        }
        
        if (a instanceof Short && b instanceof Short) {
            return (short) (Short) a == (short) (Short) b;
        }
        
        if (a instanceof Byte && b instanceof Byte) {
            return (byte) (Byte) a == (byte) (Byte) b;
        }
        
        return false;
    }
}