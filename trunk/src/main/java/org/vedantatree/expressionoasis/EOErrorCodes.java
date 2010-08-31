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
package org.vedantatree.expressionoasis;

import org.vedantatree.exceptions.ErrorCodes;


/**
 * It contains various error codes for the application
 * 
 * TODO
 * 		Should use Enum
 * 		Error code description is not set yet
 * 
 * @author mohitgupta
 */
public interface EOErrorCodes extends ErrorCodes {

	/**
	 * Error code representing the invalid operand type for any operator in 
	 * an Expression
	 */
	byte INVALID_OPERAND_TYPE = 100;

}
