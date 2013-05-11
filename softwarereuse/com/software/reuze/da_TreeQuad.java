package com.software.reuze;

/*************************************************************************
 *  Compilation:  javac QuadTree.java
 *  Execution:    java QuadTree M N
 *
 *  Quad tree.
 * 
 *************************************************************************/

public class da_TreeQuad<Key extends Comparable, Value>  {
    private Node root;

    // helper node data type
    private class Node {
        Key x, y;              // x- and y- coordinates
        Node NW, NE, SE, SW;   // four subtrees
        Value value;           // associated data

        Node(Key x, Key y, Value value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

  /***********************************************************************
    *  Insert (x, y) into appropriate quadrant
    ***********************************************************************/
    public void insert(Key x, Key y, Value value) {
        root = insert(root, x, y, value);
    }

    private Node insert(Node h, Key x, Key y, Value value) {
        if (h == null) return new Node(x, y, value);
        int a=x.compareTo(h.x);
        int b=y.compareTo(h.y);
        if (a==0 && b==0) h.value = value;  // duplicate
        else if (a<0 &&  b<0) h.SW = insert(h.SW, x, y, value);
        else if (a<0 && b>=0) h.NW = insert(h.NW, x, y, value);
        else if (a>=0 &&  b<0) h.SE = insert(h.SE, x, y, value);
        else if (a>=0 && b>=0) h.NE = insert(h.NE, x, y, value);
        return h;
    }

  /***********************************************************************
    *  Range search.
    ***********************************************************************/

    public void query2D(m_Interval2D rect) {
        query2D(root, rect);
    }

    private void query2D(Node h, m_Interval2D rect) {
        if (h == null) return;
        if (rect.contains(((Number)h.x).doubleValue(), ((Number)h.y).doubleValue()))
            System.out.println("    (" + h.x + ", " + h.y + ") " + h.value);
        Key xmin = (Key)rect.getX().getLeft();
        Key ymin = (Key)rect.getY().getLeft();
        if ( less(xmin, h.x) &&  less(ymin, h.y)) query2D(h.SW, rect);
        Key ymax = (Key)rect.getY().getRight();
        if ( less(xmin, h.x) && !less(ymax, h.y)) query2D(h.NW, rect);
        Key xmax = (Key)rect.getX().getRight();
        if (!less(xmax, h.x) &&  less(ymin, h.y)) query2D(h.SE, rect);
        if (!less(xmax, h.x) && !less(ymax, h.y)) query2D(h.NE, rect);
    }


   /*************************************************************************
    *  helper comparison functions
    *************************************************************************/

    private boolean less(Key k1, Key k2) { return k1.compareTo(k2) <  0; }
    private boolean eq  (Key k1, Key k2) { return k1.compareTo(k2) == 0; }

   /*************************************************************************
    *  test client
    *************************************************************************/
    public static void main(String[] args) {
    	int M; //queries
        int N; //points
        if (args.length<2) {
        	M=5;  N=29;
        } else {
        	M = Integer.parseInt(args[0]);
        	N  = Integer.parseInt(args[1]);
        }

        da_TreeQuad<Integer, String> st = new da_TreeQuad<Integer, String>();

        // insert N random points in the unit square
        for (int i = 0; i < N; i++) {
            Integer x = (int) (100 * Math.random());
            Integer y = (int) (100 * Math.random());
            System.out.println("(" + x + ", " + y + ")");
            st.insert(x, y, "P" + i);
        }
        System.out.println("Done preprocessing " + N + " points");

        // do some range searches
        for (int i = 0; i < M; i++) {
            Integer xmin = (int) (100 * Math.random());
            Integer ymin = (int) (100 * Math.random());
            Integer xmax = xmin + (int) (10 * Math.random());
            Integer ymax = ymin + (int) (20 * Math.random());
            m_Interval1D<Integer> intX = new m_Interval1D<Integer>(xmin, xmax);
            m_Interval1D<Integer> intY = new m_Interval1D<Integer>(ymin, ymax);
            m_Interval2D rect = new m_Interval2D(intX, intY);
            System.out.println(rect + " : ");
            st.query2D(rect);
        }
    }

}
