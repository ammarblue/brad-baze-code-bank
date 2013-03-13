import java.util.*;
public class terminaLibrary {
	public String out;
	terminaLibrary(){}
	public String read(String IN,int OpS){
		if(IN.equals("start<#")){
			System.out.println(IN);
			if(OpS==1){
				return "RUNNING: MATRIX ADD";
			}else if(OpS==2){
				return "RUNNING: PRIME NUMBER";
			}else{
				return "NO PROGRAM TO RUN";
			}
		}else if(IN.equals("date<#")){
			Date d=new Date();
			return "DATE: "+d.toString();
		}else if(IN.equals("<#")){
			return "TEST";
			//run gui defaults
		}else{
			return "ERROR NO SUCH COMMAND";
		}
	}
}
