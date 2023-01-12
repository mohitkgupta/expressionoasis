/**
 * Created on Aug 26, 2006
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

import java.util.List;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.grammar.ExpressionToken;

/**
 * Test case for parser.
 * 
 * TODO 
 * 	Add more test cases for all kind of possible supported expressions
 * 	Add negative test cases
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 */
public class TestParser extends TestCase {

	/**
	 * The parser to test.
	 */
	private Parser parser;

	/**
	 * Runs the test for parser.
	 * 
	 * @param args
	 * @throws ExpressionEngineException if unable to parse
	 */
	public static void main(String[] args) throws ExpressionEngineException {
		TestRunner.run(TestParser.class);
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
		List<ExpressionToken> tokens = parser.parse(exp);
		assertEquals("123", popToken(tokens));
		assertEquals("+", popToken(tokens));
		assertEquals("124", popToken(tokens));
		assertEquals("-", popToken(tokens));
		assertEquals("67", popToken(tokens));
		assertEquals("*", popToken(tokens));
		assertEquals("45", popToken(tokens));
		assertEquals("/", popToken(tokens));
		assertEquals("90", popToken(tokens));
		assertEquals("+", popToken(tokens));
		assertEquals("(", popToken(tokens));
		assertEquals("12", popToken(tokens));
		assertEquals("+", popToken(tokens));
		assertEquals("34", popToken(tokens));
		assertEquals("/", popToken(tokens));
		assertEquals("67", popToken(tokens));
		assertEquals(")", popToken(tokens));
		assertEquals(0, tokens.size());
	}

	public void testCompositeAritmaticExpression() throws ExpressionEngineException {
		String exp = "123 != 124";
		List<ExpressionToken> tokens = parser.parse(exp);
		assertEquals("123", popToken(tokens));
		assertEquals("!=", popToken(tokens));
		assertEquals("124", popToken(tokens));
		assertEquals(0, tokens.size());
	}

	public void testFunctionExpression() throws ExpressionEngineException {
		String exp = "120 * pow(sin(20) / tan(30), 2)";
		List<ExpressionToken> tokens = parser.parse(exp);
		assertEquals("120", popToken(tokens));
		assertEquals("*", popToken(tokens));
		assertEquals("pow", popToken(tokens));
		assertEquals("(", popToken(tokens));
		assertEquals("sin", popToken(tokens));
		assertEquals("(", popToken(tokens));
		assertEquals("20", popToken(tokens));
		assertEquals(")", popToken(tokens));
		assertEquals("/", popToken(tokens));
		assertEquals("tan", popToken(tokens));
		assertEquals("(", popToken(tokens));
		assertEquals("30", popToken(tokens));
		assertEquals(")", popToken(tokens));
		assertEquals(",", popToken(tokens));
		assertEquals("2", popToken(tokens));
		assertEquals(")", popToken(tokens));
		assertEquals(0, tokens.size());
	}

	public void testFunctionRound() throws ExpressionEngineException {
		String exp = "120 * round(2.2)";
		List<ExpressionToken> tokens = parser.parse(exp);
		assertEquals("120", popToken(tokens));
		assertEquals("*", popToken(tokens));
		assertEquals("round", popToken(tokens));
		assertEquals("(", popToken(tokens));
		assertEquals("2.2", popToken(tokens));
		assertEquals(")", popToken(tokens));
		assertEquals(0, tokens.size());
	}

	public void testArrayExpression() throws ExpressionEngineException {
		String exp = "values[0] + values[1] + values[2] + 120 * 45 / num[1][1]";
		List<ExpressionToken> tokens = parser.parse(exp);
		assertEquals("values", popToken(tokens));
		assertEquals("[", popToken(tokens));
		assertEquals("0", popToken(tokens));
		assertEquals("]", popToken(tokens));
		assertEquals("+", popToken(tokens));
		assertEquals("values", popToken(tokens));
		assertEquals("[", popToken(tokens));
		assertEquals("1", popToken(tokens));
		assertEquals("]", popToken(tokens));
		assertEquals("+", popToken(tokens));
		assertEquals("values", popToken(tokens));
		assertEquals("[", popToken(tokens));
		assertEquals("2", popToken(tokens));
		assertEquals("]", popToken(tokens));
		assertEquals("+", popToken(tokens));
		assertEquals("120", popToken(tokens));
		assertEquals("*", popToken(tokens));
		assertEquals("45", popToken(tokens));
		assertEquals("/", popToken(tokens));
		assertEquals("num", popToken(tokens));
		assertEquals("[", popToken(tokens));
		assertEquals("1", popToken(tokens));
		assertEquals("]", popToken(tokens));
		assertEquals("[", popToken(tokens));
		assertEquals("1", popToken(tokens));
		assertEquals("]", popToken(tokens));
		assertEquals(0, tokens.size());
	}

	public void testPropertyExpression() throws ExpressionEngineException {
		String exp = "1 + .address.city.name + students[0].rollNo";
		List<ExpressionToken> tokens = parser.parse(exp);
		assertEquals("1", popToken(tokens));
		assertEquals("+", popToken(tokens));
		assertEquals(".", popToken(tokens));
		assertEquals("address", popToken(tokens));
		assertEquals(".", popToken(tokens));
		assertEquals("city", popToken(tokens));
		assertEquals(".", popToken(tokens));
		assertEquals("name", popToken(tokens));
		assertEquals("+", popToken(tokens));
		assertEquals("students", popToken(tokens));
		assertEquals("[", popToken(tokens));
		assertEquals("0", popToken(tokens));
		assertEquals("]", popToken(tokens));
		assertEquals(".", popToken(tokens));
		assertEquals("rollNo", popToken(tokens));
		assertEquals(0, tokens.size());
	}
	
	public void testPropertyExpressionConfusedWithOperator() throws ExpressionEngineException {
		String exp = "roundabout.diameter + roundabout.radius";
		List<ExpressionToken> tokens = parser.parse(exp);
		assertEquals("roundabout", popToken(tokens));
		assertEquals(".", popToken(tokens));
		assertEquals("diameter", popToken(tokens));
		assertEquals("+", popToken(tokens));
		assertEquals("roundabout", popToken(tokens));
		assertEquals(".", popToken(tokens));
		assertEquals("radius", popToken(tokens));
		assertEquals(0, tokens.size());
	}


	/**
	 * Returns the token value from top of list and remove from list.
	 * 
	 * @param tokens
	 * @return
	 */
	private String popToken(List<ExpressionToken> tokens) {
		return tokens.remove(0).getValue();
	}

}