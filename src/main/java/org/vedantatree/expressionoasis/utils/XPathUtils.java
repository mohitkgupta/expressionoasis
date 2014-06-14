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
import java.net.URL;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.vedantatree.expressionoasis.exceptions.ErrorCodes;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.types.Type;
import org.vedantatree.expressionoasis.types.ValueObject;
import org.w3c.dom.Document;


/**
 * @author Mohit Gupta
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class XPathUtils
{

	private static Log	LOGGER	= LogFactory.getLog( XPathUtils.class );

	public static ValueObject evaluateExpressionToValueObject( Document document, String xPathExpression )
			throws ExpressionEngineException
	{
		XObject xObject = evaluateExpression( document, xPathExpression );
		try
		{
			if( xObject.getType() == XObject.CLASS_BOOLEAN )
			{
				return new ValueObject( new Boolean( xObject.bool() ), Type.BOOLEAN );
			}
			else if( xObject.getType() == XObject.CLASS_NUMBER )
			{
				return new ValueObject( new Double( xObject.num() ), Type.DOUBLE );
			}
			else if( xObject.getType() == XObject.CLASS_STRING )
			{
				return new ValueObject( xObject.str(), Type.STRING );
			}
			else if( xObject.getType() == XObject.CLASS_NODESET )
			{
				// passing XNodeSet
				return new ValueObject( xObject.object(), Type.XML );
			}
			else
			{
				return new ValueObject( xObject.object(), Type.ANY_TYPE );
			}
		}
		catch( TransformerException e )
		{
			ExpressionEngineException eex = new ExpressionEngineException(
					"Problem while evaluating XPath Expression-[" + xPathExpression + "]",
					ErrorCodes.EXPRESSION_EVALUATION_PROBLEM, e );
			LOGGER.error( "Problem while evaluating XPath Expression", eex );
			throw eex;
		}
	}

	public static String getXMLString( Object obj )
	{
		Utilities.assertNotNullArgument( obj );
		if( obj instanceof XNodeSet )
		{
			return ( (XNodeSet) obj ).str();
		}
		return obj.toString();
	}

	public static XObject evaluateExpression( Document document, String xPathExpression )
			throws ExpressionEngineException
	{
		XObject expressionValue = null;
		try
		{
			expressionValue = XPathAPI.eval( document, xPathExpression );
		}
		catch( TransformerException e )
		{
			ExpressionEngineException eex = new ExpressionEngineException( "Problem while parsing xPathExpression["
					+ xPathExpression + "]", ErrorCodes.EXPRESSION_EVALUATION_PROBLEM, e );
			LOGGER.error( "Problem while parsing XML", eex );
			throw eex;
		}
		return expressionValue;
	}

	public static void main( String[] args ) throws Exception
	{
		URL url = new URL( "file:\\F:\\temp\\test.xml" );
		System.out.println( url.getFile() );
		File file = new File( url.getFile() );
		System.out.println( file.getPath() );

		Document doc = XMLUtils.parseXML( url, null );

		ValueObject value = XPathUtils.evaluateExpressionToValueObject( doc, "/new1/book/@name" );
		System.out.println( "value[" + value.getValue() + "]" );

		System.out
				.println( "valueXObject[" + XPathUtils.evaluateExpression( doc, "/new1/book/@name" ).toString() + "]" );
	}
}
