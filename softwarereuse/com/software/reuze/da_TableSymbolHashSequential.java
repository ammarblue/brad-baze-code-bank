package com.software.reuze;
import com.software.reuze.d_Queue;
import com.software.reuze.f_StdIn;

/*************************************************************************
 *  Compilation:  javac SequentialSearchST.java
 *  Execution:    java SequentialSearchST
 *  
 *  Symbol table implementation with sequential search in an
 *  unordered linked list of key-value pairs.
 *
 *  % more tiny.txt
 *  S E A R C H E X A M P L E
 *
 *  % java SequentialSearchST
 *  java SequentialSearchST < tiny.txt 
 *  L 11
 *  P 10
 *  M 9
 *  X 7
 *  H 5
 *  C 4
 *  R 3
 *  A 8
 *  E 12
 *  S 0
 *
 *************************************************************************/

public class da_TableSymbolHashSequential<Key, Value> {
    private int N;           // number of key-value pairs
    private Node first;      // the linked list of key-value pairs

    // a helper linked list data type
    private class Node {
        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next)  {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    // return number of key-value pairs
    public int size() { return N; }

    // is the symbol table empty?
    public boolean isEmpty() { return size() == 0; }

    // does this symbol table contain the given key?
    public boolean contains(Key key) {
        return get(key) != null;
    }

    // return the value associated with the key, or null if the key is not present
    public Value get(Key key) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) return x.val;
        }
        return null;
    }

    // add a key-value pair, replacing old key-value pair if key is already present
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }
        for (Node x = first; x != null; x = x.next)
            if (key.equals(x.key)) { x.val = val; return; }
        first = new Node(key, val, first);
        N++;
    }

    // remove key-value pair with given key (if it's in the table)
    public void delete(Key key) {
        first = delete(first, key);
    }

    // delete key in linked list beginning at Node x
    // warning: function call stack too large if table is large
    private Node delete(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) { N--; return x.next; }
        x.next = delete(x.next, key);
        return x;
    }


    // return all keys as an Iterable
    public Iterable<Key> keys()  {
        d_Queue<Key> queue = new d_Queue<Key>();
        for (Node x = first; x != null; x = x.next)
            queue.enqueue(x.key);
        return queue;
    }




   /***********************************************************************
    * Test client
    **********************************************************************/
    public static void main(String[] args) {
        da_TableSymbolHashSequential<String, Integer> st = new da_TableSymbolHashSequential<String, Integer>();
        for (int i = 0; !f_StdIn.isEmpty(); i++) {
            String key = f_StdIn.readString();
            st.put(key, i);
        }
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }
}
