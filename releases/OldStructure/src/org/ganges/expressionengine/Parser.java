/**
 * Created on Jan 3, 2006
 *
 * Copyright (c) 2005 Ganges Organisation all rights reserved.
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of version 2 of the GNU Lesser General Public License as
 * published by the Free Software Foundation. See the GNU Lesser General Public License for more details.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.ganges.expressionengine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.grammar.DefaultXMLGrammar;
import org.ganges.expressionengine.grammar.ExpressionToken;
import org.ganges.expressionengine.grammar.Grammar;
import org.ganges.utils.StringUtils;

/**
 * This parser object parses the expressions, break these in expression tokens
 * and perform lexical analysis on these tokens. All kind of expressions can be
 * parsed like arithmetic, function call, and property accessor etc.
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
 * 6). <i> /person/address/city/name + /person/children[name='Ram'] +
 * /person/children[1] </i>. <br/>
 * 7). <i>abc=xyz || 1!=2 && 3 >= xyz && xvz <= 30 </i>. <br/>
 * <br/>
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public final class Parser {

	private static Log LOGGER = LogFactory.getLog(Parser.class);

	/**
	 * DefaultXMLGrammar object used to parse the expression
	 */
	private final Grammar grammar;

	/**
	 * Constructs the Parser with default DefaultXMLGrammar instance
	 */
	public Parser() {
		this(DefaultXMLGrammar.getInstance());
	}

	/**
	 * Constructs the Parser with given DefaultXMLGrammar object
	 * 
	 * @param grammar the grammar using which it parse the expression.
	 * @throws IllegalArgumentException if the grammar is null.
	 */
	public Parser(Grammar grammar) {
		if (grammar == null) {
			throw new IllegalArgumentException("DefaultXMLGrammar object must not be null for the parser.");
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
	 * @throws IllegalArgumentException  if the expression is not a qualified string
	 */
	public final List<ExpressionToken> parse(String expression) throws ExpressionEngineException {

		if (!StringUtils.isQualifiedString(expression)) {
			throw new ExpressionEngineException("Passed expression is not a qualified text.");
		}

		List<ExpressionToken> tokenList = new ArrayList<ExpressionToken>();

		// TODO: Can we pass pointer as 1, it would enable to get first token in first
		// pass
		parse(expression, tokenList, 0, 0);

		return tokenList;
	}

	/**
	 * It parses the expression, break it in list of tokens using rules from Grammar
	 * 
	 * Default Grammar: DefaultXMLGrammar Custom grammar can also be set.
	 * 
	 * TODO: Can we make its code simpler. Try.
	 * 
	 * @param expression      the expression string to parse
	 * @param tokenList       the list of tokens.
	 * @param currentPosition this is the pointer which represents the current
	 *                        position of parser cursor in expression string during
	 *                        parsing
	 * @param offset          is the start position of current 'token' in expression
	 *                        string during parsing. After finding a complete token,
	 *                        it resets to the start position of next token, and
	 *                        start incrementing again to find the end of next
	 *                        token.
	 * @throws ExpressionEngineException if unable to parse.
	 */
	private void parse(String expression, List<ExpressionToken> tokenList, int currentPosition, int offset)
			throws ExpressionEngineException {

		LOGGER.trace("expression[" + expression + "] pointer[" + currentPosition + "] offset[" + offset + "]");

		/*
		 * pseudo code
		 * 
		 * Get next token character from the expression (use current pointer)
		 * 
		 * If the current token is a delimiter && it does not form a valid token after
		 * combining with last token Extract the current token from expression, FINALIZE
		 * the token by adding it to tokens list, and reset the offset to start position
		 * of next token, to parse next
		 * 
		 * If this token is a delimiter && it can be combined with last added token to
		 * form a valid delimiter, do this It is to support delimiters with multiple
		 * characters
		 * 
		 * If current token character is not a delimiter && previous token is a
		 * delimiter && combination of these is approachable by grammar rules reset the
		 * offset to the previous token start position so that next parsing cycle can
		 * add both in one token
		 * 
		 * If there are more characters in expression string which are not yet parsed
		 * call the parse cycle recursively else give a chance if any last token can be
		 * formed by using current offset and pointer
		 */

		// next token
		String currentExpressionCharacter = expression.substring(currentPosition, currentPosition + 1);

		LOGGER.debug("next-token[" + currentExpressionCharacter + "] currentToken["
				+ getCurrentTokenInParsing(expression, offset, currentPosition) + "] + lastToken["
				+ getLastToken(tokenList) + "]");

		int tokenStartIndex = offset;

		// get current token in parsing from the expression
		String currentTokenInParsing = getCurrentTokenInParsing(expression, offset, currentPosition);

		// Refer to grammar.xml to understand delimiters

		// Case 1:
		// if CURRENT CHARACTER is a DELIMITER
		// && CAN NOT be combined with CURRENT TOKEN to form a single Token
		// then finalize the current parsed token, and add to token list

		// Case 2:
		// Then later check if current Token + current character form a delimiter
		// This is to handle composite delimiters case

		// Case 1 > '56 + '. Finalize 56 as token, and add to list.
		// Add + also as separate token

		// Case 2 > != 57
		// current character '=', previous token in parsing '!'
		// then '=' and '!' will be combined as one delimiter

		if (grammar.isDelimiter(currentExpressionCharacter)
				&& !grammar.isApproachable(currentTokenInParsing + currentExpressionCharacter)) {

			LOGGER.debug("delimiter+notApproachable-currentToken[" + currentTokenInParsing + "]");

			// As we found a delimiter > i.e the current character
			// Check if current parsed token so far is a valid token, for ex: '56 +'
			// If yes >> finalize the current token i.e. '56' and add to tokens list

			// The delimiter character i.e. '+' will be added to token list later
			// after next code block of multiple character delimiter

			if (StringUtils.isQualifiedString(currentTokenInParsing)) {

				validateByGrammar(currentTokenInParsing, offset);

				tokenList.add(new ExpressionToken(currentTokenInParsing, tokenStartIndex));

				LOGGER.debug("new-token-added[" + tokenList + "]");
			}

			// reset offset to beginning of next token, which is current pointer + 1
			offset = currentPosition + 1;
			LOGGER.debug("increased-offset[" + offset + "]");

			/*
			 * Case 2 > 56 != 57. current character '='
			 * 
			 * Formation of multiple character delimiters This is to support operator with
			 * more than one character like function or !=, etc.
			 * 
			 * If the previously added token is a delimiter, i.e. '!' And if a valid
			 * delimiter can be formed by concatenating the next token/character with
			 * previously added token i.e. '!=' Concatenate these into one token
			 */

			if (isPreviousTokenADelimiter(tokenList)) {

				// Get previously added token
				int lastIndex = tokenList.size() - 1;
				String previousToken = (tokenList.get(lastIndex)).getValue();

				// Add previously added token value to next token value
				String potentialCompositeDelimiter = previousToken + currentExpressionCharacter;

				LOGGER.debug("previousTokenDelimiter-previousToken[" + previousToken + "] temp["
						+ potentialCompositeDelimiter + "]");

				// if this combination can be qualified as a delimiter
				// remove last token from token list
				// add the new composite delimiter token to token list
				// built by adding current token to last token

				if (grammar.isDelimiter(potentialCompositeDelimiter)) {

					// resetting token start index to current index minus previous token length
					tokenStartIndex -= previousToken.length();

					// reset current expression character to combination of current + last token
					currentExpressionCharacter = potentialCompositeDelimiter;

					// remove last token from the token list, as a new token is formed by combining
					// with current token
					ExpressionToken lastToken = tokenList.remove(lastIndex);

					LOGGER.debug("temp-is-delimiter-removedLastToken[" + lastToken + "]");
				}
			}

			// Finalize delimiter token and add to the token list
			// ex: if composite delimiter '!='
			// ex: if simple deliver '+'

			if (StringUtils.isQualifiedString(currentExpressionCharacter)) {

				tokenList.add(new ExpressionToken(currentExpressionCharacter, tokenStartIndex));

				LOGGER.debug("token-added[" + tokenList + "]");
			}

		} // end of delimiter parsing, token formation

		// if PREVIOUS TOKEN in list is a DELIMITER && can be clubbed with CURRENT TOKEN
		// to form a NON-DELIMITER approachable token

		// Then add previous delimiter in current token > (not current character)
		// So it can be treated as a non-delimiter token combined

		// remove the last token from token list
		// and reset the offset to the initial point of last token
		// so that in next parsing cycle, last token and current token can be collected
		// as single token

		// TODO : Add example of this case

		else if (isPreviousTokenADelimiter(tokenList)
				&& grammar.isApproachable(getLastToken(tokenList) + currentTokenInParsing)) {

			String lastToken = getLastToken(tokenList);
			offset -= lastToken.length();

			ExpressionToken removedToken = tokenList.remove(tokenList.size() - 1);

			LOGGER.debug("previousToken-delimiter-approachable-removedLastToken[" + lastToken + "] removedToken["
					+ removedToken + "]", new Exception("case found"));
		}

		// Call the parser recursively to get next token.
		
		// TODO: risky increment of current position in if condition, not easy to
		// understand
		
		if (++currentPosition < expression.length()) {
			parse(expression, tokenList, currentPosition, offset);
		}

		else { // Use the token if any left at the tail end
			
			String currentToken = getCurrentTokenInParsing(expression, offset, currentPosition);
			LOGGER.debug("lastToken[" + currentToken + "]");

			if (StringUtils.isQualifiedString(currentToken)) {
				
				validateByGrammar(currentToken, offset);
				tokenList.add(new ExpressionToken(currentToken, tokenStartIndex));
				
				LOGGER.debug("token-added[" + tokenList + "]");
			}
		}
	}

	/**
	 * Check whether the previous character is delimiter or not
	 * 
	 * @param tokens list of parsed tokens
	 * @return <code>true</code> if the previous token is delimiter otherwise
	 *         <code>false</code>.
	 */
	private boolean isPreviousTokenADelimiter(List<ExpressionToken> tokens) {
		String lastToken = getLastToken(tokens);
		return (lastToken != null && grammar.isDelimiter(lastToken));
	}

	/**
	 * Returns last collected token in token list during parsing process
	 * 
	 * @param tokens the list of tokens
	 * @return last token from the list or <code>null</code> if there is no token in
	 *         the list
	 */
	private String getLastToken(List<ExpressionToken> tokens) {
		int size = tokens.size();
		return size > 0 ? ((ExpressionToken) tokens.get(size - 1)).getValue() : null;
	}

	/**
	 * Return the current token based on offset and current pointer while parsing
	 * This is not a 'finalized' token from token list, but it is still in parsing
	 * in expression string
	 * 
	 * @param expression the expression being parsed
	 * @param offset     the current offset of token during parsing - the start of
	 *                   token usually
	 * @param pointer    the current location of parser in expression
	 * @return the current token
	 */
	private String getCurrentTokenInParsing(String expression, int offset, int pointer) {
		String token = expression.substring(offset, pointer);
		return grammar.isIgnoreBlank() ? token.trim() : token;
	}

	/**
	 * Checks whether the token is valid or not using DefaultXMLGrammar
	 * 
	 * @param offset       the current offset in parsing
	 * @param currentToken current token to check the validity
	 * @throws ExpressionEngineException if the token is not allowed by the grammar
	 */
	private void validateByGrammar(String currentToken, int offset) throws ExpressionEngineException {
		if (!grammar.isAllowed(currentToken)) {
			throw new ExpressionEngineException("Invalid token \"" + currentToken + "\" at position " + (offset + 1));
		}
	}

	public static void main(String[] args) throws ExpressionEngineException {
		Parser parser = new Parser();
		List<ExpressionToken> tokens = parser.parse("pow(2,3)");
		System.out.println(tokens);
	}
}