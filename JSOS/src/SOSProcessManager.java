// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSProcessManager.java
//
import java.util.*;

class SOSProcessManager {

	// --------------------------------------------------------------------
	// Defined constants
	// --------------------------------------------------------------------
	static final int MessageSize = 8; // 8 words = 32 bytes
	static final int NumberOfProcesses = 8;
	static final int NumberOfMessageQueues = 4;
	// This is the number of message buffers for ALL the message queues.
	static final int NumberOfMessageBuffers = 50;
	static final int TimeQuantum = 5000;

	// --------------------------------------------------------------------
	// ProcessStates
	// A process is always in a state. These are the possible states.
	// --------------------------------------------------------------------
	static final int Ready = 1;
	static final int Running = 2;
	static final int Blocked = 3;

	// --------------------------------------------------------------------
	// SaveProcessState
	// Called on each interupt and exception to have the state of the
	// process that was interrupted.
	//
	// This procedure must interface with the simulated machine.
	// --------------------------------------------------------------------

	static public void SaveProcessState(int pid) {
		if (pid > 0) { // was there a running process?
			SIM.Trace(SIM.TraceSOSPM, "Save State -- pid=" + pid);
		}
	}

	// --------------------------------------------------------------------------
	// CreateProcessSysProc
	// Java version of C++ code in page 126-127 of Crowley "Operating Systems"
	// Process Creation
	//
	// This is the internal OS procedure for creating a process.
	// It will be called when a CreateProcess system call is made.
	// It is also called once during system initialization to
	// create the initial (first) process.
	// --------------------------------------------------------------------------

	int CreateProcessSysProc(int app_number, int arg) {

		// The code to run in the process is in a sequential file
		// on disk (meaning that it is in consecutive blocks on the disk)
		// first_block is the disk block address of the first block of
		// the code file. The first word of the code file contains the
		// length of the code file.
		int pid;

		// Look for a free slot in the process table.
		for (pid = 1; pid < NumberOfProcesses; ++pid) {
			if (!(SIM.sosData.pd[pid].slotAllocated))
				break;
		}
		if (pid >= NumberOfProcesses) {
			// We did not find a free slot, the create process fails.
			SIM.Trace(SIM.TraceAlways, "Create process failed -- no slots");
			return -1; // ERROR
		}

		SIM.sosData.pd[pid].slotAllocated = true; // Mark the slot as allocated.

		// Set up the memory mapping for the process
		SIM.sosData.pd[pid].base_register = pid * 1000;
		SIM.sosData.pd[pid].limit_register = 1000;

		SIM.hw.CreateProcess(app_number, arg, pid);
		// processes start out ready
		SIM.sosData.pd[pid].state = Ready;

		return pid;
	}

	// --------------------------------------------------------------------------
	// Dispatcher
	// Java version of C++ code in page 129-130 of Crowley "Operating Systems"
	// Starts a user process running.
	//
	// Find a process to run and run it.
	// --------------------------------------------------------------------------

	void Dispatcher() {

		// Pick a process to run.
		SIM.sosData.current_process = SelectProcessToRun();

		// and run it.
		RunProcess(SIM.sosData.current_process);
	}

	// --------------------------------------------------------------------------
	// SelectProcessToRun
	// Find the process to run next.
	//
	// If the current process was interrupted and is still ready, then
	// it gets to finish its time slice. Otherwise we go through the
	// processes and find the next one that is ready. We start looking
	// just after the last process we ran so that all processes get an
	// equal chane to run.
	// --------------------------------------------------------------------------

	int SelectProcessToRun() {
		// If the process was interrupted by a disk interrupt or made a
		// system call that did not block, then we let it use the rest
		// of the time slice that was alloted to it.

		int cur_proc = SIM.sosData.current_process;
		if (cur_proc > 0 && SIM.sosData.pd[cur_proc].slotAllocated
				&& SIM.sosData.pd[cur_proc].state == Ready
				&& SIM.sosData.pd[cur_proc].timeLeft > 0) {
			SIM.Trace(SIM.TraceSOSPM, "Run same process again, pid=" + cur_proc
					+ ":" + SIM.hw.ProcessThread[cur_proc].toString() + " for "
					+ SIM.sosData.pd[cur_proc].timeLeft + " ms");
			return cur_proc;
		}

		// We start from the current value of next_proc and wrap around when
		// we get to the end.
		// i counts the number of iterations so we can easily tell when
		// we have looked through the entire array.

		for (int i = 1; i < NumberOfProcesses; ++i) {
			// If we have gotten to the end of the process table then wrap
			// around to the beginning.
			if (++SIM.sosData.next_proc >= NumberOfProcesses)
				// Slot 0 and pid 0 are reserved for the OS,
				// so start with slot 1
				SIM.sosData.next_proc = 1;

			// next_proc is the process we are looking at.
			int next_proc = SIM.sosData.next_proc;
			if (SIM.sosData.pd[next_proc].slotAllocated
					&& SIM.sosData.pd[next_proc].state == Ready) {
				// If the process is ready then pick it to run.
				SIM.sosData.pd[next_proc].timeLeft = SIM.sosData.TimeQuantum;
				SIM.sosData.pd[next_proc].state = Running;
				SIM.Trace(SIM.TraceSOSPM, "Run new process, pid=" + next_proc
						+ ":" + SIM.hw.ProcessThread[next_proc].toString()
						+ " for " + SIM.sosData.TimeQuantum + " ms");
				return next_proc;
			}
		}
		// If we drop through the loop then no process is ready to run.
		return -1;
	}

	// --------------------------------------------------------------------------
	// RunProcess
	// Starts a user process running.
	//
	// This procedure must interface with the Java threads simulation.
	// --------------------------------------------------------------------------

	void RunProcess(int pid) {
		// Was a process picked to run?
		if (pid >= 0) {
			// Dispatch the process
			SIM.hw.SetTimer(SIM.sosData.pd[pid].timeLeft);
			SIM.Trace(SIM.TraceSOSPM, "Dispatching pid " + pid + " :"
					+ SIM.hw.ProcessThread[pid].toString());
			// Set up the memory mapping registers for the process
			SIM.hw.MemoryBaseRegister = SIM.sosData.pd[pid].base_register;
			SIM.hw.MemoryLimitRegister = SIM.sosData.pd[pid].limit_register;
			SIM.hw.RunProcess(pid);
		} else {
			// If there is no process to run then they must all be waiting
			// for an interrupt. Idle the processor and wait for interrupts.
			SIM.Trace(SIM.TraceSOSPM, "No ready processes, idle");
			SIM.hw.WaitForInterrupt();
		}
	}
}
