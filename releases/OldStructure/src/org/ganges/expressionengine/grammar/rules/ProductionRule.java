/**
 * Created on Jan 2, 2006
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
package org.ganges.expressionengine.grammar.rules;

import java.util.regex.Pattern;

import org.ganges.utils.StringUtils;


/**
 * This class provides the production rule based on regular expression. It uses
 * the regular expression to define a production rule.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 */
public class ProductionRule implements IProductionRule {

	/**
	 * This is the name of production rule.
	 */
	private String name;

	/**
	 * This is the regular expression for this production rule to identify the
	 * approachable tokens
	 */
	private String approachableRegExpression;

	/**
	 * This is the regular expression for this production rule to identify the
	 * allowed tokens
	 */
	private String allowedRegExpression;

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
	 * @param approachableRegExpression regular Expression for checking the 
	 * 		  approachable token
	 * @param allowedRegExpressionregular Expression for checking the 
	 * 		  approachable token
	 * @throws IllegalArgumentException if the parameters are not valid
	 */
	public ProductionRule( String name, String appRegularExpression, String allowedRegularExpression ) {
		if( !StringUtils.isQualifiedString( appRegularExpression )
				|| !StringUtils.isQualifiedString( allowedRegularExpression ) ) {
			throw new IllegalArgumentException( "Passed regular expression can't be null." );
		}

		this.name = name;
		this.allowedRegExpression = allowedRegularExpression;
		this.approachableRegExpression = appRegularExpression;
	}

	/**
	 * @see org.ganges.expressionengine.grammar.rules.IProductionRule#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see org.ganges.expressionengine.grammar.rules.IProductionRule#isApproaching(java.lang.String)
	 */
	public boolean isApproaching( String pattern ) {
		boolean approaching = Pattern.matches( approachableRegExpression, pattern );
		return approaching;
	}

	/**
	 * @see org.ganges.expressionengine.grammar.rules.IProductionRule#isAllowed(java.lang.String)
	 */
	public boolean isAllowed( String token ) {
		boolean allowed = Pattern.matches( allowedRegExpression, token );
		return allowed;
	}
}