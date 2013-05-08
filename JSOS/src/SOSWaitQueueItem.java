// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSWaitQueueItem.java
//   Java version of C++ code in page 124 of Crowley "Operating Systems"
//
//   This structure is what we put in the wait_queue.
//
class SOSWaitQueueItem {
	public int pid;
	// This is the address of the buffer (in process pid) to
	// read the message from or write the message to.
	// Thus this is not a system mode (unmapped) address
	public int buffer_address;
}
