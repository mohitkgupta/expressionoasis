/**
 * Copyright (c) 2006 VedantaTree all rights reserved.
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
package org.vedantatree.expressionoasis.expressions;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * This class expression is used to make the null value expression. It gives the
 * null value. Based on Numeric Expression.
 *
 * TODO: should we create a special null type in Type class rather than using
 * Type.createType("java.lang.Object")? Currently don't have that option as
 * the code for Type class is not yet available.
 *
 * @author Kris Marwood
 * @version 1.0
 */
public class NullExpression implements Expression {

	/**
	 * This is the long value for this expression.
	 */
	private ValueObject nullValue;

	/**
	 * Gets the value object for numeric value.
	 *
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		return nullValue;
	}

	/**
	 * Returns the long type.
	 *
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		return Type.OBJECT;
	}

	/**
	 * Initializes the numeric value object.
	 *
	 * @see org.vedantatree.expressionoasis.expressions.Expression#initialize(org.vedantatree.expressionoasis.ExpressionContext,
	 *      java.lang.Object)
	 */
	public void initialize( ExpressionContext expressionContext, Object parameters, boolean validate )
			throws ExpressionEngineException {
		nullValue = new ValueObject( null, Type.OBJECT );
	}

	/**
	 * Uninitaizes the expression
	 *
	 * @see org.vedantatree.expressionoasis.expressions.Expression#uninitialize(org.vedantatree.expressionoasis.ExpressionContext)
	 */
	public void uninitialize( ExpressionContext expressionContext ) {
		/* nothing to do here */
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "null";
	}

	/**
	* Allows an expression visitor to visit this expression and it's sub-expressions (implements Visitor design pattern).
	* @see org.vedantatree.expressionoasis.expressions.Expression#accept(org.vedantatree.expressionoasis.ExpressionVisitor)
	*/
	public void accept( ExpressionVisitor visitor ) {
		visitor.visit( this );
	}
}