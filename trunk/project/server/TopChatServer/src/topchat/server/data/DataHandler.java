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
package topchat.server.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import topchat.server.interfaces.DataHandlerInterface;
import topchat.server.interfaces.DataMediator;

/**
 * This module is notified when a data event occurs. It passes on the
 * notification to the observers registered for that event.
 */
public class DataHandler implements DataHandlerInterface
{
	/**
	 * Map of data events and list of data observers registered for one data
	 * event
	 */
	private ConcurrentHashMap<DataEvent, List<DataObserver>> observers = new ConcurrentHashMap<DataEvent, List<DataObserver>>();

	/** Connection to mediator */
	private DataMediator med = null;

	private static Logger logger = Logger.getLogger(DataHandler.class);

	/**
	 * Constructs a DataHandler
	 * 
	 * @param med
	 *            the mediator it connects to
	 * @throws Exception
	 *             if it fails to initiate
	 */
	public DataHandler(DataMediator med) throws Exception
	{
		this.med = med;
		med.setDataHandler(this);

		new SaveAllObserver(this);

		new SaveSimpleXMLObserver(this);

		logger.info("Data handling module initiated.");
	}

	/**
	 * Method called to register a DataObserver module with a DataEvent
	 * 
	 * @param observer
	 *            the DataObserver
	 * @param event
	 *            the DataEvent
	 */
	public void registerObserver(DataObserver observer, DataEvent event)
	{
		List<DataObserver> existingObservers = observers.get(event);

		if (existingObservers == null)
			existingObservers = new ArrayList<DataObserver>();
		existingObservers.add(observer);

		observers.put(event, existingObservers);

	}

	/**
	 * Handles a DataEvent with certain arguments by notifying the registered
	 * observers for that event.
	 * 
	 * @param event
	 *            the event
	 * @param args
	 *            the arguments of the event
	 */
	private void handle(DataEvent event, String[] args)
	{
		List<DataObserver> existingObservers = observers.get(event);

		if (existingObservers == null)
			return;

		for (DataObserver dataObserver : existingObservers)
		{

			dataObserver.handle(event, args);
		}
	}

	@Override
	public void handleReceived(String s)
	{
		handle(DataEvent.HANDLE_RECEIVED, new String[]{s});
	}

	@Override
	public void handleSent(String s)
	{
		handle(DataEvent.HANDLE_SENT, new String[]{s});
	}

	/**
	 * Used to obtain an application property.
	 * 
	 * @param string
	 *            the name of the property
	 * @return the value of the property
	 * @throws Exception
	 *             if the property is improperly set
	 */
	public String getProperty(String string) throws Exception
	{
		return med.getProperty(string);
	}

	@Override
	public void handleRoomCreated(String room)
	{
		handle(DataEvent.ROOM_CREATED, new String[]{room});

	}

	@Override
	public void handleRoomDestroyed(String room)
	{
		handle(DataEvent.ROOM_DESTROYED, new String[]{room});
	}

	@Override
	public void handleRoomJoined(String roomName, String roomUser)
	{
		handle(DataEvent.ROOM_JOINED, new String[]{roomName, roomUser});
	}

	@Override
	public void handleGroupMessage(String roomUser, String roomName,
			String body, int numMessages, int ref)
	{
		handle(DataEvent.GROUP_MESSAGE, new String[]{roomUser, roomName, body,
				"" + numMessages, "" + ref});

	}

	@Override
	public void handleRoomLeft(String name, String roomUser)
	{
		handle(DataEvent.ROOM_LEFT, new String[]{name, roomUser});
	}
}
