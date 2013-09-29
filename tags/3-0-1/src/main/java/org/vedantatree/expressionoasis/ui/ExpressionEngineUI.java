/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
 * 
 * This file is part of ExpressionOasis.
 *
 * ExpressionOasis is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ExpressionOasis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.ui;

import javax.swing.JFrame;


public class ExpressionEngineUI {

	private JFrame	 mainWindow;

	private KeyPad	 keyPad;

	private ErrorPane  errorPane;

	private ResultPane resultPane;
	

	public ExpressionEngineUI() {
		initializeUI();
	}

	KeyPad getKeyPad() {
		return keyPad;
	}

	ErrorPane getErrorPane() {
		return errorPane;
	}

	ResultPane getResultPane() {
		return resultPane;
	}

	private void initializeUI() {
		// create mainwindow
		// add closing listener
		// create new instance of keypad, errorpane, and result pane
		// add these to main window in required layout
		// probably border layout initially
		initializeListener();
	}

	private void initializeListener() {
		// initialize listener, if any
		// generally most of the listeners will be initialized 
		// from inner components
	}

}
