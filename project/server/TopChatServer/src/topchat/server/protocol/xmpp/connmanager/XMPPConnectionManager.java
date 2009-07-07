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

import javax.net.ssl.SSLEngine;
import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.protocol.xmpp.XMPPConstants;
import topchat.server.protocol.xmpp.XMPPProtocol;
import topchat.server.protocol.xmpp.context.ConnectedClientContext;
import topchat.server.protocol.xmpp.context.SASLSecuredStreamStartContext;
import topchat.server.protocol.xmpp.context.ResourceBindingContext;
import topchat.server.protocol.xmpp.context.SASLContext;
import topchat.server.protocol.xmpp.context.TLSSecuredStreamStartContext;
import topchat.server.protocol.xmpp.context.SessionEstablishmentContext;
import topchat.server.protocol.xmpp.context.StartTLSContext;
import topchat.server.protocol.xmpp.context.StreamStartContext;
import topchat.server.protocol.xmpp.context.XMPPContext;
import topchat.server.protocol.xmpp.entities.Room;
import topchat.server.protocol.xmpp.entities.User;
import topchat.server.protocol.xmpp.stream.element.Features;
import topchat.server.protocol.xmpp.stream.element.MessageStanza;
import topchat.server.protocol.xmpp.stream.element.XMPPAuth;
import topchat.server.protocol.xmpp.stream.element.XMPPStream;
import topchat.server.protocol.xmpp.tls.TLSEngineFactory;

/**
 * Manages a connection between the XMPP server and a client
 */
public class XMPPConnectionManager extends DefaultConnectionManager
		implements
			XMPPConstants
{
	private static Logger logger = Logger
			.getLogger(XMPPConnectionManager.class);

	/** Describes the stream initiated by the client */
	private XMPPStream receivingStream = null;
	/** Describes the stream initiated by the server */
	private XMPPStream sendingStream = null;

	/** Used for securing the stream using TLS */
	protected SSLEngine tlsEngine = null;

	/** Authentication information */
	private XMPPAuth auth = null;

	/** The protocol handling this connection manager */
	private XMPPProtocol protocol = null;

	/** The SocketChannel handled by this connection manager */
	private SocketChannel socketChannel = null;

	/** User information */
	private User user = null;

	/**
	 * Constructs an XMPPConnectionManager
	 * 
	 * @param protocol
	 *            the protocol that is used
	 * @param socketChannel
	 *            the socket that will be managed
	 */
	public XMPPConnectionManager(XMPPProtocol protocol,
			SocketChannel socketChannel)
	{
		this.protocol = protocol;
		this.socketChannel = socketChannel;

		// initiate context
		context = new StreamStartContext(this);
	}

	@Override
	public synchronized void processRead(byte[] rd, int count)
	{
		String s = new String(rd);

		logger.debug("received: " + s);

		protocol.announceRead(s);

		context.processRead(rd);
	}

	/**
	 * Method called to send data
	 * 
	 * @param data
	 *            the data to be sent
	 */
	public void send(byte[] data)
	{
		protocol.sendData(socketChannel, data);
	}

	/**
	 * Method called by the current context to announce it has finished his job.
	 */
	public synchronized void contextDone()
	{
		switchKeyContext((XMPPContext) context);
	}

	/**
	 * Change the context based on the old context
	 * 
	 * @param old
	 *            the old context
	 */
	protected synchronized void switchKeyContext(XMPPContext old)
	{
		// My FSM
		XMPPContext nextContext = old;

		if (old instanceof SessionEstablishmentContext)
		{
			protocol.userConnected(this);

			nextContext = new ConnectedClientContext(this);
		} else if (old instanceof ResourceBindingContext)
		{
			nextContext = new SessionEstablishmentContext(this);
		} else if (old instanceof SASLSecuredStreamStartContext)
		{
			nextContext = new ResourceBindingContext(this);
		} else if (old instanceof SASLContext)
		{
			if (user == null)
			{
				logger.fatal("SASL connection failed");

				closeConnection();
			} else
			{
				nextContext = new SASLSecuredStreamStartContext(this);
			}
		} else if (old instanceof TLSSecuredStreamStartContext)
		{
			nextContext = new SASLContext(this);
		} else if (old instanceof StartTLSContext)
		{
			nextContext = new TLSSecuredStreamStartContext(this);
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
	 * Method called to send close connection message
	 */
	private void closeConnection()
	{
		String closeMsg = "</stream>";
		send(closeMsg.getBytes());
	}

	/**
	 * Set the stream initiated by the client
	 * 
	 * @param stream
	 *            the receiving stream
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
	 *             if the receiving stream was not initiated first
	 */
	public void setStartStream() throws Exception
	{
		if (receivingStream == null)
		{
			throw new Exception("Initiate receiving stream first");
		}

		sendingStream = new XMPPStream(null, "example.com", "someid", null,
				"1.0");
	}

	/**
	 * Set the authentication information
	 * 
	 * @param auth
	 *            the authentication information
	 */
	public void setAuth(XMPPAuth auth)
	{
		this.auth = auth;
	}

	/**
	 * Obtain the authentication information
	 * 
	 * @return the authentication info
	 */
	public XMPPAuth getAuth()
	{
		return auth;
	}

	/**
	 * Obtain the stream initiated by the server If it was not set yet it will
	 * be set now.
	 * 
	 * @return the XMPPStream corresponding to the sending stream
	 * @throws Exception
	 *             if it cannot be obtained
	 */
	public XMPPStream getStartStream() throws Exception
	{
		if (sendingStream == null)
			setStartStream();

		return sendingStream;
	}

	/**
	 * Obtain the features offered by the server
	 * 
	 * @return the Features
	 * @throws Exception
	 *             if the features cannot be obtained
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
	 * 
	 * @return the SSLEngine used for securing
	 */
	public SSLEngine getTLSEngine()
	{
		if (tlsEngine == null)
		{
			try
			{
				tlsEngine = TLSEngineFactory.createTLSEngine();
			} catch (Exception e1)
			{
				logger.fatal("Exception on creating TLS engine" + e1);
				return null;
			}
		}

		return tlsEngine;
	}

	/**
	 * Method called to secure the communication
	 */
	public void secure()
	{
		protocol.secure(socketChannel, getTLSEngine());
	}

	/**
	 * Executes a runnable task in a new thread
	 * 
	 * @param r
	 *            the runnable task
	 */
	public void execute(Runnable r)
	{
		protocol.execute(r);
	}

	/**
	 * Set the user corresponding to this connection
	 * 
	 * @param user
	 *            the User
	 */
	public void setUser(User user)
	{
		user.setManager(this);
		this.user = user;

	}

	/**
	 * Obtain the user corresponding to this connection
	 * 
	 * @return the User
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * Set the resource of the user.
	 * 
	 * @param resource
	 *            the resource string
	 */
	public void setUserResource(String resource)
	{
		if (resource == null)
			user.resource = user.username + "1";
		else
			user.resource = resource;
	}

	/**
	 * Called when the user corresponding to this connection adds a room
	 * 
	 * @param roomName
	 *            the name of the room
	 * @param roomUser
	 *            the nickname of the user in the room
	 * @return the Room being added
	 */
	public Room addRoom(String roomName, String roomUser)
	{
		logger.debug("Create room " + roomName + " user " + user);
		user.join(roomName);
		Room room = new Room(roomName, user, roomUser);
		protocol.roomAdded(room);
		return room;
	}

	/**
	 * Called when the user corresponding to this connection joins a room
	 * 
	 * @param roomName
	 *            the name of the room
	 * @param roomUser
	 *            the nickname of the user in the room
	 * @return the Room being joined
	 */
	public Room joinRoom(String roomName, String roomUser)
	{
		logger.debug("Join room " + roomName + " user " + user);
		return protocol.roomJoined(roomName, user, roomUser);
	}

	/**
	 * Called when the user corresponding to this connection leaves a room
	 * 
	 * @param roomName
	 *            the name of the room
	 * @param roomUser
	 *            the nickname of the user in the room
	 * @return the Room being joined
	 */
	public void leaveRoom(String roomName)
	{
		logger.debug("Leave room " + roomName + " user " + user);
		user.leave(roomName);
		protocol.roomLeft(roomName, user);
	}	
	
	/**
	 * Checks if a room is created.
	 * 
	 * @param roomName
	 *            the name of the room
	 * @return true if the room is created, false otherwise
	 */
	public boolean isRoomCreated(String roomName)
	{
		return protocol.isRoomCreated(roomName);
	}

	/**
	 * Called when the user corresponding to this connection sends a groupchat
	 * message
	 * 
	 * @param message
	 *            the MessageStanza sent by the user
	 */
	public void sendGroupchat(MessageStanza message)
	{
		protocol.sendGroupChat(user, message);
	}

	/**
	 * Called to announce that the connection handled by this manager is
	 * closing.
	 */
	public void announceClosed()
	{
		logger.debug("Connection manager is closed from now on.");
	}

	/**
	 * Check the credentials of a user.
	 * 
	 * @param username
	 *            the name of the user
	 * @param pass
	 *            the password of the user
	 * @return true if the user has valid credentials, false otherwise
	 */
	public boolean checkUser(String username, String pass)
	{

		return protocol.checkUser(username, pass);
	}

}
