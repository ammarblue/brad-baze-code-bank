
public class ThreadRun extends Matrix implements Runnable {
	String name;
	Thread t;
	int cnt;
	ThreadRun(String threadname){
		name=threadname;
		cnt=Integer.parseInt(name);
		t=new Thread(this,name);
		try{
			Thread.sleep(0);
		}catch(InterruptedException e){
			System.out.println("ERROR");
		}
		t.start();
	}
	public void run(){
		for(int i=0;i<10;i++){
			
		}

	}
}
