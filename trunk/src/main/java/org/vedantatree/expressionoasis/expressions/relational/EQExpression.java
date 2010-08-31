/**
 * Copyright (c) 2006 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.

 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.

 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.expressions.relational;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * This class expression performs the equal operation on values.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 *
 * Addded support for nulls.
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class EQExpression extends BinaryOperatorExpression {

	static {
		addTypePair( EQExpression.class, Type.LONG, Type.LONG, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.DOUBLE, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.STRING, Type.STRING, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.LONG, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.DOUBLE, Type.LONG, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.BOOLEAN, Type.BOOLEAN, Type.BOOLEAN );

		// nullable type support
		addTypePair( EQExpression.class, Type.OBJECT, Type.OBJECT, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.LONG, Type.OBJECT, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.OBJECT, Type.LONG, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.DOUBLE, Type.OBJECT, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.OBJECT, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.STRING, Type.OBJECT, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.OBJECT, Type.STRING, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.BOOLEAN, Type.OBJECT, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.OBJECT, Type.BOOLEAN, Type.BOOLEAN );
	}

	/**
	 * Performs the equlity operation.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Type leftType = leftOperandExpression.getReturnType();
		Type rightType = rightOperandExpression.getReturnType();
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();
		ValueObject result = null;

		if( leftType == Type.LONG && rightType == Type.LONG ) {
			Boolean value = ( (Long) leftValue ).longValue() == ( (Long) rightValue ).longValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.DOUBLE && rightType == Type.DOUBLE ) {
			Boolean value = ( (Double) leftValue ).doubleValue() == ( (Double) rightValue ).doubleValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.STRING && rightType == Type.STRING ) {
			Boolean value = leftValue.equals( rightValue ) ? Boolean.TRUE : Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.DOUBLE && rightType == Type.LONG ) {
			Boolean value = ( (Double) leftValue ).doubleValue() == ( (Long) rightValue ).longValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.LONG && rightType == Type.DOUBLE ) {
			Boolean value = ( (Long) leftValue ).longValue() == ( (Double) rightValue ).doubleValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.BOOLEAN && rightType == Type.BOOLEAN ) {
			Boolean value = leftValue.equals( rightValue ) ? Boolean.TRUE : Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.OBJECT || rightType == Type.OBJECT ) {
			Boolean value;
			if( leftValue == null || rightValue == null ) {
				value = leftValue == rightValue;
			}
			else {
				value = leftValue.equals( rightValue ) ? Boolean.TRUE : Boolean.FALSE;
			}
			result = new ValueObject( value, Type.BOOLEAN );
		}

		return result;
	}
}