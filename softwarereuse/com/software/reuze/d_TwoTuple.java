package com.software.reuze;
//package aima.core.util.datastructure;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
public class d_TwoTuple<X, Y> {
	private final X a;

	private final Y b;

	/**
	 * Constructs a Pair from two given elements
	 * 
	 * @param a
	 *            the first element
	 * @param b
	 *            the second element
	 */
	public d_TwoTuple(X a, Y b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Returns the first element of the pair
	 * 
	 * @return the first element of the pair
	 */
	public X getFirst() {
		return a;
	}

	/**
	 * Returns the second element of the pair
	 * 
	 * @return the second element of the pair
	 */
	public Y getSecond() {
		return b;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof d_TwoTuple<?, ?>) {
			d_TwoTuple<?, ?> p = (d_TwoTuple<?, ?>) o;
			return a.equals(p.a) && b.equals(p.b);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return a.hashCode() + 31 * b.hashCode();
	}

	@Override
	public String toString() {
		return "< " + getFirst().toString() + " , " + getSecond().toString()
				+ " > ";
	}
}
