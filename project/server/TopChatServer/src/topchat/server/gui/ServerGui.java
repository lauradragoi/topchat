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

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import topchat.server.interfaces.Gui;
import topchat.server.interfaces.GuiMediator;

/**
 * Server GUI implementation
 */
@SuppressWarnings("serial")
public class ServerGui extends JPanel implements Gui, GuiConstants
{

	/** Connection to mediator */
	private GuiMediator med = null;

	/** List showing connected users */
	private ListPanel usersPanel;
	/** List showing existing rooms */
	private ListPanel roomsPanel;

	/** Label used for showing application status */
	private JLabel statusLabel;

	private static Logger logger = Logger.getLogger(ServerGui.class);

	/**
	 * Initiates the GUI and connects it to the GuiMediator
	 * 
	 * @param med
	 *            the GuiMediator connected to the Gui
	 */
	public ServerGui(GuiMediator med)
	{
		try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        
		init();

		setMediator(med);
		this.med.setGui(this);

		logger.info("GUI initiated");
	}

	/**
	 * Creates the GUI frame
	 */
	private void build()
	{
		JFrame frame = new JFrame(GuiConstants.APP_NAME);
		frame.setContentPane(this);
		frame.setSize(APP_WIDTH, APP_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Show the interface
	 */
	@Override
	public void show()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				build();
			}
		});
	}

	@Override
	public void setMediator(GuiMediator med)
	{
		this.med = med;
	}

	/**
	 * Initializes the GUI
	 */
	private void init()
	{
		usersPanel = new ListPanel(ListSelectionModel.SINGLE_SELECTION,
				"users", USERS_LIST_MIN_W, USERS_LIST_MIN_H);

		roomsPanel = new ListPanel(ListSelectionModel.SINGLE_SELECTION,
				"rooms", ROOMS_LIST_MIN_W, ROOMS_LIST_MIN_H);

		JSplitPane horizSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				roomsPanel, usersPanel);
		horizSplitPane.setResizeWeight(1);

		statusLabel = new JLabel(DEFAULT_STATUS);

		this.setLayout(new BorderLayout());
		this.add(horizSplitPane, BorderLayout.CENTER);
		this.add(statusLabel, BorderLayout.SOUTH);
	}

	@Override
	public void setStatus(String msg)
	{
		statusLabel.setText(msg);
	}

	/**
	 * Add a user
	 * 
	 * @param user
	 *            the user that is added
	 */
	@Override
	public void addUser(final String user)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				usersPanel.getListModel().addElement(user);
			}
		});
	}

	/**
	 * Add a room
	 * 
	 * @param room
	 *            the room that is added
	 */
	@Override
	public void addRoom(final String room)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				roomsPanel.getListModel().addElement(room);
			}
		});
	}

	/**
	 * Remove a user
	 * 
	 * @param user
	 *            the user to be removed
	 */
	@Override
	public void removeUser(final String user)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				usersPanel.getListModel().removeElement(user);
			}
		});
	}

	/**
	 * Remove a room
	 * 
	 * @param room
	 *            the room to be removed
	 */
	@Override
	public void removeRoom(final String room)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				roomsPanel.getListModel().removeElement(room);
			}
		});
	}

}
