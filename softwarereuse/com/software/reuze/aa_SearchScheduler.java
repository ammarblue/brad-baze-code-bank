package com.software.reuze;

/**
 * @author Ravi Mohan
 * 
 */
public class aa_SearchScheduler {

	private final int k, limit;

	private final double lam;

	public aa_SearchScheduler(int k, double lam, int limit) {
		this.k = k;
		this.lam = lam;
		this.limit = limit;
	}

	public aa_SearchScheduler() {
		this.k = 20;
		this.lam = 0.045;
		this.limit = 100;
	}

	public double getTemp(int t) {
		if (t < limit) {
			double res = k * Math.exp((-1) * lam * t);
			return res;
		} else {
			return 0.0;
		}
	}
}
