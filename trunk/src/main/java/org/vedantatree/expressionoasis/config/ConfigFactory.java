/**
 * Copyright (c) 2010 VedantaTree all rights reserved.
 * 
 *  This file is part of ExpressionOasis.
 *
 *  ExpressionOasis is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ExpressionOasis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with ExpressionOasis.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vedantatree.expressionoasis.config;

import java.io.FileInputStream;
import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


/**
 * Builds and manages the single instance of the Expression Oasis Config
 * 
 * @author Kris Marwood
 * @version 1.0
 * 
 * 
 *          Added feature to load the configuration file path from System Properties. If exist, it will use it or
 *          otherwise will use the default path i.e. 'config.xml'
 * 
 * @author Mohit Gupta
 * @version 1.1
 * @since 3.1
 */
public class ConfigFactory
{

	public static final String				CONFIG_FILE_PATH_KEY	= "EXPRESSION_OASIS_CONFIG_FILE";
	private static String					CONFIG_FILE_PATH		= "config.xml";

	private static ExpressionOasisConfig	instance;

	public static ExpressionOasisConfig getConfig()
	{
		if( instance == null )
		{

			synchronized( ConfigFactory.class )
			{
				if( instance == null )
				{
					String configFilePath = System.getProperty( CONFIG_FILE_PATH_KEY );
					boolean externalPathSet = false;
					if( configFilePath != null && configFilePath.trim().length() > 0 )
					{
						CONFIG_FILE_PATH = configFilePath;
						externalPathSet = true;
					}
					System.out.println( "Expression Oasis Configurations Path[" + CONFIG_FILE_PATH
							+ "] externally-set[" + externalPathSet + "]" );

					Serializer serializer = new Persister();
					try
					{
						InputStream stream = externalPathSet ? new FileInputStream( CONFIG_FILE_PATH )
								: ConfigFactory.class.getClassLoader().getResourceAsStream( CONFIG_FILE_PATH );
						instance = serializer.read( ExpressionOasisConfig.class, stream );
					}
					catch( Exception e )
					{
						throw new RuntimeException( "Error loading ExpressionOasis configuration: " + e.getMessage(), e );
					}
				}
			}

		}
		return instance;
	}
}
