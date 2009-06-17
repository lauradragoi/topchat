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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	// Maps a SocketChannel to a list of ByteBuffer instances
	private Map<SocketChannel, List<ByteBuffer> > pendingData = new HashMap<SocketChannel, List<ByteBuffer> >();

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
	        					SelectionKey key = change.socket.keyFor(this.selector);
	        					key.interestOps(change.ops);
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
				
		// manage this connection
		// DefaultConnectionManager connManager = prot.getConnectionManager();		
		
		// start reading after accept
		// SelectionKey newKey = socketChannel.register(key.selector(), SelectionKey.OP_READ, connManager);
		
		// this is WRONG! : only this thread should modify selection key
		// connManager.setKey(newKey);
		
		logger.info("Accepted connection from "
				+ socketChannel.socket().getRemoteSocketAddress());
	}
	
	
	private void read(SelectionKey key) throws IOException 
	{
		    SocketChannel socketChannel = (SocketChannel) key.channel();

		    // Clear out our read buffer so it's ready for new data
		    this.readBuffer.clear();
		    
		    // Attempt to read off the channel
		    int numRead;
		    try {
		      numRead = socketChannel.read(this.readBuffer);
		    } catch (IOException e) {
		      // The remote forcibly closed the connection, cancel
		      // the selection key and close the channel.
		      key.cancel();
		      socketChannel.close();
		      return;
		    }

		    if (numRead == -1) {
		      // Remote entity shut the socket down cleanly. Do the
		      // same from our end and cancel the channel.
		      key.channel().close();
		      key.cancel();
		      return;
		    }

		    // Hand the data off to the protocol
		    // The protocol itself only hands it out to an executor thread and returns
		    prot.processData(this, socketChannel, this.readBuffer.array(), numRead);
	}
	
	private void write(SelectionKey key) throws IOException 
	{
		    SocketChannel socketChannel = (SocketChannel) key.channel();

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
	 * Handle the read operation on a thread from the pool
	 * 
	 * @param key the key associated with the operation
	 * @throws IOException
	 */
	/*
	private void read(final SelectionKey key) throws IOException {
		// remove all interests
		key.interestOps(0);

		pool.execute(new Runnable() {
			public void run() {
				int bytes;
				DefaultConnectionManager conn = (DefaultConnectionManager) key.attachment();
				ByteBuffer buf = conn.getReadBuffer();
				SocketChannel socketChannel = (SocketChannel) key.channel();
				

				try {
					// read as much as you can
					while ((bytes = socketChannel.read(buf)) > 0)
						;

					// check for EOF
					if (bytes == -1)
						throw new IOException("EOF");

					processRead(conn, buf);

					// keep on reading
					key.interestOps(SelectionKey.OP_READ);

					// update selector
					key.selector().wakeup();
				} catch (IOException e) {
					logger.fatal("Connection closed: " + e.getMessage());

					conn.close();
					try {
						socketChannel.close();
					} catch (IOException exc) {
					}
				}
			}
		});
	}
	*/

	/**
	 * Handle the write operation on a thread from the pool
	 * 
	 * @param key the key associated with the operation
	 * @throws IOException
	 */	
	/*
	private void write(final SelectionKey key) throws IOException {
		// remove all interests
		key.interestOps(0);

		pool.execute(new Runnable() {
			public void run() {		
				DefaultConnectionManager conn = (DefaultConnectionManager) key.attachment();
				ByteBuffer buf = conn.getWriteBuffer();
				SocketChannel socketChannel = (SocketChannel) key.channel();
		
				try {
					while ((socketChannel.write(buf)) > 0)
						;
		
					if (!buf.hasRemaining()) {
						buf.clear();
						
						processWrite(conn, buf);
																		
						//keep on reading
						key.interestOps( SelectionKey.OP_READ);
						
						// update selector
						key.selector().wakeup();
					}
					
					
		
				} catch (IOException e) {
					logger.fatal("Connection closed: " + e.getMessage());
					
					conn.close();
					try 
					{						
						socketChannel.close();					
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		
				}
			}
		});
	}
	*/
	
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

	/**
	 * Method executed when a read operation has finished
	 * @param conn the connection manager associated with the connection on which the operation took place
	 * @param buf the buffer used for the operation
	 */
	/*
	private void processRead(DefaultConnectionManager conn, ByteBuffer buf) 
	{		
		// drain buffer
		buf.flip();

		int count = buf.remaining();
		byte[] rd = new byte[count];
	
		buf.get(rd);
		buf.clear();
			
		// inform connection manager
		conn.processRead(rd);						
	}
	*/
	

	/**
	 * Method executed when a write operation has finished
	 * @param conn the connection manager associated with the connection on which the operation took place
	 * @param buf the buffer used for the operation
	 */
	/*
	private void processWrite(DefaultConnectionManager conn, ByteBuffer buf) 
	{
		// clean buffer
		buf.clear();	
		
		// inform connection manager
		conn.processWrite();
	}
	*/
}
