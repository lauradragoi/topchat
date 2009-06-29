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

/**
 * Describes a stream element
 */
public class StreamElement implements Constants
{

	/** The type of the stream element */
	private int type = EMPTY_TYPE;

	/** Constructs a StreamElement */
	public StreamElement(int type)
	{
		this.type = type;
	}

	/**
	 * 
	 * @return true if the StreamElement is a starttls message, false otherwise
	 */
	public boolean isStartTLS()
	{
		return type == STARTTLS_TYPE;
	}

	/**
	 * 
	 * @return true if the StreamElement is a start of stream message, false
	 *         otherwise
	 */
	public boolean isXMPPStream()
	{
		return type == XMPPSTREAM_TYPE;
	}

	/**
	 * 
	 * @return true if the StreamElement is an IQ message, false otherwise
	 */
	public boolean isIq()
	{
		return type == IQSTANZA_TYPE;
	}

	/**
	 * 
	 * @return true if the StreamElement is a presence message, false otherwise
	 */
	public boolean isPresence()
	{
		return type == PRESENCE_STANZA_TYPE;
	}

	/**
	 * 
	 * @return true if the StreamElement is an auth message, false otherwise
	 */
	public boolean isAuth()
	{
		return type == AUTH_TYPE;
	}

	/**
	 * 
	 * @return true if the StreamElement is a features message, false otherwise
	 */
	public boolean isFeatures()
	{
		return type == FEATURES_TYPE;
	}

	/**
	 * 
	 * @return true if the StreamElement is a 'message' , false otherwise
	 */
	public boolean isMessage()
	{
		return type == MESSAGE_STANZA_TYPE;
	}

}
