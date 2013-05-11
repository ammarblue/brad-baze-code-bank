package com.software.reuze;
import com.software.reuze.d_Queue;
import com.software.reuze.f_StdIn;

/*************************************************************************
 *  Compilation:  javac SeparateChainingHashST.java
 *  Execution:    java SeparateChainingHashST
 *
 *  A symbol table implemented with a separate-chaining hash table.
 * 
 *  % java SeparateChainingHashST
 *
 *************************************************************************/

public class da_TableSymbolHashChaining<Key, Value> {

    // largest prime <= 2^i for i = 3 to 31
    // not currently used for doubling and shrinking
    // private static final int[] PRIMES = {
    //    7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
    //    32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
    //    8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
    //    536870909, 1073741789, 2147483647
    // };

    private int N;                                // number of key-value pairs
    private int M;                                // hash table size
    private da_TableSymbolHashSequential<Key, Value>[] st;  // array of linked-list symbol tables


    // create separate chaining hash table
    public da_TableSymbolHashChaining() {
        this(997);
    } 

    // create separate chaining hash table with M lists
    public da_TableSymbolHashChaining(int M) {
        this.M = M;
        st = (da_TableSymbolHashSequential<Key, Value>[]) new da_TableSymbolHashSequential[M];
        for (int i = 0; i < M; i++)
            st[i] = new da_TableSymbolHashSequential<Key, Value>();
    } 

    // resize the hash table to have the given number of chains b rehashing all of the keys
    private void resize(int chains) {
        da_TableSymbolHashChaining<Key, Value> temp = new da_TableSymbolHashChaining<Key, Value>(chains);
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.M  = temp.M;
        this.N  = temp.N;
        this.st = temp.st;
    }

    // hash value between 0 and M-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    } 

    // return number of key-value pairs in symbol table
    public int size() {
        return N;
    } 

    // is the symbol table empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // is the key in the symbol table?
    public boolean contains(Key key) {
        return get(key) != null;
    } 

    // return value associated with key, null if no such key
    public Value get(Key key) {
        int i = hash(key);
        return st[i].get(key);
    } 

    // insert key-value pair into the table
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }
        int i = hash(key);
        if (!st[i].contains(key)) N++;
        st[i].put(key, val);
    } 

    // delete key (and associated value) if key is in the table
    public void delete(Key key) {
        int i = hash(key);
        if (st[i].contains(key)) N--;
        st[i].delete(key);
    } 

    // return keys in symbol table as an Iterable
    public Iterable<Key> keys() {
        d_Queue<Key> queue = new d_Queue<Key>();
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }
        return queue;
    } 


   /***********************************************************************
    *  Unit test client.
    ***********************************************************************/
    public static void main(String[] args) { 
        da_TableSymbolHashChaining<String, Integer> st = new da_TableSymbolHashChaining<String, Integer>();
        for (int i = 0; !f_StdIn.isEmpty(); i++) {
            String key = f_StdIn.readString();
            st.put(key, i);
        }

        // print keys
        for (String s : st.keys()) 
            System.out.println(s + " " + st.get(s)); 
    }

}
