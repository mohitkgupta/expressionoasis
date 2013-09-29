/**
 * Created on Jan 27, 2006
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
package org.ganges.expressionengine.grammar;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ganges.expressionengine.grammar.rules.IProductionRule;
import org.ganges.expressionengine.grammar.rules.ProductionRule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This is the default XML based grammar implementation. It read the production 
 * rules from a XML file, and populate its rule sets. Use of XML makes it highly 
 * customizable without making any change to code.
 * 
 * TODO
 * 	Can we remove operators array and use precedence map keys?
 * 	DefaultXMLGrammar can be loosely coupled with Compiler, i.e. can be picked from 
 *  configuration.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 */
public class DefaultXMLGrammar implements Grammar {

	private static Log					 LOGGER			   = LogFactory.getLog( DefaultXMLGrammar.class );

	/**
	 * Static instance of the grammar.
	 */
	private static final DefaultXMLGrammar instance			 = new DefaultXMLGrammar();

	/**
	 * File path for grammar configuration.
	 */
	public static final String			 FILE_PATH			= "grammar.xml";

	/**
	 * This is the constant name for production rules tag in XML
	 */
	public static final String			 PRODUCTION_RULES	 = "productionRules";

	/**
	 * This is the constant name for binary operators tag in XML
	 */
	private static final String			BINARY_OPERATORS	 = "binaryOperators";

	/**
	 * This is the constant name for unary operators tag in XML
	 */
	public static final String			 UNARY_OPERATORS	  = "unaryOperators";

	/**
	 * This is the constant name for functions tag in XML
	 */
	public static final String			 FUNCTIONS			= "functions";

	/**
	 * This is the constant name for delimiters tag in XML
	 */
	public static final String			 DELIMITERS		   = "delimiters";

	/**
	 * This is the constant name for brackets tag in XML
	 */
	public static final String			 BRACKETS			 = "brackets";

	/**
	 * This is the constant name for ignore blank tag in XML
	 */
	public static final String			 IGNORE_BLANK		 = "ignoreBlank";

	/**
	 * This is the constant name for true tag in XML
	 */
	public static final String			 TRUE				 = "true";

	/**
	 * This is the constant name for name tag in XML
	 */
	public static final String			 NAME				 = "name";

	/**
	 * This is the constant name for bracket tag in XML
	 */
	public static final String			 BRACKET			  = "bracket";

	/**
	 * This is the constant name for left tag in XML
	 */
	public static final String			 LEFT				 = "left";

	/**
	 * This is the constant name for right tag in XML
	 */
	public static final String			 RIGHT				= "right";

	/**
	 * This is the constant name for operator tag in XML
	 */
	public static final String			 OPERATOR			 = "operator";

	/**
	 * This is the constant name for precedence tag in XML
	 */
	public static final String			 PRECEDENCE		   = "precedence";

	/**
	 * This is the constant name for delimiter tag in XML
	 */
	public static final String			 DELIMITER			= "delimiter";

	/**
	 * This is the constant name for production rule tag in XML
	 */
	public static final String			 PRODUCTION_RULE	  = "productionRule";

	/**
	 * This is the constant name for approachable pattern tag in XML
	 */
	public static final String			 APPROACHABLE_PATTERN = "approchablePattern";

	/**
	 * This is the constant name for allowed pattern tag in XML
	 */
	public static final String			 ALLOWED_PATTERN	  = "allowedPattern";

	/**
	 * Array container for production rules of this grammar.
	 */
	private IProductionRule[]			  productionRules;

	/**
	 * Array container of unary operators.
	 */
	private String[]					   unaryOperators;

	/**
	 * Array container of binary operators.
	 */
	private String[]					   binaryOperators;

	/**
	 * Array container of functions.
	 */
	private String[]					   functions;

	/**
	 * Array container of delimiters.
	 */
	private String[]					   delimiters;

	/**
	 * Array container of bracket pair
	 */
	private String[][]					 brackets;

	/**
	 * Map container for precedence of binary operators, binary operator as key and 
	 * precedence as value.
	 */
	private Map							binaryPrecedences;

	/**
	 * Map container for precedence of unary operators, unary operator as key and 
	 * precedence as value.
	 */
	private Map							unaryPrecedences;

	/**
	 * This is the indicator whether to ignore the blank or not during parsing.
	 */
	private boolean						ignoreBlank;

	/**
	 * Constructs the grammar
	 */
	private DefaultXMLGrammar() {
		configure();
	}

	/**
	 * Gets the shared instance of the grammar.
	 * 
	 * @return the singleton instance of the grammar.
	 */
	public static final DefaultXMLGrammar getInstance() {
		return instance;
	}

	/**
	 * Checks whether the token is a delimiter or not.
	 * 
	 * @param token the token
	 * @return <code>true</code> if the token is delimiter <code>false</code>
	 *         otherwise.
	 */
	public boolean isDelimiter( ExpressionToken token ) {
		return isDelimiter( token.getValue() );
	}

	/**
	 * Checks whether the token is a delimiter or not.
	 * 
	 * @param token the token
	 * @return <code>true</code> if the token is delimiter <code>false</code>
	 *         otherwise.
	 */
	public boolean isDelimiter( String token ) {
		return search( delimiters, token ) != -1;
	}

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
	 * 		  it can approach to any expression token pattern or not.
	 * @return <code>true</code> if the token pattern is approachable
	 *         <code>false</code> otherwise.
	 */
	public boolean isApproachable( String token ) {
		int length = productionRules == null ? 0 : productionRules.length;
		boolean result = false;

		for( int i = 0; i < length; i++ ) {
			result = productionRules[i].isApproaching( token );

			if( result ) {
				break;
			}
		}

		return result;
	}

	/**
	 * Checks whether the token is allowed or not. 
	 * 
	 * A token is fully constructed token. Parser generally calls this method to 
	 * check whether the current token is a valid token as per the production 
	 * rules or not.
	 * 
	 * @param token the token which is to be checked for its validity
	 * @return <code>true</code> if the token is allowed <code>false</code>
	 *         otherwise.
	 */
	public boolean isAllowed( String token ) {
		int length = productionRules == null ? 0 : productionRules.length;
		boolean result = false;

		for( int i = 0; i < length; i++ ) {
			result = productionRules[i].isAllowed( token );

			if( result ) {
				break;
			}
		}

		return result;
	}

	/**
	 * Checks whether to ignore the blanks in expression or not. It tells the 
	 * parser whether to exclude the extra blanks while parsing or not.
	 * 
	 * @return <code>true</code> if parser wants to exclude the blanks 
	 * 		   <code>false</code> otherwise.
	 */
	public boolean isIgnoreBlank() {
		return ignoreBlank;
	}

	/**
	 * Checks whether the given token is an operator or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is an operator
	 *         <code>false</code> otherwise
	 */
	public boolean isOperator( ExpressionToken token ) {
		return isOperator( token.getValue() );
	}

	/**
	 * Checks whether the given token is an operator or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is an operator
	 *         <code>false</code> otherwise
	 */
	public boolean isOperator( String token ) {
		return search( binaryOperators, token ) != -1 || search( unaryOperators, token ) != -1 || isFunction( token );
	}

	/**
	 * Checks whether the given token is a binary operator or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is a binary operator
	 *         <code>false</code> otherwise
	 */
	public boolean isBinaryOperator( ExpressionToken token ) {
		return isBinaryOperator( token.getValue() );
	}

	/**
	 * Checks whether the given token is a binary operator or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is a binary operator
	 *         <code>false</code> otherwise
	 */
	public boolean isBinaryOperator( String token ) {
		return search( binaryOperators, token ) != -1;
	}

	/**
	 * Checks whether the token is an function or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is an function
	 *         <code>false</code> otherwise
	 */
	public boolean isFunction( ExpressionToken token ) {
		return isFunction( token.getValue() );
	}

	/**
	 * Checks whether the token is an function or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is an function
	 *         <code>false</code> otherwise
	 */
	public boolean isFunction( String token ) {
		return search( functions, token ) != -1;
	}

	/**
	 * Checks whether the given operator is a unary operator or not.
	 * 
	 * @param operator the operator to check
	 * @return <code>true</code> if the operator is used as unary operator
	 *         <code>false</code> otherwise.
	 */
	public boolean isUnary( ExpressionToken operator ) {
		return isUnary( operator.getValue() );
	}

	/**
	 * Checks whether the given operator is a unary operator or not.
	 * 
	 * @param operator the operator to check
	 * @return <code>true</code> if the operator is used as unary operator
	 *         <code>false</code> otherwise.
	 */
	public boolean isUnary( String operator ) {
		return search( unaryOperators, operator ) != -1 || isFunction( operator );
	}

	/**
	 * Checks whether the token is a left bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token can be used as left bracket
	 *         <code>false</code> otherwise.
	 */
	public boolean isLeftBracket( ExpressionToken token ) {
		return isLeftBracket( token.getValue() );
	}

	/**
	 * Checks whether the token is a left bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token can be used as left bracket
	 *         <code>false</code> otherwise.
	 */
	public boolean isLeftBracket( String token ) {
		boolean result = false;
		int length = brackets == null ? 0 : brackets.length;

		for( int i = 0; i < length; i++ ) {
			result = token.equals( brackets[i][0] );

			if( result ) {
				break;
			}
		}

		return result;
	}

	/**
	 * Checks whether the token is a right bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token can be used as right bracket
	 *         <code>false</code> otherwise.
	 */
	public boolean isRightBracket( ExpressionToken token ) {
		return isRightBracket( token.getValue() );
	}

	/**
	 * Checks whether the token is a right bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token can be used as right bracket
	 *         <code>false</code> otherwise.
	 */
	public boolean isRightBracket( String token ) {
		boolean result = false;
		int length = brackets == null ? 0 : brackets.length;

		for( int i = 0; i < length; i++ ) {
			result = token.equals( brackets[i][1] );

			if( result ) {
				break;
			}
		}

		return result;
	}

	/**
	 * Check whether the given token is a bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is a bracket
	 *         <code>false</code> otherwise
	 */
	public boolean isBracket( ExpressionToken token ) {
		return isBracket( token.getValue() );
	}

	/**
	 * Check whether the given token is a bracket or not.
	 * 
	 * @param token the token to check
	 * @return <code>true</code> if the token is a bracket
	 *         <code>false</code> otherwise
	 */
	public boolean isBracket( String token ) {
		return isRightBracket( token ) || isLeftBracket( token );
	}

	/**
	 * Returns the opposite bracket for the given bracket.
	 * 
	 * @param bracket the bracket for which we need to find the opposite bracket
	 * @return the opposite part of bracket w.r.t given bracket
	 */
	public String getOppositeBracket( String bracket ) {
		String result = null;
		int length = brackets == null ? 0 : brackets.length;

		for( int i = 0; i < length; i++ ) {
			int index = search( brackets[i], bracket );

			if( index != -1 ) {
				result = index == 1 ? brackets[i][0] : brackets[i][1];

				break;
			}
		}

		return result;
	}

	/**
	 * Gets the precedence order of the given operator
	 * 
	 * @param operator the operator to check for precedence
	 * @param isUnary true if the operator is unary, as an operator can behave 
	 * 		  either as unary or as binary
	 * @return the precedence order of the operator
	 */
	public int getPrecedenceOrder( ExpressionToken operator, boolean isUnary ) {
		return getPrecedenceOrder( operator.getValue(), isUnary );
	}

	/**
	 * Gets the precedence order of the given operator
	 * 
	 * @param operator the operator to check for precedence
	 * @param isUnary true if the operator is unary, as an operator can behave 
	 * 		  either as unary or as binary
	 * @return the precedence order of the operator
	 */
	public int getPrecedenceOrder( String operator, boolean isUnary ) {
		return isUnary ? ( (Integer) unaryPrecedences.get( operator ) ).intValue() : ( (Integer) binaryPrecedences
				.get( operator ) ).intValue();
	}

	/**
	 * Configures the grammar object with specified XML file
	 */
	private void configure() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating( true );

			DocumentBuilder builder = factory.newDocumentBuilder();

			LOGGER.debug( "resourcePath[" + getClass().getClassLoader().getResource( "" ) );
			Document document = builder.parse( getClass().getClassLoader().getResourceAsStream( FILE_PATH ) );
			Element root = document.getDocumentElement();

			// Extracting production rules
			NodeList nodeList = root.getChildNodes();
			int length = nodeList.getLength();

			for( int i = 0; i < length; i++ ) {
				Node childNode = nodeList.item( i );
				String nodeName = childNode.getNodeName();

				if( childNode.getNodeType() == Node.ELEMENT_NODE ) {
					if( PRODUCTION_RULES.equals( nodeName ) ) {
						buildProductionRules( (Element) childNode );
					}
					else if( BINARY_OPERATORS.equals( nodeName ) ) {
						buildBinaryOperators( (Element) childNode );
						loadBinaryPrecedence( (Element) childNode );
					}
					else if( UNARY_OPERATORS.equals( nodeName ) ) {
						buildUnaryOperators( (Element) childNode );
						loadUnaryPrecedence( (Element) childNode );
					}
					else if( FUNCTIONS.equals( nodeName ) ) {
						buildFunctions( (Element) childNode );
						loadFunctionPrecedence( (Element) childNode );
					}
					else if( DELIMITERS.equals( nodeName ) ) {
						buildDelimiters( (Element) childNode );
					}
					else if( BRACKETS.equals( nodeName ) ) {
						loadBrackets( (Element) childNode );
					}
					else if( IGNORE_BLANK.equals( nodeName ) ) {
						ignoreBlank = TRUE.equals( ( (Element) childNode ).getAttribute( NAME ) );
					}
				}
			}
		}
		catch( Exception ex ) {
			throw new RuntimeException( "Error while loading the configurations.", ex );
		}
	}

	/**
	 * Loads the brackets from the XML configuration
	 * 
	 * @param childNode
	 */
	private void loadBrackets( Element childNode ) {
		NodeList childList = childNode.getElementsByTagName( BRACKET );
		int childLength = childList.getLength();
		brackets = new String[childLength][2];

		for( int j = 0; j < childLength; j++ ) {
			Element subChildNode = (Element) childList.item( j );
			brackets[j][0] = subChildNode.getAttribute( LEFT );
			brackets[j][1] = subChildNode.getAttribute( RIGHT );
		}
	}

	/**
	 * Loads the precedence of the binary operators
	 * 
	 * @param childNode
	 */
	private void loadBinaryPrecedence( Element childNode ) {
		if( binaryPrecedences == null ) {
			binaryPrecedences = new HashMap<String, Integer>();
		}

		loadPrecedence( childNode, binaryPrecedences );
	}

	/**
	 * Loads the precedence of the unary operators
	 * 
	 * @param childNode
	 */
	private void loadUnaryPrecedence( Element childNode ) {
		if( unaryPrecedences == null ) {
			unaryPrecedences = new HashMap<String, Integer>();
		}

		loadPrecedence( childNode, unaryPrecedences );
	}

	/**
	 * Loads the precedence of the functions
	 * 
	 * @param childNode
	 */
	private void loadFunctionPrecedence( Element childNode ) {
		if( unaryPrecedences == null ) {
			unaryPrecedences = new HashMap<String, Integer>();
		}

		loadPrecedence( childNode, unaryPrecedences );
	}

	/**
	 * Loads the precedence in the given map.
	 * 
	 * @param childNode
	 *            the operators node
	 * @param precedences
	 *            the map.
	 */
	private void loadPrecedence( Element childNode, Map<String, Integer> precedences ) {
		NodeList childList = childNode.getElementsByTagName( OPERATOR );
		int childLength = childList.getLength();

		for( int j = 0; j < childLength; j++ ) {
			Element subChildNode = (Element) childList.item( j );
			String operator = subChildNode.getAttribute( NAME );
			String precedence = subChildNode.getAttribute( PRECEDENCE );
			precedences.put( operator, new Integer( Integer.parseInt( precedence ) ) );
		}
	}

	/**
	 * Builds the delimiters
	 * 
	 * @param childNode
	 *            the node delimiters
	 */
	private void buildDelimiters( Element childNode ) {
		delimiters = buildArrayByAttribute( childNode, DELIMITER, NAME );
	}

	/**
	 * Builds the operators
	 * 
	 * @param childNode
	 *            the node for operators
	 */
	private void buildBinaryOperators( Element childNode ) {
		binaryOperators = buildArrayByAttribute( childNode, OPERATOR, NAME );
	}

	/**
	 * Builds the unary operators
	 * 
	 * @param childNode
	 *            the node for unary operators
	 */
	private void buildUnaryOperators( Element childNode ) {
		unaryOperators = buildArrayByAttribute( childNode, OPERATOR, NAME );
	}

	/**
	 * Builds the functions
	 * 
	 * @param childNode
	 *            the node for unary operators
	 */
	private void buildFunctions( Element childNode ) {
		functions = buildArrayByAttribute( childNode, OPERATOR, NAME );
	}

	/**
	 * Builds the production rules
	 * 
	 * @param childNode
	 *            the node for production rules
	 */
	private void buildProductionRules( Element childNode ) {
		NodeList childList = childNode.getElementsByTagName( PRODUCTION_RULE );
		int childLength = childList.getLength();
		productionRules = new IProductionRule[childLength];

		for( int j = 0; j < childLength; j++ ) {
			Element subChildNode = (Element) childList.item( j );
			productionRules[j] = new ProductionRule( subChildNode.getAttribute( NAME ), subChildNode
					.getAttribute( APPROACHABLE_PATTERN ), subChildNode.getAttribute( ALLOWED_PATTERN ) );
		}
	}

	/**
	 * Builds the the array of string for attribute passed by the argument for
	 * the elements specified by the tag
	 * 
	 * @param childNode the node delimiters
	 * @param attribute the attribute for which to build the string array.
	 * @param tag the tag for which the elements are identified.
	 * @return the string array.
	 */
	private String[] buildArrayByAttribute( Element childNode, String tag, String attribute ) {
		NodeList childList = childNode.getElementsByTagName( tag );
		int childLength = childList.getLength();
		String[] array = new String[childLength];

		for( int j = 0; j < childLength; j++ ) {
			Element subChildNode = (Element) childList.item( j );
			array[j] = subChildNode.getAttribute( attribute );
		}

		return array;
	}

	/**
	 * Searches the element in the string array
	 * 
	 * @param array the string elements array
	 * @param element the element to search
	 * @return the index of element in array, <code>-1</code> if not found
	 */
	private int search( String[] array, String element ) {
		int index = -1;
		int length = array == null ? 0 : array.length;

		for( int i = 0; i < length; i++ ) {
			if( element == null ) {
				if( array[i] == null ) {
					index = i;

					break;
				}
			}
			else if( element.equals( array[i] ) ) {
				index = i;

				break;
			}
		}

		return index;
	}

	/**
	 * Runs the test for grammar.
	 */
	public static void main( String[] args ) {
		DefaultXMLGrammar grammar = new DefaultXMLGrammar();
		grammar.isOperator( "+" );
	}

}