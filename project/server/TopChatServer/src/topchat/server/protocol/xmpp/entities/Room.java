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


public class Room {

	private String name = null;
	private User owner = null;
	
	private static Logger logger = Logger.getLogger(Room.class);
	
	private ConcurrentHashMap<String, RoomParticipant> roomParticipants = new ConcurrentHashMap<String, RoomParticipant>();
	
	public Room(String name, User owner, String roomUser)
	{
		this.owner = owner;
		this.name = name;
		
		roomParticipants.put(owner.toString(), new Owner(owner, roomUser));
	}
	
	@Override
	public String toString()
	{
		return "[ROOM: " + name + "] owner: " + owner ;
	}
	
	public String getName()
	{
		return name;
	}
	
	public synchronized void addParticipant(RoomParticipant participant)
	{
		if (roomParticipants.get(participant.user.toString()) != null)
		{
				logger.warn("Participant " + participant + " already in room " + name);
				return;
		}
		roomParticipants.put(participant.user.toString(), participant);
	}
	
	public synchronized Vector<RoomParticipant> getParticipants()
	{
		Collection<RoomParticipant> collection = roomParticipants.values();
		
		return new Vector<RoomParticipant>(collection);
	}
	
	public synchronized RoomParticipant getParticipant(User user)
	{
		return roomParticipants.get(user.toString());
	}
	
	public synchronized RoomParticipant removeUser(User user)
	{
		RoomParticipant participant = roomParticipants.get(user.toString());
		
		roomParticipants.remove(user.toString());
		
		return participant;
	}
}
