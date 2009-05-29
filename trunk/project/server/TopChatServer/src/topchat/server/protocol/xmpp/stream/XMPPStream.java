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
package topchat.server.protocol.xmpp.stream;

/**
 * Describes the XML stream used for communication.
 * 
 * Stream attributes:
 * 
 *           |  initiating to receiving  |  receiving to initiating
 *  ---------+---------------------------+-----------------------
 *	to       |  hostname of receiver     |  silently ignored
 *	from     |  silently ignored         |  hostname of receiver
 *	id       |  silently ignored         |  session key
 *	xml:lang |  default language         |  default language
 *	version  |  signals XMPP 1.0 support |  signals XMPP 1.0 support
 * 
 */
public class XMPPStream {

	private String to = null;
	private String from = null;
	private String id = null;
	private String lang = null;
	private String version = null;
	
	public XMPPStream(String to, String from, String id, String lang, String version)
	{
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
	
	
	public static String initialStream()
	{
		StringBuilder sb = new StringBuilder();
		//sb.append("<?xml version='1.0'?>");
		sb.append("<stream:stream ");
		//TODO : fill in from
		sb.append("from='example.com' ");
		//TODO : fill in ID
		sb.append("id='someid' ");
		sb.append("xmlns='jabber:client' ");
		sb.append("xmlns:stream='http://etherx.jabber.org/streams' ");
		sb.append("version='1.0'> ");
		return sb.toString();
	}
	
	
}
