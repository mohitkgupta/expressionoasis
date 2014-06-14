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
 * This class expression performs the not equal operation on values.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public class NEExpression extends EQExpression
{

	static
	{
		addTypePair( NEExpression.class, Type.LONG, Type.LONG, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.DOUBLE, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.STRING, Type.STRING, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.LONG, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.DOUBLE, Type.LONG, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.BOOLEAN, Type.BOOLEAN, Type.BOOLEAN );

		// nullable type support
		addTypePair( NEExpression.class, Type.OBJECT, Type.OBJECT, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.LONG, Type.OBJECT, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.OBJECT, Type.LONG, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.DOUBLE, Type.OBJECT, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.OBJECT, Type.DOUBLE, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.STRING, Type.OBJECT, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.OBJECT, Type.STRING, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.BOOLEAN, Type.OBJECT, Type.BOOLEAN );
		addTypePair( NEExpression.class, Type.OBJECT, Type.BOOLEAN, Type.BOOLEAN );
	}

	/**
	 * Performs the not equal operation
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	@Override
	public ValueObject getValue() throws ExpressionEngineException
	{
		Boolean value = (Boolean) super.getValue().getValue();

		return new ValueObject( value.booleanValue() ? Boolean.FALSE : Boolean.TRUE, Type.BOOLEAN );
	}
}