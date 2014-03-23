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
package org.ganges.expressionengine.expressions.arithmatic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.Expression;
import org.ganges.expressionengine.expressions.UnaryOperatorExpression;
import org.ganges.expressionengine.expressions.property.ArgumentExpression;
import org.ganges.expressionengine.extensions.FunctionProvider;
import org.ganges.types.MethodKey;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * This is the function expression to call the functions.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public class FunctionExpression extends UnaryOperatorExpression {

	/**
	 * This is the function name to execute
	 */
	private String		   functionName;

	/**
	 * This is the function provider.
	 */
	private FunctionProvider functionProvider;

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getValue(java.lang.Object)
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		List<ValueObject> values = new ArrayList<ValueObject>();
		ParanthesisExpression argsExpression = (ParanthesisExpression) getOperandExpression();
		populateTypesAndValues( argsExpression.getOperandExpression(), null, values );

		ValueObject[] parameters = (ValueObject[]) values.toArray( new ValueObject[values.size()] );

		return functionProvider.getFunctionValue( functionName, parameters );
	}

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	@Override
	public Type getReturnType() throws ExpressionEngineException {
		List<Type> types = new ArrayList<Type>();
		ParanthesisExpression argsExpression = (ParanthesisExpression) getOperandExpression();
		populateTypesAndValues( argsExpression.getOperandExpression(), types, null );

		Type[] parameterTypes = (Type[]) types.toArray( new Type[types.size()] );

		return functionProvider.getFunctionType( functionName, parameterTypes );
	}

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#initialize(org.ganges.expressionengine.ExpressionContext,
	 *      java.lang.Object)
	 */
	@Override
	public void initialize( ExpressionContext expressionContext, Object parameters ) throws ExpressionEngineException {
		functionName = (String) expressionContext.getContextProperty( "TOKEN" );
		super.initialize( expressionContext, parameters );
	}

	/**
	 * @see org.ganges.expressionengine.expressions.UnaryOperatorExpression#validate()
	 */
	@Override
	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException {
		// Initializes the function provider.
		ParanthesisExpression argsExpression = (ParanthesisExpression) getOperandExpression();
		List<Type> types = new ArrayList<Type>();
		List values = new ArrayList();
		populateTypesAndValues( argsExpression.getOperandExpression(), types, values );

		Type[] parameterTypes = (Type[]) types.toArray( new Type[types.size()] );

		for( Iterator functionProviders = expressionContext.getFunctionProviders().iterator(); functionProviders
				.hasNext(); ) {
			FunctionProvider functionProvider = (FunctionProvider) functionProviders.next();

			if( functionProvider.supportsFunction( functionName, parameterTypes ) ) {
				this.functionProvider = functionProvider;

				break;
			}
		}

		if( functionProvider == null ) {
			throw new ExpressionEngineException( "No Function Provider exists for function: ["
					+ MethodKey.generateKey( functionName, parameterTypes ) + "]" );
		}
	}

	/**
	 * Populates the types and values of argument/paranthesis
	 * 
	 * @param expression
	 * @param types
	 * @param values
	 * @throws ExpressionEngineException
	 */
	private void populateTypesAndValues( Expression expression, List types, List values )
			throws ExpressionEngineException {
		if( expression instanceof ArgumentExpression ) {
			ArgumentExpression argExp = (ArgumentExpression) expression;
			populateTypesAndValues( argExp.getLeftOperandExpression(), types, values );
			populateTypesAndValues( argExp.getRightOperandExpression(), types, values );
		}
		/*
		 * Argument express can be null.
		 * 
		 * @see Bug ID: 1691820
		 */
		else if( expression != null ) {
			if( types != null ) {
				types.add( expression.getReturnType() );
			}

			if( values != null ) {
				values.add( expression.getValue() );
			}
		}
	}
}