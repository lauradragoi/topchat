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
public class JID {
	
	public String jid = null;
	
	public JID(String address) throws IllegalArgumentException
	{
		this.jid = address;
		
		if (! isValid (address) )
		{
			throw new IllegalArgumentException("Invalid address");
		}
		
		
	}
	
	private boolean isValid(String address)
	{
		return false;
	}
}
