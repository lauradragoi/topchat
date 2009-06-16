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

import java.nio.channels.SelectionKey;

import javax.net.ssl.SSLEngine;

import org.apache.log4j.Logger;


public class TLSHandler {

	private static Logger logger = Logger.getLogger(TLSHandler.class);
	
	private SSLEngine tlsEngine;
    
  
	public TLSHandler()
	{				
		TLSEngineFactory tlsEngineFactory = null;
		try {
			tlsEngineFactory = new TLSEngineFactory();
		} catch (Exception e) {
			logger.fatal("Unable to create TLS Engine factory");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tlsEngine = tlsEngineFactory.getSSLEngine();	
		
		logger.debug("TLSEngine created: " + tlsEngine.getPeerHost());
	}
	
}
