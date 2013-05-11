package com.software.reuze;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
/*
# Declare the node positions between tags <nodes></nodes>.
# 1st = the unique id number for the node it should be >= 0
#       but the id numbers do not need to start with zero or
#       be sequential
# 2nd = the horizontal location of the node
# 3nd = the vertical location of the node
#
# although the locations are expressed as integers in this
# file they are parsed as floats.
#

<nodes>
100 40 100
101 124 105
102 171 102
</nodes>

# Declare the edges between tags <edges></edges>.
# 1st = the node id for the 'from' node
# 2nd = the node id for the 'to' node
# 3rd = the cost of traveling 'from' >> 'to'
# 4th = the cost of traveling 'to' >> 'from'
# If either cost is 0 (zero) then the demo program
# calculates the Euclidean (shortest) distance between
# them and uses that as the cost.
# If either cost is <0.0 then that edge will not be
# created.
# The fourth value is optional and if missing then the 
# edge will not be created (equivalent to -1)
#
# although the costs are expressed as integers in this
# file they are parsed as floats by the program
#

<edges>
100 101 84 84
101 102 46 46
102 107 40
</edges>
 */



public class f_dag_GraphFloatingEdges {
	public static void addToGraphFromFile(dag_GraphFloatingEdges g/*inout*/, String fname) {
		InputStream    fis;
		BufferedReader br;
		String         line;
		int mode = 0;
		int count = 0;
		fis = z_Processing.app.createInput(fname);
		br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		while (true) {
			try {
				line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			if (line==null) break;
		    line=line.trim();
		    if (!line.startsWith("#") && line.length() > 1) {
		      switch(mode) {
		      case 0:
		        if (line.equalsIgnoreCase("<nodes>"))
		          mode = 1;
		        else if (line.equalsIgnoreCase("<edges>"))
		          mode = 2;
		        break;
		      case 1:
		        if (line.equalsIgnoreCase("</nodes>"))
		          mode = 0;
		        else 
		          makeNode(line, g);
		        break;
		      case 2:
		        if (line.equalsIgnoreCase("</edges>"))
		          mode = 0;
		        else
		          makeEdge(line, g);
		        break;
		      } // end switch
		    } // end if
		    count++;
		  } // end while
		}
	
	public static void makeNode(String s, dag_GraphFloatingEdges g) {
		  int nodeID;
		  float x, y, z = 0;
		  String part[] = s.split(" ");
		  if (part.length >= 3) {
		    nodeID = Integer.parseInt(part[0]);
		    x = Float.parseFloat(part[1]);
		    y = Float.parseFloat(part[2]);
		    if (part.length >=4)
		      z = Float.parseFloat(part[3]);
		    g.addNode(new dag_GraphNode(nodeID, x, y, z));
		  }
		}
	
	/**
	 * Creates an edge(s) between 2 nodes.
	 * 
	 * Each line of the configuration file has either 3 or 4 entries, the
	 * first 2 are the node id numbers, for the from and to nodes. If 
	 * either node does not exist then the edge will not be created.
	 * 
	 * The third value is the cost from-to nodes and the fourth the cost 
	 * to-from node (i.e. the return cost). So it is possible to create a 
	 * bidirectional route between the nodes with different costs.
	 * 
	 * Note if a fourth value is not provided then it will not create the
	 * edge for the return route.
	 * 
	 * In both cases if the cost is =0 then the cost is calculated as the
	 * euclidean distance (shortest) between the nodes. If the cost <0 
	 * then that edge will not be created.
	 *  
	 * @param s a line from the configuration file.
	 * @param g the graph to add the edge.
	 */
	public static void makeEdge(String s, dag_GraphFloatingEdges g) {
	  int fromID, toID;
	  float costOut = 0, costBack = 0;
	  String part[] = s.split(" ");
	  if (part.length >= 3) {
	    fromID = Integer.parseInt(part[0]);
	    toID = Integer.parseInt(part[1]);
	    try {
	      costOut = Float.parseFloat(part[2]);
	    }
	    catch(Exception excp) {
	      costOut = -1;
	    }
	    try {
	      costBack = Float.parseFloat(part[3]);
	    }
	    catch(Exception excp) {
	      costBack = -1;
	    }
	    if (costOut >= 0)
	      g.addEdge(fromID, toID, costOut);
	    if (costBack >= 0)
	      g.addEdge(toID, fromID, costBack);
	  }
	}
}
