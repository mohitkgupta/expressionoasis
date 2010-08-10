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

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.extensions.DefaultVariableProvider;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;


/**
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * 
 * Testcase for expression evaluator.
 */
public class TestEvaluator extends TestCase {

	/**
	 * The expression context used by evaluator.
	 */
	private ExpressionContext expressionContext;

	/**
	 * Runs the test for parser.
	 * 
	 * @param args
	 * @throws ExpressionEngineException
	 *             if unable to parse
	 */
	public static void main( String[] args ) throws ExpressionEngineException {
		TestRunner.run( TestEvaluator.class );
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		expressionContext = new ExpressionContext();

		DefaultVariableProvider dvp = new DefaultVariableProvider();
		dvp.addVariable( "principle", new ValueObject( new Double( 100.00 ), Type.DOUBLE ) );
		dvp.addVariable( "rate", new ValueObject( new Double( 10.00 ), Type.DOUBLE ) );
		dvp.addVariable( "time", new ValueObject( new Double( 2.00 ), Type.DOUBLE ) );
		dvp.addVariable( "rates", new ValueObject( new double[] { 2.00, 3.00 }, Type.createType( double[].class ) ) );
		dvp.addVariable( "_rates", new ValueObject( new double[][] { new double[] { 2.00, 3.00 },
				new double[] { 4.00, 5.00 } }, Type.createType( double[][].class ) ) );
		dvp.addVariable( "object", new ValueObject( new Object(), Type.createType( Object.class ) ) );
		dvp.addVariable( "person", new ValueObject( new Person( "Mohit", 31 ), Type.createType( Person.class ) ) );
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
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		expressionContext = null;
	}

	public void testAritmaticExpression() throws ExpressionEngineException {
		String expression = "-10 / 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( -5, result.intValue() );

		expression = "10 + (10 / 2) - 10 * 2 + 10 % 6";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( -1, result.intValue() );

		expression = "20 - (10/-2 + (-5 * -2)) / (15 * (-5/5) )";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 20.333333333333332, result.doubleValue() );
	}

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
	}

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

	public void testVariableExpression() throws ExpressionEngineException {
		String expression = "(principle * rate * time) / 100";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "value is not matching", 20.0, result.doubleValue(), 0 );
	}

	public void testFunctionExpression() throws ExpressionEngineException {
		String expression = "abs(-4.5) + 1";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 5.5, result.doubleValue() );

		expression = "min(3, 9) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 4.0, result.doubleValue() );

		expression = "max(3, 9) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 10.0, result.doubleValue() );

		expression = "sin(90) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 1.8939966636005579, result.doubleValue() );

		expression = "cos(0) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.0, result.doubleValue() );

		expression = "tan(45) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.6197751905438613, result.doubleValue() );

		expression = "asin(0.8) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 1.9272952180016123, result.doubleValue() );

		expression = "acos(0) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.5707963267948966, result.doubleValue() );

		expression = "atan(45) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.5485777614681773, result.doubleValue() );

		expression = "atan2(45, 2) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.5263811115479857, result.doubleValue() );

		expression = "exp(2) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 8.38905609893065, result.doubleValue() );

		expression = "pow(2, 8) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 257.0, result.doubleValue() );

		expression = "log(4) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.386294361119891, result.doubleValue() );

		expression = "sqrt(256) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 17.0, result.doubleValue() );

		expression = "ceil(2.7) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 4.0, result.doubleValue() );

		expression = "floor(2.3) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 3.0, result.doubleValue() );

		expression = "rint(2.43) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 3.0, result.doubleValue() );

		expression = "round(2.43) + 1";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 3.0, result.doubleValue() );

		expression = "random()";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( true, result.doubleValue() != 0 );
	}

	public void testArrayExpression() throws ExpressionEngineException {
		String expression = "rates[1] * 10 - _rates[0][0] / 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "value is not matching", 29.0, result.doubleValue(), 0 );
	}

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

	public void testBitwiseExpression() throws ExpressionEngineException {

		String expression = "3 << 2";
		Number result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 12.0, result.doubleValue() );

		expression = "16 >> 2";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 4.0, result.doubleValue() );

		expression = "15 >>> 2";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 3.0, result.doubleValue() );

		expression = "4 | 2";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 6.0, result.doubleValue() );

		expression = "3 & 2";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 2.0, result.doubleValue() );

		expression = "~2";
		result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( -3.0, result.doubleValue() );
	}

	public void testTernaryExpression() throws ExpressionEngineException {

		String expression = "1/2 == 1/2 ? 10 : 3";
		Object result = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 10.0, ( (Number) result ).doubleValue() );

		expression = "7/3 >= 7 ? 'It is seven' : 'Not seven'";
		result = (String) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "Not seven", result );
	}

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

	public void testComplexExpression() throws ExpressionEngineException {

		String expression = "((2+3) >> 2) + xml('/new1/book/publisher/age/@value') + sqrt(256) + (20/pow(2,2))";
		Object result = ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( "15016.05.0", result );

		expression = "sqrt(256) - ((20/pow(2,2) + (round(2.6) * -2)) / (rates[1]) )";
		Number result1 = (Number) ExpressionEngine.evaluate( expression, expressionContext );
		assertEquals( 16.333333333333332, result1.doubleValue() );

	}
}