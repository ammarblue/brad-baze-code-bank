import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class Worker extends Thread {
	LinkedBlockingQueue<Work> q;
	Work w; 
	public Worker(String name, LinkedBlockingQueue<Work> q) {
		super(name);
		this.q=q;
	}
	public void run() {
		System.out.println(this.getName()+" START");
		for (;;) {
			try {
				w = q.poll(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				System.out.println(this.getName()+" interrupted");
				break;
			}
			if (w!=null) {
				System.out.println(this.getName()+" got one");
				if (!w.where.evaluate(w)) break;
			} else System.out.println(this.getName()+" woke up");
		}		
		System.out.println(this.getName()+" DONE");
	}
}
