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
 * unsupported-version/>
 * 
 * The value of the 'version' attribute provided by the initiating entity
 * in the stream header specifies a version of XMPP that is not supported by the server; 
 * 
 * the server MAY specify the version(s) it supports in the <text/> element.
 */
public class UnsupportedVersionError extends StreamError {

}
