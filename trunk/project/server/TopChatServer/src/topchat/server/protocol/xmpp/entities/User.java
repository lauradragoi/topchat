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
package topchat.server.protocol.xmpp.entities;

import java.util.Vector;

import org.apache.log4j.Logger;

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;

/**
 * Describes a client connected to the server and authenticated
 */
public class User
{
	/** The name of the user */
	public String username = null;
	public String something = null;
	/** The server domain */
	public String serverDomain = null;
	/** The password of the user */
	public String pass = null;
	/** The resource used by the user */
	public String resource = null;
	/** The status of the user */
	public String status = null;

	/**
	 * The connection manager handling the connection between the server and the
	 * user
	 */
	public XMPPConnectionManager manager;

	/** The names of the rooms joined by the user */
	public Vector<String> joinedRooms = new Vector<String>();

	private static Logger logger = Logger.getLogger(User.class);

	/**
	 * Constructs a user
	 * 
	 * @param username
	 *            the name of the user
	 * @param something
	 *            something the client sends
	 * @param pass
	 *            the password of the user
	 */
	public User(String username, String something, String pass)
	{
		this.username = username;
		this.something = something;
		this.serverDomain = "example.com";
		this.pass = pass;

	}

	public void setResource(String resource)
	{
		this.resource = resource;
	}

	@Override
	public String toString()
	{
		return username + "@" + serverDomain + "/" + resource;
	}

	public void setManager(XMPPConnectionManager manager)
	{
		this.manager = manager;
	}

	public void setStatus(String status)
	{
		logger.debug("user " + toString() + " has status " + status);
		this.status = status;
	}

	/**
	 * Method called when a User joins a room
	 * 
	 * @param roomName
	 *            the name of the Room
	 */
	public void join(String roomName)
	{
		joinedRooms.add(roomName);
	}
}
