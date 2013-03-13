import java.io.*;
public class FileRun {
	String fName;
	Parser p;
	int count;
	FileRun(String in){
		fName=in;
		p=new Parser();
	}
	public void readIn(){
		try{
			FileInputStream fstream=new FileInputStream(fName);
			DataInputStream in=new DataInputStream(fstream);
			BufferedReader br=new BufferedReader(new InputStreamReader(in));
			String lineIn;
			br.mark(500);
			while((lineIn=br.readLine())!=null){
				count++;
			}
			p.store(count);
			br.reset();
			count=0;
			while((lineIn=br.readLine())!=null){
				p.data[count]=lineIn;
				count++;
			}
		}catch(Exception e){
			System.err.println("Error: "+e.getMessage());
		}
		p.change();
		p.print();
	}
	public void writeOut(){
		try{
		    	BufferedWriter out = new BufferedWriter(new FileWriter("test2.txt"));
		    	out.write("aString");
		    	out.close();
		} catch (IOException e) {
			System.err.println("ERROR: "+e.getMessage());
		}
	}
}
