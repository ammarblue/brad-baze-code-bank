import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataRead {
	String _Fname = "";
	File _in;
	String text = "";

	DataRead() {
	}

	DataRead(String in) {
		_Fname = in;
	}

	public void Read() {
		Scanner in;
		if (_Fname.compareTo("") == 0) {
			_Fname = "test.txt";
		}
		_in = new File(_Fname);
		try {
			in = new Scanner(_in);
			while (in.hasNextLine()) {
				text += text.concat(" " + in.nextLine());
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
