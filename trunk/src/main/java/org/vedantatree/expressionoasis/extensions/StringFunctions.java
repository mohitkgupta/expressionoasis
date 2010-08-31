/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
 * 
 * This file is part of ExpressionOasis.
 *
 * ExpressionOasis is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ExpressionOasis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.extensions;

import org.vedantatree.expressionoasis.ExpressionContext;


/**
 * This class provides various utilities methods for String operations
 * 
 * @author Mohit Gupta
 *
 */
public class StringFunctions {

	public StringFunctions( ExpressionContext expressionContext ) {

	}

	public String trim( String value ) {
		return value == null ? null : value.trim();
	}

	/*
	public boolean isnull( Object value ) {
		return value == null;
	}

	    public boolean isnull( String value ) {
		return value == null;
	}

	    public boolean isnull ( long value ) {
	        return value == 0;
	    }

	    public boolean isnull( Long value ) {
		return value == null;
	}

	public String pad( String value ) {
	            return "-----" + value + "-------";
	}

	public boolean istrue( boolean value ) {
		return value == true;
	}

	    public String iif(Boolean condition, String whenTrue, String whenFalse) {
	        return condition ? whenTrue : whenFalse;
	    }

	    public Long iif(Boolean condition, Long whenTrue, Long whenFalse) {
	        return condition ? whenTrue : whenFalse;
	    }

	    public Long iif(Boolean condition, Long whenTrue, Object whenFalse) {
	        return condition ? whenTrue : (Long)whenFalse;
	    }
	 *
	 */

}
