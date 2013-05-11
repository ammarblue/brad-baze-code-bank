package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac Interval2D.java
 *  Execution:    java Interval2D
 *  
 *  2-dimensional interval data type.
 *
 *************************************************************************/

public class m_Interval2D {
    private final m_Interval1D x;
    private final m_Interval1D y;

    public m_Interval2D(m_Interval1D x, m_Interval1D y) {
        this.x = x;
        this.y = y;
    }
    public m_Interval1D getX() { return x; }
    public m_Interval1D getY() { return y; }

    // does this interval intersect that one?
    public boolean intersects(m_Interval2D that) {
        if (!this.x.intersects(that.x)) return false;
        if (!this.y.intersects(that.y)) return false;
        return true;
    }
    public m_Interval2D intersect(m_Interval2D that) {
      return new m_Interval2D(x.intersect(that.x),
                             y.intersect(that.y));
    }

    public boolean isEmpty() { return x.isEmpty()||y.isEmpty(); }

    public boolean contains(m_Interval2D p) {
        return x.contains(p.x)  && y.contains(p.y);
    }
    public boolean contains(double xx, double yy) {
        return x.contains(xx)  && y.contains(yy);
    }

    // area of this interval
    public double area() {
        return x.length() * y.length();
    }
        
    public String toString() {
        return x + " x " + y;
    }



    // test client
    public static void main(String[] args) {
        m_Interval1D interval1 = new m_Interval1D(15.0, 33.0);
        m_Interval1D interval2 = new m_Interval1D(45.0, 60.0);
        m_Interval2D interval = new m_Interval2D(interval1, interval2);
        System.out.println(interval);
    }
}
