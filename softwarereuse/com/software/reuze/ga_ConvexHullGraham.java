package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac GrahamaScan.java
 *  Execution:    java GrahamScan < input.txt
 * 
 *  Create points from standard input and compute the convex hull using
 *  Graham scan algorithm.
 *
 *************************************************************************/


import java.util.Arrays;
import java.util.Stack;

import com.software.reuze.f_StdIn;

public class ga_ConvexHullGraham {
    private Stack<ga_PointInteger> hull = new Stack<ga_PointInteger>();

    public ga_ConvexHullGraham(ga_PointInteger[] pts) {

        // defensive copy
        int N = pts.length;
        ga_PointInteger[] points = new ga_PointInteger[N];
        for (int i = 0; i < N; i++)
            points[i] = pts[i];

        // preprocess so that points[0] has lowest y-coordinate; break ties by x-coordinate
        // points[0] is an extreme point of the convex hull
        // (could do easily in linear time)
        Arrays.sort(points);

        // sort by polar angle with respect to base point points[0],
        // breaking ties by distance to points[0]
        Arrays.sort(points, 1, N, points[0].BY_CCW);

        hull.push(points[0]);       // p[0] is first extreme point

        // find index k1 of first point not equal to points[0]
        int k1;
        for (k1 = 1; k1 < N; k1++)
            if (!points[0].equals(points[k1])) break;
        if (k1 == N) return;        // all points equal

        // find index k2 of first point not collinear with points[0] and points[k1]
        int k2;
        for (k2 = k1 + 1; k2 < N; k2++)
            if (ga_PointInteger.ccw(points[0], points[k1], points[k2]) != 0) break;
        hull.push(points[k2-1]);    // points[k2-1] is second extreme point

        // Graham scan; note that points[N-1] is extreme point different from points[0]
        for (int i = k2; i < N; i++) {
            ga_PointInteger top = hull.pop();
            while (ga_PointInteger.ccw(top, hull.peek(), points[i]) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(points[i]);
        }
    }

    // return extreme points on convex hull in counterclockwise order as an Iterable
    public Iterable<ga_PointInteger> hull() {
        // reverse and discard last point (which is repeated twice)
        Stack<ga_PointInteger> s = new Stack<ga_PointInteger>();
        for (ga_PointInteger p : hull) s.push(p);
        return s;
    }

    // test client
    public static void main(String[] args) {
        int N = f_StdIn.readInt();
        ga_PointInteger[] points = new ga_PointInteger[N];
        for (int i = 0; i < N; i++) {
            int x = f_StdIn.readInt();
            int y = f_StdIn.readInt();
            points[i] = new ga_PointInteger(x, y);
        }
        ga_ConvexHullGraham graham = new ga_ConvexHullGraham(points);
        for (ga_PointInteger p : graham.hull())
            System.out.println(p);
    }

}
