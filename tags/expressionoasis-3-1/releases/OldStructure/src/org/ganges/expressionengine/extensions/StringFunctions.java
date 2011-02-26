package org.ganges.expressionengine.extensions;

import org.ganges.expressionengine.ExpressionContext;


/**
 * This class provides various utilities methods for String operations
 * 
 * @author Mohit Gupta
 *
 */
public class StringFunctions {

	public StringFunctions( ExpressionContext expressionContext ) {

	}

	public String trim( String value ) {
		return value == null ? null : value.trim();
	}

}
