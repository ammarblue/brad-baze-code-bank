// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSProgErrIntHandler.java
//   Java version of C++ code in page 146 of Crowley "Operating Systems"
//
//   Called when a program expcetion occurs.
//
//    The default action is to forcibly exit the process.
//--------------------------------------------------------------------------

class SOSProgErrIntHandler implements SIMIntHandler {
	public void HandleInterrupt(int error_code) {
		int cur_proc = SIM.sosData.current_process;

		SIM.Trace(SIM.TraceSOSPM, "Program Error, pid=" + cur_proc
				+ ", error code is " + error_code);
		SIM.hw.SetTimer(0); // cancel the timer interval
		SIM.sosData.pd[cur_proc].slotAllocated = false;
		// FreeMemory( cur_proc );
		SIM.sosData.processManager.Dispatcher();
	}
}
