package reuze.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;

import com.software.reuze.ff_XMLReader;
import com.software.reuze.ff_i_XMLInputStates;
import com.software.reuze.z_Processing;

public class ff_XMLReaderInputStream {
	public static z_Processing app;

	public static void loadXML(InputStreamReader tmxFile,
			final ff_i_XMLInputStates listener) {
		try {
			ff_XMLReader xmlReader = new ff_XMLReader() {
				Stack<String> currBranch = new Stack<String>();

				@Override
				protected void open(String name) {
					currBranch.push(name);
					listener.open(name);
				}

				@Override
				protected void attribute(String name, String value) {
					listener.attribute(name, value, currBranch.peek());
				}

				@Override
				protected void text(String text) {
					listener.text(text);
				}

				@Override
				protected void close() {
					listener.close(currBranch.pop());
				}
			};
			xmlReader.parse(tmxFile);
		} catch (IOException e) {
			throw new RuntimeException("Error Parsing XML file", e);
		}
	}

	public static void loadXML(File tmxFile, final ff_i_XMLInputStates listener) {
		try {
			loadXML(new InputStreamReader(new FileInputStream(tmxFile)),
					listener);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error Parsing XML file", e);
		}
	}

	public static void loadXML(z_Processing appx, String fname,
			final ff_i_XMLInputStates listener) {
		app = appx;
		InputStream istream = appx.createInput(fname);
		if (istream == null)
			throw new RuntimeException("Error Parsing XML file " + fname);
		loadXML(new InputStreamReader(istream), listener);
	}

	public static void loadXML(InputStream is,
			final ff_i_XMLInputStates listener) {
		if (is == null)
			throw new RuntimeException("Error Parsing XML stream ");
		loadXML(new InputStreamReader(is), listener);
	}
}
