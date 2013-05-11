package com.software.reuze;
public class pt_StopWatch {

	private long currTime;
	private long lastTime;
	private long lapTime;
	private long startTime;
	
	
	public pt_StopWatch(){
		startTime = System.nanoTime();
		reset();
	}
	
	public void reset(){
		currTime = lastTime = /*startTime =*/ System.nanoTime();
		lapTime = 0;
	}
	
	public double getRunTime(){
		return 1.0E-9 * (System.nanoTime() - startTime);
	}
	
	public double getElapsedTime(){
		currTime = System.nanoTime();
		lapTime = currTime - lastTime;
		lastTime = currTime;
		return 1.0E-9 * lapTime;
	}
}
