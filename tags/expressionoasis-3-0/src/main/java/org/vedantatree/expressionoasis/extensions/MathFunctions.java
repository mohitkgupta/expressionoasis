/**
 * Copyright (c) 2006 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.extensions;

import org.vedantatree.expressionoasis.ExpressionContext;


/**
 * This class provides the math function for experssion evaluator
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 2.2
 *
 * Changed to use nullable types.
 *
 * @author Kris Marwood
 * @version 2.3
 */
public class MathFunctions {

	/**
	 * Default constructor
	 */
	public MathFunctions( ExpressionContext expressionContext ) {
	}

	/**
	 * Returns the absolute value
	 * 
	 * @param value
	 * @return
	 */
	public static Double abs( Double value ) {
		return value == null ? null : Math.abs( value );
	}

	public static Long abs( Long value ) {
		return value == null ? null : Math.abs( value );
	}

	/**
	 * Returns the minimum of two numbers.
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static Double min( Double value1, Double value2 ) {
		return value1 == null || value2 == null ? null : Math.min( value1, value2 );
	}

	/**
	 * Returns the minimum of two numbers.
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static Long min( Long value1, Long value2 ) {
		return value1 == null || value2 == null ? null : Math.min( value1, value2 );
	}

	/**
	 * Returns the maximum of two numbers.
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static Double max( Double value1, Double value2 ) {
		return value1 == null || value2 == null ? null : Math.max( value1, value2 );
	}

	/**
	 * Returns the maximum of two numbers.
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static Long max( Long value1, Long value2 ) {
		return value1 == null || value2 == null ? null : Math.max( value1, value2 );
	}

	/**
	 * Returns the trigonometric sine of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static Double sin( Number value ) {
		return value == null ? null : Math.sin( value.doubleValue() );
	}

	/**
	 * Returns the trigonometric cosine of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static Double cos( Number value ) {
		return value == null ? null : Math.cos( value.doubleValue() );
	}

	/**
	 * Returns the trigonometric tangent of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static Double tan( Number value ) {
		return value == null ? null : Math.tan( value.doubleValue() );
	}

	/**
	 * Returns the trigonometric arc sine of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static Double asin( Number value ) {
		return value == null ? null : Math.asin( value.doubleValue() );
	}

	/**
	 * Returns the trigonometric arc cosine of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static Double acos( Number value ) {
		return value == null ? null : Math.acos( value.doubleValue() );
	}

	/**
	 * Returns the trigonometric arc tangent of an angle
	 * 
	 * @param value
	 * @return
	 */
	public static Double atan( Number value ) {
		return value == null ? null : Math.atan( value.doubleValue() );
	}

	/**
	 * Converts rectangular coordinates (<code>x</code> ,&nbsp;
	 * <code>y</code>) to polar (r,&nbsp; <i>theta </i>).
	 * 
	 * @param y
	 * @param x
	 * @return
	 */
	public static Double atan2( Number y, Number x ) {
		return y == null || x == null ? null : Math.atan2( y.doubleValue(), x.doubleValue() );
	}

	/**
	 * Returns Euler's number <i>e </i> raised to the power of a
	 * <code>double</code> value. Special cases:
	 * 
	 * @param value
	 *            the exponent to raise <i>e </i> to.
	 * @return
	 */
	public static Double exp( Number value ) {
		return value == null ? null : Math.exp( value.doubleValue() );
	}

	/**
	 * Computes the power
	 * 
	 * @param base
	 * @param exponent
	 * @return
	 */
	public static Double pow( Number base, Number exponent ) {
		return base == null || exponent == null ? null : Math.pow( base.doubleValue(), exponent.doubleValue() );
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
	public static Double log( Number value ) {
		return value == null ? null : Math.log( value.doubleValue() );
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
	public static Double sqrt( Number value ) {
		return value == null ? null : Math.sqrt( value.doubleValue() );
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
	public static Double ceil( Number value ) {
		return value == null ? null : Math.ceil( value.doubleValue() );
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
	public static Double floor( Number value ) {
		return value == null ? null : StrictMath.floor( value.doubleValue() );
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
	public static Double rint( Double value ) {
		return value == null ? null : Math.rint( value );
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
	public static Long round( Double value ) {
		return value == null ? null : Math.round( value );
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
	public static Double random() {
		return Math.random();
	}

	public Double sum( Double[] vals ) {
		Double result = null;
		for( int i = 0; i < vals.length; i++ ) {
			if( vals[i] != null ) {
				if( result == null ) {
					result = vals[i];
				}
				else {
					result += vals[i];
				}
			}
		}
		return result;
	}
}