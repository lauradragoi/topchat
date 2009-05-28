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

import topchat.server.protocol.xmpp.stream.parser.XMPPParser;
import junit.framework.TestCase;

public class ParserTest extends TestCase {
	
	XMPPParser parser = null;
	
	@Override
	protected void setUp() { 
		parser = new XMPPParser();
	}
	
	public void testParseStreamStart() throws Exception {
		parser.parseStreamStart("<stream:stream " + 
								"to='example.com' " + 
								"xmlns='jabber:client' " + 
								"xmlns:stream='http://etherx.jabber.org/streams' " + 
								"version='1.0'>");
	}
}
