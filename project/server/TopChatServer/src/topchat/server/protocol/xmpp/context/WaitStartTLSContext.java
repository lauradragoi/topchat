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

import topchat.server.defaults.DefaultContext;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.StreamElement;
import topchat.server.protocol.xmpp.stream.parser.Parser;

/**
 * In this context the server waits for the client
 * to contact it and send in the start of the stream. 
 */
public class WaitStartTLSContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(WaitStartTLSContext.class);	
	
	public WaitStartTLSContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);		
	}


	@Override
	public void processRead(byte[] rd) {		
		String s = new String(rd);
		logger.debug("Received: " + s);
		
		try {
			StreamElement result = (StreamElement)Parser.parse(s);
			
			if (result.isStartTLS())
				setDone();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
}
