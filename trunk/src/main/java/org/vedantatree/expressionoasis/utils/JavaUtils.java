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
package org.vedantatree.expressionoasis.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * This class provides the util methods for java type and
 * value conversion related.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * 
 * @version 1.0
 * 
 * Improve methods by adding assertions, added new methods for primitives
 * 
 * @author Mohit Gupta
 * @version 1.1
 * @since June 2014
 */
public final class JavaUtils
{

	/**
	 * This is the container for primitive and wrapper mappings.
	 */
	private static Map<Class, Class>	primitiveToWrappers	= new HashMap<Class, Class>();
	private static Map<Class, Class>	wrapperToprimitives	= new HashMap<Class, Class>();

	/**
	 * Filling the primitive and wraper mapping.
	 */
	static
	{
		primitiveToWrappers.put( int.class, Integer.class );
		primitiveToWrappers.put( long.class, Long.class );
		primitiveToWrappers.put( short.class, Short.class );
		primitiveToWrappers.put( byte.class, Byte.class );
		primitiveToWrappers.put( double.class, Double.class );
		primitiveToWrappers.put( float.class, Float.class );
		primitiveToWrappers.put( boolean.class, Boolean.class );
		primitiveToWrappers.put( char.class, Character.class );
		primitiveToWrappers.put( void.class, Void.class );
		wrapperToprimitives.put( java.lang.Integer.class, Integer.TYPE );
		wrapperToprimitives.put( java.lang.Long.class, Long.TYPE );
		wrapperToprimitives.put( java.lang.Short.class, Short.TYPE );
		wrapperToprimitives.put( java.lang.Byte.class, Byte.TYPE );
		wrapperToprimitives.put( java.lang.Double.class, Double.TYPE );
		wrapperToprimitives.put( java.lang.Float.class, Float.TYPE );
		wrapperToprimitives.put( java.lang.Boolean.class, Boolean.TYPE );
		wrapperToprimitives.put( java.lang.Character.class, Character.TYPE );
		wrapperToprimitives.put( java.lang.Void.class, Void.TYPE );
	}

	/**
	 * Constructs the JavaUtils
	 * This constructor is made private to restrict the use
	 * of singleton class.
	 */
	private JavaUtils()
	{
		/**
		 * Nothing to do here.
		 */
	}

	/**
	 * Converts the primitive class to wrapper class.
	 * 
	 * @param type the class to convert
	 * @return the wrapper class corresponding to give primitive class
	 */
	public final static Class convertToWrapper( Class type )
	{
		if( type == null || !type.isPrimitive() )
		{
			throw new IllegalArgumentException(
					"Given class type is wrong. Either it is null or not a primitive. given-class[" + type + "]" );
		}
		return primitiveToWrappers.get( type );
	}

	public static final Class convertToPrimitive( Class type )
	{
		if( type == null || type.isPrimitive() )
		{
			throw new IllegalArgumentException(
					"Given class type is wrong. Either it is null or is a primitive. given-class[" + type + "]" );
		}
		return wrapperToprimitives.get( type );
	}
}