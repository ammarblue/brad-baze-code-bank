package com.software.reuze;

/*************************************************************************
 *  Compilation:  javac Arbitrage.java
 *  Execution:    java Arbitrage < input.txt
 *  Dependencies: EdgeWeightedDigraph.java DirectedEdge.java
 *                BellmanFordSP.java
 *  Data file:    http://algs4.cs.princeton.edu/44sp/rates.txt
 *
 *  Arbitrage detection.
 *
 *  % more rates.txt
 *  5
 *  USD 1      0.741  0.657  1.061  1.005
 *  EUR 1.349  1      0.888  1.433  1.366
 *  GBP 1.521  1.126  1      1.614  1.538
 *  CHF 0.942  0.698  0.619  1      0.953
 *  CAD 0.995  0.732  0.650  1.049  1
 *
 *  % java Arbitrage < rates.txt
 *  1000.00000 USD =  741.00000 EUR
 *   741.00000 EUR = 1012.20600 CAD
 *  1012.20600 CAD = 1007.14497 USD
 *
 *************************************************************************/

public class b_MoneyArbitrage {

    public static void main(String[] args) {

        // V currencies
        int V = f_StdIn.readInt();
        String[] name = new String[V];

        // create complete network
        dag_DigraphEdgeWeighted G = new dag_DigraphEdgeWeighted(V);
        for (int v = 0; v < V; v++) {
            name[v] = f_StdIn.readString();
            for (int w = 0; w < V; w++) {
                double rate = f_StdIn.readDouble();
                dag_DigraphEdgeWithWeight e = new dag_DigraphEdgeWithWeight(v, w, -Math.log(rate));
                G.addEdge(e);
            }
        }

        // find negative cycle
        dag_DigraphShortestPathBellmanFord spt = new dag_DigraphShortestPathBellmanFord(G, 0);
        if (spt.hasNegativeCycle()) {
            double stake = 1000.0;
            for (dag_DigraphEdgeWithWeight e : spt.negativeCycle()) {
                System.out.printf("%10.5f %s ", stake, name[e.to()]);
                stake *= Math.exp(-e.weight());
                System.out.printf("-> %10.5f %s\n", stake, name[e.from()]);
            }
        }
        else {
            System.out.println("No arbitrage opportunity");
        }
    }

}
