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

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.Preparer;

import org.apache.log4j.Logger;


import topchat.server.defaults.DefaultContext;

/**
 * In this context the server sends
 * the stream start to the client
 */
public class SendStreamStartContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(SendStreamStartContext.class);	
			

	public SendStreamStartContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);
		
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
		
		// send it
		write(msg);			
		flush();
		
		logger.debug("Writing: " + msg);
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		
		logger.debug("Received: " + s);
						
		s = null;
	}	
	
	@Override
	public void processWrite()
	{
		setDone();
	}
}
