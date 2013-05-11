package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac MinPQ.java
 *  Execution:    java MinPQ
 *  
 *  Generic min priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order.
 *
 *  % java MinPQ
 *  a
 *  is
 *  test
 *  this
 *
 *  We use a one-based array to simplify parent and child calculations.
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

/**
 *  The <tt>MinPQ</tt> class represents a priority queue of generic keys.
 *  It supports the usual <em>insert</em> and <em>delete-the-minimum</em>
 *  operations, along with methods for peeking at the maximum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  The <em>insert</em> and <em>delete-the-minimum</em> operations are
 *  logarithmic amortized time.
 *  <p>
 *  This implementation uses a binary heap.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/34pq">Section 3.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class d_QueuePriorityGeneric<Key> implements Iterable<Key> {
    private Key[] pq;                    // store element at indices 1 to N
    private int N;                       // number of elements on priority queue
    private Comparator<Key> comparator;  // optimal comparator

   /**
     * Create an empty priority queue with the given initial capacity.
     */
    public d_QueuePriorityGeneric(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

   /**
     * Create an empty priority queue.
     */
    public d_QueuePriorityGeneric() { this(1); }

   /**
     * Create an empty priority queue with the given initial capacity,
     * using the given comparator.
     */
    public d_QueuePriorityGeneric(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

   /**
     * Create an empty priority queue using the given comparator.
     */
    public d_QueuePriorityGeneric(Comparator<Key> comparator) { this(1, comparator); }

   /**
     * Is the priority queue empty?
     */
    public boolean isEmpty() {
        return N == 0;
    }

   /**
     * Return the number of elements on the priority queue.
     */
    public int size() {
        return N;
    }

   /**
     * Return the smallest key on the priority queue.
     * Throw an exception if no such key exists because the priority queue is empty.
     */
    public Key min() {
        if (isEmpty()) throw new RuntimeException("Priority queue underflow");
        return pq[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > N;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= N; i++) temp[i] = pq[i];
        pq = temp;
    }

   /**
     * Add a new key to the priority queue.
     */
    public void insert(Key x) {
        // double size of array if necessary
        if (N == pq.length - 1) resize(2 * pq.length);

        // add x, and percolate it up to maintain heap invariant
        pq[++N] = x;
        swim(N);
        assert isMinHeap();
    }

   /**
     * Delete and return the smallest key on the priority queue.
     * Throw an exception if no such key exists because the priority queue is empty.
     */
    public Key delMin() {
        if (N == 0) throw new RuntimeException("Priority queue underflow");
        exch(1, N);
        Key min = pq[N--];
        sink(1);
        pq[N+1] = null;         // avoid loitering and help with garbage collection
        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length  / 2);
        assert isMinHeap();
        return min;
    }


   /***********************************************************************
    * Helper functions to restore the heap invariant.
    **********************************************************************/

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

   /***********************************************************************
    * Helper functions for compares and swaps.
    **********************************************************************/
    private boolean greater(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
        }
        else {
            return comparator.compare(pq[i], pq[j]) > 0;
        }
    }

    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    // is pq[1..N] a min heap?
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    // is subtree of pq[1..N] rooted at k a min heap?
    private boolean isMinHeap(int k) {
        if (k > N) return true;
        int left = 2*k, right = 2*k + 1;
        if (left  <= N && greater(k, left))  return false;
        if (right <= N && greater(k, right)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }


   /***********************************************************************
    * Iterators
    **********************************************************************/

   /**
     * Return an iterator that iterates over all of the keys on the priority queue
     * in ascending order.
     * <p>
     * The iterator doesn't implement <tt>remove()</tt> since it's optional.
     */
    public Iterator<Key> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Key> {
        // create a new pq
        private d_QueuePriorityGeneric<Key> copy;

        // add all elements to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            if (comparator == null) copy = new d_QueuePriorityGeneric<Key>(size());
            else                    copy = new d_QueuePriorityGeneric<Key>(size(), comparator);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }

   /**
     * Test client.
     */
    public static void main(String[] args) {
        d_QueuePriorityGeneric<String> pq = new d_QueuePriorityGeneric<String>();

        // insert a bunch of strings
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };
        for (String s : strings) {
            pq.insert(s);
        }


        // delete and print each key
        while (!pq.isEmpty()) {
            System.out.println(pq.delMin());
        }
        System.out.println();

        // reinsert the same strings
        for (String s : strings) {
            pq.insert(s);
        }
        // print each key using the iterator
        for (String s : pq) {
            System.out.println(s);
        }
        while (!pq.isEmpty()) {
            pq.delMin();
        }
    }

}
