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
package topchat.server.authentication;

import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import topchat.server.db.DatabaseConnector;
import topchat.server.interfaces.AuthenticationHandlerInterface;
import topchat.server.interfaces.AuthenticationMediator;
import topchat.server.mediator.Mediator;

public class AuthenticationHandler implements AuthenticationHandlerInterface {

	private AuthenticationMediator med = null;
	
	private static Logger logger = Logger.getLogger(AuthenticationHandler.class);
	
	private DatabaseConnector dbConnector = null;
	
	private String table = null;
	
	/**
	 * @param med
	 * @throws SQLException 
	 */
	public AuthenticationHandler(Mediator med) throws Exception {
		this.med = med;
		med.setAuthenticationHandler(this);
		
		logger.info("Authentication module initiated");
		
		dbConnector = init();			
	}
	
	private DatabaseConnector init() throws Exception
	{				
		String ip = null;
		try {
			ip = med.getAuthServerIP();
		} catch (Exception e) {
			logger.debug("Authentication server IP cannot be retrieved " + e);
		}
		String db = null;
		try {
			db = med.getAuthDBName();
		} catch (Exception e) {
			logger.debug("Authentication DB name cannot be retrieved " + e);
		}
		String user = null;
		try {
			user = med.getAuthDBUser();
		} catch (Exception e) {
			logger.debug("Authentication DB user cannot be retrieved " + e);
		}
		String pass = null;
		try {
			pass = med.getAuthDBPass();
		} catch (Exception e) {
			logger.debug("Authentication DB password cannot be retrieved " + e);
		}
		
		String table = null;
		try {
			table = med.getAuthTable();
		} catch (Exception e) {
			logger.debug("Authentication table cannot be retrieved " + e);
		}
		this.table = table;
				
		// init connection to database
		DatabaseConnector dbConnector = new DatabaseConnector(ip, db, user, pass);
		
		return dbConnector;
	}
	
	public boolean checkUser(String username, String password)
	{
		HashMap<String, String> constraints = new HashMap<String, String>();
		constraints.put("username", username);
		constraints.put("password", password);
		
		boolean result = dbConnector.checkExistingItem(table, constraints);
		
		logger.debug("check User " + username + " " + password + " = " + result);
		
		return result;

	}

}
