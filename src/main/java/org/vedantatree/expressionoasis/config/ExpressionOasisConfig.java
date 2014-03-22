/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
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
package org.vedantatree.expressionoasis.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.vedantatree.expressionoasis.extensions.FunctionProvider;
import org.vedantatree.expressionoasis.grammar.Grammar;


/**
 * The configuration settings for ExpressionOasis. These are loaded from an XML file
 * by the Simple XML framework based on the annotations.
 * 
 * @author Kris Marwood
 * @author 1.0
 */

@Root(name = "expressionOasisConfig")
public class ExpressionOasisConfig
{

	@Element(name = "cacheCompiledExpressions")
	private boolean							cacheCompiledExpressions;

	@Element(name = "grammarClass")
	private String							grammarClass;

	@Element(name = "grammarPath")
	private String							grammarPath;

	@ElementList(name = "functionProviders", entry = "functionProvider")
	private List<FunctionProviderConfig>	functionProviderConfigs;

	@ElementList(name = "expressions", entry = "expression")
	private List<ExpressionConfig>			expressionConfigs;

	private final List<FunctionProvider>	functionProviders	= new ArrayList<FunctionProvider>();

	/**
	 * Determines whether the expression engine should cache RPN token stacks for expression strings
	 * 
	 * @return true if the expression engine should cache RPN token stacks for expression strings
	 */
	public boolean shouldCacheCompiledExpressions()
	{
		return cacheCompiledExpressions;
	}

	/**
	 * Retieves a list of expressions configured for the expression engine.
	 * 
	 * @return a list of expressions configured for the expression engine.
	 */
	public List<ExpressionConfig> getExpressionConfigs()
	{
		return Collections.unmodifiableList( expressionConfigs );
	}

	/**
	 * Retrieves a list of function providers configured for the expression engine
	 * 
	 * @return a list of function providers configured for the expression engine
	 */
	public List<FunctionProvider> getFunctionProviders()
	{
		if( functionProviders.isEmpty() )
		{
			synchronized( functionProviders )
			{
				if( functionProviders.isEmpty() )
				{
					for( FunctionProviderConfig functionProviderConfig : functionProviderConfigs )
					{
						functionProviders.add( functionProviderConfig.getFunctionProvider() );
					}
				}
			}
		}
		return Collections.unmodifiableList( functionProviders );
	}

	public Grammar getGrammar()
	{
		try
		{
			return (Grammar) Class.forName( grammarClass ).newInstance();
		}
		catch( Exception e )
		{
			throw new RuntimeException(
					"Problem while instantiating the Grammar object. Grammar Class specified in config.xml["
							+ grammarClass + "]", e );
		}
	}
	
	public String getGrammarPath ()
	{
		return grammarPath;
	}
}