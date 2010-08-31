/**
 * Created on Jan 13, 2006
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
package org.ganges.expressionengine.expressions;

import java.util.Iterator;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.extensions.VariableProvider;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;
import org.ganges.utils.StringUtils;


/**
 * This is the expression for identifier. This identifier can be used as
 * variable or any reference name to get the corresponding value.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
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
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	public Type getReturnType() throws ExpressionEngineException {
		return variableProvider == null ? null : variableProvider.getVariableType( getIdentifierName() );
	}

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
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
	 * @see org.ganges.expressionengine.expressions.Expression#initialize(org.ganges.expressionengine.ExpressionContext,
	 *      java.lang.Object)
	 */
	public void initialize( ExpressionContext expressionContext, Object parameters ) throws ExpressionEngineException {
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
	 * @see org.ganges.expressionengine.expressions.Expression#uninitialize(org.ganges.expressionengine.ExpressionContext)
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
}