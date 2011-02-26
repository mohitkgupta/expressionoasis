package org.ganges.expressionengine.ui;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.ExpressionEngine;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;

public class ExpressionEvaluator implements IExpressionEvaluator {

	private Object leftValue = null;
	private ExpressionContext expressionContext = null;

	public ExpressionEvaluator() {
		try {
			expressionContext = new ExpressionContext();
		} catch (ExpressionEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String evaluate(String expression) throws ExpressionEngineException {
		System.out.println("Expression [ " + expression + " ]");
		leftValue = ExpressionEngine.evaluate(expression, expressionContext);
		System.out.println("LeftValue  [ " + leftValue + " ]");
		return leftValue.toString();
	}

	public String getResult(String expression) {
		try {
			return evaluate(expression);
		} catch (ExpressionEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return WRONGINPUT;
		}
	}

}
