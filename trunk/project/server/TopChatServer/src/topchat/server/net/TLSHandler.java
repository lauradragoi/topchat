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

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;

import org.apache.log4j.Logger;

import topchat.server.interfaces.Net;

/**
 * Created when a connection needs to be secured with TLS.
 * Acts as a running thread until the handshake process is complete.
 * Afterwards only acts as a container for the methods needed to encode/decode secure data.
 */
public class TLSHandler implements Runnable {

	public Net net;
	public SocketChannel socket;
	
	public SSLEngine sslEngine;
	public HandshakeStatus status;
	public boolean handshakeComplete;
	
	public ByteBuffer appBB = null;
	
	/** A dummy empty buffer */
	private static ByteBuffer dummyBB = ByteBuffer.allocate(0);
	
	private ByteBuffer readBuffer;
	private ByteBuffer writeBuffer;	 
	
	private static Logger logger = Logger.getLogger(TLSHandler.class);		
	
	public TLSHandler(Net net, SocketChannel socket, SSLEngine sslEngine)
	{
		this.net = net;
		this.socket = socket;
		this.sslEngine = sslEngine;
		
		this.status = HandshakeStatus.NEED_UNWRAP;
		this.handshakeComplete = false;
		
		int netBBSize = sslEngine.getSession().getPacketBufferSize();
		int appBBSize = sslEngine.getSession().getApplicationBufferSize();
		appBB = ByteBuffer.allocate(appBBSize);
		
		readBuffer = ByteBuffer.allocate(netBBSize);
		
		writeBuffer = ByteBuffer.allocate(netBBSize);
        writeBuffer.position(0);
        writeBuffer.limit(0);
	}
	
	private boolean dataToProcess = false;
	
	/**
	 * Decode secure received data into raw application data 
	 * @param readBuffer
	 * @return
	 */
	public synchronized byte[] processData(ByteBuffer readBuffer)
	{	
		if (!handshakeComplete)
		{		
			readBuffer.flip();
			this.readBuffer.put(readBuffer);
			
			//readSem.release();
			dataToProcess = true;
									
			return null;
		}
		else
		{
			logger.debug("Decoding data");
			readBuffer.flip();
			
			logger.debug("za limit " + readBuffer.limit());
			
			SSLEngineResult result = null;
			
			appBB = ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize());
			
			try {
				result = sslEngine.unwrap(readBuffer, appBB);
			} catch (SSLException e) {
				logger.fatal("unwrap error");
				e.printStackTrace();
			}
			logger.debug("Unwrap result " + result);
			readBuffer.compact();
			
			
			// drain
			appBB.flip();
			
			int count = appBB.remaining();
			byte[] dataResult = new byte[count];

			appBB.get(dataResult);
			
			return dataResult;
		}		
	}
	
	/**
	 * Obtain secure data corresponding to raq data
	 * @param data
	 * @return
	 */
	public byte[] getSecureData(byte[] data)
	{
		SSLEngineResult result = null;
		writeBuffer.clear();
		try {
			result = sslEngine.wrap(ByteBuffer.wrap(data), writeBuffer);
		} catch (SSLException e) {
			logger.warn("wrap exception: " + e);
		}
		writeBuffer.flip();
		
		logger.debug("wrap result " + result);
		
		int count = writeBuffer.remaining();
		byte[] secureData = new byte[count];

		writeBuffer.get(secureData);
		
		return secureData;
	}
	
	SSLEngineResult result = null;
	
	/**
	 * Handles the TLS negotiation
	 */

	public void run()
	{
		logger.debug("processing");
			
		
        // this will run until handshake is complete
        while (!handshakeComplete) 
        {          	
        	boolean result = processHandshaking();
        	
        	if (!result)
        	{
        		// have some beauty sleep until new data is received
        		try
        		{
        			Thread.sleep(50);
        		} catch (Exception e) {					
				}
        	}
        }
	}

	public synchronized boolean processHandshaking()
	{

   		switch (status) 
   		{    		
   			case NEED_UNWRAP:	
   				
   					if (!dataToProcess)
   						return false;
   					
   					dataToProcess = false;   				
   					needIO: 	   						
   					while (status == HandshakeStatus.NEED_UNWRAP) 	   					
    				{
    					readBuffer.flip();
    					
    		            try {
    						result = sslEngine.unwrap(readBuffer, appBB);
    					} catch (SSLException e) {									
    						logger.warn("unwrap exception " + e);									
    					}
    		
    					readBuffer.compact();
    			                
    					logger.debug("unwrap result " + result);
    		
    					status = result.getHandshakeStatus();
    		
    					switch (result.getStatus()) 
    					{
    						case OK:
    							switch (status) 
    							{
    								case NOT_HANDSHAKING:
    									logger.debug("Not handshaking in initial handshake");
    									continue;
    								case NEED_TASK:
    									status = doTasks();
    									break;
    								case FINISHED:
    									handshakeComplete = true;
    									return true;
    							}	
    						break;
    		
    						case BUFFER_UNDERFLOW:
    							break needIO;
    		
    						default: // BUFFER_OVERFLOW/CLOSED:
    							logger.debug("Received" + result.getStatus()
    									        	+ "during initial handshaking");
    						continue;
    					}
    					
    					logger.debug("new status " + status);
    					
    				}
    		    	
    		    	if (status != HandshakeStatus.NEED_WRAP)
    		    		break;

    		// fall through	
        	case NEED_WRAP:
        		
        		writeBuffer.clear();
        		try {
					result = sslEngine.wrap(dummyBB, writeBuffer);
				} catch (SSLException e) {
					logger.warn("wrap exception: " + e);
				}
				writeBuffer.flip();

				status = result.getHandshakeStatus();

				logger.debug("wrap result " + result);
            
				switch (result.getStatus()) 
				{
						case OK:
							if (status == HandshakeStatus.NEED_TASK) {
								status = doTasks();
							}
							
							// drain the buffer that was filled
							flush(writeBuffer);
							
	            
							if (status == HandshakeStatus.FINISHED)
							{
								logger.debug("Handshake complete.");		            			
								handshakeComplete = true;
								return true;
							}	
							
							
							break;

						default: // BUFFER_OVERFLOW/BUFFER_UNDERFLOW/CLOSED:
							logger.debug("Received" + result.getStatus()
									+ "during initial handshaking");	
						
						//continue;
				}
				break;

        	default: // NOT_HANDSHAKING/NEED_TASK/FINISHED
        		logger.warn("Invalid Handshaking State" + status);
			}	
   		
   			return true;
	}
	

	
	/**
	 * Send data to the network module to be sent
	 * @param bb
	 */
	private void flush(ByteBuffer bb)
	{
		int count = bb.remaining();
		byte[] data = new byte[count];
	
		bb.get(data);		
		
		logger.debug("Writing " + count);
		
		net.sendRaw(socket, data);	
	}
	
	/**
	 * Handle tasks from the tlsEngine
	 * @return
	 */
	private SSLEngineResult.HandshakeStatus doTasks() 
	{
		   logger.debug("Doing tasks");
	       Runnable runnable;

	       while ((runnable = sslEngine.getDelegatedTask()) != null) {
	    	   logger.debug("running task");
	           runnable.run();
	       }
	       
	       logger.debug("status after task " + sslEngine.getHandshakeStatus());
	       
	       return sslEngine.getHandshakeStatus();	      	       
	}
}
