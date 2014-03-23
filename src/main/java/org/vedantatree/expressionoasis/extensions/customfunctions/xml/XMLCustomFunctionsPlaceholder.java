/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
 * 
 * This file is part of ExpressionOasis.
 *
 * ExpressionOasis is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ExpressionOasis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.extensions.customfunctions.xml;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


/**
 * This class exists so that the Simple XML framework has a place to
 * deserialize the custom functions XML file into.
 *
 * @author kmarwood
 */
@Root
public class XMLCustomFunctionsPlaceholder {

	@ElementList(entry = "function", inline = true)
	private List<String> functionSources;

	public List<String> getFunctionSources() {
		return functionSources;
	}
}
