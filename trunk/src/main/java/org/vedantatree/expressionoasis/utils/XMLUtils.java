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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.parsers.DOMParser;
import org.vedantatree.expressionoasis.exceptions.ErrorCodes;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/**
 * @author Mohit Gupta
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLUtils
{

	private static Log	LOGGER	= LogFactory.getLog( XMLUtils.class );

	public static Document parseXML( URL xmlDocumentURL, String xsdPath ) throws ExpressionEngineException
	{
		Utilities.assertNotNullArgument( xmlDocumentURL );

		try
		{
			return parseXML( xmlDocumentURL.openStream(), xsdPath );
		}
		catch( IOException e )
		{
			LOGGER.error( "Problem while parsing the XML from URL. url[" + xmlDocumentURL.getPath() + "]" );
			throw new ExpressionEngineException( "Problem while parsing the XML from URL. url["
					+ xmlDocumentURL.getPath() + "]", ErrorCodes.RESOURCE_NOT_FOUND, e );
		}
	}

	public static Document parseXML( File xmlFile, String xsdPath ) throws ExpressionEngineException
	{
		Utilities.assertNotNullArgument( xmlFile );

		try
		{
			return parseXML( new FileInputStream( xmlFile ), xsdPath );
		}
		catch( FileNotFoundException e )
		{
			LOGGER.error( "File not found while parsing the xml. file[" + xmlFile.getAbsolutePath() + "]" );
			throw new ExpressionEngineException( "File not found while parsing the xml. file["
					+ xmlFile.getAbsolutePath() + "]", ErrorCodes.RESOURCE_NOT_FOUND, e );
		}
	}

	public static Document parseXML( InputStream is, String xsdPath ) throws ExpressionEngineException
	{
		LOGGER.debug( "entering : parseXML" );

		Utilities.assertNotNullArgument( is );
		DOMParser parser = new DOMParser();

		try
		{
			// set XSD path, if not null
			if( xsdPath != null )
			{
				parser.setFeature( "http://xml.org/sax/features/validation", true );
				parser.setProperty( "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
						xsdPath );
			}

			// Set Error Handler
			parser.setErrorHandler( new XMLErrorHandler() );

			// parse the XML
			parser.parse( new InputSource( is ) );

			return parser.getDocument();
		}
		catch( Exception e )
		{
			LOGGER.error( "Problem while parsing XML.", e );
			throw new ExpressionEngineException( "Problem while parsing XML.", ErrorCodes.PARSING_PROBLEM, e );
		}
	}

	public static void main( String[] args )
	{
	}
}

// --------------------------------------- Commented Code ----------------------------------------

// another way to parse the XML

// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
// try {
// DocumentBuilder builder = factory.newDocumentBuilder();
// return builder.parse( file );
// }
// catch( Exception e ) {
// LOGGER.error( "Problem while parsing XML.", e );
// throw new ApplicationException( IErrorCodes.XML_PARSING_ERROR, "Problem while parsing XML.", e );
// }
