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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.types.MethodKey;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;
import org.ganges.utils.JavaUtils;


/**
 * This is the default function provider.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 */
public class DefaultFunctionProvider implements FunctionProvider {

	/**
	 * This is the class having required functions.
	 */
	private Class				  functionProviderClass;

	/**
	 * Object, implementing function provider interface, and providing the function definitions
	 */
	private Object				 functionProviderObject;

	/**
	 * This is the method map for this class.
	 */
	private Map<MethodKey, Method> methodMap = new Hashtable<MethodKey, Method>();

	private boolean				initialized;

	/**
	 * Constructor.
	 * 
	 * @param functionProviderClass
	 */
	public DefaultFunctionProvider( Class functionProviderClass ) throws ExpressionEngineException {
		if( functionProviderClass == null ) {
			throw new IllegalArgumentException( "Class of function provider can't be null." );
		}
		this.functionProviderClass = functionProviderClass;
	}

	/**
	 * @see org.ganges.expressionengine.extensions.FunctionProvider#getFunctionType(java.lang.String,
	 *      org.ganges.types.Type[])
	 */
	public Type getFunctionType( String functionName, Type[] parameterTypes ) throws ExpressionEngineException {

		if( !isInitialized() ) {
			throw new ExpressionEngineException( "Function provider is not initialized by now." );
		}
		Method method = getMethod( functionName, parameterTypes );
		Type type = null;

		if( method != null ) {
			type = Type.createType( method.getReturnType() );
		}

		return type;
	}

	/**
	 * @see org.ganges.expressionengine.extensions.FunctionProvider#getFunctionValue(java.lang.String,
	 *      org.ganges.types.ValueObject[])
	 */
	public ValueObject getFunctionValue( String functionName, ValueObject[] parameters )
			throws ExpressionEngineException {
		if( !isInitialized() ) {
			throw new ExpressionEngineException( "Function provider is not initialized by now." );
		}
		Method method = getMethod( functionName, parameters );
		ValueObject value = null;

		if( method != null ) {
			try {
				int length = parameters == null ? 0 : parameters.length;
				Object[] params = new Object[length];

				for( int i = 0; i < params.length; i++ ) {
					params[i] = parameters[i].getValue();
				}

				Object result = method.invoke( functionProviderObject, params );
				value = new ValueObject( result, Type.createType( method.getReturnType() ) );
			}
			catch( Exception ex ) {
				throw new ExpressionEngineException( "Error occured while executing method.", ex );
			}
		}

		return value;
	}

	/**
	 * @see org.ganges.expressionengine.extensions.FunctionProvider#supportsFunction(java.lang.String,
	 *      org.ganges.types.Type[])
	 */
	public boolean supportsFunction( String functionName, Type[] parameterTypes ) throws ExpressionEngineException {
		return getMethod( functionName, parameterTypes ) != null;
	}

	/**
	 * Initializes the method map.
	 *  
	 */
	public void initialize( ExpressionContext expressionContext ) throws ExpressionEngineException {
		if( expressionContext == null ) {
			throw new IllegalArgumentException( "Expression Context can't be null." );
		}

		try {
			Constructor constructor = functionProviderClass.getConstructor( ExpressionContext.class );
			if( constructor == null ) {
				throw new ExpressionEngineException(
						"Function provider class does not required constructor with ExpressionContext as parameter. FunctionProviderClass["
								+ functionProviderClass + "]" );
			}
			functionProviderObject = constructor.newInstance( expressionContext );
		}
		catch( ExpressionEngineException e ) {
			throw e;
		}
		catch( Exception e ) {
			throw new ExpressionEngineException( "Exception while creating Function Provider. FunctionProviderClass["
					+ functionProviderClass + "]", e );
		}

		Method[] methods = functionProviderClass.getMethods();

		for( int i = 0; i < methods.length; i++ ) {
			MethodKey key = createMethodKey( methods[i] );
			methodMap.put( key, methods[i] );
		}
		setInitialized( true );
	}

	/*
	 * It provides the key for given method.
	 * 
	 * @see Bug ID: 1691751
	 */
	private MethodKey createMethodKey( Method method ) {
		Class[] arguments = method.getParameterTypes();
		int length = arguments == null ? 0 : arguments.length;
		Type[] parameterTypes = new Type[length];
		for( int j = 0; j < length; j++ ) {
			parameterTypes[j] = Type.createType( JavaUtils.convertToPrimitive( arguments[j] ) );
		}
		MethodKey key = new MethodKey( method.getName(), parameterTypes );
		return key;
	}	

	/**
	 * Returns the method for given arguments.
	 * 
	 * @param functionName name of the function
	 * @param parameterTypes parameter types
	 * @return
	 */
	private Method getMethod( String functionName, Type[] parameterTypes ) {
		MethodKey thatKey = new MethodKey( functionName, parameterTypes );
		Method method = (Method) methodMap.get( thatKey );
		if( method == null ) {
			Set entrySet = methodMap.entrySet();
			for( Iterator iter = entrySet.iterator(); iter.hasNext(); ) {
				Map.Entry entry = (Map.Entry) iter.next();
				MethodKey key = (MethodKey) entry.getKey();
				if( thatKey.isAssignaleFrom( key ) ) {
					method = (Method) entry.getValue();
					break;
				}
			}
		}

		return method;
	}

	/**
	 * Returns the method for given arguments.
	 * 
	 * @param functionName name of the function
	 * @param parameters parameters
	 * @return
	 */
	private Method getMethod( String functionName, ValueObject[] parameters ) {
		int length = parameters == null ? 0 : parameters.length;
		Type[] parameteTypes = new Type[length];
		for( int i = 0; i < length; i++ ) {
			parameteTypes[i] = parameters[i].getValueType();
		}
		return getMethod( functionName, parameteTypes );
	}

	private boolean isInitialized() {
		return initialized;
	}

	private void setInitialized( boolean initialized ) {
		this.initialized = initialized;
	}

}