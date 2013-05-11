package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac SuffixArray.java
 *  Execution:    java SuffixArray < input.txt
 *  
 *  A data type that computes the suffix array of a string.
 *
 *  % echo "abracadabra" | java SuffixArray
 *    10 a
 *     7 abra
 *     0 abracadabra
 *     3 acadabra
 *     5 adabra
 *     8 bra
 *     1 bracadabra
 *     4 cadabra
 *     6 dabra
 *     9 ra
 *     2 racadabra
 *
 *************************************************************************/

import java.util.Arrays;


public class da_StringSuffixArray {
    public final String[] suffixes;
    private final int N;

    public da_StringSuffixArray(String s) {
        N = s.length();
        suffixes = new String[N];
        for (int i = 0; i < N; i++)
            suffixes[i] = s.substring(i);
        Arrays.sort(suffixes);
    }

    // size of string
    public int length() { return N; }

    // index of ith sorted suffix
    public int index(int i) { return N - suffixes[i].length(); }

    // ith sorted suffix
    public String select(int i) { return suffixes[i]; }

    // number of suffixes strictly less than query
    public int rank(String query) {
        int lo = 0, hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = query.compareTo(suffixes[mid]);
            if      (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    } 

   // length of longest common prefix of s and t
    private static int lcp(String s, String t) {
        int N = Math.min(s.length(), t.length());
        for (int i = 0; i < N; i++)
            if (s.charAt(i) != t.charAt(i)) return i;
        return N;
    }

    // longest common prefix of suffixes(i) and suffixes(i-1)
    public int lcp(int i) {
        return lcp(suffixes[i], suffixes[i-1]);
    }

    // longest common prefix of suffixes(i) and suffixes(j)
    public int lcp(int i, int j) {
        return lcp(suffixes[i], suffixes[j]);
    }




    public static void main(String[] args) {
        String s = f_StdIn.readAll().trim();
        da_StringSuffixArray suffix = new da_StringSuffixArray(s);

        System.out.println("  i ind lcp rnk  select");
        System.out.println("---------------------------");
        System.out.printf("%3d %3d %3s %3d  %s\n", 0, suffix.index(0), "-", suffix.rank(suffix.select(0)), suffix.select(0));
        for (int i = 1; i < s.length(); i++) {
            int index = suffix.index(i);
            String ith = suffix.select(i);
            int lcp = suffix.lcp(i, i-1);
            int rank = suffix.rank(ith);
            System.out.printf("%3d %3d %3d %3d  %s\n", i, index, lcp, rank, ith);
        }
    }

}

