package org.ganges.expressionengine.expressions.booleanexp;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.BinaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This expression manage the ':' operator of ternary expression, 
 * more specifically result part of the ternary operator.
 * 
 * @author Mohit Gupta
 */

public class ResultTernaryExpression extends BinaryOperatorExpression {

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		//TODO change to right.. both types should be same
		return getRightOperandExpression().getReturnType();
		//		throw new ExpressionEngineException(
		//				"TODO: Should return the base type of both the results. Need to implement this with Type object." );
	}

	public ValueObject getValue() throws ExpressionEngineException {
		BinaryOperatorExpression leftBinaryExp = (BinaryOperatorExpression) leftOperandExpression;
		Object leftValue = leftBinaryExp.getLeftOperandExpression().getValue().getValue();
		return ( (Boolean) leftValue ).booleanValue() ? leftBinaryExp.getRightOperandExpression().getValue()
				: getRightOperandExpression().getValue();
	}

	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException {
		// can call super validate later when we shall implement the getReturnType method properly
		//		super.validate( expressionContext );

		if( leftOperandExpression.getReturnType() == null ) {
			throw new ExpressionEngineException( "Return type of left operand expression: [" + leftOperandExpression
					+ "] is null." );
		}

		if( rightOperandExpression.getReturnType() == null ) {
			throw new ExpressionEngineException( "Return type of right operand expression: [" + rightOperandExpression
					+ "] is null." );
		}

		if( !( getLeftOperandExpression() instanceof ConditionTernaryExpression ) ) {
			throw new ExpressionEngineException( "Left operand is not of ConditionTernary type. LeftOperand: ["
					+ leftOperandExpression + "]" );
		}
	}

}
