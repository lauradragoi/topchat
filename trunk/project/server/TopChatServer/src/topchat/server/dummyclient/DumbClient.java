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
package topchat.server.dummyclient;


import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import topchat.server.protocol.xmpp.stream.parser.Parser;

public class DumbClient {

    public static XMPPConnection conn;
    
    private static Logger logger = Logger.getLogger(DumbClient.class);

    public static void makeConnection() throws XMPPException
    {
        ConnectionConfiguration config = new ConnectionConfiguration("localhost", 5222);
        conn = new XMPPConnection(config);
        try {
            conn.connect();
            if(conn.isConnected())
                logger.info("I'm connected!");
        } catch (XMPPException ex) {
            logger.fatal(ex);
        }
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			makeConnection();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
