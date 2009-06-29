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
 * Interface implemented by the mediator module that connects the authentication
 * module to the rest of the modules of the application.
 * 
 */
public interface AuthenticationMediator
{

	/**
	 * Method called to set the authentication module connected to the mediator
	 * 
	 * @param authHandler
	 *            the authentication module
	 */
	public void setAuthenticationHandler(
			AuthenticationHandlerInterface authHandler);

	/**
	 * Method called by the authentication module in order to obtain the value
	 * of a server property.
	 * 
	 * @param property
	 *            the name of the property requested
	 * 
	 * @return the value of the property
	 * @throws Exception
	 *             if the property is not properly set
	 */
	String getProperty(String property) throws Exception;
}
