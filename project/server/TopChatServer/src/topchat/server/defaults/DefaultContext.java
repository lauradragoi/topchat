package topchat.server.defaults;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.apache.log4j.Logger;

/**
 * Describes a state of a connection between the server and a client
 * @author ldragoi
 *
 */
public class DefaultContext 
{	
	protected static final int BUF_SIZE	= 8192;		// 2^13
	
	protected ByteBuffer readBuffer = null;
	protected ByteBuffer writeBuffer = null;
		
	private   int				totalBytes	= 0;
	
	private static Logger	logger		= Logger.getLogger(DefaultContext.class);	
	
	public DefaultContext()
	{		
		readBuffer = ByteBuffer.allocateDirect(BUF_SIZE);
		writeBuffer = ByteBuffer.allocateDirect(BUF_SIZE);		
	}
	
	/**
	 * @param old
	 */
	public DefaultContext(DefaultContext old) {
		this.readBuffer  = old.readBuffer;		
		this.writeBuffer = old.writeBuffer;		
	}

	public ByteBuffer getReadBuffer()
	{
		return readBuffer;
	}

	public ByteBuffer getWriteBuffer()
	{
		return writeBuffer;
	}

	/*
	public void read(ReadableByteChannel socketChannel) throws IOException 
	{
		int bytes = 0, transferred = 0;
		//ReadableByteChannel socketChannel = (ReadableByteChannel)key.channel();
		
		
		if (socketChannel == null)
		{
			logger.warn("Socket is null");
			return ;
		}
						
		if (totalBytes == 0)
		{
			crtBuffer = sizeBuffer;
			sizeBuffer.clear();
		}
		
		if (totalBytes == 4)
		{
			logger.debug("Reading data " + sizeBuffer.getInt(0));			
			crtBuffer = readBuffer;
			prepareRead(sizeBuffer.getInt(0));			
		}
		
		while ( (bytes = socketChannel.read(crtBuffer)) > 0)
		{
			if (bytes == -1)
			{
				logger.warn("Read returned EOF");
				throw new EOFException();
			}
			
			transferred += bytes;	
			
			totalBytes += bytes;
			
			if (totalBytes == 4)
			{
				return ;					
			}
		}
		
		if (transferred == 0)
			throw new IOException("0 bytes read.");
		
//		if (totalBytes > 4)
//			progressRead(transferred);
		
		if (totalBytes > 4 && totalBytes == sizeBuffer.getInt(0) + 4)
		{			
			processRead();
			totalBytes = 0;
		}		
	}
	

	public void write(WritableByteChannel socketChannel) throws IOException {
		int bytes = 0, transferred = 0;
		//WritableByteChannel socketChannel = (WritableByteChannel)key.channel();
		
		if (totalBytes == 0)
		{
			logger.debug("Writing size " + writeBuffer.limit());
			sizeBuffer.clear();
			sizeBuffer.putInt(writeBuffer.limit());		
			sizeBuffer.flip();
			crtBuffer = sizeBuffer;			
		}
		
		if (totalBytes == 4)
		{			
			crtBuffer = writeBuffer;				
		}
	
		while ( (bytes = socketChannel.write(crtBuffer)) > 0)
		{									
			transferred += bytes;
			
			totalBytes += bytes;
			
			if (totalBytes == 4)
			{
				return ;			
			}		
		}
		
		if (transferred == 0)
		{
			logger.debug("exception 0 bytes written");
			throw new IOException("0 bytes written.");
		}
		
//		if (totalBytes > 4)
//			progressWrite(transferred);

		
		if (totalBytes > 4 && totalBytes == sizeBuffer.getInt(0) + 4)
		{
			processWrite();
			totalBytes = 0;
		}	
	}
	*/
	/**
	 * Default implementation: reset buffer to receive new message.
	 * @param messageSize - size of incoming message
	 */
	protected void prepareRead(int messageSize) throws IOException {
		readBuffer.clear();
		readBuffer.limit(messageSize);
	}	
	
	public void processRead(byte[] b) 
	{
	}
	
	public void processWrite()
	{	
	}
} 
