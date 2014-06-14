/**	
 *  Copyright (c) 2005-2014 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software. You can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL 
 *  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES 
 *  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE 
 *  OR OTHER DEALINGS IN THE SOFTWARE.See the GNU Lesser General Public License 
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis. If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Please consider to contribute any enhancements to upstream codebase. 
 *  It will help the community in getting improved code and features, and 
 *  may help you to get the later releases with your changes.
 */
package org.vedantatree.expressionoasis.expressions.property;

import java.beans.IntrospectionException;

import org.vedantatree.expressionoasis.ExpressionContext;
import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;
import org.vedantatree.expressionoasis.expressions.IdentifierExpression;
import org.vedantatree.expressionoasis.expressions.UnaryOperatorExpression;
import org.vedantatree.expressionoasis.types.Type;
import org.vedantatree.expressionoasis.types.ValueObject;
import org.vedantatree.expressionoasis.utils.BeanUtils;


/**
 * This is the unary expression which access the property of a java object,
 * and assume this java object with context object
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 * 
 *          Modified to make validation of parameters optional.
 * 
 * @author Kris Marwood
 * @version 1.1
 */
public class UnaryPropertyExpression extends UnaryOperatorExpression
{

	/**
	 * This is the java bean context property.
	 */
	private static final String	JAVA_BEAN	= "JAVA_BEAN";

	/**
	 * This is the java bean.
	 */
	private Object				bean;

	/**
	 * This is the property name
	 */
	private String				propertyName;

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getValue()
	 */
	public ValueObject getValue() throws ExpressionEngineException
	{
		ValueObject returnValue = null;

		try
		{
			Object propertyValue = BeanUtils.getPropertyValue( bean, propertyName );
			Class propertyType = BeanUtils.getPropertyType( bean.getClass(), propertyName );
			returnValue = new ValueObject( propertyValue, Type.createType( propertyType ) );
		}
		catch( Exception ex )
		{
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
	public void setValue( Object value ) throws ExpressionEngineException
	{
		try
		{
			BeanUtils.setPropertyValue( bean, propertyName, value );
		}
		catch( Exception ex )
		{
			throw new ExpressionEngineException( ex.getMessage() );
		}
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#initialize(org.vedantatree.expressionoasis.ExpressionContext,
	 *      java.lang.Object)
	 */
	@Override
	public void initialize( ExpressionContext expressionContext, Object parameters, boolean validate )
			throws ExpressionEngineException
	{
		bean = expressionContext.getContextProperty( JAVA_BEAN );
		this.propertyName = ( (IdentifierExpression) parameters ).getIdentifierName();
		super.initialize( expressionContext, parameters, validate );
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#getReturnType()
	 */
	@Override
	public Type getReturnType() throws ExpressionEngineException
	{
		Type returnType = null;

		try
		{
			Class propertyType = BeanUtils.getPropertyType( bean.getClass(), propertyName );
			returnType = Type.createType( propertyType );
		}
		catch( IntrospectionException ex )
		{
			throw new ExpressionEngineException( ex.getMessage() );
		}

		return returnType;
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.UnaryOperatorExpression#validate()
	 */
	@Override
	protected void validate( ExpressionContext expressionContext ) throws ExpressionEngineException
	{
		if( bean == null )
		{
			throw new ExpressionEngineException( "Bean is not found. Set context property [JAVA_BEAN]" );
		}

		try
		{
			Class clazz = bean.getClass();
			Class propertyType = BeanUtils.getPropertyType( clazz, propertyName );

			if( propertyType == null )
			{
				throw new ExpressionEngineException( "Property [" + propertyName + "] does not exist in " + clazz );
			}
		}
		catch( IntrospectionException ex )
		{
			throw new ExpressionEngineException( ex.getMessage() );
		}
	}

	/**
	 * @see org.vedantatree.expressionoasis.expressions.Expression#uninitialize(org.vedantatree.expressionoasis.ExpressionContext)
	 */
	@Override
	public void uninitialize( ExpressionContext expressionContext )
	{
		super.uninitialize( expressionContext );
		this.bean = null;
		this.propertyName = null;
	}
}