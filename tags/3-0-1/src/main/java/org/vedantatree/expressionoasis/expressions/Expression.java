/**
 * Copyright (c) 2006 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.expressions;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * This class represents the executable structure of an expression. 
 * 
 * Parser parse the expression and break it into individual expression tokens. 
 * This token list is feed to compiler for creating the executable structure of 
 * expressions using objects of this class. Compiler create relevant expression 
 * object (of this class) for every token and creates a relative expression 
 * chain as operands or operators. Final outcome of Compiler is a single top 
 * level expression which contains all expression in hierarchy.
 * 
 * So the final expression can be an atomic Expression class or an Expression 
 * having a large tree of children expressions with it.
 * 
 * TODO
 * 	improve tostring to represent actual expression
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 *
 * Modified to support visitor design pattern.
 *
 * @author Kris Marwood
 * @version 1.1
 */
public interface Expression {

	/**
	 * Executes and returns the value of this expression. 
	 * 
	 * If expression is not at node level, it will further evaluate its related
	 * expression which may represents operands or operators and return the 
	 * final outcome.
	 * 
	 * @throws ExpressionEngineException if there is any problem during execution
	 * @return the value of expression (after expression evaluation)
	 */
	ValueObject getValue() throws ExpressionEngineException;

	/**
	 * Gets the return type of the expression.
	 * 
	 * @throws ExpressionEngineException If there is any problem during execution
	 * @return the type of expression
	 */
	Type getReturnType() throws ExpressionEngineException;

	/**
	 * This is used to initialize the expression.
	 * 
	 * @param expressionContext contextual information, may help in 
	 * 		  initialization
	 * @param parameters for example, sub expressions and  identifiers
	 * @validate whether or not to validate the type of the expression. It can be
	 *                useful to not validate expressions when compiling an expression
	 *                for using with an ExpressionTypeFinder.
	 * @throws ExpressionEngineException if there is any problem during execution
	 */
	void initialize( ExpressionContext expressionContext, Object parameters, boolean validate )
			throws ExpressionEngineException;

	/**
	 * This is used to un-initialize the expression, so that expression can be 
	 * reused.
	 * 
	 * @param expressionContext contextual information, may help in 
	 * 		  un-initialization
	 */
	void uninitialize( ExpressionContext expressionContext );

	/**
	* Implements visitor pattern.
	*
	* @param visitor a visitor object that this expression must visit when accept is called.
	*/
	void accept( ExpressionVisitor visitor );
}