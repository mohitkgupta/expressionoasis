/**
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
package org.ganges.expressionengine.exceptions;

import org.ganges.exceptions.XException;


/**
 * This exception is used by whole Expression Engine Component to share any 
 * Erroneous information with user of the component, like at the time of parsing
 * or by the compiler while doing syntactical analysis. 
 * 
 * @author Mohit Gupta
 * @author Parmod Kamboj
 * @version 1.0
 *  
 */
public class ExpressionEngineException extends XException {

	/**
	 * This is the serialization version for this class.
	 */
	private static final long serialVersionUID = 2006122401L;

	/**
	 * Constructs the ExpressionEngineException
	 * 
	 * @param msg
	 *            the massege given by parser to tell the error.
	 */
	public ExpressionEngineException( String msg ) {
		super( msg, -1 );
	}

	/**
	 * Constructs the ExpressionEngineException
	 * 
	 * @param msg
	 *            the massege given by parser to tell the error.
	 * @param e Exception to wrap
	 */
	public ExpressionEngineException( String msg, Exception e ) {
		super( msg, -1, e );
	}
}