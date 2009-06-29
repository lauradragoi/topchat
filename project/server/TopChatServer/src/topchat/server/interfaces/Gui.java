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
 * Interface describing the GUI of the server
 * 
 */
public interface Gui
{

	/** Displays the GUI */
	public void show();

	/**
	 * Connects the GUI to the Mediator
	 * 
	 * @param med
	 *            the mediator to which the Gui connects
	 */
	public void setMediator(GuiMediator med);

	/**
	 * Informs the GUI that a new user logged in.
	 * 
	 * @param user
	 *            the name of the user that logged in
	 */
	public void addUser(String user);

	/**
	 * Informs the GUI that a new room was created
	 * 
	 * @param room
	 *            the name of the room
	 */
	public void addRoom(String room);

	/**
	 * Informs the GUI that a user logged out.
	 * 
	 * @param user
	 *            the name of the user
	 */
	public void removeUser(String user);

	/**
	 * Informs the GUI that a room was destroyed
	 * 
	 * @param room
	 *            the name of the room
	 */
	public void removeRoom(String room);

	/**
	 * Sets the status message of the GUI
	 * 
	 * @param msg
	 *            the status message
	 */
	public void setStatus(String msg);
}
