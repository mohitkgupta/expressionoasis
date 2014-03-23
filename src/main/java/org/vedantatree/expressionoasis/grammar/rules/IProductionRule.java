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
package org.vedantatree.expressionoasis.grammar.rules;

/**
 * This class represents the interface to define the production rule for any
 * grammar. It provides the way to define a valid token for the parser.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public interface IProductionRule {

	/**
	 * Returns the name of production rule.
	 * 
	 * @return the name of production rule
	 */
	String getName();

	/**
	 * Checks whether the given token is approachable using any of the pattern
	 * or not. 
	 * 
	 * Given token can be partially or fully constructed token during 
	 * parsing process. Parser/DefaultXMLGrammar generally calls this method to check whether the 
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
	boolean isApproaching( String pattern );

	/**
	 * Checks whether the token is allowed or not. 
	 * 
	 * A token is fully constructed token. DefaultXMLGrammar/Parser generally calls this method to 
	 * check whether the current token is a valid token as per the production 
	 * rules or not.
	 * 
	 * @param token the token which is to be checked for its validity
	 * @return <code>true</code> if the token is allowed <code>false</code>
	 *         otherwise.
	 */
	boolean isAllowed( String token );
}