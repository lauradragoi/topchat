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
package topchat.server.protocol.xmpp.tls;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;


import org.apache.log4j.Logger;

public class TLSEngineFactory {

	private static Logger logger = Logger.getLogger(TLSEngineFactory.class);
	
    private SSLContext sslc;

    private SSLEngine serverEngine;	// server Engine

    /*
     * The following is to set up the keystores.
     */
    private static String keyStoreFile = "security/keystore";
    private static String trustStoreFile = "security/truststore";
    
    public TLSEngineFactory() throws Exception
    {
    	
    	KeyStore ks = KeyStore.getInstance("JKS");
    	KeyStore ts = KeyStore.getInstance("JKS");

    	char[] passphrase = "password".toCharArray();

    	ks.load(new FileInputStream(keyStoreFile), passphrase);
    	ts.load(new FileInputStream(trustStoreFile), passphrase);

    	KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    	kmf.init(ks, passphrase);

    	TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    	tmf.init(ts);

    	sslc = SSLContext.getInstance("TLS");

    	sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);    	
      	
    	
    	serverEngine = sslc.createSSLEngine();
    	serverEngine.setUseClientMode(false);
    }
    
    public SSLEngine getSSLEngine()
    {
    	return serverEngine;
    }
    

}
