/**
    TopChatServer 
    Copyright (C) 2009 Laura Dragoi

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package topchat.server.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.log4j.Logger;

import topchat.server.interfaces.ConfigurationHandlerInterface;
import topchat.server.interfaces.ConfigurationMediator;

/**
 * The ConfigurationHandler is in charge of obtaining the properties used for
 * configuring the server and supplying them to the mediator. The Properties are
 * kept in a configuration file of the form property = value.
 */
public class ConfigurationHandler implements ConfigurationHandlerInterface
{

	/** The configuration file name */
	public static final String CONFIG_FILE = "topchatserver.properties";

	/**
	 * The ConfigurationMediator to which the ConfigurationHandler is connected
	 */
	@SuppressWarnings("unused")
	private ConfigurationMediator med = null;

	/** The configured properties */
	private Properties props = null;

	private static Logger logger = Logger.getLogger(ConfigurationHandler.class);

	/**
	 * Constructs the ConfigurationHandler. Connects to the
	 * ConfigurationMediator and loads the Properties from the configuration
	 * file.
	 * 
	 * @param med
	 *            the ConfigurationMediator to which the ConfigurationHandler
	 *            connects.
	 */
	public ConfigurationHandler(ConfigurationMediator med)
	{
		this.med = med;
		med.setConfigurationHandler(this);

		try
		{
			Properties props = load(CONFIG_FILE);
			setProperties(props);

			logger.info("Configuration properties loaded");

		} catch (Exception e)
		{
			logger.warn("Unable to load configuration from " + CONFIG_FILE
					+ " ( " + e + " )");
		}

		logger.info("Configuration module initiated");
	}

	/**
	 * Load the properties from a properties file.
	 * 
	 * @param fileName
	 *            the name of the file where the properties are kept
	 * @return the Properties contained in the properties file
	 * @throws Exception
	 *             if the properties cannot be loaded from the file
	 */
	private Properties load(String fileName) throws Exception
	{
		Properties myProps = new Properties();

		Reader in = null;

		try
		{
			in = new FileReader(fileName);
			myProps.load(in);
		} finally
		{
			if (in != null)
				try
				{
					in.close();
				} catch (IOException e)
				{
				}
		}

		return myProps;
	}

	private void setProperties(Properties props)
	{
		this.props = props;

		logger.debug("Properties are " + props.toString());
	}

	private boolean isPropsSet()
	{
		return (props != null);
	}

	/**
	 * Obtain the value of a certain property
	 * 
	 * @param property
	 *            the name of the property
	 * 
	 * @return the value of the property or null if it is not set
	 */
	@Override
	public String getProperty(String property)
	{
		if (isPropsSet())
		{
			return props.get(property).toString();
		}
		return null;
	}
}
