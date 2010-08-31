/**
 * Created on Jan 27, 2006
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
package org.ganges.expressionengine.expressions;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This class expression is used to make the string value expression It gives
 * the string value.
 * 
 * @author Parmod Kamboj
 * @version 1.0
 */
public class StringExpression implements Expression {

	/**
	 * This is the string value for this expression.
	 */
	private ValueObject stringValue;

	/**
	 * Gets the value object for string value.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		return stringValue;
	}

	/**
	 * Returns the string type.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		return Type.STRING;
	}

	/**
	 * Initializes the string value object.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#initialize(org.ganges.expressionengine.ExpressionContext,
	 *      java.lang.Object)
	 */
	public void initialize( ExpressionContext expressionContext, Object parameters ) throws ExpressionEngineException {
		String value = (String) parameters;
		value = value.substring( 1, value.length() - 1 );
		value = value.replaceAll( "[\\\\][']", "'" );
		stringValue = new ValueObject( value, Type.STRING );
	}

	/**
	 * Uninitaizes the expression
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#uninitialize(org.ganges.expressionengine.ExpressionContext)
	 */
	public void uninitialize( ExpressionContext expressionContext ) {
		stringValue = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return stringValue == null ? "String value not set" : (String) stringValue.getValue();
	}
}