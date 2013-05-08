// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSStart.java
//   Java version of C++ code in page 134 of Crowley "Operating Systems"
//
//   SOS initialization.  The main program for the operating system.
//
//   This is called once, when the operating system is first starting up.
//----------------------------------------------------------------------
//
import java.util.*;

class SOSStart implements Runnable {
	public void run() {
		SIM.Trace(SIM.TraceSOSStart, "Initialize SOS (Simple Operating System)");

		// sosData.memoryModel = StaticNonPaged;
		// defaut time quantum
		SIM.sosData.TimeQuantum = SOSProcessManager.TimeQuantum;

		// Initialize the interrupt vectors
		SIM.hw.SetCellUnmapped(SIM.SyscallIntVector,
				(Object) new SOSSyscallIntHandler());
		SIM.hw.SetCellUnmapped(SIM.TimerIntVector,
				(Object) new SOSTimerIntHandler());
		SIM.hw.SetCellUnmapped(SIM.DiskIntIntVector,
				(Object) new SOSDiskIntHandler());
		SIM.hw.SetCellUnmapped(SIM.ProgErrIntIntVector,
				(Object) new SOSProgErrIntHandler());

		// Initialize the subsystems
		// InitializeMemorySystem( 0, 128*64 );
		InitializeIOSystem();
		InitializeProcessSystem();

		// Create the initial process
		SIM.sosData.processManager.CreateProcessSysProc(
				SIM.sosData.initialProcessNumber, 0);

		// Start running processes
		SIM.sosData.processManager.Dispatcher();
		SIM.Trace(SIM.TraceSOSStart, "Suspending the startup thread (for good)");
		Thread.currentThread().suspend();
	}

	// --------------------------------------------------------------------
	// InitializeProcessSystem
	// Initialize the process management subsystem.
	// --------------------------------------------------------------------
	private void InitializeProcessSystem() {
		SIM.Trace(SIM.TraceSOSStart, "Initialize Process System");
		int i;

		// Create the necessary arrays and elements
		SIM.sosData.pd = new SOSProcessDescriptor[SOSProcessManager.NumberOfProcesses];
		for (i = 0; i < SOSProcessManager.NumberOfProcesses; ++i) {
			SIM.sosData.pd[i] = new SOSProcessDescriptor();
		}

		// set up the process descriptors
		// Process 0 is reserved for the system, never dispatch it.
		SIM.sosData.pd[0].slotAllocated = true;
		SIM.sosData.pd[0].state = SOSProcessManager.Blocked;

		// The other process slots start out free.
		for (i = 1; i < SOSProcessManager.NumberOfProcesses; ++i)
			SIM.sosData.pd[i].slotAllocated = false;

		// set up the pool of free message buffers
		for (i = 0; i < (SOSProcessManager.NumberOfMessageBuffers - 1); ++i)
			// link each one to the next one
			SIM.hw.SetCellUnmapped(SIM.MessageBufferArea
					+ SOSProcessManager.MessageSize * i, new Integer(i + 1));

		// The last message buffer in the free chain has a marker so
		// you can tell you are at the end of the list.
		SIM.hw.SetCellUnmapped(SIM.MessageBufferArea
				+ SOSProcessManager.MessageSize
				* (SOSProcessManager.NumberOfMessageBuffers - 1), new Integer(
				-1));
		SIM.sosData.free_message_buffer = 0;

		SIM.sosData.message_queue_allocated = new boolean[SOSProcessManager.NumberOfMessageQueues];

		SIM.sosData.message_queue = new Vector[SOSProcessManager.NumberOfMessageQueues];

		SIM.sosData.wait_queue = new Vector[SOSProcessManager.NumberOfMessageQueues];

		// All the message queues start out unallocated.
		for (i = 0; i < SOSProcessManager.NumberOfMessageQueues; ++i)
			SIM.sosData.message_queue_allocated[i] = false;

		SIM.sosData.current_process = -1;

		// This will immediate wrap around to process 1.
		SIM.sosData.next_proc = SOSProcessManager.NumberOfProcesses;

		// Create the proces manager SOS object
		SIM.sosData.processManager = new SOSProcessManager();
	}

	// --------------------------------------------------------------------
	// InitializeIOSystem
	// Initialize the IO subsystem.
	// --------------------------------------------------------------------
	private void InitializeIOSystem() {
		SIM.Trace(SIM.TraceSOSStart, "Initialize IO System");
		SIM.sosData.pending_disk_request = null;
		SIM.sosData.diskDriver = new SOSDiskDriver();
		SIM.sosData.disk_queue = new Vector();
	}
}
