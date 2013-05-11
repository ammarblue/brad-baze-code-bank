package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac Point2D.java
 *
 *  Immutable point data type for points in the plane.
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

public class ga_PointDouble implements Comparable<ga_PointDouble> {
    public static final Comparator<ga_PointDouble> BY_X_ORDER = new ByXComparator();
    public static final Comparator<ga_PointDouble> BY_Y_ORDER = new ByYComparator();

    public final Comparator<ga_PointDouble> BY_DISTANCE_TO_ORDER = new DistanceToOrder();

    private final double x;    // x coordinate
    private final double y;    // y coordinate

    public ga_PointDouble(double x, double y) { this.x = x; this.y = y; }

    // return the x-coordinate of this point
    public double x() { return x; }

    // return the y-coordinate of this point
    public double y() { return y; }

    // return the radius of this point in polar coordinates
    public double r() { return Math.sqrt(x*x + y*y); }

    // return the angle of this point in polar coordinates
    // (between -pi/2 and pi/2)
    public double theta() { return Math.atan2(y, x); }

    // return Euclidean distance between this point and that point
    public double distanceTo(ga_PointDouble that) {
       double dx = this.x - that.x;
       double dy = this.y - that.y;
       return Math.sqrt(dx*dx + dy*dy);
    }

    // return square of Euclidean distance between this point and that point
    public double distanceSquaredTo(ga_PointDouble that) {
       double dx = this.x - that.x;
       double dy = this.y - that.y;
       return dx*dx + dy*dy;
    }

    // compare by y-coordinate, breaking ties by x-coordinate
    public int compareTo(ga_PointDouble that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0;
    }

    // compare points according to their x-coordinate, breaking ties by y-coordinate
    private static class ByXComparator implements Comparator<ga_PointDouble> {
        public int compare(ga_PointDouble p, ga_PointDouble q) {
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           return 0;
        }
    }

    // compare points according to their y-coordinate, breaking ties by x-coordinate
    private static class ByYComparator implements Comparator<ga_PointDouble> {
        public int compare(ga_PointDouble p, ga_PointDouble q) {
           if (p.y < q.y) return -1;
           if (p.y > q.y) return +1;
           if (p.x < q.x) return -1;
           if (p.x > q.x) return +1;
           return 0;
        }
    }
 
    // compare points according to their distance to this point
    private class DistanceToOrder implements Comparator<ga_PointDouble> {
        public int compare(ga_PointDouble p, ga_PointDouble q) {
            double dist1 = distanceSquaredTo(p);
            double dist2 = distanceSquaredTo(q);
            if      (dist1 < dist2) return -1;
            else if (dist1 > dist2) return +1;
            else                    return  0;
        }
    }


    // does this point equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        ga_PointDouble that = (ga_PointDouble) y;
        return this.x == that.x && this.y == that.y;
    }

    // convert to string
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // plot using StdDraw
    public void draw() {
        //StdDraw.point(x, y);
    }

    // draw line from this point p to q using StdDraw
    public void drawTo(ga_PointDouble that) {
        //StdDraw.line(this.x, this.y, that.x, that.y);
    }


    public static void main(String[] args) {
    	int N;
        if (args.length==1) N = Integer.parseInt(args[0]); else N=40;
        ga_PointDouble[] points = new ga_PointDouble[N];
        for (int i = 0; i < N; i++) {
            int x = m_RandomStd.uniform(1000);
            int y = m_RandomStd.uniform(1000);
            points[i] = new ga_PointDouble(x, y);
        }
        ga_PointDouble base = new ga_PointDouble(500, 500);
        Arrays.sort(points, base.BY_DISTANCE_TO_ORDER);
        for (int i = 0; i < N; i++) {
            System.out.println(base.distanceTo(points[i]) + " " + points[i]);
        }
    }
}
