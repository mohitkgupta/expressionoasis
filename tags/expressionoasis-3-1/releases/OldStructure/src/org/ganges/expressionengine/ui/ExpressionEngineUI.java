package org.ganges.expressionengine.ui;

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
