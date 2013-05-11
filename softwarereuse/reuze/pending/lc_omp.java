package reuze.pending;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public final class lc_omp {
	public static interface IWork {
		boolean evaluate(Work w);
	}
	public static class Work {
		int start, end, step;
		int task;
		CountDownLatch c;
		IWork where;
		int who;
		int nTeam;
		public Work(IWork w) {
			where=w; c=new CountDownLatch(1);
		}
		public Work(IWork w, int start, int end, int step, int task, int tid, int n, CountDownLatch c) {
			where=w; this.start=start; this.end=end; this.step=step; this.task=task; this.c=c; who=tid; nTeam=n;
		}
		public final void countDown() {
			if (c!=null) c.countDown();
		}
		public final void add() {
			try {
				q.put(this);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public final void await() {
			try {
				c.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			c=null; //class cannot be reused at present
		}
	}
	public static class Worker extends Thread {
		LinkedBlockingQueue<Work> q;
		Work w; 
		public Worker(String name, LinkedBlockingQueue<Work> q) {
			super(name);
			this.q=q;
		}
		public void run() {
			//System.out.println(this.getName()+" START");
			int i=0; long j=0;
			for (;;) {
				try {
					w = q.take();//poll(1, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					//e.printStackTrace();
					//System.out.println(this.getName()+" interrupted");
					break;
				}
				if (w!=null) {
					//System.out.println(this.getName()+" got task="+w.task);
					long k=System.nanoTime();
					boolean b=w.where.evaluate(w);
					w.countDown(); i++; j+=System.nanoTime()-k;
					//System.out.println(this.getName()+" done");
					if (!b) break;
				} //else System.out.println(this.getName()+" woke up");
			}		
			System.out.println(this.getName()+" "+i+" "+j);
		}
	}
	public static class Jobs {
		CountDownLatch c;
		int n,i;
		public Jobs(int n) {
			c=new CountDownLatch(n);
			this.n=n;
		}
		public void add(IWork w, int taskid) {
			Work work=new Work(w, 0, 0, 0, taskid, i++, n, c);
			try {
				q.put(work);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void await() {
			try {
				c.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			c=null; //class cannot be reused at present
		}
	}
	public static final int STATIC=1;
	public static final int RUNTIME=5;
	public static final int AUTO=4;
	public static final int DYNAMIC=2;
	public static final int GUIDED=3;
	public static int N=Runtime.getRuntime().availableProcessors();
	public static final int MAX=N*3/2;
	public static final Worker t[]=new Worker[MAX];
	public static final LinkedBlockingQueue<Work> q=new LinkedBlockingQueue<Work>();
	public static final Critical default_critical=new Critical();
	public static final ConcurrentHashMap<String,Critical> criticals= new ConcurrentHashMap<String,Critical>();
	public static final long start=System.nanoTime();
	public static int policy=STATIC;
	public static int chunk=0;
	public static synchronized final int getPolicy() {
		return policy;
	}
	public static synchronized final void setPolicy(int policy, int chunk) {
		lc_omp.policy = policy; lc_omp.chunk = chunk;
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
	//all N threads execute the same task and return only after all tasks complete
	public static final void work(IWork w, int taskid) {
		work(w, taskid, N);
	}
	public static final void work(IWork w, int taskid, int nThreads) {
		CountDownLatch c=new CountDownLatch(nThreads);
		try {
			for (int i=0; i<nThreads; i++) {
				Work work=new Work(w, 0, 0, 0, taskid, i, nThreads, c);
				q.put(work);
			}
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
		if (schedule==RUNTIME) {schedule=policy; chunk=lc_omp.chunk;}
		if (schedule==AUTO) {schedule=STATIC; chunk=0;}
		CountDownLatch c=null;
		Work work;
		try {
			switch (schedule) {
			case STATIC:
				if (chunk==0) {
					c=new CountDownLatch(nThreads);
					int k=(end-start+1)/nThreads;
					int j=k%step;
					if (j!=0) k+=step-j;
					for (int i=1; i<nThreads; i++) {
						work=new Work(w, start, start+k-1, step, taskid, i-1, nThreads, c); //TODO should be clone method in IWork so user can sub-class Work
						q.put(work);
						start+=k;
					}
					work=new Work(w, start, end, step, taskid, nThreads-1, nThreads, c);
					q.put(work);
				} else assert(false);
				break;
			case AUTO:
			case DYNAMIC: {
				int k=chunk%step;
				if (k!=0) chunk+=step-k;
				k=(end-start+1)/chunk;
				int j=(end-start+1)%chunk; 
				c=new CountDownLatch(j==0?k:k+1);
				int i=0;
				while (--k>=0) {
					work=new Work(w, start, start+chunk-1, step, taskid, i, nThreads, c);
					q.put(work);
					start+=chunk;
					i=(i+1)%nThreads;
				}
				if (j!=0) {
					work=new Work(w, start, end, step, taskid, i, nThreads, c);
					q.put(work);
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
						work=new Work(w, start, end, step, taskid, i, nThreads, c);
						q.put(work);
						break;
					}
					int k=m%step;
					if (k!=0) m+=step-k;
					j-=m;
					if (j<0) m=chunk+j;
					work=new Work(w, start, start+m-1, step, taskid, i, nThreads, c);
					q.put(work);
					start+=m;
					i=(i+1)%nThreads;
				} while (j>0);
				break;
			}
			} //switch
			c.await();
			return;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static final void finish() {
		for (int i=1; i<=t.length; i++) if (t[i-1]!=null) t[i-1].interrupt();
	}
	public static class Barrier {
		int n; int limit;
		public Barrier() {
			n = -1;
		}
		public Barrier(int n) {
			assert n<32; limit=(1<<n)-1;
		}
		public final synchronized boolean step() {
			if (n < 0) {
				limit=(1<<omp_get_num_threads())-1;
				n = 0;
			}
			n |= 1<<omp_get_thread_num();
			return n==limit;
		}
		public final synchronized boolean single() {
			if (n < 0) {
				limit=(1<<omp_get_num_threads())-1;
				n = 0;
			}
			return n==0;
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
		return (double)(System.nanoTime()-start)/1.0e9;
	}
	public static final double omp_get_wtick() {
		return 10.0e-6;
	}
	public static final int omp_get_thread_num() {
		Thread t=Thread.currentThread();
		if (t instanceof Worker) return ((Worker)t).w.who;
		return 0;
	}
	public static final int omp_get_num_threads() {
		Thread t=Thread.currentThread();
		if (t instanceof Worker) return ((Worker)t).w.nTeam;
		return 1;
	}
	public static final int omp_get_max_threads() {
		return MAX;
	}
	public static final int omp_get_num_procs() {
		return Runtime.getRuntime().availableProcessors();
	}
	public static final void omp_set_num_threads(int n) {
		N=n;  //TODO need some checks here
	}
	public static final boolean isMaster() {
		return omp_get_thread_num()<=0;
	}
	public static final boolean omp_in_parallel() {
		Thread t=Thread.currentThread();
		return t instanceof Worker;
	}
}
