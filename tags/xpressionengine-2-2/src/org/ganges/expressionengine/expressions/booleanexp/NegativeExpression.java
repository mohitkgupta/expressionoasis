package org.ganges.expressionengine.expressions.booleanexp;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.UnaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This is the negative unary operator expression.
 * 
 * @author Mohit Gupta
 * @version 1.0
 */
public class NegativeExpression extends UnaryOperatorExpression {

	static {
		addTypePair( NegativeExpression.class, Type.BOOLEAN, Type.BOOLEAN );
	}

	/**
	 * Returns the negative value.
	 * 
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		Boolean value = (Boolean) getOperandExpression().getValue().getValue();
		Type resultType = getReturnType();

		return new ValueObject( value.booleanValue() ? Boolean.FALSE : Boolean.TRUE, resultType );
	}
}