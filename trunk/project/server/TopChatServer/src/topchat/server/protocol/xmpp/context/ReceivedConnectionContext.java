package topchat.server.protocol.xmpp.context;

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.XMPPParser;
import topchat.server.util.Utils;

import org.apache.log4j.Logger;


import topchat.server.defaults.DefaultContext;

/**
 * Describes the context in which the server has been contacted by a client
 * and now initiates the stream that it will used for sending data to the client.
 */
public class ReceivedConnectionContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(ReceivedConnectionContext.class);	
			

	public ReceivedConnectionContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);
		
		// obtain the start of the stream from the manager
		XMPPStream stream = null;
		try {
			stream = getXMPPManager().getStartStream();
		} catch (Exception e) {			
			logger.warn("Could not obtain sending stream start");
			e.printStackTrace();			
		}
		
		// prepare the message to be written
		String msg = XMPPParser.prepareStreamStart(stream);
		
		// send it
		write(msg);			
		flush();
		
		logger.debug("Writing: " + msg);
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		
		logger.debug("Received: " + s);
						
		s = null;
	}	
}
