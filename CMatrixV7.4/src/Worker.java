
public class Worker {
	Schedualer Sc;
	Worker(){
		Sc=new Schedualer();
		Sc.thread_start();
	}
}
