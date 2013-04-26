import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class DataRead {
	FileInputStream _in;
	File _out,_out2;
	String _InFname = "DownloadFile.txt";
	String _OutFname = "loocv_idw.txt";
	String _OutFname2= "error_statistics_idw.txt";
	day[] year;

	DataRead() {
	}

	DataRead(day[] inY, String in, String out) {
		_InFname = in;
		_OutFname = out;
		this.year = inY;
	}

	DataRead(day[] inY) {
		this.year = inY;
	}

	public void Read() {
		int i;
		try {
			_in = new FileInputStream(_InFname);
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

	public void Write() {
		_out = new File(_OutFname);
		FileWriter wr;
		try {
			if (!_out.exists()) {
				_out.createNewFile();
			}
			wr = new FileWriter(_out);
			BufferedWriter w = new BufferedWriter(wr);
			for (int i = 0; i < year.length; i++) {
				for (int j = 0; j < year[i].points.size(); j++) {
					IDW test = new IDW(year, year[i].points.get(j).x,
							year[i].points.get(j).y);
					test.setT(i);
					w.write(test.forPrint(year[i].points.get(j)));
				}
			}
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("DONE");
	}

	public void ReadforError() {
		m_Idw_Error err = new m_Idw_Error();
		int count = 0;
		try {
			_out = new File(_OutFname);
			Scanner s1 = new Scanner(_out);
			Scanner s2;
			while (s1.hasNextLine()) {
				s2 = new Scanner(s1.nextLine());
				while (s2.hasNext()) {
					switch (count) {
					case 0:
						err.lis[count].add(s2.nextDouble());
						count++;
						break;
					case 1:
						err.lis[count].add(s2.nextDouble());
						count++;
						break;
					case 2:
						err.lis[count].add(s2.nextDouble());
						count++;
						break;
					case 3:
						err.lis[count].add(s2.nextDouble());
						count++;
						break;
					case 4:
						err.lis[count].add(s2.nextDouble());
						count++;
						break;
					case 5:
						err.lis[count].add(s2.nextDouble());
						count++;
						break;
					case 6:
						err.lis[count].add(s2.nextDouble());
						count++;
						break;
					case 7:
						err.lis[count].add(s2.nextDouble());
						count++;
						break;
					case 8:
						err.lis[count].add(s2.nextDouble());
						count++;
						break;
					case 9:
						err.lis[count].add(s2.nextDouble());
						count = 0;
						break;
					default:
						System.err.print("ERROR ON READ");
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		FileWriter wr;
		_out2=new File(_OutFname2);
		try {
			if (!_out2.exists()) {
				_out2.createNewFile();
			}
			wr = new FileWriter(_out2);
			BufferedWriter w = new BufferedWriter(wr);
			wr.write(Arrays.toString(err._Mae())+"\n");
			wr.write(Arrays.toString(err._Mse())+"\n");
			wr.write(Arrays.toString(err._Rmse())+"\n");
			wr.write(Arrays.toString(err._Mare())+"\n");
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("DONE");
	}
}
