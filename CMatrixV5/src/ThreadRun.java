
public class ThreadRun implements Runnable{
	String name;
	Thread t;
	Matrix m;
	Slice c;
	ThreadRun(String threadname, Matrix x,Slice In){
		m=x;
		c=In;
		name=threadname;
		t=new Thread(this,name);
		t.start();
		try{
			Thread.sleep(0);
		}catch(InterruptedException e){
			System.out.println("ERROR");
		}
	}
	public void run(){
		for(;c.jb<=c.jt;c.jb++){
			m.matrixADD(c.k,c.jb);
			//System.out.println("Thread "+name+" k: "+c.k+" jb: "+c.jb+" jt: "+c.jt);
		}
		m.b++;
	}
}
