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
package topchat.server.protocol.xmpp;

import java.nio.channels.SocketChannel;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;

/**
 * Runnable to execute data processing request
 */
public class ProcessData implements Runnable
{
	/** The protocol */
	XMPPProtocol prot = null;
	/** The socketChannel on which the data is received */
	SocketChannel socketChannel = null;
	/** The data */
	byte[] data = null;
	/** The size of the data array */
	int count = 0;

	/**
	 * Constructs a process data entity
	 * 
	 * @param prot
	 *            the protocol
	 * @param socketChannel
	 *            the socket
	 * @param data
	 *            the data
	 * @param count
	 *            the number of bytes
	 */
	public ProcessData(XMPPProtocol prot, SocketChannel socketChannel,
			byte[] data, int count)
	{
		this.prot = prot;
		this.socketChannel = socketChannel;
		this.data = data;
		this.count = count;
	}

	@Override
	public void run()
	{
		// Obtain the connection manager for this socket
		XMPPConnectionManager connMgr = prot
				.getConnectionManager(socketChannel);

		// Send the data to the connection manager to handle it
		connMgr.processRead(data, count);
	}
}
