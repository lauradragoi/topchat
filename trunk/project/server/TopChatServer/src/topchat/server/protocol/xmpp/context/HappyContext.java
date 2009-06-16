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

import java.nio.ByteBuffer;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultContext;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.StreamElement;
import topchat.server.protocol.xmpp.stream.parser.Parser;

/**
 * In this context the server waits for the client
 * to contact it and send in the start of the stream. 
 */
public class HappyContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(HappyContext.class);	
	
	public HappyContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);		
	}


	@Override
	public void processRead(byte[] rd) {		
		String s = new String(rd);
		logger.debug("Happy receive: " + s);
		
		SSLEngine tlsEngine = mgr.getTLSEngine();
		
		logger.debug("Cipher : " + tlsEngine.getSession().getCipherSuite());
		
		HandshakeStatus hsStatus = tlsEngine.getHandshakeStatus();
		
		logger.debug("Handshake status " + hsStatus);
				
		// handshake was already done						
					
		int appSize = tlsEngine.getSession().getApplicationBufferSize();
		int netSize = tlsEngine.getSession().getPacketBufferSize();
		
		ByteBuffer dst = ByteBuffer.allocate(appSize);
		ByteBuffer buf = ByteBuffer.wrap(rd);
		
		logger.debug("Application buffer size " + appSize);
		logger.debug("Net buffer size " + netSize);
		
		logger.debug("unwrapping the buffer in the destination buffer");
		
		// first unwrap if using TLS
		SSLEngineResult result = null;
		try {
			result = tlsEngine.unwrap(buf, dst);
		} catch (SSLException e) {
			// TODO Auto-generated catch block
			logger.fatal("unwrap error");
			e.printStackTrace();
		}
		logger.debug("Unwrap result " + result.getStatus());
		
		// then process the data
		int count = dst.remaining();
		rd = new byte[count];

		dst.get(rd);
		dst.clear();
		
		s = new String(rd);
		logger.debug("Happy receive decoded: " + s);
		
	}	
	
}