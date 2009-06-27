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
package topchat.server.data.db;

import java.sql.*;
import java.util.Vector;

import org.apache.log4j.Logger;


public class DatabaseConnector {

	final String DRIVER	= "com.mysql.jdbc.Driver";
	final String IP		= "127.0.0.1"; // localhost doesn't work when not root?!
	final String DB		= "pwdb9";
	final String USER	= "root";
	final String PASS	= "";
	
	final String SEP	= "\t\t";
	
	private Connection			conn	= null;
	private PreparedStatement	stmt	= null;
	
	private static Logger logger = Logger.getLogger(DatabaseConnector.class);
	
	private void initDatabase(String url, String user, String pass) throws SQLException {
		
		// obtain connection
		conn = DriverManager.getConnection(url, user, pass);
	}
	
	private void setCommand(String command) throws SQLException {
		
		// free old connection, if any
		if (stmt != null)
			stmt.close();
		
		// prepare statement based on given command
		stmt = conn.prepareStatement(command);
	}
	
	private ResultSet executeQuery() throws SQLException {
		
		return stmt.executeQuery();
	}
	
	private boolean execute() throws SQLException {
		
		return stmt.execute();
	}
	
	private void close() throws SQLException {
		
		// close connection
		if (conn != null)
			conn.close();
			
		// close statement
		if (stmt != null)
			stmt.close();
	}	
	
	public boolean addUser(String ip, int port) {
		boolean result = false;
		try {
			Class.forName(DRIVER);
			
			initDatabase("jdbc:mysql://" + IP + ":3306/" + DB, USER, PASS);
			setCommand("INSERT INTO `idp_tema3`.`users` (`id`, `port`, `address`) " +
					"VALUES (NULL, '" + port + "', '" + ip + "')");
			result = execute();
			
			
		} catch (ClassNotFoundException e) {
			System.err.println("Cannot load SQL driver: " + DRIVER);
			
		} catch (SQLException e) {
			System.err.println("SQL Error: " + e.getMessage());
			
		} finally {
				try {
					close();
				} catch (SQLException exc) {}
		}			
		
		return result;
	}


	public String[] getUsers() {
				
		logger.debug("Obtaining users");
		Vector<String> result = new Vector<String>();
		try {
			Class.forName(DRIVER);
			
			initDatabase("jdbc:mysql://" + IP + ":3306/" + DB, USER, PASS);
			setCommand("SELECT * FROM users");
			ResultSet rs = executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
						
			while (rs.next()) {
				String currentUser = "";
				for (int i = 1; i <= columns; i++)
				{
					if ("USERNAME".equals(rsmd.getColumnName(i).toUpperCase()))
						currentUser = rs.getString(i);
					if ("ADDRESS".equals(rsmd.getColumnName(i).toUpperCase()))
						currentUser = currentUser + ":" + rs.getString(i);
				}
				
				result.add(currentUser);								
			}
			
		} catch (ClassNotFoundException e) {
			logger.fatal("Cannot load SQL driver: " + DRIVER);
			
		} catch (SQLException e) {
			logger.fatal("SQL Error: " + e.getMessage());
			
		} finally {
				try {
					close();
				} catch (SQLException exc) {}
		}		
		
		String[] anArray = new String[result.size()];
		result.copyInto(anArray);				
		return anArray;
	}

	public boolean removeUser(String ip, int port) {
		boolean result = false;
		try {
			Class.forName(DRIVER);
			
			initDatabase("jdbc:mysql://" + IP + ":3306/" + DB, USER, PASS);
			setCommand("DELETE FROM `idp_tema3`.`users` WHERE " +
					"`users`.`port` = " + port + " AND " +
					"`users`.`address` = '" + ip + "'");
			result = execute();
			
			
		} catch (ClassNotFoundException e) {
			logger.fatal("Cannot load SQL driver: " + DRIVER);
			
		} catch (SQLException e) {
			logger.fatal("SQL Error: " + e.getMessage());
			
		} finally {
				try {
					close();
				} catch (SQLException exc) {}
		}			
		
		return result;

	}	
	
}
