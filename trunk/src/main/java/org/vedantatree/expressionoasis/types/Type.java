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

import java.util.HashMap;
import java.util.Map;

import org.vedantatree.expressionoasis.utils.JavaUtils;
import org.vedantatree.expressionoasis.utils.StringUtils;
import org.vedantatree.expressionoasis.utils.Utilities;


/**
 * This class represents the type for value object. Its default implementation is for java classes.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 * 
 *          Fixed isAssignableFrom method. Wasn't working as thisClass and thatClass were around the wrong way.
 *          Changed to use nullable types and removed primitive checking.
 *          Made createType thread safe by implementing double checked locking.
 *          Cache the type class for performance.
 * 
 * @author Kris Marwood
 * @version 1.1
 * 
 *          Added documentation
 *          Fix the bug in constructor while creating the type. Wrappers types were not getting created with
 *          initialization.
 *          Restructured the code for primitive and wrapper types
 *          Added more assertions
 * 
 * @author Mohit Gupta
 * @version 1.2
 * @since June 2014
 */
public class Type
{

	/**
	 * An in memory cache for storing the generated types.
	 */
	private static final Map<String, Type>	TYPE_CACHE	= new HashMap<>();

	/**
	 * This is the byte type.
	 */
	public static final Type				BYTE		= new Type( (byte) 1, Byte.class );

	/**
	 * This is the short type.
	 */
	public static final Type				SHORT		= new Type( (byte) 2, Short.class );

	/**
	 * This is the integer type
	 */
	public static final Type				INTEGER		= new Type( (byte) 3, Integer.class );

	/**
	 * This is the long type.
	 */
	public static final Type				LONG		= new Type( (byte) 4, Long.class );

	/**
	 * This is the double type.
	 */
	public static final Type				DOUBLE		= new Type( (byte) 5, Double.class );

	/**
	 * This is the float type.
	 */
	public static final Type				FLOAT		= new Type( (byte) 6, Float.class );

	/**
	 * This is the boolean type.
	 */
	public static final Type				BOOLEAN		= new Type( (byte) 7, Boolean.class );

	/**
	 * This is the character type.
	 */
	public static final Type				CHARACTER	= new Type( (byte) 8, Character.class );

	/**
	 * This is the string type.
	 */
	public static final Type				STRING		= new Type( (byte) 9, String.class );

	/**
	 * This is the XML type.
	 */
	public static final Type				XML			= new Type( (byte) 10, null, "Type.XML" );

	/**
	 * This is the objec type (for nulls)
	 */
	public static final Type				OBJECT		= new Type( (byte) 11, Object.class );

	/**
	 * This is the type for custome types.
	 */
	public static final Type				ANY_TYPE	= new Type( (byte) Byte.MAX_VALUE, null, "Type.ANY" );

	/**
	 * This is the type for custom types.
	 */
	private static final byte				CUSTOM_TYPE	= Byte.MIN_VALUE;

	/**
	 * Name of the type. This is the main attribute based on which type is created
	 * and is being stored in cache.
	 */
	private String							typeName;

	/**
	 * Class represented by this Type. It can be null as some of the types may not
	 * have any class, for example like XML
	 */
	private Class							typeClass;

	/**
	 * Code for the type. These are predefined code to identify the default or custom types
	 */
	private byte							typeCode;

	/**
	 * Component type for this Type. Applicable only if this Type represents an Array
	 */
	private Type							componentType;

	static
	{
		populatePrimitiveTypes();
	}

	private static void populatePrimitiveTypes()
	{
		// these types will automatically be added to Type Cache from constructor
		// keeping same code as of Wrapper types, to support equality of primitiveType.equals(wrapperType) in
		// Type.equals operation

		new Type( (byte) 1, Byte.TYPE );
		new Type( (byte) 2, Short.TYPE );
		new Type( (byte) 3, Integer.TYPE );
		new Type( (byte) 4, Long.TYPE );
		new Type( (byte) 5, Double.TYPE );
		new Type( (byte) 6, Float.TYPE );
		new Type( (byte) 7, Boolean.TYPE );
		new Type( (byte) 8, Character.TYPE );
		// Void.Type
	}

	private Type( byte typeCode, Class typeClass )
	{
		this( typeCode, typeClass, null );
	}

	/**
	 * Constructs the Type
	 * 
	 * @param pTypeCode type code should always be set. No validation because it is being called from Create Type method
	 *        only
	 * @param pTypeClass Class can null, like for XML types, there is no class
	 * @param pTypeName Type name is must. It can be null only if class name is not null. In that case, we shall use
	 *        class name as type name
	 */
	private Type( byte pTypeCode, Class pTypeClass, String pTypeName )
	{
		// initialize the type name if given type name is null and class name is not null
		if( pTypeClass != null && pTypeName == null )
		{
			pTypeName = pTypeClass.getName();
		}

		if( !StringUtils.isQualifiedString( pTypeName ) )
		{
			throw new IllegalArgumentException( "Type name is not valid. typeCode[" + pTypeCode + "] typeName["
					+ pTypeName + "] typeClass[" + pTypeClass + "]" );
		}

		// Check for default type. Default types can't be overriden.
		// Also it ensures that no duplicate type can be created.
		// createType method must have already be invoked to check in TypeCache before requesting to create new type
		if( TYPE_CACHE.containsKey( pTypeName ) )
		{
			throw new IllegalArgumentException( "A type is already created for given type code. given-code["
					+ pTypeCode + "] given-typeName[" + pTypeName + "] existing-type[" + TYPE_CACHE.get( pTypeName )
					+ "]" );
		}

		this.typeCode = pTypeCode;
		this.typeName = pTypeName;

		// try to initialize the class
		if( pTypeClass == null )
		{
			try
			{
				pTypeClass = Class.forName( pTypeName );
			}
			catch( ClassNotFoundException e )
			{
				// do nothing. Some type names are not class names (e.g. Type.XML).
			}
		}
		this.typeClass = pTypeClass;

		// not synchronizing as access is already synchronized in caller 'createType'
		TYPE_CACHE.put( pTypeName, this );
	}

	/**
	 * Creates the type for given class
	 * 
	 * @param typeClass Class of the Object or Primitive for which type is being created
	 * @return the Type for given class
	 */
	public static final Type createType( Class<?> typeClass )
	{
		Utilities.assertNotNullArgument( typeClass, "Type-Class" );
		return createTypeInternal( typeClass, typeClass.getName() );
	}

	/**
	 * Creates the type for given type name
	 * 
	 * @param typeName Name of the type. It can be class name or even any other string also. In case if given name does
	 *        not match with any primitive or respective wrapper classes, it will be marked as Custom Type
	 * @return the type.
	 */
	public static final Type createType( String typeName )
	{
		return createTypeInternal( null, typeName );
	}

	private static Type createTypeInternal( Class typeClass, String typeName )
	{
		StringUtils.assertQualifiedArgument( typeName, "typeName" );

		// Search from cache.
		Type type = (Type) TYPE_CACHE.get( typeName );
		if( type == null )
		{
			synchronized( TYPE_CACHE )
			{
				type = (Type) TYPE_CACHE.get( typeName );
				if( type == null )
				{
					type = new Type( CUSTOM_TYPE, typeClass, typeName );
					TYPE_CACHE.put( typeName, type );
				}
			}
		}
		return type;
	}

	/**
	 * Checks whether the type represents an array or not.
	 * 
	 * @return <code>true</code> if the type represents an array, <code>false</code> otherwise
	 */
	public final boolean isArray()
	{
		return getComponentType() != null;
	}

	/**
	 * Gets the component type for this type.
	 * 
	 * @return the component type of this type, valid for
	 *         array type only.
	 */
	public final Type getComponentType()
	{
		// return component type if already initialize, or null if type class is null
		if( componentType != null || typeClass == null )
		{
			return componentType;
		}
		Class<?> clazz = typeClass.getComponentType();
		componentType = clazz != null ? createType( clazz ) : null;
		return componentType;
	}

	/**
	 * @param type Type to check for assignment
	 * @return true if this Type is assignable from specified Type
	 */
	public final boolean isAssignableFrom( Type type )
	{
		boolean result = false;
		if( type != null && type.getTypeClass() != null && typeClass != null )
		{
			Class<?> thisClass = typeClass;
			Class<?> thatClass = type.getTypeClass();
			result = thatClass.isAssignableFrom( thisClass );
		}
		return result;
	}

	/**
	 * Gets the typeClass
	 * 
	 * @return Returns the typeClass. It can be null for types which does not represents a specific class, like XML type
	 */
	public final Class<?> getTypeClass()
	{
		return typeClass;
	}

	/**
	 * Gets the typeName
	 * 
	 * @return Returns the typeName. It can never be null
	 */
	public final String getTypeName()
	{
		return typeName;
	}

	/**
	 * Performs the type equality.
	 */
	public boolean equals( Object arg )
	{
		boolean result = false;
		if( arg instanceof Type )
		{
			Type type = (Type) arg;
			result = type.typeCode == this.typeCode;
			if( result )
			{
				result = type.typeName.equals( typeName );
				if( !result )
				{
					if( getTypeClass() != null && getTypeClass().isPrimitive() )
					{
						String newTypeClass = JavaUtils.convertToWrapper( getTypeClass() ).getName();
						result = type.typeName.equals( newTypeClass );
					}
					else if( type.getTypeClass() != null && type.getTypeClass().isPrimitive() )
					{
						String newTypeClass = JavaUtils.convertToWrapper( type.getTypeClass() ).getName();
						result = typeName.equals( newTypeClass );
					}
				}

			}

		}
		return result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getTypeName();
	}

	public String toDebugString()
	{
		return "Type: type[" + typeCode + "] typeName[" + typeName + "] typeClass[" + typeClass + "]";
	}

	public static void main( String[] args ) throws Exception
	{
		System.out.println( Type.INTEGER );
		System.out.println( Type.BYTE );
		System.out.println( Type.DOUBLE );
		System.out.println( Type.LONG );
		System.out.println( Type.CHARACTER );
		System.out.println( Type.OBJECT );
		System.out.println( Type.ANY_TYPE );
		System.out.println( Type.STRING );
		System.out.println( boolean.class );
		System.out.println( boolean.class.getName() );
		// System.out.println(Class.forName( boolean.class.getName() ));

		Type booleanArrayType = Type.createType( new Boolean[]
		{ null, null }.getClass() );
		System.out.println( "boolean array type[" + booleanArrayType.toDebugString() + "]" );
		Type componentType = booleanArrayType.getComponentType();
		System.out.println( "boolean array type's component type[" + componentType.toDebugString() + "]" );
	}
}

// ------------------------------------------- Commented Code - to be removed soon -----------------------------

// /**
// * Gets the type name for default type.
// *
// * @return the default type name
// */
// private static final String getDefaultName( byte type )
// {
// String typeName = null;
// switch( type )
// {
// case 1:
// /*
// * @author: Mohit
// *
// * @date: 29-09-2013
// * all type names are replaced with constant strings
// * Because names will be exposed outside to specify the types in configuration etc
// * Using numbers for specifying types is not readable, hence these constant names will help
// * Code will not be broken, because this method is not used anywhere
// * TODO: Define constants
// */
// typeName = int.class.getName(); // "int"; //
// break;
// case 2:
// typeName = short.class.getName(); // "short"; //
// break;
// case 3:
// typeName = long.class.getName(); // "long"; //
// break;
// case 4:
// typeName = double.class.getName(); // "double"; //
// break;
// case 5:
// typeName = float.class.getName(); // "float"; //
// break;
// case 6:
// typeName = char.class.getName(); // "char"; //
// break;
// case 7:
// typeName = boolean.class.getName(); // "boolean"; //
// break;
// case 8:
// typeName = byte.class.getName(); // "byte"; //
// break;
// case 9:
// typeName = String.class.getName(); // "string"; //
// break;
// case 10:
// typeName = "Type.XML"; // "xml"; //
// break;
// case 11:
// typeName = Object.class.getName(); // "object"; //
// break;
// case Byte.MAX_VALUE:
// typeName = "Type.ANY"; // "any"; //
// break;
// }
// return typeName;
// }
//
