/*
 * WebStartFileHandler.java - class of the jQuantum computer simulator
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

import java.io.File;
import java.io.IOException;

import javax.jnlp.ExtendedService;
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.FileSaveService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

/**
 * Handles loading files from the local filesystem using the Java WebStart API,
 * which asks the user for permissions for individual file reads and writes if
 * the application is unsigned.
 * 
 * Insulates the rest of the code from the Java WebStart API so that if we're
 * not running in a WebStart environment and the javax.jnlp API is not
 * available, the system will still run by not making a reference to this class
 * and thereby not trying to import missing classes.
 * 
 * @author Dave Boden
 * @version 1.0
 */
public class WebStartFileHandler {

	/**
	 * Returns the contents of a web start file.
	 * 
	 * @param fileDirectoryHint
	 *            the most recently used file directory
	 * @return the contents of a web start file
	 * @throws IOException
	 * @throws WebStartFileHandlerNotAvailableException
	 */
	public WebStartFileContents getFileContents(String fileDirectoryHint)
			throws IOException, WebStartFileHandlerNotAvailableException {
		try {
			FileOpenService fos = (FileOpenService) ServiceManager
					.lookup("javax.jnlp.FileOpenService");
			FileContents fileContents = fos.openFileDialog(fileDirectoryHint,
					null);
			if (fileContents == null) {
				// User did not choose a file
				return null;
			} else {
				WebStartFileContents webStartFileContents = new WebStartFileContents();
				webStartFileContents.setFileName(fileContents.getName());
				webStartFileContents.setInputStream(fileContents
						.getInputStream());
				return webStartFileContents;
			}
		} catch (UnavailableServiceException ex) {
			throw new WebStartFileHandlerNotAvailableException(
					"javax.jnlp.FileOpenService");
		}
	}

	/**
	 * Saves the contents of the given web start file.
	 * 
	 * @param fileDirectoryHint
	 *            the directory into which the file is to be saved
	 * @param webStartFileContents
	 *            the file contents to be saved
	 * @return true if and only if the file contents could be saved successfully
	 * @throws IOException
	 * @throws WebStartFileHandlerNotAvailableException
	 */
	public boolean saveFileContents(String fileDirectoryHint,
			WebStartFileContents webStartFileContents) throws IOException,
			WebStartFileHandlerNotAvailableException {
		try {
			FileSaveService fss = (FileSaveService) ServiceManager
					.lookup("javax.jnlp.FileSaveService");
			FileContents fileContents = fss.saveFileDialog(fileDirectoryHint,
					null, webStartFileContents.getInputStream(),
					webStartFileContents.getFileName());
			// Return false if user cancelled
			return fileContents != null;
		} catch (UnavailableServiceException ex) {
			throw new WebStartFileHandlerNotAvailableException(
					"javax.jnlp.FileSaveService");
		}
	}

	/**
	 * Returns the contents of the given web start file.
	 * 
	 * @param file
	 *            the file the webs start content is to be read of
	 * @return the contents of web start file
	 * @throws IOException
	 * @throws WebStartFileHandlerNotAvailableException
	 */
	public WebStartFileContents openFile(File file) throws IOException,
			WebStartFileHandlerNotAvailableException {
		try {
			ExtendedService exs = (ExtendedService) ServiceManager
					.lookup("javax.jnlp.ExtendedService");
			FileContents fileContents = exs.openFile(file);
			WebStartFileContents webStartFileContents = new WebStartFileContents();
			webStartFileContents.setFileName(fileContents.getName());
			webStartFileContents.setInputStream(fileContents.getInputStream());
			return webStartFileContents;
		} catch (UnavailableServiceException ex) {
			throw new WebStartFileHandlerNotAvailableException(
					"javax.jnlp.ExtendedService");
		}
	}
}