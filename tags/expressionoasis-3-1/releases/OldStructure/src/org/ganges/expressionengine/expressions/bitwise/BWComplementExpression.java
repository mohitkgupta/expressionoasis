package org.ganges.expressionengine.expressions.bitwise;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.UnaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * Implementation of Bit wise Complement Operator (~)
 * It does not supports for double operands
 * 
 * @author Mohit Gupta
 */
public class BWComplementExpression extends UnaryOperatorExpression {

	static {
		addTypePair( BWComplementExpression.class, Type.INTEGER, Type.LONG );
		addTypePair( BWComplementExpression.class, Type.LONG, Type.LONG );
	}

	public ValueObject getValue() throws ExpressionEngineException {
		Number value = (Number) getOperandExpression().getValue().getValue();
		return new ValueObject( ~value.longValue(), getReturnType() );
	}

}
