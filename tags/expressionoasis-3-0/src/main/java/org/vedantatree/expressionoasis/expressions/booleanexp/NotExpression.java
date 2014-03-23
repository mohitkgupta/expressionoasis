/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
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
package org.vedantatree.expressionoasis.expressions.booleanexp;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.UnaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * This is the logical not operator expression.
 * 
 * @author Mohit Gupta
 * @version 1.0
 *
 * Added support for nulls
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class NotExpression extends UnaryOperatorExpression {

	static {
		addTypePair( NotExpression.class, Type.BOOLEAN, Type.BOOLEAN );

		// nullable type support
		addTypePair( NotExpression.class, Type.OBJECT, Type.BOOLEAN );
	}

	/**
	 * Returns the negative value.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Boolean operandValue = (Boolean) getOperandExpression().getValue().getValue();

		Boolean result = null;

		if( operandValue != null ) {
			result = operandValue.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
		}

		return new ValueObject( result, Type.BOOLEAN );
	}
}