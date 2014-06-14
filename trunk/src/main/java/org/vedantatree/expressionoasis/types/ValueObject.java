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
package org.vedantatree.expressionoasis.types;

/**
 * This is the value wrapper for any object.
 * Also contains the type information for the ibject.
 * 
 * @author Parmod Kamboj
 * @version 1.0
 * 
 */
public class ValueObject
{

	/**
	 * This is the actual value object.
	 */
	private Object	value;

	/**
	 * This is the type of the object.
	 */
	private Type	valueType;

	/**
	 * Constructs the ValueObject
	 * 
	 * @param value
	 * @param valueType
	 */
	public ValueObject( Object value, Type valueType )
	{
		if( valueType == null )
		{
			throw new IllegalArgumentException( "Value type is not valid." );
		}
		this.value = value;
		this.valueType = valueType;
	}

	/**
	 * Gets the value
	 * 
	 * @return the value
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * Gets the value type.
	 * 
	 * @return the value type.
	 */
	public Type getValueType()
	{
		return valueType;
	}

	public String toString()
	{
		return "ValueObject@" + hashCode() + ": value[" + value + "] type[" + valueType + "]";
	}
}