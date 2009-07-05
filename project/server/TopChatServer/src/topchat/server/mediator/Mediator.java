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
package topchat.server.mediator;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import topchat.server.interfaces.AuthenticationHandlerInterface;
import topchat.server.interfaces.AuthenticationMediator;
import topchat.server.interfaces.ConfigurationHandlerInterface;
import topchat.server.interfaces.ConfigurationMediator;
import topchat.server.interfaces.DataHandlerInterface;
import topchat.server.interfaces.DataMediator;
import topchat.server.interfaces.Gui;
import topchat.server.interfaces.GuiMediator;
import topchat.server.interfaces.Net;
import topchat.server.interfaces.NetMediator;
import topchat.server.interfaces.Protocol;
import topchat.server.interfaces.ProtocolMediator;

/**
 * Mediates the interaction between the server components
 * 
 */
public class Mediator implements GuiMediator, NetMediator, ProtocolMediator,
		DataMediator, AuthenticationMediator, ConfigurationMediator
{
	/** The Gui module */
	private Gui gui = null;
	/** The network module */
	private Net net = null;
	/** The protocol module */
	private Protocol prot = null;

	/** The data handling module */
	private DataHandlerInterface data = null;
	/** The configuration module */
	private ConfigurationHandlerInterface conf = null;
	/** The authentication module */
	private AuthenticationHandlerInterface auth = null;

	private static Logger logger = Logger.getLogger(Mediator.class);

	/**
	 * Starts the interaction with the other components of the server
	 * application
	 */
	public void socialize()
	{
		/** Start protocol */
		prot.start(net);

		/** Start the GUI */
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				gui.show();
			}
		});
	}

	/**
	 * Relates to the GUI component
	 * 
	 * @param gui
	 *            the gui module
	 */
	public void setGui(Gui gui)
	{
		this.gui = gui;
	}

	/**
	 * Relates to the network component
	 * 
	 * @param net
	 *            the network module
	 */
	public void setNet(Net net)
	{
		this.net = net;
	}

	/**
	 * Relates to the protocol component
	 * 
	 * @param prot
	 *            the protocol module
	 */
	public void setProtocol(Protocol prot)
	{
		this.prot = prot;
	}

	@Override
	public void addUser(final String user)
	{
		gui.addUser(user);
		gui.setStatus("User " + user + " logged in.");
		logger.info("User " + user + " logged in.");
	}

	@Override
	public void addRoom(final String room)
	{
		gui.addRoom(room);
		gui.setStatus("Room " + room + " created.");
		
		data.handleRoomCreated(room);
		
		logger.info("Room " + room + " created.");
	}

	@Override
	public void removeUser(final String user)
	{
		gui.removeUser(user);
		gui.setStatus("User " + user + " logged out.");
		logger.info("User " + user + " logged out.");
	}

	@Override
	public void removeRoom(final String room)
	{
		gui.removeRoom(room);
		gui.setStatus("Room " + room + " destroyed.");
		
		data.handleRoomDestroyed(room);
		
		logger.info("Room " + room + " destroyed.");
	}

	@Override
	public void setDataHandler(DataHandlerInterface dataManager)
	{
		data = dataManager;
	}

	@Override
	public void setAuthenticationHandler(
			AuthenticationHandlerInterface authHandler)
	{
		this.auth = authHandler;
	}

	@Override
	public void setConfigurationHandler(
			ConfigurationHandlerInterface confHandler)
	{
		this.conf = confHandler;
	}

	@Override
	public String getProperty(String property) throws Exception
	{
		String value = conf.getProperty(property);

		if (value == null)
			throw new Exception("Value must be set for " + property);

		return value;
	}

	@Override
	public boolean checkUser(String username, String pass)
	{

		return auth.checkUser(username, pass);
	}

	@Override
	public void announceRead(String s)
	{
		logger.debug("Announce read " + s);

		data.handleReceived(s);
	}

	@Override
	public void announceSend(String s)
	{
		logger.debug("Announce send " + s);

		data.handleSent(s);
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.ProtocolMediator#announceRoomJoined(java.lang.String, java.lang.String)
	 */
	@Override
	public void announceRoomJoined(String roomName, String roomUser)
	{
		data.handleRoomJoined(roomName, roomUser);
		
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.ProtocolMediator#announceGroupMessage(java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public void announceGroupMessage(String roomUser, String roomName,
			String body, int numMessages, int ref)
	{
		data.handleGroupMessage(roomUser, roomName, body, numMessages, ref);
		
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.ProtocolMediator#announceRoomLeft(java.lang.String, java.lang.String)
	 */
	@Override
	public void announceRoomLeft(String name, String roomUser)
	{
		data.handleRoomLeft(name, roomUser);
	}
}
