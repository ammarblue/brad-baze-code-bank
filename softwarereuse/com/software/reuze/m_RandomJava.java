package com.software.reuze;
//package aima.core.util;

import java.util.Random;


/**
 * Implementation of the Randomizer Interface using Java's java.util.Random
 * class.
 * 
 * @author Ravi Mohan
 * 
 */
public class m_RandomJava implements m_i_Random {
	private static Random _r = new Random();
	private Random r = null;

	public m_RandomJava() {
		this(_r);
	}

	public m_RandomJava(Random r) {
		this.r = r;
	}

	//
	// START-Randomizer
	public double nextDouble() {
		return r.nextDouble();
	}

	// END-Randomizer
	//
}
