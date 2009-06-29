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
package topchat.server.protocol.xmpp.context;

import java.util.Vector;

import org.apache.log4j.Logger;

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.element.StreamElement;
import topchat.server.protocol.xmpp.stream.element.IQStanza;
import topchat.server.protocol.xmpp.stream.parser.Parser;

/**
 * The context in which the resource name used by a user is negotiated with the
 * server
 */
public class ResourceBindingContext extends XMPPContext
{

	private static Logger logger = Logger
			.getLogger(ResourceBindingContext.class);

	/**
	 * The iq stanza received by the server containing the user request for
	 * resource binding
	 */
	IQStanza iqStanza = null;

	/**
	 * Constructs a ResourceBindingContext
	 * 
	 * @param mgr
	 *            the manager handling this context
	 */
	public ResourceBindingContext(XMPPConnectionManager mgr)
	{
		super(mgr);
	}

	@Override
	public void processRead(byte[] rd)
	{
		String s = new String(rd);
		logger.debug("received: " + s);

		// expecting IQ stanza
		if (iqStanza == null)
		{
			try
			{
				iqStanza = getIqBind(s);

				if (iqStanza == null)
					throw new Exception("Expected IQ Stanza not received.");

				getXMPPManager().setUserResource(iqStanza.getData("resource"));

				// to do
				sendIqBind(iqStanza);

				setDone();
			} catch (Exception e)
			{
				logger.debug("Unexpected content." + s);
			}
		}

	}

	/**
	 * Obtain the received request for resource binding
	 * 
	 * @param s
	 *            the received request
	 * @return the IQStanza corresponding to the received request
	 * @throws Exception
	 *             if the IQStanza cannot be retrieved
	 */
	private IQStanza getIqBind(String s) throws Exception
	{
		// process start stream
		IQStanza iqStanza = null;
		Vector<StreamElement> streamElements = null;

		try
		{
			streamElements = Parser.parse(s);
		} catch (Exception e)
		{
			logger.warn("Error in receiving stream start from client" + e);
			throw new Exception("Expected IQ stanza not received.");
		}

		for (StreamElement element : streamElements)
		{
			if (element.isIq())
			{
				iqStanza = (IQStanza) element;
				logger.debug("IQStanza found " + iqStanza);
			} else
			{
				logger.debug("Unexpected element received " + element);
			}
		}

		return iqStanza;

	}

	/**
	 * Send the resource binding response to the client
	 * 
	 * @param iqStanza
	 *            holds the IQStanza corresponding to the response
	 */
	private void sendIqBind(IQStanza iqStanza)
	{
		String msg = "<iq type='result' id='" + iqStanza.getAttribute("id")
				+ "'>" + "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'>"
				+ "<jid>" + getXMPPManager().getUser().username + "@"
				+ getXMPPManager().getUser().serverDomain + "/"
				+ getXMPPManager().getUser().resource + "</jid>" + "</bind>"
				+ "</iq>";

		getXMPPManager().send(msg.getBytes());
	}

}