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
package topchat.server.protocol.xmpp;

/**
 * Constants used by the XMPP protocol
 */
public interface XMPPConstants 
{
	/** Listening port used by the server */
	public static final int XMPP_DEFAULT_SERVER_PORT = 5222;
	
	/** The default number of threads in the executor pool */
	public static final int DEFAULT_EXECUTOR_THREADS = 5;
}
