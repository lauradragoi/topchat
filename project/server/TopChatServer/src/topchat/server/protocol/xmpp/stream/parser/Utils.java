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

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * Contains useful methods for parsing
 */
public class Utils {

	/**
	 * Method used to add a start element to an event writer
	 * @param eventFactory
	 * @param writer
	 * @param prefix
	 * @param namespaceUri
	 * @param localName
	 */
	static void addStartElement(XMLEventFactory eventFactory, XMLEventWriter writer,
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
	
	/**
	 * Method used to add an end element to an event writer
	 * @param eventFactory
	 * @param writer
	 * @param prefix
	 * @param namespaceUri
	 * @param localName
	 */
	static void addEndElement(XMLEventFactory eventFactory, XMLEventWriter writer,
			String prefix, String namespaceUri, String localName)
	{
	    XMLEvent event = eventFactory.createEndElement(prefix, 
	            namespaceUri, localName);
	    try {
			writer.add(event);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * Method used to add a namespace to an event writer
	 * @param eventFactory
	 * @param writer
	 * @param prefix
	 * @param namespaceUri
	 */
	static void addNamespace(XMLEventFactory eventFactory, XMLEventWriter writer,
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

	/**
	 * Method used to add an attribute to an event writer
	 * @param eventFactory
	 * @param writer
	 * @param name
	 * @param value
	 */
	static void addAttribute(XMLEventFactory eventFactory, XMLEventWriter writer,
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

	/**
	 * Method used to finish writing to an event writer
	 * @param writer
	 */
	static void endWrite(XMLEventWriter writer)
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
	 * Method used for creating an event reader for a message that will be parsed
	 * @param msg
	 * @return
	 */
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

	/**
	 * Method used for creating an event writer using the provided output stream
	 * @param baos
	 * @return
	 */
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

}
