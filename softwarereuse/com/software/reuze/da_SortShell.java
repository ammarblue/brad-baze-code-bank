package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac Shell.java
 *  Execution:    java Shell N
 *  
 *  Shellsort sort N random real numbers between 0 and 1.
 *
 *  Uses increment sequence proposed by Sedgewick and Incerpi.
 *  The nth element of the sequence is the smallest integer >= 2.5^n
 *  that is relatively prime to all previous terms in the sequence.
 *  For example, incs[4] is 41 because 2.5^4 = 39.0625 and 41 is
 *  the next integer that is relatively prime to 3, 7, and 16.
 *
 *************************************************************************/

public class da_SortShell {

    // sort the array a[] in ascending order using Shellsort
    public static void sort(Comparable[] a) {
        int N = a.length;

        // 3x+1 increment sequence:  1, 4, 13, 40, 121, 364, 1093, ... 
        int h = 1;
        while (h < N/3) h = 3*h + 1; 

        while (h >= 1) {
            // h-sort the array
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h) {
                    exch(a, j, j-h);
                }
            }
            assert isSorted(a, h); 
            h /= 3;
        }
        assert isSorted(a);
    }



   /***********************************************************************
    *  Helper sorting functions
    ***********************************************************************/
    
    // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
    }
        
    // exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }


   /***********************************************************************
    *  Check if array is sorted - useful for debugging
    ***********************************************************************/
    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }

    // is the array h-sorted?
    private static boolean isSorted(Comparable[] a, int h) {
        for (int i = h; i < a.length; i++)
            if (less(a[i], a[i-h])) return false;
        return true;
    }


    // test client
    public static void main(String[] args) {

        // generate array of N random reals between 0 and 1
        int N;
        if (args.length==1) N = Integer.parseInt(args[0]); else N=12;
        Double[] a = new Double[N];
        for (int i = 0; i < N; i++) {
            a[i] = Math.random();
        }
        
        // sort the array
        sort(a);

        // display results
        for (int i = 0; i < N; i++) {
            System.out.println(a[i]);
        }
        System.out.println("isSorted = " + isSorted(a));
    }

}
