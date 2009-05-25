package topchat.server.protocol.xmpp.context;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.defaults.DefaultContext;

public class XMPPContext extends DefaultContext {

	private static Logger logger = Logger.getLogger(XMPPContext.class);	
	
	
	public XMPPContext() {
			super();
	}
	 

	public XMPPContext(DefaultContext old) {
		super(old);
	}
	
	public XMPPContext(DefaultConnectionManager mgr, DefaultContext old) {
		super(mgr, old);	
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
