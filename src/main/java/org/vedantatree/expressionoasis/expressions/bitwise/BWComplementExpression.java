/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
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
package org.vedantatree.expressionoasis.expressions.bitwise;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.UnaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * Implementation of Bit wise Complement Operator (~)
 * It does not supports for double operands
 * 
 * @author Mohit Gupta
 *
 * Added support for nulls
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class BWComplementExpression extends UnaryOperatorExpression {

	static {
		addTypePair( BWComplementExpression.class, Type.INTEGER, Type.LONG );
		addTypePair( BWComplementExpression.class, Type.LONG, Type.LONG );

		// nullable type support
		addTypePair( BWComplementExpression.class, Type.OBJECT, Type.LONG );

	}

	public ValueObject getValue() throws ExpressionEngineException {
		Long result = null;
		Number value = (Number) getOperandExpression().getValue().getValue();

		if( value != null ) {
			result = ~value.longValue();
		}

		return new ValueObject( result, getReturnType() );
	}

}
