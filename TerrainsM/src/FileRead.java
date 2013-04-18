import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import processing.core.PVector;

public class FileRead {
	Scanner _in = null;
	String _Fname = "test.txt";
	PhotoGen store = new PhotoGen();;

	FileRead() {
	}

	FileRead(String in) {
		_Fname = in;
	}

	FileRead(PhotoGen in) {
		store = in;
	}

	public void Read() {
		int i;
		try {
			_in = new Scanner(new BufferedReader(new FileReader(_Fname)));
			while (_in.hasNext()) {
				String n = _in.next();
				int noi = _in.nextInt();
				boolean cl = _in.nextBoolean();
			}
		} catch (FileNotFoundException ex) {
			System.err.println("FILE NOT FOUND ERROR");
		} finally {
			if (_in != null) {
				_in.close();
			}
		}
	}
}
