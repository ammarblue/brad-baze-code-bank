package com.software.reuze;
import com.software.reuze.dag_GraphIntegers;
import com.software.reuze.f_In;
import com.software.reuze.f_StdIn;

/*************************************************************************
 *  Compilation:  javac SymbolGraph.java
 *  Execution:    java SymbolGraph
 *  Dependencies: ST.java GraphIntegers.java In.java
 *  
 *  %  java SymbolGraph routes.txt " "
 *  JFK
 *     MCO
 *     ATL
 *     ORD
 *  LAX
 *     PHX
 *     LAS
 *
 *  % java SymbolGraph movies.txt "/"
 *  Tin Men (1987)
 *     DeBoy, David
 *     Blumenfeld, Alan
 *     ...
 *     Geppi, Cindy
 *     Hershey, Barbara ...
 *  Bacon, Kevin
 *     Mystic River (2003)
 *     Friday the 13th (1980)
 *     Flatliners (1990)
 *     Few Good Men, A (1992)
 *     ...
 *
 *************************************************************************/

public class da_TableStringAssociations {
    private d_TableSymbol<String, Integer> st;  // string -> index
    private String[] keys;           // index  -> string
    private dag_GraphIntegers G;

    public da_TableStringAssociations(String filename, String delimiter) {
        st = new d_TableSymbol<String, Integer>();

        // First pass builds the index by reading strings to associate
        // distinct strings with an index
        f_In in = new f_In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            for (int i = 0; i < a.length; i++) {
                if (!st.contains(a[i]))
                    st.put(a[i], st.size());
            }
        }

        // inverted index to get string keys in an aray
        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        // second pass builds the graph by connecting first vertex on each
        // line to all others
        G = new dag_GraphIntegers(st.size());
        in = new f_In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            int v = st.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                int w = st.get(a[i]);
                G.addEdge(v, w);
            }
        }
    }

    public boolean contains(String s) {
        return st.contains(s);
    }

    public int index(String s) {
        return st.get(s);
    }

    public String name(int v) {
        return keys[v];
    }

    public dag_GraphIntegers G() {
        return G;
    }


    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];
        da_TableStringAssociations sg = new da_TableStringAssociations(filename, delimiter);
        dag_GraphIntegers G = sg.G();
        while (!f_StdIn.isEmpty()) {
            String t = f_StdIn.readLine();
            for (int v : G.adj(sg.index(t))) {
                System.out.println("   " + sg.name(v));
            }
        }
    }
}
