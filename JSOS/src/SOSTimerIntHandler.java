// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSTimerIntHandler.java
//   Java version of C++ code in pages 132-133 of Crowley "Operating Systems"
//
class SOSTimerIntHandler implements SIMIntHandler {
	public void HandleInterrupt(int arg) { // arg ignored in timer interrupts
		int cur_proc = SIM.sosData.current_process;
		SIM.Trace(SIM.TraceSOSPM, "Timer Interupt -- current process="
				+ cur_proc);
		SOSProcessManager.SaveProcessState(cur_proc);

		SIM.Trace(SIM.TraceSOSPM, "Suspending pid " + cur_proc + " : "
				+ SIM.hw.ProcessThread[cur_proc].toString());
		// Suspend the current process (part of the SIMulation)
		SIM.hw.ProcessThread[cur_proc].suspend();
		SIM.sosData.pd[cur_proc].timeLeft = 0;
		SIM.sosData.pd[cur_proc].state = SOSProcessManager.Ready;
		// Pick another process to run (it could be the same one)
		SIM.sosData.processManager.Dispatcher();
	}
}
