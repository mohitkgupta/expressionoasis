/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.config;

import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


/**
 * Builds and manages the single instance of the Expression Oasis Config
 *
 * @author Kris Marwood
 * @version 1.0
 */
public class ConfigFactory {

	private static final String		  FILE_PATH = "config.xml";

	private static ExpressionOasisConfig instance;

	public static ExpressionOasisConfig getConfig() {
		if( instance == null ) {
			synchronized( ConfigFactory.class ) {
				if( instance == null ) {
					Serializer serializer = new Persister();
					try {
						ClassLoader classLoader = ConfigFactory.class.getClassLoader();
						InputStream stream = classLoader.getResourceAsStream( FILE_PATH );
						instance = serializer.read( ExpressionOasisConfig.class, stream );
					}
					catch( Exception e ) {
						throw new RuntimeException( "Error loading ExpressionOasis configuration: " + e.getMessage(), e );
					}
				}
			}

		}
		return instance;
	}
}
