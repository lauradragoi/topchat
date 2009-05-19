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

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Interface describing the network module of the server
 * 
 */
public interface Net {

	/** Connects to the mediator */
	public void setMediator(NetMediator med);
	
	public void setProtocol(Protocol prot);
	
	public void start(int port);	
}
