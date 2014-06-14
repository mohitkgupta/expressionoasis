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
 * This is the expression class for AND operatiorn.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 * 
 *          Added support for nulls
 * 
 * @author Kris Marwood
 * @version 1.1
 */
public class AndExpression extends BinaryOperatorExpression
{

	static
	{
		addTypePair( AndExpression.class, Type.BOOLEAN, Type.BOOLEAN, Type.BOOLEAN );

		// nullable type support
		addTypePair( AndExpression.class, Type.BOOLEAN, Type.OBJECT, Type.BOOLEAN );
		addTypePair( AndExpression.class, Type.OBJECT, Type.BOOLEAN, Type.BOOLEAN );
		addTypePair( AndExpression.class, Type.OBJECT, Type.OBJECT, Type.BOOLEAN );
	}

	/**
	 * Gets the value of this operator.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException
	{
		Object leftValue = leftOperandExpression.getValue().getValue();
		Object rightValue = rightOperandExpression.getValue().getValue();

		Boolean value = null;

		if( leftValue != null && rightValue != null )
		{
			value = ( (Boolean) leftValue ).booleanValue() && ( (Boolean) rightValue ).booleanValue() ? Boolean.TRUE
					: Boolean.FALSE;
		}

		return new ValueObject( value, Type.BOOLEAN );
	}
}