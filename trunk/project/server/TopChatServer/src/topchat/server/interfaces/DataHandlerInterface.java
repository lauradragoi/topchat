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
	 * Method called in order to determine the handling of the received data
	 * 
	 * @param s
	 *            the received data
	 */
	void handleReceived(String s);

	/**
	 * Method called in order to determine the handling of the sent data
	 * 
	 * @param s
	 *            the sent data
	 */
	void handleSent(String s);

}
