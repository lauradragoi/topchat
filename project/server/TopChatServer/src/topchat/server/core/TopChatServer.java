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
package topchat.server.core;

import org.apache.log4j.Logger;

import topchat.server.authentication.AuthenticationHandler;
import topchat.server.configuration.ConfigurationHandler;
import topchat.server.data.DataHandler;
import topchat.server.gui.ServerGui;
import topchat.server.interfaces.AuthenticationHandlerInterface;
import topchat.server.interfaces.ConfigurationHandlerInterface;
import topchat.server.interfaces.DataHandlerInterface;
import topchat.server.interfaces.Gui;
import topchat.server.interfaces.Net;
import topchat.server.interfaces.Protocol;
import topchat.server.mediator.Mediator;
import topchat.server.net.ServerNet;
import topchat.server.protocol.xmpp.XMPPProtocol;

/**
 * Main class of the server implementation.
 */
public class TopChatServer
{

	private static Logger logger = Logger.getLogger(TopChatServer.class);

	/**
	 * The entry point for the server.
	 * 
	 * Initiates the Mediator, the ConfigurationHandler module, the GUI module,
	 * the Network module, the Protocol, the Authentication module and the
	 * DataHandling module and requests that the Mediator makes these components
	 * work together.
	 * 
	 * @param args
	 *            the arguments received by the program (not used)
	 */
	public static void main(String[] args)
	{
		Mediator med = new Mediator();

		@SuppressWarnings("unused")
		ConfigurationHandlerInterface confHandler = new ConfigurationHandler(
				med);

		@SuppressWarnings("unused")
		Gui gui = new ServerGui(med);

		@SuppressWarnings("unused")
		Net net = new ServerNet(med);

		@SuppressWarnings("unused")
		Protocol prot = new XMPPProtocol(med);

		try
		{
			@SuppressWarnings("unused")
			AuthenticationHandlerInterface authHandler = new AuthenticationHandler(
					med);
		} catch (Exception e)
		{
			logger.fatal("Unable to initialize authentication module " + e);
		}

		try
		{
			@SuppressWarnings("unused")
			DataHandlerInterface dataHandler = new DataHandler(med);
		} catch (Exception e)
		{
			logger.fatal("Unable to initialize data handling module " + e);
		}

		logger.info("TopChatServer started.");

		med.socialize();
	}

}
