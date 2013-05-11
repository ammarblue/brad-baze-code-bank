package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac TransitiveClosure.java
 *  Execution:    java TransitiveClosure V E
 *  Dependencies: Digraph.java DepthFirstDirectedPaths.java
 *
 *  Compute transitive closure of a digraph and support
 *  reachability queries.
 *
 *  Preprocessing time: O(V(E + V)) time.
 *  Query time: O(1).
 *  Space: O(V^2).
 *
 *  % java TransitiveClosure 10 20
 *  V = 10
 *  E = 20
 *  0: 9 2 7 5 
 *  1: 6 7 
 *  2: 
 *  3: 9 9 9 7 6 
 *  4: 7 
 *  5: 7 
 *  6: 7 1 
 *  7: 2 
 *  8: 7 6 0 
 *  9: 2 
 *  
 *  Transitive closure
 *  -----------------------------------
 *         0  1  2  3  4  5  6  7  8  9
 *    0:   x     x        x     x     x
 *    1:      x  x           x  x      
 *    2:         x                     
 *    3:      x  x  x        x  x     x
 *    4:         x     x        x      
 *    5:         x        x     x      
 *    6:      x  x           x  x      
 *    7:         x              x      
 *    8:   x  x  x        x  x  x  x  x
 *    9:         x                    x
 *
 *************************************************************************/

public class dag_DigraphTransitiveClosure {
    public dag_DigraphVisitDepthFirstSet[] tc;  // tc[v] = reachable from v

    public dag_DigraphTransitiveClosure(dag_Digraph G) {
        tc = new dag_DigraphVisitDepthFirstSet[G.V()];
        for (int v = 0; v < G.V(); v++)
            tc[v] = new dag_DigraphVisitDepthFirstSet(G, v);
    }

    public boolean reachable(int v, int w) {
        return tc[v].isReachable(w);
    }

    // test client
    public static void main(String[] args) {
    	int V;
        int E;
        if (args.length<2) {
        	V=5;  E=9;
        } else {
        	V = Integer.parseInt(args[0]);
        	E  = Integer.parseInt(args[1]);
        }
        dag_Digraph G = new dag_Digraph(V, E);
        System.out.println(G);
        dag_DigraphTransitiveClosure tc = new dag_DigraphTransitiveClosure(G);

        // print header
        System.out.println("Transitive closure");
        System.out.println("-----------------------------------");
        System.out.print("     ");
        for (int v = 0; v < G.V(); v++)
            System.out.printf("%3d", v);
        System.out.println();

        // print transitive closure
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%3d: ", v);
            for (int w = 0; w < G.V(); w++) {
                if (tc.reachable(v, w)) System.out.printf("  x");
                else                    System.out.printf("   ");
            }
            System.out.println();
        }
    }

}
