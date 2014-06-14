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

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.expressionoasis.types.Type;
import org.vedantatree.expressionoasis.types.ValueObject;


/**
 * This expression manage the '?' operator of ternary expression,
 * more specifically condition part of the ternary operator.
 * 
 * @author Mohit Gupta
 */
public class ConditionTernaryExpression extends BinaryOperatorExpression
{

	static
	{
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
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException
	{
		return Type.BOOLEAN;
	}

	public ValueObject getValue() throws ExpressionEngineException
	{
		Object leftValue = leftOperandExpression.getValue().getValue();
		return new ValueObject( ( (Boolean) leftValue ).booleanValue(), getReturnType() );
	}

}
