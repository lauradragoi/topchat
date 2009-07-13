package topchat.client.reference;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import topchat.client.gui.ChatPanel;

/**
 * Un renderer pentru celule care contin mesaje.
 */
public class ConversationMessageRenderer extends ConversationMessageCell implements TableCellRenderer{

    ChatPanel panel;

    public ConversationMessageRenderer(ChatPanel p){
        this.panel = p;
    }
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		//System.out.println("Drawing message at " + row + " " + column);
        ConversationMessageCell cell = (ConversationMessageCell)table.getModel().getValueAt(row, column);
        if(row == panel.hrow && column == panel.hcol){
            cell.setBackground(Color.cyan);
          //  cell.setForeground(Color.darkGray);
            
        }
        else{
            cell.setBackground(Color.white);
         //   cell.setForeground(this.color);
       }

		try
		{
			return cell;//(ConversationMessageCell)table.getModel().getValueAt(row, column);
		} catch (Exception e)
		{
			System.out.println("exceptie" + e);

		}
		return null;
	}

}