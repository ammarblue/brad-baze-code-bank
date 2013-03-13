import java.util.*;
public class terminaLibrary extends GUI{
	String out;
	String ftime;
	terminaLibrary(){}
	public String read(String IN,int OpS){
		if(IN.equals("START<#")){
			System.out.println(IN);
			return "PLEASE STATE PROGRAM TO RUN";
		}else if(IN.equals("DATE<#")){
			Date d=new Date();
			return "DATE: "+d.toString();
		}else if(IN.equals("<#")){
			return "TEST";
		}else if(IN.equals("VIEW DATA<#")){
			return super.MatrixSwap();
		}else if(IN.equals("START MATRIXADD<#")||IN.equals("MATRIXADD<#")){
			Matrix A=new Matrix(this);
			A.setMatrix();
			A.worker();
			A.mStore();
			ftime=A.ftime();
			return "MATRIX ADD COMPLEATED WITHOUT ERROR";
		}else if(IN.equals("HELP<#")){
			return "This is the JAVA OpenMP for loop test implamentation program.\nThis is a terminal based program.\n" +
					"There are several comands that can be run from this terminal.\n" +
					"HELP: list this and comands\n" +
					"DATE: give you the date and time on the time line\n" +
					"START <program>:runs a specified prgram\n" +
					"VIEW DATA: will show(if any) date that is stored in memmory\n" +
					"TEST: prints a test string\n";
		}else if(IN.equals("TIME<#")){
			return "TOTAL RUN TIME: "+ftime;
		}else{
			return "ERROR NO SUCH COMMAND";
		}
	}
}
