/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.chat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import topchat.client.connection.ClientConnection;

/**
 *
 * @author Oana Iancu
 */
public class Room extends MultiUserChat{

    ArrayList <ContactDetails> roomUsers;
    //String subject;
    DefaultListModel listModel;

    public Room(String roomName,String roomNick,DefaultListModel model) throws XMPPException{

       super(ClientConnection.connection,roomName);
       this.listModel = model;

       this.addParticipantStatusListener(new ParticipantStatusListener() {

            public void joined(String newName) {
                
            }

            public void left(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void kicked(String arg0, String arg1, String arg2) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void voiceGranted(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void voiceRevoked(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void banned(String arg0, String arg1, String arg2) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void membershipGranted(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void membershipRevoked(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void moderatorGranted(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void moderatorRevoked(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void ownershipGranted(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void ownershipRevoked(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void adminGranted(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void adminRevoked(String arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void nicknameChanged(String arg0, String arg1) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
       );
       //RoomInfo info = MultiUserChat.getRoomInfo(ClientConnection.connection,roomName);
       //.out.println("aa"+ info.getOccupantsCount());
       roomUsers = new ArrayList<ContactDetails>();
    }
    public void createRoom(String roomNick) throws XMPPException{
        // Create the room
       create(roomNick);
       sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
    }
    public void joinRoom(String roomNick) throws XMPPException{
        //join this room
        join(roomNick);
    }

    public void addUserInRoom(ContactDetails newContact){
        roomUsers.add(newContact);
    }

     public void removeUserFromRoom(ContactDetails contact){
        roomUsers.remove(contact);
    }

     public String getNick(){
         return this.getNick();
     }


}
