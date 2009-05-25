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

import topchat.server.gui.ServerGui;
import topchat.server.interfaces.Gui;
import topchat.server.interfaces.Net;
import topchat.server.interfaces.Protocol;
import topchat.server.mediator.Mediator;
import topchat.server.net.ServerNet;
import topchat.server.protocol.xmpp.XMPPProtocol;

/**
 * Main class of the server implementation.
 */

public class TopChatServer {

	private static Logger logger = Logger.getLogger(TopChatServer.class);

	/**
	 * The entry point for the server.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Mediator med = new Mediator();

		@SuppressWarnings("unused")
		Gui gui = new ServerGui(med);

		@SuppressWarnings("unused")
		Net net = new ServerNet(med);

		@SuppressWarnings("unused")
		Protocol prot = new XMPPProtocol(med);		

		logger.info("TopChatServer started.");

		med.socialize();
	}

}
