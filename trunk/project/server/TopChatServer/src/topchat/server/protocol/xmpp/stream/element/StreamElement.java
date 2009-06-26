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
package topchat.server.protocol.xmpp.stream.element;


public class StreamElement implements Constants {
	
	private int type = EMPTY_TYPE;
	
	public StreamElement(int type)
	{
		this.type = type;
	}
	
	public boolean isStartTLS()
	{
		return type == STARTTLS_TYPE;
	}
	
	public boolean isXMPPStream()
	{
		return type == XMPPSTREAM_TYPE;
	}
	
	public boolean isIq()
	{
		return type == IQSTANZA_TYPE;
	}
	
	public boolean isPresence()
	{
		return type == PRESENCE_STANZA_TYPE;
	}

	public boolean isAuth()
	{
		return type == AUTH_TYPE;
	}
	
	public boolean isFeatures()
	{
		return type == FEATURES_TYPE;
	}
	
	public boolean isMessage()
	{
		return type == MESSAGE_STANZA_TYPE;
	}
	
}
