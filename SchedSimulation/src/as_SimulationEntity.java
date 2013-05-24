import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class as_SimulationEntity {

	static ArrayList<as_SimulationEntity> list = new ArrayList<as_SimulationEntity>();
	final String name;
	final int maxCapacity;
	int amtUsed;
	int maxUsed;
	int numEnters;
	double sumDelayTime, sumDelaySquared;
	PriorityBlockingQueue<as_SimulationThread.Request> q = new PriorityBlockingQueue<as_SimulationThread.Request>();

	/* Show all stores */
	public static void Print() {
		if (list.isEmpty())
			return;
		System.out
				.println("Entity\t\tCapacity\tnEnters\tmaxUsed\tMeanWait\tStdDevWait");
		for (as_SimulationEntity s : list) {
			double m = 0;
			double d = 0;
			if (s.numEnters != 0) {
				m = s.sumDelayTime / s.numEnters;
				if (s.numEnters != 1) {
					d = s.sumDelayTime * s.sumDelayTime / s.numEnters;
					d = s.sumDelaySquared - d;
					d = d / (s.numEnters - 1.0);
					d = Math.sqrt(d);
				}
			}
			System.out.printf("%-15s\n",s.name);
			System.out.printf("%s\t%.3f\t\t%.3f\n", s.name + "\t\t  "
					+ s.maxCapacity + "\t\t " + s.numEnters + "\t  "
					+ s.maxUsed, m, d);
		}
	}

	/* Initialize a store, with name = sname and maximum capacity = num */
	public as_SimulationEntity(String sname, int capacity) {
		name = sname;
		maxCapacity = capacity;
		list.add(this);
	}

	/*
	 * Enter num into store, with priority to establish ordering of requests
	 * when store's capacity is exceeded.
	 */
	public void Enter(int num, int priority) {
		if (num <= 0 || num > maxCapacity)
			throw new RuntimeException("illegal Enter request with num of:"
					+ num);
		int needed = amtUsed + num;
		as_SimulationThread t = (as_SimulationThread) Thread.currentThread();
		t.request.entryTime = as_SimulationThread.clock;
		if (needed > maxCapacity) {
			t.request.num = num;
			t.request.priority = priority;
			q.add(t.request);
			as_SimulationDiscrete.bThreads.incrementAndGet();
			t.block();
			as_SimulationDiscrete.bThreads.decrementAndGet();
			double x = as_SimulationThread.clock - t.request.entryTime;
			sumDelayTime += x;
			sumDelaySquared += x * x;
			as_SimulationDiscrete.Hold(0);
		} else {
			amtUsed = needed;
			if (amtUsed > maxUsed)
				maxUsed = amtUsed;
			numEnters++;
			System.out.println(t.getName() + " enters " + num + " in store "
					+ name + " leaving " + (maxCapacity - amtUsed));
		}
	}

	public void Enter(int num) {
		Enter(num, ((as_SimulationThread) Thread.currentThread()).priority);
	} // defaults to thread's priority

	/* Remove num from store */
	public void Leave(int num) {
		if (num <= 0 || amtUsed < num)
			throw new RuntimeException("illegal Leave request");
		amtUsed -= num;
		as_SimulationThread t = (as_SimulationThread) Thread.currentThread();
		System.out.println(t.getName() + " returns " + num + " to store "
				+ name + " leaving " + (maxCapacity - amtUsed));
		for (;;) {
			as_SimulationThread.Request r = q.peek();
			if (r == null)
				return;
			int needed = amtUsed + r.num;
			if (needed > maxCapacity)
				return;
			try {
				r = q.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			amtUsed = needed;
			if (amtUsed > maxUsed)
				maxUsed = amtUsed;
			numEnters++;
			System.out.println(r.owner.getName() + " enters " + r.num
					+ " in store " + name + " leaving "
					+ (maxCapacity - amtUsed));
			try {
				r.owner.bar.await(); // actually the 2nd wait on a barrier wakes
										// the thread
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	/* Show quantity held in store currently */
	public int Used() {
		return amtUsed;
	}

	public int Available() {
		return maxCapacity - amtUsed;
	}

	public boolean isEmpty() {
		return amtUsed == 0;
	}

	public boolean isFull() {
		return amtUsed == maxCapacity;
	}

}
