package topchat.server.protocol.xmpp.context;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultContext;

public class AwaitingConnectionContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(AwaitingConnectionContext.class);	
	
	public AwaitingConnectionContext() {
		super();
	}

	public AwaitingConnectionContext(DefaultContext old) {
		super(old);
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		logger.debug("Received: " + s);
				
		rd = null;
		s = null;
	}	

}
