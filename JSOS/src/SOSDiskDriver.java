// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSDiskDriver.java
//   Java version of C++ code in pages 147 and 149 of Crowley "Operating Systems"
//
import java.util.*;

class SOSDiskDriver {
	// --------------------------------------------------------------------
	// Disk constants
	// --------------------------------------------------------------------
	static final int DiskBlockSize = 128;
	static final int DiskNumber = 0; // only one disk for now
	static final int NumberOfDiskPages = 32;
	static final int SizeOfDiskPage = 64;

	// --------------------------------------------------------------------
	// DiskIO
	// Places a disk IO request record on the disk request queue and then
	// wakes up the disk scheduler.
	//
	// This procedure is called from the system call interrupt handler.
	// --------------------------------------------------------------------

	void DiskIO(int command, int disk_block, int buffer_address) {
		SIM.Trace(SIM.TraceSOSDisk, "Queue Disk IO request: block="
				+ disk_block + ", buffer=" + buffer_address);

		// Create a new disk request and fill in the fields.
		int cur_proc = SIM.sosData.current_process;
		SOSDiskRequest req = new SOSDiskRequest();
		req.command = command;
		req.disk_block = disk_block;
		// change this to an absolute address
		req.buffer_address = buffer_address
				+ SIM.sosData.pd[cur_proc].base_register;
		req.pid = cur_proc;

		// Then insert it on the queue.
		SIM.sosData.disk_queue.addElement(req);
		SIM.sosData.pd[cur_proc].state = SOSProcessManager.Blocked;

		// Wake up the disk scheduler if it is idle.
		ScheduleDisk();
	}

	// --------------------------------------------------------------------
	// ScheduleDisk
	// Takes IO request from the disk queue and starts the disk.
	//
	// This procedure is often called when the disk is busy or there
	// are not requests to service. In those cases it simply returns.
	// For writes, this procedure moves the data from the user's
	// address space into the OSs address space since all disk IO
	// must have buffers in the OSs address space.
	// --------------------------------------------------------------------

	void ScheduleDisk() {

		// If the disk is already busy we cannot schedule it.
		if (DiskBusy()) {
			return;
		}

		// Simply return if there is no disk request to service.
		Vector queue = SIM.sosData.disk_queue;
		if (queue.isEmpty()) {
			return;
		}

		// Get the first disk request from the disk request queue.
		SOSDiskRequest req = (SOSDiskRequest) queue.firstElement();
		queue.removeElementAt(0);

		// remember which process is waiting for the disk operation
		SIM.sosData.pending_disk_request = req;

		// issue the read or write, with disk interrupt enabled
		if (req.command == SOSSyscallIntHandler.DiskReadSystemCall) {
			IssueDiskRead(req.disk_block, DiskBlockSize, 1);
		} else {
			// copy the block from MIPS memory into an OS buffer
			SOSSyscallIntHandler.MemoryCopy(req.buffer_address,
					SIM.SystemDiskBuffer, DiskBlockSize);
			IssueDiskWrite(req.disk_block, SIM.SystemDiskBuffer, 1);
		}
	}

	// --------------------------------------------------------------------
	// DiskBusy
	// Returns the status of the disk (busy or not).
	//
	// This procedure uses the simulation's disk busy procedure.
	// --------------------------------------------------------------------
	boolean DiskBusy() {
		int status_reg = SIM.hw.GetCellUnmappedAsInt(SIM.DiskStatusRegister);
		SIM.Trace(SIM.TraceSOSDisk,
				"DiskBusy, status reg = " + Integer.toBinaryString(status_reg));
		return status_reg != 0;
	}

	// --------------------------------------------------------------------
	// IssueDiskRead
	// Start a read operation on the disk
	//
	// This procedure uses the simulation's disk read procedure.
	// --------------------------------------------------------------------

	void IssueDiskRead(int block_number, int buffer_address,
			int enable_disk_interrupt) {
		SIM.Trace(SIM.TraceSOSDisk, "Disk Read -- block=" + block_number
				+ ", buffer_address=" + buffer_address);
		SIM.hw.SetCellUnmapped(SIM.DiskAddressRegister, buffer_address);
		SIM.hw.SetCellUnmapped(SIM.DiskControlRegister,
				(enable_disk_interrupt << 30) + (block_number << 10));
	}

	// --------------------------------------------------------------------
	// IssueDiskWrite
	// Start a write operation on the disk
	//
	// This procedure uses the simulation's disk write procedure.
	// --------------------------------------------------------------------

	void IssueDiskWrite(int block_number, int buffer_address,
			int enable_disk_interrupt) {
		SIM.Trace(SIM.TraceSOSDisk, "Disk Write -- block=" + block_number
				+ ", buffer_address=" + buffer_address);
		SIM.hw.SetCellUnmapped(SIM.DiskAddressRegister, buffer_address);
		SIM.hw.SetCellUnmapped(SIM.DiskControlRegister, (1 << 31)
				+ (enable_disk_interrupt << 30) + (block_number << 10));
	}
}
