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
package org.vedantatree.expressionoasis.extensions.customfunctions;

import java.util.List;


/**
 * CustomFunctionSourceProviders provide source code for custom methods
 * that will be created at run-time to be used as expression engine functions.
 * 
 * Currently an an XML based-implementation exists. It may be useful to create
 * an implementation that stores the config in a database table.
 *
 * @author Kris Marwood
 * @version 1.0
 */
public interface CustomFunctionSourceProvider {

	/* Retrieves a list of source codes which can be used to methods
	   in a dynamically generated java class */
	List<String> getFunctionSources();
}
