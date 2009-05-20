package topchat.server.protocol.xmpp.context;

import org.apache.log4j.Logger;

public class SecondaryContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(SecondaryContext.class);	
	
	public SecondaryContext(int bufferSize) {
		super(bufferSize);
		// TODO Auto-generated constructor stub
	}
	
	public void processRead(byte[] rd) {
		String s = new String(rd);
		logger.debug("Received in sec context: " + s);
				
		rd = null;
		s = null;
	}	

}
