/*
 * Created on Oct 28, 2004
 */
package x10.array;

import x10.lang.StructureReferenceArray;
import x10.lang.dist;
import x10.lang.place;
import x10.lang.point;

public abstract class StructureArray extends StructureReferenceArray {
    public StructureArray(dist d,int elSize) {
        super(d,elSize);
    }

    public static class Assign extends Operator.Scan {
        private final int c_;

        public Assign(int c) {
        	 throw new RuntimeException("unimplemented");
            //c_ = c;
        }

        public int apply(int i) {
            return c_;
        }
    }

    protected void assign(StructureArray rhs) {
        assert rhs instanceof StructureArray;

        throw new RuntimeException("unimplemented");
        /*
        ShortArray rhs_t =  rhs;
        for (Iterator it = rhs_t.distribution.region.iterator(); it.hasNext();) {
            point pos = (point) it.next();
            set(rhs_t.get(pos), pos);
        }
        */
    }

	/*
	 * Generic implementation - an array with fixed, known number of dimensions
	 * can of course do without the Iterator.
	 */
    public void pointwise(StructureArray res, Operator.Pointwise op, StructureArray arg) {
        assert res.distribution.equals(distribution);
        assert arg.distribution.equals(distribution);
        
        throw new RuntimeException("unimplemented");
       /*
        place here = x10.lang.Runtime.runtime.currentPlace();
        StructureArray arg_t =  arg;
        StructureArray res_t = res;
        try {
            for (Iterator it = distribution.region.iterator(); it.hasNext(); ) {      
                point p = (point) it.next();
                place pl = distribution.get(p);
                x10.lang.Runtime.runtime.setCurrentPlace(pl);
                int arg1 = getInt(p);
                int arg2 = arg_t.get(p);
                int val = op.apply(p, arg1, arg2);
                res_t.set(val, p);
            }
        } finally {
            x10.lang.Runtime.runtime.setCurrentPlace(here);
        }
        */
    }
	
    public void pointwise(StructureArray res, Operator.Pointwise op) {
        assert res == null || res.distribution.equals(distribution);
        throw new RuntimeException("unimplemented");
        /*
        place here = x10.lang.Runtime.runtime.currentPlace();
        try {
            for (Iterator it = distribution.region.iterator(); it.hasNext(); ) {
                point p = (point) it.next();
                place pl = distribution.get(p);
                x10.lang.Runtime.runtime.setCurrentPlace(pl);
                short arg1 = get(p);
                short val = op.apply(p, arg1);
                if (res != null)
                    res.set(val, p);
            } 
        } finally {
            x10.lang.Runtime.runtime.setCurrentPlace(here);
        }
        */
    }
	
    public void reduction(Operator.Reduction op) {
        place here = x10.lang.Runtime.runtime.currentPlace();
        throw new RuntimeException("unimplemented");
        /*
        try {
            for (Iterator it = distribution.region.iterator(); it.hasNext(); ) {
                point p = (point) it.next();
                place pl = distribution.get(p);
                x10.lang.Runtime.runtime.setCurrentPlace(pl);
                short arg1 = get(p);
                op.apply(arg1);
            } 
        } finally {
            x10.lang.Runtime.runtime.setCurrentPlace(here);
        }
        */
    }
	
    public void scan(StructureArray res, Operator.Scan op) {
        assert res.distribution.equals(distribution);
        
        throw new RuntimeException("unimplemented");
        
        /*
        place here = x10.lang.Runtime.runtime.currentPlace();
        try {
            for (Iterator it = distribution.region.iterator(); it.hasNext(); ) {
                point p = (point) it.next();
                place pl = distribution.get(p);
                x10.lang.Runtime.runtime.setCurrentPlace(pl);
                short arg1 = get(p);
                res.set(op.apply(arg1), p);
            }
        } finally {
            x10.lang.Runtime.runtime.setCurrentPlace(here);
        }
        */
    }
	
    public void scan( StructureArray res, pointwiseOp op ) {
        assert res == null || res instanceof StructureArray;
        assert res.distribution.equals(distribution);
        
        throw new RuntimeException("unimplemented");
        
        /*
        
        place here = x10.lang.Runtime.runtime.currentPlace();
        ShortArray res_t = (res == null) ? null : (ShortArray) res;
        try {
            for (Iterator it = distribution.region.iterator(); it.hasNext();) {
                point p = (point) it.next();
                place pl = distribution.get(p);
                x10.lang.Runtime.runtime.setCurrentPlace(pl);
                short val = op.apply(p);
                if (res_t != null)
                    res_t.set(val, p);
            }           
        } finally {
            x10.lang.Runtime.runtime.setCurrentPlace(here);
        }
        */
    }
	public void circshift (int[] args) {
		throw new RuntimeException("TODO");
	}
	
    /**
     * Generic flat access.
     */
	abstract public byte    setByte( byte v, point/*(region)*/ p,int offset);
	abstract public char    setChar( char v, point/*(region)*/ p,int offset);
	abstract public boolean setBoolean( boolean v, point/*(region)*/ p,int offset);
	abstract public short   setShort( short v, point/*(region)*/ p,int offset);
	abstract public int     setInt( int v, point/*(region)*/ p,int offset);
	abstract public long    setLong( long v, point/*(region)*/ p,int offset);
	abstract public float   setFloat( float v, point/*(region)*/ p,int offset);
	abstract public double  setDouble( double v, point/*(region)*/ p,int offset);
	
	abstract public byte    setByte( byte v, int p,int offset);
	abstract public char    setChar( char v, int p,int offset);
	abstract public boolean setBoolean( boolean v, int p,int offset);
	abstract public short   setShort( short v, int p,int offset);
	abstract public int     setInt( int v, int p,int offset);
	abstract public long    setLong( long v, int p,int offset);
	abstract public float   setFloat( float v, int p,int offset);
	abstract public double  setDouble( double v, int p,int offset);
	
	
	abstract public byte    setByte( byte v, int p, int q,int offset);
	abstract public char    setChar( char v, int p, int q,int offset);
	abstract public boolean setBoolean( boolean v, int p, int q,int offset);
	abstract public short   setShort( short v, int p, int q,int offset);
	abstract public int     setInt( int v, int p, int q,int offset);
	abstract public long    setLong( long v, int p, int q,int offset);
	abstract public float   setFloat( float v, int p, int q,int offset);
	abstract public double  setDouble( double v, int p, int q,int offset);
	
	
	abstract public byte    setByte( byte v, int p, int q, int r,int offset);
	abstract public char    setChar( char v, int p, int q, int r,int offset);
	abstract public boolean setBoolean( boolean v, int p, int q, int r,int offset);
	abstract public short   setShort( short v, int p, int q, int r,int offset);
	abstract public int     setInt( int v, int p, int q, int r,int offset);
	abstract public long    setLong( long v, int p, int q, int r,int offset);
	abstract public float   setFloat( float v, int p, int q, int r,int offset);
	abstract public double  setDouble( double v, int p, int q, int r,int offset);
	
	
	abstract public byte    setByte( byte v, int p, int q, int r, int s,int offset);
	abstract public char    setChar( char v, int p, int q, int r, int s,int offset);
	abstract public boolean setBoolean( boolean v, int p, int q, int r, int s,int offset);
	abstract public short   setShort( short v, int p, int q, int r, int s,int offset);
	abstract public int     setInt( int v, int p, int q, int r, int s,int offset);
	abstract public long    setLong( long v, int p, int q, int r, int s,int offset);
	abstract public float   setFloat( float v, int p, int q, int r, int s,int offset);
	abstract public double  setDouble( double v, int p, int q, int r, int s,int offset);
	
		

    /**
     * Generic flat access.
     */
	abstract public byte getByte( point/*(region)*/ p,int offset);
	abstract public char getChar( point/*(region)*/ p,int offset);
	abstract public boolean getBoolean( point/*(region)*/ p,int offset);
	abstract public short getShort( point/*(region)*/ p,int offset);
	abstract public int getInt( point/*(region)*/ p,int offset);
	abstract public long getLong( point/*(region)*/ p,int offset);
	abstract public float getFloat( point/*(region)*/ p,int offset);
	abstract public double getDouble( point/*(region)*/ p,int offset);
	
	abstract public byte    getByte( int p,int offset);
	abstract public char    getChar( int p,int offset);
	abstract public boolean getBoolean( int p,int offset);
	abstract public short   getShort( int p,int offset);
	abstract public int     getInt( int p,int offset);
	abstract public long    getLong( int p,int offset);
	abstract public float   getFloat( int p,int offset);
	abstract public double  getDouble( int p,int offset);
	
	
	abstract public byte    getByte( int p, int q,int offset);
	abstract public char    getChar( int p, int q,int offset);
	abstract public boolean getBoolean( int p, int q,int offset);
	abstract public short   getShort( int p, int q,int offset);
	abstract public int     getInt( int p, int q,int offset);
	abstract public long    getLong( int p, int q,int offset);
	abstract public float   getFloat( int p, int q,int offset);
	abstract public double  getDouble( int p, int q,int offset);
	
	
	abstract public byte    getByte( int p, int q, int r,int offset);
	abstract public char    getChar( int p, int q, int r,int offset);
	abstract public boolean getBoolean( int p, int q, int r,int offset);
	abstract public short   getShort( int p, int q, int r,int offset);
	abstract public int     getInt( int p, int q, int r,int offset);
	abstract public long    getLong( int p, int q, int r,int offset);
	abstract public float   getFloat( int p, int q, int r,int offset);
	abstract public double  getDouble( int p, int q, int r,int offset);
	
	
	abstract public byte    getByte( int p, int q, int r, int s,int offset);
	abstract public char    getChar( int p, int q, int r, int s,int offset);
	abstract public boolean getBoolean( int p, int q, int r, int s,int offset);
	abstract public short   getShort( int p, int q, int r, int s,int offset);
	abstract public int     getInt( int p, int q, int r, int s,int offset);
	abstract public long    getLong( int p, int q, int r, int s,int offset);
	abstract public float   getFloat( int p, int q, int r, int s,int offset);
	abstract public double  getDouble( int p, int q, int r, int s,int offset);
	
	
    
    
    /*
    public Object toJava() {        
        final int[] dims_tmp = new int[distribution.rank];       
        for (int i = 0; i < distribution.rank; ++i) {
            dims_tmp[i] = distribution.region.rank(i).high() + 1;
        }
        
        final Object ret = java.lang.reflect.Array.newInstance(Short.TYPE, dims_tmp);
        pointwise(null, new Operator.Pointwise() {
            public short apply(point p, short arg) {
                Object handle = ret;
                int i = 0;
                for (; i < dims_tmp.length - 1; ++i) {
                    handle = java.lang.reflect.Array.get(handle, p.get(i));
                }
                java.lang.reflect.Array.setShort(handle, p.get(i), arg);
                return arg;
            }
        });
        return ret;
    }
    */
    /* for debugging */
    /*
    public static void printArray(String prefix, short[][] a) {
        System.out.print(prefix + "{");
        for (int i = 0; i < a.length; ++i) {
            System.out.print("{");
            for (int j = 0; j < a[i].length; ++ j) {
                System.out.print(a[i][j]);
                if (j < a[i].length - 1)
                    System.out.print(", ");
            }
            System.out.print("}");
            if (i < a.length - 1)
                System.out.print(", ");
        }
        System.out.println("}");
    }
*/
}