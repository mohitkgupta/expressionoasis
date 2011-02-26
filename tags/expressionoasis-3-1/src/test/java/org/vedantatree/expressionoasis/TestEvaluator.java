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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vedantatree.expressionoasis.EOErrorCodes;
import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.ExpressionEngine;
import org.vedantatree.expressionoasis.ExpressionEngineConstants;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.extensions.DefaultVariableProvider;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;


/**
 * Testcase for expression evaluator.
 *
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 *
 * Modified to use JUnit 4.
 * 
 * Added tests for new custom functions, and tests to confirm
 * equality expression now works with booleans, and nullable
 * type support.
 *
 * @author Kris Marwood
 * @author 1.1
 */
public class TestEvaluator {

	/**
	 * The expression context used by evaluator.
	 */
	private ExpressionContext expressionContext;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		expressionContext = new ExpressionContext();

		DefaultVariableProvider dvp = new DefaultVariableProvider();
		dvp.addVariable( "principle", new ValueObject( new Double( 100.00 ), Type.DOUBLE ) );
		dvp.addVariable( "rate", new ValueObject( new Double( 10.00 ), Type.DOUBLE ) );
		dvp.addVariable( "time", new ValueObject( new Double( 2.00 ), Type.DOUBLE ) );
		dvp.addVariable( "rates", new ValueObject( new Double[] { 2.00, 3.00 }, Type.createType( Double[].class ) ) );
		dvp.addVariable( "_rates", new ValueObject( new Double[][] { new Double[] { 2.00, 3.00 },
				new Double[] { 4.00, 5.00 } }, Type.createType( Double[][].class ) ) );

		String[] names = new String[] { "Kris", "Mohit", "Parmod" };
		dvp.addVariable( "_names", new ValueObject( names, Type.createType( names.getClass() ) ) );

		dvp.addVariable( "doublesWithNull", new ValueObject( new Double[] { 2.00, 3.00, null }, Type
				.createType( Double[].class ) ) );

		dvp.addVariable( "doublesAllNull", new ValueObject( new Double[] { null, null, null }, Type
				.createType( Double[].class ) ) );
		dvp.addVariable( "longsAllNull", new ValueObject( new Long[] { null, null, null }, Type
				.createType( Long[].class ) ) );
		dvp.addVariable( "booleansAllNull", new ValueObject( new Boolean[] { null, null, null }, Type
				.createType( Boolean[].class ) ) );

		dvp.addVariable( "nameIndex", new ValueObject( new Long( 0 ), Type.LONG ) );

		dvp.addVariable( "object", new ValueObject( new Object(), Type.createType( Object.class ) ) );
		dvp.addVariable( "person", new ValueObject( new Person( "Mohit", 31 ), Type.createType( Person.class ) ) );

		dvp.addVariable( "null_string", new ValueObject( null, Type.STRING ) );
		dvp.addVariable( "non_null_string", new ValueObject( "I'm not null!", Type.STRING ) );

		expressionContext.addVariableProvider( dvp );
		expressionContext.setContextProperty( "JAVA_BEAN", new Object() );
		System.err.println( getClass().getClassLoader().getResource( "" ) );
		expressionContext.setContextProperty( ExpressionEngineConstants.EXPRESSION_CONTENXT_XML_PATH_URL, getClass()
				.getClassLoader().getResource( "test.xml" ) );
	}

	public class Person {

		private String name;
		private int	age;

		public Person( String name, int age ) {
			setName( name );
			setAge( age );
		}

		public void setName( String name ) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setAge( int age ) {
			this.age = age;
		}

		public int getAge() {
			return age;
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		expressionContext = null;
	}

	@Test
	public void testAritmaticExpression() throws ExpressionEngineException {
		String expression = "-10 / 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( -5, result.intValue() );

		expression = "10 + (10 / 2) - 10 * 2 + 10 % 6";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( -1, result.intValue() );

		expression = "20 - (10/-2 + (-5 * -2)) / (15 * (-5/5) )";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 20.333333333333332, result.doubleValue(), 0 );
	}

	@Test
	public void testLogicalExpression() throws ExpressionEngineException {
		String expression = "true && !true";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( false, result.booleanValue() );

		expression = "(false && !true) && !(false && !true)";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( false, result.booleanValue() );

		expression = "(false && !true) || (!(false && !true) || !false)";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( true, result.booleanValue() );

		expression = "true == true";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result.booleanValue() );

		expression = "false == false";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result.booleanValue() );

		expression = "true != false";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result.booleanValue() );

	}

	@Test
	public void testRelationalExpression() throws ExpressionEngineException {
		String expression = "1/2 == 2/2";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( false, result.booleanValue() );

		expression = "2 != 3";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( true, result.booleanValue() );

		expression = "2 < 3";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( true, result.booleanValue() );

		expression = "2 <= 2";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( true, result.booleanValue() );

		expression = "2 > 3";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( false, result.booleanValue() );

		expression = "4 >= 3";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( true, result.booleanValue() );
	}

	@Test
	public void testVariableExpression() throws ExpressionEngineException {
		String expression = "(principle * rate * time) / 100";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "value is not matching", 20.0, result.doubleValue(), 0 );
	}

	@Test
	public void testFunctionExpression() throws ExpressionEngineException {
		String expression = "abs(-4.5) + 1";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 5.5, result.doubleValue(), 0 );

		expression = "min(3, 9) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 4.0, result.doubleValue(), 0 );

		expression = "max(3, 9) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 10.0, result.doubleValue(), 0 );

		expression = "sin(90) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 1.8939966636005579, result.doubleValue(), 0 );

		expression = "cos(0) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.0, result.doubleValue(), 0 );

		expression = "tan(45) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.6197751905438613, result.doubleValue(), 0 );

		expression = "asin(0.8) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 1.9272952180016123, result.doubleValue(), 0 );

		expression = "acos(0) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.5707963267948966, result.doubleValue(), 0 );

		expression = "atan(45) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.5485777614681773, result.doubleValue(), 0 );

		expression = "atan2(45, 2) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.5263811115479857, result.doubleValue(), 0 );

		expression = "exp(2) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 8.38905609893065, result.doubleValue(), 0 );

		expression = "pow(2, 8) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 257.0, result.doubleValue(), 0 );

		expression = "log(4) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.386294361119891, result.doubleValue(), 0 );

		expression = "sqrt(256) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 17.0, result.doubleValue(), 0 );

		expression = "ceil(2.7) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 4.0, result.doubleValue(), 0 );

		expression = "floor(2.3) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 3.0, result.doubleValue(), 0 );

		expression = "rint(2.43) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 3.0, result.doubleValue(), 0 );

		expression = "round(2.43) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 3.0, result.doubleValue(), 0 );

		expression = "random()";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( true, result.doubleValue() != 0 );
	}

	@Test
	public void testArrayExpression() throws ExpressionEngineException {
		String expression = "rates[1] * 10 - _rates[0][0] / 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "value is not matching", 29.0, result.doubleValue(), 0 );
	}

	@Test
	public void testPropertyExpression() throws ExpressionEngineException {
		String expression = "object.class.name";
		String result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "Wrong outcome", "java.lang.Object", result );

		expression = "person.class.name";
		result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "Wrong outcome", Person.class.getName(), result );

		expression = "person.name";
		result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "Wrong outcome", "Mohit", result );

		expression = "person.age";
		Integer age = (Integer) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "Wrong outcome", new Integer( 31 ), age );
	}

	@Test
	public void test_bitwise_signed_right_shift_expression() throws ExpressionEngineException {
		String expression = "16 >> 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 4, result.longValue() );

		expression = "16 >> null";
		result = null;
		try {
			result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		}
		catch( ExpressionEngineException e ) {
			assertEquals( EOErrorCodes.INVALID_OPERAND_TYPE, e.getErrorCode() );
		}
		assertNull( result );

		expression = "longsAllNull[0] >> 2";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_bitwise_signed_left_shift_expression() throws ExpressionEngineException {
		String expression = "3 << 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 12, result.longValue() );

		expression = "3 << null";
		result = null;
		try {
			result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		}
		catch( ExpressionEngineException e ) {
			assertEquals( EOErrorCodes.INVALID_OPERAND_TYPE, e.getErrorCode() );
		}
		assertNull( result );

		expression = "longsAllNull[0] << 2";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_bitwise_unsigned_right_shift_expression() throws ExpressionEngineException {
		String expression = "15 >>> 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 3, result.longValue() );

		expression = "15 >>> null";
		result = null;
		try {
			result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		}
		catch( ExpressionEngineException e ) {
			assertEquals( EOErrorCodes.INVALID_OPERAND_TYPE, e.getErrorCode() );
		}
		assertNull( result );

		expression = "longsAllNull[0] >>> 2";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_bitwise_or_expression() throws ExpressionEngineException {
		String expression = "4 | 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 6, result.longValue() );

		expression = "4 | null";
		result = null;
		try {
			result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		}
		catch( ExpressionEngineException e ) {
			assertEquals( EOErrorCodes.INVALID_OPERAND_TYPE, e.getErrorCode() );
		}
		assertNull( result );

		expression = "null | 4";
		result = null;
		try {
			result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		}
		catch( ExpressionEngineException e ) {
			assertEquals( EOErrorCodes.INVALID_OPERAND_TYPE, e.getErrorCode() );
		}
		assertNull( result );
	}

	@Test
	public void test_bitwise_complement_expression() throws ExpressionEngineException {
		String expression = "~2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( -3, result.longValue() );

		expression = "~null";
		result = null;
		try {
			result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		}
		catch( ExpressionEngineException e ) {
			assertEquals( EOErrorCodes.INVALID_OPERAND_TYPE, e.getErrorCode() );
		}
		assertNull( result );

		expression = "~longsAllNull[0]";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_bitwise_and_expression() throws ExpressionEngineException {
		String expression = "3 & 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2, result.longValue() );

		expression = "3 & null";
		result = null;
		try {
			result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		}
		catch( ExpressionEngineException e ) {
			assertEquals( EOErrorCodes.INVALID_OPERAND_TYPE, e.getErrorCode() );
		}
		assertNull( result );

		expression = "null & 3";
		result = null;
		try {
			result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		}
		catch( ExpressionEngineException e ) {
			assertEquals( EOErrorCodes.INVALID_OPERAND_TYPE, e.getErrorCode() );
		}
		assertNull( result );
	}

	@Test
	public void test_bitwise_xor_expression() throws ExpressionEngineException {
		String expression = "1 ^ 0";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 1, result.longValue() );

		expression = "1 ^ 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 0, result.longValue() );

		expression = "0 ^ 0";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 0, result.longValue() );

		expression = "3 ^ 5";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 6, result.longValue() );

		expression = "null ^ 3";
		result = null;
		try {
			result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		}
		catch( ExpressionEngineException e ) {
			assertEquals( EOErrorCodes.INVALID_OPERAND_TYPE, e.getErrorCode() );
		}
		assertNull( result );
	}

	@Test
	public void testTernaryExpression() throws ExpressionEngineException {

		String expression = "1/2 == 1/2 ? 10 : 3";
		Object result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 10.0, ( (Number) result ).doubleValue(), 0 );

		expression = "7/3 >= 7 ? 'It is seven' : 'Not seven'";
		result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "Not seven", result );

	}

	// Application does not identify null as special keyword as of now
	@Test
	public void test_ternary_expression_null_if_true() throws ExpressionEngineException {
		String expression = "1 == 1 ? null : 'blah'";
		Object result = ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_ternary_expression_null_if_false() throws ExpressionEngineException {
		String expression = "1 == 2 ? 'blah' : null";
		Object result = ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void testXMLExpression() throws ExpressionEngineException {

		String expression = "xml('/new1/book/publisher/age/@value')";
		Object result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "50", result );

		expression = "(7/3 >= 7 ? 'It is seven' : 'Not seven') + trim(xml('/new1/book/publisher/weight'))";
		result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "Not seven72", result );

		expression = "(7/3 >= 7 ? 100 : 200) + trim(xml('/new1/book/publisher/weight'))";
		result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "20072", result );
	}

	@Test
	public void testComplexExpression() throws ExpressionEngineException {

		String expression = "((2+3) >> 2) + xml('/new1/book/publisher/age/@value') + sqrt(256) + (20/pow(2,2))";
		Object result = ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "15016.05.0", result );

		expression = "sqrt(256) - ((20/pow(2,2) + (round(2.6) * -2)) / (rates[1]) )";
		Number result1 = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 16.333333333333332, result1.doubleValue(), 0 );

	}

	@Test
	public void test_isnull_on_null_constant() throws ExpressionEngineException {
		String expression = "isnull(null)";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_isnull_on_null_string() throws ExpressionEngineException {
		String expression = "isnull(null_string)";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_isnull_on_non_null_string() throws ExpressionEngineException {
		String expression = "isnull(non_null_string)";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );
	}

	@Test
	public void test_iif_true_value_on_same_datatypes() throws ExpressionEngineException {
		String expression = "iif(true, 5, 6)";
		Long result = (Long) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( new Long( 5 ), result );
	}

	@Test
	public void test_iif_false_value_on_same_datatypes() throws ExpressionEngineException {
		String expression = "iif(false, 5.3, 6.7)";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( new Double( 6.7 ), result );
	}

	@Test
	public void test_iif_on_different_datatypes() throws ExpressionEngineException {
		String expression = "iif(true, 'wastrue', 6)";
		String result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "wastrue", result );
	}

	@Test
	public void test_iif_with_null() throws ExpressionEngineException {
		String expression = "iif(true, null, 6)";
		Long result = (Long) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( null, result );
	}

	@Test
	public void test_string_array() throws ExpressionEngineException {
		String expression = "_names[nameIndex]";
		String result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "Kris", result );
	}

	@Test
	public void test_sum_doubles_with_null() throws ExpressionEngineException {
		String expression = "sum(doublesWithNull)";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 5.0, result, 0 );
	}

	@Test
	public void test_sum_doubles_all_null() throws ExpressionEngineException {
		String expression = "sum(doublesAllNull)";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( null, result );
	}

	@Test
	public void test_null_equals_null() throws ExpressionEngineException {
		String expression = "null == null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_null_null_inequality() throws ExpressionEngineException {
		String expression = "null != null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );
	}

	@Test
	public void test_null_double_equality() throws ExpressionEngineException {
		String expression = "1.0 == null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );
	}

	@Test
	public void test_double_null_equality() throws ExpressionEngineException {
		String expression = "null == 1.0";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );
	}

	@Test
	public void test_String_null_equality() throws ExpressionEngineException {
		String expression = "'null' == null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );
	}

	@Test
	public void test_null_double_inequality() throws ExpressionEngineException {
		String expression = "null != 1.0";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_double_null_inequality() throws ExpressionEngineException {
		String expression = "1.0 != null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_null_string_inequality() throws ExpressionEngineException {
		String expression = "null != ''";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_string_null_inequality() throws ExpressionEngineException {
		String expression = "'funky' != null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_add_null_and_double() throws ExpressionEngineException {
		String expression = "null + 5.0";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_add_double_and_null() throws ExpressionEngineException {
		String expression = "5.0 + null";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_add_null_and_long() throws ExpressionEngineException {
		String expression = "null + 5";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_add_long_and_null() throws ExpressionEngineException {
		String expression = "5 + null";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_add_null_and_null() throws ExpressionEngineException {
		String expression = "null + null";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_add_double_null_and_double_null() throws ExpressionEngineException {
		String expression = "doublesAllNull[0] + doublesAllNull[1]";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_subtract_null_and_double() throws ExpressionEngineException {
		String expression = "null - 5.0";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_subtract_double_and_null() throws ExpressionEngineException {
		String expression = "5.0 - null";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_subtract_null_and_long() throws ExpressionEngineException {
		String expression = "null - 5";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_subtract_long_and_null() throws ExpressionEngineException {
		String expression = "5 - null";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_subtract_null_double_and_null_double() throws ExpressionEngineException {
		String expression = "doublesAllNull[0] - doublesAllNull[1]";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test(expected = ExpressionEngineException.class)
	public void test_subtract_null_and_string() throws ExpressionEngineException {
		String expression = "null - 'asdf'";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_multiply_null_and_double() throws ExpressionEngineException {
		String expression = "null * 5.0";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_multiply_double_and_null() throws ExpressionEngineException {
		String expression = "5.0 * null";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_multiply_null_and_long() throws ExpressionEngineException {
		String expression = "null * 5";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_multiply_long_and_null() throws ExpressionEngineException {
		String expression = "5 * null";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_multiply_null_double_and_null_double() throws ExpressionEngineException {
		String expression = "doublesAllNull[0] * doublesAllNull[1]";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_divide_null_and_double() throws ExpressionEngineException {
		String expression = "null / 5.0";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_divide_double_and_null() throws ExpressionEngineException {
		String expression = "5.0 / null";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_divide_null_and_long() throws ExpressionEngineException {
		String expression = "null / 5";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_divide_long_and_null() throws ExpressionEngineException {
		String expression = "5 / null";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_divide_null_double_and_null_double() throws ExpressionEngineException {
		String expression = "doublesAllNull[0] / doublesAllNull[1]";
		Double result = (Double) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_boolean_null_relational_expression() throws ExpressionEngineException {
		String expression = "true == null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );

		expression = "true != null";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );

		expression = "false == null";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );

		expression = "false != null";
		result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_LTE_expression_long_null() throws ExpressionEngineException {
		String expression = "1 <= null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_LTE_expression_null_long() throws ExpressionEngineException {
		String expression = "null <= 1";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_LTE_expression_double_null() throws ExpressionEngineException {
		String expression = "1.0 <= null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_LTE_expression_null_double() throws ExpressionEngineException {
		String expression = "null <= 1.0";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_LTE_expression_null_null() throws ExpressionEngineException {
		String expression = "null <= null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_LT_expression_long_null() throws ExpressionEngineException {
		String expression = "1 < null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_LT_expression_null_long() throws ExpressionEngineException {
		String expression = "null < 1";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_LT_expression_double_null() throws ExpressionEngineException {
		String expression = "1.0 < null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_LT_expression_null_double() throws ExpressionEngineException {
		String expression = "null < 1.0";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_LT_expression_null_null() throws ExpressionEngineException {
		String expression = "null < null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GTE_expression_long_null() throws ExpressionEngineException {
		String expression = "1 >= null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GTE_expression_null_long() throws ExpressionEngineException {
		String expression = "null >= 1";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GTE_expression_double_null() throws ExpressionEngineException {
		String expression = "1.0 >= null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GTE_expression_null_double() throws ExpressionEngineException {
		String expression = "null >= 1.0";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GTE_expression_null_null() throws ExpressionEngineException {
		String expression = "null >= null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GT_expression_long_null() throws ExpressionEngineException {
		String expression = "1 > null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GT_expression_null_long() throws ExpressionEngineException {
		String expression = "null > 1";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GT_expression_double_null() throws ExpressionEngineException {
		String expression = "1.0 > null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GT_expression_null_double() throws ExpressionEngineException {
		String expression = "null > 1.0";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_GT_expression_null_null() throws ExpressionEngineException {
		String expression = "null > null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_And_expression_true_true() throws ExpressionEngineException {
		String expression = "true && true";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_And_expression_false_false() throws ExpressionEngineException {
		String expression = "false && false";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );
	}

	@Test
	public void test_And_expression_true_false() throws ExpressionEngineException {
		String expression = "true && false";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );
	}

	@Test
	public void test_And_expression_false_true() throws ExpressionEngineException {
		String expression = "false && true";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );
	}

	@Test
	public void test_And_expression_true_null_constant() throws ExpressionEngineException {
		String expression = "true && null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_And_expression_true_null_boolean() throws ExpressionEngineException {
		String expression = "true && booleansAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_And_expression_false_null_constant() throws ExpressionEngineException {
		String expression = "false && null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_And_expression_false_null_boolean() throws ExpressionEngineException {
		String expression = "false && booleansAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_And_expression_false_null_constant_null_constant() throws ExpressionEngineException {
		String expression = "null && null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_And_expression_false_null_boolean_null_boolean() throws ExpressionEngineException {
		String expression = "booleansAllNull[0] && booleansAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Or_expression_true_true() throws ExpressionEngineException {
		String expression = "true || true";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_Or_expression_false_false() throws ExpressionEngineException {
		String expression = "false || false";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertFalse( result );
	}

	@Test
	public void test_Or_expression_true_false() throws ExpressionEngineException {
		String expression = "true || false";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_Or_expression_false_true() throws ExpressionEngineException {
		String expression = "false || true";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertTrue( result );
	}

	@Test
	public void test_Or_expression_true_null_constant() throws ExpressionEngineException {
		String expression = "true || null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Or_expression_true_null_boolean() throws ExpressionEngineException {
		String expression = "true || booleansAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Or_expression_false_null_constant() throws ExpressionEngineException {
		String expression = "false || null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Or_expression_false_null_boolean() throws ExpressionEngineException {
		String expression = "false || booleansAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Or_expression_false_null_constant_null_constant() throws ExpressionEngineException {
		String expression = "null || null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Or_expression_false_null_boolean_null_boolean() throws ExpressionEngineException {
		String expression = "booleansAllNull[0] || booleansAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Not_expression_null_boolean() throws ExpressionEngineException {
		String expression = "!booleansAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Not_expression_null_constant() throws ExpressionEngineException {
		String expression = "!null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Plus_expression_null_constant() throws ExpressionEngineException {
		String expression = "+null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Plus_expression_null_double() throws ExpressionEngineException {
		String expression = "+doublesAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Plus_expression_null_long() throws ExpressionEngineException {
		String expression = "+longsAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Minus_expression_null_constant() throws ExpressionEngineException {
		String expression = "-null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Minus_expression_null_double() throws ExpressionEngineException {
		String expression = "-doublesAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_Minus_expression_null_long() throws ExpressionEngineException {
		String expression = "-longsAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_remainder_expression_long_null_long() throws ExpressionEngineException {
		String expression = "3 % longsAllNull[0]";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_remainder_expression_long_null_constant() throws ExpressionEngineException {
		String expression = "3 % null";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_remainder_expression_long_long_null() throws ExpressionEngineException {
		String expression = "longsAllNull[0] % 3";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

	@Test
	public void test_remainder_expression_null_constant_long() throws ExpressionEngineException {
		String expression = "null % 3";
		Boolean result = (Boolean) ExpressionEngine.evaluate( expression, expressionContext );
		assertNull( result );
	}

}