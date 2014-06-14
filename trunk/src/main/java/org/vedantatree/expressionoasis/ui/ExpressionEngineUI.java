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

import javax.swing.JFrame;


public class ExpressionEngineUI
{

	private JFrame		mainWindow;

	private KeyPad		keyPad;

	private ErrorPane	errorPane;

	private ResultPane	resultPane;

	public ExpressionEngineUI()
	{
		initializeUI();
	}

	KeyPad getKeyPad()
	{
		return keyPad;
	}

	ErrorPane getErrorPane()
	{
		return errorPane;
	}

	ResultPane getResultPane()
	{
		return resultPane;
	}

	private void initializeUI()
	{
		// create mainwindow
		// add closing listener
		// create new instance of keypad, errorpane, and result pane
		// add these to main window in required layout
		// probably border layout initially
		initializeListener();
	}

	private void initializeListener()
	{
		// initialize listener, if any
		// generally most of the listeners will be initialized
		// from inner components
	}

}
