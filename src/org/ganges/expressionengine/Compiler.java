/**
 * Created on Jan 2, 2006
 *
 * Copyright (c) 2005 Ganges Organisation all rights reserved.
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU General Public License as
 * published by the Free Software Foundation. See the GNU General Public License for more details.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.ganges.expressionengine;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.Expression;
import org.ganges.expressionengine.expressions.ExpressionFactory;
import org.ganges.expressionengine.grammar.DefaultXMLGrammar;
import org.ganges.expressionengine.grammar.ExpressionToken;
import org.ganges.expressionengine.grammar.Grammar;


/**
 * This class performs the compilation operation in expression evaluation 
 * process. 
 * <br>
 * It parses the expression using Parser, which returns it list of expression 
 * tokens as per rules specified with DefaultXMLGrammar. Compiler restructure these tokens
 * in Reverse Polish Notation and then create the Expression Object's tree for 
 * all the tokens.
 * 
 * TODO
 * 	Precedence can be stored with Token itself during parsing process - probably not much benefit as precedence is not used repeatedly
 *  can we set operator, function etc values to token itself from parsing process?
 *  Give example of expression compilation and expression tree
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public class Compiler {

	private static Log	LOGGER = LogFactory.getLog( Compiler.class );

	/**
	 * DefaultXMLGrammar instance used to parse the Expression by Parser
	 */
	private final Grammar grammar;

	/**
	 * Parser object used to parse the Expression
	 */
	private final Parser  parser;

	/**
	 * Constructs the Compiler with default DefaultXMLGrammar Instance
	 */
	public Compiler() {
		this( DefaultXMLGrammar.getInstance() );
	}

	/**
	 * Constructs the Compiler with specified DefaultXMLGrammar instance.
	 * 
	 * @param grammar the grammar using which compiler will parse the expression
	 * @throws IllegalArgumentException if the grammar object is null
	 */
	public Compiler( Grammar grammer ) {
		if( grammer == null ) {
			throw new IllegalArgumentException( "Grammer must not be null for the parser." );
		}

		this.grammar = grammer;
		this.parser = new Parser( grammer );
	}

	/**
	 * It compiles the expression string and prepare the tree of Expression
	 * Objects.
	 * 
	 * @param expression the expression string to compile
	 * @param expressionContext the object contains the contextual information,
	 * 		  which may be required for expression evaluation. It may be like 
	 * 		  an Java Bean in case of property expression 
	 * @return compiled expression, a tree of Expression Objects
	 * @throws ExpressionEngineException if unable to compile the expression
	 */
	public Expression compile( String expression, ExpressionContext expressionContext )
			throws ExpressionEngineException {

		List<ExpressionToken> expressionTokens = parser.parse( expression );
		Stack<ExpressionToken> tokensInRPN = restructureTokensInRPN( expressionTokens );
		return buildExpression( tokensInRPN, expressionContext );
	}

	/**
	 * It restructures the given list of Expression Tokens in 'Reverse Polish
	 * Notation'.
	 * 
	 * @param expressionTokensList list of Expression Tokens
	 * @return the stack of restructured Expression Tokens in 'RPN'
	 * @throws ExpressionEngineException if Expression Tokens are invalid
	 */
	protected Stack<ExpressionToken> restructureTokensInRPN( List<ExpressionToken> expressionTokensList )
			throws ExpressionEngineException {

		// stack to collect operators
		Stack<ExpressionToken> operatorStack = new Stack<ExpressionToken>();

		// stack to collect restructured expression tokens
		Stack<ExpressionToken> rpnStack = new Stack<ExpressionToken>();

		/*Pseudo code
		 * Iterate over tokens
		 * If current token is a operator
		 * 		check the tokens on operator stack till it get emptied
		 * 			if top token is an operator and its precedence is >= current token precedence
		 * 				remove from opstack and add to rpnstack 
		 * 		add current token to operator stack
		 * If current token is bracket
		 * 		If current token is left bracket
		 * 			add it to operator stack
		 * 		If current token is right bracket
		 * 			pop the tokens from operator stack till we get the left bracket
		 * 			and add these to rpn stack
		 * 			if we found left bracket
		 * 				create bracket pair and add to rpn stack
		 * Otherwise 
		 * 		push the token to rpn stack
		 * If operator stack is not empty
		 * 		empty it from top and push all tokens to rpn stack
		 * 
		 * RPN is ready!!		
		 */

		ExpressionToken lastToken = null;
		for( Iterator<ExpressionToken> iter = expressionTokensList.iterator(); iter.hasNext(); ) {
			ExpressionToken currentToken = (ExpressionToken) iter.next();

			// Put the operator/function on operator stack, 
			// and shift higher precedence operator to RPN stack from top

			if( grammar.isOperator( currentToken ) ) {

				// fix for bug#1 @ google code
				// handle unary operators, if unary operators are in the beginning of expression 
				if( operatorStack.isEmpty() && rpnStack.isEmpty() ) {
					operatorStack.push( new UnaryToken( currentToken ) );
				}
				// or if unary operator are after left bracket
				else if( grammar.isOperator( lastToken ) || grammar.isLeftBracket( lastToken )) {
					operatorStack.push( new UnaryToken( currentToken ) );
				}
				else {

					LOGGER.debug( "currentTokenPP[" + currentToken + "]" );
					int currentTokenPrecedence = grammar.getPrecedenceOrder( currentToken, isUnary( currentToken ) );

					// Remove high precedence operator from opStack and add to RPN stack
					while( !operatorStack.isEmpty() ) {
						ExpressionToken peekToken = (ExpressionToken) operatorStack.peek();

						// if the topmost token on stack is an operator
						// and its precedence is greater than precedence of current token
						// pop it from operator stack and add to RPN stack

						if( grammar.isOperator( peekToken )
								&& currentTokenPrecedence <= grammar
										.getPrecedenceOrder( peekToken, isUnary( peekToken ) ) ) {
							operatorStack.pop();
							rpnStack.push( peekToken );
						}
						else {
							break;
						}
					}

					operatorStack.push( currentToken );
				}
			}

			// Work for brackets
			else if( grammar.isBracket( currentToken ) ) {

				if( grammar.isLeftBracket( currentToken ) ) {
					// Push the bracket onto the opStack
					operatorStack.push( currentToken );
				}

				// Else pop the elements from opStack until left
				// bracket ffror this right bracket is found. 
				// throw an error if no left bracket is found
				else {
					boolean leftBracketFound = false;

					while( !operatorStack.isEmpty() ) {
						ExpressionToken peekOperator = (ExpressionToken) operatorStack.peek();
						leftBracketFound = grammar.isLeftBracket( peekOperator );
						operatorStack.pop();

						// TODO: collect more cases of unary and simple token and add more examples
						if( leftBracketFound ) {
							// Putting the bracket pair on rpnStack
							// to handle the array [] - binary, and function () - unary cases
							String value = peekOperator.getValue()
									+ grammar.getOppositeBracket( peekOperator.getValue() );
							ExpressionToken bracketToken = grammar.isUnary( value ) ? new UnaryToken( value,
									peekOperator.getIndex() ) : new ExpressionToken( value, peekOperator.getIndex() );
							rpnStack.push( bracketToken );

							break;
						}

						rpnStack.push( peekOperator );
					}

					if( !leftBracketFound ) {
						throw new ExpressionEngineException( "Left bracket is missing for \"" + currentToken.getValue()
								+ "\" at " + currentToken.getIndex() );
					}
				}
			}

			// Work for operands
			else {
				rpnStack.push( currentToken );
			}
			lastToken = currentToken;
		}

		// push rest of the tokens to RPN stack
		while( !operatorStack.isEmpty() ) {
			ExpressionToken element = (ExpressionToken) operatorStack.peek();

			// No enclosing bracket if any left bracket is found
			// Throw the error in this case.
			if( grammar.isLeftBracket( element.getValue() ) ) {
				throw new ExpressionEngineException( "Right bracket is missing for \"" + element.getValue() + "\" at "
						+ element.getIndex() );
			}

			operatorStack.pop();
			rpnStack.push( element );
		}

		return rpnStack;
	}

	/**
	 * It builds the tree of Expression objects using tokens from RPN Stack.
	 * 
	 * @param rpnTokens the stack of tokens in RPN structure
	 * @param expressionContext the object which may contain contextual information for expressions
	 * @return the tree of expression objects
	 * @throws ExpressionEngineException
	 */
	protected Expression buildExpression( Stack<ExpressionToken> rpnTokens, ExpressionContext expressionContext )
			throws ExpressionEngineException {

		Stack<Expression> expressionStack = new Stack<Expression>();
		int size = rpnTokens.size();

		for( int i = 0; i < size; i++ ) {
			ExpressionToken token = rpnTokens.get( i );

			// By default expression is for operand element.
			String type = ExpressionFactory.OPERAND;
			Object initializationParameters = token.getValue();

			// Expression for operator
			if( grammar.isOperator( token ) ) {

				// Expression for function/unary operator
				if( isUnary( token ) ) {
					type = grammar.isFunction( token.getValue() ) ? ExpressionFactory.FUNCTION
							: ExpressionFactory.UNARY;
					/*
					 * Earlier we were assuming that expressionStack must have
					 * at least one expression in case of Unary Expression. 
					 * Hence we were asserting for at least one expression in 
					 * expression stack here. But this assumption failed in case 
					 * of function call with zero arguments. So the assertion 
					 * has been removed now.
					 * 
					 * @see Bug ID: 1691820
					 */
					initializationParameters = expressionStack.size() == 0 ? null : expressionStack.pop();
				}
				else {
					if( expressionStack.size() < 2 ) {
						throw new ExpressionEngineException(
								"Something wrong while compiling expression, as expression stack has less than 2 expression in case of binray expression. expressionStack["
										+ expressionStack + "]" );
					}

					type = ExpressionFactory.BINARY;

					Expression rightExpression = expressionStack.pop();
					Expression leftExpression = expressionStack.pop();
					initializationParameters = new Expression[] { leftExpression, rightExpression };
				}
			}

			LOGGER.debug( "token[" + token + "]" );
			Expression expression = ExpressionFactory.getInstance().createExpression( token.getValue(), type );
			LOGGER.debug( "expression[" + expression + "]" );
			expressionContext.setContextProperty( "TOKEN", token.getValue() );
			expression.initialize( expressionContext, initializationParameters );
			expressionContext.setContextProperty( "TOKEN", null );
			expressionStack.push( expression );
		}

		if( expressionStack.size() != 1 ) {
			throw new ExpressionEngineException( "Unable to compile expression. Expression Stack size should be one here. expressionStackSize[" + expressionStack.size() + "] rpnTokens[" + rpnTokens + "]" );
		}

		return expressionStack.peek();
	}

	/**
	 * Checks whether the operator is used as unary operator or not.
	 * 
	 * @param token
	 * @return <code>true</code> if the token is used as unary
	 *         <code>false</code> otherwise.
	 */
	private boolean isUnary( ExpressionToken token ) {
		return token instanceof UnaryToken || grammar.isFunction( token ); // || grammar.isUnary( token );
	}

	/**
	 * This is just an indicator class of operator token that this operator is
	 * used as unary operator
	 */
	private class UnaryToken extends ExpressionToken {

		/**
		 * Constructs the UnaryToken
		 * 
		 * @param token
		 */
		public UnaryToken( ExpressionToken token ) {
			this( token.getValue(), token.getIndex() );
		}

		/**
		 * Constructs the UnaryToken
		 * 
		 * @param value
		 * @param index
		 */
		public UnaryToken( String value, int index ) {
			super( value, index );
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "{u:" + getValue() + ", " + getIndex() + "}";
		}
	}

	/** Not in use currently
	 * 
	 * Resolves the unary operator. Makes them as binary opertor with the same
	 * semantic as with unary operator.
	 * 
	 * @param tokenList
	 *            the list of tokens from expression.
	 * @return the list of expression with resolved unary operators.
	 * @throws ExpressionEngineException
	 *             if the expression is invalid or operator can be used as unary
	 *             while being used as unary in expression.
	 */

	/*
	 * Possible case for an operator to be an unary operator 1. Nothing, Unary
	 * Operator, Operand. 2. Operator, Unary Operator. 3. Bracket, Unary
	 * Operator.
	 */
	private List resolveUnaryOperator( List tokenList ) throws ExpressionEngineException {
		boolean wasOperator = true;
		int length = tokenList.size();

		for( int i = 0; i < length; i++ ) {
			ExpressionToken token = (ExpressionToken) tokenList.get( i );

			if( grammar.isOperator( token.getValue() ) ) {
				if( wasOperator ) {
					if( !grammar.isUnary( token.getValue() ) ) {
						throw new ExpressionEngineException( "The operator \"" + token.getValue()
								+ "\" can't be used unary at " + token.getIndex() );
					}

					tokenList.set( i, new UnaryToken( token ) );
				}

				wasOperator = true;
			}
			else if( grammar.isLeftBracket( token.getValue() ) ) {
				wasOperator = true;
			}
			else {
				wasOperator = false;
			}
		}

		return tokenList;
	}

}