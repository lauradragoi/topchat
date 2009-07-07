package topchat.client.reference;


import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Un renderer pentru celule care contin mesaje.
 */
public class ConversationMessageRenderer extends ConversationMessageCell implements TableCellRenderer
{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		//System.out.println("Drawing message at " + row + " " + column);
		try
		{
			return (ConversationMessageCell)table.getModel().getValueAt(row, column);
		} catch (Exception e)
		{
			System.out.println("exceptie" + e);

		}
		return null;
	}

}