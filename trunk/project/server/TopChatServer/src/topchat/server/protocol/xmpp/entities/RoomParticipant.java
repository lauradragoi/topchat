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

public class RoomParticipant {

	protected User user = null;
	protected String affiliation = null;
	protected String role = null;
	protected String roomUser = null;
	
	
	public RoomParticipant()
	{
		
	}
	
	public RoomParticipant(User user, String roomUser)
	{
		this.user = user;
		this.roomUser = roomUser;
		affiliation = "none";
		role = "participant";		
	}
	
	@Override
	public String toString()
	{
		return "[PARTICIPANT] " + user.toString() + " ROOM_USER " + roomUser + 
				" affiliation " + affiliation + " role " + role;
	}
	
	public String getRoomUser()
	{
		return roomUser;
	}
	
	public String getRole()
	{
		return role;
	}
	
	public String getAffiliation()
	{
		return affiliation;
	}
	
}
