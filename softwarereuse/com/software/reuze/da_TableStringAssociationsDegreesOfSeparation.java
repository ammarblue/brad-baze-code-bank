package com.software.reuze;
import com.software.reuze.dag_GraphIntegers;
import com.software.reuze.dag_GraphVisitBreadthFirst;
import com.software.reuze.f_StdIn;

/*************************************************************************
 *  Compilation:  javac DegreesOfSeparation.java
 *  Execution:    java DegreesOfSeparation filename delimiter
 *  Dependencies: SymbolGraph.java Graph.java BreadthFirstPaths.java
 *  
 *  %  java DegreesOfSeparation routes.txt " " "JFK"
 *  java DegreesOfSeparation routes.txt " " JFK
 *  Source: JFK
 *  LAS
 *     JFK
 *     ORD
 *     DEN
 *     LAS
 *  DFW
 *     JFK
 *     ORD
 *     DFW
 *  EWR
 *     Not in database.
 *
 *  %  java DegreesOfSeparation movies.txt "/" "Bacon, Kevin"
 *  Source: Bacon, Kevin
 *  Kidman, Nicole
 *     Bacon, Kevin
 *     Few Good Men, A (1992)
 *     Cruise, Tom
 *     Days of Thunder (1990)
 *     Kidman, Nicole
 *  Grant, Cary
 *     Bacon, Kevin
 *     Mystic River (2003)
 *     Willis, Susan
 *     Majestic, The (2001)
 *     Landau, Martin
 *     North by Northwest (1959)
 *     Grant, Cary
 *
 *  % java DegreesOfSeparation movies.txt "/" "Animal House (1978)"
 *  Titanic (1997)
 *     Animal House (1978)
 *     Allen, Karen (I)
 *     Raiders of the Lost Ark (1981)
 *     Taylor, Rocky (I)
 *     Titanic (1997)
 *  To Catch a Thief (1955)
 *     Animal House (1978)
 *     Vernon, John (I)
 *     Topaz (1969)
 *     Hitchcock, Alfred (I)
 *     To Catch a Thief (1955)
 *
 *************************************************************************/

public class da_TableStringAssociationsDegreesOfSeparation {
    public static void main(String[] args) {
        String filename  = args[0];
        String delimiter = args[1];
        String source    = args[2];

        System.out.println("Source: " + source);

        da_TableStringAssociations sg = new da_TableStringAssociations(filename, delimiter);
        dag_GraphIntegers G = sg.G();
        if (!sg.contains(source)) {
        	System.out.println(source + " not in database.");
            return;
        }

        int s = sg.index(source);
        dag_GraphVisitBreadthFirst bfs = new dag_GraphVisitBreadthFirst(G, s);

        while (!f_StdIn.isEmpty()) {
            String sink = f_StdIn.readLine();
            if (sg.contains(sink)) {
                int t = sg.index(sink);
                if (bfs.hasPathTo(t)) {
                    for (int v : bfs.pathTo(t)) {
                    	System.out.println("   " + sg.name(v));
                    }
                }
                else {
                	System.out.println("Not connected");
                }
            }
            else {
            	System.out.println("   Not in database.");
            }
        }
    }
}
