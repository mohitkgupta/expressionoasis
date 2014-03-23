/**
 * Created on Jan 13, 2006
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
package org.ganges.expressionengine.expressions;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.types.Type;
import org.ganges.utils.StringUtils;


/**
 * This class defines the abstract implementation of unary operator expression.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * 
 * @version 1.0
 */
public abstract class UnaryOperatorExpression implements Expression {

	/**
	 * This is the type pair mapping for all the unary operators.
	 */
	private static Map typePairMapping = new Hashtable();

	/**
	 * This is the operand expression for this unary operator expression. There 
	 * will be only one operand with unary operator expression
	 */
	private Expression operandExpression;

	/**
	 * Initializes the operand expression.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#initialize(org.ganges.expressionengine.ExpressionContext,
	 *      java.lang.Object)
	 */
	public void initialize( ExpressionContext expressionContext, Object parameters ) throws ExpressionEngineException {
		this.operandExpression = (Expression) parameters;
		/*
		 * Earlier operandExpression was asserted to be not-null, but it can be null
		 * for function expression having zero arguments.
		 * 
		 * @see Bug ID: 1691820
		 */
		validate( expressionContext );
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
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		HashMap typeMapping = (HashMap) typePairMapping.get( getClass() );

		return (Type) typeMapping.get( operandExpression.getReturnType() );
	}

	/**
	 * Uninitializes the unary operator expression.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#uninitialize(org.ganges.expressionengine.ExpressionContext)
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
					+ "\"] is not supported by operator \"" + prefix + "\"" );
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
}