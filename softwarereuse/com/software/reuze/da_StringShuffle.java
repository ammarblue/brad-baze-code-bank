package com.software.reuze;

/*************************************************************************
 *  Compilation:  javac Shuffle.java
 *  Execution:    java Shuffle < list.txt
 *  
 *  Reads in a list of strings and prints them in random order.
 *  The Knuth (or Fisher-Yates) shuffling algorithm guarantees
 *  to rearrange the elements in uniformly random order, under
 *  the assumption that Math.random() generates independent and
 *  uniformly distributed numbers between 0 and 1.
 *
 *  % more cards.txt
 *  2C 3C 4C 5C 6C 7C 8C 9C 10C JC QC KC AC
 *  2D 3D 4D 5D 6D 7D 8D 9D 10D JD QD KD AD
 *  2H 3H 4H 5H 6H 7H 8H 9H 10H JH QH KH AH
 *  2S 3S 4S 5S 6S 7S 8S 9S 10S JS QS KS AS
 *
 *  % java Shuffle < cards.txt
 *  6H
 *  9C
 *  8H
 *  7C
 *  JS
 *  ...
 *  KH
 *
 *************************************************************************/

public class da_StringShuffle { 
    public static void main(String args[]) {

        // read in the data
        String[] a = f_StdIn.readAll().split("\\s+");
        shuffle(a);

        // print permutation
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
    }
    
    public static void shuffle(String[] a /*inout*/) {
        // shuffle
    	int N = a.length;
        for (int i = 0; i < N; i++) {
            // int from remainder of deck
            int r = i + (int) (Math.random() * (N - i));
            String swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }
    }
}

