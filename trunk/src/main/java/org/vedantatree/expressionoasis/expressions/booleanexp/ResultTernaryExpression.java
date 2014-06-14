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
package org.vedantatree.expressionoasis.expressions.booleanexp;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.expressionoasis.types.Type;
import org.vedantatree.expressionoasis.types.ValueObject;


/**
 * This expression manage the ':' operator of ternary expression,
 * more specifically result part of the ternary operator.
 * 
 * @author Mohit Gupta
 */

public class ResultTernaryExpression extends BinaryOperatorExpression
{

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException
	{
		// TODO change to right.. both types should be same
		return getRightOperandExpression().getReturnType();
		// throw new ExpressionEngineException(
		// "TODO: Should return the base type of both the results. Need to implement this with Type object." );
	}

	public ValueObject getValue() throws ExpressionEngineException
	{
		BinaryOperatorExpression leftBinaryExp = (BinaryOperatorExpression) leftOperandExpression;
		Object leftValue = leftBinaryExp.getLeftOperandExpression().getValue().getValue();
		return ( (Boolean) leftValue ).booleanValue() ? leftBinaryExp.getRightOperandExpression().getValue()
				: getRightOperandExpression().getValue();
	}

	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException
	{
		// can call super validate later when we shall implement the getReturnType method properly
		// super.validate( expressionContext );

		if( leftOperandExpression.getReturnType() == null )
		{
			throw new ExpressionEngineException( "Return type of left operand expression: [" + leftOperandExpression
					+ "] is null." );
		}

		if( rightOperandExpression.getReturnType() == null )
		{
			throw new ExpressionEngineException( "Return type of right operand expression: [" + rightOperandExpression
					+ "] is null." );
		}

		if( !( getLeftOperandExpression() instanceof ConditionTernaryExpression ) )
		{
			throw new ExpressionEngineException( "Left operand is not of ConditionTernary type. LeftOperand: ["
					+ leftOperandExpression + "]" );
		}
	}

}
