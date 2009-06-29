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
 * Describes an auth element
 */
public class XMPPAuth extends StreamElement implements Constants
{

	/** the mechanism used for authentication */
	public String mechanism;
	/** the initial challenge sent */
	public String initialChallenge;

	/**
	 * Constructs an XMPPAuth element
	 * 
	 * @param mechanism
	 *            the authentication mechanism
	 * @param initialChallenge
	 *            the initial challenge
	 */
	public XMPPAuth(String mechanism, String initialChallenge)
	{
		super(AUTH_TYPE);
		this.mechanism = mechanism;
		this.initialChallenge = initialChallenge;
	}
}
