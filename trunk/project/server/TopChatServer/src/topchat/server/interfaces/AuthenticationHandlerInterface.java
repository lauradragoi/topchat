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
 * Interface implemented by the module handling the authentication.
 */
public interface AuthenticationHandlerInterface
{

	/**
	 * This method is called to check if the credentials of a user are valid
	 * 
	 * @param username
	 *            the name of the user
	 * @param password
	 *            the password of the user
	 * 
	 * @return true if the user credentials are valid, false otherwise
	 */
	public boolean checkUser(String username, String password);
}
