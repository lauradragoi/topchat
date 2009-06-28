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
package topchat.server.interfaces;

/**
 * Interface describing the entity that acts as a
 * mediator for the protocol components
 */
public interface ProtocolMediator {
	
	/** Connect to the protocol */
	public void setProtocol(Protocol prot);

	/** Called when a user is added */
	public void addUser(String user);

	/** Called when a room is added */
	public void addRoom(String room);

	/**
	 * @param roomName
	 */
	public void removeRoom(String roomName);

	/**
	 * @param string
	 */
	public void removeUser(String string);

	/**
	 * @param username
	 * @param pass
	 * @return
	 */
	public boolean checkUser(String username, String pass);

	/**
	 * @param s
	 */
	public void announceRead(String s);

	/**
	 * @param string
	 */
	public void announceSend(String string);
}
