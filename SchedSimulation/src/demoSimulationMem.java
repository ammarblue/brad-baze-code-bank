import java.util.logging.Logger;

public class demoSimulationMem extends as_SimulationThread {
	int PT;
	int RT;
	private static Logger log = Logger.getLogger(as_SimulationEntity.class
			.getName());// error log for this class only
	static as_SimulationEntity Procs = new as_SimulationEntity("PTable", 1);
	static da_Histogram waittime = new da_Histogram("wait time", 0, 1, 50);

	public demoSimulationMem(String name, int ptime) {
		super(name);
		PT = ptime;
		RT = PT;
	}

	@Override
	public void run() {
		super.run();
		double start = clock;
		while (RT > 0) {
			Procs.Enter(1);// go in
			//run for some time 
			int rtime=1;
			as_SimulationDiscrete.Hold(rtime);
			Procs.Leave(1);// go out
			RT-=rtime;
		}
		waittime.Tabulate(clock - start); // get stats
		exit();
	}
}
