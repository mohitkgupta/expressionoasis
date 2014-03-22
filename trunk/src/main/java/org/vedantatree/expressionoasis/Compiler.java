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
package org.vedantatree.expressionoasis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vedantatree.expressionoasis.config.ConfigFactory;
import org.vedantatree.expressionoasis.config.ExpressionOasisConfig;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.Expression;
import org.vedantatree.expressionoasis.expressions.ExpressionFactory;
import org.vedantatree.expressionoasis.grammar.DefaultXMLGrammar;
import org.vedantatree.expressionoasis.grammar.ExpressionToken;
import org.vedantatree.expressionoasis.grammar.Grammar;


/**
 * This class performs the compilation operation in expression evaluation
 * process. <br>
 * It parses the expression using Parser, which returns it list of expression
 * tokens as per rules specified with DefaultXMLGrammar. Compiler restructure these tokens
 * in Reverse Polish Notation and then create the Expression Object's tree for
 * all the tokens.
 * 
 * TODO
 * Precedence can be stored with Token itself during parsing process - probably not much benefit as precedence is not
 * used repeatedly
 * can we set operator, function etc values to token itself from parsing process?
 * Give example of expression compilation and expression tree
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 * 
 *          Added support for caching of compiled expressions to improve performance for applications
 *          which repeatedly evaluate the same expressions against a different set of variables.
 *          This can be turned on or off in the XML config file.
 * 
 *          Modified to use collections that are not synchronized in order to reduce monitor conention.
 *          Still uses Stack, which wraps Vector, which is synchronized. Consider using another Stack
 *          implementation such as Google Collections.
 * 
 * @author Kris Marwood
 * @version 1.1
 * 
 * 
 *          Made Grammar Configurable from config.xml. Now Grammar class can be defined in config.xml and Compiler will
 *          pick it from there. It adds facility to use any custom Grammar object
 * 
 * @author Mohit Gupta
 * @version 1.2
 * @since March 2014
 */
public class Compiler
{

	private static Log									LOGGER	= LogFactory.getLog( Compiler.class );

	/**
	 * DefaultXMLGrammar instance used to parse the Expression by Parser
	 */
	private final Grammar								grammar;

	/**
	 * Parser object used to parse the Expression
	 */
	private final Parser								parser;

	/**
	 * Specifies whether compiled RPN tokens for a given expression string should be cached.
	 * Some applications will be unlikely to re-evaluate the same expression strings, in which
	 * case caching would be a waste of resources. Other applications may re-evaluate the same
	 * expressions repeatedly, making recompilation wasteful.
	 */
	private final boolean								expressionCachingEnabled;

	/**
	 * Cache of RPN tokens produced by the parser / compiler for expressions
	 */
	private final Map<String, Stack<ExpressionToken>>	compiledExpressionRPNTokenCache;

	/**
	 * Constructs the Compiler with default DefaultXMLGrammar Instance
	 */
	public Compiler()
	{
		this( ConfigFactory.getConfig().getGrammar() );
	}

	/**
	 * Constructs the Compiler with specified DefaultXMLGrammar instance.
	 * 
	 * @param grammar the grammar using which compiler will parse the expression
	 * @throws IllegalArgumentException if the grammar object is null
	 */
	public Compiler( Grammar grammar )
	{
		if( grammar == null )
		{
			throw new IllegalArgumentException( "Grammar must not be null for the parser." );
		}

		this.grammar = grammar;
		this.parser = new Parser( grammar );

		expressionCachingEnabled = ConfigFactory.getConfig().shouldCacheCompiledExpressions();

		LOGGER.debug( "expression-caching-enabled[" + expressionCachingEnabled + "]" );
		compiledExpressionRPNTokenCache = expressionCachingEnabled ? new HashMap<String, Stack<ExpressionToken>>()
				: null;
	}
	
	Grammar getGrammar ()
	{
		return grammar;
	}

	/**
	 * Retrieves a Stack of tokens in RPN that represents the expression
	 * 
	 * @param expression the expression to parse
	 * @return the stack of restructured Expression Tokens in 'RPN'
	 * @throws ExpressionEngineException
	 */
	protected Stack<ExpressionToken> getTokensInRPN( String expression ) throws ExpressionEngineException
	{
		Stack<ExpressionToken> tokensInRPN = null;

		if( expressionCachingEnabled )
		{
			tokensInRPN = compiledExpressionRPNTokenCache.get( expression );
		}

		if( tokensInRPN == null )
		{
			List<ExpressionToken> expressionTokens = parser.parse( expression );
			tokensInRPN = restructureTokensInRPN( expressionTokens );

			if( expressionCachingEnabled )
			{
				compiledExpressionRPNTokenCache.put( expression, tokensInRPN );
			}
		}
		return tokensInRPN;
	}

	/**
	 * It restructures the given list of Expression Tokens in 'Reverse Polish
	 * Notation'.
	 * 
	 * @param expressionTokensList list of Expression Tokens
	 * @return the stack of restructured Expression Tokens in 'RPN'
	 * @throws ExpressionEngineException if Expression Tokens are invalid
	 */
	private Stack<ExpressionToken> restructureTokensInRPN( List<ExpressionToken> expressionTokensList )
			throws ExpressionEngineException
	{

		// stack to collect operators
		Stack<ExpressionToken> operatorStack = new Stack<ExpressionToken>();

		// stack to collect restructured expression tokens
		Stack<ExpressionToken> rpnStack = new Stack<ExpressionToken>();

		/*
		 * Pseudo code
		 * Iterate over tokens
		 * If current token is a operator
		 * check the tokens on operator stack till it get emptied
		 * if top token is an operator and its precedence is >= current token precedence
		 * remove from opstack and add to rpnstack
		 * add current token to operator stack
		 * If current token is bracket
		 * If current token is left bracket
		 * add it to operator stack
		 * If current token is right bracket
		 * pop the tokens from operator stack till we get the left bracket
		 * and add these to rpn stack
		 * if we found left bracket
		 * create bracket pair and add to rpn stack
		 * Otherwise
		 * push the token to rpn stack
		 * If operator stack is not empty
		 * empty it from top and push all tokens to rpn stack
		 * 
		 * RPN is ready!!
		 */

		ExpressionToken lastToken = null;
		for( Iterator<ExpressionToken> iter = expressionTokensList.iterator(); iter.hasNext(); )
		{
			ExpressionToken currentToken = (ExpressionToken) iter.next();

			// Put the operator/function on operator stack,
			// and shift higher precedence operator to RPN stack from top

			if( grammar.isOperator( currentToken ) )
			{

				// fix for bug#1 @ googlecode
				// handle unary operators, if unary operators are in the beginning of expression
				if( operatorStack.isEmpty() && rpnStack.isEmpty() )
				{
					operatorStack.push( new UnaryToken( currentToken ) );
				}
				// or if unary operator are after left bracket
				else if( grammar.isOperator( lastToken ) || grammar.isLeftBracket( lastToken ) )
				{
					operatorStack.push( new UnaryToken( currentToken ) );
				}
				else
				{

					LOGGER.debug( "currentTokenPP[" + currentToken + "]" );
					int currentTokenPrecedence = grammar.getPrecedenceOrder( currentToken, isUnary( currentToken ) );

					// Remove high precedence operator from opStack and add to RPN stack
					while( !operatorStack.isEmpty() )
					{
						ExpressionToken peekToken = (ExpressionToken) operatorStack.peek();

						// if the topmost token on stack is an operator
						// and its precedence is greater than precedence of current token
						// pop it from operator stack and add to RPN stack

						if( grammar.isOperator( peekToken )
								&& currentTokenPrecedence <= grammar
										.getPrecedenceOrder( peekToken, isUnary( peekToken ) ) )
						{
							operatorStack.pop();
							rpnStack.push( peekToken );
						}
						else
						{
							break;
						}
					}

					operatorStack.push( currentToken );
				}
			}

			// Work for brackets
			else if( grammar.isBracket( currentToken ) )
			{

				if( grammar.isLeftBracket( currentToken ) )
				{
					// Push the bracket onto the opStack
					operatorStack.push( currentToken );
				}

				// Else pop the elements from opStack until left
				// bracket ffror this right bracket is found.
				// throw an error if no left bracket is found
				else
				{
					boolean leftBracketFound = false;

					while( !operatorStack.isEmpty() )
					{
						ExpressionToken peekOperator = (ExpressionToken) operatorStack.peek();
						leftBracketFound = grammar.isLeftBracket( peekOperator );
						operatorStack.pop();

						// TODO: collect more cases of unary and simple token and add more examples
						if( leftBracketFound )
						{
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

					if( !leftBracketFound )
					{
						throw new ExpressionEngineException( "Left bracket is missing for \"" + currentToken.getValue()
								+ "\" at " + currentToken.getIndex() );
					}
				}
			}

			// Work for operands
			else
			{
				rpnStack.push( currentToken );
			}
			lastToken = currentToken;
		}

		// push rest of the tokens to RPN stack
		while( !operatorStack.isEmpty() )
		{
			ExpressionToken element = (ExpressionToken) operatorStack.peek();

			// No enclosing bracket if any left bracket is found
			// Throw the error in this case.
			if( grammar.isLeftBracket( element.getValue() ) )
			{
				throw new ExpressionEngineException( "Right bracket is missing for \"" + element.getValue() + "\" at "
						+ element.getIndex() );
			}

			operatorStack.pop();
			rpnStack.push( element );
		}

		return rpnStack;
	}

	/**
	 * Builds a tree of Expression objects representing the expression.
	 * 
	 * @param expression the string representing the expression to build
	 * @param expressionContext the object which may contain contextual information for expressions
	 * @return the tree of expression objects
	 * @throws ExpressionEngineException
	 */
	public Expression compile( String expression, ExpressionContext expressionContext, boolean validate )
			throws ExpressionEngineException
	{

		Stack<ExpressionToken> rpnTokens = getTokensInRPN( expression );

		Stack<Expression> expressionStack = new Stack<Expression>();
		int size = rpnTokens.size();

		for( int i = 0; i < size; i++ )
		{
			ExpressionToken token = rpnTokens.get( i );

			// By default expression is for operand element.
			String type = ExpressionFactory.OPERAND;
			Object initializationParameters = token.getValue();

			// Expression for operator
			if( grammar.isOperator( token ) )
			{

				// Expression for function/unary operator
				if( isUnary( token ) )
				{
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
					 * @see Bug ID: 1691820 @ sourceforge
					 */
					initializationParameters = expressionStack.size() == 0 ? null : expressionStack.pop();
				}
				else
				{
					if( expressionStack.size() < 2 )
					{
						throw new ExpressionEngineException(
								"Something wrong while compiling expression, as expression stack has less than 2 expression in case of binray expression. expressionStack["
										+ expressionStack + "]" );
					}

					type = ExpressionFactory.BINARY;

					Expression rightExpression = expressionStack.pop();
					Expression leftExpression = expressionStack.pop();
					initializationParameters = new Expression[]
					{ leftExpression, rightExpression };
				}
			}

			if( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "token[" + token + "]" );
			}
			Expression compiledExpression = ExpressionFactory.getInstance().createExpression( token.getValue(), type );
			if( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "expression[" + compiledExpression + "]" );
			}
			expressionContext
					.setContextProperty( ExpressionEngineConstants.EXPRESSION_CONTENXT_TOKEN, token.getValue() );
			compiledExpression.initialize( expressionContext, initializationParameters, validate );
			expressionContext.setContextProperty( ExpressionEngineConstants.EXPRESSION_CONTENXT_TOKEN, null );
			expressionStack.push( compiledExpression );
		}

		if( expressionStack.size() != 1 )
		{
			throw new ExpressionEngineException(
					"Unable to compile expression. Expression Stack size should be one here. expressionStackSize["
							+ expressionStack.size() + "] rpnTokens[" + rpnTokens + "]" );
		}

		return expressionStack.peek();
	}

	/**
	 * Checks whether the operator is used as unary operator or not.
	 * 
	 * @param token
	 * @return <code>true</code> if the token is used as unary <code>false</code> otherwise.
	 */
	private boolean isUnary( ExpressionToken token )
	{
		return token instanceof UnaryToken || grammar.isFunction( token ); // || grammar.isUnary( token );
	}

	/**
	 * This is just an indicator class of operator token that this operator is
	 * used as unary operator
	 */
	private class UnaryToken extends ExpressionToken
	{

		/**
		 * Constructs the UnaryToken
		 * 
		 * @param token
		 */
		public UnaryToken( ExpressionToken token )
		{
			this( token.getValue(), token.getIndex() );
		}

		/**
		 * Constructs the UnaryToken
		 * 
		 * @param value
		 * @param index
		 */
		public UnaryToken( String value, int index )
		{
			super( value, index );
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "{u:" + getValue() + ", " + getIndex() + "}";
		}
	}

	/**
	 * Not in use currently
	 * 
	 * Resolves the unary operator. Makes them as binary opertor with the same
	 * semantic as with unary operator.
	 * 
	 * @param tokenList
	 *        the list of tokens from expression.
	 * @return the list of expression with resolved unary operators.
	 * @throws ExpressionEngineException
	 *         if the expression is invalid or operator can be used as unary
	 *         while being used as unary in expression.
	 */

	/*
	 * Possible case for an operator to be an unary operator 1. Nothing, Unary
	 * Operator, Operand. 2. Operator, Unary Operator. 3. Bracket, Unary
	 * Operator.
	 */
	private List<ExpressionToken> resolveUnaryOperator( List<ExpressionToken> tokenList )
			throws ExpressionEngineException
	{
		boolean wasOperator = true;
		int length = tokenList.size();

		for( int i = 0; i < length; i++ )
		{
			ExpressionToken token = (ExpressionToken) tokenList.get( i );

			if( grammar.isOperator( token.getValue() ) )
			{
				if( wasOperator )
				{
					if( !grammar.isUnary( token.getValue() ) )
					{
						throw new ExpressionEngineException( "The operator \"" + token.getValue()
								+ "\" can't be used unary at " + token.getIndex() );
					}

					tokenList.set( i, new UnaryToken( token ) );
				}

				wasOperator = true;
			}
			else if( grammar.isLeftBracket( token.getValue() ) )
			{
				wasOperator = true;
			}
			else
			{
				wasOperator = false;
			}
		}

		return tokenList;
	}

}