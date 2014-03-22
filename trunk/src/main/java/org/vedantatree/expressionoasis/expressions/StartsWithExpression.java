package org.vedantatree.expressionoasis.expressions;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * 
 * @author Girish Kumar
 * @version {version}
 */
public class StartsWithExpression extends BinaryOperatorExpression
{

	static
	{
		addTypePair( StartsWithExpression.class, Type.STRING, Type.STRING, Type.BOOLEAN );
	}

	@Override
	public void initialize( ExpressionContext expressionContext, Object parameters, boolean validate )
			throws ExpressionEngineException
	{
		super.initialize( expressionContext, parameters, validate );
	}

	@Override
	public ValueObject getValue() throws ExpressionEngineException
	{
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();
		Object result = false;
		Type returnType = getReturnType();

		if( leftValue != null && rightValue != null )
		{
			String leftStr = (String) leftValue;
			String rightStr = (String) rightValue;
			result = leftStr.toUpperCase().startsWith( rightStr.toUpperCase() );
		}

		return new ValueObject( result, returnType );
	}

}
