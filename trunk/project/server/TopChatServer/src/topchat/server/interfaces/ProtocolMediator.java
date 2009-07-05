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
 * Interface describing the entity that acts as a mediator for the protocol
 * components
 */
public interface ProtocolMediator
{

	/**
	 * Connect to the protocol
	 * 
	 * @param prot
	 *            the protocol connected to the mediator
	 */
	public void setProtocol(Protocol prot);

	/**
	 * Called when a user is added
	 * 
	 * @param user
	 *            the name of the added user
	 */
	public void addUser(String user);

	/**
	 * Called when a room is added
	 * 
	 * @param room
	 *            the name of the added room
	 */
	public void addRoom(String room);

	/**
	 * Called when a room is removed
	 * 
	 * @param roomName
	 *            the name of the removed room
	 */
	public void removeRoom(String roomName);

	/**
	 * Called when a user is removed
	 * 
	 * @param string
	 *            the name of the removed user
	 */
	public void removeUser(String string);

	/**
	 * 
	 * Checks the credential of a certain user
	 * 
	 * @param username
	 *            the name of the user
	 * @param pass
	 *            the password of the user
	 * @return true if the credentials of the user are valid, false otherwise
	 */
	public boolean checkUser(String username, String pass);

	/**
	 * Called to announce that data was read
	 * 
	 * @param s
	 *            the read data
	 */
	public void announceRead(String s);

	/**
	 * Called to announce that data was sent
	 * 
	 * @param string
	 *            the sent data
	 */
	public void announceSend(String string);

	/**
	 * @param roomName
	 * @param roomUser
	 */
	public void announceRoomJoined(String roomName, String roomUser);

	/**
	 * @param roomUser
	 * @param roomName
	 * @param body
	 * @param numMessages
	 * @param ref
	 */
	public void announceGroupMessage(String roomUser, String roomName,
			String body, int numMessages, int ref);

	/**
	 * @param name
	 * @param roomUser
	 */
	public void announceRoomLeft(String name, String roomUser);
}
