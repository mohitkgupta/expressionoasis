/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
 * 
 * This file is part of ExpressionOasis.
 *
 * ExpressionOasis is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ExpressionOasis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.ui;

public interface IExpressionEvaluator {
	
	/**
	 * Generic/Numeric and Sign variables
	 */
	public static final String RESET = "reset";
	public static final String PLUS = "+";
	public static final String MINUS = "-";
	public static final String MULTIPLY = "*";
	public static final String DIVISION = "/";
	public static final String PLUSMINUS = "-/+";
	public static final String FRACTION = ".";
	public static final String EQUALS = "=";
	public static final String MODULUS = "%";
	public static final String CLEAR = "C";
	public static final String LEFTBRACE = "(";
	public static final String RIGHTBRACE = ")";
	
	/**
	 * Message Variables
	 */
	public static final String EXPRESSIONEVALUATOR = "Expression Evaluator";
	public static final String WRONGINPUT = "Please enter a valid expression";
	public static final String EXPRESSIONLABEL = "Expression :";
	public static final String RESULTLABEL = "Expression Result :";
	
	/**
	 * Function Varaibles
	 */
	public static final String ABS = "abs";  //Call getResult
	public static final String SIN = "sin"; //Call getResult
	public static final String COS = "cos";//Call getResult
	public static final String TAN = "tan";//Call getResult
	public static final String ASIN = "asin";//Call getResult
	public static final String ACOS = "acos";//Call getResult
	public static final String ATAN = "atan";//Call getResult
	public static final String ATAN2 = "atan2";//Call getResult
	public static final String EXP = "exp";//Call getResult
	public static final String LOG = "log";//Call getResult
	public static final String SQRT = "sqrt";//Call getResult
	public static final String CEIL = "ceil";//Call getResult
	public static final String FLOOR = "floor";//Call getResult
	public static final String RINT = "rint";//Call getResult
	public static final String ROUND = "round";//Call getResult
	public static final String RANDOM = "random";//Call getResult
	public static final String POW = "pow";
	public static final String MIN = "min";
	public static final String MAX = "max";
	
	public static final String SIGNEDLEFTSHIFT = "<<"; //call equals
	public static final String SIGNEDRIGHTSHIFT = ">>";//call equals
	public static final String UNSIGNEDRIGHTSHIFT = ">>>";//call equals
	public static final String BITWISEOR = "|";//call equals
	public static final String BITWISEAND = "&";//call equals
	public static final String BITWISECOMPLEMENT = "~";//call equals
	public static final String GREATERTHANEQUALTO = ">=";//call equals
	public static final String GREATERTHAN = ">";//call equals
	public static final String LESSTHANEQUALTO = "<=";//call equals
	public static final String LESSTHAN = "<";//call equals
	public static final String NOTEQUALTO = "!=";//call equals
	public static final String EQUALTO = "==";//call equals
	
	
	

}
