/**	
 *  Copyright (c) 2005-2014 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software. You can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL 
 *  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES 
 *  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE 
 *  OR OTHER DEALINGS IN THE SOFTWARE.See the GNU Lesser General Public License 
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis. If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Please consider to contribute any enhancements to upstream codebase. 
 *  It will help the community in getting improved code and features, and 
 *  may help you to get the later releases with your changes.
 */
package org.vedantatree.expressionoasis.expressions.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.Expression;
import org.vedantatree.expressionoasis.expressions.UnaryOperatorExpression;
import org.vedantatree.expressionoasis.expressions.arithmatic.ParanthesisExpression;
import org.vedantatree.expressionoasis.extensions.FunctionProvider;
import org.vedantatree.expressionoasis.types.MethodKey;
import org.vedantatree.expressionoasis.types.Type;
import org.vedantatree.expressionoasis.types.ValueObject;


/**
 * This is the function expression to call the functions.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 * 
 *          Modified to make validation of parameters optional.
 * 
 * @author Kris Marwood
 * @version 1.1
 */
public class FunctionExpression extends UnaryOperatorExpression
{

	/**
	 * This is the function name to execute
	 */
	private String				functionName;

	/**
	 * This is the function provider.
	 */
	private FunctionProvider	functionProvider;

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue(java.lang.Object)
	 */
	public ValueObject getValue() throws ExpressionEngineException
	{
		List<ValueObject> values = new ArrayList<ValueObject>();
		ParanthesisExpression argsExpression = (ParanthesisExpression) getOperandExpression();
		populateTypesAndValues( argsExpression.getOperandExpression(), null, values );

		ValueObject[] parameters = (ValueObject[]) values.toArray( new ValueObject[values.size()] );

		return functionProvider.getFunctionValue( functionName, parameters );
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	@Override
	public Type getReturnType() throws ExpressionEngineException
	{
		List<Type> types = new ArrayList<Type>();
		ParanthesisExpression argsExpression = (ParanthesisExpression) getOperandExpression();
		populateTypesAndValues( argsExpression.getOperandExpression(), types, null );

		Type[] parameterTypes = (Type[]) types.toArray( new Type[types.size()] );

		return functionProvider.getFunctionType( functionName, parameterTypes );
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#initialize(org.vedantatree.expressionoasis.ExpressionContext,
	 *      java.lang.Object)
	 */
	@Override
	public void initialize( ExpressionContext expressionContext, Object parameters, boolean validate )
			throws ExpressionEngineException
	{
		functionName = (String) expressionContext.getContextProperty( "TOKEN" );
		super.initialize( expressionContext, parameters, validate );
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.UnaryOperatorExpression#validate()
	 */
	@Override
	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException
	{
		// Initializes the function provider.
		ParanthesisExpression argsExpression = (ParanthesisExpression) getOperandExpression();
		List<Type> types = new ArrayList<Type>();
		List values = new ArrayList();
		populateTypesAndValues( argsExpression.getOperandExpression(), types, values );

		Type[] parameterTypes = (Type[]) types.toArray( new Type[types.size()] );

		for( Iterator functionProviders = expressionContext.getFunctionProviders().iterator(); functionProviders
				.hasNext(); )
		{
			FunctionProvider functionProvider = (FunctionProvider) functionProviders.next();

			if( functionProvider.supportsFunction( functionName, parameterTypes ) )
			{
				this.functionProvider = functionProvider;

				break;
			}
		}

		if( functionProvider == null )
		{
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
			throws ExpressionEngineException
	{
		if( expression instanceof ArgumentExpression )
		{
			ArgumentExpression argExp = (ArgumentExpression) expression;
			populateTypesAndValues( argExp.getLeftOperandExpression(), types, values );
			populateTypesAndValues( argExp.getRightOperandExpression(), types, values );
		}
		/*
		 * Argument express can be null.
		 * 
		 * @see Bug ID: 1691820
		 */
		else if( expression != null )
		{
			if( types != null )
			{
				types.add( expression.getReturnType() );
			}

			if( values != null )
			{
				values.add( expression.getValue() );
			}
		}
	}
}