import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class as_SimulationDiscrete {
	static AtomicInteger nThreads = new AtomicInteger(),
			bThreads = new AtomicInteger();
	static PriorityBlockingQueue<as_SimulationThread> q = new PriorityBlockingQueue<as_SimulationThread>();
	
	static GraphingData graph = new GraphingData();
	
	public static void Hold(double delay) {
		if (delay < 0)
			throw new RuntimeException("wait time less than zero");
		if (!(Thread.currentThread() instanceof as_SimulationThread))
			throw new RuntimeException("not a simulation thread");
		as_SimulationThread t = (as_SimulationThread) Thread.currentThread();
		t.eventTime = as_SimulationThread.clock + delay;
		if (delay != 0)
			System.out.println("time=" + as_SimulationThread.clock
					+ " delaying " + t);
		bThreads.incrementAndGet();
		t.block(q);
	}

	public static void Hold(int delay) {
		Hold((double) delay);
	}

	public static void Hold(float delay) {
		Hold((double) delay);
	}

	public static void Hold(long delay) {
		Hold((double) delay);
	}

	public static double time() {
		return as_SimulationThread.clock;
	}

	public static void PrintStats() {
	}

	public static void newThread() {
		as_SimulationThread t = (as_SimulationThread) Thread.currentThread();
		System.out.println("time=" + as_SimulationThread.clock + " starting "
				+ t);
		t.setPriority(2);
		nThreads.incrementAndGet();
		Hold(0);
	}

	public static void stopThread() {
		as_SimulationThread t = (as_SimulationThread) Thread.currentThread();
		nThreads.decrementAndGet();
		System.out
				.println("time=" + as_SimulationThread.clock + " ending " + t);
	}

	private static class T extends Thread {
		public T() {
			super();
			this.setPriority(1);
		}

		@Override
		public void run() {
			while (as_SimulationThread.clock == 0
					&& (bThreads.intValue() == 0 || bThreads.intValue() < nThreads
							.intValue()))
				try {
					sleep(1);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					System.exit(-1);
				}
			for (;;) {
				System.out.println("Enter Schedular");
				while (bThreads.intValue() < nThreads.intValue())
					try {
						sleep(1);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
						System.exit(-1);
					}
				as_SimulationThread t = null;
				if(as_SimulationThread.threadList.size()>1){
					sched();
				}
				try {
					t = q.poll(500, TimeUnit.MILLISECONDS);
					if(!as_SimulationThread.threadList.isEmpty()){
						as_SimulationThread.threadList.remove(t);
					}
					if (t == null)
						break;
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(-1);
				}
				as_SimulationThread.clock = t.eventTime;
				System.out.println("Exiting Schedular");
				System.out.println("time=" + as_SimulationThread.clock
						+ " running " + t);
				bThreads.decrementAndGet();
				try {
					t.bar.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(-1);
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			} // for
			System.out.println("TIME=" + as_SimulationThread.clock);
			as_SimulationEntity.Print();
			da_Histogram.Print();
			graph.makeGraph(da_Histogram.getData(), da_Histogram.getSteps());
			// System.exit(0);
		}
		public synchronized void sched(){
			int index=0;
			for(int i=1;i<as_SimulationThread.threadList.size();i++){
				if(as_SimulationThread.threadList.get(i-1).RT<as_SimulationThread.threadList.get(i).RT){
					index=i-1;
				}else{
					index=i;
				}
			}
			as_SimulationThread.threadList.get(index).setSimPriority(1);
		}
	}

	static {
		T x = new T();
		x.start();
	}
}
