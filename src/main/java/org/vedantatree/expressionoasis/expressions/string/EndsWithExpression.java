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
package org.vedantatree.expressionoasis.expressions.string;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.expressionoasis.types.Type;
import org.vedantatree.expressionoasis.types.ValueObject;


/**
 * Expression to evaluate the String.endsWith type expression.
 * 
 * Expression format > userName endsWith 'Girish'. 'endsWith' expression has been added to Grammar.xml
 * 
 * @author Girish Kumar
 * @version 1.0
 * @since 3.1
 */
public class EndsWithExpression extends BinaryOperatorExpression
{

	static
	{
		addTypePair( EndsWithExpression.class, Type.STRING, Type.STRING, Type.BOOLEAN );

		// null support
		addTypePair( EndsWithExpression.class, Type.OBJECT, Type.OBJECT, Type.BOOLEAN );
		addTypePair( EndsWithExpression.class, Type.OBJECT, Type.STRING, Type.BOOLEAN );
		addTypePair( EndsWithExpression.class, Type.STRING, Type.OBJECT, Type.BOOLEAN );
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
			result = leftStr.toUpperCase().endsWith( rightStr.toUpperCase() );
		}

		return new ValueObject( result, returnType );
	}

}
