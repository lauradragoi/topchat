/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.chat;

import java.util.ArrayList;
import org.jivesoftware.smack.XMPPException;
import topchat.client.gui.ChatPanel;

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
    
        userRooms = new ArrayList<Room>();
        userContacts = new ArrayList<ContactDetails>();
    }
    public void addRoom(String addr,String nick,String status,ChatPanel roomPanel) throws XMPPException{
        Room newRoom = new Room(addr, nick,roomPanel);
        newRoom.createRoom(nick);
        newRoom.joinRoom(nick);
        userRooms.add(newRoom);
        roomPanel.usersListModel.addElement(nick+" - "+status);
    }
    public void joinRoom(String addr,String nick,String status,ChatPanel roomPanel) throws XMPPException{
        Room newRoom = new Room(addr, nick,roomPanel);
        newRoom.joinRoom(nick);
        userRooms.add(newRoom);
        roomPanel.usersListModel.addElement(nick+" - "+status);
    }
    public void removeRoom(Room room){
        userRooms.remove(room);
    }
    public void addContact(ContactDetails newContact,ChatPanel roomPanel){
        userContacts.add(newContact);
    }
    public void removeRoom(ContactDetails contact){
        userContacts.remove(contact);
    }
}
