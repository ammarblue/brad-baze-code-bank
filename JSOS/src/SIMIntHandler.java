// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SIMIntHandler.java
//   Interface implemented by all interrupt handlers
//
interface SIMIntHandler {
	public void HandleInterrupt(int arg);
}
