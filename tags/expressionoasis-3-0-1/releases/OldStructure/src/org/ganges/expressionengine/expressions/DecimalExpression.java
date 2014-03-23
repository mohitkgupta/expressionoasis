/**
 * Created on Jan 27, 2006
 *
 * Copyright (c) 2005 Ganges Organisation all rights reserved.
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU Lesser General Public License as
 * published by the Free Software Foundation. See the GNU Lesser General Public License for more details.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.ganges.expressionengine.expressions;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This class expression is used to make the decimal value expression It gives
 * the decimal value.
 * 
 * @author Parmod Kamboj
 * @version 1.0
 */
public class DecimalExpression implements Expression {

	/**
	 * This is the decimal value for this expression.
	 */
	private ValueObject decimalValue;

	/**
	 * Gets the value object for decimal value.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		return decimalValue;
	}

	/**
	 * Returns the double type.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		return Type.DOUBLE;
	}

	/**
	 * Initializes the double value object.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#initialize(org.ganges.expressionengine.ExpressionContext,
	 *      java.lang.Object)
	 */
	public void initialize( ExpressionContext expressionContext, Object objectInfo ) throws ExpressionEngineException {
		Double value = new Double( (String) objectInfo );
		decimalValue = new ValueObject( value, Type.DOUBLE );
	}

	/**
	 * Uninitaizes the expression
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#uninitialize(org.ganges.expressionengine.ExpressionContext)
	 */
	public void uninitialize( ExpressionContext expressionContext ) {
		decimalValue = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return decimalValue == null ? "null-not-initialized" : decimalValue.getValue() + "";
	}
}