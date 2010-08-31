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
package org.vedantatree.expressionoasis.grammar.rules;

import java.util.regex.Pattern;

import org.vedantatree.utils.StringUtils;


/**
 * This class provides the production rule based on regular expression. It uses
 * the regular expression to define a production rule.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 *
 * Modified to compile and reuse approachable and allowed regular expressions in
 * order to improve performance.
 *
 * @author Kris Marwood
 * @version 1.1
 */
public class ProductionRule implements IProductionRule {

	/**
	 * This is the name of production rule.
	 */
	private String  name;

	/**
	 * This is the regular expression for this production rule to identify the
	 * approachable tokens
	 */
	private Pattern approachableRegexPattern;

	/**
	 * This is the regular expression for this production rule to identify the
	 * allowed tokens
	 */
	private Pattern allowedRegexPattern;

	/**
	 * Constructs the ProductionRule
	 * 
	 * @param name name of the production rule
	 * @param regularExpression regular expression, will be used as approachable 
	 * 		  and allowed regular expression
	 * @throws IllegalArgumentException if the parameters are not valid
	 */
	public ProductionRule( String name, String regularExpression ) {
		this( name, regularExpression, regularExpression );
	}

	/**
	 * Constructs the ProductionRule
	 * 
	 * @param name name of the production rule
	 * @param approachableRegex Regular Expression for checking the
	 * 		  approachable token
	 * @param allowedRegex Expression for checking the
	 * 		  approachable token
	 * @throws IllegalArgumentException if the parameters are not valid
	 */
	public ProductionRule( String name, String approachableRegex, String allowedRegex ) {
		if( !StringUtils.isQualifiedString( approachableRegex ) || !StringUtils.isQualifiedString( allowedRegex ) ) {
			throw new IllegalArgumentException( "Passed regular expression can't be null." );
		}

		this.name = name;
		this.allowedRegexPattern = Pattern.compile( allowedRegex );
		this.approachableRegexPattern = Pattern.compile( approachableRegex );
	}

	/**
	 * @see org.vedantatree.expressionoasis.grammar.rules.IProductionRule#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see org.vedantatree.expressionoasis.grammar.rules.IProductionRule#isApproaching(java.lang.String)
	 */
	public boolean isApproaching( String pattern ) {
		boolean approaching = approachableRegexPattern.matcher( pattern ).matches();
		return approaching;
	}

	/**
	 * @see org.vedantatree.expressionoasis.grammar.rules.IProductionRule#isAllowed(java.lang.String)
	 */
	public boolean isAllowed( String token ) {
		boolean allowed = allowedRegexPattern.matcher( token ).matches();
		return allowed;
	}
}