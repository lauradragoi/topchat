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

import java.nio.channels.SocketChannel;
import topchat.server.defaults.DefaultConnectionManager;

/**
 * Interface describing the protocol used by the server
 * 
 */
public interface Protocol
{
	/**
	 * Connects to the mediator
	 * 
	 * @param med
	 *            the ProtocolMediator to which the Protocol connects to
	 */
	public void setMediator(ProtocolMediator med);

	/**
	 * Obtain the port used for listening for connections
	 * 
	 * @return the port used for listening
	 */
	public int getListeningPort();

	/**
	 * Start the protocol using the specified network module
	 * 
	 * @param net
	 *            the network module used by the protocol
	 */
	public void start(Net net);

	/**
	 * Called when the protocol should process certain data received on a
	 * specific socket
	 * 
	 * @param socketChannel
	 *            the socket on which the data was received
	 * @param data
	 *            the received data
	 * @param count
	 *            the number of bytes received
	 */
	public void processData(SocketChannel socketChannel, byte[] data, int count);

	/**
	 * Obtain the connection manager for a specific socket
	 * 
	 * @param socketChannel
	 *            the socket for which the connection manager is requested
	 * @return the DefaultConnectionManager handling the specified socket
	 */
	public DefaultConnectionManager getConnectionManager(
			SocketChannel socketChannel);

	/**
	 * Request execution of specific tasks by the protocol
	 * 
	 * @param r
	 *            the runnable task
	 */
	public void execute(Runnable r);

	/**
	 * Method called to announce that a connection was closed
	 * 
	 * @param socketChannel
	 *            the socket on which the connection that was closed was
	 *            functioning
	 */
	public void connectionClosed(SocketChannel socketChannel);

}
