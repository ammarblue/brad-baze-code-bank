package com.software.reuze;



public class ib_GraphTiled {
	/**
	 * Create a tiled graph from an image.
	 * This method will accept 1 or 2 images to create a tiled graph (a 2D rectangular
	 * arrangements of nodes.
	 * TODO draws lines across black star points
	 */
	public static void makeGraphFromBWimage(dag_GraphFloatingEdges g, z_BufferedImage backImg, z_BufferedImage costImg, int tilesX, int tilesY, boolean allowDiagonals) {
	  int dx = backImg.getWidth() / tilesX;
	  int dy = backImg.getHeight() / tilesY;
	  int sx = dx / 2, sy = dy / 2;
	  // use deltaX to avoid horizontal wrap around edges
	  int deltaX = tilesX + 1; // must be > tilesX

	  float hCost = dx, vCost = dy, dCost = (float)Math.sqrt(dx*dx + dy*dy);
	  float cost = 0;
	  int px, py, nodeID, col;
	  dag_GraphNode aNode;

	  py = sy;
	  for (int y = 0; y < tilesY ; y++) {
	    nodeID = deltaX * y + deltaX;
	    px = sx;
	    for (int x = 0; x < tilesX; x++) {
	      // Calculate the cost
	      if (costImg == null) {  //black or white
	        col = backImg.getRGB(px, py) & 0xff00;
	        cost = 1;
	      }
	      else {
	        col = costImg.getRGB(px, py);  //assumes r==g==b for gray scale
	        col = (col&0xff00)>>8;   //works no matter where alpha is
	        cost = 1.0f + (256.0f - col)/ 16.0f;
	      }
	      // If color is not black then create the node and edges
	      if (col != 0) {
	        aNode = new dag_GraphNode(nodeID, px, py);
	        g.addNode(aNode);
	        if (x > 0) {
	          g.addEdge(nodeID, nodeID - 1, hCost * cost);
	          if (allowDiagonals) {
	            g.addEdge(nodeID, nodeID - deltaX - 1, dCost * cost);
	            g.addEdge(nodeID, nodeID + deltaX - 1, dCost * cost);
	          }
	        }
	        if (x < tilesX -1) {
	          g.addEdge(nodeID, nodeID + 1, hCost * cost);
	          if (allowDiagonals) {
	            g.addEdge(nodeID, nodeID - deltaX + 1, dCost * cost);
	            g.addEdge(nodeID, nodeID + deltaX + 1, dCost * cost);
	          }
	        }
	        if (y > 0)
	          g.addEdge(nodeID, nodeID - deltaX, vCost * cost);
	        if (y < tilesY - 1)
	          g.addEdge(nodeID, nodeID + deltaX, vCost * cost);
	      }
	      px += dx;
	      nodeID++;
	    }
	    py += dy;
	  }
	}
}
