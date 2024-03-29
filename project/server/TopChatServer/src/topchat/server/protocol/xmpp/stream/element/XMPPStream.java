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
 * Describes the XML stream used for communication.
 * 
 * Stream attributes:
 * 
 * | initiating to receiving | receiving to initiating
 * ---------+---------------------------+----------------------- to | hostname
 * of receiver | silently ignored from | silently ignored | hostname of receiver
 * id | silently ignored | session key xml:lang | default language | default
 * language version | signals XMPP 1.0 support | signals XMPP 1.0 support
 * 
 */
public class XMPPStream extends StreamElement implements Constants
{

	private String to = null;
	private String from = null;
	private String id = null;
	private String lang = null;
	private String version = null;

	/**
	 * Constructs an XMPPStream
	 * 
	 * @param to
	 *            the to field
	 * @param from
	 *            the from field
	 * @param id
	 *            the id of the stream
	 * @param lang
	 *            the language
	 * @param version
	 *            the version
	 */
	public XMPPStream(String to, String from, String id, String lang,
			String version)
	{
		super(XMPPSTREAM_TYPE);

		this.to = to;
		this.from = from;
		this.id = id;
		this.lang = lang;
		this.version = version;
	}

	public String getTo()
	{
		return to;
	}

	public String getFrom()
	{
		return from;
	}

	public String getVersion()
	{
		return version;
	}

	public String getId()
	{
		return id;
	}

	public String getLang()
	{
		return lang;
	}
}
