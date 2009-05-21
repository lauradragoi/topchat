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

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import org.apache.log4j.Logger;

/**
 * Manages a connection between the server and a client
 * @author ldragoi
 *
 */
public class DefaultConnectionManager 
{
	/** The current context of the connection */
	protected DefaultContext context;
	
	/** The selection key associated with the connection */
	protected SelectionKey	key	= null;	

	private static Logger logger = Logger.getLogger(DefaultConnectionManager.class);
		
	/**
	 * Obtain buffer used for reading on this connection
	 * from the context
	 * @return read buffer
	 */
	public ByteBuffer getReadBuffer()
	{
		return context.getReadBuffer();
	}

	/**
	 * Obtain buffer used for writing on this connection
	 * from the context
	 * @return write buffer
	 */
	public ByteBuffer getWriteBuffer()
	{
		return context.getWriteBuffer();
	}
		
	/**
	 * Method called after write operation has occured
	 */
	public void processWrite() 
	{
		logger.debug("Written.");
		
		// inform the context
		context.processWrite();
	}
	
	/**
	 * Method called after read operation has occured
	 * @param rd byte array containing read data
	 */
	public void processRead(byte[] rd) 
	{
		String s = new String(rd);
		
		logger.debug("Received: " + s);
		
		// inform the context
		context.processRead(rd);

		// clean up
		s = null;
	}	
	
	/**
	 * Set the key associated with this connection
	 * @param key the value to be set for the key
	 */
	public void setKey(SelectionKey key) {
		this.key = key;
	}	
	
	/**
	 * Method called to announce that the write buffer from the context
	 * contains data that needs to be written
	 */
	public void registerForWrite()
	{
		logger.debug("Registered key for write");
		
		key.interestOps( key.interestOps() | SelectionKey.OP_WRITE );
		key.selector().wakeup();
	}
	
	/**
	 * Method called to announce that the context is ready to read data
	 * @deprecated connection is always ready for reading data
	 */
	public void registerForRead()
	{
		logger.debug("Registered key for read");
		
		key.interestOps( key.interestOps() | SelectionKey.OP_READ );
		key.selector().wakeup();
	}	
	
	
	/**
	 * Method called to stop reading data
	 * @deprecated network module handles unregistering
	 */
	public void unregisterRead()
	{
		logger.debug("Unregister read");
		
		key.interestOps( key.interestOps() & (~SelectionKey.OP_READ) );
		key.selector().wakeup();
	}
	
	/**
	 * Method called to stop writing data
	 * @deprecated network module handles unregistering
	 */	
	public void unregisterWrite()
	{
		logger.debug("Unregister write");
		
		key.interestOps( key.interestOps() & (~SelectionKey.OP_WRITE) );
		key.selector().wakeup();
	}	
}
