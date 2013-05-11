package com.software.reuze;
public class da_TreeBinarySearchOptimalInt {
private static int[] weights = {0,5,14,10,7,18,9};
private static final int n = weights.length-1;
private static int[][] cost = new int[n+2][n+1];
private static int[][] root = new int[n+2][n+1];
public static void main(String[] args) {
optimize();
for ( int i = 1; i < n + 2; i++ ) {
    for ( int j = 0; j < n + 1; j++ )
        if ( j >= i - 1 ) System.out.printf( "%4d", root[i][j]);
        else System.out.print("    ");
    System.out.println();
}
for ( int i = 1; i < n + 2; i++ ) {
    for ( int j = 0; j < n + 1; j++ )
        if ( j >= i - 1 ) System.out.printf( "%4d", cost[i][j]);
        else System.out.print("    ");
    System.out.println();
}
}

public static void optimize() {
for ( int low = n + 1; low >= 1; low-- )
    for ( int high = low - 1; high <= n; high++ ) choose(low, high );
}

public static void choose(int low, int high ) {
int bestCost=0, bestRoot= -1, rCost, r,k=0;
if ( high > low ) { //non-empty tree
    bestCost = Integer.MAX_VALUE;
    k=p(low, high );
} else if (low == high) {
    bestCost = Integer.MAX_VALUE;
    k=weights[low];
}
for (r = low; r <= high; r++ ) {
    rCost = k+cost[low][r-1] + cost[r+1][high];
    if (rCost < bestCost) {
        bestCost = rCost;  bestRoot = r;
     }
}
cost[low][high] = bestCost;
root[low][high] = bestRoot;
}

public static int p(int low, int high ) {
int weight = 0;
if ( low <= high )
    for ( int i = low; i <= high; i++ ) weight += weights[i];
return weight;
}
}
