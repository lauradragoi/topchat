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

import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.interfaces.Net;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;

public class ProcessData implements Runnable 
{
	XMPPProtocol prot;
	Net net = null;
	SocketChannel socketChannel = null;
	byte[] data = null;
	int count = 0;
	
	public ProcessData(XMPPProtocol prot, Net net, SocketChannel socketChannel, byte[] data, int count)
	{
		this.prot = prot;
		this.net = net;
		this.socketChannel = socketChannel;
		this.data = data;
		this.count = count;
	}

	@Override
	public void run()
	{
		// Obtain the connection manager for this socket
		XMPPConnectionManager connMgr = prot.getConnectionManager(socketChannel);
		
		// Send the data to the connection manager
		connMgr.processRead(data, count);
	}
}
