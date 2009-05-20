package topchat.server.defaults;

import java.nio.ByteBuffer;

/**
 * Describes a state of a connection between the server and a client
 * @author ldragoi
 *
 */
public class DefaultContext 
{	
	private ByteBuffer readBuffer = null;
	private ByteBuffer writeBuffer = null;
	
	public DefaultContext()
	{		
	}
	
	public DefaultContext(int bufferSize)
	{
		super();
		readBuffer = ByteBuffer.allocateDirect(bufferSize);
		writeBuffer = ByteBuffer.allocateDirect(bufferSize);
	}

	public ByteBuffer getReadBuffer()
	{
		return readBuffer;
	}

	public ByteBuffer getWriteBuffer()
	{
		return writeBuffer;
	}
	
	
	public void processRead(byte[] rd) 
	{
	}
	
	public void processWrite()
	{	
	}
} 
