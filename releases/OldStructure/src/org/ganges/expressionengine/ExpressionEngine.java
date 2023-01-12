/**
 * Created on Jan 2, 2006
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
package org.ganges.expressionengine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.Expression;
import org.ganges.types.ValueObject;

/**
 * It is a Utility class for XpressionEngine Framework. It provides utility
 * methods for accessing various functionalities of the Framework.
 * 
 * A few points to consider -If any of the sub-expression is returning
 * Type.ANY_TYPE, i.e. type will be decided at runtime. -If any of the
 * sub-expression is returning Type.ANY_TYPE, the whole expression will return
 * Type.ANY_TYPE -If expression contains XML expression, XML document 'URL'
 * should be set as property to Context before executing the expression -In case
 * of XML expression, it will always return the String value as it does not have
 * any way to identify the required type of the value - To override all above,
 * currently we are getting only string type value from XML expressions
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public final class ExpressionEngine {

	private static Log LOGGER = LogFactory.getLog(ExpressionEngine.class);

	/**
	 * Compiler instance to compile the expressions
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
	 * @param expression        the expression to evaluate
	 * @param expressionContext the object contains the contextual information,
	 *                          which may be required for expression evaluation. It
	 *                          may be like an Java Bean in case of property
	 *                          expression
	 * @return the result of expression
	 * @throws ExpressionEngineException if unable to parse the expression
	 */
	public static Object evaluate(String expression, ExpressionContext expressionContext)
			throws ExpressionEngineException {

		Expression compiledExpression = compiler.compile(expression, expressionContext);
		ValueObject expressionValue = compiledExpression.getValue();

		LOGGER.debug("expressionValue[" + (expressionValue == null ? null : expressionValue.getValue()) + "]");

		return expressionValue.getValue();
	}

	/**
	 * Compiles the expression string and prepares the expression tree with relevant
	 * Expression's objects.
	 * 
	 * @param expression        the expression to compile
	 * @param expressionContext the object contains the contextual information,
	 *                          which may be required for expression evaluation. It
	 *                          may be like an Java Bean in case of property
	 *                          expression
	 * @return compiled Expression object. It is actually a tree of Expression
	 *         Objects
	 * @throws ExpressionEngineException if unable to compile the expression
	 */
	public static Expression compileExpression(String expression, ExpressionContext expressionContext)
			throws ExpressionEngineException {

		return compiler.compile(expression, expressionContext);
	}

	public static void main(String[] args) throws ExpressionEngineException {
		// String expression = "(false && !true) && !(false && !true)";
		// String expression = "-3 * -2 + 10 - -2/-2";
		// String expression = "20 - (10/-2 + (-5 * -2)) / (15 * (-5/5) )";
		String expression = "(1/1!=1) ? 7 : 5";

		Object result = ExpressionEngine.evaluate(expression, new ExpressionContext());

		System.out.println("Result[" + result + "]");

		// String expression = "true ? 2 : 5";
		// System.out.println( "complement" + ( 1 & number ) );
	}
}