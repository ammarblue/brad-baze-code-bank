// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSData.java
//   Java version of C++ code in page 123-124 of Crowley "Operating Systems"
//
import java.util.*;

public class SOSData {
	// The class containing all SOS global data.
	//
	// ***** Process data *****
	//
	// The current process is the one that is currently running.
	// It is possible for this to be 0 indicating that no process is running.
	public int current_process;

	// The next process to look at when dispatching
	public int next_proc;

	// The process array
	public SOSProcessDescriptor[] pd;

	// The time to execute each process
	public int TimeQuantum;

	// the initial process
	public int initialProcessNumber = 2;

	//
	// ***** Messages data *****
	//

	// A pool of message buffers
	// The message buffers are in the process's memory allocation
	// cells 500 to 899 (50 eight word buffers)
	public int free_message_buffer; // points (into array) to head of free list

	// The message queues
	// Keep track of which queues are allocated
	public boolean[] message_queue_allocated;

	// Each logical message queue is implemented with two
	// Java Vectors used as queues, one (message_queue) to hold the
	// messages and another (wait_queue) to hold WaitQueueItems which
	// record processes that are waiting for messages on the queue.
	public Vector[] message_queue; //TODO change to queue

	public Vector[] wait_queue;//TODO change to queue

	//
	// ***** Memory management data *****
	//

	// public MemoryModel memoryModel;
	// public Block BlockList; // The block list

	//
	// ***** Disk driver data *****
	//
	// Disk queue -- of disk requests waiting to be serviced.
	public SOSDiskRequest pending_disk_request = new SOSDiskRequest();

	// The items on this Queue are pointers to DiskRequests
	public Vector disk_queue;//TODO change to queue

	//
	// ***** Handles to SOS objects *****
	//
	public SOSDiskDriver diskDriver;
	public SOSProcessManager processManager;
}
