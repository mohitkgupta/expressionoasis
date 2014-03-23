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
import org.ganges.expressionengine.expressions.IdentifierExpression;
import org.ganges.expressionengine.expressions.UnaryOperatorExpression;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;
import org.ganges.utils.BeanUtils;


/**
 * This is the unary expression which access the property of a java object, 
 * and assume this java object with context object
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public class UnaryPropertyExpression extends UnaryOperatorExpression {

	/**
	 * This is the java bean context property.
	 */
	private static final String JAVA_BEAN = "JAVA_BEAN";

	/**
	 * This is the java bean.
	 */
	private Object			  bean;

	/**
	 * This is the property name
	 */
	private String			  propertyName;

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException {
		ValueObject returnValue = null;

		try {
			Object propertyValue = BeanUtils.getPropertyValue( bean, propertyName );
			Class propertyType = BeanUtils.getPropertyType( bean.getClass(), propertyName );
			returnValue = new ValueObject( propertyValue, Type.createType( propertyType ) );
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
		try {
			BeanUtils.setPropertyValue( bean, propertyName, value );
		}
		catch( Exception ex ) {
			throw new ExpressionEngineException( ex.getMessage() );
		}
	}

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#initialize(org.ganges.expressionengine.ExpressionContext,
	 *      java.lang.Object)
	 */
	@Override
	public void initialize( ExpressionContext expressionContext, Object parameters ) throws ExpressionEngineException {
		bean = expressionContext.getContextProperty( JAVA_BEAN );
		this.propertyName = ( (IdentifierExpression) parameters ).getIdentifierName();
		super.initialize( expressionContext, parameters );
	}

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#getReturnType()
	 */
	@Override
	public Type getReturnType() throws ExpressionEngineException {
		Type returnType = null;

		try {
			Class propertyType = BeanUtils.getPropertyType( bean.getClass(), propertyName );
			returnType = Type.createType( propertyType );
		}
		catch( IntrospectionException ex ) {
			throw new ExpressionEngineException( ex.getMessage() );
		}

		return returnType;
	}

	/**
	 * @see org.ganges.expressionengine.expressions.UnaryOperatorExpression#validate()
	 */
	@Override
	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException {
		if( bean == null ) {
			throw new ExpressionEngineException( "Bean is not found. Set context property [JAVA_BEAN]" );
		}

		try {
			Class clazz = bean.getClass();
			Class propertyType = BeanUtils.getPropertyType( clazz, propertyName );

			if( propertyType == null ) {
				throw new ExpressionEngineException( "Property [" + propertyName + "] does not exist in " + clazz );
			}
		}
		catch( IntrospectionException ex ) {
			throw new ExpressionEngineException( ex.getMessage() );
		}
	}

	/**
	 * @see org.ganges.expressionengine.expressions.Expression#uninitialize(org.ganges.expressionengine.ExpressionContext)
	 */
	@Override
	public void uninitialize( ExpressionContext expressionContext ) {
		super.uninitialize( expressionContext );
		this.bean = null;
		this.propertyName = null;
	}
}