package topchat.server.protocol.xmpp.context;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultContext;

public class SecondaryContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(SecondaryContext.class);	
	

	
	/**
	 * @param old
	 */
	public SecondaryContext(DefaultContext old) {
		super(old);
	}



	public void processRead(byte[] rd) {
		String s = new String(rd);
		logger.debug("Received in sec context: " + s);
				
		rd = null;
		s = null;
	}	

}
