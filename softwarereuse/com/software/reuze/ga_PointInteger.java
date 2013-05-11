package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *
 *  Implementation of 2D point with integer coordinates.
 *
 *  Illustrates the use of a non-static Comparator that orders 
 *  points by increasing distance to a base point.
 * 
 *  % java Point 10
 *  165.8463143998081 (584, 643)
 *  202.85216291674092 (665, 382)
 *  207.88458336298052 (320, 604)
 *  225.39077177204928 (724, 475)
 *  348.8624370722649 (536, 847)
 *  430.5090010673412 (917, 607)
 *  431.26442004876776 (533, 930)
 *  452.3593704125073 (898, 285)
 *  506.5984603213871 (19, 341)
 *  570.7092079159053 (897, 910)
 *
 *************************************************************************/


import java.util.Comparator;
import java.util.Arrays;

//TODO import com.software.reuze.vgu_StdDraw;

public class ga_PointInteger implements Comparable<ga_PointInteger> { 
    private final int x;
    private final int y;

    public final Comparator<ga_PointInteger> BY_DISTANCE_TO = new ByDistanceToComparator();
    public final Comparator<ga_PointInteger> BY_CCW         = new ByCCWComparator();

    public static final Comparator<ga_PointInteger> BY_X = new ByXComparator();
    public static final Comparator<ga_PointInteger> BY_Y = new ByYComparator();

    // create and initialize a point with given (x, y)
    public ga_PointInteger(int x, int y) {
       this.x = x;
       this.y = y;
    }

    // return Euclidean distance between this point and that point
    public double distanceTo(ga_PointInteger that) {
       double dx = this.x - that.x;
       double dy = this.y - that.y;
       return Math.sqrt(dx*dx + dy*dy);
    }

    // is a-b-c a counter-clockwise turn?
    // -1 if counter-clockwise, +1 if clockwise, 0 if collinear
    public static int ccw(ga_PointInteger a, ga_PointInteger b, ga_PointInteger c) {
       int area2 = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
       if      (area2 < 0) return -1;
       else if (area2 > 0) return +1;
       else                return  0;
    }

    // twice signed area of a-b-c
    public static int area2(ga_PointInteger a, ga_PointInteger b, ga_PointInteger c) {
       return (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
    }


    // convert to string
    public String toString() {
       return "(" + x + ", " + y + ")";
    }

    // plot this pointn using standard draw
    public void draw() {
       //TODO vgu_StdDraw.point(x, y);
    }

    // draw line segment from this point to that point
    public void drawTo(ga_PointInteger that) {
       //TODO vgu_StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // natural order: compare by y-coordinate; break ties by x-coordinate
    public int compareTo(ga_PointInteger that) {
       if (this.y < that.y) return -1;
       if (this.y > that.y) return +1;
       if (this.x > that.x) return -1;
       if (this.x < that.x) return +1;
       return 0;
    }

    // are the x- y-coordinates of the two points the same?
    public boolean equals(Object y) {
       if (y == this) return true;
       if (y == null) return false;
       if (y.getClass() != this.getClass()) return false;
       ga_PointInteger that = (ga_PointInteger) y;
       return (this.x == that.x) && (this.y == that.y);
    }

    /**********************************************************************
     *  Comparator that compares points according to polar angle
     *  they make with this point. The polar angle is measured with
     *  respect to a ray emanating from this point and pointing eastward.
     *  Break ties according to distance to this point.
     *
     *  Tie-breaking rule is only for degeneracy, e.g., if three
     *  collinear points on the convex hull, then this enables us
     *  to grab only first and last points.
     *
     *  Precondition:  q1 and q2 are in upper quadrant, relative to p.
     *
     *  Technically, this breaks the contract for compare() if
     *  called with points with y coordinate less than p.
     *
     **********************************************************************/
    private class ByCCWComparator implements Comparator<ga_PointInteger> {
       public int compare(ga_PointInteger q1, ga_PointInteger q2) {
          int ccw = ccw(ga_PointInteger.this, q1, q2);
          if (ccw == -1) return -1;
          if (ccw == +1) return +1;

          int dx1 = q1.x - x;
          int dx2 = q2.x - x;
          int dy1 = q1.y - y;
          int dy2 = q2.y - y;

          // assert dy1 >= 0 && dy2 >= 0;  // or breaks compare() contract

          // break ties by distance to this point
          // should be able to replace distance calculation with
          // projection because three points are collinear
          if      (dx1*dx1 + dy1*dy1 < dx2*dx2 + dy2*dy2) return -1;
          else if (dx1*dx1 + dy1*dy1 > dx2*dx2 + dy2*dy2) return +1;
          else                                            return  0;
       }
    }

    
    
    // compare points according to their distance to this point
    private class ByDistanceToComparator implements Comparator<ga_PointInteger> {
        public int compare(ga_PointInteger p, ga_PointInteger q) {
            if      (distanceTo(p) < distanceTo(q)) return -1;
            else if (distanceTo(p) > distanceTo(q)) return +1;
            else                                    return  0;
        }
    }

    // compare points according to their x-coordinate, breaking ties by y-coordinate
    private static class ByXComparator implements Comparator<ga_PointInteger> {
        public int compare(ga_PointInteger p, ga_PointInteger q) {
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           return 0;
        }
    }

    // compare points according to their y-coordinate, breaking ties by x-coordinate
    private static class ByYComparator implements Comparator<ga_PointInteger> {
        public int compare(ga_PointInteger p, ga_PointInteger q) {
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           return 0;
        }
    }



   // generate N random points and sort by distance to (500, 500)
   public static void main(String[] args) {
      int N;
      if (args.length==1) N = Integer.parseInt(args[0]); else N=40;
      ga_PointInteger[] points = new ga_PointInteger[N];
      for (int i = 0; i < N; i++) {
          int x = m_RandomStd.uniform(1000);
          int y = m_RandomStd.uniform(1000);
          points[i] = new ga_PointInteger(x, y);
      }
      ga_PointInteger base = new ga_PointInteger(500, 500);
      Arrays.sort(points, base.BY_DISTANCE_TO);
      for (int i = 0; i < N; i++) {
          System.out.println(base.distanceTo(points[i]) + " " + points[i]);
      }
   }
}
