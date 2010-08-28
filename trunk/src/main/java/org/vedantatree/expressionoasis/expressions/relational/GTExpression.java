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
 * Performs the greater than operation.
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
public class GTExpression extends LTEExpression {

	static {
		addTypePair( GTExpression.class, Type.LONG, Type.LONG, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.DOUBLE, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.LONG, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.DOUBLE, Type.LONG, Type.BOOLEAN );

		// nullable type support
		addTypePair( GTExpression.class, Type.OBJECT, Type.OBJECT, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.LONG, Type.OBJECT, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.OBJECT, Type.LONG, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.DOUBLE, Type.OBJECT, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.OBJECT, Type.DOUBLE, Type.BOOLEAN );
	}

	/**
	 * Returns the value of result of grater than operation
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	@Override
	public ValueObject getValue() throws ExpressionEngineException {
		ValueObject LTEvalueObject = super.getValue();

		ValueObject result = null;
		if( LTEvalueObject != null ) {
			Boolean LTEboolValue = (Boolean) LTEvalueObject.getValue();
			if( LTEboolValue != null ) {
				result = new ValueObject( LTEboolValue.booleanValue() ? Boolean.FALSE : Boolean.TRUE, Type.BOOLEAN );
			}
		}

		if( result == null ) {
			result = new ValueObject( null, Type.BOOLEAN );
		}

		return result;
	}
}