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
 * Describes a query element
 */
public class Query extends XMPPStanza
{
	/** Constructs a query element */
	public Query()
	{
		super(QUERY_TYPE);
	}

	@Override
	public String toString()
	{
		return "[QUERY] namespace " + getNamespace();
	}

	/**
	 * Checks whether a query is a Roster query
	 * 
	 * @return true if it is a Roster query, false otherwise
	 */
	public boolean isRosterQuery()
	{
		return "jabber:iq:roster".equals(namespace);
	}

	/**
	 * Checks whether a query is a MUC owner query
	 * 
	 * @return true if it is a MUC owner query, false otherwise
	 */
	public boolean isMUCOwnerQuery()
	{
		return "http://jabber.org/protocol/muc#owner".equals(namespace);
	}

}
