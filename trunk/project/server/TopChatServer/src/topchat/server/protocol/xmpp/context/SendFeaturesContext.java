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

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.stream.Features;
import topchat.server.protocol.xmpp.stream.parser.Preparer;

import org.apache.log4j.Logger;


import topchat.server.defaults.DefaultContext;

/**
 * In this context the server announces the client what features it can offer
 */
public class SendFeaturesContext extends XMPPContext {

	private static Logger logger = Logger.getLogger(SendFeaturesContext.class);	
			

	public SendFeaturesContext(XMPPConnectionManager mgr, DefaultContext old) {
		super(mgr, old);
				
		Features ft = null;
		try {
			ft = getXMPPManager().getFeatures();
		} catch (Exception e) {			
			logger.warn("Could not obtain features info");
			e.printStackTrace();			
		}
		
		// prepare the message to be written
		String msg = Preparer.prepareFeatures(ft);
		
		// send it
		write(msg);			
		flush();
		
		logger.debug("Writing: " + msg);
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		
		logger.debug("Received: " + s);
						
		s = null;
	}	
	
	@Override
	public void processWrite()
	{
		setDone();
	}
}