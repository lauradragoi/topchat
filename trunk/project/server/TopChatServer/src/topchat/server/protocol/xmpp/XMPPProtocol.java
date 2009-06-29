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
package topchat.server.protocol.xmpp;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLEngine;

import org.apache.log4j.Logger;

import topchat.server.interfaces.Net;
import topchat.server.interfaces.Protocol;
import topchat.server.interfaces.ProtocolMediator;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.entities.Room;
import topchat.server.protocol.xmpp.entities.RoomParticipant;
import topchat.server.protocol.xmpp.entities.User;
import topchat.server.protocol.xmpp.stream.element.MessageStanza;

/**
 * Implementation of the XMPP protocol
 */
public class XMPPProtocol implements Protocol, XMPPConstants
{
	/** The mediator */
	private ProtocolMediator med = null;
	/** The network module */
	private Net net = null;

	/** Pool of threads used to process data received from network */
	private static ExecutorService pool = Executors
			.newFixedThreadPool(DEFAULT_EXECUTOR_THREADS);

	/** Map of SocketChannels and connection managers */
	private static ConcurrentHashMap<SocketChannel, XMPPConnectionManager> connectionManagers = new ConcurrentHashMap<SocketChannel, XMPPConnectionManager>();

	/** Map of the rooms created on the server */
	private static ConcurrentHashMap<String, Room> createdRooms = new ConcurrentHashMap<String, Room>();

	private static Logger logger = Logger.getLogger(XMPPProtocol.class);

	/**
	 * Constructs the XMPPProtocol
	 * 
	 * @param med
	 *            the mediator to which the protocol connects
	 */
	public XMPPProtocol(ProtocolMediator med)
	{
		setMediator(med);

		this.med.setProtocol(this);

		logger.info("XMPPProtocol initiated");
	}

	@Override
	public void setMediator(ProtocolMediator med)
	{
		this.med = med;
	}

	@Override
	public int getListeningPort()
	{
		return XMPP_DEFAULT_SERVER_PORT;
	}

	@Override
	public void start(Net net)
	{
		this.net = net;
		this.net.setProtocol(this);

		logger.info("Protocol started");

		this.net.start(getListeningPort());
	}

	@Override
	/*
	 * * Obtain the connection manager for a specified SocketChannel. If no such
	 * manager exists it is created now and added to the map maintained by the
	 * protocol.
	 */
	public XMPPConnectionManager getConnectionManager(
			SocketChannel socketChannel)
	{
		XMPPConnectionManager connManager = connectionManagers
				.get(socketChannel);

		if (connManager == null)
		{
			connManager = new XMPPConnectionManager(this, socketChannel);
			connectionManagers.put(socketChannel, connManager);
		}

		return connManager;
	}

	@Override
	/*
	 * * Schedules for the received data to be processed on a thread from the
	 * executor pool
	 */
	public void processData(SocketChannel socketChannel, byte[] data, int count)
	{
		byte[] dataCopy = new byte[count];
		System.arraycopy(data, 0, dataCopy, 0, count);
		pool.execute(new ProcessData(this, socketChannel, dataCopy, count));
	}

	/**
	 * Called to require that the socket is secured with TLS
	 * 
	 * @param socketChannel
	 *            the socket supposed to be secured
	 * @param sslEngine
	 *            the SSLEngine used for securing the socket
	 */
	public void secure(SocketChannel socketChannel, SSLEngine sslEngine)
	{
		// send the request to the network module
		net.secure(socketChannel, sslEngine);
	}

	/**
	 * Schedules the specified data to be sent on that SocketChannel
	 * 
	 * @param socketChannel
	 *            the socket on which the data should be sent
	 * @param data
	 *            the data which should be sent
	 */
	public void sendData(SocketChannel socketChannel, byte[] data)
	{
		med.announceSend(new String(data));
		net.send(socketChannel, data);
	}

	@Override
	public void execute(Runnable r)
	{
		pool.execute(r);
	}

	/**
	 * Announce that the user using the connection handled by manager has
	 * finished connecting.
	 * 
	 * @param manager
	 *            the connection manager
	 */
	public void userConnected(XMPPConnectionManager manager)
	{
		// announce the mediator
		med.addUser(manager.getUser().toString());
	}

	/**
	 * Announce that a room was added
	 * 
	 * @param room
	 *            the name of the added room
	 */
	public void roomAdded(Room room)
	{
		logger.debug("Room added " + room);
		createdRooms.put(room.getName(), room);
		med.addRoom(room.getName());
	}

	/**
	 * Announces that a user has joined a room
	 * 
	 * @param roomName
	 *            the name of the room
	 * @param user
	 *            the User entity
	 * @param roomUser
	 *            the nickname used by the user in the room
	 * @return a reference to the Room joined by the user
	 */
	public Room roomJoined(String roomName, User user, String roomUser)
	{
		logger.debug("Room joined " + roomName);
		Room room = createdRooms.get(roomName);

		if (room == null)
		{
			logger.debug("Cannot join a non existing room.");
			return null;
		}

		user.join(roomName);

		RoomParticipant newParticipant = new RoomParticipant(user, roomUser);
		room.addParticipant(newParticipant);

		// announce other participants that the new participant has entered the
		// room
		for (RoomParticipant participant : room.getParticipants())
		{

			String presenceMsg = "<presence " + "id='a1'" + "to='"
					+ participant.getUser().toString() + "'" + "from='"
					+ roomName + "/" + roomUser + "'>" + "<status>"
					+ participant.getUser().status + "</status>"
					+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
					+ "<item affiliation='" + newParticipant.getAffiliation()
					+ "' " + "role='" + newParticipant.getRole()
					+ "'><reason></reason><actor jid=''/></item>"
					+ "</x></presence>";

			participant.getUser().manager.send(presenceMsg.getBytes());
		}

		return room;
	}

	/**
	 * Check if a room was created
	 * 
	 * @param roomName
	 *            the name of the room
	 * @return true if the room is created, false otherwise
	 */
	public boolean isRoomCreated(String roomName)
	{
		return (createdRooms.get(roomName) != null);
	}

	/**
	 * Send a groupchat message sent by a user to the members of that room.
	 * 
	 * @param user
	 *            the User sending the message
	 * @param message
	 *            the MessageStanza sent by the user
	 */
	public void sendGroupChat(User user, MessageStanza message)
	{
		// find the room
		String roomName = message.getAttribute("to");
		logger.debug("Roomname " + roomName);

		Room room = createdRooms.get(roomName);

		logger.debug("room is " + room);

		if (room != null)
		{
			RoomParticipant sender = room.getParticipant(user);

			logger.debug("sender is " + sender);

			if (sender == null)
			{
				logger.debug("Sender invalid.");
				return;
			}

			logger.debug("Sending group message to " + roomName);

			for (RoomParticipant participant : room.getParticipants())
			{
				if (participant == null)
					continue;

				String body = message.getData("body");
				if (body == null)
					body = "";

				String msg = "<message id='" + message.getAttribute("id")
						+ "' to='" + participant.getUser().toString() + "'"
						+ " from='" + roomName + "/" + sender.getRoomUser()
						+ "'" + " type='groupchat'" + "><body>" + body
						+ "</body></message>";

				participant.getUser().manager.send(msg.getBytes());

			}
		} else
		{
			logger.debug("Cannot send group message to non exiting group "
					+ roomName);
		}
	}

	@Override
	public void connectionClosed(SocketChannel socketChannel)
	{

		XMPPConnectionManager manager = connectionManagers.get(socketChannel);

		// user is leaving
		User user = manager.getUser();
		if (user != null)
		{

			med.removeUser(user.toString());

			for (String roomName : user.joinedRooms)
			{
				Room room = createdRooms.get(roomName);

				if (room != null)
				{
					RoomParticipant participant = room.removeUser(user);
					logger.debug("User " + user + " removed from " + roomName);

					if (room.getParticipants().size() == 0)
					{
						// remove the room
						createdRooms.remove(roomName);
						logger
								.debug("Room "
										+ roomName
										+ " was removed due to the fact that it was empty.");

						med.removeRoom(roomName);
					} else
					{
						userLeftRoom(room, participant);
					}
				}
			}

		}

		manager.announceClosed();

		logger.debug("Manager was removed for user " + user);
		connectionManagers.remove(socketChannel);
	}

	/**
	 * Method called when a user leaves a room in order to announce the other
	 * users.
	 * 
	 * @param room
	 *            the room left by the user
	 * @param leavingParticipant
	 *            the RoomParticipant corresponding to the leaving user
	 */
	public void userLeftRoom(Room room, RoomParticipant leavingParticipant)
	{
		if (leavingParticipant == null)
		{
			logger.warn("How come we are announcing the other participants in "
					+ room.getName() + " that there is no one leaving??");
			return;
		}

		logger.debug("announcing participants in " + room.getName() + " that "
				+ leavingParticipant.getRoomUser() + " is leaving");

		// announce other participants that the new participant has left the
		// room
		for (RoomParticipant participant : room.getParticipants())
		{

			String presenceMsg = "<presence " + "id='a1'" + "to='"
					+ participant.getUser().toString() + "'" + "from='"
					+ room.getName() + "/" + leavingParticipant.getRoomUser()
					+ "'" + "type='unavailable'>"
					+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
					+ "<item affiliation='"
					+ leavingParticipant.getAffiliation() + "' " + "role='"
					+ leavingParticipant.getRole()
					+ "'><reason></reason><actor jid=''/></item>"
					+ "</x></presence>";

			participant.getUser().manager.send(presenceMsg.getBytes());
		}
	}

	/**
	 * Checks the credentials of a user trying to log in to the server
	 * 
	 * @param username
	 *            the name of the user
	 * @param pass
	 *            the password of the user
	 * @return true if the user has valid credentials, false otherwise
	 */
	public boolean checkUser(String username, String pass)
	{

		return med.checkUser(username, pass);
	}

	/**
	 * Called to announce data new data was received
	 * 
	 * @param s
	 *            the received data
	 */
	public void announceRead(String s)
	{
		med.announceRead(s);
	}
}
