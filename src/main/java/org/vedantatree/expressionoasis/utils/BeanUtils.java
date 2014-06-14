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
package org.vedantatree.expressionoasis.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;


/**
 * This class provides the util methods for bean management.
 * One can handle bean using these util method like method invocation by name
 * property accessing by the name and many more.
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 */
public final class BeanUtils
{

	/**
	 * Constructs the BeanUtils
	 * This is made private to restrict the use to singlton object
	 * of this class.
	 */
	private BeanUtils()
	{
		/**
		 * Nothing to do here.
		 */
	}

	/**
	 * Checks whether the property is editable or not. Generally it checks
	 * whether the mutator is available for this property or not. A property is
	 * editable iff the property mutator is available.
	 * 
	 * @param beanClass the class of bean whose property is being inspecting.
	 * @param property the name of property to inspect.
	 * @return Returns <code>true</code> if the property is editable else <code>false</code>
	 * @throws IntrospectionException
	 */
	public static final boolean isPropertyEditable( Class beanClass, String property ) throws IntrospectionException
	{
		// Gets the property descriptor for given property
		PropertyDescriptor descriptor = getPropertyDescriptor( beanClass, property );
		return descriptor == null ? false : descriptor.getWriteMethod() != null;
	}

	/**
	 * Gets the return type of property.
	 * 
	 * @param beanClass the class of bean whose property is being inspecting.
	 * @param property the name of property
	 * @return the type of property. Returns <code>null</code> if the property name
	 *         if not found in bean class.
	 * @throws IntrospectionException
	 */
	public static final Class getPropertyType( Class beanClass, String property ) throws IntrospectionException
	{
		// Gets the property descriptor for property name.
		PropertyDescriptor descriptor = getPropertyDescriptor( beanClass, property );
		return descriptor == null ? null : descriptor.getPropertyType();
	}

	/**
	 * Gets the value of property from the bean object.
	 * 
	 * @param bean the object of bean class whose property is to access.
	 * @param property the name of property.
	 * @return the value of property.
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static final Object getPropertyValue( Object bean, String property ) throws IntrospectionException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Object propertyValue = null;
		PropertyDescriptor descriptor = getPropertyDescriptor( bean.getClass(), property );
		if( descriptor != null && descriptor.getReadMethod() != null )
		{
			propertyValue = descriptor.getReadMethod().invoke( bean, null );
		}
		return propertyValue;
	}

	/**
	 * Sets the value of property to the object of bean
	 * 
	 * @param bean the bean object whose proeprty is to set.
	 * @param property the name of property.
	 * @param value the value to set for the property.
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static final void setPropertyValue( Object bean, String property, Object value )
			throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		PropertyDescriptor descriptor = getPropertyDescriptor( bean.getClass(), property );
		if( descriptor != null && descriptor.getWriteMethod() != null )
		{
			descriptor.getWriteMethod().invoke( bean, new Object[]
			{ value } );
		}
	}

	/**
	 * Gets the property descriptor for the property name from given class.
	 * 
	 * @param beanClass the class of bean
	 * @param property the name of property.
	 * @return the property descriptor if property name found else return <code>null</code>.
	 * @throws IntrospectionException
	 */
	public static final PropertyDescriptor getPropertyDescriptor( Class beanClass, String property )
			throws IntrospectionException
	{
		BeanInfo beanInfo = Introspector.getBeanInfo( beanClass );
		PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
		int length = descriptors == null ? 0 : descriptors.length;
		for( int i = 0; i < length; i++ )
		{
			if( descriptors[i].getDisplayName().equals( property ) )
			{
				return descriptors[i];
			}
		}
		return null;
	}
}