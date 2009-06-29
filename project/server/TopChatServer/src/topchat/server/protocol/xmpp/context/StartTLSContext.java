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
import topchat.server.protocol.xmpp.stream.parser.Parser;
import topchat.server.protocol.xmpp.stream.parser.Preparer;

/**
 * In this context the server waits for the client to contact it and send in the
 * start of the stream.
 */
public class StartTLSContext extends XMPPContext
{

	private static Logger logger = Logger.getLogger(StartTLSContext.class);

	/**
	 * Constructs a StartTLSContext
	 * 
	 * @param mgr
	 *            the manager handling this context
	 */
	public StartTLSContext(XMPPConnectionManager mgr)
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
			Vector<StreamElement> streamElements = Parser.parse(s);

			for (StreamElement element : streamElements)
			{
				// receive starttls
				if (element.isStartTLS())
				{
					logger.debug("sec now");
					// secure the connection
					getXMPPManager().secure();
					logger.debug("sec now done");

					// send proceed
					sendProceedTLS();

					setDone();
				}
			}

		} catch (Exception e)
		{
			logger.fatal("StartTLS exception " + e);
		}
	}

	/**
	 * Sends the proceed message
	 */
	private void sendProceedTLS()
	{
		// prepare the message to be written
		String msg = Preparer.prepareProceed();

		getXMPPManager().send(msg.getBytes());
	}
}
