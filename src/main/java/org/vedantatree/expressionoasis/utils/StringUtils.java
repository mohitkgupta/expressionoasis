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

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class provides the util methods for string operations.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 * 
 */
public final class StringUtils
{

	private static Log	LOGGER	= LogFactory.getLog( StringUtils.class );

	/**
	 * Constructs the StringUtils
	 * This is made private to restrict its use to
	 * singleton class.
	 */
	private StringUtils()
	{
		/**
		 * Nothing to do here.
		 */
	}

	public static void assertQualifiedArgument( String attribute, String attributeName )
	{
		if( !isQualifiedString( attribute ) )
		{
			IllegalArgumentException iae = new IllegalArgumentException(
					"Null or zero length value found. attribute-name-message[" + attributeName + "] value[" + attribute
							+ "]" );
			LOGGER.error( iae );
			throw iae;
		}
	}

	public static void assertQualifiedArgument( String attribute )
	{
		if( !isQualifiedString( attribute ) )
		{
			IllegalArgumentException iae = new IllegalArgumentException( "Null or zero length string found. str["
					+ attribute + "]" );
			LOGGER.error( iae );
			throw iae;
		}
	}

	/**
	 * Checks whether the string is a qualified string or not.
	 * A string is a qualified string if it is not null and contains
	 * at least one charcter other than blank character.
	 * 
	 * @param str the string to inspect.
	 * @return Returns <code>true</code> is the string is qualified <code>false</code> otherwise.
	 */
	public final static boolean isQualifiedString( String str )
	{
		return str != null && !str.trim().equals( "" );
	}

	/**
	 * Creates an empty string of given length.
	 * 
	 * @param length the length of string.
	 * @return the empty string.
	 */
	public final static String blankString( int length )
	{
		StringBuffer buffer = new StringBuffer( length );
		for( int i = 0; i < length; i++ )
		{
			buffer.append( ' ' );
		}
		return buffer.toString();
	}

	/**
	 * Gets the last token for spcefied dlimiter and string.
	 * 
	 * @param value the string to parse
	 * @param delimiter the delimiter used for parsing.
	 * @return
	 */
	public final static String getLastToken( String value, String delimiter )
	{
		String result = null;
		StringTokenizer tokenizer = new StringTokenizer( value, delimiter );
		while( tokenizer.hasMoreElements() )
		{
			result = (String) tokenizer.nextElement();
		}
		return result;
	}

	public final static String objectArrayToString( Object[] objectArray )
	{
		StringBuffer arrayString = new StringBuffer( "{" );
		if( objectArray == null )
		{
			arrayString.append( "null" );
		}
		else if( objectArray.length == 0 )
		{
			arrayString.append( "empty" );
		}
		else
		{
			boolean first = true;
			for( int i = 0; i < objectArray.length; i++ )
			{
				if( !first )
				{
					arrayString.append( ", " );
				}
				arrayString.append( objectArray[i] );
				first = false;
			}
		}
		arrayString.append( "}" );

		return arrayString.toString();
	}

	public static void main( String[] args )
	{
		Object[] array = new Object[]
		{ "1", "2", "Ram" };
		System.out.println( objectArrayToString( array ) );
	}
}