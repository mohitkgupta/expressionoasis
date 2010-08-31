/**
 * Copyright (c) 2005 VedantaTree all rights reserved.
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
package org.vedantatree.expressionoasis.expressions.arithmatic;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * This class expression performs the modulus on integer values.
 * 
 * @author Parmod Kamboj
 * @version 1.0
 *
 * Added support for nulls
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class RemainderExpression extends BinaryOperatorExpression {

	static {
		addTypePair( RemainderExpression.class, Type.LONG, Type.LONG, Type.LONG );

		// nullable type support
		addTypePair( RemainderExpression.class, Type.LONG, Type.OBJECT, Type.LONG );
		addTypePair( RemainderExpression.class, Type.OBJECT, Type.LONG, Type.LONG );

	}

	/**
	 * Performs the modulus.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();
		Object result = null;

		if( leftValue != null && rightValue != null ) {
			result = new Long( ( (Number) leftValue ).longValue() % ( (Number) rightValue ).longValue() );
		}

		return new ValueObject( result, getReturnType() );
	}
}