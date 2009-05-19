package topchat.server.defaults;

import java.nio.ByteBuffer;

public class DefaultContext 
{	
	private ByteBuffer internalBuffer = null;
	
	public DefaultContext()
	{
		
	}
	
	public DefaultContext(int bufferSize)
	{
		internalBuffer = ByteBuffer.allocateDirect(bufferSize);
	}

	public ByteBuffer getBuffer()
	{
		return internalBuffer;
	}
} 
