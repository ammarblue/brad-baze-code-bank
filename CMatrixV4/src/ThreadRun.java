
public class ThreadRun implements Runnable{
	String name;
	Thread t;
	Matrix m;
	ThreadRun(String threadname, Matrix x){
		m = x;
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
		for(;m.jb<=m.jt;m.jb++){
			m.matrixC[m.k][m.jb]=m.matrixA[m.k][m.jb]+m.matrixB[m.k][m.jb];
			//System.out.println(": k "+m.k+", jb "+m.jb+", jt "+m.jt);
		}
		m.b++;
	}
}
