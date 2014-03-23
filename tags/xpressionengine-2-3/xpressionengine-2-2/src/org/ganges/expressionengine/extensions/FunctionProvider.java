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

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This class represents the interface for function providers for Expression 
 * Engine. Object of this type will be resolving the functions for any 
 * expression. It can support more than one functions.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public interface FunctionProvider {

	/**
	 * Gets the return type of the function.
	 * 
	 * @param functionName the name of the function.
	 * @param parameterTypes the types of the parameters
	 * @return the return type of the function
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	Type getFunctionType( String functionName, Type[] parameterTypes ) throws ExpressionEngineException;

	/**
	 * Gets the return value of function.
	 * 
	 * @param functionName the name of the function
	 * @param parameters the parameters values of the function
	 * @return the return value of the function
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	ValueObject getFunctionValue( String functionName, ValueObject[] parameters ) throws ExpressionEngineException;

	/**
	 * Checks whether the this function provider supports any function or not.
	 * 
	 * @param functionName the name of the function
	 * @param parameterTypes the type of parameters for the function
	 * @return <code>true</code> if function provider supports the function
	 * 		   <code>false</code> otherwise 
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	boolean supportsFunction( String functionName, Type[] parameterTypes ) throws ExpressionEngineException;
}