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
	
	public DefaultConnectionManager()
	{
		context = new DefaultContext();
	}
	
	public ByteBuffer getReadBuffer()
	{
		return context.getReadBuffer();
	}

	public ByteBuffer getWriteBuffer()
	{
		return context.getWriteBuffer();
	}
	
	public void processWrite() 
	{
		logger.debug("Written.");
	}
	
	public void processRead(byte[] rd) 
	{
		String s = new String(rd);
		logger.debug("Received: " + s);

		rd = null;
		s = null;
	}	
	
	public void setKey(SelectionKey key) {
		this.key = key;
	}	

}
