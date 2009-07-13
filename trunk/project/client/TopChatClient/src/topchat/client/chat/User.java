/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.chat;

import java.util.ArrayList;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import topchat.client.gui.ChatPanel;

/**
 *
 * @author Oana Iancu
 */
public class User {

    public String username, password, nickname, status;
    ArrayList <Room> userRooms;
    /*ca sa desenez referintele bine trebuie ca userul sa retina id-ul 
     * primului mesaj care apare in camera dupa ce a dat join
     * ca apoi cand deseneaza referintele sa faca diferenta
     */
    public int first_id = -1;
    public int last_id = -1;

    public User(String userName, String passwd, String nick, String stat) throws XMPPException{
        this.username = userName;
        this.password = passwd;
        this.status = stat;
        this.nickname = nick;

        userRooms = new ArrayList<Room>();
    }
    public void addRoom(String addr,String nick,String status,ChatPanel roomPanel) throws XMPPException{
        Room newRoom = new Room(addr, nick,roomPanel);
        newRoom.createRoom(nick);
        newRoom.joinRoom(nick);
        userRooms.add(newRoom);
        if(!newRoom.isUserInRoom(nick)){
            roomPanel.usersListModel.addElement(nick+" - "+status);
        }
    }
    public void joinRoom(String addr,String nick,String status,ChatPanel roomPanel) throws XMPPException{
        Room newRoom = new Room(addr, nick,roomPanel);
        newRoom.joinRoom(nick);
        newRoom.muc.changeAvailabilityStatus(status, Presence.Mode.available);
        userRooms.add(newRoom);
        if(!newRoom.isUserInRoom(nick))
            roomPanel.usersListModel.addElement(nick+" - "+status);
    }

    public Room getRoom(ChatPanel panel){
        for(int i=0; i<userRooms.size(); i++)
            if(userRooms.get(i).RoomPanel.equals(panel))
                return userRooms.get(i);
        return null;
    }

    public void removeRoom(Room room) throws XMPPException{
        userRooms.remove(room);
        //room.muc.changeAvailabilityStatus(status, Presence.Mode.available);
        //room.muc.leave();
        room.muc.destroy(null,null);
    }
    public String  getUserStatus(String nick,String room){
        ArrayList<User> roomUsers;
        roomUsers = new ArrayList<User>();

        for(int i=0; i<userRooms.size(); i++)
            if(userRooms.get(i).roomName.compareTo(room) == 0)
                roomUsers = userRooms.get(i).roomUsers;

        for(int i=0; i< roomUsers.size(); i++ )
            if (roomUsers.get(i).nickname.compareTo(nick) == 0)
                return roomUsers.get(i).status;
        
        return null;
    }
    //public String getStatus(){
    //    return thi
    //}
    public void changeStatus(String newStatus){
        this.status = newStatus;
    }
    public void changeNick(String newNick){
        this.nickname = newNick;
    }
     public boolean  isRoomAlready(String roomName){
        for(int i=0; i<userRooms.size(); i++)
            if(userRooms.get(i).roomName.compareTo(roomName) == 0)
                return true;
        return false;
    }
}
