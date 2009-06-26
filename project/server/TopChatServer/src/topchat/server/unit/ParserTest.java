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
package topchat.server.unit;

import java.util.Vector;

import org.junit.Test;

import topchat.server.protocol.xmpp.stream.element.StreamElement;
import topchat.server.protocol.xmpp.stream.element.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.Parser;
import junit.framework.TestCase;

/**
 * Contains methods used for testing the XMPPParser implementation
 */
public class ParserTest extends TestCase {
		
	@Override
	protected void setUp() { 		
	}
	
	/**
	 * Test stream start received from topchatclient
	 * @throws Exception
	 */
	public void testParseStreamStart() throws Exception {
		
		setName("Testing correct parsing of stream start: topchatclient version");
		
		Vector<StreamElement> streamElements = Parser.parse("<stream:stream " + 
				"to='example.com' " + 
				"xmlns='jabber:client' " + 
				"xmlns:stream='http://etherx.jabber.org/streams' " + 
				"version='1.0'>");
		
		for (StreamElement element : streamElements)
		{
			if (element.isXMPPStream())
			{
				XMPPStream stream = (XMPPStream) element;
		
				assertEquals("'version' incorrectly retrieved", "1.0", stream.getVersion());
				assertEquals("'to' incorrectly retrieved ", "example.com", stream.getTo());	
			}
		}		
	}
	
	/**
	 * Test stream start received from pidgin
	 * @throws Exception
	 */
	public void testParseStreamStart2() throws Exception {
		
		setName("Testing correct parsing of stream start: pidgin version");
		
		Vector<StreamElement> streamElements = Parser.parse("<?xml version='1.0' ?>" +
				" <stream:stream" +
				" to='me.my'" +
				" xmlns='jabber:client'" +
				" xmlns:stream='http://etherx.jabber.org/streams'" +
				" version='1.0'>");
		
		for (StreamElement element : streamElements)
		{
			if (element.isXMPPStream())
			{
				XMPPStream stream = (XMPPStream) element;
		
				assertEquals("'version' incorrectly retrieved", "1.0",  stream.getVersion() );
				assertEquals("'to' incorrectly retrieved ", "me.my", stream.getTo());
			}
		}	
	}
		
	/**
	 * Test correct parsing when end of stream is received
	 * @throws Exception
	 */
	public void testParseEndStream() throws Exception {
		
		setName("Testing correct parsing of end stream");
		
		Parser.parse("</stream:stream>");
	}
	
	/**
	 * Test parsing of received message
	 * @throws Exception
	 */
	public void testParseMessage() throws Exception {
		
		setName("Testing correct parsing of message");
		
		Parser.parse("<message from='juliet@example.com'" +
						 " to='romeo@example.net'" +
						 " xml:lang='en'>" +
						 " <body>Art thou not Romeo, and a Montague?</body>" +
						 " </message>");
	}
	
	/**
	 * Test parsing of received iq
	 * @throws Exception
	 */
	public void testParseIq() throws Exception {
		
		setName("Testing correct parsing of IQ");
		
		Parser.parse(" <iq to='bar'> " +
						 " <query/>" +
						 " </iq>");
	}
	
	/**
	 * Test parsing of received presence
	 * @throws Exception
	 */
	public void testParsePresence() throws Exception {
		
		setName("Testing correct parsing of presence");
		
		Parser.parse("<presence>" +
						 "  <show/> " +
						 " </presence>" );
	}	
		
	/**
	 * Test parsing of an error message
	 */
	public void testParseError() throws Exception
	{
		setName("Testing correct parsing of an error message");
		
		Parser.parse("<stream:error>" +
							 " <xml-not-well-formed" +
							 " xmlns='urn:ietf:params:xml:ns:xmpp-streams'/>" +
							 " </stream:error>");		
	}
	
	
	/**
	 * Test parsing of feature message
	 * 
	 * This is sent by server. 
	 */
	public void testParseFeatures() throws Exception
	{
		setName("Testing correct parsing of a feature message");
		
		Parser.parse("<stream:features>" +
						 "<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'>" +
						 "<required/>" +
						 "</starttls>" +
						 "<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
						 "<mechanism>DIGEST-MD5</mechanism>" + 
						 "<mechanism>PLAIN</mechanism>" +
						 "</mechanisms>" +
						 "</stream:features>");	
	}
	
	/**
	 * Test parsing of starttls from client
	 */
	public void testParseStarttls() throws Exception
	{
		setName("Testing correct parsing of starttls message");
		
		Parser.parse("<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>");
	}
	
	/**
	 * Test parsing of proceed sent by server
	 * 
	 * This is sent by server.
	 */
	public void testParseProceed() throws Exception
	{
		setName("Testing correct parsing of proceed message");
		
		Parser.parse("<proceed xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>");
	}
	
	/**
	 * Test parsing TLS negotiation failure
	 * 
	 * Sent by server.
	 */
	public void testParseTLSFailure() throws Exception
	{
		setName("Testing correct parsing of TLS failure");
		
		Parser.parse("<failure xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>" +
						 "</stream:stream>");
	}
	
	
	/**
	 * Test parsing of a message containing an error
	 */	
	@Test(expected=Exception.class)
	public void testParseMessageWithError() {
		
		setName("Testing correct parsing of message containing badly formed xml");
		
		try {
			Parser.parse("<message xml:lang='en'>" +
							 " <body>Bad XML, no closing body tag!" +
							 " </message>" );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
