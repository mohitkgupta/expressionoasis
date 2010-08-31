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
import java.util.Map;

import org.vedantatree.expressionoasis.EOErrorCodes;
import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.types.Type;
import org.vedantatree.utils.StringUtils;


/**
 * This class defines the abstract implemenation for binary operator expression.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 *
 * Modified to support visitor design pattern. Also replaced usage of Hashtable
 * with HashMap for performance improvement as synchronisation shouldn't be
 * required.
 *
 * @author Kris Marwood
 * @version 1.1
 */
public abstract class BinaryOperatorExpression implements Expression {

	/**
	 * This is the type pairs cache.
	 */
	private static final Map TYPE_PAIR_CACHE = new HashMap();

	/**
	 * This is the type pair mapping for all the binary operators.
	 */
	private static Map	   typePairMapping = new HashMap();

	/**
	 * Left operand expression for this binary operator expression.
	 */
	protected Expression	 leftOperandExpression;

	/**
	 * Right operand expression for this binary operator expression.
	 */
	protected Expression	 rightOperandExpression;

	/**
	 * Initializes the child expressions.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#initialize(org.vedantatree.expressionoasis.ExpressionContext,
	 *      java.lang.Object)
	 */
	public void initialize( ExpressionContext expressionContext, Object parameters, boolean validate )
			throws ExpressionEngineException {
		Expression[] arguments = null;

		try {
			arguments = (Expression[]) parameters;

			if( arguments == null || arguments.length != 2 || arguments[0] == null || arguments[1] == null ) {
				throw new ExpressionEngineException( "Child expressions information is not valid." );
			}
		}
		catch( ClassCastException ex ) {
			throw new ExpressionEngineException( "Child expressions information is not valid." );
		}

		leftOperandExpression = arguments[0];
		rightOperandExpression = arguments[1];

		if( validate ) {
			validate( expressionContext );
		}
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		HashMap typeMapping = (HashMap) typePairMapping.get( getClass() );

		Type type = (Type) typeMapping.get( createTypePair( leftOperandExpression.getReturnType(),
				rightOperandExpression.getReturnType() ) );
		if( type == null ) {
			if( getLeftOperandExpression().getReturnType() == Type.ANY_TYPE
					|| getRightOperandExpression().getReturnType() == Type.ANY_TYPE ) {
				type = Type.ANY_TYPE;
			}
		}
		return type;
	}

	/**
	 * Gets the value of leftOperandExpression.
	 * 
	 * @return Returns the leftOperandExpression.
	 */
	public Expression getLeftOperandExpression() {
		return leftOperandExpression;
	}

	/**
	 * Gets the value of rightOperandExpression.
	 * 
	 * @return Returns the rightOperandExpression.
	 */
	public Expression getRightOperandExpression() {
		return rightOperandExpression;
	}

	/**
	 * Uninitializes the binary expression.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#uninitialize(org.vedantatree.expressionoasis.ExpressionContext)
	 */
	public void uninitialize( ExpressionContext expressionContext ) {
		this.leftOperandExpression = null;
		this.rightOperandExpression = null;
	}

	/**
	 * Creates the type pair for given types pair
	 * 
	 * @param leftType
	 * @param rightType
	 * @return
	 */
	protected static final TypePair createTypePair( Type leftType, Type rightType ) {
		String key = leftType.getTypeName() + rightType.getTypeName();
		TypePair typePair = (TypePair) TYPE_PAIR_CACHE.get( key );

		if( typePair == null ) {
			typePair = new TypePair( leftType, rightType );
			TYPE_PAIR_CACHE.put( key, typePair );
		}

		return typePair;
	}

	/**
	 * Adds the type mapping for the given operator.
	 * 
	 * User can add as many mapping as required for operator. Like in case of 
	 * '+' operator, one possible mapping is left type = integer, right type = 
	 * string, result type = string. Because if we add integer to string, it 
	 * will result in a string value.
	 * 
	 * @param clazz class of the operator like +, - etc
	 * @param leftType type of left operand
	 * @param rightType type of right operand
	 * @param resultType type of result
	 */
	protected static final void addTypePair( Class clazz, Type leftType, Type rightType, Type resultType ) {
		if( clazz == null || !BinaryOperatorExpression.class.isAssignableFrom( clazz ) ) {
			throw new IllegalArgumentException( "\"" + clazz.getName()
					+ "\" is not a valid binary operator expression class." );
		}

		HashMap typeMapping = (HashMap) typePairMapping.get( clazz );

		if( typeMapping == null ) {
			typeMapping = new HashMap();
			typePairMapping.put( clazz, typeMapping );
		}

		typeMapping.put( createTypePair( leftType, rightType ), resultType );
	}

	/**
	 * Validates the expression.
	 * 
	 * @param expressionContext
	 * @throws ExpressionEngineException
	 */
	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException {
		if( leftOperandExpression.getReturnType() == null ) {
			throw new ExpressionEngineException( "Return type of left operand expression: [" + leftOperandExpression
					+ "] is null.", EOErrorCodes.INVALID_OPERAND_TYPE, null );
		}

		if( rightOperandExpression.getReturnType() == null ) {
			throw new ExpressionEngineException( "Return type of right operand expression: [" + rightOperandExpression
					+ "] is null.", EOErrorCodes.INVALID_OPERAND_TYPE, null );
		}

		if( getReturnType() == null ) {
			String prefix = StringUtils.getLastToken( getClass().getName(), "." );
			prefix = prefix.substring( 0, prefix.length() - "Expression".length() );
			throw new ExpressionEngineException( "Either no type mapping is defined or Operands of types: [\""
					+ leftOperandExpression.getReturnType() + "\", \"" + rightOperandExpression.getReturnType()
					+ "\"] are not supported by operator \"" + prefix + "\"", EOErrorCodes.INVALID_OPERAND_TYPE, null );
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String prefix = StringUtils.getLastToken( getClass().getName(), "." );
		prefix = prefix.substring( 0, prefix.length() );

		return prefix + "( " + leftOperandExpression + ", " + rightOperandExpression + " )";
	}

	/**
	 * Allows an expression visitor to visit this expression and it's sub-expressions (implements Visitor design pattern).
	 * @see org.vedantatree.expressionoasis.expressions.Expression#accept(org.vedantatree.expressionoasis.ExpressionVisitor)
	 */
	public void accept( ExpressionVisitor visitor ) {
		visitor.visit( this );
		leftOperandExpression.accept( visitor );
		rightOperandExpression.accept( visitor );
	}

	/**
	 * This is the class for making pair of valid types for a binary operator.
	 */
	protected static class TypePair {

		/**
		 * This is the left side operand's type.
		 */
		private Type leftType;

		/**
		 * This is the right side operand's type.
		 */
		private Type rightType;

		/**
		 * Constructs the TypePair
		 * 
		 * @param leftType type of left operand
		 * @param rightType type of right operand
		 */
		public TypePair( Type leftType, Type rightType ) {
			this.leftType = leftType;
			this.rightType = rightType;
		}

		/**
		 * Performs the equlity of type pairs
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals( Object arg ) {
			boolean result = false;

			if( arg instanceof TypePair ) {
				TypePair typePair = (TypePair) arg;
				result = typePair.leftType == leftType && typePair.rightType == rightType;
			}

			return result;
		}

		/**
		 * Returns the hashcode of the type pair.
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return ( leftType.getTypeName() + rightType.getTypeName() ).hashCode();
		}
	}
}