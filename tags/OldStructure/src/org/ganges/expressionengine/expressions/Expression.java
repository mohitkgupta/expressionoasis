/**
 * Created on Jan 3, 2006
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
package org.ganges.expressionengine.expressions;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


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
	 * @throws ExpressionEngineException if there is any problem during execution
	 */
	void initialize( ExpressionContext expressionContext, Object parameters ) throws ExpressionEngineException;

	/**
	 * This is used to un-initialize the expression, so that expression can be 
	 * reused.
	 * 
	 * @param expressionContext contextual information, may help in 
	 * 		  un-initialization
	 */
	void uninitialize( ExpressionContext expressionContext );
}