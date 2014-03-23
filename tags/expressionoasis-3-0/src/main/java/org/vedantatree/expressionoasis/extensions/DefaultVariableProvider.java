/**
 * Copyright (c) 2006 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.extensions;

import java.util.HashMap;
import java.util.Map;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;
import org.vedantatree.utils.StringUtils;


/**
 * This is the default implementation of the variable provider.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * 
 * @version 1.0
 *
 * Modified to use HashMap rather than Hashtable for performance reasons. I think
 * this is safe to do, but may need to reverse if thread-safety issues are encountered.
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class DefaultVariableProvider implements VariableProvider {

	/**
	 * This the variable name and value mapping.
	 */
	private Map<String, ValueObject> variableValues = new HashMap<String, ValueObject>();

	/**
	 * It initialize the Function provider with expression context and also
	 * gives a chance to pre-initialize any internal states for operations
	 * 
	 * @param expressionContext Context of current expression evaluation process
	 */
	public void initialize( ExpressionContext expressionContext ) throws ExpressionEngineException {
	}

	/**
	 * Add a new variable value to Variable Provider Cache
	 * 
	 * @param variableName name of the variable to add
	 * @param valueObject value of the variable
	 */
	public void addVariable( String variableName, ValueObject valueObject ) {
		if( !StringUtils.isQualifiedString( variableName ) || valueObject == null ) {
			throw new IllegalArgumentException( "Passed parameters are not valid." );
		}

		variableValues.put( variableName, valueObject );
	}

	/**
	 * Removes the variable from Variable Provider Cache by given name
	 * 
	 * @param variableName name of the the variable to remove
	 */
	public void removeVariable( String variableName ) {
		variableValues.remove( variableName );
	}

	/**
	 * Clears the variable names.
	 */
	public void clear() {
		variableValues.clear();
	}

	/**
	 * @see org.vedantatree.expressionoasis.extensions.VariableProvider#getVariableType(java.lang.String)
	 */
	public Type getVariableType( String variableName ) throws ExpressionEngineException {
		Type varType = null;

		if( supportsVariable( variableName ) ) {
			varType = ( (ValueObject) variableValues.get( variableName ) ).getValueType();
		}

		return varType;
	}

	/**
	 * @see org.vedantatree.expressionoasis.extensions.VariableProvider#getVariableValue(java.lang.String)
	 */
	public ValueObject getVariableValue( String variableName ) throws ExpressionEngineException {
		ValueObject varValue = null;

		//if( supportsVariable( variableName ) ) {
		varValue = (ValueObject) variableValues.get( variableName );
		//}

		return varValue;
	}

	/**
	 * @see org.vedantatree.expressionoasis.extensions.VariableProvider#supportsVariable(java.lang.String)
	 */
	public boolean supportsVariable( String variableName ) throws ExpressionEngineException {
		return variableValues.containsKey( variableName );
	}
}