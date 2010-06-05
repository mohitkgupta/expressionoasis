/**
 * Created on Jan 16, 2006
 *
 * Copyright (c) 2005 Ganges Organisation all rights reserved.
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation. See the GNU General Public License for more details.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.ganges.expressionengine.extensions;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This class represents the interface for variable providers for Expression 
 * Engine. Object of this type will be resolving the variable for any 
 * expression. It can support more than one variables.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public interface VariableProvider {

	/**
	 * It initialize the Function provider with expression context and also
	 * gives a chance to pre-initialize any internal states for operations
	 * 
	 * @param expressionContext Context of current expression evaluation process
	 */
	void initialize (ExpressionContext expressionContext) throws ExpressionEngineException;
	
	/**
	 * Gets the type of variable.
	 * 
	 * @param variableName name of the variable
	 * @return type of the variable
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	Type getVariableType( String variableName ) throws ExpressionEngineException;

	/**
	 * Gets the value of variable.
	 * 
	 * @param variableName name of the variable.
	 * @return value of variable wrapped in <code>ValueObject</code>
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	ValueObject getVariableValue( String variableName ) throws ExpressionEngineException;

	/**
	 * Checks whether the specified variable is supported by this variable 
	 * provider or not.
	 * 
	 * @param variableName name of the variable
	 * @return <code>true</code> if the variable is supported by this provider
	 *         <code>false</code> otherwise
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	boolean supportsVariable( String variableName ) throws ExpressionEngineException;
}