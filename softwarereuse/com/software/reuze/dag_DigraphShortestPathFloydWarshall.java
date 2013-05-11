package com.software.reuze;
import java.util.Arrays;
public class dag_DigraphShortestPathFloydWarshall {
static int x[][];
static int nV;
static void add(int from, int to) {
    assert(from > 0 && from <= nV);
    assert(to > 0 && to <= nV);
    x[from-1][to-1]=1;
}
static void FloydWarshall() {
    for (int k=0; k<nV; k++) {
        for (int i=0; i<nV; i++)
            for (int j=0; j<nV; j++) //all paths via k
                x[i][j]=Math.min(x[i][j], x[i][k]+x[k][j]);
        System.out.println(Arrays.deepToString(x));
    }
}
public static void main(String[] args) {
    x=new int[6][6];
    nV=6;
    for (int i=0; i<nV; i++)
        for (int j=0; j<nV; j++)
            x[i][j]=99;
    add(1,6); add(2,6); add(3,5);
    add(4,1); add(4,2); add(6,3);
    FloydWarshall();
}
}
