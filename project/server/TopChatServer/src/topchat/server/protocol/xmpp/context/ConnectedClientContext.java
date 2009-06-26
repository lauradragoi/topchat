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

public class ConnectedClientContext extends XMPPContext 
{		
	private static Logger logger = Logger.getLogger(ConnectedClientContext.class);
	

	public ConnectedClientContext(XMPPConnectionManager mgr) {
		super(mgr);		
	}

	@Override
	public void processRead(byte[] rd) 
	{
		String s = new String(rd);
		logger.debug("received: " + s);	
		
		Vector<StreamElement> streamElements = null;
				
		try {		
			streamElements = Parser.parse(s);						
		} catch (Exception e) {		
			logger.warn("Error in parsing " + e);
			return ;
		}
		
		for (StreamElement element : streamElements)		
			processElement(element);		
		
		//	setDone();
	}
		
	
	private void processElement(StreamElement streamElement)
	{
		if (streamElement.isIq())
		{
			IQStanza iqStanza = (IQStanza) streamElement;
			logger.debug("FOUND IQ " + iqStanza);
			
			if (iqStanza.isGet())
			{
				logger.debug("iq is get, query" + iqStanza.getQuery());								
			}
			else if (iqStanza.isSet())
			{
				logger.debug("iq is set " + iqStanza.getQuery());
				
				Query query = iqStanza.getQuery();
				
				if (query != null)
				{
					
					if (query.isMUCOwnerQuery())
					{
						String iqMsg = "<iq type='result' " +
						"id='" + iqStanza.getAttribute("id") + "'" +
						"to='" + getXMPPManager().getUser().toString() + "'" +
						"from='" + iqStanza.getAttribute("to") + "'" +
						"><query xmlns='http://jabber.org/protocol/muc#owner'></query></iq>";
			
						getXMPPManager().send(iqMsg.getBytes());
					}
				}
			}							
		}
		else if (streamElement.isPresence())
		{
			PresenceStanza presenceStanza = (PresenceStanza) streamElement;
			logger.debug("FOUND PRESENCE " + presenceStanza);
			
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
							Room room = getXMPPManager().joinRoom(roomName, roomUser);
							
							for (RoomParticipant participant : room.getParticipants())
							{
								
								// Service Sends Presence from Existing Occupants to New Occupant
								
								String presenceMsg = "<presence " +
									"id='" + presenceStanza.getAttribute("id") + "'" +
									"to='" + getXMPPManager().getUser().toString() + "'" +
									"from='" + roomName + "/" + participant.getRoomUser() + "'>" +
									"<status>"+ participant.getUser().status +"</status>" +
									"<x xmlns='http://jabber.org/protocol/muc#user'>" +
									"<item affiliation='" + participant.getAffiliation() + "' jid='" 
									+ getXMPPManager().getUser().toString() + "'" +
						    		"role='" + participant.getRole() + "'><reason></reason><actor jid=''/></item>" +					    		
						    		"</x></presence>";		
							
								getXMPPManager().send(presenceMsg.getBytes());
							}
							
						}
						else
						{
							Room room = getXMPPManager().addRoom(roomName, roomUser);
							
							// Service Sends Presence from Existing Occupants to New Occupant
							
							// Status code 201 means the room was just created
							for (RoomParticipant participant : room.getParticipants())
							{
								
								// Service Sends Presence from Existing Occupants to New Occupant
								
								String presenceMsg = "<presence " +
									"id='" + presenceStanza.getAttribute("id") + "'" +
									"to='" + getXMPPManager().getUser().toString() + "'" +
									"from='" + roomName + "/" + participant.getRoomUser() + "'>" +
									"<status>"+ participant.getUser().status +"</status>" +
									"<x xmlns='http://jabber.org/protocol/muc#user'>" +
									"<item affiliation='" + participant.getAffiliation() + "' jid='" 
									+ getXMPPManager().getUser().toString() + "'" +
						    		"role='" + participant.getRole() + "'><reason></reason><actor jid=''/></item>" +
						    		"<status code='201' />" +
						    		"</x></presence>";		
							
								getXMPPManager().send(presenceMsg.getBytes());
							}
						}
					}					
				}				
			}
			
			// this could be a status presence
			String status = presenceStanza.getData("status");
				
			if (status != null)
				getXMPPManager().getUser().setStatus(status);
									
		}
		else if (streamElement.isMessage())
		{
			MessageStanza messageStanza = (MessageStanza) streamElement;
			logger.debug("FOUND MESSAGE " + messageStanza);
			
			if ("groupchat".equals(messageStanza.getAttribute("type")))
			{
				getXMPPManager().sendGroupchat(messageStanza);
			}
		}
	}
	
}
