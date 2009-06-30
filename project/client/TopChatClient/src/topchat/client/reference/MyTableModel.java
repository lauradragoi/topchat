package topchat.client.reference;
import javax.swing.table.DefaultTableModel;


public class MyTableModel extends DefaultTableModel
{
	String columnNames[] = { "Referinta", "Replica" };

    @Override
	  public int getColumnCount() {
	    return columnNames.length;
	  }

    @Override
	  public String getColumnName(int column) {
	    return columnNames[column];
	  }
}



