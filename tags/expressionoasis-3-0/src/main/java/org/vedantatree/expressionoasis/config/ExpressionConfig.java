/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.

 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


/**
 * Represents the configuration for an Expression
 *
 * @author Kris Marwood
 */
@Element(name = "expression")
public class ExpressionConfig {

	@Attribute(name = "name")
	private String expressionName;

	@Attribute(name = "className")
	private String className;

	@Attribute(name = "type")
	private String expressionType;

	/**
	 * @return the expressionName
	 */
	public String getExpressionName() {
		return expressionName;
	}

	/**
	 * @return the className
	 */
	public Class getExpressionClass() {
		Class expressionClass = null;
		try {
			expressionClass = Class.forName( className );
		}
		catch( ClassNotFoundException e ) {
			throw new RuntimeException( "Error loading expression class " + e.getMessage(), e );
		}
		return expressionClass;
	}

	/**
	 * @return the expressionType
	 */
	public String getExpressionType() {
		return expressionType;
	}
}
