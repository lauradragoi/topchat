package topchat.server.protocol.xmpp.context;

import org.apache.log4j.Logger;

public class InitialContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(InitialContext.class);	
	
	public InitialContext(int bufferSize) {
		super(bufferSize);
		// TODO Auto-generated constructor stub
	}
	
	public void processRead(byte[] rd) {
		String s = new String(rd);
		logger.debug("Received in initial context: " + s);
				
		rd = null;
		s = null;
	}	

}
