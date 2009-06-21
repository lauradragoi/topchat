package topchat.server.protocol.xmpp.context;

import java.nio.ByteBuffer;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.defaults.DefaultContext;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;

/** Base class for all XMPP contexts */
public class XMPPContext extends DefaultContext {

	private static Logger logger = Logger.getLogger(XMPPContext.class);	
	
	public XMPPContext(DefaultConnectionManager mgr) {
		super(mgr);	
	}
	
	/*
	public XMPPContext() {
			super();
	}
	 

	public XMPPContext(DefaultContext old) {
		super(old);
	}
	
	public XMPPContext(DefaultConnectionManager mgr) {
		super(mgr);	
	}
	
	public XMPPContext(DefaultConnectionManager mgr, DefaultContext old) {
		super(mgr, old);	
	}
	*/

	public void processWrite()
	{
		//logger.debug("Written");
	}
	
	public void processRead(byte[] rd) {
		String s = new String(rd);
		logger.debug("Received: " + s);
				
		rd = null;
		s = null;
	}	
	
	public XMPPConnectionManager getXMPPManager()
	{
		return (XMPPConnectionManager) mgr;
	}
	

	public void setDone()
	{
		getXMPPManager().contextDone();
	}
	

	
	protected byte[] decodeData(byte[] data)
	{
		//String s = new String(data);
		//logger.debug("Happy receive: " + s);
		
		ByteBuffer src = ByteBuffer.wrap(data);
		
		SSLEngine tlsEngine = getXMPPManager().getTLSEngine();
						
		int appSize = tlsEngine.getSession().getApplicationBufferSize();
		ByteBuffer dst = ByteBuffer.allocate(appSize);
		
		// unwrap
		SSLEngineResult result = null;
		try {
			result = tlsEngine.unwrap(src, dst);
		} catch (SSLException e) {
			logger.fatal("unwrap error");
			e.printStackTrace();
		}
		logger.debug("Unwrap result " + result);
		
		// drain
		dst.flip();
		
		int count = dst.remaining();
		byte[] dataResult = new byte[count];

		dst.get(dataResult);
		
		return dataResult;
	}
	
	
}
