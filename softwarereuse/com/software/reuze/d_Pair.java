package com.software.reuze;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
public class d_Pair<X, Y> {
	public X a;

	public Y b;

	/**
	 * Constructs a Pair from two given elements
	 * 
	 * @param a
	 *            the first element
	 * @param b
	 *            the second element
	 */
	public d_Pair(X a, Y b) {
		this.a = a;
		this.b = b;
	}
	public d_Pair() {}
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
		if (o instanceof d_Pair<?, ?>) {
			d_Pair<?, ?> p = (d_Pair<?, ?>) o;
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
