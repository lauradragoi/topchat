/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.chat;

import java.util.ArrayList;
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

    public Room(String roomName,String roomNick) throws XMPPException{

       super(ClientConnection.connection,roomName);
       // Create the room
       this.create(roomNick);
       this.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
       //this.addParticipantListener(listener);
       this.addParticipantStatusListener(new ParticipantStatusListener() {

            public void joined(String arg0) {
                
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
       //join this room
       this.join(roomNick);
       //RoomInfo info = MultiUserChat.getRoomInfo(ClientConnection.connection,roomName);
       //.out.println("aa"+ info.getOccupantsCount());
       roomUsers = new ArrayList<ContactDetails>();
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
