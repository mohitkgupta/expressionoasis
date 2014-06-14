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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Utilities
{

	private static Log	logger	= LogFactory.getLog( Utilities.class );

	public static void assertNotNullArgument( Object attribute, String attributeName )
	{
		if( attribute instanceof String )
		{
			StringUtils.assertQualifiedArgument( (String) attribute, attributeName );
		}
		else if( attribute == null )
		{

			IllegalArgumentException iae = new IllegalArgumentException(
					"Null Object specified as value of attribute. attribute-name-message[" + attributeName + "]" );
			logger.error( iae );
			throw iae;
		}
	}

	public static void assertNotNullArgument( Object attribute )
	{
		if( attribute instanceof String )
		{
			StringUtils.assertQualifiedArgument( (String) attribute );
		}
		else if( attribute == null )
		{
			IllegalArgumentException iae = new IllegalArgumentException( "Null Object specified as attribute" );
			logger.error( iae );
			throw iae;
		}
	}

	/**
	 * Validates email address.
	 * 
	 * @param emailID: email address to be verified
	 * @return boolean: true if varified correctly false if email address is incorrect
	 */
	public static boolean validateEmailID( String emailID )
	{

		if( emailID.indexOf( '@' ) == -1 || emailID.indexOf( '@' ) != emailID.lastIndexOf( '@' )
				|| emailID.lastIndexOf( '@' ) == ( emailID.length() - 1 ) || emailID.indexOf( '@' ) == 0 )
		{
			return false;
		}
		else if( emailID.lastIndexOf( '.' ) == -1 || ( emailID.length() - emailID.lastIndexOf( '.' ) - 1 ) < 2 )
		{
			return false;
		}
		else if( emailID.indexOf( '@' ) > emailID.lastIndexOf( '.' )
				|| ( emailID.lastIndexOf( '.' ) - emailID.indexOf( '@' ) ) == 1 )
		{
			return false;
		}
		else if( emailID.indexOf( ' ' ) != -1 )
		{
			return false;
		}
		else
		{
			String spclChars = ";:?<>!~`!#$%^&*()+=|\\/'\"";

			for( int i = 0; i < spclChars.length(); i++ )
			{
				if( emailID.indexOf( spclChars.charAt( i ) ) != -1 )
				{
					return false;
				}
			}

			return true;

		}
	}

	/**
	 * Converts a camel case string into title case string.
	 * 
	 * @param inpString: string to be converted
	 * @return String coverted string
	 */
	public static String convertCamelToTitleString( String inpString )
	{

		while( true )
		{
			if( inpString.indexOf( "_" ) != -1 )
			{
				int k = inpString.indexOf( '_' );
				inpString = inpString.substring( 0, ( k ) ) + inpString.substring( k + 1 );
			}
			else
				break;
		}

		for( int i = 0; i < inpString.length(); i++ )
		{
			String ch;
			String rg = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			if( i != 0 )
			{
				ch = "" + inpString.charAt( i );
				if( rg.indexOf( ch ) != -1 )
				{
					inpString = inpString.substring( 0, i ) + " " + inpString.substring( i );
					i = i + 2;
				}
			}
		}

		return inpString;
	}

	/**
	 * displays map's keys and its value.
	 * 
	 * @param map
	 */
	public static void printMapEntries( Map map )
	{

		logger.info( "------------------------------------------" );
		logger.info( "Printing map >> " );

		int size = map.size();
		Object[] keys = map.keySet().toArray();

		for( int i = 0; i < size; i++ )
		{
			logger.info( "entry[" + keys[i] + "] = " + map.get( keys[i] ) );
		}

		logger.info( "-------------------------------------------" );

	}

	public static int[] getIndexArray( String combinedString )
	{
		System.out.println( "Combined string is [" + combinedString + "]" );
		String[] idArray = combinedString.split( ":" );
		int[] idIntArray = new int[idArray.length];
		for( int i = 0; i < idArray.length; i++ )
		{
			idIntArray[i] = Integer.parseInt( idArray[i].trim() );
			System.out.println( "Long id at index [" + i + "] is [" + idIntArray[i] + "]" );
		}
		return idIntArray;
	}
}
