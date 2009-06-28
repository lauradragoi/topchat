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
import topchat.server.mediator.Mediator;

public class ConfigurationHandler implements ConfigurationHandlerInterface {

	public static final String CONFIG_FILE =  "topchatserver.properties";
	
	@SuppressWarnings("unused")
	private ConfigurationMediator med = null;
	
	private Properties props = null;
	
	private static Logger logger = Logger.getLogger(ConfigurationHandler.class);
	
	/**
	 * @param med
	 */
	public ConfigurationHandler(Mediator med) {
		this.med = med;
		med.setConfigurationHandler(this);
		
		logger.info("Configuration module initiated");
		
		try
		{
			Properties props = load(CONFIG_FILE);
			setProperties(props);
			
			logger.debug("Configuration loaded");
			
		} catch (Exception e) {
			logger.warn("Unable to load configuration from " + CONFIG_FILE + " ( " + e + " )");
		}
	}
	
	private Properties load(String fileName) throws Exception
	{
		Properties myProps = new Properties();
		
		Reader in = null;
		
		try {
			in = new FileReader(fileName);
			myProps.load(in);											
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {}
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

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.ConfigurationHandlerInterface#getProperty(java.lang.String)
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
