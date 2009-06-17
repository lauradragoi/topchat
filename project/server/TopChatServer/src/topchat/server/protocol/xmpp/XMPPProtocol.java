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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.interfaces.Net;
import topchat.server.interfaces.Protocol;
import topchat.server.interfaces.ProtocolMediator;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;

/**
 * Implementation of the XMPP protocol
 */
public class XMPPProtocol implements Protocol, XMPPConstants 
{	
	private ProtocolMediator med = null;
	private Net net = null;
	
	/** Pool of threads used to process data received from network */
	private static ExecutorService pool = Executors.newFixedThreadPool(DEFAULT_EXECUTOR_THREADS);
	
	/** Map of SocketChannels and connection managers */
	private static ConcurrentHashMap<SocketChannel, XMPPConnectionManager> connectionManagers = new ConcurrentHashMap<SocketChannel, XMPPConnectionManager>();

	private static Logger logger = Logger.getLogger(XMPPProtocol.class);		

	public XMPPProtocol(ProtocolMediator med) 
	{
		setMediator(med);
		
		this.med.setProtocol(this);

		logger.info("XMPPProtocol initiated");
	}

	@Override
	public void setMediator(ProtocolMediator med) {
		this.med = med;
	}

	@Override
	public int getListeningPort() {
		return XMPP_DEFAULT_SERVER_PORT;
	}

	@Override
	/**
	 * Basically starts the network module
	 */
	public void start(Net net) 
	{
		this.net = net;
		this.net.setProtocol(this);
						
		logger.info("Protocol started");
		
		this.net.start(getListeningPort());
	}
	
	@Override
	/**
	 * Obtain the connection manager for a specified SocketChannel.
	 * If no such manager exists it is created now and added to the map maintained by the protocol.
	 */
	public XMPPConnectionManager getConnectionManager(SocketChannel socketChannel)
	{
		XMPPConnectionManager connManager = connectionManagers.get(socketChannel);
		
		if (connManager == null)
		{
			connManager = new XMPPConnectionManager(this, socketChannel);
			connectionManagers.put(socketChannel, connManager);
		}
		
		return connManager;
	}	

	@Override
	/**
	 * Schedules for the received data to be processed on a thread from the executor pool
	 */
	public void processData(Net net, SocketChannel socketChannel, byte[] data, int count)
	{		
	    byte[] dataCopy = new byte[count];
	    System.arraycopy(data, 0, dataCopy, 0, count);		
		pool.execute(new ProcessData(this, net, socketChannel, dataCopy, count));
	}
	
	
	/**
	 * Schedules the specified data to be sent on that SocketChannel
	 */
	public void sendData(SocketChannel socketChannel, byte[] data)
	{
		net.send(socketChannel, data);
	}
}
