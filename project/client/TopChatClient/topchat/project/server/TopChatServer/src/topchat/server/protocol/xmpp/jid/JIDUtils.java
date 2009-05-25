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
package topchat.server.protocol.xmpp.jid;

/**
 * Class describing a Jabber ID
 *
 * The format is:
 *   jid             = [ node "@" ] domain [ "/" resource ]
 *	 domain          = fqdn / address-literal
 *   fqdn            = (sub-domain 1*("." sub-domain))
 *   sub-domain      = (internationalized domain label)
 *   address-literal = IPv4address / IPv6address
 *
 */
public class JIDUtils {
			
	public static boolean isValid(String address)
	{
		//TODO
		return true;
	}
	
	public static String parseDomain(String address)
	{
		//TODO
		return null;
	}
	
	public static String parseNode(String address)
	{
		//TODO
		return null;
	}
	
	public static String parseResource(String address)
	{
		//TODO
		return null;
	}
}
