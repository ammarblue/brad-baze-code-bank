// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SOSDiskRequest.java
//   Java version of C++ code in page 124 of Crowley "Operating Systems"
//
//   The structure type for a disk request.
//
class SOSDiskRequest {
	public int command; // read block or write block
	public int disk_block; // block number on disk
	public int buffer_address; // address in the OSs address space
	public int pid; // process making the request
}
