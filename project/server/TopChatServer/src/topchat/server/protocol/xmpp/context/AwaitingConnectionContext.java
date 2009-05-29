package topchat.server.protocol.xmpp.context;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.defaults.DefaultContext;
import topchat.server.protocol.xmpp.stream.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.XMPPParser;

/**
 * Describes the context in which the server is waiting for a client to contact it
 */
public class AwaitingConnectionContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(AwaitingConnectionContext.class);	
	
	public AwaitingConnectionContext(DefaultConnectionManager mgr) {
		super(mgr);
	}

	public AwaitingConnectionContext(DefaultContext old) {
		super(old);
	}

	@Override
	public void processRead(byte[] rd) {		
		String s = new String(rd);
		logger.debug("Received: " + s);
		
		// process start stream
		XMPPStream stream = XMPPParser.parseStreamStart(s);

		getXMPPManager().setReceivingStream(stream);
	}	

}
