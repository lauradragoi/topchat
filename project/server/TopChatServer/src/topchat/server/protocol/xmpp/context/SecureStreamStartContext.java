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

import org.apache.log4j.Logger;


import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.Parser;
import topchat.server.protocol.xmpp.stream.parser.Preparer;

/**
 * In this context the server waits for the client
 * to contact it and send in the start of the stream. 
 */
public class SecureStreamStartContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(SecureStreamStartContext.class);	
	
	public SecureStreamStartContext(XMPPConnectionManager mgr) {
		super(mgr);		
	}

	@Override
	public void processRead(byte[] rd) 
	{
		String s = new String(rd);
		logger.debug("wait secure stream start received: " + s);	
		
		processStartStream(s);
		
		//sendStreamStart();
		
		//setDone();
	}	
	
	private void processStartStream(String s)
	{
		// process start stream
		XMPPStream stream = null;
		
		try {
			stream = (XMPPStream) Parser.parse(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			logger.warn("Error in receiving stream start from client");	
		}

		getXMPPManager().setReceivingStream(stream);		
	}
	
	private void sendStreamStart()
	{
		// obtain the start of the stream from the manager
		XMPPStream stream = null;
		try {
			stream = getXMPPManager().getStartStream();
		} catch (Exception e) {			
			logger.warn("Could not obtain sending stream start");
			e.printStackTrace();			
		}
		
		// prepare the message to be written
		String msg = Preparer.prepareStreamStart(stream);
		
		getXMPPManager().send(msg.getBytes());
	}	
	
}