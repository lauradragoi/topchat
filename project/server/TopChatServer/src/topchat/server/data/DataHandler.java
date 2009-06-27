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

import org.apache.log4j.Logger;

import topchat.server.db.DatabaseConnector;
import topchat.server.interfaces.DataHandlerInterface;
import topchat.server.interfaces.DataMediator;

public class DataHandler implements DataHandlerInterface {

	/** Connection to mediator */
	@SuppressWarnings("unused")
	private DataMediator med = null;
	
//	private DatabaseConnector databaseConnector = new DatabaseConnector();
	
	private static Logger logger = Logger.getLogger(DataHandler.class);
	
	public DataHandler(DataMediator med)
	{
		this.med = med;
		med.setDataHandler(this);		
		
		logger.info("Data handling module initiated.");
	}
}
