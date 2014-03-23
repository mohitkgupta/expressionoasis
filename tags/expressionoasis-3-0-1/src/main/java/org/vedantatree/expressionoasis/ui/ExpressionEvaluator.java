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

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.ExpressionEngine;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;


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
