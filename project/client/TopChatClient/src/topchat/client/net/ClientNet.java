/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.net;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

/**
 *
 * @author Oana Iancu
 */
public class ClientNet implements NetConstants{
    public static XMPPConnection conn;

    public static void makeConnection() throws XMPPException
    {
        // Create a connection to the jabber.org server on a specific port.
        ConnectionConfiguration config = new ConnectionConfiguration(SERVER_NAME, PORT);
        conn = new XMPPConnection(config);
        try {
            conn.connect();
            if(conn.isConnected())
                Logger.getLogger(ClientNet.class.getName()).log(Level.INFO, "I'm connected!");
        } catch (XMPPException ex) {
            Logger.getLogger(ClientNet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void readPacket()
    {
        // Create a new presence. Pass in false to indicate we're unavailable.
        Presence presence = new Presence(Presence.Type.unavailable);
        presence.setStatus("Gone fishing");
        // Send the packet (assume we have a XMPPConnection instance called "con").
        conn.sendPacket(presence);

    }
}
