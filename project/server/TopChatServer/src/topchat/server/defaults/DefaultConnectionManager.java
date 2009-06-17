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
package topchat.server.defaults;

import org.apache.log4j.Logger;


/**
 * Manages a connection between the server and a client
 *
 */
public class DefaultConnectionManager 
{
	/** The current context of the connection */
	protected DefaultContext context;
	

	private static Logger logger = Logger.getLogger(DefaultConnectionManager.class);
		
	/**
	 * Method called by the protocol to inform the connection manager 
	 * 	when data is received from the network.
	 * @param rd
	 * @param count
	 */
	public void processRead(byte[] rd, int count) 
	{
		String s = new String(rd);		
		logger.debug("Received: " + s);
		
		// inform the context
		context.processRead(rd);
	}
	
}
