
public class ThreadRun implements Runnable{
	String name;
	Thread t;
	int MA,MB,MC;
	ThreadRun(String threadname,int A,int B){
		name=threadname;
		t=new Thread(this,name);
		t.start();
		MA=A;
		MB=B;
	}
	public void run(){
		MC=MA+MB;
		System.out.println(MC);
	}
	public int getMC(){
		return MC;
	}
}
