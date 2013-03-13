import java.util.*;
public class Main {
	public static void main(String args[]){
		Date MAIN=new Date();
		Matrix A=new Matrix();
		A.setMatrix();
		A.worker();
		//A.store(temp);
		A.mPrint();
		Date FMAIN=new Date();
		System.out.println("START AND END TIME OF MAIN    "+MAIN+" "+FMAIN);
		System.out.println("END OF LINE");
	}
}
