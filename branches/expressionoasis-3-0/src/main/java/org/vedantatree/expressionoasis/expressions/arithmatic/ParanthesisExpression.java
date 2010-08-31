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

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.UnaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * This expression represents the parenthesis for other child expression
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public class ParanthesisExpression extends UnaryOperatorExpression {

	/**
	 * Returns value of child expression.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		return getOperandExpression().getValue();
	}

	/**
	 * Retrusn the type of child expression
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	@Override
	public Type getReturnType() throws ExpressionEngineException {
		return getOperandExpression().getReturnType();
	}

	/**
	 * Do nothing.
	 */
	@Override
	protected void validate( ExpressionContext expressionContext ) {
		/**
		 * Nothing to validate
		 */
	}
}