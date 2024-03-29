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
package topchat.server.protocol.xmpp.stream.element;

/**
 * Constants defining the types of stream elements
 */
public interface Constants
{
	public static final int EMPTY_TYPE = -1;
	public static final int STARTTLS_TYPE = 0;
	public static final int XMPPSTREAM_TYPE = 1;
	public static final int AUTH_TYPE = 2;
	public static final int FEATURES_TYPE = 3;
	public static final int IQSTANZA_TYPE = 4;
	public static final int MESSAGE_STANZA_TYPE = 5;
	public static final int PRESENCE_STANZA_TYPE = 6;
	public static final int QUERY_TYPE = 7;
	public static final int X_TYPE = 8;
}
