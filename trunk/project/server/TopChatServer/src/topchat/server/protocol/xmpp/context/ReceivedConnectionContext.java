package topchat.server.protocol.xmpp.context;

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.XMPPParser;
import topchat.server.util.Utils;

import org.apache.log4j.Logger;


import topchat.server.defaults.DefaultContext;

public class ReceivedConnectionContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(ReceivedConnectionContext.class);	
			

	public ReceivedConnectionContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);
		
	
		XMPPStream stream = null;
		try {
			stream = getXMPPManager().getStartStream();
		} catch (Exception e) {			
			logger.warn("Could not obtain sending stream start");
			e.printStackTrace();			
		}
		
		String msg = XMPPParser.prepareStreamStart(stream);
		
		logger.debug("Writing: " + msg);
		
		write(msg);			
		flush();
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		
		logger.debug("Received: " + s);
						
		s = null;
		
	}	
}
