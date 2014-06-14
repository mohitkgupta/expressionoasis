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
package org.vedantatree.expressionoasis.extensions.customfunctions;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.extensions.DefaultFunctionProvider;


/**
 * Provides custom functions where the source code for the functions is stored as configuration
 * in order to make adding new functions very simple.
 * 
 * @author Kris Marwood
 * @version 1.0
 */
public class CustomFunctionProvider extends DefaultFunctionProvider
{

	/**
	 * Dynamically generates a class at run time based on the source code of methods
	 * provided by the sourceProvider, and makes the methods available to the
	 * expression engine as functions.
	 * 
	 * @param sourceProvider class that will provide the source code for the custom functions
	 * @throws ExpressionEngineException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public CustomFunctionProvider( Class<? extends CustomFunctionSourceProvider> sourceProvider )
			throws ExpressionEngineException
	{
		super( getCustomFunctionsClass( sourceProvider ) );
	}

	/**
	 * Makes the class that contains the custom functions
	 * 
	 * @param sourceProvider class that will provide the source code for the custom functions
	 * @return a class whose methods are the custom functions
	 */
	private static Class<? extends Object> getCustomFunctionsClass(
			Class<? extends CustomFunctionSourceProvider> sourceProvider )
	{
		CustomFunctionSourceProvider provider = null;

		try
		{
			provider = (CustomFunctionSourceProvider) sourceProvider.newInstance();
		}
		catch( InstantiationException e )
		{
			throw new RuntimeException( "Error creating custom functions class: " + e.getMessage(), e );
		}
		catch( IllegalAccessException e )
		{
			throw new RuntimeException( "Error creating custom functions class: " + e.getMessage(), e );
		}

		return CustomFunctionsClassFactory.getCustomFunctionsClass( provider );
	}
}
