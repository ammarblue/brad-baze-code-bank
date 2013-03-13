//this is the threads them selfs
public class THREADS implements Runnable{
	Thread t;
	Operations o;
	Slice s;
	String op;
	THREADS(Operations ino,Slice ins,String op){
		o=ino;
		s=ins;
		op=op;
		t=new Thread(this);
		t.start();
	}
	public void run(){
		o.op(op, s.a, s.b);//no computations are on in the thread but in the Operations object that was created in schedualer
	}
}
