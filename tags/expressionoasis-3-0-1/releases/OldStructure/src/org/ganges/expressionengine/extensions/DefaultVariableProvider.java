/**
 * Created on Jan 16, 2006
 *
 * Copyright (c) 2005 Ganges Organisation all rights reserved.
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU Lesser General Public License as
 * published by the Free Software Foundation. See the GNU Lesser General Public License for more details.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.ganges.expressionengine.extensions;

import java.util.Hashtable;
import java.util.Map;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;
import org.ganges.utils.StringUtils;


/**
 * This is the default implementation of the variable provider.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * 
 * @version 1.0
 */
public class DefaultVariableProvider implements VariableProvider {

	/**
	 * This the variable name and value mapping.
	 */
	private Map<String, ValueObject> variableValues = new Hashtable<String, ValueObject>();

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
	 * @see org.ganges.expressionengine.extensions.VariableProvider#getVariableType(java.lang.String)
	 */
	public Type getVariableType( String variableName ) throws ExpressionEngineException {
		Type varType = null;

		if( supportsVariable( variableName ) ) {
			varType = ( (ValueObject) variableValues.get( variableName ) ).getValueType();
		}

		return varType;
	}

	/**
	 * @see org.ganges.expressionengine.extensions.VariableProvider#getVariableValue(java.lang.String)
	 */
	public ValueObject getVariableValue( String variableName ) throws ExpressionEngineException {
		ValueObject varValue = null;

		if( supportsVariable( variableName ) ) {
			varValue = (ValueObject) variableValues.get( variableName );
		}

		return varValue;
	}

	/**
	 * @see org.ganges.expressionengine.extensions.VariableProvider#supportsVariable(java.lang.String)
	 */
	public boolean supportsVariable( String variableName ) throws ExpressionEngineException {
		return variableValues.containsKey( variableName );
	}
}