import java.io.*;

public class DataRead {
	FileInputStream _in;
	String _Fname = "DownloadFile.txt";

	DataRead() {
	}

	DataRead(String in) {
		_Fname = in;
	}

	public void Read() {
		int i;
		try {
			_in = new FileInputStream(_Fname);
		} catch (FileNotFoundException ex) {
			System.out.println("File not found");
			return;
		}
		try {
			do {
				i = _in.read();
				if (i != -1) {
					System.out.print((char) i);
				}
			} while (i != -1);
		} catch (IOException exc) {
			System.out.println("Read error");
		}
		try {
			_in.close();
		} catch (IOException exc) {
			System.out.println("Error on close");
		}
	}
}
