package org.ganges.expressionengine.expressions.bitwise;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.BinaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * Implementation of Bit wise Singed Left Shift Operator (<<)
 * It does not work for double operands
 * 
 * @author Mohit Gupta
 */
public class BWSignedLeftShiftExpression extends BinaryOperatorExpression {

	static {
		addTypePair( BWSignedLeftShiftExpression.class, Type.INTEGER, Type.INTEGER, Type.LONG );
		addTypePair( BWSignedLeftShiftExpression.class, Type.LONG, Type.INTEGER, Type.LONG );
		addTypePair( BWSignedLeftShiftExpression.class, Type.INTEGER, Type.LONG, Type.LONG );
		addTypePair( BWSignedLeftShiftExpression.class, Type.LONG, Type.LONG, Type.LONG );
	}

	public ValueObject getValue() throws ExpressionEngineException {
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();

		long leftLongValue = ( (Number) leftValue ).longValue();
		long rightLongValue = ( (Number) rightValue ).longValue();

		long result = leftLongValue << rightLongValue;

		return new ValueObject( new Long( result ), getReturnType() );
	}
}
