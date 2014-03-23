/**
 * Created on Jan 26, 2006
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
package org.ganges.expressionengine.expressions.arithmatic;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.BinaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This is the class expression which performs the divide operations.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public class DivideExpression extends BinaryOperatorExpression {

	static {
		// changed the return type to double even in case of long & long operands, 
		// because division result should always support decimal
		addTypePair( DivideExpression.class, Type.LONG, Type.LONG, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.DOUBLE, Type.DOUBLE, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.LONG, Type.DOUBLE, Type.DOUBLE );
		addTypePair( DivideExpression.class, Type.DOUBLE, Type.LONG, Type.DOUBLE );
	}

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		// always returning double because division result should always support decimals
		return Type.DOUBLE;
	}

	/**
	 * Performs the divide operation.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();
		Object result = null;
		Type returnType = getReturnType();

		//commented after changing the return type to double permanently
		//		if( returnType == Type.LONG ) {
		//			result = new Long( ( (Number) leftValue ).longValue() / ( (Number) rightValue ).longValue() );
		//		}
		//		else 
		if( returnType == Type.DOUBLE ) {
			result = new Double( ( (Number) leftValue ).doubleValue() / ( (Number) rightValue ).doubleValue() );
		}

		return new ValueObject( result, returnType );
	}
}