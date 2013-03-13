
public class Schedualer {
	Slice S;
	Operations O;
	Threads T[];
	Schedualer(){
		S=new Slice();
		O=new Operations();
		T=new Threads[S.NUM_THREADS];
	}
	public void thread_start(){
		for(int i=0;i<S.NUM_THREADS;i++){
			T[i]=new Threads(Integer.toString(i),S,O);
		}
	}
}
