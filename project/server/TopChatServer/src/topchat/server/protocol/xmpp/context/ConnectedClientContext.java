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

import java.util.Vector;

import org.apache.log4j.Logger;

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.element.IQStanza;
import topchat.server.protocol.xmpp.stream.element.PresenceStanza;
import topchat.server.protocol.xmpp.stream.element.StreamElement;
import topchat.server.protocol.xmpp.stream.parser.Parser;

public class ConnectedClientContext extends XMPPContext 
{		
	private static Logger logger = Logger.getLogger(ConnectedClientContext.class);
	

	public ConnectedClientContext(XMPPConnectionManager mgr) {
		super(mgr);		
	}

	@Override
	public void processRead(byte[] rd) 
	{
		String s = new String(rd);
		logger.debug("received: " + s);	
		
		Vector<StreamElement> streamElements = null;
				
		try {		
			streamElements = Parser.parse(s);						
		} catch (Exception e) {		
			logger.warn("Error in parsing " + e);
			return ;
		}
		
		for (StreamElement element : streamElements)		
			processElement(element);
		

		
		//	setDone();
	}
		
	
	private void processElement(StreamElement streamElement)
	{
		if (streamElement.isIq())
		{
			IQStanza iqStanza = (IQStanza) streamElement;
			logger.debug("FOUND IQ");
		}
		else if (streamElement.isPresence())
		{
			PresenceStanza presenceStanza = (PresenceStanza) streamElement;
			logger.debug("FOUND PRESENCE");

		}
	}
}
