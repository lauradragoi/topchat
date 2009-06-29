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
 * Describes the features stream element
 */
public class Features extends StreamElement implements Constants
{
	/** Flag indicating if TLS is used */
	private boolean useOfTLS = false;
	/** Flag indicating if SASL is used */
	private boolean useOfSASL = false;
	/** Flag indicating if MD5 is used */
	private boolean useMD5 = false;
	/** Flag indicating if PLAIN is used */
	private boolean usePlain = false;
	/** Flag indicating if resource binding is offered */
	private boolean offerBinding = false;
	/** Flag indicating if session establishment is supported */
	private boolean sessionSupported = false;

	/**
	 * Constructs a Features element
	 * 
	 * @param useTLS
	 *            flag indicating if TLS is used
	 */
	public Features(boolean useTLS)
	{
		super(FEATURES_TYPE);
		useOfTLS = useTLS;
	}

	/**
	 * Constructs a Features element
	 * 
	 * @param useTLS
	 *            flag indicating if TLS is used
	 * @param useSASL
	 *            flag indicating if SASL is used
	 */
	public Features(boolean useTLS, boolean useSASL)
	{
		super(FEATURES_TYPE);
		useOfTLS = useTLS;
		useOfSASL = useSASL;

		if (useSASL)
		{
			useMD5 = false;
			usePlain = true;
		}
	}

	/**
	 * Constructs a Features element
	 * 
	 * @param useTLS
	 *            flag indicating if TLS is used
	 * @param useSASL
	 *            flag indicating if SASL is used
	 * @param offerBinding
	 *            flag indicating if resource binding is offered
	 */
	public Features(boolean useTLS, boolean useSASL, boolean offerBinding)
	{
		super(FEATURES_TYPE);
		useOfTLS = useTLS;
		useOfSASL = useSASL;
		this.offerBinding = offerBinding;

		if (useSASL)
		{
			useMD5 = true;
			usePlain = true;
		}

		if (offerBinding)
		{
			sessionSupported = true;
		}
	}

	/**
	 * Check if TLS used
	 * 
	 * @return true if TLS is used, false otherwise
	 */
	public boolean usesTLS()
	{
		return useOfTLS;
	}

	/**
	 * Check if SASL is used
	 * 
	 * @return true if SASL is used, false otherwise
	 */
	public boolean usesSASL()
	{
		return useOfSASL;
	}

	/**
	 * Check if MD5 is used for SASL
	 * 
	 * @return true if MD5 is used, false otherwise
	 */
	public boolean usesMD5()
	{
		return useMD5;
	}

	/**
	 * Check if PLAIN is used for SASL
	 * 
	 * @return true if PLAIN is used, false otherwise
	 */
	public boolean usesPlain()
	{
		return usePlain;
	}

	/**
	 * Check if resource binding is used
	 * 
	 * @return true if resource binding is offered.
	 */
	public boolean offersBinding()
	{
		return offerBinding;
	}

	/**
	 * Check if session establishment is supported
	 * 
	 * @return true if session establishment is supported
	 */
	public boolean sessionSupported()
	{
		return sessionSupported;
	}
}
