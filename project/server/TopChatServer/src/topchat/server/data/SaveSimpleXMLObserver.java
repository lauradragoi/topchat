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
package topchat.server.data;

import java.util.HashMap;

import org.apache.log4j.Logger;

import topchat.server.db.DatabaseConnector;

public class SaveSimpleXMLObserver extends DataObserver
{

	/** Name of the table where the data will be saved */
	private String table = null;

	/** Connection to the database where the data will be saved */
	private DatabaseConnector dbConnector = null;

	private DataHandler handler;

	private static Logger logger = Logger.getLogger(SaveAllObserver.class);

	/**
	 * Constructs a SaveAllHandler connected to a DataMediator and initiates the
	 * connection to the database used for saving the data.
	 * 
	 * @param dataHandler
	 *            the DataMediator to which the DataHandler connects
	 * @throws Exception
	 *             if the database connection cannot be initiated
	 */
	public SaveSimpleXMLObserver(DataHandler dataHandler) throws Exception
	{
		this.handler = dataHandler;
		
		dbConnector = init();
	
		handler.registerObserver(this, DataEvent.ROOM_JOINED);
		handler.registerObserver(this, DataEvent.ROOM_LEFT);
		handler.registerObserver(this, DataEvent.GROUP_MESSAGE);

		logger.info("SaveSimpleXMLObserver initiated.");
	}
	

	/**
	 * Initiate a connection to the database used for saving data.
	 * 
	 * @return the DatabaseConnector corresponding to the initiated connection
	 * @throws Exception
	 *             if the properties needed for connecting are improperly set or
	 *             the connection to the database does not succeed.
	 */
	private DatabaseConnector init() throws Exception
	{
		// init properties
		String ip = handler.getProperty("data.simplexml.server");
		String db = handler.getProperty("data.simplexml.db");
		String user = handler.getProperty("data.simplexml.user");
		String pass = handler.getProperty("data.simplexml.pass");
		String table = handler.getProperty("data.simplexml.table");

		setTable(table);

		// init connection to database
		DatabaseConnector dbConnector = new DatabaseConnector(ip, db, user,
				pass);

		return dbConnector;
	}

	private void setTable(String table)
	{
		this.table = table;
	}

	/**
	 * Save a received message in the database.
	 * 
	 * @param s
	 *            the received message
	 */
	public void handleReceived(String s)
	{

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("type", "receive");
		values.put("message", s);

		boolean result = dbConnector.insertValues(table, values);

		logger.debug("Insert in " + table + " of received msg " + s + " was "
				+ result);
	}

	/**
	 * Save a sent message in the database.
	 * 
	 * @param s
	 *            the sent message
	 */
	public void handleSent(String s)
	{
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("type", "sent");
		values.put("message", s);

		boolean result = dbConnector.insertValues(table, values);

		logger.debug("insert in " + table + " of sent " + s + " was " + result);
	}

	@Override
	public void handle(DataEvent event, String[] args)
	{
		switch (event)
		{
			case HANDLE_RECEIVED :
				handleReceived(args[0]);
				break;
			case HANDLE_SENT :
				handleSent(args[0]);
				break;		
			case ROOM_JOINED:
				handleRoomJoined(args[0], args[1]);
				break;
			case ROOM_LEFT:
				handleRoomLeft(args[0], args[1]);
				break;
			case GROUP_MESSAGE:
				handleGroupMessage(args[0], args[1], args[2], args[3], args[4]);
				break;
			default :
				break;
		}
		
	}


	private void handleRoomLeft(String room, String user)
	{
		HashMap<String, String> values = new HashMap<String, String>();		
		values.put("nick", user);
		values.put("room", room);
		values.put("type", "leave");
		values.put("message", "left room.");

		boolean result = dbConnector.insertValues(table, values);

		logger.debug("insert in " + table + " of room left was " + result);
	}


	/**
	 * @param string
	 * @param string2
	 */
	private void handleRoomJoined(String room, String user)
	{
		HashMap<String, String> values = new HashMap<String, String>();		
		values.put("nick", user);
		values.put("room", room);
		values.put("type", "join");
		values.put("message", "joined room.");


		boolean result = dbConnector.insertValues(table, values);

		logger.debug("insert in " + table + " of joined was " + result);
		
	}
	
	/**
	 * @param roomUser
	 * @param roomName
	 * @param body
	 * @param numMessages
	 * @param ref
	 */
	private void handleGroupMessage(String roomUser, String roomName, String body,
			String id, String ref)
	{
		HashMap<String, String> values = new HashMap<String, String>();		
		values.put("nick", roomUser);
		values.put("room", roomName);	
		values.put("type", "message");
		values.put("message", body);
		values.put("msg_id", id);
		values.put("reference", ref);

		boolean result = dbConnector.insertValues(table, values);

		logger.debug("insert in " + table + " of group message was " + result);
	}


}
