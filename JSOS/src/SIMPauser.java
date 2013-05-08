// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SIMPauser.java
//
//   This class allows SOS to be paused.
//   checkIfPaused is called in many places within SOS and will stop
//   the calling thread if the system is paused.
//
class SIMPauser {
	private boolean systemIsPaused = false;

	public synchronized void pause() {
		systemIsPaused = true;
	}

	public synchronized void unpause() {
		systemIsPaused = false;
		notifyAll();
	}

	public synchronized boolean isPaused() {
		return systemIsPaused;
	}

	public synchronized void checkIfPaused() {
		while (systemIsPaused) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}
}
