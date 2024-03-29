/**
    TopChatServer 
    Copyright (C) 2009 Laura Dragoi

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package topchat.server.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A panel containing a scrollable list with a title
 * 
 */
@SuppressWarnings("serial")
public class ListPanel extends JPanel
{

	private JList list;
	private DefaultListModel listModel;

	/**
	 * Constructs a ListPanel
	 * 
	 * @param accessMode
	 *            the ListSelectionModel used by the contained JList
	 * @param label
	 *            the caption of the JLabel used as a title for the panel
	 * @param width
	 *            the width of the panel
	 * @param height
	 *            the height of the panel
	 */
	public ListPanel(int accessMode, String label, int width, int height)
	{
		listModel = new DefaultListModel();

		list = new JList(listModel);
		list.setSelectionMode(accessMode);

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setMinimumSize(new Dimension(width, height));

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(new JLabel(label));
		add(scrollPane);
	}

	public JList getList()
	{
		return list;
	}

	public DefaultListModel getListModel()
	{
		return listModel;
	}

}
