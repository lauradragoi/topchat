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
package topchat.server.interfaces;

/**
 * Interface implemented by the mediator which connects the data handling module
 * to the rest of the server.
 * 
 */
public interface DataMediator
{

	/**
	 * Method called to set the data module that is connected to the mediator.
	 * 
	 * @param dataHandler
	 *            the data handling module
	 */
	public void setDataHandler(DataHandlerInterface dataHandler);

	/**
	 * Method called to obtain a property configured by the server.
	 * 
	 * @param string
	 *            the name of the property
	 * @return the value of the property
	 */
	public String getProperty(String string) throws Exception;
}
