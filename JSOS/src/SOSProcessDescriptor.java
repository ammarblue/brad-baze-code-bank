// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
//
// SOSProcessDescriptor.java
//   Java version of C++ code in page 123 of Crowley "Operating Systems"
//
//   The process descriptor is the data structure that records the
//   state of a process.  It includes the register state and the
//   process state.

import java.awt.*;

//
class SOSProcessDescriptor {
	public boolean slotAllocated; // Boolean: is the slot free or used?
	public int timeLeft; // time left from the last time slice in ms
	public int state; // ready, running or blocked
	public int base_register;
	public int limit_register;
}
