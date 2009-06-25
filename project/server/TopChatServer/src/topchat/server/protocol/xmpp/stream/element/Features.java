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


public class Features extends StreamElement implements Constants {

	private boolean useOfTLS = false;
	private boolean useOfSASL = false;
	private boolean useMD5 = false;
	private boolean usePlain = false;
	private boolean offerBinding = false;
	private boolean sessionSupported = false;
	
	public Features(boolean useTLS)
	{
		super(FEATURES_TYPE);
		useOfTLS = useTLS;
	}
	
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

	
	public boolean usesTLS()
	{
		return useOfTLS;
	}
	
	public boolean usesSASL()
	{
		return useOfSASL;
	}
	
	public boolean usesMD5()
	{
		return useMD5;
	}
	
	public boolean usesPlain()
	{
		return usePlain;
	}
	
	public boolean offersBinding()
	{
		return offerBinding;
	}
	
	public boolean sessionSupported()
	{
		return sessionSupported;
	}
}
