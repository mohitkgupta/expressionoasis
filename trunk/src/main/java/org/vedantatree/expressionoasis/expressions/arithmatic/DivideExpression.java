/**
 * Copyright (c) 2005 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.

 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.expressions.arithmatic;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * This is the class expression which performs the divide operations.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 * 
 * Added support for nulls
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class DivideExpression extends BinaryOperatorExpression {

	static {
		// changed the return type to double even in case of long & long operands, 
		// because division result should always support decimal
		addTypePair( DivideExpression.class, Type.LONG, Type.LONG, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.DOUBLE, Type.DOUBLE, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.LONG, Type.DOUBLE, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.DOUBLE, Type.LONG, Type.DOUBLE );

		// nullable type support
		addTypePair( DivideExpression.class, Type.OBJECT, Type.OBJECT, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.DOUBLE, Type.OBJECT, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.OBJECT, Type.DOUBLE, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.LONG, Type.OBJECT, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.OBJECT, Type.LONG, Type.DOUBLE );
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		// always returning double because division result should always support decimals
		return Type.DOUBLE;
	}

	/**
	 * Performs the divide operation.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();
		Object result = null;
		Type returnType = getReturnType();

		//commented after changing the return type to double permanently
		//		if( returnType == Type.LONG ) {
		//			result = new Long( ( (Number) leftValue ).longValue() / ( (Number) rightValue ).longValue() );
		//		}
		//		else
		if( leftValue != null && rightValue != null ) {
			if( returnType == Type.DOUBLE ) {
				result = new Double( ( (Number) leftValue ).doubleValue() / ( (Number) rightValue ).doubleValue() );
			}
		}

		return new ValueObject( result, returnType );
	}
}