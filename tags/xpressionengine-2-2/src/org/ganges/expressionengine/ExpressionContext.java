/**
 * Created on Jan 2, 2006
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
package org.ganges.expressionengine;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ganges.expressionengine.extensions.DefaultFunctionProvider;
import org.ganges.expressionengine.extensions.FunctionProvider;
import org.ganges.expressionengine.extensions.MathFunctions;
import org.ganges.expressionengine.extensions.VariableProvider;


/**
 * Object of this class carry the contextual information for expressions during 
 * expression evaluation process. Like it can have information for variable 
 * providers, function providers and other properties.
 * 
 * This is a thread safe object, hence can be used with multiple expression 
 * evaluation process at the same time in multiple threads. Care needs to be 
 * taken for the properties if threads are changing them at the same time. It 
 * may results in unexpected result.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public class ExpressionContext {

	/**
	 * List container for variable providers. Variable providers are used to 
	 * provide the value of variables used in expression.
	 */
	private List<VariableProvider> variableProviders = new Vector<VariableProvider>();

	/**
	 * List container for function providers. Function providers are used to 
	 * call the functions given in expression.
	 */
	private List<FunctionProvider> functionProviders = new Vector<FunctionProvider>();

	/**
	 * Map container for key <> properties. It contains the value of various 
	 * properties used in expression.
	 */
	private Map<Object, Object>	properties		= new Hashtable<Object, Object>();

	/**
	 * Constructor
	 */
	public ExpressionContext() {
		addFunctionProvider( new DefaultFunctionProvider( MathFunctions.class ) );
	}

	/**
	 * Adds variable provider to the expression context.
	 * 
	 * @param variableProvider the variable provider to add
	 */
	public void addVariableProvider( VariableProvider variableProvider ) {
		variableProviders.add( variableProvider );
	}

	/**
	 * Adds function provider to the expression context.
	 * 
	 * @param functionProvider the function provider to add
	 */
	public void addFunctionProvider( FunctionProvider functionProvider ) {
		functionProviders.add( functionProvider );
	}

	/**
	 * Gets the collection of function providers. It is an unmodifiable list.
	 * 
	 * @return Returns the functionProviders.
	 */
	public Collection<FunctionProvider> getFunctionProviders() {
		return Collections.unmodifiableList( functionProviders );
	}

	/**
	 * Gets the collection of variable providers. It is an unmodifiable list.
	 * 
	 * @return Returns the variableProviders
	 */
	public Collection<VariableProvider> getVariableProviders() {
		return Collections.unmodifiableList( variableProviders );
	}

	/**
	 * Sets the value for any context property.
	 * 
	 * @param propertyName the name of property
	 * @param propertyValue the value of property
	 */
	public void setContextProperty( String propertyName, Object propertyValue ) {
		if( propertyName == null ) {
			throw new IllegalArgumentException( "Property name can't be null." );
		}

		if( propertyValue == null ) {
			properties.remove( propertyName );
		}
		else {
			properties.put( propertyName, propertyValue );
		}
	}

	/**
	 * Returns the value for a property.
	 * 
	 * @param propertyName the name of property
	 * @return the value corresponding to the property. <code>null</code> if
	 *         no property exists in expression context
	 */
	public Object getContextProperty( String propertyName ) {
		return properties.get( propertyName );
	}

	/**
	 * Resets the expression context. Cleans all the states of this expression
	 * context.
	 */
	public void reset() {
		variableProviders.clear();
		functionProviders.clear();
		properties.clear();
	}
}