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
package org.vedantatree.expressionoasis.ui;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.ExpressionEngine;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;


public class ExpressionEvaluator implements IExpressionEvaluator
{

	private Object				leftValue			= null;
	private ExpressionContext	expressionContext	= null;

	public ExpressionEvaluator()
	{
		try
		{
			expressionContext = new ExpressionContext();
		}
		catch( ExpressionEngineException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String evaluate( String expression ) throws ExpressionEngineException
	{
		System.out.println( "Expression [ " + expression + " ]" );
		leftValue = ExpressionEngine.evaluate( expression, expressionContext );
		System.out.println( "LeftValue  [ " + leftValue + " ]" );
		return leftValue.toString();
	}

	public String getResult( String expression )
	{
		try
		{
			return evaluate( expression );
		}
		catch( ExpressionEngineException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return WRONGINPUT;
		}
	}

}
