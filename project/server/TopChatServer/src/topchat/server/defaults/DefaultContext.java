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

import topchat.server.util.Utils;


/**
 * Describes the state of a connection between the server and a client
 *
 */
public abstract class DefaultContext 
{	
	// Size of the buffers
	protected static final int BUF_SIZE	= 8192;		// 2^13
	
	// Buffers for read and write operations
	protected ByteBuffer readBuffer = null;
	protected ByteBuffer writeBuffer = null;
	
	// Connection manager handling this context
	protected DefaultConnectionManager mgr = null;
		

	/**
	 * Constructs context
	 */
	public DefaultContext()
	{		
		// allocate buffers
		readBuffer = ByteBuffer.allocateDirect(BUF_SIZE);
		writeBuffer = ByteBuffer.allocateDirect(BUF_SIZE);		
	}
	
	/**
	 * Constructs context controlled by a connection manager
	 * @param mgr the connection manager controlling the context being created
	 */
	public DefaultContext(DefaultConnectionManager mgr)
	{
		this();
		this.mgr = mgr;
	}
	
	/**
	 * Constructs context starting from an existing context
	 * @param old
	 */
	public DefaultContext(DefaultContext old) 
	{		
		// use old buffers
		this.readBuffer  = old.readBuffer;		
		this.writeBuffer = old.writeBuffer;		
	}
	
	/**
	 * Constructs context starting from an existing context
	 * controlled by a connection manager
	 * 
	 * @param mgr the connection manager controlling the context being created 
	 * @param old
	 */
	public DefaultContext(DefaultConnectionManager mgr, DefaultContext old) 
	{
		this(old);
		this.mgr = mgr;
	}

	/**
	 * Obtain buffer used for reading on this connection 
	 * @return read buffer
	 */	
	public ByteBuffer getReadBuffer()
	{
		return readBuffer;
	}

	/**
	 * Obtain buffer used for writing on this connection 
	 * @return write buffer
	 */
	public ByteBuffer getWriteBuffer()
	{
		return writeBuffer;
	}

	/** 
	 * Method to be executed after a reading operation has completed.
	 * @param b the data that was read
	 */
	public abstract void processRead(byte[] b);
	
	/**
	 * Method to be executed after a writing operation has completed.
	 */
	public abstract void processWrite();
	
	/**
	 * Prepare write buffer for draining and announce interest in writing
	 */
	public void flush()
	{
		writeBuffer.flip();
		mgr.registerForWrite();
	}
	
	/**
	 * Put a string into the write buffer
	 * @param s
	 */
	public void write(String s)
	{
		Utils.putStringToBuffer(s, writeBuffer);	
	}
} 
