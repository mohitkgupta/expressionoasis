/**	
 *  Copyright (c) 2005-2014 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software. You can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL 
 *  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES 
 *  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE 
 *  OR OTHER DEALINGS IN THE SOFTWARE.See the GNU Lesser General Public License 
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis. If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Please consider to contribute any enhancements to upstream codebase. 
 *  It will help the community in getting improved code and features, and 
 *  may help you to get the later releases with your changes.
 */
package org.vedantatree.expressionoasis.extensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.ExpressionEngine;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.ExpressionFactory;
import org.vedantatree.expressionoasis.grammar.Grammar;
import org.vedantatree.expressionoasis.types.MethodKey;
import org.vedantatree.expressionoasis.types.Type;
import org.vedantatree.expressionoasis.types.ValueObject;


/**
 * This is the default function provider.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 * 
 *          Modified so that the constructor registers the methods of the functionProviderClass
 *          as functions with the grammar and expression factory.
 * 
 *          Modified so that the functionProviderObject is created only on the first invocation
 *          of initialize, rather than every time a new ExpressionContext is created.
 * 
 *          Modified so that method keys use non-primitive type names.
 * 
 * @author Kris Marwood
 * @version 1.1
 */
public class DefaultFunctionProvider implements FunctionProvider
{

	private static Log				LOGGER		= LogFactory.getLog( DefaultFunctionProvider.class );

	/**
	 * This is the class having required functions.
	 */
	private Class					functionProviderClass;

	/**
	 * Object, implementing function provider interface, and providing the function definitions
	 */
	private Object					functionProviderObject;

	/**
	 * This is the method map for this class.
	 */
	private Map<MethodKey, Method>	methodMap	= new HashMap<MethodKey, Method>();

	private boolean					initialized;

	/**
	 * Constructor.
	 * 
	 * @param functionProviderClass
	 */
	public DefaultFunctionProvider( Class functionProviderClass ) throws ExpressionEngineException
	{
		if( functionProviderClass == null )
		{
			throw new IllegalArgumentException( "Class of function provider can't be null." );
		}
		this.functionProviderClass = functionProviderClass;

		if( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "Creating function provider for class " + functionProviderClass.getName() );
		}

		registerFunctions();
	}

	/**
	 * Registers the methods of the functionProviderClass with the grammar and expression factory.
	 */
	private void registerFunctions()
	{
		ExpressionFactory factory = ExpressionFactory.getInstance();
		Grammar grammar = ExpressionEngine.getGrammar();

		Method[] methods = functionProviderClass.getDeclaredMethods();
		for( int i = 0; i < methods.length; i++ )
		{
			String functionName = methods[i].getName();
			factory.addFunction( functionName );
			grammar.addFunction( functionName );

			if( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "Registered function " + functionName );
			}
		}
	}

	/**
	 * @see org.vedantatree.expressionoasis.extensions.FunctionProvider#getFunctionType(java.lang.String,
	 *      org.vedantatree.types.Type[])
	 */
	public Type getFunctionType( String functionName, Type[] parameterTypes ) throws ExpressionEngineException
	{

		if( !isInitialized() )
		{
			throw new ExpressionEngineException( "Function provider is not initialized by now." );
		}
		Method method = getMethod( functionName, parameterTypes );
		Type type = null;

		if( method != null )
		{
			type = Type.createType( method.getReturnType() );
		}

		return type;
	}

	/**
	 * @see org.vedantatree.expressionoasis.extensions.FunctionProvider#getFunctionValue(java.lang.String,
	 *      org.vedantatree.types.ValueObject[])
	 */
	public ValueObject getFunctionValue( String functionName, ValueObject[] parameters )
			throws ExpressionEngineException
	{
		if( !isInitialized() )
		{
			throw new ExpressionEngineException( "Function provider is not initialized by now." );
		}
		Method method = getMethod( functionName, parameters );
		ValueObject value = null;

		if( method != null )
		{
			try
			{
				int length = parameters == null ? 0 : parameters.length;
				Object[] params = new Object[length];

				for( int i = 0; i < params.length; i++ )
				{
					params[i] = parameters[i].getValue();
				}

				Object result = method.invoke( functionProviderObject, params );
				value = new ValueObject( result, Type.createType( method.getReturnType() ) );
			}
			catch( Exception ex )
			{
				throw new ExpressionEngineException( "Error occured while executing method.", ex );
			}
		}

		return value;
	}

	/**
	 * @see org.vedantatree.expressionoasis.extensions.FunctionProvider#supportsFunction(java.lang.String,
	 *      org.vedantatree.types.Type[])
	 */
	public boolean supportsFunction( String functionName, Type[] parameterTypes ) throws ExpressionEngineException
	{
		return getMethod( functionName, parameterTypes ) != null;
	}

	/**
	 * Initializes the method map.
	 * 
	 */
	public void initialize( ExpressionContext expressionContext ) throws ExpressionEngineException
	{
		if( expressionContext == null )
		{
			throw new IllegalArgumentException( "Expression Context can't be null." );
		}

		/*
		 * all function providers are now created once and cached for reuse rather than every time
		 * an expression context is created.
		 */
		if( functionProviderObject == null )
		{
			try
			{
				Constructor constructor = functionProviderClass.getConstructor( ExpressionContext.class );
				if( constructor == null )
				{
					throw new ExpressionEngineException(
							"Function provider class does not required constructor with ExpressionContext as parameter. FunctionProviderClass["
									+ functionProviderClass + "]" );
				}
				functionProviderObject = constructor.newInstance( expressionContext );
			}
			catch( ExpressionEngineException e )
			{
				throw e;
			}
			catch( Exception e )
			{
				throw new ExpressionEngineException(
						"Exception while creating Function Provider. FunctionProviderClass[" + functionProviderClass
								+ "]", e );
			}

			Method[] methods = functionProviderClass.getMethods();

			for( int i = 0; i < methods.length; i++ )
			{
				MethodKey key = createMethodKey( methods[i] );
				methodMap.put( key, methods[i] );
			}
		}
		setInitialized( true );
	}

	/*
	 * It provides the key for given method.
	 * 
	 * @see Bug ID: 1691751
	 */
	private MethodKey createMethodKey( Method method )
	{
		Class[] arguments = method.getParameterTypes();
		int length = arguments == null ? 0 : arguments.length;
		Type[] parameterTypes = new Type[length];
		for( int j = 0; j < length; j++ )
		{
			try
			{
				parameterTypes[j] = Type.createType( arguments[j] );
			}
			catch( Exception e )
			{
				throw new RuntimeException( "Error creating method key: " + e.getMessage(), e );
			}
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
	private Method getMethod( String functionName, Type[] parameterTypes )
	{
		MethodKey thatKey = new MethodKey( functionName, parameterTypes );
		Method method = (Method) methodMap.get( thatKey );
		if( method == null )
		{
			Set entrySet = methodMap.entrySet();
			for( Iterator iter = entrySet.iterator(); iter.hasNext(); )
			{
				Map.Entry entry = (Map.Entry) iter.next();
				MethodKey key = (MethodKey) entry.getKey();
				if( thatKey.isAssignaleFrom( key ) )
				{
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
	private Method getMethod( String functionName, ValueObject[] parameters )
	{
		int length = parameters == null ? 0 : parameters.length;
		Type[] parameteTypes = new Type[length];
		for( int i = 0; i < length; i++ )
		{
			parameteTypes[i] = parameters[i].getValueType();
		}
		return getMethod( functionName, parameteTypes );
	}

	private boolean isInitialized()
	{
		return initialized;
	}

	private void setInitialized( boolean initialized )
	{
		this.initialized = initialized;
	}

}