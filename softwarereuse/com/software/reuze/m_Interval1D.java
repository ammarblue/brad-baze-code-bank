package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac Interval1D.java
 *  Execution:    java Interval1D
 *  
 *  1-dimensional numeric interval data type.
 *
 *************************************************************************/

public class m_Interval1D<TYPE extends Number> {
    private final TYPE left;
    private final TYPE right;

    public m_Interval1D(TYPE left, TYPE right) {
        if (left.doubleValue() <= right.doubleValue()) {
            this.left  = left;
            this.right = right;
        }
        else throw new RuntimeException("Illegal interval "+left+" "+right);
    }

    public m_Interval1D(m_Interval1D x) {
      left=(TYPE)x.left;   right=(TYPE)x.right;
    }

    public TYPE getLeft() { return left; }
    public TYPE getRight() { return right; }

    // does this interval intersect that one?
    public boolean intersects(m_Interval1D that) {
        if (this.right.doubleValue() < that.left.doubleValue()) return false;
        if (that.right.doubleValue() < this.left.doubleValue()) return false;
        return true;
    }

    public m_Interval1D intersect(m_Interval1D that) {
      double r=right.doubleValue(), l1=that.left.doubleValue();
      if (r < l1) return new m_Interval1D(left,left);
      double r1=that.right.doubleValue(), l=left.doubleValue();
      if (r1 < l) return new m_Interval1D(left,left);
      return new m_Interval1D( (l>l1)?left:that.left,(r<r1)?right:that.right);
    }

    // does this interval contain x?
    public boolean contains(double x) {
        return (right.doubleValue() >= x) && (x >= left.doubleValue());
    }
    public boolean contains(m_Interval1D x) {
        return (right.doubleValue() >= x.right.doubleValue()) &&
               (x.left.doubleValue() >= left.doubleValue());
    }
    public boolean isEmpty() { return (left.doubleValue() == right.doubleValue()); }

    // length of this interval
    public double length() {
        return right.doubleValue() - left.doubleValue();
    }
        
    public String toString() {
        return "[" + left + ", " + right + "]";
    }

    // test client
    public static void main(String[] args) {
        m_Interval1D[] intervals = new m_Interval1D[4];
        intervals[0] = new m_Interval1D<Integer>(15, 33);
        intervals[1] = new m_Interval1D(45.0, 60.0);
        intervals[2] = new m_Interval1D(20.0, 70.0);
        intervals[3] = new m_Interval1D(49.0, 65.0);

        for (int i = 0; i < intervals.length; i++)
            System.out.println(intervals[i]);
        System.out.println(intervals[3].length());
        System.out.println(intervals[3].intersect(intervals[1]));
    }
}
