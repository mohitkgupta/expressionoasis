/**
 * Copyright (c) 2006 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.grammar;

/**
 * Object of this class provides the grammar rules for parsing the expression.
 * Parser uses this class to parse the string expression in list of Expression
 * Tokens, and Compiler uses it to build the expression tree.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public interface Grammar
{

	/**
	 * Checks whether the token is a delimiter or not.
	 * 
	 * @param token the token
	 * @return <code>true</code> if the token is delimiter <code>false</code> otherwise.
	 */
	boolean isDelimiter( ExpressionToken token );

	/**
	 * Checks whether the token is a delimiter or not.
	 * 
	 * @param token the token
	 * @return <code>true</code> if the token is delimiter <code>false</code> otherwise.
	 */
	boolean isDelimiter( String token );

	/**
	 * Checks whether the given token is approachable using any of the pattern
	 * or not.
	 * 
	 * Given token can be partially or fully constructed token during
	 * parsing process. Parser generally calls this method to check whether the
	 * current token can be combined with next character of expression to form
	 * some meaningful token or not. If not, then it utilize the existing
	 * collected characters as one token, otherwise it keep collecting
	 * characters.
	 * 
	 * @param token the token, partially or full constructed, to check whether
	 *        it can approach to any expression token pattern or not.
	 * @return <code>true</code> if the token pattern is approachable <code>false</code> otherwise.
	 */
	boolean isApproachable( String token );

	/**
	 * Checks whether the token is allowed or not.
	 * 
	 * A token is fully constructed token. Parser generally calls this method to
	 * check whether the current token is a valid token as per the production
	 * rules or not.
	 * 
	 * @param token the token which is to be checked for its validity
	 * @return <code>true</code> if the token is allowed <code>false</code> otherwise.
	 */
	boolean isAllowed( String token );

	/**
	 * Checks whether the given token is an operator or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is an operator <code>false</code> otherwise
	 */
	boolean isOperator( ExpressionToken token );

	/**
	 * Checks whether the given token is an operator or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is an operator <code>false</code> otherwise
	 */
	boolean isOperator( String token );

	/**
	 * Checks whether the token is an function or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is an function <code>false</code> otherwise
	 */
	boolean isFunction( ExpressionToken token );

	/**
	 * Checks whether the token is an function or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is an function <code>false</code> otherwise
	 */
	boolean isFunction( String token );

	/**
	 * Use this method to add any function identification to the grammar.
	 * 
	 * Grammar implementation will generally load the function identification itself from some configuration file. Like,
	 * in case of DefaultXMLGrammar implementation, it is loaded from grammar.xml. However, developer may opt to add
	 * functions using API also.
	 * 
	 * @param functionName name of the function to add
	 */
	void addFunction( String functionName );

	/**
	 * Checks whether the given token is a binary operator or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is a binary operator <code>false</code> otherwise
	 */
	boolean isBinaryOperator( ExpressionToken token );

	/**
	 * Checks whether the given token is a binary operator or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is a binary operator <code>false</code> otherwise
	 */
	boolean isBinaryOperator( String token );

	/**
	 * Checks whether the given operator is a unary operator or not.
	 * 
	 * @param operator the operator to check
	 * @return <code>true</code> if the operator is used as unary operator <code>false</code> otherwise.
	 */
	boolean isUnary( ExpressionToken operator );

	/**
	 * Checks whether the given operator is a unary operator or not.
	 * 
	 * @param operator the operator to check
	 * @return <code>true</code> if the operator is used as unary operator <code>false</code> otherwise.
	 */
	boolean isUnary( String operator );

	/**
	 * Gets the precedence order of the given operator
	 * 
	 * @param operator the operator to check for precedence
	 * @param isUnary true if the operator is unary, as an operator can behave
	 *        either as unary or as binary
	 * @return the precedence order of the operator
	 */
	int getPrecedenceOrder( ExpressionToken operator, boolean isUnary );

	/**
	 * Gets the precedence order of the given operator
	 * 
	 * @param operator the operator to check for precedence
	 * @param isUnary true if the operator is unary, as an operator can behave
	 *        either as unary or as binary
	 * @return the precedence order of the operator
	 */
	int getPrecedenceOrder( String operator, boolean isUnary );

	/**
	 * Checks whether the token is a left bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token can be used as left bracket <code>false</code> otherwise.
	 */
	boolean isLeftBracket( ExpressionToken token );

	/**
	 * Checks whether the token is a left bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token can be used as left bracket <code>false</code> otherwise.
	 */
	boolean isLeftBracket( String token );

	/**
	 * Checks whether the token is a right bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token can be used as right bracket <code>false</code> otherwise.
	 */
	boolean isRightBracket( ExpressionToken token );

	/**
	 * Checks whether the token is a right bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token can be used as right bracket <code>false</code> otherwise.
	 */
	boolean isRightBracket( String token );

	/**
	 * Check whether the given token is a bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is a bracket <code>false</code> otherwise
	 */
	boolean isBracket( ExpressionToken token );

	/**
	 * Check whether the given token is a bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is a bracket <code>false</code> otherwise
	 */
	boolean isBracket( String token );

	/**
	 * Returns the opposite bracket for the given bracket.
	 * 
	 * @param bracket the bracket for which we need to find the opposite bracket
	 * @return the opposite part of bracket w.r.t given bracket
	 */
	String getOppositeBracket( String bracket );

	/**
	 * Checks whether to ignore the blanks in expression or not. It tells the
	 * parser whether to exclude the extra blanks while parsing or not.
	 * 
	 * @return <code>true</code> if parser wants to exclude the blanks <code>false</code> otherwise.
	 */
	boolean isIgnoreBlank();
}