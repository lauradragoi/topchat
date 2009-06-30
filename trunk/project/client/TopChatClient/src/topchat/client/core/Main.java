/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topchat.client.core;

import javax.swing.UIManager;
import topchat.client.gui.LoginGUI;

/**
 *
 * @author Oana Iancu
 */
public class Main {

    public static LoginGUI login_gui = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        // TODO code application logic here
        login_gui = new LoginGUI();
        login_gui.show();

    }

}
