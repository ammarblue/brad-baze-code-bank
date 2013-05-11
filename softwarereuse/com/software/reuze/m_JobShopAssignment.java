package com.software.reuze;
/*  Compilation:  javac Assignment.java
 *  Execution:    java Assignment N
 *  Dependencies: DijkstraSP.java DirectedEdge.java
 *
 *  Solve an assignment problem of N machines to N jobs in N^3 log N time using the
 *  successive shortest path algorithm.
 *
 *  Remark: could use dense version of Dijsktra's algorithm for
 *  improved theoretical efficiency of N^3, but it doesn't seem to
 *  help in practice.
 *
 *  Assumes N-by-N cost matrix is nonnegative.
 *
 *
 *********************************************************************/
import java.util.Arrays;
import java.util.Stack;

public class m_JobShopAssignment {
    private static final int UNMATCHED = -1;

    private int N;              // number of rows and columns
    private double[][] weight;  // the N-by-N cost matrix
    private double[] px;        // px[i] = dual variable for row i
    private double[] py;        // py[j] = dual variable for col j
    private int[] xy;           // xy[i] = j means i-j is a match
    private int[] yx;           // yx[j] = i means i-j is a match

 
    public m_JobShopAssignment(double[][] weight) {
        N = weight.length;
        this.weight = new double[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                this.weight[i][j] = weight[i][j];

        // dual variables
        px = new double[N];
        py = new double[N];

        // initial matching is empty
        xy = new int[N];
        yx = new int[N];
        for (int i = 0; i < N; i++) xy[i] = UNMATCHED;
        for (int j = 0; j < N; j++) yx[j] = UNMATCHED;

        // add N edges to matching
        for (int k = 0; k < N; k++) {
            System.out.println(k);
            assert isDualFeasible();
            assert isComplementarySlack();
            augment();
        }
        assert check();
    }

    // find shortest augmenting path and update
    private void augment() {

        // build residual graph
        dag_DigraphEdgeWeighted G = new dag_DigraphEdgeWeighted(2*N+2);
        int s = 2*N, t = 2*N+1;
        for (int i = 0; i < N; i++) {
            if (xy[i] == UNMATCHED) G.addEdge(new dag_DigraphEdgeWithWeight(s, i, 0.0));
        }
        for (int j = 0; j < N; j++) {
            if (yx[j] == UNMATCHED) G.addEdge(new dag_DigraphEdgeWithWeight(N+j, t, py[j]));
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (xy[i] == j) G.addEdge(new dag_DigraphEdgeWithWeight(N+j, i, 0.0));
                else            G.addEdge(new dag_DigraphEdgeWithWeight(i, N+j, reduced(i, j)));
            }
        }

        // compute shortest path from s to every other vertex
        dag_DigraphShortestPathDijkstra spt = new dag_DigraphShortestPathDijkstra(G, s);

        // augment along alternating path
        for (dag_DigraphEdgeWithWeight e : spt.pathTo(t)) {
            int i = e.from(), j = e.to() - N;
            if (i < N) {
                xy[i] = j;
                yx[j] = i;
            }
        }

        // update dual variables
        for (int i = 0; i < N; i++) px[i] += spt.distTo(i);
        for (int j = 0; j < N; j++) py[j] += spt.distTo(N+j);
    }

    // reduced cost of i-j
    private double reduced(int i, int j) {
        return weight[i][j] + px[i] - py[j];
    }

    // total weight of min weight perfect matching
    public double weight() {
        double total = 0.0;
        for (int i = 0; i < N; i++) {
            if (xy[i] != UNMATCHED)
                total += weight[i][xy[i]];
        }
        return total;
    }

    public int sol(int i) {
        return xy[i];
    }

    // check that dual variables are feasible
    private boolean isDualFeasible() {
        // check that all edges have >= 0 reduced cost
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (reduced(i, j) < 0) {
                    System.out.println("Dual variables are not feasible");
                    return false;
                }
            }
        }
        return true;
    }

    // check that primal and dual variables are complementary slack
    private boolean isComplementarySlack() {

        // check that all matched edges have 0-reduced cost
        for (int i = 0; i < N; i++) {
            if ((xy[i] != UNMATCHED) && (reduced(i, xy[i]) != 0)) {
                System.out.println("Primal and dual variables are not complementary slack");
                return false;
            }
        }
        return true;
    }

    // check that primal variables are a perfect matching
    private boolean isPerfectMatching() {

        // check that xy[] is a perfect matching
        boolean[] perm = new boolean[N];
        for (int i = 0; i < N; i++) {
            if (perm[xy[i]]) {
                System.out.println("Not a perfect matching");
                return false;
            }
            perm[xy[i]] = true;
        }

        // check that xy[] and yx[] are inverses
        for (int j = 0; j < N; j++) {
            if (xy[yx[j]] != j) {
                System.out.println("xy[] and yx[] are not inverses");
                return false;
            }
        }
        for (int i = 0; i < N; i++) {
            if (yx[xy[i]] != i) {
                System.out.println("xy[] and yx[] are not inverses");
                return false;
            }
        }

        return true;
    }


    // check optimality conditions
    private boolean check() {
        return isPerfectMatching() && isDualFeasible() && isComplementarySlack();
    }

    public static void main(String[] args) {

        int N = Integer.parseInt(args[0]);
        double[][] weight = new double[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                weight[i][j] = Math.random();
        System.out.println(Arrays.deepToString(weight));
        m_JobShopAssignment assignment = new m_JobShopAssignment(weight);
        System.out.println("weight = " + assignment.weight());
        for (int i = 0; i < N; i++)
            System.out.println(i + "-" + assignment.sol(i));
    }

}
