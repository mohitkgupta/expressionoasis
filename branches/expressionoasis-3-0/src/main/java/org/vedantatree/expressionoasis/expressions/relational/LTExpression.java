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
package org.vedantatree.expressionoasis.expressions.relational;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * Performs the less than operation.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 *
 * Added support for nulls
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class LTExpression extends GTEExpression {

	static {
		addTypePair( LTExpression.class, Type.LONG, Type.LONG, Type.BOOLEAN );
		addTypePair( LTExpression.class, Type.DOUBLE, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( LTExpression.class, Type.LONG, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( LTExpression.class, Type.DOUBLE, Type.LONG, Type.BOOLEAN );

		// nullable type support
		addTypePair( LTExpression.class, Type.OBJECT, Type.OBJECT, Type.BOOLEAN );
		addTypePair( LTExpression.class, Type.LONG, Type.OBJECT, Type.BOOLEAN );
		addTypePair( LTExpression.class, Type.OBJECT, Type.LONG, Type.BOOLEAN );
		addTypePair( LTExpression.class, Type.DOUBLE, Type.OBJECT, Type.BOOLEAN );
		addTypePair( LTExpression.class, Type.OBJECT, Type.DOUBLE, Type.BOOLEAN );
	}

	/**
	 * Returns the value of less than operation.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	@Override
	public ValueObject getValue() throws ExpressionEngineException {
		ValueObject GTEvalueObject = super.getValue();

		ValueObject result = null;
		if( GTEvalueObject != null ) {
			Boolean GTEboolValue = (Boolean) GTEvalueObject.getValue();
			if( GTEboolValue != null ) {
				result = new ValueObject( GTEboolValue.booleanValue() ? Boolean.FALSE : Boolean.TRUE, Type.BOOLEAN );
			}
		}

		if( result == null ) {
			result = new ValueObject( null, Type.BOOLEAN );
		}

		return result;
	}
}