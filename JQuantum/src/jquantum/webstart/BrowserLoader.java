/*
 * BrowserLoader.java - class of the jQuantum computer simulator
 *
 * Copyright (C) 2009 Dave Boden
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses
 * or write to the Free Software Foundation,Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA 02110-1301  USA
 */
package jquantum.webstart;

import java.net.URL;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

/**
 * Provides a static method to direct the browser to a specified URL.
 * 
 * @author Dave Boden
 * @version 1.0
 */
public class BrowserLoader {
	// Suppresses default constructor, ensuring non-instantiability.
	private BrowserLoader() {
	}

	/**
	 * Direct the browser to the specified URL.
	 * 
	 * @param url
	 *            the URL the browser is to be directed to
	 * @return true if the URL is successfully shown in the browser
	 */
	public static boolean showInBrowser(URL url) {
		try {
			BasicService service = (BasicService) ServiceManager
					.lookup("javax.jnlp.BasicService");
			service.showDocument(url);
			return true;
		} catch (UnavailableServiceException ex) {
			return false;
		}
	}
}
