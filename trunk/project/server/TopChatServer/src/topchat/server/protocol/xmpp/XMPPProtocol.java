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
package topchat.server.protocol.xmpp;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.defaults.DefaultContext;
import topchat.server.interfaces.Net;
import topchat.server.interfaces.Protocol;
import topchat.server.interfaces.ProtocolMediator;
import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;
import topchat.server.protocol.xmpp.context.XMPPContext;

public class XMPPProtocol implements Protocol, XMPPConstants {

	@SuppressWarnings("unused")
	private ProtocolMediator med = null;
	private Net net = null;

	private static Logger logger = Logger.getLogger(XMPPProtocol.class);

	public XMPPProtocol(ProtocolMediator med) {
		setMediator(med);
		med.setProtocol(this);

		logger.info("XMPPProtocol initiated");
	}

	@Override
	public void setMediator(ProtocolMediator med) {
		this.med = med;
	}

	@Override
	public int getListeningPort() {
		return XMPP_DEFAULT_SERVER_PORT;
	}

	@Override
	public void start(Net net) 
	{
		this.net = net;
		net.setProtocol(this);
						
		logger.info("Protocol started");
		
		net.start(getListeningPort());
	}
	
	@Override
	public DefaultConnectionManager getConnectionManager()
	{
		return new XMPPConnectionManager();
	}	
	
	 
}
