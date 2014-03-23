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
package org.vedantatree.expressionoasis.expressions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vedantatree.expressionoasis.config.ConfigFactory;
import org.vedantatree.expressionoasis.config.ExpressionConfig;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.property.FunctionExpression;


/**
 * This is the expression factory which creates the expression for given 
 * expression token.
 * 
 * It picks the mapping of tokens and corresponding expression classes from a 
 * XML file.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 *
 * Performance improvements:
 *	- Cache expression classes so they only have to be loaded once via reflection
 *	- Compile and cache the operand regular expressions so they can be reused
 *
 * Config enhancements:
 *	- expression configurations are retrieved from main config file, which is read
 *        via Simple XML framework rather than low-level XML APIs.
 *	- added addFunction method for function providers to register their function
 *	  expressions.
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class ExpressionFactory {

	/**
	 * This is the static singleton SHARED_INSTANCE of expression factory.
	 */
	private static ExpressionFactory   SHARED_INSTANCE	  = new ExpressionFactory();

	/**
	 * This is the unary expression type
	 */
	public static final String		 UNARY				= "unary";

	/**
	 * This is the binary expression type.
	 */
	public static final String		 BINARY			   = "binary";

	/**
	 * This is the function expression type.
	 */
	public static final String		 FUNCTION			 = "function";

	/**
	 * This is the operand expression type.
	 */
	public static final String		 OPERAND			  = "operand";

	/**
	 * This is the true constant for absolute.
	 */
	public static final String		 TRUE				 = "true";

	/**
	 * This is the key class mapping.
	 */
	private Map<String, HashMap<?, ?>> typeClassMapping	 = new HashMap<String, HashMap<?, ?>>();

	/**
	 * The "name" of an operand expression is a regex. This is a cache of the compiled operand regex's.
	 */
	private Map<String, Pattern>	   operandRegexCache	= new HashMap<String, Pattern>();

	/**
	 * A cache of the classes for different expression types. Used to minimise reflection calls.
	 */
	private Map<String, Class>		 expressionClassCache = new HashMap<String, Class>();

	/**
	 * Constructs the ExpressionFactory
	 */
	private ExpressionFactory() {
		configure();
	}

	/**
	 * Adds a function expression for the specified funcion name
	 * 
	 * @param name name of the function to add
	 */
	public void addFunction( String functionName ) {
		HashMap map = (HashMap) typeClassMapping.get( FUNCTION );
		map.put( functionName, FunctionExpression.class.getName() );
	}

	/**
	 * Configures the expressions defined in config.xml
	 */
	private void configure() {
		typeClassMapping.put( UNARY, new HashMap() );
		typeClassMapping.put( BINARY, new HashMap() );
		typeClassMapping.put( OPERAND, new HashMap() );
		typeClassMapping.put( FUNCTION, new HashMap() );

		List<ExpressionConfig> expressions = ConfigFactory.getConfig().getExpressionConfigs();

		for( ExpressionConfig expression : expressions ) {
			String type = expression.getExpressionType();
			String token = expression.getExpressionName();
			Class clazz = expression.getExpressionClass();

			HashMap map = (HashMap) typeClassMapping.get( type );
			map.put( token, clazz.getName() );

			expressionClassCache.put( clazz.getName(), clazz );

			if( type.equals( OPERAND ) ) {
				Pattern pattern = Pattern.compile( token );
				operandRegexCache.put( token, pattern );
			}
		}

		FunctionExpression functionExpression = new FunctionExpression();
		Class functionExpressionClass = functionExpression.getClass();
		expressionClassCache.put( functionExpressionClass.getName(), functionExpressionClass );
	}

	/**
	 * Gets the shared instance of Expression Factory
	 * 
	 * @return Returns the shared instance
	 */
	public static ExpressionFactory getInstance() {
		return SHARED_INSTANCE;
	}

	/**
	 * Creates the expression object for given expression token and expression 
	 * type
	 * 
	 * @param expressionToken the token of expression
	 * @param type the type of expression i.e. operand, operator etc
	 * @return the expression object
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	public Expression createExpression( String expressionToken, String type ) throws ExpressionEngineException {
		HashMap mapping = (HashMap) typeClassMapping.get( type );
		String className = (String) ( mapping == null ? null : mapping.get( expressionToken ) );

		// Handling for operand type expressions
		// because operand expression matches with regular expression, hence can 
		// not be searched using map searching
		if( OPERAND == type ) {
			for( Iterator iter = mapping.keySet().iterator(); iter.hasNext(); ) {
				String operandPattern = (String) iter.next();

				Pattern pattern = operandRegexCache.get( operandPattern );
				Matcher matcher = pattern.matcher( expressionToken );
				if( matcher.matches() ) {
					className = (String) mapping.get( operandPattern );
					break;
				}
			}
		}

		if( className == null ) {
			throw new ExpressionEngineException( "Unable to find any expression class mapping for token \""
					+ expressionToken + "\" in type \"" + type + "\"" );
		}

		try {
			// earlier we were returning null, if class name is null
			// however remove that case now, as unable to recall any case for that
			Class clazz = expressionClassCache.get( className );
			return (Expression) clazz.newInstance();
		}
		catch( Exception ex ) {
			throw new ExpressionEngineException( "Unable to create the expression for token \"" + expressionToken
					+ "\" in type \"" + type + "\"" );
		}
	}
}