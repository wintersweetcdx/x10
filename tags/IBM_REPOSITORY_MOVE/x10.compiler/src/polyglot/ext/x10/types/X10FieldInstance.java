package polyglot.ext.x10.types;

import polyglot.types.FieldInstance;

/**
 * Represents information about a Property. A property has the same
 * attributes as a Field, except that it is always public, instance and final
 * and has no initializer.
 * @author vj
 *
 */
public interface X10FieldInstance extends FieldInstance {
	
	public static final String MAGIC_PROPERTY_NAME = "propertyNames$";
	/** Is this field a property? */
	
	boolean isProperty();
	void setProperty();

}
