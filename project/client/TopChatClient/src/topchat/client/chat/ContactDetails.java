/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.chat;

/**
 *
 * @author Oana Iancu
 */
public class ContactDetails {
    String name,password,status;

    public ContactDetails(){

    }
    public ContactDetails(String name,String pass,String stat){
        this.name = name;
        this.password = pass;
        this.status = stat;
    }
    //public void changeStatus(String newStatus){
    //    this.status = newStatus;
    //}
}
