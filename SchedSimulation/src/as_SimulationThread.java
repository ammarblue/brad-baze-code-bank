import java.util.Comparator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.PriorityBlockingQueue;

public class as_SimulationThread extends Thread implements
		Comparable<as_SimulationThread> {
	public static double clock;
	double eventTime;
	int priority;
	CyclicBarrier bar;
	Request request;

	static class Request implements Comparable<Request> {
		int num, priority;
		double entryTime;
		as_SimulationThread owner;

		public Request(as_SimulationThread owner) {
			this.owner = owner;
		}

		public int compareTo(Request o) {
			return priority < o.priority ? -1 : (priority > o.priority ? 1 : 0);
		}
	}

	public as_SimulationThread(String name) {
		super();
		this.setName(name);
		bar = new CyclicBarrier(2);
		request = new Request(this);
	}

	@Override
	public void run() {
		as_SimulationDiscrete.newThread();
	}

	public int compareTo(as_SimulationThread arg0) {
		as_SimulationThread t1 = this;
		as_SimulationThread t2 = (as_SimulationThread) arg0;
		// System.out.println("comparing "+this+" to "+arg0);
		if (t1.eventTime < t2.eventTime)
			return -1;
		if (t1.eventTime == t2.eventTime)
			return t1.priority < t2.priority ? -1
					: (t1.priority > t2.priority ? 1 : 0);
		return +1;
	}

	public void exit() {
		as_SimulationDiscrete.stopThread();
	}

	@Override
	public String toString() {
		return getName() + " et=" + eventTime + " pr=" + priority;
	}

	public void block(PriorityBlockingQueue<as_SimulationThread> q) {
		q.add(this);
		try {
			bar.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void block() {
		try {
			bar.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
