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
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * Implementation of Bit wise Exclusive OR Operator (^)
 * It does not supports for double operands
 * 
 * @author Mohit Gupta
 * 
 *         Added support for nulls
 * 
 * @author Kris Marwood
 * @version 1.1
 * 
 * 	Added support for Boolean type operands
 * 
 * @author Girish Kumar
 * @version 1.2
 * @since 3.1
 */
public class BWXorExpression extends BinaryOperatorExpression
{

	static
	{
		addTypePair( BWXorExpression.class, Type.INTEGER, Type.INTEGER, Type.LONG );
		addTypePair( BWXorExpression.class, Type.LONG, Type.LONG, Type.LONG );
		addTypePair( BWXorExpression.class, Type.INTEGER, Type.LONG, Type.LONG );
		addTypePair( BWXorExpression.class, Type.LONG, Type.INTEGER, Type.LONG );
		addTypePair( BWXorExpression.class, Type.BOOLEAN, Type.BOOLEAN, Type.BOOLEAN );

		// nullable type support
		addTypePair( BWXorExpression.class, Type.OBJECT, Type.OBJECT, Type.LONG );
		addTypePair( BWXorExpression.class, Type.INTEGER, Type.OBJECT, Type.LONG );
		addTypePair( BWXorExpression.class, Type.OBJECT, Type.INTEGER, Type.LONG );
		addTypePair( BWXorExpression.class, Type.LONG, Type.OBJECT, Type.LONG );
		addTypePair( BWXorExpression.class, Type.OBJECT, Type.LONG, Type.LONG );
	}

	public ValueObject getValue() throws ExpressionEngineException
	{
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();
		Object result = null;
		Type returnType = getReturnType();

		if( leftValue != null && rightValue != null )
		{
			if( returnType == Type.BOOLEAN )
			{
				result = new Boolean( ( (Boolean) leftValue ) ^ ( (Boolean) rightValue ) );
			}
			else
			{
				long leftLongValue = ( (Number) leftValue ).longValue();
				long rightLongValue = ( (Number) rightValue ).longValue();
				result = leftLongValue ^ rightLongValue;
			}
		}

		return new ValueObject( result, returnType );
	}
}
