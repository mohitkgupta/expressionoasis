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
package org.vedantatree.expressionoasis.expressions.relational;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.types.Type;
import org.vedantatree.expressionoasis.types.ValueObject;


/**
 * Performs the greater than operation.
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
public class GTExpression extends LTEExpression
{

	static
	{
		addTypePair( GTExpression.class, Type.LONG, Type.LONG, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.DOUBLE, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.LONG, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.DOUBLE, Type.LONG, Type.BOOLEAN );

		// nullable type support
		addTypePair( GTExpression.class, Type.OBJECT, Type.OBJECT, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.LONG, Type.OBJECT, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.OBJECT, Type.LONG, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.DOUBLE, Type.OBJECT, Type.BOOLEAN );
		addTypePair( GTExpression.class, Type.OBJECT, Type.DOUBLE, Type.BOOLEAN );
	}

	/**
	 * Returns the value of result of grater than operation
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	@Override
	public ValueObject getValue() throws ExpressionEngineException
	{
		ValueObject LTEvalueObject = super.getValue();

		ValueObject result = null;
		if( LTEvalueObject != null )
		{
			Boolean LTEboolValue = (Boolean) LTEvalueObject.getValue();
			if( LTEboolValue != null )
			{
				result = new ValueObject( LTEboolValue.booleanValue() ? Boolean.FALSE : Boolean.TRUE, Type.BOOLEAN );
			}
		}

		if( result == null )
		{
			result = new ValueObject( null, Type.BOOLEAN );
		}

		return result;
	}
}