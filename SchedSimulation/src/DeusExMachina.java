
public class DeusExMachina extends as_SimulationThread {
	ms_StatisticsDistributions sd = new ms_StatisticsDistributions();

	public DeusExMachina(String name) {
		super(name);
	}

	@Override
	public void run() {
		super.run();
		for (int i = 0; i < 100; i++) {
			int ptime = (int)(Math.random()*200)+1;// set size of
															// proc time
			demoSimulationMem RW = new demoSimulationMem("proc " + i, ptime);
			RW.start();
		}
		exit();
	}
}