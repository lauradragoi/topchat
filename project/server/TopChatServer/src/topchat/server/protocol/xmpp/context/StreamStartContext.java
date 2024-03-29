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
import topchat.server.protocol.xmpp.stream.element.Features;
import topchat.server.protocol.xmpp.stream.element.StreamElement;
import topchat.server.protocol.xmpp.stream.element.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.Parser;
import topchat.server.protocol.xmpp.stream.parser.Preparer;

/**
 * In this context the server waits for the client to contact it and send in the
 * start of the stream.
 */
public class StreamStartContext extends XMPPContext
{

	private static Logger logger = Logger.getLogger(StreamStartContext.class);

	/**
	 * Constructs a StreamStartContext
	 * 
	 * @param mgr
	 *            the manager handling this context
	 */
	public StreamStartContext(XMPPConnectionManager mgr)
	{
		super(mgr);
	}

	@Override
	public void processRead(byte[] rd)
	{
		String s = new String(rd);
		logger.debug("Received: " + s);

		try
		{
			processStartStream(s);

			sendStreamStart();

			sendFeatures();

			setDone();
		} catch (Exception e)
		{
			logger.warn("Error in processing stream start." + e);
		}
	}

	/**
	 * Process the received start of stream
	 * 
	 * @param s
	 *            the received data
	 */
	private void processStartStream(String s) throws Exception
	{
		// process start stream

		Vector<StreamElement> streamElements = Parser.parse(s);

		for (StreamElement element : streamElements)
		{
			if (element.isXMPPStream())
			{
				XMPPStream stream = (XMPPStream) element;
				getXMPPManager().setReceivingStream(stream);
			} else
				throw new Exception("Element is not an XMPPStream. " + s);
		}
	}

	/**
	 * Send the stream start response
	 */
	private void sendStreamStart()
	{
		// obtain the start of the stream from the manager
		XMPPStream stream = null;
		try
		{
			stream = getXMPPManager().getStartStream();
		} catch (Exception e)
		{
			logger.warn("Could not obtain sending stream start " + e);
		}

		// prepare the message to be written
		String msg = Preparer.prepareStreamStart(stream);

		getXMPPManager().send(msg.getBytes());
	}

	/**
	 * Advertise the features currently offered by the server
	 */
	private void sendFeatures()
	{
		Features ft = null;
		try
		{
			ft = getXMPPManager().getFeatures();
		} catch (Exception e)
		{
			logger.warn("Could not obtain features info " + e);
		}

		// prepare the message to be written
		String msg = Preparer.prepareFeatures(ft);

		getXMPPManager().send(msg.getBytes());
	}
}
