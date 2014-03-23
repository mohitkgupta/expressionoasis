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
package org.vedantatree.expressionoasis.expressions.relational;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * Performs the greater than equal operations.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 *
 * Added support for nulls
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class GTEExpression extends BinaryOperatorExpression {

	static {
		addTypePair( GTEExpression.class, Type.LONG, Type.LONG, Type.BOOLEAN );
		addTypePair( GTEExpression.class, Type.DOUBLE, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( GTEExpression.class, Type.LONG, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( GTEExpression.class, Type.DOUBLE, Type.LONG, Type.BOOLEAN );

		// nullable type support
		addTypePair( GTEExpression.class, Type.OBJECT, Type.OBJECT, Type.BOOLEAN );
		addTypePair( GTEExpression.class, Type.LONG, Type.OBJECT, Type.BOOLEAN );
		addTypePair( GTEExpression.class, Type.OBJECT, Type.LONG, Type.BOOLEAN );
		addTypePair( GTEExpression.class, Type.DOUBLE, Type.OBJECT, Type.BOOLEAN );
		addTypePair( GTEExpression.class, Type.OBJECT, Type.DOUBLE, Type.BOOLEAN );
	}

	/**
	 * Performs the greater than equal operations.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Type leftType = leftOperandExpression.getReturnType();
		Type rightType = rightOperandExpression.getReturnType();
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();

		ValueObject result = null;

		if( leftType == Type.OBJECT && rightType == Type.OBJECT ) {
			result = new ValueObject( null, Type.BOOLEAN );
		}
		else if( ( leftType == Type.LONG && rightType == Type.LONG )
				|| ( leftType == Type.OBJECT && rightType == Type.LONG )
				|| ( leftType == Type.LONG && rightType == Type.OBJECT ) ) {
			Boolean value = null;
			if( leftValue != null && rightValue != null ) {
				value = ( (Long) leftValue ).longValue() >= ( (Long) rightValue ).longValue() ? Boolean.TRUE
						: Boolean.FALSE;
			}
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( ( leftType == Type.DOUBLE && rightType == Type.DOUBLE )
				|| ( leftType == Type.OBJECT && rightType == Type.DOUBLE )
				|| ( leftType == Type.DOUBLE && rightType == Type.OBJECT ) ) {
			Boolean value = null;
			if( leftValue != null && rightValue != null ) {
				value = ( (Double) leftValue ).doubleValue() >= ( (Double) rightValue ).doubleValue() ? Boolean.TRUE
						: Boolean.FALSE;
			}
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.DOUBLE && rightType == Type.LONG ) {
			Boolean value = ( (Double) leftValue ).doubleValue() >= ( (Long) rightValue ).longValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.LONG && rightType == Type.DOUBLE ) {
			Boolean value = ( (Long) leftValue ).longValue() >= ( (Double) rightValue ).doubleValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}

		return result;
	}
}