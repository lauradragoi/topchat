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
 * Describes an I/Q stanza
 */
public class IQStanza extends XMPPStanza
{

	/** The query stanza contained by the IQStanza (if any) */
	private Query query = null;

	/**
	 * Constructs an IQStanza
	 */
	public IQStanza()
	{
		super(IQSTANZA_TYPE);
	}

	/**
	 * 
	 * @return true if the type of the IQStanza is get
	 */
	public boolean isGet()
	{
		return "get".equals(getAttribute("type"));
	}

	/**
	 * 
	 * @return true if the type of the IQStanza is set
	 */
	public boolean isSet()
	{
		return "set".equals(getAttribute("type"));
	}

	/**
	 * Adds a query to an IQStanza
	 * 
	 * @param query
	 *            the query to be added
	 */
	public void addQuery(Query query)
	{
		this.query = query;
	}

	/**
	 * Obtain the query inside the IQStanza
	 * 
	 * @return the query
	 */
	public Query getQuery()
	{
		return query;
	}
}
