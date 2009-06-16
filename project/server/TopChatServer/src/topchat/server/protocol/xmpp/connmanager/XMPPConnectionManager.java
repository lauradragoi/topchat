/**
    TopChatServer 
    Copyright (C) 2009 Laura Dragoi

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package topchat.server.protocol.xmpp.connmanager;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

import org.apache.log4j.Logger;
import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.protocol.xmpp.XMPPConstants;
import topchat.server.protocol.xmpp.context.TLSHandshakeContext;
import topchat.server.protocol.xmpp.context.WaitStartTLSContext;
import topchat.server.protocol.xmpp.context.WaitStreamStartContext;
import topchat.server.protocol.xmpp.context.SendFeaturesContext;
import topchat.server.protocol.xmpp.context.SendProceedTLS;
import topchat.server.protocol.xmpp.context.SendStreamStartContext;
import topchat.server.protocol.xmpp.context.XMPPContext;
import topchat.server.protocol.xmpp.stream.Features;
import topchat.server.protocol.xmpp.stream.XMPPStream;
import topchat.server.protocol.xmpp.tls.TLSEngineFactory;

/**
 * Manages a connection between the XMPP server and a client
 * @author ldragoi
 *
 */
public class XMPPConnectionManager extends DefaultConnectionManager 
			implements XMPPConstants 
{
	private static Logger logger = Logger.getLogger(XMPPConnectionManager.class);
	
	/** Describes the stream initiated by the client */
	private XMPPStream receivingStream = null;
	/** Describes the stream initiated by the server */
	private XMPPStream sendingStream = null;
	
	public XMPPConnectionManager()
	{
		context  = new WaitStreamStartContext(this); 		
	}
	
	@Override
	public synchronized void processWrite() 
	{
		context.processWrite();								
	}

	@Override
	public synchronized void processRead(byte[] rd) 
	{
		String s = new String(rd);

		context.processRead(rd);	
	}	
	
	
	@Override
	public void close()
	{
		key.cancel();
		logger.info("Client out");		
	}
	
	/**
	 * Method called by the current context to announce 
	 * it has finished his job.
	 */
	public void contextDone()
	{		
		switchKeyContext((XMPPContext) context);
	}
	
	/**
	 * Change the context based on the old context
	 * 
	 * @param old 
	 */
	protected synchronized void switchKeyContext(XMPPContext old)
	{						
		XMPPContext nextContext = old;
		
		if (old instanceof SendProceedTLS)
		{
			nextContext = new TLSHandshakeContext(this, old);
		}
		else if (old instanceof WaitStartTLSContext)
		{
			nextContext = new SendProceedTLS(this, old);
		}
		else if (old instanceof SendFeaturesContext)
		{	
			nextContext = new WaitStartTLSContext(this, old);					
		}
		else if (old instanceof SendStreamStartContext)
		{
			nextContext = new SendFeaturesContext(this, old);						
		} else if (old instanceof WaitStreamStartContext)
		{			
			nextContext = new SendStreamStartContext(this, old);						
		} else if (old instanceof XMPPContext)
		{				
			nextContext = new WaitStreamStartContext(this);
		}
									
		// enter next context
		context = nextContext;		
		
		logger.info("Context is now " + context.getClass().getSimpleName());
	}	
	
	/**
	 * Set the stream initiated by the client
	 * @param stream
	 */
	public void setReceivingStream(XMPPStream stream)
	{
		this.receivingStream = stream;
	}
	
	/**
	 * Set the stream that will be initiated by the server
	 * 
	 * @throws Exception
	 */
	public void setStartStream() throws Exception
	{
		if (receivingStream == null)
		{
			throw new Exception("Initiate receiving stream first");
		}
		
		sendingStream = new XMPPStream(null, "example.com", "someid", null, "1.0");
	}
	
	/**
	 * Obtain the stream initiated by the server
	 * If it was not set yet it will be set now.
	 * 
	 * @return
	 * @throws Exception
	 */	
	public XMPPStream getStartStream() throws Exception
	{
		if (sendingStream == null)
			setStartStream();
		
		return sendingStream;
	}
	
	
	public Features getFeatures() throws Exception
	{
		return new Features(true);
	}
	
	/**
	 * Secure the current connection by using TLS
	 */
	public void secureConnection()
	{
		if (tlsEngine == null)
		{
			TLSEngineFactory tlsEngineFactory = null;
			try 
			{
				tlsEngineFactory = new TLSEngineFactory();
				
			} catch (Exception e) 
			{
				logger.fatal("Unable to create TLS Engine factory");
				e.printStackTrace();
			}
			
			tlsEngine = tlsEngineFactory.getSSLEngine();
			
			try {
				tlsEngine.beginHandshake();
			} catch (SSLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			useTLS = true;
		}
	}
}
