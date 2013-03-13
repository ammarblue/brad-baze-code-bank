import java.util.*;
public class NewThreads implements Runnable{
	String name;
	Thread t;
	int setUp;
	int setDown;
	NewThreads(String threadname, int iterationDown,int iterationUp){
		name=threadname;
		t=new Thread(this,name);
		t.start();
		setUp=iterationUp;
		setDown=iterationDown;
	}
	public void run(){
			for (int k=setDown; k<setUp; k++){ 
		        for (int j=2; j<k; j++)
		        {
		            if (k%j==0){
		                break;
		            }else if (k==j+1){
		            }
		        }
			}
			Date d6=new Date();
			System.out.println(name+" "+d6+" exiting");
	}
}