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

import topchat.server.interfaces.DataManagerInterface;
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
public class Mediator implements GuiMediator, NetMediator, ProtocolMediator, DataMediator {

	private Gui gui = null;
	private Net net = null;
	private Protocol prot = null;
	private DataManagerInterface data = null;

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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui.show();
			}
		});
	}

	/**
	 * Relates to the GUI component
	 */
	public void setGui(Gui gui) {
		this.gui = gui;
	}

	/**
	 * Relates to the network component
	 */
	public void setNet(Net net) {
		this.net = net;
	}

	/**
	 * Relates to the protocol component
	 * 
	 * @param prot
	 */
	public void setProtocol(Protocol prot) {
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
	public void addRoom(final String room) {
		gui.addRoom(room);
		gui.setStatus("Room " + room + " created.");
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
	public void removeRoom(final String room) {
		gui.removeRoom(room);
		gui.setStatus("Room " + room + " destroyed.");
		logger.info("Room " + room + " destroyed.");
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.DataMediator#setDataManager(topchat.server.interfaces.DataManagerInterface)
	 */
	@Override
	public void setDataManager(DataManagerInterface dataManager) {
		data = dataManager;		
	}
}
