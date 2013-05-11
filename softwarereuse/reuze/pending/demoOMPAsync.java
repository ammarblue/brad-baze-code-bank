package reuze.pending;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class demoOMPAsync {
	static final int N=100;

	static final class Test implements Callable<Double> {
		double x;
		public Test(double x) {this.x=x;}
		public Double call() throws Exception {
			return Math.sqrt(x);
		}		
	}
	static FutureTask<Double> sqrt(double x) {
		FutureTask<Double> f = new FutureTask<Double>(new Test(x));
		f.run();
		return f;
	}
	public static void main(String args[]) {
		long time=System.nanoTime();
		FutureTask f[]=new FutureTask[N];
		for (int i=0; i<N; i++) f[i]=sqrt(i);
		try {
			for (int i=0; i<N; i++) System.out.println(f[i].get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println(System.nanoTime()-time);
	}
}
