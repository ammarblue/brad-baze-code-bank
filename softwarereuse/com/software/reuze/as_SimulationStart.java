package com.software.reuze;

import reuze.pending.demoSimulationCar;

public class as_SimulationStart extends as_SimulationThread {
	public as_SimulationStart(String name) {
		super(name);
	}
	public as_SimulationStart() {
		super("STARTER");
	}
	@Override
	public void run() { //put start code here for your simulation
		for (int i=0; i<15; i++) new demoSimulationCar("car"+i).start();
		exit();  //must terminate every simulation thread
	}
}
