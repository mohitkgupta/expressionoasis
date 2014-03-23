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
import org.vedantatree.expressionoasis.expressions.UnaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * This is the negative unary operator expression.
 * 
 * @author Parmod Kamboj
 * @version 1.0
 *
 * Added support for nulls
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class MinusExpression extends UnaryOperatorExpression {

	static {
		addTypePair( MinusExpression.class, Type.LONG, Type.LONG );
		addTypePair( MinusExpression.class, Type.DOUBLE, Type.DOUBLE );

		// nullable type support
		addTypePair( MinusExpression.class, Type.OBJECT, Type.OBJECT );
	}

	/**
	 * Returns the negative value.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Number value = (Number) getOperandExpression().getValue().getValue();
		Type resultType = getReturnType();

		if( value != null ) {
			if( resultType == Type.LONG ) {
				value = new Long( value.longValue() * -1 );
			}
			else {
				value = new Double( value.doubleValue() * -1 );
			}
		}

		return new ValueObject( value, resultType );
	}
}