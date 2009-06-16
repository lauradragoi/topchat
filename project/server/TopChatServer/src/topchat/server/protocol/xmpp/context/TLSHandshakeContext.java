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
package topchat.server.protocol.xmpp.context;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultContext;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;

/**
 * Context in which the server and the client negotiate the TLS handshake.
 * Context runs for as long as the handshake takes place in a separate thread as well as 
 * handling read or write events on the stream.
 */
public class TLSHandshakeContext extends XMPPContext implements Runnable {

	private static Logger logger = Logger.getLogger(TLSHandshakeContext.class);	
			
	/** The TLS engine used for securing the stream */
	private SSLEngine tlsEngine;
	
	/** The status of the handshake */
	private HandshakeStatus initialHSStatus;
	/** Flag indicating whether or not the handshake was finished */
	private boolean initialHSComplete;

	/** Size of the buffers needed in this context */
	private int appBBSize;
	private int netBBSize;
	   
	/** A dummy empty buffer */
	private static ByteBuffer dummyBB = ByteBuffer.allocate(0);
	
	/** A buffer to hold application data */
	private ByteBuffer appBB;

	/** Semaphores used for synchronizing the thread of this context with the read and write events of the stream */
	private Semaphore writeSem = new Semaphore(0);
	private Semaphore readSem = new Semaphore(0);	   
	
	public TLSHandshakeContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);
						
		tlsEngine = mgr.getTLSEngine();
		
        initialHSStatus = HandshakeStatus.NEED_UNWRAP;
        initialHSComplete = false;

        netBBSize = tlsEngine.getSession().getPacketBufferSize();
        appBBSize = tlsEngine.getSession().getApplicationBufferSize();

        // resize buffers to fit TLS needs
        readBuffer = ByteBuffer.allocate(netBBSize);
        writeBuffer = ByteBuffer.allocate(netBBSize);
        writeBuffer.position(0);
        writeBuffer.limit(0);

        appBB = ByteBuffer.allocate(appBBSize);
        
        // start the context thread - its job is to handle initial handshake
        new Thread(this).start();
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		
		logger.debug("Received: " + s);
						
		s = null;
		
		// announce read
		readSem.release();	
		
		// put read data back in buffer to be unwrapped
		readBuffer.put(rd);
		
		logger.debug("read released");		
	}	
	
	@Override
	public void processWrite()
	{	
		// announce write
		writeSem.release();
		
		logger.debug("write released");
	}
	
	@Override
    public void run()
    {
        SSLEngineResult result = null;
        
        // this will run until handshake is complete
        while (!initialHSComplete) 
        {        
	        switch (initialHSStatus) 
	        {
	        	case NEED_UNWRAP:	        		 	        	
	        		// wait for something to be read
					readSem.acquireUninterruptibly();
	        	
					needIO: while (initialHSStatus == HandshakeStatus.NEED_UNWRAP) 
							{
								readBuffer.flip();
								
				                try {
									result = tlsEngine.unwrap(readBuffer, appBB);
								} catch (SSLException e) {									
									logger.warn("unwrap exception " + e);									
								}

								readBuffer.compact();
	                	                
								logger.debug("unwrap result " + result);
	               
								initialHSStatus = result.getHandshakeStatus();
	                
								switch (result.getStatus()) 
								{
									case OK:
										switch (initialHSStatus) 
										{
											case NOT_HANDSHAKING:
												logger.debug("Not handshaking in initial handshake");
												continue;
											case NEED_TASK:
												initialHSStatus = doTasks();
												break;
											case FINISHED:
												initialHSComplete = true;
												break needIO;
										}	
									break;
	
									case BUFFER_UNDERFLOW:
										break needIO;
	
									default: // BUFFER_OVERFLOW/CLOSED:
										logger.debug("Received" + result.getStatus()
												        	+ "during initial handshaking");
									continue;
								}
							}
	
				// fall through	
	        	case NEED_WRAP:
	        		
	        		writeBuffer.clear();
	        		try {
						result = tlsEngine.wrap(dummyBB, writeBuffer);
					} catch (SSLException e) {
						logger.warn("wrap exception: " + e);
					}
					writeBuffer.flip();

					initialHSStatus = result.getHandshakeStatus();
	
					logger.debug("wrap result " + result);
	            
					switch (result.getStatus()) 
					{
							case OK:
								if (initialHSStatus == HandshakeStatus.NEED_TASK) {
									initialHSStatus = doTasks();
								}
								
								// drain the buffer that was filled
								flush(writeBuffer);
		            
								if (initialHSStatus == HandshakeStatus.FINISHED)
								{
									logger.debug("done ");		            			
									initialHSComplete = true;
									continue;
								}								
								break;
	
							default: // BUFFER_OVERFLOW/BUFFER_UNDERFLOW/CLOSED:
								logger.debug("Received" + result.getStatus()
										+ "during initial handshaking");
							continue;
					}
					break;
	
	        	default: // NOT_HANDSHAKING/NEED_TASK/FINISHED
	        		logger.warn("Invalid Handshaking State" + initialHSStatus);
	        	
	        } // switch
        }
        
        // if loop is exited handshake is complete
        // signal end of this context
        setDone();
    }

   /**
    * Blocks until information in Buffer has been sent on the stream.
    */
   private void flush(ByteBuffer bb)
   {
	   // announce manager reading desire
	   mgr.registerForWrite();
	   
	   // block until write is signaled
	   writeSem.acquireUninterruptibly();			   
   }

   /**
    * Handle tasks from the tlsEngine
    * @return
    */
   private SSLEngineResult.HandshakeStatus doTasks() {

       Runnable runnable;

       while ((runnable = tlsEngine.getDelegatedTask()) != null) {
           runnable.run();
       }
       return tlsEngine.getHandshakeStatus();
   }
 		
}
