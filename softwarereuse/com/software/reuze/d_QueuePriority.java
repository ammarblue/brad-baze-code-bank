package com.software.reuze;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 80.<br>
 * <br>
 * The priority queue, which pops the element of the queue with the highest
 * priority according to some ordering function.
 * 
 * @author Ciaran O'Reilly
 */
public class d_QueuePriority<E> extends java.util.PriorityQueue<E> implements
		d_i_Queue<E> {
	private static final long serialVersionUID = 1;

	public d_QueuePriority() {
		super();
	}

	public d_QueuePriority(Collection<? extends E> c) {
		super(c);
	}

	public d_QueuePriority(int initialCapacity) {
		super(initialCapacity);
	}

	public d_QueuePriority(int initialCapacity, Comparator<? super E> comparator) {
		super(initialCapacity, comparator);
	}

	public d_QueuePriority(d_QueuePriority<? extends E> c) {
		super(c);
	}

	public d_QueuePriority(SortedSet<? extends E> c) {
		super(c);
	}

	//
	// START-Queue
	public boolean isEmpty() {
		return 0 == size();
	}

	public E pop() {
		return poll();
	}

	public d_i_Queue<E> insert(E element) {
		if (offer(element)) {
			return this;
		}
		return null;
	}
	// END-Queue
	//
}