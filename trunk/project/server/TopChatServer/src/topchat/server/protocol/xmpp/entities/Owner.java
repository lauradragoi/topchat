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

/**
 * An owner is the RoomParticipant who created the room.
 */
public class Owner extends RoomParticipant
{

	/**
	 * Constructs an Owner
	 * 
	 * @param user
	 *            the User creating the room
	 * @param roomUser
	 *            the nick of the user in the created room
	 */
	public Owner(User user, String roomUser)
	{
		this.user = user;
		this.roomUser = roomUser;
		this.affiliation = "owner";
		this.role = "moderator";
	}

}
