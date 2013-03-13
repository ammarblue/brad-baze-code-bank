
public class Schedualer {
	Slice S;
	Operations O;
	Data D;
	Threads T[];
	boolean done;
	Schedualer(){
		S=new Slice();
		O=new Operations();
		T=new Threads[S.NUM_THREADS];
		D=new Data();
	}
	public void thread_start(){
		for(int i=0;i<S.NUM_THREADS;i++){
			T[i]=new Threads(Integer.toString(i),S,O,D,this);
		}
	}
	public void print(){
		for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				System.out.println(D.MC[i][j]);
			}
		}
	}
}
