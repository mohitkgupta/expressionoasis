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
package org.ganges.expressionengine.expressions.relational;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.BinaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This class expression performs the equal operation on values.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 */
public class EQExpression extends BinaryOperatorExpression {

	static {
		addTypePair( EQExpression.class, Type.LONG, Type.LONG, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.DOUBLE, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.STRING, Type.STRING, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.LONG, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.DOUBLE, Type.LONG, Type.BOOLEAN );
		addTypePair( EQExpression.class, Type.BOOLEAN, Type.BOOLEAN, Type.BOOLEAN );
	}

	/**
	 * Performs the equlity operation.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Type leftType = leftOperandExpression.getReturnType();
		Type rightType = rightOperandExpression.getReturnType();
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();
		ValueObject result = null;

		if( leftType == Type.LONG && rightType == Type.LONG ) {
			Boolean value = ( (Long) leftValue ).longValue() == ( (Long) rightValue ).longValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.DOUBLE && rightType == Type.DOUBLE ) {
			Boolean value = ( (Double) leftValue ).doubleValue() == ( (Double) rightValue ).doubleValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.STRING && rightType == Type.STRING ) {
			Boolean value = leftValue.equals( rightValue ) ? Boolean.TRUE : Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.DOUBLE && rightType == Type.LONG ) {
			Boolean value = ( (Double) leftValue ).doubleValue() == ( (Long) rightValue ).longValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.LONG && rightType == Type.DOUBLE ) {
			Boolean value = ( (Long) leftValue ).longValue() == ( (Double) rightValue ).doubleValue() ? Boolean.TRUE
					: Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}
		else if( leftType == Type.BOOLEAN && rightType == Type.BOOLEAN ) {
			Boolean value = leftValue.equals( rightValue ) ? Boolean.TRUE : Boolean.FALSE;
			result = new ValueObject( value, Type.BOOLEAN );
		}

		return result;
	}
}