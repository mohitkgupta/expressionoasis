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
package org.vedantatree.expressionoasis.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


/**
 * Represents the configuration for an Expression
 * 
 * @author Kris Marwood
 */
@Element(name = "expression")
public class ExpressionConfig
{

	@Attribute(name = "name")
	private String	expressionName;

	@Attribute(name = "className")
	private String	className;

	@Attribute(name = "type")
	private String	expressionType;

	/**
	 * @return the expressionName
	 */
	public String getExpressionName()
	{
		return expressionName;
	}

	/**
	 * @return the className
	 */
	public Class getExpressionClass()
	{
		Class expressionClass = null;
		try
		{
			expressionClass = Class.forName( className );
		}
		catch( ClassNotFoundException e )
		{
			throw new RuntimeException( "Error loading expression class[" + className + "]", e );
		}
		return expressionClass;
	}

	/**
	 * @return the expressionType
	 */
	public String getExpressionType()
	{
		return expressionType;
	}
}
