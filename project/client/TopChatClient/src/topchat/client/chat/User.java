/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.chat;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import topchat.client.connection.ClientConnection;

/**
 *
 * @author Oana Iancu
 */
public class User {

    String username, password;
    ArrayList <Room> userRooms;
    ArrayList <ContactDetails> userContacts;

    public User(String userName, String passwd) throws XMPPException{
        this.username = userName;
        this.password = passwd;
        //System.out.println(userName+"  "+passwd );
        userRooms = new ArrayList<Room>();
        userContacts = new ArrayList<ContactDetails>();
    }
    public void getContacts(){
        XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener() {

            public void connectionCreated(XMPPConnection newConn) {
                    userContacts.add(new ContactDetails(newConn.getUser(), "",""));
                    System.out.println(newConn.getUser());
            }
        });
    }
    public void addRoom(String addr,String nick,DefaultListModel listModel) throws XMPPException{
        Room newRoom = new Room(addr, nick,listModel);
        newRoom.createRoom(nick);
        newRoom.joinRoom(nick);
        userRooms.add(newRoom);
        listModel.addElement(nick);
    }
    public void joinRoom(String addr,String nick,DefaultListModel listModel) throws XMPPException{
        Room newRoom = new Room(addr, nick,listModel);
        newRoom.joinRoom(nick);
        userRooms.add(newRoom);
        listModel.addElement(nick);
    }
    public void removeRoom(Room room){
        userRooms.remove(room);
    }
    public void addContact(ContactDetails newContact){
        userContacts.add(newContact);
    }
    public void removeRoom(ContactDetails contact){
        userContacts.remove(contact);
    }
}
