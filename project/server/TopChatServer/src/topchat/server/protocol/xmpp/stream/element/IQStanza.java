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

public class IQStanza extends XMPPStanza {
	
	private Query query = null;

	public IQStanza()
	{
		super(IQSTANZA_TYPE);
	}
	
	public boolean isGet()
	{
		return "get".equals(getAttribute("type"));
	}
	
	public boolean isSet()
	{
		return "set".equals(getAttribute("type"));
	}
	
	public void addQuery(Query query)
	{
		this.query = query;
	}
	
	public Query getQuery()
	{
		return query;
	}
}
