
import java.util.logging.Logger;

public class demoSimulationMem extends as_SimulationThread {
	int PT;
	private static Logger log = Logger.getLogger(as_SimulationEntity.class
			.getName());// error log for this class only
	static as_SimulationEntity Procs = new as_SimulationEntity("PTable", 1000);
	static da_Histogram waittime = new da_Histogram("wait time", 0, 4, 45);

	public demoSimulationMem(String name, int ptime) {
		super(name);
		PT = ptime;
	}

	@Override
	public void run() {
		super.run();
		double start = clock;
		Procs.Enter(PT);
		as_SimulationDiscrete.Hold((int)(Math.random()*30));// time to run
		Procs.Leave(PT);
		waittime.Tabulate(clock - start); // time to enter, do business, and
		exit();
	}
}
