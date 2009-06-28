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
package topchat.server.defaults;

/**
 * Manages a connection between the server and a client
 */
public abstract class DefaultConnectionManager
{
	/** The current context of the connection */
	protected DefaultContext context;

	/**
	 * Method called by the protocol to inform the connection manager when data
	 * is received from the network.
	 * 
	 * @param rd
	 *            the received data
	 * @param count
	 *            the number of bytes received
	 */
	public abstract void processRead(byte[] rd, int count);
}
