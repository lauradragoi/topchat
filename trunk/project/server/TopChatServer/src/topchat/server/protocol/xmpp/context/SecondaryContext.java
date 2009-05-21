package topchat.server.protocol.xmpp.context;

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.util.Utils;

import org.apache.log4j.Logger;


import topchat.server.defaults.DefaultContext;

public class SecondaryContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(SecondaryContext.class);	
			
	/**
	 * @param old
	 */
	public SecondaryContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);
		
		writeBuffer.clear();
		Utils.putStringToBuffer("toi", writeBuffer);				
		writeBuffer.flip();
		
		mgr.registerForWrite();
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		
		logger.debug("Received in sec context: " + s);
						
		s = null;
		
		writeBuffer.clear();
		Utils.putStringToBuffer("toi", writeBuffer);				
		writeBuffer.flip();
		
		mgr.registerForWrite();
	}	
}
