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


public class XMPPStanza extends StreamElement implements Constants {

	private Map<String, String> attributes = new HashMap<String, String>();
	private Map<String, String> data = new HashMap<String, String>();	
	
	private static Logger logger = Logger.getLogger(XMPPStanza.class);
	
	public XMPPStanza(int type) {
		super(type);		
	}

	public void addAttribute(String name, String value)
	{
		logger.debug("add attribute : " + name + " : " + value);
		attributes.put(name, value);
	}
	
	public void addData(String name, String value)
	{
		logger.debug("add data : " + name + " : " + value);
		data.put(name, value);
	}
	
	public String getAttribute(String name)
	{	
		return attributes.get(name);
	}
	
	public String getData(String name)
	{		
		return data.get(name);
	}	
	
	@Override
	public String toString()
	{
		return "[Stanza]";
	}
}
