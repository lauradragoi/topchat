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

import org.junit.Test;

import topchat.server.protocol.xmpp.stream.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.XMPPParser;
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
		
		XMPPStream stream = (XMPPStream) XMPPParser.parse("<stream:stream " + 
								"to='example.com' " + 
								"xmlns='jabber:client' " + 
								"xmlns:stream='http://etherx.jabber.org/streams' " + 
								"version='1.0'>");
		
		assertEquals("'version' incorrectly retrieved", "1.0", stream.getVersion());
		assertEquals("'to' incorrectly retrieved ", "example.com", stream.getTo());		
	}
	
	/**
	 * Test stream start received from pidgin
	 * @throws Exception
	 */
	public void testParseStreamStart2() throws Exception {
		
		setName("Testing correct parsing of stream start: pidgin version");
		
		XMPPStream stream = (XMPPStream) XMPPParser.parse("<?xml version='1.0' ?>" +
				" <stream:stream" +
				" to='me.my'" +
				" xmlns='jabber:client'" +
				" xmlns:stream='http://etherx.jabber.org/streams'" +
				" version='1.0'>");
		
		assertEquals("'version' incorrectly retrieved", "1.0",  stream.getVersion() );
		assertEquals("'to' incorrectly retrieved ", "me.my", stream.getTo());
	}
	
	/**
	 * Test writing correct xml as start of stream message
	 * @throws Exception
	 */
	public void testPrepareStreamStart() throws Exception {
		
		setName("Testing if stream start is correctly created");
		
		XMPPStream stream = new XMPPStream(null, "example.com", "someid", null, "1.0");
		
		String result = XMPPParser.prepareStreamStart(stream);		
		
		XMPPStream newStream = (XMPPStream) XMPPParser.parse(result);
		
		assertEquals("'version' incorrectly set", "1.0",  newStream.getVersion() );
		assertEquals("'to' incorrectly set ", null, newStream.getTo());
		assertEquals("'from' incorrectly set ", "example.com", newStream.getFrom());
		assertEquals("'id' incorrectly set ", "someid", newStream.getId());
	}
	
	/**
	 * Test correct parsing when end of stream is received
	 * @throws Exception
	 */
	public void testParseEndStream() throws Exception {
		
		setName("Testing correct parsing of end stream");
		
		XMPPParser.parse("</stream:stream>");
	}
	
	/**
	 * Test parsing of received message
	 * @throws Exception
	 */
	public void testParseMessage() throws Exception {
		
		setName("Testing correct parsing of message");
		
		XMPPParser.parse("<message from='juliet@example.com'" +
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
		
		XMPPParser.parse(" <iq to='bar'> " +
						 " <query/>" +
						 " </iq>");
	}
	
	/**
	 * Test parsing of received presence
	 * @throws Exception
	 */
	public void testParsePresence() throws Exception {
		
		setName("Testing correct parsing of presence");
		
		XMPPParser.parse("<presence>" +
						 "  <show/> " +
						 " </presence>" );
	}	
		
	/**
	 * Test parsing of an error message
	 */
	public void testParseError() throws Exception
	{
		setName("Testing correct parsing of an error message");
		
		XMPPParser.parse("<stream:error>" +
							 " <xml-not-well-formed" +
							 " xmlns='urn:ietf:params:xml:ns:xmpp-streams'/>" +
							 " </stream:error>");		
	}
	
	
	/**
	 * Test parsing of feature message
	 */
	public void testParseFeatures() throws Exception
	{
		setName("Testing correct parsing of a feature message");
		
		XMPPParser.parse("<stream:features>" +
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
		
		XMPPParser.parse("<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>");
	}
	
	/**
	 * Test parsing of proceed sent by server
	 */
	public void testParseProceed() throws Exception
	{
		setName("Testing correct parsing of proceed message");
		
		XMPPParser.parse("<proceed xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>");
	}
	
	/**
	 * Test parsing TLS negotiation failure
	 */
	public void testParseTLSFailure() throws Exception
	{
		setName("Testing correct parsing of TLS failure");
		
		XMPPParser.parse("<failure xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>" +
						 "</stream:stream>");
	}
	
	
	/**
	 * Test parsing of a message containing an error
	 */	
	@Test(expected=Exception.class)
	public void testParseMessageWithError() {
		
		setName("Testing correct parsing of message containing badly formed xml");
		
		try {
			XMPPParser.parse("<message xml:lang='en'>" +
							 " <body>Bad XML, no closing body tag!" +
							 " </message>" );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
