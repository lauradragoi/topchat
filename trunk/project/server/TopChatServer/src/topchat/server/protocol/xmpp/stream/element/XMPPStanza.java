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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Describes an XMPP stream element
 */
public class XMPPStanza extends StreamElement implements Constants
{

	/** A map of attribute names and values */
	private Map<String, String> attributes = new HashMap<String, String>();
	/** A map of data names and values */
	private Map<String, String> data = new HashMap<String, String>();

	/** The namespace of the stanza */
	protected String namespace = null;

	private static Logger logger = Logger.getLogger(XMPPStanza.class);

	/**
	 * Constructs an XMPPStanza
	 * 
	 * @param type
	 *            the type of the XMPPStanza
	 */
	public XMPPStanza(int type)
	{
		super(type);
	}

	/**
	 * Adds an attribute
	 * 
	 * @param name
	 *            the name of the attribute
	 * @param value
	 *            the value of the attribute
	 */
	public void addAttribute(String name, String value)
	{
		logger.debug("add attribute : " + name + " : " + value);
		attributes.put(name, value);
	}

	/**
	 * Adds data
	 * 
	 * @param name
	 *            the name of the data
	 * @param value
	 *            the value of the data
	 */
	public void addData(String name, String value)
	{
		logger.debug("add data : " + name + " : " + value);
		data.put(name, value);
	}

	/**
	 * Obtain the value of an attribute
	 * 
	 * @param name
	 *            the name of the attribute
	 * @return the value of the attribute
	 */
	public String getAttribute(String name)
	{
		return attributes.get(name);
	}

	/**
	 * Obtain the value of a data element
	 * 
	 * @param name
	 *            the name of the data
	 * @return the value of the data
	 */
	public String getData(String name)
	{
		return data.get(name);
	}

	/**
	 * Add a namespace to a query element
	 * 
	 * @param namespace
	 *            the namespace to be added
	 */
	public void addNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	/**
	 * Retrieve the namespace of the query
	 * 
	 * @return the namespace
	 */
	public String getNamespace()
	{
		return namespace;
	}

	@Override
	public String toString()
	{
		return "[Stanza]";
	}
}
