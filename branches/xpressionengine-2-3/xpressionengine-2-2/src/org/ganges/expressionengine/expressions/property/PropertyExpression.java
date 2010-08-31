/**
 * Created on Jan 26, 2006
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
package org.ganges.expressionengine.expressions.property;

import java.beans.IntrospectionException;

import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.expressionengine.expressions.BinaryOperatorExpression;
import org.ganges.expressionengine.expressions.IdentifierExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;
import org.ganges.utils.BeanUtils;


/**
 * This is the property expression to access the property of any java object.
 * 
 * @author Parmod Kamboj
 * @author Mohit Gupta
 * @version 1.0
 */
public class PropertyExpression extends BinaryOperatorExpression {

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
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
	 * @see org.ganges.expressionengine.expressions.BinaryOperatorExpression#validate()
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
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
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