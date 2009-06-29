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
package topchat.server.net;

import java.nio.channels.SocketChannel;

import javax.net.ssl.SSLEngine;

/**
 * Defines a change request to a network module (like change socket operation
 * from read to write)
 */
public class ChangeRequest
{
	/** Defines the operation of registering a socket for securing it */
	public static final int REGISTER = 1;
	/**
	 * Defines the operation of changing the type of the operation executing on
	 * a socket (read/write)
	 */
	public static final int CHANGEOPS = 2;

	/** The socket on which the change will occur */
	public SocketChannel socket;
	/** The type of the change request */
	public int type;
	/** The new operation */
	public int ops;
	/** The SSLEngine used for securing the socket */
	public SSLEngine sslEngine;

	/**
	 * Constructs the ChangeRequest to modify the type of the operation
	 * executing on a socket.
	 * 
	 * @param socket
	 *            the socket on which the change is requested
	 * @param type
	 *            the type of the ChangeRequest
	 * @param ops
	 *            the new operation
	 */
	public ChangeRequest(SocketChannel socket, int type, int ops)
	{
		this.socket = socket;
		this.type = type;
		this.ops = ops;
	}

	/**
	 * Constructs the ChangeRequest to register the socket for securing
	 * 
	 * @param socket
	 *            the socket on which the change is requested
	 * @param type
	 *            the type of the ChangeRequest
	 * @param sslEngine
	 *            the SSLEngine used for securing
	 */
	public ChangeRequest(SocketChannel socket, int type, SSLEngine sslEngine)
	{
		this.socket = socket;
		this.type = type;
		this.sslEngine = sslEngine;
	}
}
