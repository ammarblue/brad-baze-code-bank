// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSDiskIntHandler.java
//   Java version of C++ code in page 148 of Crowley "Operating Systems"
//
class SOSDiskIntHandler implements SIMIntHandler {
	// --------------------------------------------------------------------
	// DiskInterruptHandler
	// Called when a disk operation completes.
	//
	// It makes the process that was waiting for the disk IO ready again.
	// Then it calls the disk scheduler to see if another request can be
	// started and the dispatcher to start a process running.
	// --------------------------------------------------------------------

	public void HandleInterrupt(int arg) {
		int pid;

		SIM.Trace(SIM.TraceSOSDisk, "DiskIntHandler called");

		int cur_proc = SIM.sosData.current_process;
		if (cur_proc > 0) {
			SOSProcessManager.SaveProcessState(cur_proc);

			// cancel timer interval and record the amount of time
			// that was left in it
			SIM.sosData.pd[cur_proc].timeLeft = SIM.hw.SetTimer(0);
			SIM.sosData.pd[cur_proc].state = SOSProcessManager.Ready;
			SIM.Trace(SIM.TraceSOSDisk, "Disk Interrupt, current process="
					+ cur_proc + ", time left="
					+ SIM.sosData.pd[cur_proc].timeLeft);
		}

		// If this is a read, we have to transfer the disk block into
		// the simulated memory. This is really a part of the
		// simulation code rather than part of the SOS.
		SOSDiskRequest dr = SIM.sosData.pending_disk_request;
		if (dr != null) {
			if (dr.command == SOSSyscallIntHandler.DiskReadSystemCall) {
				SOSSyscallIntHandler.MemoryCopy(SIM.SystemDiskBuffer,
						dr.buffer_address, SOSDiskDriver.DiskBlockSize);
			}

			// The process waiting for the disk is now ready to run
			pid = dr.pid;
			SIM.sosData.pd[pid].state = SOSProcessManager.Ready;
			SIM.sosData.pending_disk_request = null;
		}

		// Wake up the disk driver
		SIM.sosData.diskDriver.ScheduleDisk();

		// Call the dispatcher
		SIM.sosData.processManager.Dispatcher();
	}
}
