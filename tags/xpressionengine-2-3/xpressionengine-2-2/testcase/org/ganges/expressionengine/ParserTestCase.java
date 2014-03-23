/**
 * Created on Aug 26, 2006
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

import java.util.List;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.grammar.ExpressionToken;


/**
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * 
 * Test case for parser.
 */
public class ParserTestCase extends TestCase {

	/**
	 * The parser to test.
	 */
	private Parser parser;

	/**
	 * Runs the test for parser.
	 * 
	 * @param args
	 * @throws ExpressionEngineException
	 *             if unable to parse
	 */
	public static void main( String[] args ) throws ExpressionEngineException {
		TestRunner.run( ParserTestCase.class );
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		parser = new Parser();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		parser = null;
	}

	public void testAritmaticExpression() throws ExpressionEngineException {
		String exp = "123 + 124 - 67 * 45 / 90 + (12 + 34 / 67)";
		List tokens = parser.parse( exp );
		assertEquals( "123", popToken( tokens ) );
		assertEquals( "+", popToken( tokens ) );
		assertEquals( "124", popToken( tokens ) );
		assertEquals( "-", popToken( tokens ) );
		assertEquals( "67", popToken( tokens ) );
		assertEquals( "*", popToken( tokens ) );
		assertEquals( "45", popToken( tokens ) );
		assertEquals( "/", popToken( tokens ) );
		assertEquals( "90", popToken( tokens ) );
		assertEquals( "+", popToken( tokens ) );
		assertEquals( "(", popToken( tokens ) );
		assertEquals( "12", popToken( tokens ) );
		assertEquals( "+", popToken( tokens ) );
		assertEquals( "34", popToken( tokens ) );
		assertEquals( "/", popToken( tokens ) );
		assertEquals( "67", popToken( tokens ) );
		assertEquals( ")", popToken( tokens ) );
		assertEquals( 0, tokens.size() );
	}

	public void testFunctionExpression() throws ExpressionEngineException {
		String exp = "120 * pow(sin(20) / tan(30), 2)";
		List tokens = parser.parse( exp );
		assertEquals( "120", popToken( tokens ) );
		assertEquals( "*", popToken( tokens ) );
		assertEquals( "pow", popToken( tokens ) );
		assertEquals( "(", popToken( tokens ) );
		assertEquals( "sin", popToken( tokens ) );
		assertEquals( "(", popToken( tokens ) );
		assertEquals( "20", popToken( tokens ) );
		assertEquals( ")", popToken( tokens ) );
		assertEquals( "/", popToken( tokens ) );
		assertEquals( "tan", popToken( tokens ) );
		assertEquals( "(", popToken( tokens ) );
		assertEquals( "30", popToken( tokens ) );
		assertEquals( ")", popToken( tokens ) );
		assertEquals( ",", popToken( tokens ) );
		assertEquals( "2", popToken( tokens ) );
		assertEquals( ")", popToken( tokens ) );
		assertEquals( 0, tokens.size() );
	}

	public void testArrayExpression() throws ExpressionEngineException {
		String exp = "values[0] + values[1] + values[2] + 120 * 45 / num[1][1]";
		List tokens = parser.parse( exp );
		assertEquals( "values", popToken( tokens ) );
		assertEquals( "[", popToken( tokens ) );
		assertEquals( "0", popToken( tokens ) );
		assertEquals( "]", popToken( tokens ) );
		assertEquals( "+", popToken( tokens ) );
		assertEquals( "values", popToken( tokens ) );
		assertEquals( "[", popToken( tokens ) );
		assertEquals( "1", popToken( tokens ) );
		assertEquals( "]", popToken( tokens ) );
		assertEquals( "+", popToken( tokens ) );
		assertEquals( "values", popToken( tokens ) );
		assertEquals( "[", popToken( tokens ) );
		assertEquals( "2", popToken( tokens ) );
		assertEquals( "]", popToken( tokens ) );
		assertEquals( "+", popToken( tokens ) );
		assertEquals( "120", popToken( tokens ) );
		assertEquals( "*", popToken( tokens ) );
		assertEquals( "45", popToken( tokens ) );
		assertEquals( "/", popToken( tokens ) );
		assertEquals( "num", popToken( tokens ) );
		assertEquals( "[", popToken( tokens ) );
		assertEquals( "1", popToken( tokens ) );
		assertEquals( "]", popToken( tokens ) );
		assertEquals( "[", popToken( tokens ) );
		assertEquals( "1", popToken( tokens ) );
		assertEquals( "]", popToken( tokens ) );
		assertEquals( 0, tokens.size() );
	}

	public void testPropertyExpression() throws ExpressionEngineException {
		String exp = "1 + .address.city.name + students[0].rollNo";
		List tokens = parser.parse( exp );
		assertEquals( "1", popToken( tokens ) );
		assertEquals( "+", popToken( tokens ) );
		assertEquals( ".", popToken( tokens ) );
		assertEquals( "address", popToken( tokens ) );
		assertEquals( ".", popToken( tokens ) );
		assertEquals( "city", popToken( tokens ) );
		assertEquals( ".", popToken( tokens ) );
		assertEquals( "name", popToken( tokens ) );
		assertEquals( "+", popToken( tokens ) );
		assertEquals( "students", popToken( tokens ) );
		assertEquals( "[", popToken( tokens ) );
		assertEquals( "0", popToken( tokens ) );
		assertEquals( "]", popToken( tokens ) );
		assertEquals( ".", popToken( tokens ) );
		assertEquals( "rollNo", popToken( tokens ) );
		assertEquals( 0, tokens.size() );
	}

	/**
	 * Returns the token value from top of list and remove from list.
	 * 
	 * @param tokens
	 * @return
	 */
	private String popToken( List tokens ) {
		ExpressionToken token = (ExpressionToken) tokens.remove( 0 );
		return token.getValue();
	}

}