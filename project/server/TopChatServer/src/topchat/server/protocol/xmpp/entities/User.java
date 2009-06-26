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
package topchat.server.protocol.xmpp.entities;

import org.apache.log4j.Logger;

import topchat.server.protocol.xmpp.connmanager.XMPPConnectionManager;



public class User {
	public String username = null;
	public String something = null;
	public String serverDomain = null;
	public String pass = null;
	public String resource = null;
	public String status = null;
	
	public XMPPConnectionManager manager;
	
	private static Logger logger = Logger.getLogger(User.class);
	
	public User(String username, String something, String pass)
	{
		this.username = username;
		this.something = something;
		this.serverDomain = "example.com";
		this.pass = pass;
		 
	}
	
	public void setResource(String resource)
	{
		this.resource = resource;
	}
	
	@Override
	public String toString()
	{
		return username + "@" + serverDomain + "/" + resource;
	}
	
	public void setManager(XMPPConnectionManager manager)
	{
		this.manager = manager;
	}
	
	public void setStatus(String status)
	{
		logger.debug("user " + toString() + " has status " + status);
		this.status = status;
	}
}
