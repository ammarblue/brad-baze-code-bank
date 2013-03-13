
public class ThreadRun implements Runnable{
	String name;
	Thread t;
	int cnt,i,jb,jt;
	Matrix m;
	ThreadRun(String threadname, Matrix x){
		m = x;
		name=threadname;
		t=new Thread(this,name);
		cnt=Integer.parseInt(name);
		i=cnt/10;
		jb=(cnt-(10*i))*10;
		jt=jb+9;
		t.start();
		try{
			Thread.sleep(0);
		}catch(InterruptedException e){
			System.out.println("ERROR");
		}
	}
	public void run(){
		for(;jb<=jt;jb++){
			m.matrixC[i][jb]=m.matrixA[i][jb]+m.matrixB[i][jb];
			//System.out.println(jb);
		}
		m.b++;
	}
}
