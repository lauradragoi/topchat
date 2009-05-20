package topchat.server.protocol.xmpp.context;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultContext;

public class XMPPContext extends DefaultContext {

	private static Logger logger = Logger.getLogger(XMPPContext.class);	
	
	public XMPPContext(int bufferSize)
	{
		super(bufferSize);
	}
	
	public void processWrite()
	{
		logger.debug("Written");
	}
	
	public void processRead(byte[] rd) {
		String s = new String(rd);
		logger.debug("Received: " + s);
				
		rd = null;
		s = null;
	}	
}
