package reuze.pending;

import com.software.reuze.as_SimulationDiscrete;
import com.software.reuze.as_SimulationEntity;
import com.software.reuze.as_SimulationThread;
import com.software.reuze.da_Histogram;
import com.software.reuze.ms_StatisticsDistributions;

public class demoSimulationCar extends as_SimulationThread {
	static as_SimulationEntity carpark=new as_SimulationEntity("carpark",10);
	static da_Histogram waittime=new da_Histogram("wait time",0,10,20);
	public demoSimulationCar(String name) {
		super(name);
	}
	static int x=100;
	@Override
	public void run() {
		super.run();
		double start=clock;
		carpark.Enter(1);
		as_SimulationDiscrete.Hold(50);
		carpark.Leave(1);
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
			for (int i=0; i<16; i++) {
				demoSimulationCar cars=new demoSimulationCar("Car "+i);
				cars.start();
			}
			exit();
		}
	}
	public static void main(String args[]) {
		DeusExMachina dem=new DeusExMachina("Car Park");
		dem.start();
	}
}
