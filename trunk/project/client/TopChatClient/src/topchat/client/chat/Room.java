/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.chat;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;
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
    public int first_id=-1;
    public boolean hack = true;
    //ChatGUI roomChatGUI;
    public Vector colors = new Vector(5);

    public Room(String roomName,String roomNick){
       muc = new MultiUserChat(ClientConnection.connection, roomName);
       this.roomName = roomName;
    }
    public Room(String roomName,String roomNick,ChatPanel roomPanel) throws XMPPException{

       colors.add("000000");
       colors.add("0000ff");
       colors.add("ff0000");
       colors.add("00ff00");
       colors.add("ff00ff");
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
                        try {
                            addUserInRoom(new User("", "", newUserNick, ""));
                        } catch (XMPPException ex) {
                            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
                    //if (isUserInRoom(newUserNick)==true)
                    
                        RoomPanel.usersListModel.removeElement(newUserNick);
                        //ClientConnection.user.userRooms.remove(newRoomAddress);
                    
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

        RoomPanel.send.addMouseListener(new MouseListener() {

            

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                try {
                    sendMessage();

                } catch (XMPPException ex) {
                    Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            public void mouseReleased(MouseEvent e) {
                RoomPanel.chatMessage.setText("");
                RoomPanel.reference = -1;//test
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
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
                   // RoomPanel.chatMessage.setText("");
                }
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    RoomPanel.chatMessage.setText("");
                    RoomPanel.reference = -1;//test
                }
            }
        });

       muc.addMessageListener(new PacketListener() {

            public synchronized void processPacket(Packet arg0) {

                System.out.println("cucu!!!!!!!!!!!!!!!!!!!!!!!");
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
                String mesaj = s.substring(0, s.indexOf("~"));
               // s = s.substring(s.indexOf('~') + 1);
               // s = s.substring(0, s.indexOf('~'));

                String color = s.substring(s.indexOf('~') + 1);
                Color newColor = new Color(Integer.parseInt( color ,16) );

                System.out.println("am primit: " + s +  "  " +  " nr_mesaj " + nr +  " id_ref " + id+ " color:"+color);
                if (first_id == -1)
                    first_id = nr;

                if (ClientConnection.user.last_id == nr)
                    return ;
                ClientConnection.user.last_id = nr;                
                
                RoomPanel.addMessage(From+" : "+mesaj, nr-first_id, newColor);
                int ceva = nr-first_id;
                System.out.println("am adaugat mesaj: "+ceva);
				
				if ( id > -1){
					// am primit referinta de la server
					//RoomPanel.addReference(nr-ClientConnection.user.first_id, id-ClientConnection.user.first_id);
                    RoomPanel.addReference(nr-first_id, id-first_id,newColor);
                    int ceva1 = nr-first_id;
                    int ceva2 = id-first_id;
                    System.out.println("am adaugat ref: "+ceva1+" "+ceva2);
                    RoomPanel.ref = false;

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
        int ceva;
        
        if (RoomPanel.reference != -1)
            //ceva = RoomPanel.reference+ClientConnection.user.first_id;
            ceva = RoomPanel.reference+first_id;
        else
            ceva = RoomPanel.reference;

        int index = RoomPanel.jComboBox1.getSelectedIndex();
        String s = "#"+ceva+"#"+RoomPanel.chatMessage.getText()+"~"+(String)colors.elementAt(index);
        System.out.println("send message: "+s);
        muc.sendMessage(s);
        hack = false;
       
        
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
