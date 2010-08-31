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

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.UnaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This is the positive unary operator expression.
 * 
 * @author Parmod Kamboj
 * @version 1.0
 */
public class PlusExpression extends UnaryOperatorExpression {

	static {
		addTypePair( PlusExpression.class, Type.LONG, Type.LONG );
		addTypePair( PlusExpression.class, Type.DOUBLE, Type.DOUBLE );
	}

	/**
	 * Returns the positive value as it is.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		return getOperandExpression().getValue();
	}
}