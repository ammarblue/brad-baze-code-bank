import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;


public final class JOMP {
	public static final int STATIC=1;
	public static final int RUNTIME=5;
	public static final int AUTO=4;
	public static final int DYNAMIC=2;
	public static final int GUIDED=3;
	public static final int MAX=8;
	public static final Worker t[]=new Worker[MAX];
	public static int N=8;
	public static final LinkedBlockingQueue<Work> q=new LinkedBlockingQueue<Work>();
	public static final Critical default_critical=new Critical();
	public static final ConcurrentHashMap<String,Critical> criticals= new ConcurrentHashMap<String,Critical>();
	public static final long start=System.nanoTime();
	public static int policy=STATIC;//control statement
	public static int chunk=0;
	public static synchronized final int getPolicy() {
		return policy;
	}
	public static synchronized final void setPolicy(int policy, int chunk) {
		JOMP.policy = policy; JOMP.chunk = chunk;
	}
	public static synchronized final int getChunk() {
		return chunk;	
	}
	static {
		for (int i=1; i<=N; i++) {
			t[i-1]=new Worker("T"+i, q);
			t[i-1].start();
		}
	}
	public static final void work(IWork w, int taskid) {
		work(w, taskid, N);
	}
	public static final void work(IWork w, int taskid, int nThreads) {
		CountDownLatch c=new CountDownLatch(N);
		for (int i=0; i<N; i++) {
			Work work=new Work(w, 0, 0, taskid, i, nThreads, c);
			q.add(work);
		}
		try {
			c.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static final void work(IWork w, int start, int end, int taskid) {
		work(w, start, end, 1, taskid, N, STATIC, 0);
	}
	public static final void work(IWork w, int start, int end, int step, int taskid) {
		work(w, start, end, step, taskid, N, STATIC, 0);
	}
	public static final void work(IWork w, int start, int end, int step, int taskid, int nThreads) {
		work(w, start, end, step, taskid, nThreads, STATIC, 0);
	}
	public static final void work(IWork w, int start, int end, int step, int taskid, int nThreads, int schedule, int chunk) {
		//TODO add bypass for N=1 case?
		if (nThreads<1) nThreads=N;
		else nThreads=Math.min(nThreads, MAX);
		if (schedule==RUNTIME) {schedule=policy; chunk=JOMP.chunk;}
		if (schedule==AUTO) {schedule=STATIC; chunk=0;}
		CountDownLatch c=null;
		Work work;
		switch (schedule) {
		case STATIC:
			if (chunk==0) {
				c=new CountDownLatch(nThreads);
				int k=(end-start+1)/nThreads;
				int j=k%step;
				if (j!=0) k+=step-j;
				for (int i=1; i<nThreads; i++) {
					work=new Work(w, start, start+k-1, taskid, i-1, nThreads, c);
					q.add(work);
					start+=k;
				}
				work=new Work(w, start, end, taskid, nThreads-1, nThreads, c);
				q.add(work);
			} else assert(false);
			break;
		case DYNAMIC: {
				int k=chunk%step;
				if (k!=0) chunk+=step-k;
				k=(end-start+1)/chunk;
				int j=(end-start+1)%chunk; 
				c=new CountDownLatch(j==0?k:k+1);
				int i=0;
				while (--k>=0) {
					work=new Work(w, start, start+chunk-1, taskid, i, nThreads, c);
					q.add(work);
					start+=chunk;
					i=(i+1)%nThreads;
				}
				if (j!=0) {
					work=new Work(w, start, end, taskid, i, nThreads, c);
					q.add(work);
				}
			}
			break;
		case GUIDED: {
			int j=(end-start+1);			
			int i; int p=0;
			do {
				int m=Math.max(j/nThreads, chunk);
				if (m==0) {p++; break;}
				int k=m%step;
				if (k!=0) m+=step-k;
				j-=m;
				if (j<0) m=chunk+j;
				p++;
			} while (j>0);
			c=new CountDownLatch(p);
			j=(end-start+1);			
			i=0;
			do {
				int m=Math.max(j/nThreads, chunk);
				if (m==0) {
					work=new Work(w, start, end, taskid, i, nThreads, c);
					q.add(work);
					break;
				}
				int k=m%step;
				if (k!=0) m+=step-k;
				j-=m;
				if (j<0) m=chunk+j;
				work=new Work(w, start, start+m-1, taskid, i, nThreads, c);
				q.add(work);
				start+=m;
				i=(i+1)%nThreads;
			} while (j>0);
			break;
		}
		} //switch
		try {
			c.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static final void finish() {
		for (int i=1; i<=t.length; i++) if (t[i-1]!=null) t[i-1].interrupt();
	}
	public static class Barrier {
		int n=0; int limit;
		public Barrier(int n) {
			assert n<32; limit=(1<<n)-1;
		}
		public final synchronized boolean step() {
			n |= 1<<omp_get_thread_num();
			return n==limit;
		}
		public boolean single() {
			boolean first;
			synchronized (this) {
				first= n==0;
				if (first) n |= 1<<omp_get_thread_num();
			}
			return first;
		}
	}
	public static class Critical extends Object {
	}
	public static final Critical critical() {
		return default_critical;
	}
	public static final Critical critical(String name) {
		Critical c = criticals.get(name);
		if (c==null) {
			c = new Critical();
			criticals.put(name, c);
		}
		return c;
	}
	public static final double omp_get_wtime() {
		return (double)(System.nanoTime()-start)/10.0e9;
	}
	public static final double omp_get_wtick() {
		return 10.0e-6;
	}
	public static final int omp_get_thread_num() {
		Thread t=Thread.currentThread();
		if (t instanceof Worker) return ((Worker)t).w.who;
		return -1;
	}
	public static final int omp_get_num_threads() {
		Thread t=Thread.currentThread();
		if (t instanceof Worker) return ((Worker)t).w.nTeam;
		return 1;
	}
	public static final int omp_get_max_threads() {
		return N;
	}
	public static final int omp_get_num_procs() {
		return MAX;
	}
	public static final void omp_set_num_threads(int n) {
		N=n;
	}
	public static boolean isMaster() {
		return omp_get_thread_num()<=0;
	}
	public static boolean omp_in_parallel() {
		return omp_get_thread_num()>=0;
	}
}
