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
package topchat.server.protocol.xmpp.stream.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;


public class XMPPParser {

	private static Logger logger = Logger.getLogger(XMPPParser.class);
	
	XMLInputFactory factory;
	
	public XMPPParser()
	{		
		factory = XMLInputFactory.newInstance();		
	}
	
	public void parseStreamStart(String msg)
	{
		InputStream is = null;
		try 
		{
			is = new ByteArrayInputStream(msg.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		XMLEventReader reader = null;
		try 
		{
			reader = factory.createXMLEventReader(is);
		} catch (XMLStreamException e) {			
			e.printStackTrace();
		}
		
		int count = 0;
		while (reader.hasNext(  )) 
		{
		    XMLEvent event = null;
			try {
				event = reader.nextEvent(  );
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
			}
		    if (event.isStartElement()) 
		    {
		    	logger.debug("start "  + event.toString());
		    	logger.debug("name " + ((StartElement)event).getName());
		    	
		    	
	        } else {
		        //processOther(event);
		    	logger.debug("not start" + event.toString());
		    }
		}

		
	//	logger.info(parser.getLocalName());
	}
}
