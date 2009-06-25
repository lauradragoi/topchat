/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChatGUI.java
 *
 * Created on 04.05.2009, 15:41:14
 */

package topchat.client.gui;

/**
 *
 * @author Oana Iancu
 */
public class ChatGUI extends javax.swing.JFrame {
    public static NewRoom addRoom;
    public static JoinNewRoom joinRoom;
    

    /** Creates new form ChatGUI */
    public ChatGUI() {
        initComponents();
        addRoom = new NewRoom();
        joinRoom = new JoinNewRoom();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        joinButton = new javax.swing.JButton();
        addRoomButton = new javax.swing.JButton();
        TabbedRooms = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.MatteBorder(new javax.swing.ImageIcon(getClass().getResource("/topchat/client/resources/logo.png")))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 700));

        joinButton.setText("Join Room");
        joinButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                joinRoomButton(evt);
            }
        });

        addRoomButton.setText("Add Room");
        addRoomButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addRoomButtonButton(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(634, Short.MAX_VALUE)
                .addComponent(joinButton)
                .addGap(18, 18, 18)
                .addComponent(addRoomButton)
                .addGap(67, 67, 67))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabbedRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(TabbedRooms, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(joinButton)
                    .addComponent(addRoomButton)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addRoomButtonButton(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addRoomButtonButton
        addRoom.mainGUI=this;
        addRoom.show();
}//GEN-LAST:event_addRoomButtonButton

    private void joinRoomButton(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_joinRoomButton
        joinRoom.mainGUI=this;
        joinRoom.show();
    }//GEN-LAST:event_joinRoomButton

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTabbedPane TabbedRooms;
    private javax.swing.JButton addRoomButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton joinButton;
    // End of variables declaration//GEN-END:variables

}
