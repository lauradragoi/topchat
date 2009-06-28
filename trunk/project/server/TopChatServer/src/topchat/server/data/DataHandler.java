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
import topchat.server.interfaces.DataHandlerInterface;
import topchat.server.interfaces.DataMediator;

/**
 * This module is responsible for processing the data received/sent by the
 * server. It currently saves all this data in a database.
 */
public class DataHandler implements DataHandlerInterface
{

	/** Connection to mediator */
	private DataMediator med = null;

	/** Name of the table where the data will be saved */
	private String table = null;

	/** Connection to the database where the data will be saved */
	private DatabaseConnector dbConnector = null;

	private static Logger logger = Logger.getLogger(DataHandler.class);

	/**
	 * Constructs a DataHandler connected to a DataMediator and initiates the
	 * connection to the database used for saving the data.
	 * 
	 * @param med
	 *            the DataMediator to which the DataHandler connects
	 * @throws Exception
	 *             if the database connection cannot be initiated
	 */
	public DataHandler(DataMediator med) throws Exception
	{
		this.med = med;
		med.setDataHandler(this);

		dbConnector = init();

		logger.info("Data handling module initiated.");
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
		String ip = med.getProperty("data.server");
		String db = med.getProperty("data.db");
		String user = med.getProperty("data.user");
		String pass = med.getProperty("data.pass");
		String table = med.getProperty("data.table");

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
	@Override
	public void saveReceived(String s)
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
	@Override
	public void saveSent(String s)
	{
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("type", "sent");
		values.put("message", s);

		boolean result = dbConnector.insertValues(table, values);

		logger.debug("insert in " + table + " of sent " + s + " was " + result);
	}
}
