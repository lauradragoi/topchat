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

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.SSLEngine;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import org.apache.log4j.Logger;
import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.protocol.xmpp.XMPPConstants;
import topchat.server.protocol.xmpp.XMPPProtocol;
import topchat.server.protocol.xmpp.context.ExtraSecureStreamStartContext;
import topchat.server.protocol.xmpp.context.ResourceBindingContext;
import topchat.server.protocol.xmpp.context.SASLContext;
import topchat.server.protocol.xmpp.context.SecureStreamStartContext;
import topchat.server.protocol.xmpp.context.StartTLSContext;
import topchat.server.protocol.xmpp.context.StreamStartContext;
import topchat.server.protocol.xmpp.context.XMPPContext;
import topchat.server.protocol.xmpp.stream.Features;
import topchat.server.protocol.xmpp.stream.XMPPAuth;
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

	/** Used for securing the stream using TLS */
	protected SSLEngine tlsEngine = null;	
	
	private XMPPAuth auth = null;
	
	/** The protocol handling this connection manager */
	private XMPPProtocol protocol = null;
	
	/** The SocketChannel handled by this connection manager */
	private SocketChannel socketChannel = null;
	
	public XMPPConnectionManager(XMPPProtocol protocol, SocketChannel socketChannel)
	{
		this.protocol = protocol;
		this.socketChannel = socketChannel;
		
		// initiate context
		context  = new StreamStartContext(this); 		
	}
	
	@Override
	public synchronized void processRead(byte[] rd, int count) 
	{
		String s = new String(rd);
		
		logger.debug("received: " + s);
		
		context.processRead(rd);			
	}	
	
	public void send(byte[] data)
	{
		protocol.sendData(socketChannel, data);
	}
	
	/**
	 * Method called by the current context to announce 
	 * it has finished his job.
	 */
	public synchronized void contextDone()
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
		
		if (old instanceof ExtraSecureStreamStartContext)
		{
			nextContext = new ResourceBindingContext(this);
		} else if (old instanceof SASLContext)
		{
			nextContext = new ExtraSecureStreamStartContext(this);	
		} else if (old instanceof SecureStreamStartContext)
		{
			nextContext = new SASLContext(this);			
		} else if (old instanceof StartTLSContext)
		{
			nextContext = new SecureStreamStartContext(this);		
		} else if (old instanceof StreamStartContext)
		{			
			nextContext = new StartTLSContext(this);						
		} else if (old instanceof XMPPContext)
		{				
			nextContext = new StreamStartContext(this);
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
		logger.debug("set");
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
	
	public void setAuth(XMPPAuth auth)
	{
		/*
		SaslServer ss = null;
		Map<String, String> props = new TreeMap<String, String>();
        props.put(Sasl.QOP, "auth");

		try {
			logger.debug("auth mec "  + auth.mechanism);
			ss = Sasl.createSaslServer("PLAIN", 
				    "xmpp", "TopChatServer", props, null);
		} catch (SaslException e) {
			logger.debug("Exception on creating sasl server");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("sasl server up " + ss);
		
		*/
		
		this.auth = auth;
	}
	
	public XMPPAuth getAuth()
	{
		return auth;
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
	
	/**
	 * Obtain the features offered by the server
	 * @return
	 * @throws Exception
	 */
	public Features getFeatures() throws Exception
	{
		if (tlsEngine == null)
			return new Features(true);
		else if (auth == null)
			return new Features(false, true);
		else
			return new Features(false, false, true);
	}
	

	/**
	 * Obtain TLS engine used for securing the stream
	 * @return
	 */
	public SSLEngine getTLSEngine()
	{
		if (tlsEngine == null)
		{		
			try {
				tlsEngine = TLSEngineFactory.createTLSEngine();
			} catch (Exception e1) {
				logger.fatal("Exception on creating TLS engine" + e1);			
				return null;
			}
		}	
		
		return tlsEngine;
	}
	
	public void secure()
	{
		protocol.secure(socketChannel, getTLSEngine());
	}
	
	public void execute(Runnable r)
	{
		protocol.execute(r);
	}
}
