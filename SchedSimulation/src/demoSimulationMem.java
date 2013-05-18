

import java.util.logging.Logger;



public class demoSimulationMem extends as_SimulationThread {
	int memSize;
	private static Logger log=Logger.getLogger(as_SimulationEntity.class.getName());//error log for this class only
	static as_SimulationEntity memory=new as_SimulationEntity("Memory",40000);
	static da_Histogram waittime=new da_Histogram("wait time",0,10,20);
	public demoSimulationMem(String name,int mem) {
		super(name);
		memSize=mem;
	}
	//static int x=100;
	@Override
	public void run() {
		super.run();
		double start=clock;
		memory.Enter(memSize);
		as_SimulationDiscrete.Hold((Math.random()*20+1));
		memory.Leave(memSize);
		waittime.Tabulate(clock-start);  //time to enter, do business, and exit
		exit();
	}

	public static class DeusExMachina extends as_SimulationThread {
		ms_StatisticsDistributions sd=new ms_StatisticsDistributions();
		public DeusExMachina(String name) {
			super(name);
		}
		@Override
		public void run() {
			super.run();
			for (int i=0; i<1000; i++) {
				int mem=(int)(Math.random()*1024+16);//set size of memRequest
				demoSimulationMem RW=new demoSimulationMem("page "+i,mem);
				RW.start();
			}
			exit();
		}
	}
	public static void main(String args[]) {
		DeusExMachina dem=new DeusExMachina("Memory Block");
		dem.start();
	}
}
