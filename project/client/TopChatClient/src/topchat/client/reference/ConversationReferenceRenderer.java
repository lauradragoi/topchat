package topchat.client.reference;


import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Un renderer pentru celulele care contin referinte.
 */
public class ConversationReferenceRenderer extends ConversationReferenceCell implements TableCellRenderer
{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		System.out.println("Drawing reference at " + row + " " + column);
		try
		{
			return (ConversationReferenceCell)table.getModel().getValueAt(row, column);
		} catch (Exception e)
		{
			System.out.println("exceptie" + e);

		}
		return null;
	}


}