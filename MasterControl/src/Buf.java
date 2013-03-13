import java.util.*;
import java.io.*;
public class Buf implements Runnable{
	Thread t;
	Scanner in;
	MC mc;
	boolean state=false;
	Buf(MC in){
		mc=in;
		t.start();
	}
	public void run(){
		try {
			in=new Scanner(new File("Data.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void getJobs(){
		
	}
}
