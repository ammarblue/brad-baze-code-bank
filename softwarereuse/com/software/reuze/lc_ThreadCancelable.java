package com.software.reuze;

/**
 * Implements a thread with an additional flag indicating cancellation.
 * 
 * @author R. Lunde
 * @author Mike Stampone
 */
public class lc_ThreadCancelable extends Thread {

	/**
	 * Returns <code>true</code> if the current thread is canceled
	 * 
	 * @return <code>true</code> if the current thread is canceled
	 */
	public static boolean currIsCanceled() {
		if (Thread.currentThread() instanceof lc_ThreadCancelable)
			return ((lc_ThreadCancelable) Thread.currentThread()).isCanceled;
		return false;
	}

	private boolean isCanceled;

	/**
	 * Returns <code>true</code> if this thread is canceled
	 * 
	 * @return <code>true</code> if this thread is canceled
	 */
	public boolean isCanceled() {
		return isCanceled;
	}

	/**
	 * Cancels this thread
	 */
	public void cancel() {
		isCanceled = true;
	}
}
