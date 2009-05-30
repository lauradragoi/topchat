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

import java.io.ByteArrayOutputStream;
import java.text.ParsePosition;
import java.util.Iterator;


import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
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
			
	/**
	 * Parse the message sent by the client to initiate the communication
	 * @param msg the message received through the stream
	 */
	@SuppressWarnings("unchecked")
	public static XMPPStream parseStreamStart(String msg) throws Exception
	{
		XMLEventReader reader = ParserUtils.createReader(msg);
		
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
				logger.fatal("Exception when reading next event", e);
				
				throw new Exception("Exception on parsing");								
			}
			
		    if (event.isStartElement()) 
		    {		  
		    	StartElement startElement = ((StartElement) event);
		    	
		    	logger.debug("start "  + event.toString());
		    	logger.debug("name " + startElement.getName());		
		    	logger.debug("name local" + startElement.getName().getLocalPart());		    			    	
		    	logger.debug("as start: " + startElement.asStartElement());
		    	
		    	if ("stream".equals(startElement.getName().getLocalPart()))
		    	{			    	
		    			streamProcessed = true;
		    	
				    	// walk through start element attributes
				    	for (Iterator it = startElement.getAttributes(); it.hasNext();) 
				    	{
							Attribute attribute = (Attribute) it.next();
							logger.debug("attribute name: " + attribute.getName());
		
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
		    	}
	        } else {		        
		    	logger.debug("not start" + event.toString());
		    }
		}		
		
		return new XMPPStream(to, from, id, lang, version);
	}
	
	/**
	 * Generic parsing function
	 * @param msg
	 */
	public static void parse(String msg) throws Exception
	{
		// hack: this could be the end stream tag which cannot be parsed properly by stax
		if (isEndStream(msg))
		{
			logger.info("End of stream");
			return;
		}
		
		// hack: stream:error throws unbound prefix error, remove prefix as workaround
		if (isErrorMessage(msg))
		{
			logger.info("Error message sent on stream");
			parseErrorMessage(msg);
			return;
		}
				
		XMLEventReader reader = ParserUtils.createReader(msg);
				
		while (reader.hasNext(  )) 
		{
		    XMLEvent event = null;
		    
			try 
			{
				event = reader.nextEvent();
				
			} catch (XMLStreamException e) {
				logger.fatal("Exception when reading next event", e);
				
				throw new Exception("Exception on parsing");	
			}
			
		    if (event.isStartElement()) 
		    {		  
		    	StartElement startElement = ((StartElement) event);
		    	logger.debug("start: " + startElement.toString());
		    	
		    	logger.debug("loca " + startElement.getName().getLocalPart().toString());
		    	
		    	if ("message".equals(startElement.getName().getLocalPart().toString()))
		    		parseMessage(startElement, reader);
		    	
		    	if ("iq".equals(startElement.getName().getLocalPart().toString()))
	    			parseIq(startElement, reader);
		    	
		    	if ("presence".equals(startElement.getName().getLocalPart().toString()))
	    			parsePresence(startElement, reader);
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}
	}
	
	/**
	 * Method that parses a message stanza
	 * @param start
	 * @param reader
	 */
	private static void parseMessage(StartElement start, XMLEventReader reader ) throws Exception
	{
		boolean endMessage = false;
		
		while (reader.hasNext(  ) && !endMessage) 
		{
		    XMLEvent event = null;
		    
			try 
			{
				event = reader.nextEvent();
				
			} catch (XMLStreamException e) {
				logger.fatal("Exception when reading next event", e);
				
				throw new Exception("Exception on parsing");		
			}
			
		    if (event.isStartElement()) 
		    {		  
		    	StartElement startElement = ((StartElement) event);
		    	logger.debug("start: " + startElement.toString());		    			    	
		    }
		    else if (event.isEndElement())
		    {
		    	EndElement endElement = (EndElement) event;
		    	
		    	if ("message".equals(endElement.getName().getLocalPart().toString()))
		    		endMessage = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
	}
	
	/**
	 * Method that parses an Iq stanza
	 * @param start
	 * @param reader
	 */
	private static void parseIq(StartElement start, XMLEventReader reader ) throws Exception
	{
		boolean endIq = false;
		
		while (reader.hasNext(  ) && !endIq) 
		{
		    XMLEvent event = null;
		    
			try 
			{
				event = reader.nextEvent();
				
			} catch (XMLStreamException e) {
				logger.fatal("Exception when reading next event", e);
				
				throw new Exception("Exception on parsing");				
			}
			
		    if (event.isStartElement()) 
		    {		  
		    	StartElement startElement = ((StartElement) event);
		    	logger.debug("start: " + startElement.toString());		    			    	
		    }
		    else if (event.isEndElement())
		    {
		    	EndElement endElement = (EndElement) event;
		    	
		    	if ("iq".equals(endElement.getName().getLocalPart().toString()))
		    		endIq = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
	}
	
	/**
	 * Method that parses a presence stanza
	 * @param start
	 * @param reader
	 */
	private static void parsePresence(StartElement start, XMLEventReader reader ) throws Exception
	{
		boolean endPresence = false;
		
		while (reader.hasNext(  ) && !endPresence) 
		{
		    XMLEvent event = null;
		    
			try 
			{
				event = reader.nextEvent();
				
			} catch (XMLStreamException e) {
				logger.fatal("Exception when reading next event", e);
				
				throw new Exception("Exception on parsing");				
			}
			
		    if (event.isStartElement()) 
		    {		  
		    	StartElement startElement = ((StartElement) event);
		    	logger.debug("start: " + startElement.toString());		    			    	
		    }
		    else if (event.isEndElement())
		    {
		    	EndElement endElement = (EndElement) event;
		    	
		    	if ("presence".equals(endElement.getName().getLocalPart().toString()))
		    		endPresence = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
	}	
	
	
	/**
	 * Method returning true if end of stream was reached
	 * @param msg
	 * @return
	 */
	private static boolean isEndStream(String msg)
	{
		return "</stream:stream>".equals(msg);
	}
	
	/**
	 * Method returning true if the argument is an error message
	 * @param msg
	 * @return
	 */
	private static boolean isErrorMessage(String msg)
	{
		return msg.startsWith("<stream:error>");
	}
	
	
	/**
	 * Method parsing an error message
	 * @param msg
	 * @return
	 */
	private static void parseErrorMessage(String msg) throws Exception
	{
		logger.debug("initial error message : " + msg);
		msg = msg.replace("<stream:", "<");
		msg = msg.replace("</stream:", "</");
		
		logger.debug("proper error message : " + msg);
		
		parseProperError(msg);		
	}
	
	/**
	 * Method that parses a properly formed error message
	 * @param start
	 * @param reader
	 */
	private static void parseProperError(String msg) throws Exception
	{
		XMLEventReader reader = ParserUtils.createReader(msg);
		
		boolean endError = false;
		
		while (reader.hasNext(  ) && !endError) 
		{
		    XMLEvent event = null;
		    
			try 
			{
				event = reader.nextEvent();
				
			} catch (XMLStreamException e) {
				logger.fatal("Exception when reading next event", e);
				
				throw new Exception("Exception on parsing");				
			}
			
		    if (event.isStartElement()) 
		    {		  
		    	StartElement startElement = ((StartElement) event);
		    	logger.debug("start: " + startElement.toString());		    			    	
		    }
		    else if (event.isEndElement())
		    {
		    	EndElement endElement = (EndElement) event;
		    	
		    	if ("error".equals(endElement.getName().getLocalPart().toString()))
		    		endError = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
	}	
	
	
	/**
	 * Create the corresponding xml message from the XMPPStream object
	 * @param stream
	 * @return
	 */
	public static String prepareStreamStart(XMPPStream stream)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEventWriter writer = ParserUtils.createWriter(baos);
		
		XMLEventFactory  eventFactory = XMLEventFactory.newInstance();
		
		ParserUtils.addStartElement(eventFactory, writer, 
						"stream", "http://etherx.jabber.org/streams", "stream");		

		ParserUtils.addNamespace(eventFactory, writer, "stream", "http://etherx.jabber.org/streams");
				
		ParserUtils.addAttribute(eventFactory, writer, "from", stream.getFrom());
		ParserUtils.addAttribute(eventFactory, writer, "id", stream.getId());
		ParserUtils.addAttribute(eventFactory, writer, "version", stream.getVersion());
		
		ParserUtils.endWrite(writer);
		
		String result = new String(baos.toByteArray());
		
		// hack for ending tag without adding end tag (why does stax make me do this?)
		result = result.concat(">");
		
		logger.debug(result);
		
		return result; 
	}
	

}
