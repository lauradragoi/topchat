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
import topchat.server.protocol.xmpp.stream.XMPPAuth;
import topchat.server.protocol.xmpp.stream.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.Parser;
import topchat.server.protocol.xmpp.stream.parser.Preparer;

/**
* In this context the server waits for the client
* to contact it and send in the start of the stream. 
*/
public class SASLContext extends XMPPContext 
{
	private boolean authReceived = false;
	
	private static Logger logger = Logger.getLogger(SASLContext.class);
	

	public SASLContext(XMPPConnectionManager mgr) {
		super(mgr);		
	}

	@Override
	public void processRead(byte[] rd) 
	{
		String s = new String(rd);
		logger.debug("received: " + s);	

		if (!authReceived)
		{
			processAuth(s);
			authReceived = true;
			
			if ("PLAIN".equals(getXMPPManager().getAuth().mechanism))
			{
				sendSuccess();
				setDone();
			}
			else
			{
				logger.debug("still to implement other SASL mechanisms");
			}
				
		}
		else
		{
			logger.debug("still to implement other SASL mechanisms");
		}
		
		//setDone();
	}	

	
	private void sendSuccess()
	{
		// prepare the message to be written
		String msg = "<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>";
		
		getXMPPManager().send(msg.getBytes());	
	}
	
	private void processAuth(String s)
	{
		XMPPAuth auth = null;
		
		try {
			auth = (XMPPAuth) Parser.parse(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			logger.warn("Error in receiving stream start from client");	
		}

		getXMPPManager().setAuth(auth);		
	}
}