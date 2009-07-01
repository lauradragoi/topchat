/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChatPanel.java
 *
 * Created on 24.06.2009, 21:30:35
 */

package topchat.client.gui;
import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import topchat.client.chat.Room;
import topchat.client.connection.ClientConnection;
import topchat.client.reference.ConversationMessage;
import topchat.client.reference.MyTableModel;
import topchat.client.reference.ConversationMessageRenderer;
import topchat.client.reference.ConversationReferenceCell;
import topchat.client.reference.ConversationReferenceRenderer;

/**
 *
 * @author Oana Iancu
 */
public class ChatPanel extends javax.swing.JPanel implements ActionListener{

    public DefaultListModel usersListModel;
    public PopupMenu popup;
    public MenuItem item;

    String[] columnNames = {"Referinte" , "Replici"};
    Vector columnNamesV = new Vector(Arrays.asList(columnNames));

    /** Tabelul */
    public JTable table = null;
    public TableModel model = new MyTableModel();
    /** avem referinta */
    public boolean ref = false;
    public int reference = -1;
    public int source = -1;


    /** Creates new form ChatPanel */
    public ChatPanel() {
        usersListModel = new DefaultListModel();
        initComponents();

        // Set component with initial focus; must be done before the frame is made visible
        //this.chatMessage.requestFocus();

        popup = new PopupMenu();
        item = new MenuItem("Leave room!");
        popup.add(item);
        item.addActionListener(this);
        this.add(popup);
        //Mask Mouse Events
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);


        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };
        table.setGridColor(Color.WHITE);
        table.setIntercellSpacing(new Dimension(1, 1));
    	table.getColumnModel().getColumn(0).setCellRenderer(new ConversationReferenceRenderer());
    	table.getColumnModel().getColumn(1).setCellRenderer(new ConversationMessageRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jScrollPane1.setViewportView(table);
        // Listen for value changes in the scroll pane's scrollbars
        MyAdjustmentListener l = new MyAdjustmentListener();
        this.jScrollPane1.getVerticalScrollBar().addAdjustmentListener(l);

        table.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                ref = true;
                //id-ul mesajului la care fac referinta e dat de linia selectata
                reference = table.getSelectedRow();
                source = table.getRowCount();
            }
            public void mousePressed(MouseEvent e) {
            }
            public void mouseReleased(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
        });

        this.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == e.VK_ENTER){
                    SendButtonKeyPressed(e);
                }
            }
            public void keyReleased(KeyEvent e) {

            }
        });


    }

    //processing the mouse events
    @Override
    public void processMouseEvent(MouseEvent mouseevent)
    {
        super.processMouseEvent(mouseevent);
        //this.chatMessage.setFocusable(true);
        this.chatMessage.requestFocus();
        //check if popup triggered
        if(mouseevent.isPopupTrigger())
        {
            //if yes show the PopupMenu
            popup.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
        }
    }

    //handle action on the MenuItems
    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource().equals(item))
        {
           Room room = ClientConnection.user.getRoom(this);
           ClientConnection.user.removeRoom(room);
           this.usersListModel.removeElement(ClientConnection.user.nickname);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chatMessage = new javax.swing.JTextField();
        send = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        usersList = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(204, 204, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        setPreferredSize(new java.awt.Dimension(500, 350));

        chatMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chatMessageKeyPressed(evt);
            }
        });

        send.setText("Send");
        send.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SendMessageButton(evt);
            }
        });
        send.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SendButtonKeyPressed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(204, 204, 255));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Users");

        usersList.setModel(usersListModel);
        jScrollPane2.setViewportView(usersList);

        jScrollPane1.setAutoscrolls(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 227, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(send))
                    .addComponent(jScrollPane1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel1)))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(send, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void SendMessageButton(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SendMessageButton
        
    }//GEN-LAST:event_SendMessageButton

    private void SendButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SendButtonKeyPressed

    }//GEN-LAST:event_SendButtonKeyPressed

    private void chatMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chatMessageKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_chatMessageKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField chatMessage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JButton send;
    public javax.swing.JList usersList;
    // End of variables declaration//GEN-END:variables


     /**
     * Adauga o referinta
     * @param source
     * @param destination
     */
    public synchronized void addReference(int source, int destination)
    {
    	if (source < destination)
    	{
    		addReference(destination, source);
    		return;
    	}

    	// anunta toate mesajele dintre sursa si destinatie despre referinta
    	// (fiindca o sa afisez sageata pe celulele referinta din dreptul acestor mesaje)
    	for (int i = destination; i <= source; i++)
    	{
    		ConversationReferenceCell myCell = ((ConversationReferenceCell)model.getValueAt(i, 0));
    		myCell.addReference(source, destination);

    		model.setValueAt(myCell, i, 0);
    	}

    	System.out.println("reference added " + source + "  " + destination);
    }

    /**
     * Adauga un mesaj
     */
    public synchronized void addMessage(String msg,int id)
    {

		DefaultTableModel defmodel =  (DefaultTableModel)table.getModel();

		ConversationMessage message = new ConversationMessage(msg, defmodel.getRowCount(),id);
		defmodel.addRow(message);

		System.out.println("message added " + msg);
    }
}

class MyAdjustmentListener implements AdjustmentListener {
        // This method is called whenever the value of a scrollbar is changed,
        // either by the user or programmatically.
        public void adjustmentValueChanged(AdjustmentEvent evt) {
            Adjustable source = evt.getAdjustable();

            // getValueIsAdjusting() returns true if the user is currently
            // dragging the scrollbar's knob and has not picked a final value
            if (evt.getValueIsAdjusting()) {
                // The user is dragging the knob
                return;
            }

            // Determine which scrollbar fired the event
            int orient = source.getOrientation();
            if (orient == Adjustable.HORIZONTAL) {
                // Event from horizontal scrollbar
            } else {
                // Event from vertical scrollbar
            }

            // Determine the type of event
            int type = evt.getAdjustmentType();
            switch (type) {
              case AdjustmentEvent.UNIT_INCREMENT:
                  // Scrollbar was increased by one unit
                  source.setUnitIncrement(source.getUnitIncrement());
                  break;
              case AdjustmentEvent.UNIT_DECREMENT:
                  // Scrollbar was decreased by one unit
                  source.setUnitIncrement(source.getUnitIncrement());
                  break;
              case AdjustmentEvent.BLOCK_INCREMENT:
                  // Scrollbar was increased by one block
                  source.setBlockIncrement(source.getBlockIncrement());
                  break;
              case AdjustmentEvent.BLOCK_DECREMENT:
                  // Scrollbar was decreased by one block
                  source.setBlockIncrement(source.getBlockIncrement());
                  break;
              case AdjustmentEvent.TRACK:
                  // The knob on the scrollbar was dragged
                  source.setValue(source.getMaximum());
                  break;
            }

            // Get current value
            int value = evt.getValue();
        }
    }

