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

public class DataHandler implements DataHandlerInterface {

	/** Connection to mediator */
	private DataMediator med = null;
	
	private String table = null;
	
	private DatabaseConnector dbConnector = null;
	
	private static Logger logger = Logger.getLogger(DataHandler.class);
	
	public DataHandler(DataMediator med) throws Exception
	{
		this.med = med;
		med.setDataHandler(this);		
		
		logger.info("Data handling module initiated.");
		
		dbConnector = init();				
	}
	
	private DatabaseConnector init() throws Exception
	{				
		String ip = null;
		try {
			ip = med.getProperty("data.server");
		} catch (Exception e) {
			logger.debug("Data server IP cannot be retrieved " + e);
		}
		String db = null;
		try {
			db = med.getProperty("data.db");
		} catch (Exception e) {
			logger.debug("Data DB name cannot be retrieved " + e);
		}
		String user = null;
		try {
			user = med.getProperty("data.user");
		} catch (Exception e) {
			logger.debug("Data DB user cannot be retrieved " + e);
		}
		String pass = null;
		try {
			pass = med.getProperty("data.pass");
		} catch (Exception e) {
			logger.debug("Data DB password cannot be retrieved " + e);
		}
		
		String table = null;
		try {
			table = med.getProperty("data.table");
		} catch (Exception e) {
			logger.debug("Data table cannot be retrieved " + e);
		}
		this.table = table;
				
		// init connection to database
		DatabaseConnector dbConnector = new DatabaseConnector(ip, db, user, pass);
		
		return dbConnector;
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.DataHandlerInterface#saveRead(java.lang.String)
	 */
	@Override
	public void saveRead(String s) {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("type", "receive");
		values.put("message", s);
		
		boolean result = dbConnector.insertValues(table, values);
		
		logger.debug("insert in " + table + " of received " + s + " was " + result);					
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.DataHandlerInterface#saveSent(java.lang.String)
	 */
	@Override
	public void saveSent(String s) {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("type", "sent");
		values.put("message", s);
		
		boolean result = dbConnector.insertValues(table, values);
		
		logger.debug("insert in " + table + " of sent " + s + " was " + result);			
	}
}
