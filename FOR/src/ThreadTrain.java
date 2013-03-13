import java.util.*;
public class ThreadTrain implements Runnable{
	String name;
	Thread t;
	int train;
	int trainEnd;
	int ender;
	Date finish, start;
	ThreadTrain(String threadname,int i){
		name=threadname;
		t=new Thread(this,name);
		t.start();
		if(i==1){
			train=i;
			trainEnd=i+10;
		}else if(i>1){
			train=i*10-10;
			trainEnd=i*10;
		}
		ender=i;
	}
	public void run(){
		for (int k=train; k<trainEnd; k++){ 
	        for (int j=2; j<k; j++)
	        {
	            if (k%j==0){
	                break;
	            }else if (k==j+1){
	            }
	        }
		}
		if(ender==49999){
			finish=new Date();
			System.out.println("LOOP END "+finish);
		}else if (ender==1){
			start=new Date();
			System.out.println("LOOP START "+start);
		}else{}
	}
}