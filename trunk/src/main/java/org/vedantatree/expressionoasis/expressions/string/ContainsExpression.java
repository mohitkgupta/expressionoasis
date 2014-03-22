package org.vedantatree.expressionoasis.expressions.string;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * Expression to evaluate the String.contains type expression.  
 * 
 * Expression format > userName contains 'Girish'. 'contains' expression has been added to Grammar.xml
 * 
 * @author Girish Kumar
 * @version 1.0
 * @since 3.1
 */
public class ContainsExpression extends BinaryOperatorExpression
{

	static
	{
		addTypePair( ContainsExpression.class, Type.STRING, Type.STRING, Type.BOOLEAN );

		//null support
		addTypePair( ContainsExpression.class, Type.OBJECT, Type.OBJECT, Type.BOOLEAN );
		addTypePair( ContainsExpression.class, Type.OBJECT, Type.STRING, Type.BOOLEAN );
		addTypePair( ContainsExpression.class, Type.STRING, Type.OBJECT, Type.BOOLEAN );
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
			result = leftStr.toUpperCase().contains( rightStr.toUpperCase() );
		}

		return new ValueObject( result, returnType );
	}

}
