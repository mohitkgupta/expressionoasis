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
package org.vedantatree.expressionoasis.extensions.customfunctions;

import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;

import org.vedantatree.expressionoasis.exceptions.ExpressionEngineException;


/**
 * Builds a java class dynamically at run-time from method source code
 * configured outside of the code base. This enables new functions to be
 * added to the expression engine by configuration without recompiling.
 *
 * Currently only supports the creating of one custom functions class
 * as the class name is hard coded.
 *
 * @author Kris Marwood
 * @version 1.0
 */
public class CustomFunctionsClassFactory {

	private static final String					 CLASS_NAME		   = "DynamicMethods";

	private static volatile Class<? extends Object> customFunctionsClass = null;

	public static Class<? extends Object> getCustomFunctionsClass( CustomFunctionSourceProvider sourceProvider ) {
		if( customFunctionsClass == null ) {
			synchronized( CustomFunctionsClassFactory.class ) {
				if( customFunctionsClass == null ) {
					customFunctionsClass = makeCustomFunctionsClass( sourceProvider );
				}
			}
		}
		return customFunctionsClass;
	}

	/**
	 * Makes a class at runtime whose methods are defined by thes source code provided by the sourceProvider.
	*
	 * @param sourceProvider provides the source code for the methods in the dynamically generated class
	 * @return a class whose methods are generated from the source code provided by the sourceProvider
	 * @throws ExpressionEngineException
	 */

	private static Class<? extends Object> makeCustomFunctionsClass( CustomFunctionSourceProvider sourceProvider ) {
		try {
			ClassPool pool = ClassPool.getDefault();

			// define the class
			CtClass classDef = pool.makeClass( CLASS_NAME );

			// add the constructor
			String constructor = "public " + CLASS_NAME
					+ "(org.vedantatree.expressionoasis.ExpressionContext expressionContext) {}";
			CtConstructor constructorDef = CtNewConstructor.make( constructor, classDef );
			classDef.addConstructor( constructorDef );

			// add all the methods defined by the sourceProvider
			List<String> methodSources = sourceProvider.getFunctionSources();
			for( String methodSource : methodSources ) {
				CtMethod methodDef = CtNewMethod.make( methodSource, classDef );
				classDef.addMethod( methodDef );
			}

			// generate the bytecode
			customFunctionsClass = (Class<? extends Object>) classDef.toClass();
		}
		catch( CannotCompileException e ) {
			throw new RuntimeException( "Error creating custom functions class", e );
		}
		return customFunctionsClass;
	}
}
