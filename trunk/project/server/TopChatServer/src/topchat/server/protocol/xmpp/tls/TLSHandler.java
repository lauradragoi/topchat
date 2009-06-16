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
package topchat.server.protocol.xmpp.tls;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.Semaphore;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.xml.stream.events.StartElement;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultConnectionManager;


public class TLSHandler implements Runnable {

	private static Logger logger = Logger.getLogger(TLSHandler.class);
	
	private SSLEngine tlsEngine;
	private boolean handshake = false;
	
	private SelectionKey sk = null;
    
    /*
     * During the initial handshake, keep track of the next SSLEngine operation that needs to occur:
     *
     * NEED_WRAP/NEED_UNWRAP
     *
     * Once the initial handshake has completed, we can short circuit handshake checks with
     * initialHSComplete.
     */
   private HandshakeStatus initialHSStatus;
   private boolean initialHSComplete;

   private int appBBSize;
   private int netBBSize;

   /*
     * All I/O goes through these buffers. It might be nice to use a cache of ByteBuffers so we're
     * not alloc/dealloc'ing ByteBuffer's for each new SSLEngine. Outbound application data is
     * supplied to us by our callers.
     */
   private ByteBuffer incomingNetBB;
   private ByteBuffer outgoingNetBB;

   private ByteBuffer appBB;

   /*
     * An empty ByteBuffer for use when one isn't available, say as a source buffer during initial
     * handshake wraps or for close operations.
     */
   private static ByteBuffer hsBB = ByteBuffer.allocate(0);	
   
//   private ReadableByteChannel rbc;
//   private WritableByteChannel wbc;
	
   private DefaultConnectionManager mgr;
   
   private Semaphore write_sem = new Semaphore(0);
   private Semaphore read_sem = new Semaphore(0);
   
	public TLSHandler(DefaultConnectionManager mgr, SSLEngine tlsEngine, SelectionKey key)
	{	
		this.mgr = mgr;
		this.tlsEngine = tlsEngine;
		
//		rbc = (ReadableByteChannel) key.channel();
//		wbc = (WritableByteChannel) key.channel();
		
        initialHSStatus = HandshakeStatus.NEED_UNWRAP;
        initialHSComplete = false;

        netBBSize = tlsEngine.getSession().getPacketBufferSize();
        appBBSize = tlsEngine.getSession().getApplicationBufferSize();

        incomingNetBB = ByteBuffer.allocate(netBBSize);
        outgoingNetBB = ByteBuffer.allocate(netBBSize);
        outgoingNetBB.position(0);
        outgoingNetBB.limit(0);

        appBB = ByteBuffer.allocate(appBBSize);		
		
        // start this thread - its job is to handle initial handshake
        new Thread(this).run();
        
		// make sure it handshakes 	
        /*
        while (!initialHSComplete) {
            try {
				initialHSComplete = doHandshake(key);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        */
	}
	
	// sunt la fel ca un context
	// am un buffer de citire si unul de scriere
	// modulul de retea pe ele le foloseste
	// deasemenea aflu cand se citeste ceva ca mi se apeleaza process read 
	// la fel si cu write
	
	public ByteBuffer getReadBuffer()
	{
		return incomingNetBB;
	}
	
	public ByteBuffer getWriteBuffer()
	{
		return outgoingNetBB;
		
	}


	
	public boolean isHandshakeComplete()
	{
		return initialHSComplete;
	}
	
 
	public void processRead(byte[] rd)
	{
		read_sem.release();
		incomingNetBB.wrap(rd);
		/*
		try {
			doHandshake(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public void processWrite()
	{
		write_sem.release();
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
	        if (outgoingNetBB.hasRemaining()) {
	        	       
	        	logger.debug("outgoing net buffer still has remaining");
	
	            try {
					if (!flush(outgoingNetBB)) {
						logger.debug("cannot flush outgoing net buffer");
					    continue;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	            // See if we need to switch from write to read mode.
	
	            switch (initialHSStatus) {
	
	            /*
	                * Is this the last buffer?
	                */
	            case FINISHED:
	            	logger.debug("finished");
	                initialHSComplete = true;
	
	            case NEED_UNWRAP:
	            	logger.debug("need unwrap 1");
	            	
	                if (sk != null) {
	                    sk.interestOps(SelectionKey.OP_READ);
	                }
	                break;
	            }
	
	            logger.debug("ret " + initialHSComplete + "  1");
	             
	            continue;
	        }
	

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
	                incomingNetBB.flip();
	                logger.debug("flipped");
	                try {
						result = tlsEngine.unwrap(incomingNetBB, appBB);
					} catch (SSLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                logger.debug("unwrapped");
	                incomingNetBB.compact();
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
	                    if (sk != null) {
	                        sk.interestOps(SelectionKey.OP_READ);
	                    }
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
	            outgoingNetBB.clear();
	            try {
						result = tlsEngine.wrap(hsBB, outgoingNetBB);
					} catch (SSLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            outgoingNetBB.flip();
	
	            initialHSStatus = result.getHandshakeStatus();
	
	            logger.debug("wrap result " + result);
	            
	            switch (result.getStatus()) {
	            case OK:
	
	                if (initialHSStatus == HandshakeStatus.NEED_TASK) {
	                    initialHSStatus = doTasks();
	                }
	
	                if (sk != null) {
	                    sk.interestOps(SelectionKey.OP_WRITE);
	                }
	
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
    }

    /*
     * Writes ByteBuffer to the SocketChannel. Returns true when the ByteBuffer has no remaining
     * data.
     */
   private boolean flush(ByteBuffer bb) throws IOException {
	   
	   outgoingNetBB.flip();
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
 	
	
	/*
	private void doHandshake(SSLEngine tlsEngine)
	{
		HandshakeStatus hsStatus = tlsEngine.getHandshakeStatus();
		
		int appSize = tlsEngine.getSession().getApplicationBufferSize();
		int netSize = tlsEngine.getSession().getPacketBufferSize();
		
		ByteBuffer buf = ByteBuffer.allocate(netSize);
		ByteBuffer dst = ByteBuffer.allocate(appSize);
		
		ByteBuffer wrapDst = ByteBuffer.allocate(netSize);
		wrapDst.position(0);
		wrapDst.limit(0);
		
		SSLEngineResult result = null;
		
		logger.debug("Handshake status : " + hsStatus);
		
		switch (hsStatus) {
			case NEED_UNWRAP:
				while (hsStatus == HandshakeStatus.NEED_UNWRAP)
				{
					try {
						result = tlsEngine.unwrap(buf, dst);						
					} catch (SSLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		    
					
	                hsStatus = result.getHandshakeStatus();		
	                
	                logger.debug("unwrapped hsStatus: " + hsStatus + " res " + result.getStatus());
	                
	                switch (result.getStatus()) {
	                	case OK:
	                			switch (hsStatus) 
	                			{
	                				case NOT_HANDSHAKING:
	                					logger.fatal("Not handshaking during initial handshake");
	                					return;
	                					
	                				case NEED_TASK:
	                					
	                			        Runnable runnable;

	                			        while ((runnable = tlsEngine.getDelegatedTask()) != null) {
	                			            runnable.run();
	                			        }
	                			        
	                			        hsStatus = tlsEngine.getHandshakeStatus();
	                			        
	                			        logger.debug("hsStatus after task run " + hsStatus);
	                			        
	                					break;
	                			}		                   
	                }
				}
			
			//break;					
			// fall through	
			case NEED_WRAP:
				 ByteBuffer dummySrc = ByteBuffer.allocate(0);
				 
				 wrapDst.clear();
		         try {
					result = tlsEngine.wrap(dummySrc, wrapDst);
		         } catch (SSLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		         }
		         
		         wrapDst.flip();
		         

		         hsStatus = result.getHandshakeStatus();

		         logger.debug("wrap did + hsStatus: " + hsStatus + " res " + result.getStatus());
		         
		         switch (result.getStatus()) 
		         {
//		         /*
		            case OK:
		                if (initialHSStatus == HandshakeStatus.NEED_TASK) {
		                    initialHSStatus = doTasks();
		                }

		                if (sk != null) {
		                    sk.interestOps(SelectionKey.OP_WRITE);
		                }

		                break;

		            default: // BUFFER_OVERFLOW/BUFFER_UNDERFLOW/CLOSED:
		                throw new IOException("Received" + result.getStatus()
		                        + "during initial handshaking");
		                        //
		         }
		         
		         break;					
		            
			default:
				break;
		}
		
		if (hsStatus == HandshakeStatus.NEED_UNWRAP)
		{			
			while (hsStatus == HandshakeStatus.NEED_UNWRAP)
			{
				try {
					result = tlsEngine.unwrap(buf, dst);
				} catch (SSLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		    
				
                hsStatus = result.getHandshakeStatus();		
                
                logger.debug("2unwrapped hsStatus: " + hsStatus + " res " + result.getStatus());
                
                switch (result.getStatus()) {
                	case OK:
                			switch (hsStatus) 
                			{
                				case NOT_HANDSHAKING:
                					logger.fatal("2Not handshaking during initial handshake");
                					return;
                					
                				case NEED_TASK:
                					
                			        Runnable runnable;

                			        while ((runnable = tlsEngine.getDelegatedTask()) != null) {
                			            runnable.run();
                			        }
                			        
                			        hsStatus = tlsEngine.getHandshakeStatus();
                			        
                			        logger.debug("2hsStatus after task run " + hsStatus);
                			        
                					break;
                			}
                	case BUFFER_UNDERFLOW:	                	     	                	      
                		return;
                }
			}
		
		}		
	}
	*/
}
