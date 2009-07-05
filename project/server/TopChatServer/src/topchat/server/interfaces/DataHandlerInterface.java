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
 * Interface implemented by the module in charge of handling the received and
 * sent data information
 * 
 */
public interface DataHandlerInterface
{

	/**
	 * Method called when new data is received
	 * 
	 * @param s
	 *            the received data
	 */
	void handleReceived(String s);

	/**
	 * Method called when new data is sent
	 * 
	 * @param s
	 *            the sent data
	 */
	void handleSent(String s);

	/**
	 * Method called when a room is created
	 * 
	 * @param room
	 *            the room that is created
	 */
	void handleRoomCreated(String room);

	/**
	 * Method called when a room is destroyed
	 * 
	 * @param room
	 *            the room being destroyed
	 */
	void handleRoomDestroyed(String room);

	/**
	 * Method called when a room is joined by a user
	 * 
	 * @param roomName
	 *            the name of the room
	 * @param roomUser
	 *            the nickname of the user in the room
	 */
	void handleRoomJoined(String roomName, String roomUser);

	/**
	 * Method called when a message is sent to a room.
	 * 
	 * @param roomUser
	 *            the nickname of the user sending the message
	 * @param roomName
	 *            the name of the room
	 * @param body
	 *            the body of the message
	 * @param numMessages
	 *            the numerical id of the message in the group conversation
	 * @param ref
	 *            the numerical id of the message referred by this message or 0
	 *            if no such reference
	 */
	void handleGroupMessage(String roomUser, String roomName, String body,
			int numMessages, int ref);

	/**
	 * Method called when a user leaves a room
	 * 
	 * @param name
	 *            the name of the room
	 * @param roomUser
	 *            the nickname of the user in the room
	 */
	void handleRoomLeft(String name, String roomUser);

}
