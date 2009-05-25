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
package topchat.server.protocol.xmpp.stream.error;

/**
 * Describes the XML tag that defines an error
 * 
 * Syntax:
 * 
 * <stream:error>
 * 	<defined-condition xmlns='urn:ietf:params:xml:ns:xmpp-streams'/>
 * 	<text xmlns='urn:ietf:params:xml:ns:xmpp-streams'
 *  	     xml:lang='langcode'>
 *   	OPTIONAL descriptive text
 * 	</text>
 * 	[OPTIONAL application-specific condition element]
 * </stream:error>
 */
public class StreamError {

}
