package x10.lang;

/** The class of all multidimensional,  distributed int value arrays
 * in X10.  Specialized from valueArray by replacing the type parameter
 * with int.

 * Handtranslated from the X10 code in x10/lang/intValueArray.x10
 * 
 * @author vj 1/9/2005
 */

public abstract /*value*/ class complex4ValueArray extends complex4Array implements ValueType  {
	
	public complex4ValueArray( dist D) {
		super( D );
	}
	
}