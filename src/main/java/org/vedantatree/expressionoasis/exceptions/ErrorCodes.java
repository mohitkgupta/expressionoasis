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

/**
 * @author Mohit Gupta
 */
public interface ErrorCodes
{

	static final int		UNKNOWN_PROBLEM					= 1;

	static final int		SERVER_PROBLEM					= 2;

	static final int		USER_PROBLEM					= 3;

	static final int		PERSISTENCE_SYSTEM_PROBLEM		= 4;

	static final int		IO_PROBLEM						= 5;

	static final int		RESOURCE_NOT_FOUND				= 6;

	static final int		PARSING_PROBLEM					= 7;

	static final int		EXPRESSION_EVALUATION_PROBLEM	= 7;

	static final String[]	ERROR_DESCRIPTION				=
															{ "Unknow Problem", "Server Internal Problem",
			"User Created Probelm", "Persistence System Problem", "IO Problem", "Resources not found",
			"Parsing Problem"								};

}