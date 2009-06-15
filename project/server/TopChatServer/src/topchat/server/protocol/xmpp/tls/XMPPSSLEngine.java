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

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;

import org.apache.log4j.Logger;

public class XMPPSSLEngine {

	private static Logger logger = Logger.getLogger(XMPPSSLEngine.class);
	
    private SSLContext sslc;

    private SSLEngine serverEngine;	// server Engine
    private ByteBuffer serverOut;	// write side of serverEngine
    private ByteBuffer serverIn;	// read side of serverEngine

    /*
     * For data transport, this example uses local ByteBuffers.  This
     * isn't really useful, but the purpose of this example is to show
     * SSLEngine concepts, not how to do network transport.
     */
    private ByteBuffer cTOs;		// "reliable" transport client->server
    private ByteBuffer sTOc;		// "reliable" transport server->client

    /*
     * The following is to set up the keystores.
     */
    private static String keyStoreFile = "testkeys";
    private static String trustStoreFile = "testkeys";
    private static String passwd = "passphrase";
	
    
    public XMPPSSLEngine() throws Exception
    {
    	KeyStore ks = KeyStore.getInstance("JKS");
    	KeyStore ts = KeyStore.getInstance("JKS");

    	char[] passphrase = "passphrase".toCharArray();

    	ks.load(new FileInputStream(keyStoreFile), passphrase);
    	ts.load(new FileInputStream(trustStoreFile), passphrase);

    	KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    	kmf.init(ks, passphrase);

    	TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    	tmf.init(ts);

    	SSLContext sslCtx = SSLContext.getInstance("TLS");

    	sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    	sslc = sslCtx;
    }
    
    
    public void execute() throws Exception {
    	boolean dataDone = false;

    	createSSLEngines();
    	createBuffers();

    	SSLEngineResult clientResult;	// results from client's last operation
    	SSLEngineResult serverResult;	// results from server's last operation

    	/*
    	 * Examining the SSLEngineResults could be much more involved,
    	 * and may alter the overall flow of the application.
    	 *
    	 * For example, if we received a BUFFER_OVERFLOW when trying
    	 * to write to the output pipe, we could reallocate a larger
    	 * pipe, but instead we wait for the peer to drain it.
    	 */
    	while (!isEngineClosed(serverEngine)) 
    	{
    	    serverResult = serverEngine.wrap(serverOut, sTOc);
    	    log("server wrap: ", serverResult);
    	    runDelegatedTasks(serverResult, serverEngine);

    	    cTOs.flip();
    	    sTOc.flip();

    	    log("----");

    	    serverResult = serverEngine.unwrap(cTOs, serverIn);
    	    log("server unwrap: ", serverResult);
    	    runDelegatedTasks(serverResult, serverEngine);

    	    cTOs.compact();
    	    sTOc.compact();

    	    /*
    	     * After we've transfered all application data between the client
    	     * and server, we close the clientEngine's outbound stream.
    	     * This generates a close_notify handshake message, which the
    	     * server engine receives and responds by closing itself.
    	     *
    	     * In normal operation, each SSLEngine should call
    	     * closeOutbound().  To protect against truncation attacks,
    	     * SSLEngine.closeInbound() should be called whenever it has
    	     * determined that no more input data will ever be
    	     * available (say a closed input stream).
    	     */
    	    if (!dataDone) {
    	    	// serverEngine.closeOutbound();
    	    	dataDone = true;
    	    }
    	}
        }

        /*
         * Using the SSLContext created during object creation,
         * create/configure the SSLEngines we'll use for this demo.
         */
        private void createSSLEngines() throws Exception {
	    	/*
	    	 * Configure the serverEngine to act as a server in the SSL/TLS
	    	 * handshake.  Also, require SSL client authentication.
	    	 */
	    	serverEngine = sslc.createSSLEngine();
	    	serverEngine.setUseClientMode(false);
	    	serverEngine.setNeedClientAuth(true);	
        }

        /*
         * Create and size the buffers appropriately.
         */
        private void createBuffers() {

    	/*
    	 * We'll assume the buffer sizes are the same
    	 * between client and server.
    	 */
    	SSLSession session = serverEngine.getSession();
    	int appBufferMax = session.getApplicationBufferSize();
    	int netBufferMax = session.getPacketBufferSize();

    	/*
    	 * We'll make the input buffers a bit bigger than the max needed
    	 * size, so that unwrap()s following a successful data transfer
    	 * won't generate BUFFER_OVERFLOWS.
    	 *
    	 * We'll use a mix of direct and indirect ByteBuffers for
    	 * tutorial purposes only.  In reality, only use direct
    	 * ByteBuffers when they give a clear performance enhancement.
    	 */
    	
    	serverIn = ByteBuffer.allocate(appBufferMax + 50);

    	cTOs = ByteBuffer.allocateDirect(netBufferMax);
    	sTOc = ByteBuffer.allocateDirect(netBufferMax);

    	
    	serverOut = ByteBuffer.wrap("Hello Client, I'm Server".getBytes());
        }

        /*
         * If the result indicates that we have outstanding tasks to do,
         * go ahead and run them in this thread.
         */
        private static void runDelegatedTasks(SSLEngineResult result,
    	    SSLEngine engine) throws Exception {

    	if (result.getHandshakeStatus() == HandshakeStatus.NEED_TASK) {
    	    Runnable runnable;
    	    while ((runnable = engine.getDelegatedTask()) != null) {
    		log("\trunning delegated task...");
    		runnable.run();
    	    }
    	    HandshakeStatus hsStatus = engine.getHandshakeStatus();
    	    if (hsStatus == HandshakeStatus.NEED_TASK) {
    		throw new Exception(
    		    "handshake shouldn't need additional tasks");
    	    }
    	    log("\tnew HandshakeStatus: " + hsStatus);
    	}
        }

        private static boolean isEngineClosed(SSLEngine engine) {
    	return (engine.isOutboundDone() && engine.isInboundDone());
        }

        /*
         * Simple check to make sure everything came across as expected.
         */
        private static void checkTransfer(ByteBuffer a, ByteBuffer b)
    	    throws Exception {
    	a.flip();
    	b.flip();

    	if (!a.equals(b)) {
    	    throw new Exception("Data didn't transfer cleanly");
    	} else {
    	    log("\tData transferred cleanly");
    	}

    	a.position(a.limit());
    	b.position(b.limit());
    	a.limit(a.capacity());
    	b.limit(b.capacity());
        }
        
        private static boolean resultOnce = true;

        private static void log(String str, SSLEngineResult result) {
    	
    	if (resultOnce) {
    	    resultOnce = false;
    	    System.out.println("The format of the SSLEngineResult is: \n" +
    		"\t\"getStatus() / getHandshakeStatus()\" +\n" +
    		"\t\"bytesConsumed() / bytesProduced()\"\n");
    	}
    	HandshakeStatus hsStatus = result.getHandshakeStatus();
    	log(str +
    	    result.getStatus() + "/" + hsStatus + ", " +
    	    result.bytesConsumed() + "/" + result.bytesProduced() +
    	    " bytes");
    	if (hsStatus == HandshakeStatus.FINISHED) {
    	    log("\t...ready for application data");
    	}
        }
        
        private static void log(String str) {        	
        	    System.out.println(str);        	
        }
}
