
public class RUNNER implements Runnable{
	int state;
	Thread t;
	RUNNER(int ID){
		t=new Thread(this,Integer.toString(ID));
		t.start();
	}
	public void run(){
		while(state==0){//if they are starting or just practicing they do nothing 
			try{
				//System.out.println("Thread "+t.getName()+" ON DECK");
				t.sleep(0);		
			}catch(InterruptedException e){
				System.out.println("ERROR AT WAIT ON THREAD "+t.getName());
			}
		}
		System.out.println("RUNNING THREAD "+t.getName());
	}
	public void retire(){
		System.out.println("ENDDING THREAD "+t.getName());
	}
}
