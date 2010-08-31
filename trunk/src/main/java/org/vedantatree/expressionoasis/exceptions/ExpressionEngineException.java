/**
 * Copyright (c) 2005 VedantaTree all rights reserved.
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
package org.vedantatree.expressionoasis.exceptions;

import org.vedantatree.exceptions.XException;


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

	/**
	 * Constructs the ExpressionEngineException
	 * 
	 * @param msg
	 *            the massege given by parser to tell the error
	 * @param errorCode code representing the error type
	 * @param e Exception to wrap
	 */
	public ExpressionEngineException( String msg, int errorCode, Exception e ) {
		super( msg, errorCode, e );
	}
}