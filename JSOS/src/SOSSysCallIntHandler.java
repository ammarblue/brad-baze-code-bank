// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSSyscallIntHandler.java
//   Java version of C++ code in pages 141-144 of Crowley "Operating Systems"
//
import java.util.*;

class SOSSyscallIntHandler implements SIMIntHandler {
	// ------------------------------------------------------------------
	// System call numbers
	// Arbitrary numbers, as long as they are all different.
	// ------------------------------------------------------------------
	static final int CreateProcessSystemCall = 1;
	static final int ExitProcessSystemCall = 2;
	static final int CreateMessageQueueSystemCall = 3;
	static final int SendMessageSystemCall = 4;
	static final int ReceiveMessageSystemCall = 5;
	static final int DiskReadSystemCall = 6;
	static final int DiskWriteSystemCall = 7;

	// ------------------------------------------------------------------------
	// SystemCallInterruptHandler
	// Called when a process makes a system call.
	//
	// Each system call is handled in an arm of the switch statement.
	// A procedure is calls if the length of the case is too long.
	// ------------------------------------------------------------------------
	public void HandleInterrupt(int addr) {

		int cur_proc = SIM.sosData.current_process;
		// Do dynamic process data to save in this simulation
		SOSProcessManager.SaveProcessState(cur_proc);

		// cancel timer interval and record the amount of time that was left
		SIM.sosData.pd[cur_proc].timeLeft = SIM.hw.SetTimer(0);
		SIM.sosData.pd[cur_proc].state = SOSProcessManager.Ready;

		// fetch the system call number and switch on it
		int base_reg = SIM.sosData.pd[cur_proc].base_register;
		int system_call_number = SIM.hw.GetCellUnmappedAsInt(addr + 1
				+ base_reg);

		// Declare the local variables.
		int block_number, to_q, from_q, disk_block, return_code;
		int buffer;
		int user_msg;
		int msg_no;

		// Switch on the system call number
		switch (system_call_number) {

		case CreateProcessSystemCall:
			// get the system call arguments from the registers
			int app_number = SIM.hw.GetCellUnmappedAsInt(addr + 2 + base_reg);
			SIM.Trace(SIM.TraceSOSSyscall, "Create system call: pid="
					+ cur_proc + ", application number=" + app_number);

			// return the return code
			int arg = SIM.hw.GetCellUnmappedAsInt(addr + 3 + base_reg);
			int ret = SIM.sosData.processManager.CreateProcessSysProc(
					app_number, arg);
			SIM.hw.SetCellUnmapped(addr + base_reg, ret);
			break;

		case ExitProcessSystemCall:
			return_code = SIM.hw.GetCellUnmappedAsInt(addr + base_reg);
			// we don't save the return code in this OS so
			// just free up the pd slot
			SIM.Trace(SIM.TraceSOSSyscall, "Exit system call -- pid="
					+ cur_proc + ", return code=" + return_code);
			SIM.sosData.pd[cur_proc].slotAllocated = false;
			SIM.sosData.pd[cur_proc].state = SOSProcessManager.Blocked;
			// FreeMemory( SIM.sosData.current_process );
			break;

		case CreateMessageQueueSystemCall:
			// find a free message queue
			int i;
			for (i = 0; i < SOSProcessManager.NumberOfMessageQueues; ++i) {
				if (!SIM.sosData.message_queue_allocated[i]) {
					break;
				}
			}
			if (i >= SOSProcessManager.NumberOfMessageQueues) {
				// signal the error, message queue overflow
				// return a value that is invalid
				SIM.hw.SetCellUnmapped(addr + base_reg, -1);
				break;
			}
			SIM.Trace(SIM.TraceSOSSyscall,
					"Create message queue system call: pid=" + cur_proc
							+ ", mqid=" + i);
			// Create the two Queues and return the index to the
			// message queue.
			SIM.sosData.message_queue_allocated[i] = true;
			SIM.sosData.message_queue[i] = new Vector();
			SIM.sosData.wait_queue[i] = new Vector();
			SIM.hw.SetCellUnmapped(addr + base_reg, i);
			break;

		case SendMessageSystemCall:
			// get the arguments
			user_msg = SIM.hw.GetCellUnmappedAsInt(addr + 2 + base_reg);
			// Convert the user's buffer address to an absolute address
			user_msg += base_reg;
			to_q = SIM.hw.GetCellUnmappedAsInt(addr + 3 + base_reg);
			SIM.Trace(SIM.TraceSOSSyscall, "Send message system call: pid="
					+ cur_proc + ", mqid=" + to_q + ", user message address="
					+ user_msg);

			// check for an invalid queue identifier
			if (!SIM.sosData.message_queue_allocated[to_q]) {
				SIM.hw.SetCellUnmapped(addr + base_reg, -1);
				break;
			}
			msg_no = GetMessageBuffer();
			// make sure we have not run out of message buffers
			if (msg_no == -1) {
				SIM.hw.SetCellUnmapped(addr + base_reg, -2);
				break;
			}
			// copy the message vector from the system caller's memory
			// into the system's message buffer
			MemoryCopy(user_msg, SIM.MessageBufferArea
					+ SOSProcessManager.MessageSize * msg_no,
					SOSProcessManager.MessageSize);
			Vector wq = SIM.sosData.wait_queue[to_q];
			if (!wq.isEmpty()) {
				// some process is waiting for a message, deliver it immediately
				Object oitem = wq.firstElement();
				wq.removeElementAt(0);
				SOSWaitQueueItem wqitem = (SOSWaitQueueItem) oitem;
				TransferMessage(msg_no, wqitem.buffer_address);
				SIM.sosData.pd[wqitem.pid].state = SOSProcessManager.Ready;
			} else {
				// otherwise put it on the queue
				SIM.sosData.message_queue[to_q].addElement(new Integer(msg_no));
			}
			SIM.hw.SetCellUnmapped(addr + base_reg, 0);
			break;

		case ReceiveMessageSystemCall:
			user_msg = SIM.hw.GetCellUnmappedAsInt(addr + 2 + base_reg);
			// Convert the user's buffer address to a phsyical address
			user_msg += base_reg;
			from_q = SIM.hw.GetCellUnmappedAsInt(addr + 3 + base_reg);
			SIM.Trace(SIM.TraceSOSSyscall, "Receive message system call, pid="
					+ cur_proc + ", mqid=" + from_q + ", user msg addr="
					+ user_msg);

			// check for an invalid queue identifier
			if (!SIM.sosData.message_queue_allocated[from_q]) {
				SIM.hw.SetCellUnmapped(addr + base_reg, -1);
				break;
			}
			if (SIM.sosData.message_queue[from_q].isEmpty()) {
				SIM.sosData.pd[SIM.sosData.current_process].state = SOSProcessManager.Blocked;
				SOSWaitQueueItem item = new SOSWaitQueueItem();
				item.pid = SIM.sosData.current_process;
				item.buffer_address = user_msg;
				SIM.sosData.wait_queue[from_q].addElement(item);
			} else {
				msg_no = ((Integer) SIM.sosData.message_queue[from_q]
						.firstElement()).intValue();
				SIM.sosData.message_queue[from_q].removeElementAt(0);
				TransferMessage(msg_no, user_msg);
			}
			SIM.hw.SetCellUnmapped(addr + base_reg, 0);
			break;

		case DiskReadSystemCall:
		case DiskWriteSystemCall:
			buffer = SIM.hw.GetCellUnmappedAsInt(addr + 2 + base_reg);
			// convert to physical address
			buffer += base_reg;
			disk_block = SIM.hw.GetCellUnmappedAsInt(addr + 3 + base_reg);
			SIM.Trace(SIM.TraceSOSSyscall, "Disk IO system call: pid="
					+ cur_proc + ", block=" + disk_block
					+ ", user buffer address=" + buffer);

			SIM.sosData.diskDriver.DiskIO(system_call_number, disk_block,
					buffer);
			SIM.hw.SetCellUnmapped(addr + base_reg, 0);
			break;
		}
		SIM.sosData.processManager.Dispatcher();
	}

	// --------------------------------------------------------------------------
	// TransferMessage
	// Transfer message to the receiver process and free the message buffer
	//
	// A convenience procedure to do the two parts of receiving a message.
	// Both SendMessage and ReceiveMessage might call this procedure.
	// --------------------------------------------------------------------------
	private void TransferMessage(int msg_no, int user_msg) {
		MemoryCopy(SIM.MessageBufferArea + SOSProcessManager.MessageSize
				* msg_no, user_msg, SOSProcessManager.MessageSize);
		FreeMessageBuffer(msg_no);
	}

	// --------------------------------------------------------------------
	// GetMessageBuffer
	// Java version of code on page 125 in Crowley "Operating Systems"
	// Procedure for allocating message buffers.
	//
	// A convenience procedure.
	// --------------------------------------------------------------------

	private int GetMessageBuffer() {
		// get the head of the free list
		int msg_no = SIM.sosData.free_message_buffer;
		if (msg_no != -1) {
			// follow the link to the next buffer
			SIM.sosData.free_message_buffer = SIM.hw
					.GetCellUnmappedAsInt(SIM.MessageBufferArea
							+ SOSProcessManager.MessageSize * msg_no);
		}
		return msg_no;
	}

	// --------------------------------------------------------------------
	// FreeMessageBuffer
	// Java version of code on page 125 in Crowley "Operating Systems"
	// Procedures for freeing message buffers.
	//
	// A convenience procedure.
	// --------------------------------------------------------------------

	private void FreeMessageBuffer(int msg_no) {
		SIM.hw.SetCellUnmapped(SIM.MessageBufferArea
				+ SOSProcessManager.MessageSize * msg_no, new Integer(
				SIM.sosData.free_message_buffer));
		SIM.sosData.free_message_buffer = msg_no;
	}

	// --------------------------------------------------------------------------
	// MemoryCopy
	// Transfers data in the memory array.
	// Java version of code on page 145 in Crowley "Operating Systems"
	//
	// This procedure must interface with the Java threads simulation.
	// It actually just copies between places in the memory array.
	// --------------------------------------------------------------------------

	static public void MemoryCopy(int from, int to, int len) {
		SIM.Trace(SIM.TraceSOSSyscall, "MemoryCopy from=" + from + ", to=" + to
				+ ", len=" + len);
		for (int i = 0; i < len; ++i) {
			SIM.hw.SetCellUnmapped(to + i, SIM.hw.GetCellUnmapped(from + i));
		}
	}
}
