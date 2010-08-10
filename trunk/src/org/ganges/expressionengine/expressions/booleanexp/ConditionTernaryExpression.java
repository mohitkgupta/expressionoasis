package org.ganges.expressionengine.expressions.booleanexp;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.BinaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This expression manage the '?' operator of ternary expression, 
 * more specifically condition part of the ternary operator.
 * 
 * @author Mohit Gupta
 */
public class ConditionTernaryExpression extends BinaryOperatorExpression {

	static {
		addTypePair( ConditionTernaryExpression.class, Type.BOOLEAN, Type.BOOLEAN, Type.BOOLEAN );
		addTypePair( ConditionTernaryExpression.class, Type.BOOLEAN, Type.BYTE, Type.BOOLEAN );
		addTypePair( ConditionTernaryExpression.class, Type.BOOLEAN, Type.CHARACTER, Type.BOOLEAN );
		addTypePair( ConditionTernaryExpression.class, Type.BOOLEAN, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( ConditionTernaryExpression.class, Type.BOOLEAN, Type.FLOAT, Type.BOOLEAN );
		addTypePair( ConditionTernaryExpression.class, Type.BOOLEAN, Type.INTEGER, Type.BOOLEAN );
		addTypePair( ConditionTernaryExpression.class, Type.BOOLEAN, Type.LONG, Type.BOOLEAN );
		addTypePair( ConditionTernaryExpression.class, Type.BOOLEAN, Type.SHORT, Type.BOOLEAN );
		addTypePair( ConditionTernaryExpression.class, Type.BOOLEAN, Type.STRING, Type.BOOLEAN );
	}

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		return Type.BOOLEAN;
	}

	public ValueObject getValue() throws ExpressionEngineException {
		Object leftValue = leftOperandExpression.getValue().getValue();
		return new ValueObject( ( (Boolean) leftValue ).booleanValue(), getReturnType() );
	}

}
