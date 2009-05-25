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
package topchat.server.gui;

import topchat.server.util.Constants;

/**
 * Interface containing constants used in the GUI
 */
public interface GuiConstants extends Constants {

	public static final int APP_WIDTH = 700;
	public static final int APP_HEIGHT = 500;

	public static final int USERS_LIST_MIN_W = 100;
	public static final int USERS_LIST_MIN_H = 150;

	public static final int ROOMS_LIST_MIN_W = USERS_LIST_MIN_W;
	public static final int ROOMS_LIST_MIN_H = USERS_LIST_MIN_H;

	public static final String DEFAULT_STATUS = "";
}
