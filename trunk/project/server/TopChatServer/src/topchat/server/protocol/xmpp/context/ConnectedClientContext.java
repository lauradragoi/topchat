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
import topchat.server.protocol.xmpp.entities.Room;
import topchat.server.protocol.xmpp.entities.RoomParticipant;
import topchat.server.protocol.xmpp.stream.element.IQStanza;
import topchat.server.protocol.xmpp.stream.element.MessageStanza;
import topchat.server.protocol.xmpp.stream.element.PresenceStanza;
import topchat.server.protocol.xmpp.stream.element.Query;
import topchat.server.protocol.xmpp.stream.element.StreamElement;
import topchat.server.protocol.xmpp.stream.element.XElement;
import topchat.server.protocol.xmpp.stream.parser.Parser;

/**
 * The context in which the client is connected and authenticated
 */
public class ConnectedClientContext extends XMPPContext
{
	private static Logger logger = Logger
			.getLogger(ConnectedClientContext.class);

	/**
	 * Constructs a ConnectedClientContext
	 * 
	 * @param mgr
	 *            the manager of this context
	 */
	public ConnectedClientContext(XMPPConnectionManager mgr)
	{
		super(mgr);
	}

	@Override
	public synchronized void processRead(byte[] rd)
	{
		String s = new String(rd);
		logger.debug("received: " + s);

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
			processElement(element);
	}

	/**
	 * Called to process an XMPP StreamElement
	 * 
	 * @param streamElement
	 *            the element to process
	 */
	private synchronized void processElement(StreamElement streamElement)
	{

		if (streamElement.isIq())
		{
			IQStanza iqStanza = (IQStanza) streamElement;
			logger.debug("FOUND IQ " + iqStanza);

			proccessIq(iqStanza);
		} else if (streamElement.isPresence())
		{
			PresenceStanza presenceStanza = (PresenceStanza) streamElement;
			logger.debug("FOUND PRESENCE " + presenceStanza);

			processPresence(presenceStanza);
		} else if (streamElement.isMessage())
		{
			MessageStanza messageStanza = (MessageStanza) streamElement;
			logger.debug("FOUND MESSAGE " + messageStanza);

			if ("groupchat".equals(messageStanza.getAttribute("type")))
			{
				getXMPPManager().sendGroupchat(messageStanza);
			}
		}
	}

	/**
	 * Called to process a PresenceStanza
	 * 
	 * @param presenceStanza
	 *            the stanza to be processed
	 */
	private void processPresence(PresenceStanza presenceStanza)
	{
		processStatus(presenceStanza);
		
		processAvailability(presenceStanza);


		processXElement(presenceStanza);
	}

	private void processAvailability(PresenceStanza presenceStanza)
	{
		// if unavailable is received from connected client check if it is trying
		// to exit a room
		if ("unavailable".equals(presenceStanza.getAttribute("type")))
		{
			String to = presenceStanza.getAttribute("to");
			
			if (to != null)
			{
				String[] toElements = to.split("/");
				String roomName = toElements[0];
				

				if (getXMPPManager().isRoomCreated(roomName))
				{
					getXMPPManager().leaveRoom(roomName);
				}
			}
		}		
	}

	/**
	 * Called to process an XElement
	 * 
	 * @param presenceStanza
	 *            the PresenceStanza containing the XElement
	 */
	private void processXElement(PresenceStanza presenceStanza)
	{
		XElement xElement = presenceStanza.getXElement();

		if (xElement != null)
		{
			// Jabber User Seeks to Enter a Room (Multi-User Chat)
			if (xElement.isMUC())
			{
				// join/add room
				String to = presenceStanza.getAttribute("to");

				if (to != null)
				{
					String[] toElements = to.split("/");
					String roomName = toElements[0];
					String roomUser = toElements[1];

					if (getXMPPManager().isRoomCreated(roomName))
					{
						processJoinRoom(roomName, roomUser, presenceStanza);
					} else
					{
						processAddRoom(roomName, roomUser, presenceStanza);
					}
				}
			}
		}

	}

	/**
	 * Called to process the add of a new room
	 * 
	 * @param roomName
	 *            the name of the room
	 * @param roomUser
	 *            the nickname of the user that adds this room
	 * @param presenceStanza
	 *            the PresenceStanza sent to request the creation of this room
	 */
	private void processAddRoom(String roomName, String roomUser,
			PresenceStanza presenceStanza)
	{
		Room room = getXMPPManager().addRoom(roomName, roomUser);

		// Service Sends Presence from Existing Occupants to New Occupant

		// Status code 201 means the room was just created
		for (RoomParticipant participant : room.getParticipants())
		{

			// Service Sends Presence from Existing Occupants to New Occupant

			String presenceMsg = "<presence " + "id='"
					+ presenceStanza.getAttribute("id") + "'" + "to='"
					+ getXMPPManager().getUser().toString() + "'" + "from='"
					+ roomName + "/" + participant.getRoomUser() + "'>"
					+ "<status>" + participant.getUser().status + "</status>"
					+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
					+ "<item affiliation='" + participant.getAffiliation()
					+ "' jid='" + getXMPPManager().getUser().toString() + "'"
					+ "role='" + participant.getRole()
					+ "'><reason></reason><actor jid=''/></item>"
					+ "<status code='201' />" + "</x></presence>";

			getXMPPManager().send(presenceMsg.getBytes());
		}
	}

	/**
	 * Called to process that the user in this context joined a room
	 * 
	 * @param roomName
	 *            the name of the room
	 * @param roomUser
	 *            the nickname of the user in this room
	 * @param presenceStanza
	 *            the PresenceStanza sent for joining the room
	 */
	private void processJoinRoom(String roomName, String roomUser,
			PresenceStanza presenceStanza)
	{
		Room room = getXMPPManager().joinRoom(roomName, roomUser);

		for (RoomParticipant participant : room.getParticipants())
		{

			// Service Sends Presence from Existing Occupants to New Occupant

			String presenceMsg = "<presence " + "id='"
					+ presenceStanza.getAttribute("id") + "'" + "to='"
					+ getXMPPManager().getUser().toString() + "'" + "from='"
					+ roomName + "/" + participant.getRoomUser() + "'>"
					+ "<status>" + participant.getUser().status + "</status>"
					+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
					+ "<item affiliation='" + participant.getAffiliation()
					+ "' jid='" + getXMPPManager().getUser().toString() + "'"
					+ "role='" + participant.getRole()
					+ "'><reason></reason><actor jid=''/></item>"
					+ "</x></presence>";

			getXMPPManager().send(presenceMsg.getBytes());
		}

	}
	

	/**
	 * Called to process any status information sent with a presence
	 * 
	 * @param presenceStanza
	 *            the PresenceStanza checked for status information
	 */
	private void processStatus(PresenceStanza presenceStanza)
	{
		// this could be a status presence
		String status = presenceStanza.getData("status");

		if (status != null)
			getXMPPManager().getUser().setStatus(status);
	}

	/**
	 * Called to process an I/Q stanza
	 * 
	 * @param iqStanza
	 *            the IQStanza to be processed
	 */
	private void proccessIq(IQStanza iqStanza)
	{
		if (iqStanza.isGet())
		{
			logger.debug("iq is get, query" + iqStanza.getQuery());
		} else if (iqStanza.isSet())
		{
			logger.debug("iq is set " + iqStanza.getQuery());

			Query query = iqStanza.getQuery();

			if (query != null)
			{

				if (query.isMUCOwnerQuery())
				{
					String iqMsg = "<iq type='result' "
							+ "id='"
							+ iqStanza.getAttribute("id")
							+ "'"
							+ "to='"
							+ getXMPPManager().getUser().toString()
							+ "'"
							+ "from='"
							+ iqStanza.getAttribute("to")
							+ "'"
							+ "><query xmlns='http://jabber.org/protocol/muc#owner'></query></iq>";

					getXMPPManager().send(iqMsg.getBytes());
				}
			}
		}
	}

}
