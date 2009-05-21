package topchat.server.protocol.xmpp.context;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultContext;

public class InitialContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(InitialContext.class);	
	
	public InitialContext() {
		super();
	}

	public InitialContext(DefaultContext old) {
		super(old);
	}

	public void processRead(byte[] rd) {
		String s = new String(rd);
		logger.debug("Received in initial context: " + s);
				
		rd = null;
		s = null;
	}	

}
