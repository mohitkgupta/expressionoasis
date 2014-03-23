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
package org.vedantatree.expressionoasis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.vedantatree.expressionoasis.config.ConfigFactory;
import org.vedantatree.expressionoasis.config.ExpressionOasisConfig;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.extensions.DefaultVariableProvider;
import org.vedantatree.expressionoasis.extensions.FunctionProvider;
import org.vedantatree.expressionoasis.extensions.VariableProvider;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


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
 *
 * Modified constructor to register function providers that are configured in 
 * XML file rather than hardcoded function providers, so that function providers
 * can be added and removed as required by a given application without needing
 * to recompile.
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class ExpressionContext {

	/**
	 * List container for variable providers. Variable providers are used to 
	 * provide the value of variables used in expression.
	 */
	private Map<String, VariableProvider> variableProviders = new HashMap<String, VariableProvider>();

	/**
	 * List container for function providers. Function providers are used to 
	 * call the functions given in expression.
	 */
	private List<FunctionProvider>		functionProviders = new ArrayList<FunctionProvider>();

	/**
	 * Map container for key <> properties. It contains the value of various 
	 * properties used in expression.
	 */
	private Map<Object, Object>		   properties		= new Hashtable<Object, Object>();

	/**
	 * Constructor
	 */
	public ExpressionContext() throws ExpressionEngineException {
		registerFunctionProviders();
		DefaultVariableProvider dvp = new DefaultVariableProvider();

		// for supporting the null operand with various operators, 
		// Expression Engine does not understand null as special keyword
		// Need to look for more appropriate method
		dvp.addVariable( "null", new ValueObject( null, Type.OBJECT ) );
		addVariableProvider( dvp );

	}

	/**
	 * Registers the function providers configured in the config XML file.
	 *
	 * @throws ExpressionEngineException if an error occurs when initializing function provider
	 */
	private void registerFunctionProviders() throws ExpressionEngineException {
		ExpressionOasisConfig config = ConfigFactory.getConfig();
		List<FunctionProvider> providers = config.getFunctionProviders();
		for( FunctionProvider provider : providers ) {
			addFunctionProvider( provider );
		}
	}

	public void addVariableProvider( VariableProvider variableProvider, String providerName ) {
		variableProviders.put( providerName, variableProvider );
	}

	/**
	 * Adds variable provider to the expression context.
	 * 
	 * @param variableProvider the variable provider to add
	 */
	public void addVariableProvider( VariableProvider variableProvider ) {
		variableProviders.put( String.valueOf( variableProvider.hashCode() ), variableProvider );
	}

	/**
	 * Adds function provider to the expression context.
	 * 
	 * @param functionProvider the function provider to add
	 */
	public void addFunctionProvider( FunctionProvider functionProvider ) throws ExpressionEngineException {
		functionProviders.add( functionProvider );
		functionProvider.initialize( this );
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
		return Collections.unmodifiableCollection( variableProviders.values() );
	}

	public VariableProvider getVariableProvider( String providerName ) {
		return variableProviders.get( providerName );
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

	/* Resets the variable provider so this expression context can be reused with
	 * a new set of variables.
	 */
	public void clearVariableProviders() {
		variableProviders.clear();
	}

	/**
	 * Resets the expression context. Cleans all the states of this expression
	 * context for further reuse.
	 */
	public void reset() {
		variableProviders.clear();
		functionProviders.clear();
		properties.clear();
	}
}