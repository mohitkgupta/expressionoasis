/**
 * Created on Jan 13, 2006
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
package org.ganges.expressionengine.expressions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This is the expression factory which creates the expression for given 
 * expression token.
 * 
 * It picks the mapping of tokens and corresponding expression classes from a 
 * XML file.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public class ExpressionFactory {

	/**
	 * This is the static singleton SHARED_INSTANCE of expression factory.
	 */
	private static ExpressionFactory   SHARED_INSTANCE  = new ExpressionFactory();

	/**
	 * This is the file path for expressions configuration.
	 */
	public static final String		 FILE_PATH		= "expressions.xml";

	/**
	 * This is the type tag for XML
	 */
	public static final String		 TYPE			 = "type";

	/**
	 * This is the class tag for XML
	 */
	public static final String		 CLASS			= "class";

	/**
	 * This is the name tag for XML
	 */
	public static final String		 NAME			 = "name";

	/**
	 * This is the unary expression type
	 */
	public static final String		 UNARY			= "unary";

	/**
	 * This is the binary expression type.
	 */
	public static final String		 BINARY		   = "binary";

	/**
	 * This is the function expression type.
	 */
	public static final String		 FUNCTION		 = "function";

	/**
	 * This is the operand expression type.
	 */
	public static final String		 OPERAND		  = "operand";

	/**
	 * This is the true constant for absolute.
	 */
	public static final String		 TRUE			 = "true";

	/**
	 * This is the key class mapping.
	 */
	private Map<String, HashMap<?, ?>> typeClassMapping = new HashMap<String, HashMap<?, ?>>();

	/**
	 * Constructs the ExpressionFactory
	 */
	private ExpressionFactory() {
		configure();
	}

	/**
	 * Configures the expressions for the given configuration specified by the
	 * expressions.xml
	 */
	private void configure() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating( true );

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( getClass().getClassLoader().getResourceAsStream( FILE_PATH ) );
			Element root = document.getDocumentElement();

			// Extracting production rules
			NodeList nodeList = root.getChildNodes();
			int length = nodeList.getLength();
			typeClassMapping.put( UNARY, new HashMap() );
			typeClassMapping.put( BINARY, new HashMap() );
			typeClassMapping.put( OPERAND, new HashMap() );
			typeClassMapping.put( FUNCTION, new HashMap() );

			for( int i = 0; i < length; i++ ) {
				Node childNode = nodeList.item( i );

				if( childNode.getNodeType() == Node.ELEMENT_NODE ) {
					Element element = ( (Element) childNode );
					String token = element.getAttribute( NAME );
					String type = element.getAttribute( TYPE );
					String clazz = element.getAttribute( CLASS );
					HashMap map = (HashMap) typeClassMapping.get( type );
					map.put( token, clazz );
				}
			}
		}
		catch( Exception ex ) {
			throw new RuntimeException( "Error while loading the configurations.", ex );
		}
	}

	/**
	 * Gets the shared instance of Expression Factory
	 * 
	 * @return Returns the shared instance
	 */
	public static ExpressionFactory getInstance() {
		return SHARED_INSTANCE;
	}

	/**
	 * Creates the expression object for given expression token and expression 
	 * type
	 * 
	 * @param expressionToken the token of expression
	 * @param type the type of expression i.e. operand, operator etc
	 * @return the expression object
	 * @throws ExpressionEngineException if anything goes wrong
	 */
	public Expression createExpression( String expressionToken, String type ) throws ExpressionEngineException {
		HashMap mapping = (HashMap) typeClassMapping.get( type );
		String className = (String) ( mapping == null ? null : mapping.get( expressionToken ) );

		// Handling for operand type expressions
		// because operand expression matches with regular expression, hence can 
		// not be searched using map searching
		if( OPERAND == type ) {
			for( Iterator iter = mapping.keySet().iterator(); iter.hasNext(); ) {
				String operandPattern = (String) iter.next();

				if( Pattern.matches( operandPattern, expressionToken ) ) {
					className = (String) mapping.get( operandPattern );
					break;
				}
			}
		}

		if( className == null ) {
			throw new ExpressionEngineException( "Unable to find any expression class mapping for token \""
					+ expressionToken + "\" in type \"" + type + "\"" );
		}

		try {
			// earlier we were returning null, if class name is null
			// however remove that case now, as unable to recall any case for that
			return (Expression) Class.forName( className ).newInstance();
		}
		catch( Exception ex ) {
			throw new ExpressionEngineException( "Unable to create the expression for token \"" + expressionToken
					+ "\" in type \"" + type + "\"" );
		}
	}
}