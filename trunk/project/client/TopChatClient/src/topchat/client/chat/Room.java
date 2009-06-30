/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import topchat.client.connection.ClientConnection;
import topchat.client.gui.ChatPanel;
import topchat.client.gui.JoinNewRoom;

/**
 *
 * @author Oana Iancu
 */
public class Room {

    public MultiUserChat muc = null;
    public ArrayList <User> roomUsers;
    public ChatPanel RoomPanel;
    public String roomName;
    public JoinNewRoom joinedRoom;
    //ChatGUI roomChatGUI;


    public Room(String roomName,String roomNick){
       muc = new MultiUserChat(ClientConnection.connection, roomName);
       this.roomName = roomName;
    }
    public Room(String roomName,String roomNick,ChatPanel roomPanel) throws XMPPException{

       muc = new MultiUserChat(ClientConnection.connection, roomName);
       this.RoomPanel = roomPanel;
       this.roomName = roomName;
       roomUsers = new ArrayList<User>();
       
       muc.addPresenceInterceptor(new PacketInterceptor() {

            public void interceptPacket(Packet arg0) {
                System.out.println("status_incerceptor " + arg0);
            }
        });
       
       muc.addParticipantStatusListener(new ParticipantStatusListener() {

           public synchronized void joined(String newName) {
             
                    //newName = "myroom@conference.jabber.org/roomnick"
                    char[] newRoom = new char[newName.length()];
                    String newUserNick;
                    String newRoomAddress;
                    int endIndex = 0;
                    int startIndex = 0;
                    newRoom = newName.toCharArray();
                    for (int i = 0; i < newRoom.length; i++) {
                        if (newRoom[i] == '/')
                            startIndex = i + 1;
                        if (newRoom[i] == '@')
                            endIndex = i;
                    }
                    newRoomAddress = newName.substring(0, endIndex);
                    newUserNick = newName.substring(startIndex, newRoom.length);
                    //System.out.println("address: " + newRoomAddress + " nick: " + newRoomNick);
                    if (!isUserInRoom(newUserNick))
                    {
                        RoomPanel.usersListModel.addElement(newUserNick);//+" - "+joinedRoom.jTextField3.getText());
                    }
            }

            public void left(String newName) {
                    char[] newRoom = new char[newName.length()];
                    String newUserNick;
                    String newRoomAddress;
                    int endIndex = 0;
                    int startIndex = 0;
                    newRoom = newName.toCharArray();
                    for (int i = 0; i < newRoom.length; i++) {
                        if (newRoom[i] == '/')
                            startIndex = i + 1;
                        if (newRoom[i] == '@')
                            endIndex = i;
                    }
                    newRoomAddress = newName.substring(0, endIndex);
                    newUserNick = newName.substring(startIndex, newRoom.length);
                    System.out.println("address: " + newRoomAddress + " nick: " + newUserNick);
                    if (isUserInRoom(newUserNick)==true)
                    {
                        RoomPanel.usersListModel.removeElement(newUserNick);//+" - "+joinedRoom.jTextField3.getText());
                        //ClientConnection.user.userRooms.remove(newRoomAddress);
                    }
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
       RoomPanel.send.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage();

                } catch (XMPPException ex) {
                    Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                }
                RoomPanel.chatMessage.setText("");
            }
        });
       muc.addMessageListener(new PacketListener() {

            public void processPacket(Packet arg0) {
                System.out.println(((Message)arg0).getBody());
                char[] sender = new char[((Message)arg0).getFrom().length()];
                String From;
                int startIndex = 0;
                sender = ((Message)arg0).getFrom().toCharArray();
                for (int i = 0; i < sender.length; i++) {
                    if (sender[i] == '/') {
                        startIndex = i+1;
                    }
                }
                From = ((Message)arg0).getFrom().substring(startIndex, sender.length);
                
                String s = ((Message)arg0).getBody();
                s = s.substring(1);
                int id = Integer.parseInt(s);
                s = s.substring(s.indexOf('#') + 1);
                RoomPanel.addMessage(From+" : "+s,id);

                if(RoomPanel.ref == true && RoomPanel.reference != -1){
                    RoomPanel.addReference(RoomPanel.source, RoomPanel.reference);
                    RoomPanel.ref = false;
                    RoomPanel.reference = -1;
                    RoomPanel.source = -1;
                }
            }

        });
      
    }

   
    public void createRoom(String Nick) throws XMPPException{
        // Create the room
       muc.create(Nick);
       muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
    }
    public void joinRoom(String Nick) throws XMPPException{
        //join this room
        muc.join(Nick);   
    }

    public void sendMessage() throws XMPPException{
        String s = "#"+RoomPanel.reference+"#"+RoomPanel.chatMessage.getText()+ "#";
        muc.sendMessage(RoomPanel.chatMessage.getText());
    }
    public void sendStatus(String status) throws XMPPException{

        muc.sendMessage(status);
    }
    public boolean  isUserInRoom(String nick){
        for(int i=0; i<roomUsers.size(); i++)
            if(roomUsers.get(i).nickname.compareTo(nick) == 0)
                return true;
        return false;
    }
    
    public void addUserInRoom(User newUser){
        roomUsers.add(newUser);
    }

     public void removeUserFromRoom(User user){
        roomUsers.remove(user);
    }
}
