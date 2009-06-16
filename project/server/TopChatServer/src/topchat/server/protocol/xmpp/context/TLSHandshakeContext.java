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
import java.nio.channels.SelectionKey;
import java.util.concurrent.Semaphore;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultContext;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;

public class TLSHandshakeContext extends XMPPContext implements Runnable {

	private static Logger logger = Logger.getLogger(TLSHandshakeContext.class);	
			
	private SSLEngine tlsEngine;
	
	   private HandshakeStatus initialHSStatus;
	   private boolean initialHSComplete;

	   private int appBBSize;
	   private int netBBSize;
	   
	   private static ByteBuffer hsBB = ByteBuffer.allocate(0);	
	   private ByteBuffer appBB;

	   private Semaphore write_sem = new Semaphore(0);
	   private Semaphore read_sem = new Semaphore(0);	   
	
	public TLSHandshakeContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);
					
		mgr.secureConnection();	
		
		tlsEngine = mgr.getTLSEngine();
		
        initialHSStatus = HandshakeStatus.NEED_UNWRAP;
        initialHSComplete = false;

        netBBSize = tlsEngine.getSession().getPacketBufferSize();
        appBBSize = tlsEngine.getSession().getApplicationBufferSize();

        // resize buffers to fit tls needs
        readBuffer = ByteBuffer.allocate(netBBSize);
        writeBuffer = ByteBuffer.allocate(netBBSize);
        writeBuffer.position(0);
        writeBuffer.limit(0);

        appBB = ByteBuffer.allocate(appBBSize);
        
        // start this thread - its job is to handle initial handshake
        new Thread(this).start();
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		
		logger.debug("Received: " + s);
						
		s = null;
		
		read_sem.release();
		readBuffer.put(rd);
		logger.debug("read released");
		
	}	
	
	@Override
	public void processWrite()
	{
		logger.debug("write buffer has : "  + writeBuffer.remaining());
		write_sem.release();
		logger.debug("write released");
	}
	
	@Override
    public void run()
    {
        SSLEngineResult result = null;

        // doar ca eu sunt triggerat mereu
        // ca asa sunt eu
        
        
        // this will run until handshake is complete
        while (!initialHSComplete) 
        {

	        logger.debug("handshake not complete");
	        
	        /*
	           * Flush out the outgoing buffer, if there's anything left in it.
	           */
	        // daca am chestii de trimis
	        // ar trebui sa imi informez manageru de conexiune si sa ma flipez ca sa fiu
	        // comfortabil la accesare de catre modulul de retea
	        // si ar trebui sa astept pana s-a procesat scrierea
	        /*
	        if (writeBuffer.hasRemaining()) {
	        	       
	        	logger.debug("outgoing net buffer still has remaining" + writeBuffer.remaining());
	
	            try {
					if (!flush(writeBuffer)) {
						logger.debug("cannot flush outgoing net buffer");
					    continue;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	            // See if we need to switch from write to read mode.
	
	            switch (initialHSStatus) {
	
	            //
	                // Is this the last buffer?
	                ///
	            case FINISHED:
	            	logger.debug("finished");
	                initialHSComplete = true;
	
	            case NEED_UNWRAP:
	            	logger.debug("need unwrap 1");
	            	
	           //     if (sk != null) {
	           //         sk.interestOps(SelectionKey.OP_READ);
	           //     }
	                break;
	            }
	
	            logger.debug("ret " + initialHSComplete + "  1");
	             
	            continue;
	        }
	*/
	

	        switch (initialHSStatus) {
	
	        case NEED_UNWRAP:
	        	logger.debug("need unwrap 2 ");   
	        	
	        	//read_sem.acquire();
	        	/*
	            try {
						if (rbc.read(incomingNetBB) == -1) {
	            		
							logger.debug("read -1");
						    tlsEngine.closeInbound();
						    logger.debug("cannot read or eof " );
						    continue;
						}
					} catch (SSLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				*/
	        	try {
						read_sem.acquire();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        	
	            needIO: while (initialHSStatus == HandshakeStatus.NEED_UNWRAP) {
	            	logger.debug("i still need unwrap");
	                /*
	                     * Don't need to resize requestBB, since no app data should be generated here.
	                     */
	                readBuffer.flip();
	                logger.debug("flipped");
	                try {
						result = tlsEngine.unwrap(readBuffer, appBB);
					} catch (SSLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                logger.debug("unwrapped");
	                readBuffer.compact();
	                logger.debug("compacted");
	                
	                logger.debug("unwrap result " + result);
	               
	                initialHSStatus = result.getHandshakeStatus();
	                
	                switch (result.getStatus()) {
	
	                case OK:
	                    switch (initialHSStatus) {
	                    case NOT_HANDSHAKING:
	                    	continue;
	                        //throw new IOException("Not handshaking during initial handshake");
	
	                    case NEED_TASK:
	                        initialHSStatus = doTasks();
	                        break;
	
	                    case FINISHED:
	                        initialHSComplete = true;
	                        break needIO;
	                    }
	
	                    break;
	
	                case BUFFER_UNDERFLOW:
	                    /*
	                          * Need to go reread the Channel for more data.
	                          */
	                  //  if (sk != null) {
	                  //      sk.interestOps(SelectionKey.OP_READ);
	                  //  }
	                    break needIO;
	
	                default: // BUFFER_OVERFLOW/CLOSED:
	                    //throw new IOException("Received" + result.getStatus()
	                    //        + "during initial handshaking");
	                	continue;
	                }
	            }
	
	            /*
	                * Just transitioned from read to write.
	                */
	            if (initialHSStatus != HandshakeStatus.NEED_WRAP) {
	                break;
	            }
	
	        // Fall through and fill the write buffers.
	
	        case NEED_WRAP:
	            /*
	                * The flush above guarantees the out buffer to be empty
	                */
	            writeBuffer.clear();
	            try {
						result = tlsEngine.wrap(hsBB, writeBuffer);
					} catch (SSLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            writeBuffer.flip();

	
	            initialHSStatus = result.getHandshakeStatus();
	
	            logger.debug("wrap result " + result);
	            
	            switch (result.getStatus()) {
	            case OK:
	
	                if (initialHSStatus == HandshakeStatus.NEED_TASK) {
	                    initialHSStatus = doTasks();
	                }
	
	                //mgr.registerForWrite();
	                
		            try {
						flush(writeBuffer);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            if (initialHSStatus == HandshakeStatus.FINISHED)
		            {
		            	logger.debug("done ");
		            			
		            	initialHSComplete = true;
		            	continue;
		            }
	               // if (sk != null) {
	              //      sk.interestOps(SelectionKey.OP_WRITE);
	               // }
	
	                break;
	
	            default: // BUFFER_OVERFLOW/BUFFER_UNDERFLOW/CLOSED:
	               // throw new IOException("Received" + result.getStatus()
	               //         + "during initial handshaking");
	            	continue;
	            }
	            break;
	
	        default: // NOT_HANDSHAKING/NEED_TASK/FINISHED
	            throw new RuntimeException("Invalid Handshaking State" + initialHSStatus);
	        } // switch
        }
        
        setDone();
    }

    /*
     * Writes ByteBuffer to the SocketChannel. Returns true when the ByteBuffer has no remaining
     * data.
     */
   private boolean flush(ByteBuffer bb) throws IOException {
	   
	//   writeBuffer.flip();
	   mgr.registerForWrite();
	   
	   	try {
	   		write_sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
       //wbc.write(bb);
       //return !bb.hasRemaining();
	   
   }

   /*
     * Do all the outstanding handshake tasks in the current Thread.
     */
   private SSLEngineResult.HandshakeStatus doTasks() {

       Runnable runnable;

       /*
          * We could run this in a separate thread, but do in the current for now.
          */
       while ((runnable = tlsEngine.getDelegatedTask()) != null) {
           runnable.run();
       }
       return tlsEngine.getHandshakeStatus();
   }
 		
}
