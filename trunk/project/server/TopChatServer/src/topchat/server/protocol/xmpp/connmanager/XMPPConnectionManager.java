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
package topchat.server.protocol.xmpp.connmanager;

import org.apache.log4j.Logger;
import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.protocol.xmpp.XMPPConstants;
import topchat.server.protocol.xmpp.context.AwaitingConnectionContext;
import topchat.server.protocol.xmpp.context.ReceivedConnectionContext;
import topchat.server.protocol.xmpp.context.XMPPContext;
import topchat.server.protocol.xmpp.stream.XMPPStream;

/**
 * Manages a connection between the XMPP server and a client
 * @author ldragoi
 *
 */
public class XMPPConnectionManager extends DefaultConnectionManager 
			implements XMPPConstants 
{
	private static Logger logger = Logger.getLogger(XMPPConnectionManager.class);
	
	private XMPPStream receivingStream = null;
	private XMPPStream sendingStream = null;
	
	public XMPPConnectionManager()
	{
		context  = new AwaitingConnectionContext(this); 
	}
	
	@Override
	public void processWrite() 
	{
		//logger.debug("Written.");
		
		context.processWrite();
		
		switchKeyContext( (XMPPContext) context);				
	}

	@Override
	public void processRead(byte[] rd) 
	{
		String s = new String(rd);
		logger.debug("Received: " + s);

		context.processRead(rd);
		
		switchKeyContext((XMPPContext) context );
	}	
	
	/**
	 * Changed the context based on the old context
	 * @param old the old context
	 */
	protected void switchKeyContext(XMPPContext old)
	{		
		XMPPContext nextContext = old;
		
		if (old instanceof ReceivedConnectionContext)
		{
			logger.info("Remain in secondary context");
			return;			
		} else if (old instanceof AwaitingConnectionContext)
		{
			nextContext = new ReceivedConnectionContext(this, old);
			logger.info("Switch to secondary context");
		} else if (old instanceof XMPPContext)
		{
			nextContext = new AwaitingConnectionContext(old);
			logger.info("Switch to initial context");
		}

									
		// enter next context
		context = nextContext;		
	}	
	
	public void setReceivingStream(XMPPStream stream)
	{
		this.receivingStream = stream;
	}
	
	public void setStartStream() throws Exception
	{
		if (receivingStream == null)
		{
			throw new Exception("Initiate receiving stream first");
		}
		
		sendingStream = new XMPPStream(null, "example.com", "someid", null, "1.0");
	}
	
	public XMPPStream getStartStream() throws Exception
	{
		if (sendingStream == null)
			setStartStream();
		
		return sendingStream;
	}
	
	
}
