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
package topchat.server.db;

import java.sql.*;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * A DatabaseConnector provides a connection to a MySQL database using the
 * Connector/J driver. Queries can be executed on the database to which the
 * connection is established.
 */
public class DatabaseConnector
{

	final String DRIVER = "com.mysql.jdbc.Driver";
	private String port = "3306";

	/** The connection to the database */
	private Connection conn = null;
	/** The statement containing the query to be executed on the database */
	private PreparedStatement stmt = null;

	private static Logger logger = Logger.getLogger(DatabaseConnector.class);

	/**
	 * Constructs a DatabaseConnector.
	 * 
	 * @param ip
	 *            the address of the server where the database is located.
	 * @param db
	 *            the name of the database
	 * @param user
	 *            the user name used for accessing the database
	 * @param pass
	 *            the password of the user
	 * @throws Exception
	 *             if the connection fails
	 */
	public DatabaseConnector(String ip, String db, String user, String pass)
			throws Exception
	{
		try
		{
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e)
		{
			logger.debug("Driver not found for connecting to database");
			throw new Exception("Driver not found.");
		}

		conn = initDatabase(getDatabaseURL(ip, db), user, pass);

		logger.debug("Database connection initialized to " + ip + " " + db
				+ " " + user + " " + pass);
	}

	/**
	 * Returns the corresponding url in JDBC format for a database.
	 * 
	 * @param ip
	 *            the address of the server where the database is located
	 * @param db
	 *            the name of the server
	 * @return the String containing the url
	 */
	private String getDatabaseURL(String ip, String db)
	{
		return "jdbc:mysql://" + ip + ":" + port + "/" + db;
	}

	/**
	 * Initialize a Connection to a database
	 * 
	 * @param url
	 *            the JDBC format url for connecting to the database
	 * @param user
	 *            the user used for accessing the database
	 * @param pass
	 *            the password of the user
	 * @return the Connection to the database
	 * @throws SQLException
	 *             if the connection fails
	 */
	private Connection initDatabase(String url, String user, String pass)
			throws SQLException
	{
		logger.debug("init db " + url + " " + user + " " + pass);
		// obtain connection
		Connection conn = DriverManager.getConnection(url, user, pass);
		return conn;
	}

	/**
	 * Set a command (query) to be executed next on the connected database.
	 * 
	 * @param command
	 *            the command to be executed
	 * @throws SQLException
	 *             if the command does not correspond to a proper
	 *             PreparedStatement
	 */
	public void setCommand(String command) throws SQLException
	{

		// free old connection, if any
		if (stmt != null)
			stmt.close();

		// prepare statement based on given command
		stmt = conn.prepareStatement(command);
	}

	/**
	 * Execute the query corresponding to the last set command.
	 * 
	 * @return the ResultSet returned by query
	 * @throws SQLException
	 *             if the query throws an exception while executing
	 */
	public ResultSet executeQuery() throws SQLException
	{

		return stmt.executeQuery();
	}

	/**
	 * Execute the last set command.
	 * 
	 * @return the result returned by the execution
	 * @throws SQLException
	 *             if the command throws an exception while executing
	 */
	public boolean execute() throws SQLException
	{

		return stmt.execute();
	}

	/**
	 * Closes the database connection
	 * 
	 * @throws SQLException
	 *             if anything goes wrong while closing the connection
	 */
	public void close() throws SQLException
	{

		// close connection
		if (conn != null)
			conn.close();

		// close statement
		if (stmt != null)
			stmt.close();
	}

	/**
	 * Check if an item respecting certain constraints exists in a table
	 * 
	 * @param table
	 *            the name of the table
	 * @param constraints
	 *            a HashMap containing column names as keys and item value as
	 *            values
	 * @return true if there is such an item in the table, false otherwise
	 */
	public boolean checkExistingItem(String table,
			HashMap<String, String> constraints)
	{
		String keys = "";
		String condition = "";
		for (String s : constraints.keySet())
		{
			if (keys.length() > 0)
				keys = keys.concat(",");
			keys = keys.concat("`" + s + "`");
			if (condition.length() > 0)
				condition = condition.concat(" AND ");
			condition = condition.concat(s + "='" + constraints.get(s) + "'");
		}

		logger.debug("keys " + keys + " condition " + condition);

		String cmd = "SELECT " + keys + " FROM " + table + " WHERE "
				+ condition;

		try
		{
			setCommand(cmd);
			ResultSet rs = executeQuery();

			if (rs.next())
			{
				logger.debug("item exists in table " + table
						+ " with constraints " + condition);
				return true;
			}
		} catch (SQLException e)
		{
			logger.debug("SQL error " + e);
		}

		return false;
	}

	/**
	 * Insert an entry in a table with certain values.
	 * 
	 * @param table
	 *            the name of the table
	 * @param values
	 *            a HashMap containing the column names as keys and the values
	 *            to be inserted as values.
	 * @return true if the insert was successful, false otherwise
	 */
	public boolean insertValues(String table, HashMap<String, String> values)
	{
		boolean result = false;

		String keys = "";
		String val = "";
		for (String s : values.keySet())
		{
			if (keys.length() > 0)
				keys = keys.concat(",");
			keys = keys.concat("`" + s + "`");
			if (val.length() > 0)
				val = val.concat(",");
			String cleanValue = values.get(s).replace("'", "\"");
			val = val.concat("'" + cleanValue + "'");
		}

		logger.debug("keys " + keys + " values " + val);

		keys = keys.concat(", `timestamp`");
		val = val.concat(", null");

		String cmd = "INSERT INTO `" + table + "` (" + keys + ") VALUES ("
				+ val + ")";

		logger.debug("command " + cmd);

		try
		{
			setCommand(cmd);
			result = execute();
		} catch (SQLException e)
		{
			logger.debug("Insert error in " + table + " :" + e);
		}

		return result;
	}

}
