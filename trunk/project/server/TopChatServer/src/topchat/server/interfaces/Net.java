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
public interface Net {

	/** Connects to the mediator */
	public void setMediator(NetMediator med);
	
	/** Connects to the protocol */
	public void setProtocol(Protocol prot);
	
	/** Starts the network module and sets the port for listening */
	public void start(int port);	
	
	/** Send some data on a specific socket */
	public void send(SocketChannel socket, byte[] data);
	
	/** 
	 * Send data on a specific socket without modifying it 
	 * ( TLS encoding, etc )
	 */
	public void sendRaw(SocketChannel socket, byte[] data);

	/** Initiate a request to secure the stream using TLS on one socket */
	public void secure(SocketChannel socketChannel, SSLEngine sslEngine);	
}
