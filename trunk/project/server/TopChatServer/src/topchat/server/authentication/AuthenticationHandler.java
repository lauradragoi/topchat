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
package topchat.server.authentication;

import org.apache.log4j.Logger;

import topchat.server.gui.ServerGui;
import topchat.server.interfaces.AuthenticationHandlerInterface;
import topchat.server.interfaces.AuthenticationMediator;
import topchat.server.mediator.Mediator;

public class AuthenticationHandler implements AuthenticationHandlerInterface {

	private AuthenticationMediator med = null;
	
	private static Logger logger = Logger.getLogger(AuthenticationHandler.class);
	
	/**
	 * @param med
	 */
	public AuthenticationHandler(Mediator med) {
		this.med = med;
		med.setAuthenticationHandler(this);
		
		logger.info("Authentication module initiated");
	}

}
