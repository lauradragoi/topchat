package topchat.server.protocol.xmpp.context;

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.XMPPStream;
import topchat.server.util.Utils;

import org.apache.log4j.Logger;


import topchat.server.defaults.DefaultContext;

public class ReceivedConnectionContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(ReceivedConnectionContext.class);	
			

	public ReceivedConnectionContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);
		
		logger.debug("Writing: " + XMPPStream.initialStream());
		Utils.putStringToBuffer(XMPPStream.initialStream(), writeBuffer);				
		writeBuffer.flip();
		
		mgr.registerForWrite();
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		
		logger.debug("Received: " + s);
						
		s = null;
		
		//writeBuffer.clear();
		//Utils.putStringToBuffer("toi", writeBuffer);				
		//writeBuffer.flip();
		
		//mgr.registerForWrite();
	}	
}
