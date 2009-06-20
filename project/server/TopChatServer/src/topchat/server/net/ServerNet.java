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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLEngine;

import org.apache.log4j.Logger;

import topchat.server.interfaces.Net;
import topchat.server.interfaces.NetMediator;
import topchat.server.interfaces.Protocol;

/**
 * The network module of the server
 * 
 * @author ldragoi
 * 
 */
public class ServerNet implements Net, NetConstants, Runnable {

	private NetMediator med = null;
	private Protocol prot = null;

	private ServerSocketChannel serverSocketChannel = null;
	
	private Selector selector = null;
	
	@SuppressWarnings("unused")
	private int port;
	
	// The buffer into which we'll read data when it's available
	private ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	
	// A list of ChangeRequest instances
	private List<ChangeRequest> changeRequests = new LinkedList<ChangeRequest>();

	// A map of data to be sent on the sockets 
	// Maps a SocketChannel to a list of ByteBuffer instances
	private ConcurrentHashMap<SocketChannel, List<ByteBuffer> > pendingData = new ConcurrentHashMap<SocketChannel, List<ByteBuffer> >();

	// A map of TLSHandlers used to secure the socket channels
	private ConcurrentHashMap<SocketChannel, TLSHandler> securingData = new ConcurrentHashMap<SocketChannel, TLSHandler>();
	
	private static Logger logger = Logger.getLogger(ServerNet.class);

	/**
	 * Constructor for the network module
	 * 
	 * @param med 
	 */
	public ServerNet(NetMediator med) 
	{
		setMediator(med);
		this.med.setNet(this);

		logger.info("NET initiated");
	}

	/**
	 * Starts listening for connections and the main loop of the network module
	 * @param port the port on which the server will listen
	 */
	@Override
	public void start(int port) 
	{
		this.port = port;
		
		selector = initSelector(port);

		// start the main loop in a new thread
		new Thread(this).start();
	}
	
	/**
	 * Sets the mediator
	 */
	@Override
	public void setMediator(NetMediator med) {
		this.med = med;
	}

	/**
	 * Sets the protocol
	 */	
	@Override	
	public void setProtocol(Protocol prot) {
		this.prot = prot;
	}	
	
	/**
	 * Init the selector to start waiting for connections
	 * @param port the port on which the server will be listening on
	 */
	private Selector initSelector(int port) 
	{
		Selector selector = null;
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("Exception on Selector.open");
			cleanup();
			return null;
		}

		try {
			serverSocketChannel = ServerSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("Exception on opening server socket channel.");
			cleanup();
			return null;
		}

		try {
			serverSocketChannel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
			logger
					.fatal("Exception in configuring blocking on server socket channel.");
			cleanup();
			return null;
		}

		try {
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("Exception when binding on port " + port);
			cleanup();
			return null;
		}

		try {
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
			logger.fatal("Exception when registering selector");
			cleanup();
			return null;
		}

		logger.info("Awaiting connection on port " + port);
		
		return selector;
	}

	@Override
	/**
	 * The main loop of the network module
	 */
	public void run() 
	{
		logger.info("Started network module loop");
		
		while (true) 
		{	
	        // Process any pending changes
	        synchronized(this.changeRequests) 
	        {
	        	Iterator<ChangeRequest> changes = this.changeRequests.iterator();
	        	while (changes.hasNext()) 
	        	{
	        		ChangeRequest change = changes.next();
	        		switch(change.type) 
	        		{
	        			case ChangeRequest.CHANGEOPS:
	        			{
	        					SelectionKey key = change.socket.keyFor(this.selector);
	        					key.interestOps(change.ops);
	        			}
	        					break;
	        			case ChangeRequest.REGISTER:
	        			{
	        					SelectionKey key = change.socket.keyFor(this.selector);
	        					// check if socket is registered with selector
	        					if (key != null)
	        					{
	        						// check if socket is already secured
        							if (securingData.get(change.socket) == null)
        							{
        								// socket was not previously secured - secure it now
        								TLSHandler tlsHandler = new TLSHandler(this, change.socket, change.sslEngine);
        								prot.execute(tlsHandler);
        								securingData.put(change.socket, tlsHandler);        								
        							}
        							else
        							{
        								logger.debug("Attempt to re-secure socket " + change.socket + " ignored.");
        							}        							        						
	        					}
	        			}
	        					break;
	        		}
	        	}
	        	this.changeRequests.clear(); 
	        }
			
			
			try {
				selector.select();
			} catch (IOException e) {
				logger.fatal("Exception on select" + e);
				cleanup();
				return;
			}
			
			
			for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext();) 
			{
					SelectionKey key = it.next();
					it.remove();
					
			        if (!key.isValid()) 
			        {
			              continue;
			        }
			        
			        if (key.isAcceptable()) {

						try {
							accept(key);
						} catch (IOException e) 
						{							
							logger.fatal("Exception on accept. " + e);
							cleanup();
							return;
						}

					} else if (key.isReadable()) {

						try {
							read(key);
						} catch (IOException e) 
						{							
							logger.fatal("Exception on read. " + e);
							cleanup();
							return;
						}

					} else if (key.isWritable()) {

						try {
							write(key);
						} catch (IOException e) 
						{						
							logger.fatal("Exception on write " + e);
							cleanup();
							return;
						}

					}
			}
		}
	}	
	
	/**
	 * Initiate a write request of some data on a certain socket
	 * @param socket
	 * @param data
	 */
	public void send(SocketChannel socket, byte[] data) 
	{
	    // check if socket channel is secure
	    TLSHandler tlsHandler = securingData.get(socket);
	    if ( tlsHandler != null )
	    {
	    	byte[] secureData = tlsHandler.getSecureData(data);
	    	sendRaw(socket, secureData);
	    }	
	    else
	    {
	    	sendRaw(socket, data);
	    }
	}
	
	/**
	 * Initiate a write request of some data on a certain socket
	 * @param socket
	 * @param data
	 */
	public void sendRaw(SocketChannel socket, byte[] data) 
	{
	    synchronized (this.changeRequests) {
	    	// Indicate we want the interest ops set changed
	    	this.changeRequests.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));
	      
	    	// And queue the data we want written
	    	synchronized (this.pendingData) 
	    	{
	    		List<ByteBuffer> queue = this.pendingData.get(socket);
	    		if (queue == null) {
	    			queue = new ArrayList<ByteBuffer>();
	    			this.pendingData.put(socket, queue);
	    		}
	    		queue.add(ByteBuffer.wrap(data));
	    	}
	    }
	    
	    // Finally, wake up our selecting thread so it can make the required changes
	    this.selector.wakeup();
	}
	
	/**
	 * Initiate a request to secure the stream using TLS on one socket
	 * @param socket
	 * @param data
	 */
	public void secure(SocketChannel socket, SSLEngine sslEngine) 
	{
	    synchronized (this.changeRequests) {
	    	// Indicate we want the interest ops set changed
	    	this.changeRequests.add(new ChangeRequest(socket, ChangeRequest.REGISTER, sslEngine));	      
	    }
	    
	    // Finally, wake up our selecting thread so it can make the required changes
	    this.selector.wakeup();
	}	

	
	/**
	 * Accept a new connection and put in read mode
	 * @param key the key associated with the connection to be accepted
	 * @throws IOException
	 */
	private void accept(SelectionKey key) throws IOException 
	{
		
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();
		
	    // Accept the connection and make it non-blocking
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);				
		
	    // Register the new SocketChannel with our Selector, indicating
	    // we'd like to be notified when there's data waiting to be read
	    socketChannel.register(this.selector, SelectionKey.OP_READ);
						
		logger.info("Accepted connection from "
				+ socketChannel.socket().getRemoteSocketAddress());
	}
	
	
	private void read(SelectionKey key) throws IOException 
	{
		    SocketChannel socketChannel = (SocketChannel) key.channel();
		    
		    // check if socket channel is secure
		    TLSHandler tlsHandler = securingData.get(socketChannel);
		    if ( tlsHandler != null )
		    {
		    		logger.debug("Read on secured socket " + socketChannel);
		    		System.out.println("Handshake status " + tlsHandler.sslEngine.getHandshakeStatus());
		    		
		    	    int netBBSize = tlsHandler.sslEngine.getSession().getPacketBufferSize();
		    	    
		    	    // resize read buffer to fit data
		    	    readBuffer = ByteBuffer.allocate(netBBSize);
		    }	
			

		    // Clear out our read buffer so it's ready for new data
		    this.readBuffer.clear();
		    
		    // Attempt to read off the channel
		    int numRead;
		    try {
		      numRead = socketChannel.read(this.readBuffer);
		    } catch (IOException e) {
		      logger.debug("Client forced quit. " + e);
		      // The remote forcibly closed the connection, cancel
		      // the selection key and close the channel.
		      key.cancel();
		      socketChannel.close();
		      return;
		    }

		    if (numRead == -1) {
		      logger.debug("Client quit cleanly");
		      // Remote entity shut the socket down cleanly. Do the
		      // same from our end and cancel the channel.
		      key.channel().close();
		      key.cancel();
		      return;
		    }
		    
		    if ( tlsHandler != null )
		    {		    	
		    	byte[] data = tlsHandler.processData(readBuffer);
		    
		    	if (data != null)
		    	{
		    		 // Hand the data off to the protocol
				    // The protocol itself only hands it out to an executor thread and returns
		    		prot.processData(this, socketChannel, data, data.length);
		    	}
		    }
		    else
		    {
	
			    // Hand the data off to the protocol
			    // The protocol itself only hands it out to an executor thread and returns
			    prot.processData(this, socketChannel, this.readBuffer.array(), numRead);
		    }
	}
	
	private void write(SelectionKey key) throws IOException 
	{
		    SocketChannel socketChannel = (SocketChannel) key.channel();

		    // check if socket channel is secure
	    	if (securingData.get(socketChannel) != null)
		    	logger.debug("Write on secured socket " + socketChannel);		    
		    
		    synchronized (this.pendingData) 
		    {
		    	List<ByteBuffer> queue = this.pendingData.get(socketChannel);
		      
		    	// Write until there's not more data ...
		    	while (!queue.isEmpty()) 
		    	{
		    		ByteBuffer buf = (ByteBuffer) queue.get(0);
		    		socketChannel.write(buf);
		    		if (buf.remaining() > 0) {
		    			// ... or the socket's buffer fills up
		    			break;
		    		}
		    		queue.remove(0);
		    	}
		      
		    	if (queue.isEmpty()) 
		    	{
			        // We wrote away all data, so we're no longer interested
			        // in writing on this socket. Switch back to waiting for
			        // data.
		    		key.interestOps(SelectionKey.OP_READ);
		    	}
		    }
	}
	
	/**
	 * Method used to close resources.
	 */
	private void cleanup() {
		if (selector != null)
			try {
				selector.close();
			} catch (IOException e) {
			}

		if (serverSocketChannel != null)
			try {
				serverSocketChannel.close();
			} catch (IOException e) {
			}
	}

}
