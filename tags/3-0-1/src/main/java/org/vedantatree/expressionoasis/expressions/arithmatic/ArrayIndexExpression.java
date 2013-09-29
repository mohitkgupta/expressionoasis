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

import java.lang.reflect.Array;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;
import org.vedantatree.utils.StringUtils;


/**
 * This is the class expression to manipulate the indexed value from the array.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 */
public class ArrayIndexExpression extends BinaryOperatorExpression {

	/**
	 * Gets the value from the array.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Object value = leftOperandExpression.getValue().getValue();
		long index = ( (Number) rightOperandExpression.getValue().getValue() ).longValue();

		return new ValueObject( Array.get( value, (int) index ), getReturnType() );
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	@Override
	public Type getReturnType() throws ExpressionEngineException {
		return leftOperandExpression.getReturnType().getComponentType();
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression#validate()
	 */
	@Override
	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException {
		if( !leftOperandExpression.getReturnType().isArray() || rightOperandExpression.getReturnType() != Type.LONG ) {
			String prefix = StringUtils.getLastToken( getClass().getName(), "." );
			prefix = prefix.substring( 0, prefix.length() - "Expression".length() );
			throw new ExpressionEngineException( "Operands of types: [\"" + leftOperandExpression.getReturnType()
					+ "\", \"" + rightOperandExpression.getReturnType() + "\"] are not supported by operator \""
					+ prefix + "\"" );
		}
	}
}