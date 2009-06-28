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

import java.util.HashMap;
import org.apache.log4j.Logger;
import topchat.server.db.DatabaseConnector;
import topchat.server.interfaces.AuthenticationHandlerInterface;
import topchat.server.interfaces.AuthenticationMediator;

/**
 * The AuthenticationHandler is the module in charge of authenticating clients.
 * 
 * It checks if the credentials of the user exist in the authentication
 * database.
 */
public class AuthenticationHandler implements AuthenticationHandlerInterface
{
	/** The mediator to which the AuthenticationHandler connects */
	private AuthenticationMediator med = null;

	private static Logger logger = Logger
			.getLogger(AuthenticationHandler.class);

	/** The connection to the database used for authentication */
	private DatabaseConnector dbConnector = null;

	/**
	 * The name of the table in the database that contains the authentication
	 * info
	 */
	private String table = null;

	/**
	 * Constructs an AuthenticationHandler connected to an
	 * AuthenticationMediator. The connection to the database used for
	 * authentication will also be established.
	 * 
	 * @param med
	 *            the AuthenticationMediator to which the module connects
	 * @throws Exception
	 *             if the connection to the database fails
	 */
	public AuthenticationHandler(AuthenticationMediator med) throws Exception
	{
		// connect to mediator
		this.med = med;

		// connect mediator to this module
		med.setAuthenticationHandler(this);

		// init connection to database
		dbConnector = initDB();

		logger.info("Authentication module initiated");
	}

	/**
	 * Initialize the connection to the database used for authentication.
	 * 
	 * @return the DatabaseConnector corresponding to the initiated connection.
	 * @throws Exception
	 *             if the properties necessary for establishing the connection
	 *             are improperly set or the database connection cannot be
	 *             initialized.
	 * 
	 */
	private DatabaseConnector initDB() throws Exception
	{
		// obtain the properties
		String ip = med.getProperty("authentication.server");
		String db = med.getProperty("authentication.db");
		String user = med.getProperty("authentication.user");
		String pass = med.getProperty("authentication.pass");
		String table = med.getProperty("authentication.table");

		// set the table for future uses
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
	 * This method checks whether a user with a certain username and password is
	 * authorized to connect to the server.
	 * 
	 * @param username
	 *            the name of the user
	 * @param password
	 *            the password of the user
	 * 
	 * @return true if the user is allowed to connect, false otherwise
	 * 
	 */
	public boolean checkUser(String username, String password)
	{
		// create constraints
		HashMap<String, String> constraints = new HashMap<String, String>();
		constraints.put("username", username);
		constraints.put("password", password);

		// query database
		boolean result = dbConnector.checkExistingItem(table, constraints);

		logger.info("Check user " + username + "returned " + result);

		return result;
	}

}
