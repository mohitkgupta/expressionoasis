/**
 * Created on Jan 26, 2006
 *
 * Copyright (c) 2005 Ganges Organisation all rights reserved.
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation. See the GNU General Public License for more details.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.ganges.expressionengine.expressions.arithmatic;

import java.lang.reflect.Array;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.BinaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;
import org.ganges.utils.StringUtils;


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
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Object value = leftOperandExpression.getValue().getValue();
		long index = ( (Number) rightOperandExpression.getValue().getValue() ).longValue();

		return new ValueObject( Array.get( value, (int) index ), getReturnType() );
	}

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	@Override
	public Type getReturnType() throws ExpressionEngineException {
		return leftOperandExpression.getReturnType().getComponentType();
	}

	/**
	 * @see org.ganges.expressionengine.expressions.BinaryOperatorExpression#validate()
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