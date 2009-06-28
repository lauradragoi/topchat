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
 * Describes the state of a connection between the server and a client
 * 
 */
public abstract class DefaultContext
{
	/** Connection manager handling this context */
	protected DefaultConnectionManager mgr = null;

	/**
	 * Constructs context controlled by a connection manager
	 * 
	 * @param mgr
	 *            the connection manager controlling the context being created
	 */
	public DefaultContext(DefaultConnectionManager mgr)
	{
		this.mgr = mgr;
	}

	/**
	 * Method to be executed after a reading operation has completed.
	 * 
	 * @param b
	 *            the data that was read
	 */
	public abstract void processRead(byte[] b);

}
