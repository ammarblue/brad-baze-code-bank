/*
 * WebStartFileHandlerNotAvailableException.java - class of jQuantum
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

/**
 * This is an exception that is thrown whenever a web start file handler is not
 * available.
 * 
 * @author Dave Boden
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WebStartFileHandlerNotAvailableException extends Exception {
	/**
	 * Constructs a WebStartFileHandlerNotAvailableException.
	 * 
	 * @param serviceName
	 *            the service name for the detail message (which is saved for
	 *            later retrieval by the {@link Throwable#getMessage()} method)
	 */
	public WebStartFileHandlerNotAvailableException(String serviceName) {
		super("Running in a WebStart environment but " + serviceName
				+ " is not available.");
	}
}
