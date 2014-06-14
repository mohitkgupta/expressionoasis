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
package org.vedantatree.expressionoasis.extensions;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.types.Type;
import org.vedantatree.expressionoasis.types.ValueObject;


/**
 * This class represents the interface for function providers for Expression
 * Engine. Object of this type will be resolving the functions for any
 * expression. It can support more than one functions.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public interface FunctionProvider
{

	/**
	 * It initialize the Function provider with expression context and also
	 * gives a chance to pre-initialize any internal states for operations
	 * 
	 * @param expressionContext Context of current expression evaluation process
	 */
	void initialize( ExpressionContext expressionContext ) throws ExpressionEngineException;

	/**
	 * Gets the return type of the function.
	 * 
	 * @param functionName the name of the function.
	 * @param parameterTypes the types of the parameters
	 * @return the return type of the function
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	Type getFunctionType( String functionName, Type[] parameterTypes ) throws ExpressionEngineException;

	/**
	 * Gets the return value of function.
	 * 
	 * @param functionName the name of the function
	 * @param parameters the parameters values of the function
	 * @return the return value of the function
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	ValueObject getFunctionValue( String functionName, ValueObject[] parameters ) throws ExpressionEngineException;

	/**
	 * Checks whether the this function provider supports any function or not.
	 * 
	 * @param functionName the name of the function
	 * @param parameterTypes the type of parameters for the function
	 * @return <code>true</code> if function provider supports the function <code>false</code> otherwise
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	boolean supportsFunction( String functionName, Type[] parameterTypes ) throws ExpressionEngineException;
}