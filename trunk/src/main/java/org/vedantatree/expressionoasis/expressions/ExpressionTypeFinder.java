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
package org.vedantatree.expressionoasis.expressions;

import java.util.LinkedHashSet;


/**
 * Use to find sub-expressions of a particular type within a given expression.
 * For example, the following would extract a set of variables (IdentifierExpressions)
 * from an expression:
 * 
 * ExpressionTypeFinder finder = new ExpressionTypeFinder(someExpression, IdentifierExpression.class);
 * Set<Expression> variables = finder.getExpressions();
 * 
 * @author Kris Marwood
 * @version 1.0
 */
public class ExpressionTypeFinder implements ExpressionVisitor
{

	/* the expression to search to find sub-expressions */
	private final Expression				expressionToSearch;

	/* the type of sub-expression to search for */
	private final Class						expressionTypeToFind;

	/* the sub-expressions found within the expression */
	private final LinkedHashSet<Expression>	foundExpressions;

	/**
	 * Creates a new ExpressionTypeFinder
	 * 
	 * @param expressionToSearch the expression to search to find sub-expressions
	 * @param expressionTypeToFind the type of the sub-expressions to search
	 */
	public ExpressionTypeFinder( Expression expressionToSearch, Class expressionTypeToFind )
	{
		if( expressionToSearch == null )
		{
			throw new IllegalArgumentException( "expressionToSearch must not be null" );
		}
		if( expressionTypeToFind == null )
		{
			throw new IllegalArgumentException( "expressionTypeToFind must not be null" );
		}
		if( !Expression.class.isAssignableFrom( expressionTypeToFind ) )
		{
			throw new java.lang.IllegalArgumentException( "expressionTypeToFind must be an Expression class" );
		}

		this.expressionTypeToFind = expressionTypeToFind;
		this.expressionToSearch = expressionToSearch;

		foundExpressions = new LinkedHashSet<Expression>();
	}

	/**
	 * Implementation of the visitor design pattern. An expression will call this
	 * with itself as the <code>expression</code> parameter.
	 * 
	 * @param expression
	 */
	public void visit( Expression expression )
	{
		if( expression.getClass().equals( expressionTypeToFind ) )
		{
			foundExpressions.add( expression );
		}
	}

	/**
	 * Gets a set of subexpressions of a given type
	 * 
	 * @return a set of expressions of type <code>expressionTypeToFind</code> contained within
	 *         <code>expressionToSearch</code>
	 */
	public LinkedHashSet<Expression> getExpressions()
	{
		expressionToSearch.accept( this );
		return foundExpressions;
	}
}
