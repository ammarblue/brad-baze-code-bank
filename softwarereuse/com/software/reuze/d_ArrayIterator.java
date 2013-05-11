package com.software.reuze;

import java.util.Iterator;

/**
 * Iterates efficiently through an array.
 * 
 * @author Ruediger Lunde
 */
public class d_ArrayIterator<T> implements Iterator<T> {

	T[] values;
	int counter;

	public d_ArrayIterator(T[] values) {
		this.values = values;
		counter = 0;
	}

	public boolean hasNext() {
		return counter < values.length;
	}

	public T next() {
		return values[counter++];
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
