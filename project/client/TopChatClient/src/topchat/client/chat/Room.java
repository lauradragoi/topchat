/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
            }
            public void voiceGranted(String arg0) {
            }
            public void voiceRevoked(String arg0) {
            }
            public void banned(String arg0, String arg1, String arg2) {
            }
            public void membershipGranted(String arg0) {
            }
            public void membershipRevoked(String arg0) {
            }
            public void moderatorGranted(String arg0) {
            }
            public void moderatorRevoked(String arg0) {
            }
            public void ownershipGranted(String arg0) {
            }
            public void ownershipRevoked(String arg0) {
            }
            public void adminGranted(String arg0) {
            }
            public void adminRevoked(String arg0) {
            }
            public void nicknameChanged(String arg0, String arg1) {
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
       RoomPanel.chatMessage.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                 try {
                    sendMessage();

                    } catch (XMPPException ex) {
                        Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    RoomPanel.chatMessage.setText("");
                }
            }
            public void keyReleased(KeyEvent e) {                
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
				
				int nr = -1;
                int id = -1;
								
				try{
					nr = Integer.parseInt(s.substring(0, s.indexOf("#")));
				} catch(Exception e){}

                s = s.substring(s.indexOf('#') + 1);
				
				try{				
					id = Integer.parseInt(s.substring(0, s.indexOf("#")));
				} catch(Exception e){}

                s = s.substring(s.indexOf('#') + 1);
				System.out.println("pfff " + s +  "  " +  " nr " + nr +  " id " + id);
                if (ClientConnection.user.first_id == -1)
                    ClientConnection.user.first_id = nr;

                RoomPanel.addMessage(From+" : "+s, nr);
                
				
				if (RoomPanel.ref == true && id != -1){
					// am primit referinta de la server
					RoomPanel.addReference(nr-ClientConnection.user.first_id, id-ClientConnection.user.first_id);
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
        String s = "#"+RoomPanel.reference+"#"+RoomPanel.chatMessage.getText();
        muc.sendMessage(s);
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
