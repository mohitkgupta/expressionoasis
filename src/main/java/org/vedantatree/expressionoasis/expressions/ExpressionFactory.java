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
package org.vedantatree.expressionoasis.expressions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
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
 *          Performance improvements:
 *          - Cache expression classes so they only have to be loaded once via reflection
 *          - Compile and cache the operand regular expressions so they can be reused
 * 
 *          Config enhancements:
 *          - expression configurations are retrieved from main config file, which is read via Simple XML framework
 *          rather than
 *          low-level XML APIs.
 *          - added addFunction method for function providers to register their function expressions.
 * 
 * @author Kris Marwood
 * @version 1.1
 * 
 *          Merged expression class cache in existing type <> expression cache
 *          Deprecated the addFunction method, as we are returning FunctionExpression statically for all functions
 *          Improved the logic for Operand type expression search while creating the Expression
 * 
 * @author Mohit Gupta
 * @version 1.2
 * @since 3.1
 */
public class ExpressionFactory
{

	/**
	 * This is the static singleton SHARED_INSTANCE of expression factory.
	 */
	private static ExpressionFactory		SHARED_INSTANCE				= new ExpressionFactory();

	/**
	 * This is the unary expression type
	 */
	public static final String				UNARY						= "unary";

	/**
	 * This is the binary expression type.
	 */
	public static final String				BINARY						= "binary";

	/**
	 * This is the function expression type.
	 */
	public static final String				FUNCTION					= "function";

	/**
	 * This is the operand expression type.
	 */
	public static final String				OPERAND						= "operand";

	/**
	 * This is the true constant for absolute.
	 */
	public static final String				TRUE						= "true";

	/**
	 * Cache for Expression Type <> (Expression Token <> Expression Class)
	 * 
	 * This is loaded from config.xml initially and is used while searching for right Expression class to create the
	 * Expression object
	 */
	private Map<String, Map<String, Class>>	expressionTypeClassMapping	= new HashMap<String, Map<String, Class>>();

	/**
	 * Cache for Operand type expressions token <> corresponding compiled pattern
	 * 
	 * It is maintained to avoid compiling of regular expression again and again. If we are getting any Operand type
	 * of Expression for creating a Expression Object, we shall try to match the given expression token with stored
	 * Patterns. If any pattern match, pick the class for its token i.e. key in the map, from expressionTypeClassMapping
	 * 
	 * The "name" of an operand expression is a regex. This is a cache of the compiled operand regex's.
	 */
	private Map<String, Pattern>			operandRegExCache			= new HashMap<String, Pattern>();

	/**
	 * Constructs the ExpressionFactory
	 */
	private ExpressionFactory()
	{
		configure();
	}

	/**
	 * Gets the shared instance of Expression Factory
	 * 
	 * @return Returns the shared instance
	 */
	public static ExpressionFactory getInstance()
	{
		return SHARED_INSTANCE;
	}

	/**
	 * Adds a function expression for the specified function name
	 * 
	 * @param name name of the function to add
	 * @deprecated No need to add function in ExpressionFactory, as it will always return FunctionExpression for all
	 *             functions
	 */
	public void addFunction( String functionName )
	{
		// Map<String, Class> map = expressionTypeClassMapping.get( FUNCTION );
		// map.put( functionName, FunctionExpression.class );
	}

	/**
	 * Configures the expressions defined in config.xml
	 */
	private void configure()
	{
		expressionTypeClassMapping.put( UNARY, new HashMap() );
		expressionTypeClassMapping.put( BINARY, new HashMap() );
		expressionTypeClassMapping.put( OPERAND, new HashMap() );

		List<ExpressionConfig> expressions = ConfigFactory.getConfig().getExpressionConfigs();

		for( ExpressionConfig expression : expressions )
		{
			String expressionType = expression.getExpressionType();
			String expressionToken = expression.getExpressionName();
			Class expressionClass = expression.getExpressionClass();

			// add expression class to the cache to reuse while creating Expression Object
			// expressionClassCache.put( expressionClass.getName(), expressionClass );

			/*
			 * Store the name of the expression class, against expression token (or called name also)
			 * There can be more than one expression classes for one Expression Type,
			 * like for Binary Expression Type - minus expression, plus expression etc
			 */
			Map<String, Class> expressionClassMap = expressionTypeClassMapping.get( expressionType );
			expressionClassMap.put( expressionToken, expressionClass );

			/*
			 * If expression type is Operand, let us compile its pattern and store it in cache against expressionToken.
			 * 
			 * Later, while creating the Expression, if specific token is of Operand type
			 * We shall try to match each pattern with given token and will find the right Expression class
			 */
			if( expressionType.equals( OPERAND ) )
			{
				Pattern pattern = Pattern.compile( expressionToken );
				operandRegExCache.put( expressionToken, pattern );
			}
		}

		// No need for Function mapping, as there is only one Expression for function type as of now
		// expressionTypeClassMapping.put( FUNCTION, new HashMap() );

		// store FunctionExpression class specifically for expressions of Function type
		// expressionClassCache.put( FunctionExpression.class.getName(), FunctionExpression.class );

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
	public Expression createExpression( String expressionToken, String type ) throws ExpressionEngineException
	{
		Class expressionClass = null;
		Map<String, Class> expressionTokenClassMap = expressionTypeClassMapping.get( type );

		/*
		 * If expression
		 */
		if( FUNCTION.equals( type ) )
		{
			expressionClass = FunctionExpression.class;
		}
		else if( OPERAND.equals( type ) )
		{
			for( Iterator<Map.Entry<String, Pattern>> iterator = operandRegExCache.entrySet().iterator(); iterator
					.hasNext(); )
			{
				Map.Entry<String, Pattern> operandTokenRegEx = iterator.next();
				Matcher operandMatcher = operandTokenRegEx.getValue().matcher( expressionToken );
				if( operandMatcher.matches() )
				{
					expressionClass = expressionTokenClassMap.get( operandTokenRegEx.getKey() );
					break;
				}
			}
			Assert.assertNotNull(
					"If expression token is of Operand type, corresponding regular expression must be defined in "
							+ "config.xml. No regular expression, hence no expression class found for ["
							+ expressionToken + "]", expressionClass );

		}
		else
		{
			// get the expression class mapping for give Expression type
			expressionClass = ( expressionTokenClassMap == null ? null : expressionTokenClassMap.get( expressionToken ) );

		}

		if( expressionClass == null )
		{
			throw new ExpressionEngineException( "Unable to find any expression class mapping for token \""
					+ expressionToken + "\" in type \"" + type + "\"" );
		}

		try
		{
			return (Expression) expressionClass.newInstance();
		}
		catch( Exception ex )
		{
			throw new ExpressionEngineException( "Unable to create the expression for token[" + expressionToken
					+ "] in type[" + type + "]" );
		}
	}
}

/**
 * A cache for the classes of different expression types.
 * fully qualified class name of expression <> Loaded Class
 * 
 * This cache is maintained to minimize the reflection calls for initializing the expression's classes. Expression
 * Factory get resolve the expression type based on specific token value and expression type, and then search this
 * cache for already loaded expression class. If it is not already loaded, class will be loaded using reflection
 * and will be stored in this cache.
 * 
 * TODO: Should be converted to Memory Sensitive Cache
 */
// private Map<String, Class> expressionClassCache = new HashMap<String, Class>();

// for( Iterator iter = expressionTokenClassMap.keySet().iterator(); iter.hasNext(); )
// {
// String operandPattern = (String) iter.next();
//
// Pattern pattern = operandRegExCache.get( operandPattern );
// Matcher matcher = pattern.matcher( expressionToken );
// if( matcher.matches() )
// {
// expressionClass = expressionTokenClassMap.get( operandPattern );
// break;
// }
// }
