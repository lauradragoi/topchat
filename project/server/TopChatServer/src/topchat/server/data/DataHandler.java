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

public class DataHandler implements DataHandlerInterface
{
	private ConcurrentHashMap<DataEvent, List<DataObserver> > observers = new ConcurrentHashMap<DataEvent, List<DataObserver> >();
	
	/** Connection to mediator */
	private DataMediator med = null;
	
	private static Logger logger = Logger.getLogger(DataHandler.class);
	
	public DataHandler(DataMediator med) throws Exception
	{
		this.med = med;
		med.setDataHandler(this);
		
		new SaveAllObserver(this);
		
		new SaveSimpleXMLObserver(this);

		logger.info("Data handling module initiated.");
	}
	
	public void registerObserver(DataObserver observer, DataEvent event)
	{
		List<DataObserver> existingObservers = observers.get(event);
		
		if (existingObservers == null)
			existingObservers = new ArrayList<DataObserver>();
		existingObservers.add(observer);
		
		observers.put(event, existingObservers);
			
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.DataHandlerInterface#handleReceived(java.lang.String)
	 */
	@Override
	public void handleReceived(String s)
	{
		handle(DataEvent.HANDLE_RECEIVED, new String[]{s});		
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.DataHandlerInterface#handleSent(java.lang.String)
	 */
	@Override
	public void handleSent(String s)
	{
		handle(DataEvent.HANDLE_SENT, new String[]{s});	
	}

	public void handle(DataEvent event, String[] args)
	{	
		List<DataObserver> existingObservers = observers.get(event);
		
		if (existingObservers == null)
			return;
		
		for (DataObserver dataObserver : existingObservers)
		{
			
			dataObserver.handle(event, args);
		}
	}

	/**
	 * @param string
	 * @return
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

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.DataHandlerInterface#handleRoomJoined(java.lang.String, java.lang.String)
	 */
	@Override
	public void handleRoomJoined(String roomName, String roomUser)
	{
		handle(DataEvent.ROOM_JOINED, new String[]{roomName, roomUser});
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.DataHandlerInterface#handleGroupMessage(java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public void handleGroupMessage(String roomUser, String roomName,
			String body, int numMessages, int ref)
	{
		handle(DataEvent.GROUP_MESSAGE, new String[]{roomUser, roomName,
				body, "" + numMessages, "" +ref});
		
	}

	/* (non-Javadoc)
	 * @see topchat.server.interfaces.DataHandlerInterface#handleRoomLeft(java.lang.String, java.lang.String)
	 */
	@Override
	public void handleRoomLeft(String name, String roomUser)
	{
		handle(DataEvent.ROOM_LEFT, new String[]{name, roomUser});		
	}
}
