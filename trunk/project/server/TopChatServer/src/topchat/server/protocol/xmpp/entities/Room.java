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

import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * Describes a room that was created.
 */
public class Room
{

	/** The name of the room */
	private String name = null;
	/** The user that created the room */
	private User owner = null;

	private static Logger logger = Logger.getLogger(Room.class);

	/** Maps the user names to their RoomParticipant info */
	private ConcurrentHashMap<String, RoomParticipant> roomParticipants = new ConcurrentHashMap<String, RoomParticipant>();

	/**
	 * Constructs a Room
	 * 
	 * @param name
	 *            the name of the user
	 * @param owner
	 *            the User that created the room
	 * @param roomUser
	 *            the nickname of the owner in the room
	 */
	public Room(String name, User owner, String roomUser)
	{
		this.owner = owner;
		this.name = name;

		roomParticipants.put(owner.toString(), new Owner(owner, roomUser));
	}

	@Override
	public String toString()
	{
		return "[ROOM: " + name + "] owner: " + owner;
	}

	public String getName()
	{
		return name;
	}

	/**
	 * Adds a participant to a room
	 * 
	 * @param participant
	 *            the RoomParticipant to be added
	 */
	public synchronized void addParticipant(RoomParticipant participant)
	{
		if (roomParticipants.get(participant.user.toString()) != null)
		{
			logger.warn("Participant " + participant + " already in room "
					+ name);
			return;
		}
		roomParticipants.put(participant.user.toString(), participant);
	}

	/**
	 * Retrieve all the RoomParticipants in one room
	 * 
	 * @return a Vector of RoomParticipant
	 */
	public synchronized Vector<RoomParticipant> getParticipants()
	{
		Collection<RoomParticipant> collection = roomParticipants.values();

		return new Vector<RoomParticipant>(collection);
	}

	/**
	 * Retrieve the RoomParticipant information for a User
	 * 
	 * @param user
	 *            the User requested
	 * @return the RoomParticipant info
	 */
	public synchronized RoomParticipant getParticipant(User user)
	{
		return roomParticipants.get(user.toString());
	}

	/**
	 * Remove a user from a room
	 * 
	 * @param user
	 *            the User to be removed
	 * @return the RoomParticipant information of the removed user
	 */
	public synchronized RoomParticipant removeUser(User user)
	{
		RoomParticipant participant = roomParticipants.get(user.toString());

		roomParticipants.remove(user.toString());

		return participant;
	}
}
