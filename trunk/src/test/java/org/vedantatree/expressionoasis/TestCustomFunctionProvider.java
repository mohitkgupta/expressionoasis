package org.vedantatree.expressionoasis;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.ExpressionFactory;
import org.vedantatree.expressionoasis.extensions.FunctionProvider;
import org.vedantatree.expressionoasis.grammar.DefaultXMLGrammar;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


public class TestCustomFunctionProvider implements FunctionProvider
{

	public void initialize( ExpressionContext expressionContext ) throws ExpressionEngineException
	{
		ExpressionFactory.getInstance().addFunction( "mySum" );

		// alternatively this can be defined in grammer.xml with 'functions' element
		ExpressionEngine.getGrammar().addFunction( "mySum" );
	}

	public Type getFunctionType( String functionName, Type[] parameterTypes ) throws ExpressionEngineException
	{
		if( functionName.equals( "mySum" ) )
			return Type.DOUBLE;
		return null;
	}

	public ValueObject getFunctionValue( String functionName, ValueObject[] parameters )
			throws ExpressionEngineException
	{
		if( functionName.equals( "mySum" ) )
		{
			double sum = 0;
			for( ValueObject v : parameters )
				sum += ( (Number) v.getValue() ).doubleValue();
			return new ValueObject( sum, Type.DOUBLE );
		}

		return null;
	}

	public boolean supportsFunction( String functionName, Type[] parameterTypes ) throws ExpressionEngineException
	{
		return functionName.equals( "mySum" );
	}

}
