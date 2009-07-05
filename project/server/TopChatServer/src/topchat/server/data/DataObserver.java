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

/**
 * A DataObserver handles DataEvents that it previously registered to receive
 * from a DataHandler.
 */
public abstract class DataObserver
{

	/**
	 * Method called to handle a DataEvent with certain arguments
	 * 
	 * @param event
	 *            the DataEvent that occurred
	 * @param args
	 *            the arguments of the event
	 */
	public abstract void handle(DataEvent event, String[] args);

}
