package topchat.server.protocol.xmpp.context;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.defaults.DefaultContext;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;

/** Base class for all XMPP contexts */
public class XMPPContext extends DefaultContext
{

	private static Logger logger = Logger.getLogger(XMPPContext.class);

	/**
	 * Construct a new XMPPContext
	 * 
	 * @param mgr
	 *            the connection manager for that context
	 */
	public XMPPContext(DefaultConnectionManager mgr)
	{
		super(mgr);
	}

	/**
	 * Process received data
	 * 
	 * @param rd
	 *            the received data
	 */
	public void processRead(byte[] rd)
	{
		String s = new String(rd);
		logger.debug("Received: " + s);
	}

	/**
	 * Obtain the connection manager for this context
	 * 
	 * @return the connection manager
	 */
	public XMPPConnectionManager getXMPPManager()
	{
		return (XMPPConnectionManager) mgr;
	}

	/**
	 * Announce the connection manager that the current context has finished its
	 * operations.
	 * 
	 * This should be called from processRead to prevent receiving data that
	 * should not be processed by this context before switching to the next
	 * context.
	 */
	public void setDone()
	{
		getXMPPManager().contextDone();
	}
}
