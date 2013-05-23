
import java.util.logging.Logger;

public class demoSimulationMem extends as_SimulationThread {
	int PT;
	private static Logger log = Logger.getLogger(as_SimulationEntity.class
			.getName());// error log for this class only
	static as_SimulationEntity memory = new as_SimulationEntity("PTable", 40000);
	static da_Histogram waittime = new da_Histogram("wait time", 0, 10, 20);

	public demoSimulationMem(String name, int ptime) {
		super(name);
		PT = ptime;
	}

	// static int x=100;
	@Override
	public void run() {
		super.run();
		double start = clock;
		memory.Enter(PT);
		as_SimulationDiscrete.Hold(2);// time to run
		memory.Leave(PT);
		waittime.Tabulate(clock - start); // time to enter, do business, and
											// exit
		exit();
	}

	public static class DeusExMachina extends as_SimulationThread {
		ms_StatisticsDistributions sd = new ms_StatisticsDistributions();

		public DeusExMachina(String name) {
			super(name);
		}

		@Override
		public void run() {
			super.run();
			for (int i = 0; i < 10; i++) {
				int ptime = (int) (Math.random() * 1024 + 16);// set size of
																// proc time
				demoSimulationMem RW = new demoSimulationMem("proc " + i, ptime);
				RW.start();
			}
			exit();
		}
	}

	public static void main(String args[]) {
		DeusExMachina dem = new DeusExMachina("Process Table");
		dem.start();
	}
}
