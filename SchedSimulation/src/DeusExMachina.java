public class DeusExMachina extends as_SimulationThread {
	ms_StatisticsDistributions sd = new ms_StatisticsDistributions();

	public DeusExMachina(String name) {
		super(name);
	}

	@Override
	public void run() {
		super.run();
		for (int i = 0; i < 20; i++) {
			// set execution time needed
			int ptime = (int) (Math.random() * 3) + 1;
			// create proc
			demoSimulationMem RW = new demoSimulationMem("proc " + i, ptime);
			// place into system
			RW.start();
		}
		exit();
	}
}