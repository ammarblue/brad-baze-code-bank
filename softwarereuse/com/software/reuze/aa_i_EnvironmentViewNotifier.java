package com.software.reuze;
//package aima.core.agent;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface aa_i_EnvironmentViewNotifier {
	/**
	 * A simple notification message, to be forwarded to an Environment's
	 * registered EnvironmentViews.
	 * 
	 * @param msg
	 *            the message to be forwarded to the EnvironmentViews.
	 */
	void notifyViews(String msg);
}
