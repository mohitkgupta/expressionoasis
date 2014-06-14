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

public class MethodKey
{

	public MethodKey( String methodName, Type parameterTypes[] )
	{
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		key = generateKey( methodName, parameterTypes );
	}

	public static String generateKey( String methodName, Type parameterTypes[] )
	{
		if( methodName == null )
			throw new IllegalArgumentException( "Passed methodName can't be null." );
		StringBuffer buffer = new StringBuffer( methodName );
		buffer.append( '(' );
		int length = parameterTypes != null ? parameterTypes.length : 0;
		for( int i = 0; i < length; i++ )
		{
			Type type = parameterTypes[i];
			buffer.append( type.getTypeName() );
			if( i < length - 1 )
				buffer.append( ',' );
		}

		buffer.append( ')' );
		return buffer.toString();
	}

	public boolean isAssignaleFrom( MethodKey key )
	{
		boolean result = false;
		if( key != null && key.methodName.equals( methodName ) )
		{
			Type thatParameterTypes[] = key.parameterTypes;
			int thatLength = thatParameterTypes != null ? thatParameterTypes.length : 0;
			int length = parameterTypes != null ? parameterTypes.length : 0;
			if( thatLength == length )
			{
				boolean assignableFrom = true;
				for( int i = 0; i < length; i++ )
				{
					if( parameterTypes[i].isAssignableFrom( thatParameterTypes[i] ) )
						continue;
					assignableFrom = false;
					break;
				}

				result = assignableFrom;
			}
		}
		return result;
	}

	public boolean equals( Object obj )
	{
		boolean result = false;
		if( obj instanceof MethodKey )
		{
			MethodKey thatKey = (MethodKey) obj;
			result = thatKey.key.equals( key );
		}
		return result;
	}

	public int hashCode()
	{
		return key.hashCode();
	}

	public String toString()
	{
		return key.toString();
	}

	private String	methodName;
	private Type	parameterTypes[];
	private String	key;
}
