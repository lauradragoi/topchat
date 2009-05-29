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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;


import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import topchat.server.protocol.xmpp.stream.XMPPStream;

/**
 * Contains methods used for parsing messages sent by the client
 * and methods for writing messages to the client.
 * 
 * Uses StaX API included in JAVA SE 6 in javax.xml.stream
 *
 */
public class XMPPParser {

	private static Logger logger = Logger.getLogger(XMPPParser.class);
		
	
	public static XMLEventReader createReader(String msg)
	{
		XMLInputFactory factory = XMLInputFactory.newInstance();	
		
		// obtain input stream
		InputStream is = null;
		try 
		{
			is = new ByteArrayInputStream(msg.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// create event reader from input stream
		XMLEventReader reader = null;
		try 
		{
			reader = factory.createXMLEventReader(is);
		} catch (XMLStreamException e) {			
			e.printStackTrace();
		}
		
		return reader;
	}
	
	public static XMLEventWriter createWriter(ByteArrayOutputStream baos)
	{
		XMLOutputFactory factory      = XMLOutputFactory.newInstance();
		
	    XMLEventWriter writer = null;
		try {
			writer = factory.createXMLEventWriter(baos);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		return writer;
	}
	
	/**
	 * Parse the message sent by the client to initiate the communication
	 * @param msg the message received through the stream
	 */
	@SuppressWarnings("unchecked")
	public static XMPPStream parseStreamStart(String msg)
	{
		XMLEventReader reader = createReader(msg);
		
		String to = null;
		String version = null;
		String id = null;
		String lang = null;
		String from = null;
		
	
		// looking from 'stream' start element only
		boolean streamProcessed = false;
	
		while (reader.hasNext(  ) && !streamProcessed) 
		{
		    XMLEvent event = null;
		    
			try 
			{
				event = reader.nextEvent(  );
				
			} catch (XMLStreamException e) {
				logger.warn("Exception when reading next event");
				//e.printStackTrace();				
			}
			
		    if (event.isStartElement()) 
		    {		  
		    	StartElement startElement = ((StartElement) event);
		    	
		    	logger.debug("start "  + event.toString());
		    	logger.debug("name " + startElement.getName());		
		    	logger.debug("name local" + startElement.getName().getLocalPart());		    			    	
		    	logger.debug("as start: " + startElement.asStartElement());
		    	
		    	if ("stream".equals(startElement.getName().getLocalPart()))
		    			streamProcessed = true;
		    	
		    	// walk through start element attributes
		    	for (Iterator it = startElement.getAttributes(); it.hasNext();) 
		    	{
					Attribute attribute = (Attribute) it.next();
					logger.debug("attribute name: x" + attribute.getName() + "x");

					logger.debug("attribute value: " + attribute.getValue());
				
					// set 'to' field
					if ("to".equals( attribute.getName().toString()) )
					{
						to = attribute.getValue();
						logger.debug("set to " + to);
					}
					
					// set 'version' field
					if ("version".equals( attribute.getName().toString()) )
					{
						version = attribute.getValue();
						logger.debug("set version " + version);
					}
					
					if ("id".equals( attribute.getName().toString()) )
					{
						id = attribute.getValue();
						logger.debug("set id " + id);
					}		
					
					if ("from".equals( attribute.getName().toString()) )
					{
						from = attribute.getValue();
						logger.debug("set from " + from);
					}	
		    	}
		    	
		    	// walk through start element namespaces
		    	for (Iterator it = startElement.getNamespaces(); it.hasNext();) 
		    	{
		    		Attribute attribute = (Attribute) it.next();
		    		
					logger.debug("namespace name: " + attribute.getName());
					logger.debug("namespace value: " + attribute.getValue());
		    	}		    		
		    	
	        } else {		        
		    	logger.debug("not start" + event.toString());
		    }
		}		
		
		return new XMPPStream(to, from, id, lang, version);
	}
	
	
	private static void addStartElement(XMLEventFactory eventFactory, XMLEventWriter writer,
			String prefix, String namespaceUri, String localName)
	{
	    XMLEvent event = eventFactory.createStartElement(prefix, 
	            namespaceUri, localName);
	    try {
			writer.add(event);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	private static void addNamespace(XMLEventFactory eventFactory, XMLEventWriter writer,
			String prefix, String namespaceUri)
	{
		XMLEvent event = eventFactory.createNamespace(prefix, namespaceUri);
	    try {
			writer.add(event);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void addAttribute(XMLEventFactory eventFactory, XMLEventWriter writer,
			String name, String value)
	{
		XMLEvent event = eventFactory.createAttribute(name, value);
	    try {
			writer.add(event);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void endWrite(XMLEventWriter writer)
	{
		try {
			writer.flush();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			writer.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the corresponding xml tag from the XMPPStream object
	 * @param stream
	 * @return
	 */
	public static String prepareStreamStart(XMPPStream stream)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEventWriter writer = createWriter(baos);
		
		XMLEventFactory  eventFactory = XMLEventFactory.newInstance();
		
		addStartElement(eventFactory, writer, 
						"stream", "http://etherx.jabber.org/streams", "stream");		

		addNamespace(eventFactory, writer, "stream", "http://etherx.jabber.org/streams");

				
		addAttribute(eventFactory, writer, "from", stream.getFrom());
		addAttribute(eventFactory, writer, "id", stream.getId());
		addAttribute(eventFactory, writer, "version", stream.getVersion());


		
		endWrite(writer);
		
		String result = new String(baos.toByteArray());
		
		// hack for end tag
		result = result.concat(">");
		
		logger.info(result);
		
		return result; 
	}
}
