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

/** Describes a presence stanza */
public class PresenceStanza extends XMPPStanza
{

	/** The X element contained by the presence stanza (if any) */
	private XElement xElement = null;

	/** Constructs a PresenceStanza */
	public PresenceStanza()
	{
		super(PRESENCE_STANZA_TYPE);
	}

	@Override
	public String toString()
	{
		return "[PRESENCE] id=" + getAttribute("id") + " to="
				+ getAttribute("to") + " x " + xElement + " status "
				+ getData("status");
	}

	/**
	 * Add an x element to a presence stanza
	 * 
	 * @param xElement
	 *            the x element to be added
	 */
	public void addXElement(XElement xElement)
	{
		this.xElement = xElement;
	}

	/**
	 * Obtain the x element inside the presence stanza
	 * 
	 * @return the x element
	 */
	public XElement getXElement()
	{
		return xElement;
	}
}
