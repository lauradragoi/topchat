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
package topchat.server.net;

import java.nio.channels.SocketChannel;

import javax.net.ssl.SSLEngine;

public class ChangeRequest 
{
	  public static final int REGISTER = 1;
	  public static final int CHANGEOPS = 2;
	  
	  public SocketChannel socket;
	  public int type;
	  public int ops;
	  public SSLEngine sslEngine;
	  
	  public ChangeRequest(SocketChannel socket, int type, int ops) {
	    this.socket = socket;
	    this.type = type;
	    this.ops = ops;
	  }
	  
	  public ChangeRequest(SocketChannel socket, int type, SSLEngine sslEngine) {
		    this.socket = socket;
		    this.type = type;
		    this.sslEngine = sslEngine;
	  }	  
}
