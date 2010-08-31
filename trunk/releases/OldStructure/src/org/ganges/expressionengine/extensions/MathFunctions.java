/**
 * Created on Jan 5, 2006
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
package org.ganges.expressionengine.extensions;

import org.ganges.expressionengine.ExpressionContext;

/**
 * This class provides the math function for experssion evaluator
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 2.2
 */
public class MathFunctions {
	
	
	/**
	 * Default constructor
	 */
	public MathFunctions (ExpressionContext expressionContext)
	{
	}


	/**
	 * Returns the absolute value
	 * 
	 * @param value
	 * @return
	 */
	public static double abs( double value ) {
		return Math.abs( value );
	}

	/**
	 * Returns the minimum of two numbers.
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double min( double value1, double value2 ) {
		return Math.min( value1, value2 );
	}

	/**
	 * Returns the maximum of two numbers.
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double max( double value1, double value2 ) {
		return Math.max( value1, value2 );
	}

	/**
	 * Returns the trigonometric sine of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static double sin( double value ) {
		return Math.sin( value );
	}

	/**
	 * Returns the trigonometric cosine of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static double cos( double value ) {
		return Math.cos( value );
	}

	/**
	 * Returns the trigonometric tangent of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static double tan( double value ) {
		double result = Math.tan( value );
		return result;
	}

	/**
	 * Returns the trigonometric arc sine of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static double asin( double value ) {
		return Math.asin( value );
	}

	/**
	 * Returns the trigonometric arc cosine of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static double acos( double value ) {
		return Math.acos( value );
	}

	/**
	 * Returns the trigonometric arc tangent of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static double atan( double value ) {
		return Math.atan( value );
	}

	/**
	 * Converts rectangular coordinates (<code>x</code> ,&nbsp;
	 * <code>y</code>) to polar (r,&nbsp; <i>theta </i>).
	 * 
	 * @param y
	 * @param x
	 * @return
	 */
	public static double atan2( double y, double x ) {
		return Math.atan2( y, x );
	}

	/**
	 * Returns Euler's number <i>e </i> raised to the power of a
	 * <code>double</code> value. Special cases:
	 * 
	 * @param value
	 *            the exponent to raise <i>e </i> to.
	 * @return
	 */
	public static double exp( double value ) {
		return Math.exp( value );
	}

	/**
	 * Computes the power
	 * 
	 * @param base
	 * @param exponent
	 * @return
	 */
	public static double pow( double base, double exponent ) {
		return Math.pow( base, exponent );
	}

	/**
	 * Returns the natural logarithm (base <i>e </i>) of a <code>double</code>
	 * value.
	 * 
	 * @param value
	 *            a value
	 * @return the value ln&nbsp; <code>a</code>, the natural logarithm of
	 *         <code>a</code>.
	 */
	public static double log( double value ) {
		return Math.log( value );
	}

	/**
	 * Returns the correctly rounded positive square root of a
	 * <code>double</code> value.
	 * 
	 * @param value
	 *            a value.
	 * @return the positive square root of <code>a</code>. If the argument is
	 *         NaN or less than zero, the result is NaN.
	 */
	public static double sqrt( double value ) {
		return Math.sqrt( value );
	}

	/**
	 * Returns the smallest (closest to negative infinity) <code>double</code>
	 * value that is greater than or equal to the argument and is equal to a
	 * mathematical integer.
	 * 
	 * @param a
	 *            a value.
	 * @return
	 */
	public static double ceil( double a ) {
		return Math.ceil( a );
	}

	/**
	 * Returns the largest (closest to positive infinity) <code>double</code>
	 * value that is less than or equal to the argument and is equal to a
	 * mathematical integer.
	 * 
	 * @param value
	 *            a value.
	 * @return
	 */
	public static double floor( double value ) {
		return StrictMath.floor( value );
	}

	/**
	 * Returns the <code>double</code> value that is closest in value to the
	 * argument and is equal to a mathematical integer. If two
	 * <code>double</code> values that are mathematical integers are equally
	 * close, the result is the integer value that is even.
	 * 
	 * @param value
	 *            a <code>double</code> value.
	 * @return
	 */
	public static double rint( double value ) {
		return Math.rint( value );
	}

	/**
	 * Returns the closest <code>int</code> to the argument. The result is
	 * rounded to an integer by adding 1/2, taking the floor of the result, and
	 * casting the result to type <code>int</code>. In other words, the
	 * result is equal to the value of the expression:
	 * <p>
	 * 
	 * <pre>
	 * (int) Math.floor(a + 0.5f)
	 * </pre>
	 * 
	 * <p>
	 * 
	 * @param a
	 *            a floating-point value to be rounded to an integer.
	 * @return the value of the argument rounded to the nearest <code>int</code>
	 *         value.
	 */
	public static long round( double value ) {
		return Math.round( value );
	}

	/**
	 * Returns a <code>double</code> value with a positive sign, greater than
	 * or equal to <code>0.0</code> and less than <code>1.0</code>.
	 * Returned values are chosen pseudorandomly with (approximately) uniform
	 * distribution from that range.
	 * 
	 * @return a pseudorandom <code>double</code> greater than or equal to
	 *         <code>0.0</code> and less than <code>1.0</code>.
	 */
	public static double random() {
		return Math.random();
	}
}