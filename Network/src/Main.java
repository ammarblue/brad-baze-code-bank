import java.io.*;
import java.util.*;
import java.net.*;
public class Main {
	public static void main(String args[]){
		String ip="192.168.137.1";
		String ping_out="";
		String ping_cmd="ping "+ip;
		try{
			Runtime r=Runtime.getRuntime();
			Process p=r.exec(ping_cmd);
			BufferedReader in=new BufferedReader(new InputStreamReader(p.getInputStream()));
			String input_line;
			while((input_line=in.readLine())!=null){
				System.out.println(input_line);
				ping_out+=input_line;
			}
			in.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}
}
