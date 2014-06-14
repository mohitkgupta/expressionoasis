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
package org.vedantatree.expressionoasis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.grammar.DefaultXMLGrammar;
import org.vedantatree.expressionoasis.grammar.ExpressionToken;
import org.vedantatree.expressionoasis.grammar.Grammar;
import org.vedantatree.expressionoasis.utils.StringUtils;


/**
 * This parser object parses the expressions, break these in expression
 * tokens and perform lexical analysis on these tokens. All kind of expressions
 * can be parsed like arithmetic, function call, and property accessor etc.
 * 
 * By default, it ignores the extra blanks.
 * 
 * <br/>
 * <br/>
 * Some examples of expression
 * <hr/>
 * <br/>
 * 1). <i>123 + 124 - 67 * 45 div 90 + (12 + 34 div 67) </i>. <br/>
 * 2). <i>principal * pow(( 1 - rate div 100 ), time) </i>. <br/>
 * 3). <i>120 * pow(sin(20) div tan(30), 2) </i>. <br/>
 * 4). <i>values[0] + values[1] + values[2] + 120 * 45 div num[1][1] </i>. <br/>
 * 5). <i>person.address.city.name + students[0].rollNo </i>. <br/>
 * 6). <i> /person/address/city/name + /person/children[name='Ram'] + /person/children[1] </i>. <br/>
 * 7). <i>abc=xyz || 1!=2 && 3 >= xyz && xvz <= 30 </i>. <br/>
 * <br/>
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 * 
 *          Wrapped logger debug/trace calls with checks to see if debugging/tracing is enabled,
 *          as they were causing significant performance issues unnecessarily.
 * 
 * @author Kris Marwood
 * @version 1.1
 */
public final class Parser
{

	private static Log		LOGGER	= LogFactory.getLog( Parser.class );

	/**
	 * DefaultXMLGrammar object used to parse the expression
	 */
	private final Grammar	grammar;

	/**
	 * Constructs the Parser with default DefaultXMLGrammar instance
	 * 
	 * @deprecated seems of no use, since Parser is always created from Compiler with Grammar instance
	 */
	public Parser()
	{
		// this( DefaultXMLGrammar.getInstance() );
		throw new UnsupportedOperationException(
				"This constructor is no longer supported, since Parse is always initiated from Compiler with Grammar instance" );
	}

	/**
	 * Constructs the Parser with given DefaultXMLGrammar object
	 * 
	 * @param grammar
	 *        the grammar using which it parse the expression.
	 * @throws IllegalArgumentException
	 *         if the grammar is null.
	 */
	public Parser( Grammar grammar )
	{
		if( grammar == null )
		{
			throw new IllegalArgumentException( "DefaultXMLGrammar object must not be null for the parser." );
		}

		this.grammar = grammar;
	}

	/**
	 * It parses the given expression string into ExpressionToken objects and
	 * returns the list of ExpressionTokens.
	 * 
	 * @param expression the expression string to parse
	 * @return list of ExpressionToken
	 * @throws ExpressionEngineException if unable to parse
	 * @throws IllegalArgumentException
	 *         if the expression is not a qualified string
	 */
	public final List<ExpressionToken> parse( String expression ) throws ExpressionEngineException
	{
		if( !StringUtils.isQualifiedString( expression ) )
		{
			throw new ExpressionEngineException( "Passed expression is not a qualified text." );
		}

		List<ExpressionToken> tokenList = new ArrayList<ExpressionToken>();
		// TODO: probably can pass pointer as 1, because it will enable to get first token in first pass
		parse( expression, tokenList, 0, 0 );
		return tokenList;
	}

	/**
	 * It parses the expression, break it in list of tokens as per DefaultXMLGrammar rules
	 * 
	 * @param expression the expression string to parse
	 * @param tokenList the list of tokens.
	 * @param currentPosition this is the pointer which represents the current position
	 *        of parser cursor in expression string during parsing
	 * @param offset this is the start position of token in expression string
	 *        during parsing. It resets to next position after finding a
	 *        complete token, and start increasing again to find the next token
	 *        end
	 * @throws ExpressionEngineException if unable to parse.
	 */
	private void parse( String expression, List<ExpressionToken> tokenList, int currentPosition, int offset )
			throws ExpressionEngineException
	{

		if( LOGGER.isTraceEnabled() )
		{
			LOGGER.trace( "expression[" + expression + "] pointer[" + currentPosition + "] offset[" + offset + "]" );
		}

		/*
		 * pseudo code
		 * 
		 * Get next token character from the expression (from current pointer)
		 * 
		 * If the token character is a delimiter and it can not be combined with
		 * last token to form a valid token
		 * Extract the current token from expression,
		 * FINALIZE the token by adding it to tokens list,
		 * and reset the offset to start position of next token
		 * 
		 * If this token is a delimiter and it can be combined with last added
		 * token to form a valid delimiter,
		 * do this
		 * It is to support delimiters with multiple characters
		 * 
		 * If current token character is not a delimiter
		 * And previous token is a delimiter
		 * And combination of these is approachable by grammar rules
		 * reset the offset to the previous token start position
		 * so that next parsing cycle can add both in one token
		 * 
		 * If there are more characters in expression string
		 * which are not yet parsed
		 * call the parse cycle recursively
		 * else
		 * give a chance if any last token can be formed by using
		 * current offset and pointer
		 */

		// next token
		String currentExpressionCharacter = expression.substring( currentPosition, currentPosition + 1 );

		if( LOGGER.isDebugEnabled() )
		{
			LOGGER.debug( "next-token[" + currentExpressionCharacter + "] currentToken["
					+ getCurrentToken( expression, offset, currentPosition ) + "] + lastToken["
					+ getLastToken( tokenList ) + "]" );
		}

		int index = offset;

		// Checks whether the current character is a delimiter
		// and it can not form a valid token by adding to current token in expression string
		// Process it as delimiter
		// Here we extract the last token contents from expression string
		// and add these to token list as a token
		// Then we check if last token was a delimiter and current delimiter can be added to it to form a valid
		// dilimiter
		// If yes, then combine these and create new token, add to token list
		// Otherwise add the current delimiter as a token to the token list
		// Refer to grammar.xml to understand delimiters

		if( grammar.isDelimiter( currentExpressionCharacter )
				&& !grammar.isApproachable( getCurrentToken( expression, offset, currentPosition )
						+ currentExpressionCharacter ) )
		{

			// get current token from the list
			String currentToken = getCurrentToken( expression, offset, currentPosition );

			if( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "delimiter+notApproachable-currentToken[" + currentToken + "]" );
			}

			// if current token string is valid, add new token to tokens list
			// Hence put the delimiter character as token

			if( StringUtils.isQualifiedString( currentToken ) )
			{
				checkValid( currentToken, offset );
				tokenList.add( new ExpressionToken( currentToken, index ) );

				if( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "token-added[" + tokenList + "]" );
				}
			}

			offset = currentPosition + 1;

			if( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "increased-offset[" + offset + "]" );
			}

			// Formation of multiple character delimiters
			// This is to support operator with more than one character.
			// If the previously added token is a delimiter,
			// And if a valid delimiter can be formed
			// by concatenating the next token/character with previously added token
			// Concatenate these into one token

			if( isPreviousTokenADelimiter( tokenList ) )
			{

				// Get previously added token
				int lastIndex = tokenList.size() - 1;
				String previousToken = ( tokenList.get( lastIndex ) ).getValue();

				// Add previously added token value to next token value
				String temp = previousToken + currentExpressionCharacter;

				if( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "previousTokenDelimiter-previousToken[" + previousToken + "] temp[" + temp + "]" );
				}

				// if this combination can be qualified as a delimiter
				// remove last token from token list and add the new token,
				// built by adding current token to last token, to the token list

				if( grammar.isDelimiter( temp ) )
				{
					index -= previousToken.length();
					currentExpressionCharacter = temp;
					ExpressionToken lastToken = tokenList.remove( lastIndex );

					if( LOGGER.isDebugEnabled() )
					{
						LOGGER.debug( "temp-is-delimiter-removedLastToken[" + lastToken + "]" );
					}
				}
			}

			if( StringUtils.isQualifiedString( currentExpressionCharacter ) )
			{
				tokenList.add( new ExpressionToken( currentExpressionCharacter, index ) );

				if( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "token-added[" + tokenList + "]" );
				}
			}
		}

		// Give a chance to include previous delimiter in current token (not current character),
		// if these are approachable as per grammar
		// remove the last token from token list
		// and reset the offset to the initial point of last token
		// so that in next parsing cycle, last token and current token can be
		// collected as single token

		else if( isPreviousTokenADelimiter( tokenList )
				&& grammar.isApproachable( getLastToken( tokenList )
						+ getCurrentToken( expression, offset, currentPosition ) ) )
		{
			String lastToken = getLastToken( tokenList );
			offset -= lastToken.length();
			ExpressionToken removedToken = tokenList.remove( tokenList.size() - 1 );

			if( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "previousToken-delimiter-approachable-removedLastToken[" + lastToken + "] removedToken["
						+ removedToken + "]", new Exception( "case found" ) );
			}
		}

		// Call the parser recursively to get next token.
		if( ++currentPosition < expression.length() )
		{
			parse( expression, tokenList, currentPosition, offset );
		}
		else
		{
			// Put the token if any at last or not.
			String currentToken = getCurrentToken( expression, offset, currentPosition );

			if( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "lastToken[" + currentToken + "]" );
			}

			if( StringUtils.isQualifiedString( currentToken ) )
			{
				checkValid( currentToken, offset );
				tokenList.add( new ExpressionToken( currentToken, index ) );

				if( LOGGER.isDebugEnabled() )
				{
					LOGGER.debug( "token-added[" + tokenList + "]" );
				}
			}
		}
	}

	/**
	 * Check whether the previous character is delimiter or not
	 * 
	 * @param tokens list of parsed tokens
	 * @return <code>true</code> if the previous token is delimiter
	 *         otherwise <code>false</code>.
	 */
	private boolean isPreviousTokenADelimiter( List<ExpressionToken> tokens )
	{
		String lastToken = getLastToken( tokens );
		boolean lastDelimiter = lastToken != null && grammar.isDelimiter( lastToken );
		return lastDelimiter;
	}

	/**
	 * Returns last collected token in token list during parsing process
	 * 
	 * @param tokens the list of tokens
	 * @return last token from the list or <code>null</code> if there is no token in the list
	 */
	private String getLastToken( List<ExpressionToken> tokens )
	{
		int size = tokens.size();
		return size > 0 ? ( (ExpressionToken) tokens.get( size - 1 ) ).getValue() : null;
	}

	/**
	 * Return the current token based on offset and current pointer while parsing
	 * 
	 * @param expression the expression being parsed
	 * @param offset the current offset of token during parsing
	 * @param pointer the current location of parser in expression
	 * @return the current token
	 */
	private String getCurrentToken( String expression, int offset, int pointer )
	{
		String token = expression.substring( offset, pointer );
		return grammar.isIgnoreBlank() ? token.trim() : token;
	}

	/**
	 * Checks whether the token is valid or not using DefaultXMLGrammar
	 * 
	 * @param offset the current offset in parsing
	 * @param currentToken current token to check the validity
	 * @throws ExpressionEngineException if the token is not allowed by the grammar
	 */
	private void checkValid( String currentToken, int offset ) throws ExpressionEngineException
	{
		if( !grammar.isAllowed( currentToken ) )
		{
			throw new ExpressionEngineException( "Invalid token \"" + currentToken + "\" at position " + ( offset + 1 ) );
		}
	}

	public static void main( String[] args ) throws ExpressionEngineException
	{
		Parser parser = new Parser( new DefaultXMLGrammar() );
		List<ExpressionToken> tokens = parser.parse( "pow(2,null)" );
		System.out.println( tokens );
	}
}