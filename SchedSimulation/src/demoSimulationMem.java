import java.util.logging.Logger;

public class demoSimulationMem extends as_SimulationThread {
	int priority;
	static int quantum=1;//time quantum
	private static Logger log = Logger.getLogger(as_SimulationEntity.class
			.getName());// error log for this class only
	static as_SimulationEntity Procs = new as_SimulationEntity("PTable", 1);
	static da_Histogram waittime = new da_Histogram("wait time", 0, 1, 50);
	//name, proc time, priority
	public demoSimulationMem(String name, int ptime,int p) {
		super(name);
		priority = p;
		RT = ptime;
	}

	@Override
	public void run() {
		super.run();
		double start = clock;
		Procs.Enter(1,priority);// go in
		// run for some time
		as_SimulationDiscrete.Hold(quantum);
		Procs.Leave(1);// go out
		RT -= quantum;
		while (RT > 0) {//this is a simple round robin
			Procs.Enter(1);// go in
			// run for some time
			as_SimulationDiscrete.Hold(quantum);
			Procs.Leave(1);// go out
			RT -= quantum;
		}
		waittime.Tabulate(clock - start); // get stats
		exit();
	}
}
