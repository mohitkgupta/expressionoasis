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
package org.vedantatree.expressionoasis.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This exception is used by whole Expression Engine Component to share any
 * Erroneous information with user of the component, like at the time of parsing
 * or by the compiler while doing syntactical analysis.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 * 
 */
public class ExpressionEngineException extends Exception
{

	private static Log				LOGGER					= LogFactory.getLog( ExpressionEngineException.class );

	/**
	 * This is the serialization version for this class.
	 */
	private static final long		serialVersionUID		= 2006122401L;

	protected static final String	CUSTOMIZED_ERROR_CODE	= "Customized Error Code";

	private int						errorCode				= Integer.MIN_VALUE;

	/**
	 * Constructs the ExpressionEngineException
	 * 
	 * @param msg
	 *        the massege given by parser to tell the error.
	 */
	public ExpressionEngineException( String msg )
	{
		this( msg, -1, null );
	}

	/**
	 * Constructs the ExpressionEngineException
	 * 
	 * @param msg
	 *        the massege given by parser to tell the error.
	 * @param e Exception to wrap
	 */
	public ExpressionEngineException( String msg, Throwable e )
	{
		this( msg, -1, e );
	}

	/**
	 * Constructs the ExpressionEngineException
	 * 
	 * @param msg
	 *        the massege given by parser to tell the error
	 * @param errorCode code representing the error type
	 * @param e Exception to wrap
	 */
	public ExpressionEngineException( String msg, int errorCode, Throwable e )
	{
		this( msg, errorCode, null, false );
	}

	public ExpressionEngineException( String message, int errorCode, Throwable cause, boolean debug )
	{
		super( message, null );
		this.errorCode = errorCode;
		if( debug )
		{
			LOGGER.debug( getMessage(), cause );
		}
		else
		{
			LOGGER.error( getMessage(), cause );
		}
	}

	public final int getErrorCode()
	{
		return errorCode;
	}

	/*
	 * TODO: Need to use Resource Bundle here.
	 * Resource Bundle should be used on the basis of error codes.
	 * Message can be treated as error detail for technical usage.
	 * The string loaded from resource bundler using error code should be
	 * displayed to the user as error description.
	 */
	public String getMessage()
	{
		return super.getMessage() + ": error-code[" + errorCode + "] code-description[" + getErrorDescription() + "]";
	}

	protected String getErrorDescription()
	{
		return ( errorCode >= ErrorCodes.ERROR_DESCRIPTION.length || errorCode < 0 ) ? CUSTOMIZED_ERROR_CODE
				: ErrorCodes.ERROR_DESCRIPTION[errorCode];
	}

}