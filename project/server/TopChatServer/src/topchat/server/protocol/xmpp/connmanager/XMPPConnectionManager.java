package topchat.server.protocol.xmpp.connmanager;

import org.apache.log4j.Logger;
import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.protocol.xmpp.XMPPConstants;
import topchat.server.protocol.xmpp.context.InitialContext;
import topchat.server.protocol.xmpp.context.SecondaryContext;
import topchat.server.protocol.xmpp.context.XMPPContext;

/**
 * Manages a connection between the XMPP server and a client
 * @author ldragoi
 *
 */
public class XMPPConnectionManager extends DefaultConnectionManager 
			implements XMPPConstants 
{
	private static Logger logger = Logger.getLogger(XMPPConnectionManager.class);
	
	public XMPPConnectionManager()
	{
		context  = new InitialContext(); 
	}
	
	public void processWrite() 
	{
		logger.debug("Written.");
		
		context.processWrite();
		
		switchKeyContext( (XMPPContext) context);				
	}

	public void processRead(byte[] rd) 
	{
		String s = new String(rd);
		logger.debug("Received: " + s);

		context.processRead(rd);
		
		switchKeyContext((XMPPContext) context );
		
		rd = null;
		s = null;
	}	
	
	protected void switchKeyContext(XMPPContext old)
	{		
		XMPPContext nextContext = old;
		
		if (old instanceof XMPPContext)
		{
			nextContext = new InitialContext(old);
			logger.info("Switch to initial context");
		}
		if (old instanceof InitialContext)
		{
			nextContext = new SecondaryContext(old);
			logger.info("Switch to secondary context");
		}
									
		// enter next context
		context = nextContext;
		key.attach(nextContext);		
	}	
}
