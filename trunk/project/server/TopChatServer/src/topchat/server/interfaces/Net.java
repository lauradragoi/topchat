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

import javax.net.ssl.SSLEngine;

/**
 * Interface describing the network module of the server
 * 
 */
public interface Net
{

	/**
	 * Connects to the mediator
	 * 
	 * @param med
	 *            the mediator to which the network module connects
	 */
	public void setMediator(NetMediator med);

	/**
	 * Connects to the protocol
	 * 
	 * @param prot
	 *            the protocol to which the network module connects
	 */
	public void setProtocol(Protocol prot);

	/**
	 * Starts the network module and sets the port for listening
	 * 
	 * @param port
	 *            the port used for listening
	 */
	public void start(int port);

	/**
	 * Send some data on a specific socket
	 * 
	 * @param socket
	 *            the socket on which data will be sent
	 * @param data
	 *            the data to be sent
	 */
	public void send(SocketChannel socket, byte[] data);

	/**
	 * Send data on a specific socket without modifying it ( TLS encoding, etc )
	 * 
	 * @param socket
	 *            the socket on which data will be sent
	 * @param data
	 *            the raw data to be sent
	 */
	public void sendRaw(SocketChannel socket, byte[] data);

	/**
	 * Initiate a request to secure the stream using TLS on one socket
	 * 
	 * @param socketChannel
	 *            the socket on which TLS should be used
	 * @param sslEngine
	 *            the SSLEngine used for securing the channel with TLS
	 */
	public void secure(SocketChannel socketChannel, SSLEngine sslEngine);
}
