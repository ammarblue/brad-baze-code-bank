
public class clock implements Runnable{
	Thread t;
	Job parent;
	boolean kill=false;
	clock(Job in){
		parent=in;
		t=new Thread();
		t.start();
	}
	public void run(){
		long run_time=System.currentTimeMillis();
		while(((run_time/1000)/60)!=((run_time/1000)/60)+parent.terminate_time){
		}
		parent.p.destroy();
	}
}
