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
import org.simpleframework.xml.Root;


/**
 * Represents a constructur argument to be passed to a class defined
 * in a configuration file
 * 
 * @author Kris Marwood
 * @version 1.0
 */

@Root(name = "constructorArg")
public class ConstructorArgument
{

	@Attribute(name = "className")
	private String	className;

	@Attribute(name = "value")
	private String	value;

	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return className + " = " + value;
	}
}
