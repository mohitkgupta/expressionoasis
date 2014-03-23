/**
 * Copyright (c) 2006 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.

 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.

 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.expressions.property;

import java.beans.IntrospectionException;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression;
import org.vedantatree.expressionoasis.expressions.IdentifierExpression;
import org.vedantatree.types.Type;
import org.vedantatree.types.ValueObject;
import org.vedantatree.utils.BeanUtils;


/**
 * This is the property expression to access the property of any java object.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 */
public class PropertyExpression extends BinaryOperatorExpression {

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		ValueObject value = leftOperandExpression.getValue();
		Type type = leftOperandExpression.getReturnType();
		ValueObject returnValue = null;

		try {
			Class clazz = Class.forName( type.getTypeName() );

			// Right expression must be Identifier expression.
			String propertyName = ( (IdentifierExpression) rightOperandExpression ).getIdentifierName();
			Object propertyValue = BeanUtils.getPropertyValue( value.getValue(), propertyName );
			Class propertyType = BeanUtils.getPropertyType( clazz, propertyName );
			returnValue = new ValueObject( propertyValue, Type.createType( propertyType ) );
		}
		catch( ClassNotFoundException ex ) {
			throw new ExpressionEngineException( type.getTypeName() + " class is not found." );
		}
		catch( Exception ex ) {
			throw new ExpressionEngineException( ex.getMessage() );
		}

		return returnValue;
	}

	/**
	 * Sets the value
	 * 
	 * @param value
	 * @throws ExpressionEngineException
	 */
	public void setValue( Object value ) throws ExpressionEngineException {
		ValueObject valueObject = leftOperandExpression.getValue();

		try {
			// Right expression must be Identifier expression.
			String propertyName = ( (IdentifierExpression) rightOperandExpression ).getIdentifierName();
			BeanUtils.setPropertyValue( valueObject.getValue(), propertyName, value );
		}
		catch( Exception ex ) {
			throw new ExpressionEngineException( ex.getMessage() );
		}
	}

	/**
	 * Default validation is not required.
	 * 
	 * @see org.vedantatree.expressionoasis.expressions.BinaryOperatorExpression#validate()
	 */
	@Override
	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException {
		Type type = leftOperandExpression.getReturnType();

		if( type == null ) {
			throw new ExpressionEngineException( "Return type of left operand expression: [" + leftOperandExpression
					+ "] is null." );
		}

		try {
			Class clazz = Class.forName( type.getTypeName() );

			// Right expression must be Identifier expression.
			String propertyName = ( (IdentifierExpression) rightOperandExpression ).getIdentifierName();
			Class propertyType = BeanUtils.getPropertyType( clazz, propertyName );

			if( propertyType == null ) {
				throw new ExpressionEngineException( "Property [" + propertyName + "] does not exist in " + clazz );
			}
		}
		catch( ClassNotFoundException ex ) {
			throw new ExpressionEngineException( type.getTypeName() + " class is not found." );
		}
		catch( IntrospectionException ex ) {
			throw new ExpressionEngineException( ex.getMessage() );
		}
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	@Override
	public Type getReturnType() throws ExpressionEngineException {
		Type returnType = null;
		Type type = leftOperandExpression.getReturnType();

		try {
			Class clazz = Class.forName( type.getTypeName() );

			// Right expression must be Identifier expression.
			String propertyName = ( (IdentifierExpression) rightOperandExpression ).getIdentifierName();
			Class propertyType = BeanUtils.getPropertyType( clazz, propertyName );
			returnType = Type.createType( propertyType );
		}
		catch( ClassNotFoundException ex ) {
			throw new ExpressionEngineException( type.getTypeName() + " class is not found." );
		}
		catch( IntrospectionException ex ) {
			throw new ExpressionEngineException( ex.getMessage() );
		}

		return returnType;
	}
}