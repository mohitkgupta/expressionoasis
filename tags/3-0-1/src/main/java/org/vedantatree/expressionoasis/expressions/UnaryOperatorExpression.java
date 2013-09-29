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

import java.util.HashMap;
import java.util.Map;

import org.vedantatree.expressionoasis.EOErrorCodes;
import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.types.Type;
import org.vedantatree.utils.StringUtils;


/**
 * This class defines the abstract implementation of unary operator expression.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * 
 * @version 1.0
 *
 * Modified to support visitor design pattern.
 *
 * @author Kris Marwood
 * @version 1.1
 */
public abstract class UnaryOperatorExpression implements Expression {

	/**
	 * This is the type pair mapping for all the unary operators.
	 */
	private static Map typePairMapping = new HashMap();

	/**
	 * This is the operand expression for this unary operator expression. There 
	 * will be only one operand with unary operator expression
	 */
	private Expression operandExpression;

	/**
	 * Initializes the operand expression.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#initialize(org.vedantatree.expressionoasis.ExpressionContext,
	 *      java.lang.Object)
	 */
	public void initialize( ExpressionContext expressionContext, Object parameters, boolean validate )
			throws ExpressionEngineException {
		this.operandExpression = (Expression) parameters;
		/*
		 * Earlier operandExpression was asserted to be not-null, but it can be null
		 * for function expression having zero arguments.
		 * 
		 * @see Bug ID: 1691820
		 */
		if( validate ) {
			validate( expressionContext );
		}
	}

	/**
	 * Gets the operand expression for this unary operator
	 * 
	 * @return Returns the operandExpression.
	 */
	public Expression getOperandExpression() {
		return operandExpression;
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		HashMap typeMapping = (HashMap) typePairMapping.get( getClass() );

		return (Type) typeMapping.get( operandExpression.getReturnType() );
	}

	/**
	 * Uninitializes the unary operator expression.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#uninitialize(org.vedantatree.expressionoasis.ExpressionContext)
	 */
	public void uninitialize( ExpressionContext expressionContext ) {
		this.operandExpression = null;
	}

	/**
	 * Validates the expression.
	 * 
	 * @param expressionContext
	 * @throws ExpressionEngineException
	 */
	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException {
		HashMap typeMapping = (HashMap) typePairMapping.get( getClass() );

		if( typeMapping == null ) {
			throw new ExpressionEngineException( "No type mapping specified for class \"" + getClass().getName() + "\"" );
		}

		if( typeMapping.get( operandExpression.getReturnType() ) == null ) {
			String prefix = StringUtils.getLastToken( getClass().getName(), "." );
			prefix = prefix.substring( 0, prefix.length() - "Expression".length() );
			throw new ExpressionEngineException( "Operand of type: [\"" + operandExpression.getReturnType()
					+ "\"] is not supported by operator \"" + prefix + "\"", EOErrorCodes.INVALID_OPERAND_TYPE, null );
		}
	}

	/**
	 * Adds the type mapping for the given operand type and its expected result
	 * type for a operator.
	 * 
	 * @param clazz class of the operator
	 * @param operandType type of operand
	 * @param resultType type of the result
	 */
	protected static final void addTypePair( Class clazz, Type operandType, Type resultType ) {
		if( clazz == null || !UnaryOperatorExpression.class.isAssignableFrom( clazz ) ) {
			throw new IllegalArgumentException( "\"" + clazz.getName()
					+ "\" is not a valid unary operator expression class." );
		}

		HashMap typeMapping = (HashMap) typePairMapping.get( clazz );

		if( typeMapping == null ) {
			typeMapping = new HashMap();
			typePairMapping.put( clazz, typeMapping );
		}

		typeMapping.put( operandType, resultType );
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String prefix = StringUtils.getLastToken( getClass().getName(), "." );
		prefix = prefix.substring( 0, prefix.length() );

		return prefix + ":[" + operandExpression + "]";
	}

	/**
	 * Allows an expression visitor to visit this expression and it's sub-expressions (implements Visitor design pattern).
	 * @see org.vedantatree.expressionoasis.expressions.Expression#accept(org.vedantatree.expressionoasis.ExpressionVisitor)
	 */
	public void accept( ExpressionVisitor visitor ) {
		visitor.visit( this );
		operandExpression.accept( visitor );
	}
}