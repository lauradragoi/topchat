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

import java.util.Iterator;


import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import topchat.server.protocol.xmpp.stream.Constants;
import topchat.server.protocol.xmpp.stream.Features;
import topchat.server.protocol.xmpp.stream.StreamElement;
import topchat.server.protocol.xmpp.stream.XMPPStream;

/**
 * Contains methods used for parsing messages sent by the client
 * and methods for writing messages to the client.
 * 
 * Uses StaX API included in JAVA SE 6 in javax.xml.stream
 *
 */
public class Parser implements Constants {

	private static Logger logger = Logger.getLogger(Parser.class);
			
	/**
	 * Generic parsing function
	 * @param msg
	 */
	public static Object parse(String msg) throws Exception
	{
		Object result = null;
		logger.debug(msg);
				
		// hack: if msg contains end of stream StAX signals document not well formed 
		// (obviously because msg contains no start of stream)
		boolean endOfStream = false;
		
		if (endOfStream = containsEndOfStream(msg))
		{
			msg = cleanupEndOfStream(msg); 
		}
		
		logger.debug("after cleanupEndOfStream: " + msg);
				
		// hack: remove 'stream:' prefix due to which unbound prefix error is thrown by StAX
		msg = cleanupStreamPrefix(msg);
		
		logger.debug("after cleanupStreamPrefix: " + msg);
		
		// hack: don't parse empty messages	(end of stream becomes empty after cleanup)	
		if ( msg.trim().length() > 0)
		{
			result = genericParse(msg);
		}
		
		if (endOfStream)
			logger.info("End of stream reached");
		
		return result;		
	}
	
	private static Object genericParse(String msg) throws Exception
	{
		Object result = null;
		
		XMLEventReader reader = Utils.createReader(msg);
		
		logger.debug("parsing : " + msg);
		
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
		    	logger.debug("start element: " + startElement.toString());
		    	
		    	logger.debug("local name: " + startElement.getName().getLocalPart().toString());
		    	
		    	String local = startElement.getName().getLocalPart().toString();
		    	
		    	if ("message".equals(local))
		    		parseMessage(startElement, reader);
		    	
		    	if ("iq".equals(local))
	    			parseIq(startElement, reader);
		    	
		    	if ("presence".equals(local))
	    			parsePresence(startElement, reader);
		    	
		    	if ("error".equals(local))
		    		parseError(startElement, reader);
		    	
		    	if ("features".equals(local))
		    	{
		    		result = parseFeatures(startElement, reader);
		    	}
		    	
		    	if ("starttls".equals(local))
		    	{
		    		result = parseStartTLS(startElement, reader);
		    	}
		    	
		    	if ("proceed".equals(local))
		    		parseProceed(startElement, reader);
		    	
		    	if ("failure".equals(local))
		    		parseFailure(startElement, reader);
		    	
		    	if ("stream".equals(local))
		    	{
		    		result = parseStreamStart(startElement, reader);
		    		
		    		// hack : stop parsing if stream start detected to avoid StAX parsing error 
		    		// due to lack of end of stream in parsed string
		    		break;
		    	}
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
		
		return result;
	}
	
	/**
	 * Method that parses a message stanza
	 * @param start
	 * @param reader
	 */
	private static void parseMessage(StartElement start, XMLEventReader reader ) throws Exception
	{
		boolean end = false;
		
		while (reader.hasNext(  ) && !end) 
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
		    		end = true;
		    	
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
		boolean end = false;
		
		while (reader.hasNext(  ) && !end) 
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
		    		end = true;
		    	
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
		boolean end = false;
		
		while (reader.hasNext(  ) && !end) 
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
		    		end = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
	}	
	
	
	/**
	 * Method that parses an error message
	 * @param start
	 * @param reader
	 */
	private static void parseError(StartElement start, XMLEventReader reader ) throws Exception
	{
		boolean end = false;
		
		while (reader.hasNext(  ) && !end) 
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
		    		end = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
	}
	
	/**
	 * Method that parses a features stanza
	 * @param start
	 * @param reader
	 */
	private static Features parseFeatures(StartElement start, XMLEventReader reader ) throws Exception
	{
		boolean end = false;
		
		boolean usesTLS = false;
		boolean usesSASL = false;
		
		while (reader.hasNext(  ) && !end) 
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
		    	
		    	if ("starttls".equals(startElement.getName().getLocalPart()))
		    		usesTLS = true;
		    	
		    	if ("mechanisms".equals(startElement.getName().getLocalPart()))
		    		usesSASL = true;
		    	
		    }
		    else if (event.isEndElement())
		    {
		    	EndElement endElement = (EndElement) event;
		    	
		    	if ("features".equals(endElement.getName().getLocalPart().toString()))
		    		end = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
		
		return new Features(usesTLS, usesSASL);
	}
	
	/**
	 * Method that parses a starttls
	 * @param start
	 * @param reader
	 */
	private static StreamElement parseStartTLS(StartElement start, XMLEventReader reader ) throws Exception
	{
		boolean end = false;
		
		while (reader.hasNext(  ) && !end) 
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
		    	
		    	if ("starttls".equals(endElement.getName().getLocalPart().toString()))
		    		end = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
		
		return new StreamElement(STARTTLS_TYPE);
	}	
	
	/**
	 * Method that parses proceed
	 * @param start
	 * @param reader
	 */
	private static void parseProceed(StartElement start, XMLEventReader reader ) throws Exception
	{
		boolean end = false;
		
		while (reader.hasNext(  ) && !end) 
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
		    	
		    	if ("proceed".equals(endElement.getName().getLocalPart().toString()))
		    		end = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
	}		
	
	
	/**
	 * Method that parses failure
	 * @param start
	 * @param reader
	 */
	private static void parseFailure(StartElement start, XMLEventReader reader ) throws Exception
	{
		boolean end = false;
		
		while (reader.hasNext(  ) && !end) 
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
		    	
		    	if ("failure".equals(endElement.getName().getLocalPart().toString()))
		    		end = true;
		    	
		    	logger.debug(event.toString());
		    }
		    else
		    {
		    	logger.debug(event.toString());
		    }
		}	
	}		
	
	
	/**
	 * Parse the message sent by the client to initiate the communication
	 */
	@SuppressWarnings("unchecked")
	private static XMPPStream parseStreamStart(StartElement start, XMLEventReader reader ) throws Exception
	{	
		String to = null;
		String version = null;
		String id = null;
		String lang = null;
		String from = null;		
	
  	
		// walk through start element attributes
		for (Iterator it = start.getAttributes(); it.hasNext();) 
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
		    	
    				
		return new XMPPStream(to, from, id, lang, version);
	}
	
	/**
	 * Method returning true if the message contains end of stream info
	 * @param msg
	 * @return
	 */
	private static boolean containsEndOfStream(String msg)
	{
		return msg.contains("</stream:stream>");
	}
	

	/**
	 * Method that removes the stream: prefix
	 * @param msg
	 * @return
	 */
	private static String cleanupStreamPrefix(String msg)
	{
		// do not remove stream prefix if it will not cause parse error
		if (msg.contains("xmlns:stream"))
			return msg;
		
		return msg.replace("stream:", "");
	}	
	
	/**
	 * Method that removes the end of stream info
	 * @param msg
	 * @return
	 */
	private static String cleanupEndOfStream(String msg)
	{
		return msg.replace("</stream:stream>", "");
	}

}
