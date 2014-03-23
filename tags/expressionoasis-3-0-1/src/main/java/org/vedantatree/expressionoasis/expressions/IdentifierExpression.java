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
package org.vedantatree.expressionoasis.expressions;

import java.util.Iterator;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.extensions.VariableProvider;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;
import org.vedantatree.utils.StringUtils;


/**
 * This is the expression for identifier. This identifier can be used as
 * variable or any reference name to get the corresponding value.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 *
 * Modified to support visitor design pattern.
 * 
 * @author Kris Marwood
 * @version 1.1
 */
public class IdentifierExpression implements Expression {

	/**
	 * This is the name of identifier.
	 */
	private String		   identifierName;

	/**
	 * This is the variableProvider of identifier.
	 */
	private VariableProvider variableProvider;

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		return variableProvider == null ? null : variableProvider.getVariableType( getIdentifierName() );
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		if( variableProvider == null ) {
			throw new ExpressionEngineException( "Variable Provider does not exist: [" + getIdentifierName() + "]" );
		}

		return variableProvider.getVariableValue( getIdentifierName() );
	}

	/**
	 * Initializes the identifier name
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.Expression#initialize(org.vedantatree.expressionoasis.ExpressionContext,
	 *      java.lang.Object)
	 */
	public void initialize( ExpressionContext expressionContext, Object parameters, boolean validate )
			throws ExpressionEngineException {
		this.identifierName = (String) parameters;

		if( !StringUtils.isQualifiedString( this.identifierName ) ) {
			throw new ExpressionEngineException( "Identifier name is not valid" );
		}

		// Initializes the variable provider.
		for( Iterator variableProviders = expressionContext.getVariableProviders().iterator(); variableProviders
				.hasNext(); ) {
			VariableProvider variableProvider = (VariableProvider) variableProviders.next();

			if( variableProvider.supportsVariable( getIdentifierName() ) ) {
				this.variableProvider = variableProvider;
				break;
			}
		}
	}

	/**
	 * Gets the name of identifier.
	 * 
	 * @return Returns the identifierName.
	 */
	public String getIdentifierName() {
		return identifierName;
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#uninitialize(org.vedantatree.expressionoasis.ExpressionContext)
	 */
	public void uninitialize( ExpressionContext expressionContext ) {
		this.identifierName = null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IdentifierExpression:[" + getIdentifierName() + "]";
	}

	/**
	* Allows an expression visitor to visit this expression and it's sub-expressions (implements Visitor design pattern).
	* @see org.vedantatree.expressionoasis.expressions.Expression#accept(org.vedantatree.expressionoasis.ExpressionVisitor)
	*/
	public void accept( ExpressionVisitor visitor ) {
		visitor.visit( this );
	}
}