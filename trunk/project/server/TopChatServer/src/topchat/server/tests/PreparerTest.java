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
package topchat.server.tests;

import java.util.Vector;

import topchat.server.protocol.xmpp.stream.element.Features;
import topchat.server.protocol.xmpp.stream.element.StreamElement;
import topchat.server.protocol.xmpp.stream.element.XMPPStream;
import topchat.server.protocol.xmpp.stream.parser.Parser;
import topchat.server.protocol.xmpp.stream.parser.Preparer;
import junit.framework.TestCase;

public class PreparerTest  extends TestCase {
		
	@Override
	protected void setUp() { 		
	}
	
	/**
	 * Test writing correct xml as start of features message
	 * @throws Exception
	 */
	public void testPrepareFeatures() throws Exception {
		
		setName("Testing if features is correctly created");
		
		Features ft = new Features(true);
		
		String result = Preparer.prepareFeatures(ft);		
		
		Vector<StreamElement> streamElements = Parser.parse(result);
		
		for (StreamElement element : streamElements)
		{
			if (element.isFeatures())
			{
				Features newFt = (Features) element;
		
				assertEquals("use of tls not detected", true,  newFt.usesTLS() );
			}
		}
	}		
	
	/**
	 * Test writing correct xml as start of features message
	 * @throws Exception
	 */
	public void testPrepareFeaturesSASL() throws Exception {
		
		setName("Testing if features is correctly created");
		
		Features ft = new Features(false, true);
		
		String result = Preparer.prepareFeatures(ft);		
		
		Vector<StreamElement> streamElements = Parser.parse(result);
		
		for (StreamElement element : streamElements)
		{
			if (element.isFeatures())
			{
				Features newFt = (Features) element;
		
				assertEquals("use of sals not detected", true,  newFt.usesSASL() );
			}
		}
	}	
	
	
	/**
	 * Test writing correct xml as start of features message
	 * @throws Exception
	 */
	public void testPrepareFeaturesBind() throws Exception {
		
		setName("Testing if features is correctly created");
		
		Features ft = new Features(false, false, true);
		
		String result = Preparer.prepareFeatures(ft);		
		
		Vector<StreamElement> streamElements = Parser.parse(result);
		
		for (StreamElement element : streamElements)
		{
			if (element.isFeatures())
			{
				@SuppressWarnings("unused")
				Features newFt = (Features) element;
			}
		}
	}	
		
	
	/**
	 * Test writing correct xml as start of 'proceed' message
	 * @throws Exception
	 */
	public void testPrepareProceed() throws Exception {
		
		setName("Testing if 'proceed' is correctly created");
						
		String result = Preparer.prepareProceed();
		
		assertEquals("<proceed xmlns=\"urn:ietf:params:xml:ns:xmpp-tls\">" +
				"</proceed>", result);
	}
	
	/**
	 * Test writing correct xml as tls failure
	 * @throws Exception
	 */
	public void testPrepareTLSFailure() throws Exception {
		
		setName("Testing if tls failure is correctly created");
						
		String result = Preparer.prepareTLSFailure();
		
		assertEquals("<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-tls\">" +
				"</failure></stream:stream>", result);
	}	
	
	/**
	 * Test writing correct xml as start of stream message
	 * @throws Exception
	 */
	public void testPrepareStreamStart() throws Exception {
		
		setName("Testing if stream start is correctly created");
		
		XMPPStream stream = new XMPPStream(null, "example.com", "someid", null, "1.0");
		
		String result = Preparer.prepareStreamStart(stream);		
		
		Vector<StreamElement> streamElements = Parser.parse(result);
		
		for (StreamElement element : streamElements)
		{
			if (element.isXMPPStream())
			{
				XMPPStream newStream = (XMPPStream) element;
		
				assertEquals("'version' incorrectly set", "1.0",  newStream.getVersion() );
				assertEquals("'to' incorrectly set ", null, newStream.getTo());
				assertEquals("'from' incorrectly set ", "example.com", newStream.getFrom());
				assertEquals("'id' incorrectly set ", "someid", newStream.getId());
			}
		}		
	}	
}
