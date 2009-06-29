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
import org.jivesoftware.smack.util.Base64;

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.entities.User;
import topchat.server.protocol.xmpp.stream.element.StreamElement;
import topchat.server.protocol.xmpp.stream.element.XMPPAuth;
import topchat.server.protocol.xmpp.stream.parser.Parser;

/**
 * In this context the server waits for the client to contact it and send in the
 * start of the stream.
 */
public class SASLContext extends XMPPContext
{
	/** Flag set on true if the authentication info has been received */
	private boolean authReceived = false;

	private static Logger logger = Logger.getLogger(SASLContext.class);

	/**
	 * Constructs a SASLContext
	 * 
	 * @param mgr
	 *            the manager handling this context
	 */
	public SASLContext(XMPPConnectionManager mgr)
	{
		super(mgr);
	}

	@Override
	public void processRead(byte[] rd)
	{
		String s = new String(rd);
		logger.debug("Received: " + s);

		// if the authentication data was not received
		if (!authReceived)
		{
			try
			{
				getAuth(s);
				authReceived = true;

				XMPPAuth auth = getXMPPManager().getAuth();
				if ("PLAIN".equals(auth.mechanism))
				{
					User user = decodeUser(auth.initialChallenge);
					if (isValidUser(user))
					{
						sendSuccess();
						getXMPPManager().setUser(user);

						setDone();
					} else
					{
						sendFailure();
						setDone();
					}
				} else
				{
					logger.debug("still to implement other SASL mechanisms");
				}
			} catch (Exception e)
			{
				logger.debug("Unexpected content." + s);
			}
		} else
		{
			logger.debug("Received smth else");
		}
	}

	/**
	 * Check user authentication info
	 * 
	 * @param user
	 *            the User to be checked
	 * @return true if the authentication info is valid
	 */
	private boolean isValidUser(User user)
	{
		logger.debug("Authentication info is : " + user.username + " "
				+ user.something + " " + user.pass);
		return getXMPPManager().checkUser(user.username, user.pass);
	}

	/**
	 * Decode the user information sent by the client. (PLAIN authentication)
	 * Uses SMACK API decode function.
	 * 
	 * @param initialChallenge
	 *            the challenge received from the client
	 * @return the User information
	 */
	private User decodeUser(String initialChallenge)
	{
		byte[] token = new byte[0];
		if (initialChallenge.length() > 0)
		{
			// If auth request includes a value then validate it
			token = Base64.decode(initialChallenge.trim());
			if (token == null)
			{
				token = new byte[0];
			}
		}

		logger.debug("auth " + new String(token));

		String info = new String(token);

		byte[] regex = new byte[1];
		regex[0] = 0;
		String[] authInfo = info.split(new String(regex));

		String username = authInfo[0];
		String something = authInfo[1];
		String pass = authInfo[2];

		return new User(username, something, pass);
	}

	/**
	 * Send a success response to the client
	 */
	private void sendSuccess()
	{
		// prepare the message to be written
		String msg = "<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'/>";

		getXMPPManager().send(msg.getBytes());
	}

	/**
	 * Send failure response
	 */
	private void sendFailure()
	{
		// prepare the message to be written
		String msg = "<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>"
				+ "<not-authorized/>" + "</failure>";

		getXMPPManager().send(msg.getBytes());
	}

	/**
	 * Obtain authentication info from received data
	 * 
	 * @param s
	 *            the received data
	 * @throws Exception
	 *             if authentication info fails to be retrieved
	 */
	private void getAuth(String s) throws Exception
	{
		XMPPAuth auth = null;

		Vector<StreamElement> streamElements = null;

		try
		{
			streamElements = Parser.parse(s);
		} catch (Exception e)
		{
			logger.warn("Error in parsing " + e);
			return;
		}

		for (StreamElement element : streamElements)
		{
			if (element.isAuth())
			{
				auth = (XMPPAuth) element;
				logger.debug("Auth found " + auth);
			} else
			{
				logger.debug("Unexpected element received " + element);
			}
		}

		getXMPPManager().setAuth(auth);
	}
}