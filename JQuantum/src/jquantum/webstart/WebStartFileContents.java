/*
 * WebStartFileContents.java - class of the jQuantum computer simulator
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

import java.io.InputStream;

/**
 * Represents the contents of a web start file as an input stream.
 * 
 * @author Dave Boden
 * @version 1.0
 */
public class WebStartFileContents {

	private String fileName;
	private InputStream inputStream;

	/**
	 * Returns the name of the web start file.
	 * 
	 * @return the name of the web start file
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns the name of this web start file.
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Returns the input stream as the content of this web start file.
	 * 
	 * @return the input stream as the content of this web start file
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Sets the input stream as the content of this web start file.
	 * 
	 * @param inputStream
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
