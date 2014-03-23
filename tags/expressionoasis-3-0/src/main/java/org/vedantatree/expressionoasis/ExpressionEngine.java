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

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.Expression;
import org.vedantatree.expressionoasis.expressions.ExpressionTypeFinder;
import org.vedantatree.expressionoasis.expressions.IdentifierExpression;
import org.vedantatree.types.ValueObject;


/**
 * It is a Utility class for XpressionEngine Framework. It provides 
 * utility methods for accessing various functionalities of the Framework.
 * 
 * A few points to consider
 * -If any of the sub-expression is returning Type.ANY_TYPE, i.e. type will be
 * decided at runtime.
 * -If any of the sub-expression is returning Type.ANY_TYPE, the whole expression
 * will return Type.ANY_TYPE
 * -If expression contains XML expression, XML document 'URL' should be set
 * as property to Context before executing the expression
 * -In case of XML expression, it will always return the String value as it 
 * does not have any way to identify the required type of the value
 * - To override all above, currently we are getting only string type value 
 * from XML expressions
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 *
 * Added the ability to extract the variable names from an expression.
 *
 * @author Kris Marwood
 * @version 1.1
 */
public final class ExpressionEngine {

	private static Log			LOGGER   = LogFactory.getLog( ExpressionEngine.class );

	/**
	 * Compiler instance used to compile the expressions
	 */
	private static final Compiler compiler = new Compiler();

	/**
	 * Constructor made private to restrict object construction
	 */
	private ExpressionEngine() {
		/*
		 * Nothing to do here.
		 */
	}

	/**
	 * Evaluates the expression and returns the result
	 * 
	 * @param expression the expression to evaluate
	 * @param expressionContext the object contains the contextual information,
	 * 		  which may be required for expression evaluation. It may be like 
	 * 		  an Java Bean in case of property expression 
	 * @return the result of expression
	 * @throws ExpressionEngineException if unable to parse the expression
	 */
	public static Object evaluate( String expression, ExpressionContext expressionContext )
			throws ExpressionEngineException {
		Expression compiledExpression = compileExpression( expression, expressionContext, true );
		ValueObject expressionValue = compiledExpression.getValue();

		if( LOGGER.isDebugEnabled() ) {
			LOGGER.debug( "expressionValue[" + expressionValue.getValue() + "]" );
		}
		return expressionValue.getValue();
	}

	/**
	 * Compiles the expression string and prepares the expression tree with 
	 * relevant Expression's objects.
	 * 
	 * @param expression the expression to compile
	 * @param expressionContext the object contains the contextual information,
	 * 		  which may be required for expression evaluation. It may be like 
	 * 		  an Java Bean in case of property expression
	 * @param validate true if the operands should be validated
	 * @return compiled Expression object. It is actually a tree of Expression 
	 * 		   Objects
	 * @throws ExpressionEngineException if unable to compile the expression
	 */
	public static Expression compileExpression( String expression, ExpressionContext expressionContext, boolean validate )
			throws ExpressionEngineException {
		return compiler.compile( expression, expressionContext, validate );
	}

	/**
	 * Retrieves a set of variable names contained within the specified expression string
	 *
	 * @param expression the expression to extract the variable names for
	 * @return a set of variable names contained within the specified expression string
	 * @throws ExpressionEngineException
	 */
	public static Set<String> getVariableNames( String expression ) throws ExpressionEngineException {
		// it may matter to a user of this code what order the variable names are in, hence LinkedHashSet
		LinkedHashSet<String> variableNames = new LinkedHashSet<String>();

		Expression exp = compileExpression( expression, new ExpressionContext(), false );
		ExpressionTypeFinder finder = new ExpressionTypeFinder( exp, IdentifierExpression.class );
		Set<Expression> foundVariables = finder.getExpressions();

		for( Expression variable : foundVariables ) {
			String variableName = ( (IdentifierExpression) variable ).getIdentifierName();
			variableNames.add( ( variableName ) );
		}

		return variableNames;
	}

	public static void main( String[] args ) throws ExpressionEngineException {
		//		String expression = "(false && !true) && !(false && !true)";
		//		String expression = "-3 * -2 + 10 - -2/-2";
		//		String expression = "20 - (10/-2 + (-5 * -2)) / (15 * (-5/5) )";
		//		String expression = "(1/1!=1) ? 7 : 5";
		//		String expression = "true == true";
		//
		//
		//		String expression = "iif(1!=1, 'asdf', 'jkl')";
		//		Object result  = ExpressionEngine.evaluate( expression, new ExpressionContext() );
		//		System.out.println("Result[" + result + "]");
		//
		//		expression = "iif(true, 5, 66)";
		//		result  = ExpressionEngine.evaluate( expression, new ExpressionContext() );
		//		System.out.println("Result[" + result + "]");

		//String expression = "iif(false,5,null)";
		//String expression = "isnull(iif(false, 5, null))";
		//String expression = "isnull('asdf')";
		//String expression = "'asdf' + 5 + 'jkl'";

		//String expression = "iif(isnull(null),'null', 'not null')";

		//String expression = "5 * 6";

		//String expression = "abs(5) + 3";

		String expression = "null != 1.0";

		Object result = ExpressionEngine.evaluate( expression, new ExpressionContext() );
		result = ExpressionEngine.evaluate( expression, new ExpressionContext() );

		System.out.println( "Result[" + result + "]" );

		//		String expression = "true ? 2 : 5";
		//		System.out.println( "complement" + ( 1 & number ) );
	}
}