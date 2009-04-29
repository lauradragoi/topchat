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

import topchat.server.interfaces.Protocol;
import topchat.server.interfaces.ProtocolMediator;

public class XMPPProtocol implements Protocol, XMPPConstants {

	private ProtocolMediator med = null;

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
	public byte[] prepareWrite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processRead(byte[] rd) {
		String s = new String(rd);
		logger.debug("Received: " + s);

		rd = null;
		s = null;
	}
}
