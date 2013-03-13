import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
public class Read {
	String symbol;
	String tag;
	Read(){
		setSymbol(null);
		printData();
	}
	public void setSymbol(String in){
		Scanner get=new Scanner(System.in);
		if(in!=null){
			symbol=in;
		}else{
			System.out.println("ENTER STOCK SYMBOL THEN HIT ENTER TO ADD MORE");
			System.out.println("TYPE RUN TO START DATA STREAM");
			symbol=get.next();
			while(true){
				if(get.hasNext("RUN")){break;}
				symbol=symbol.concat("+");
				symbol=symbol.concat(get.next());
			}
		}
	}
	public void setTag(String in){
		
	}
	public void printData(){
		try{
			URL site=new URL("http://finance.yahoo.com/d/quotes.csv?s="+symbol+"&f=shg");
			URLConnection conn=site.openConnection();
			InputStreamReader inStream=new InputStreamReader(conn.getInputStream());
			BufferedReader buff=new BufferedReader(inStream);
			String data=buff.readLine();
			while(data!=null){
				System.out.println(data);
				data=buff.readLine();
			}
		}catch(Exception e){
			System.out.println("ERROR");
		}
	}
	public void options(int in){
		if(in==0){
			
		}else if(in==1){
			
		}else{
			System.out.println("Not a option list");
		}
	}
}
