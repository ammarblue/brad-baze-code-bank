package com.software.reuze;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;


public class m_ScaleMapNonLinear {

    public class Sample implements Comparable<Sample> {

        public final double x, y;

        public Sample(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public int compareTo(Sample b) {
            return (int) Math.signum(x - b.x);
        }
    }

    private TreeSet<Sample> samples;

    private double rangeMin = Float.MAX_VALUE;
    private double rangeMax = Float.MIN_VALUE;

    public m_ScaleMapNonLinear() {
        samples = new TreeSet<Sample>();
    }

    public m_ScaleMapNonLinear addSample(double x, double y) {
        samples.add(new Sample(x, y));
        rangeMin = m_MathUtils.min(y, rangeMin);
        rangeMax = m_MathUtils.max(y, rangeMax);
        return this;
    }

    public NavigableSet<Sample> getSamples() {
        return samples;
    }

    public double map(double x) {
        Sample t = new Sample(x, 0);
        SortedSet<Sample> aset = samples.headSet(t);
        SortedSet<Sample> bset = samples.tailSet(t);
        if (aset.isEmpty()) {
            return bset.first().y;
        } else {
            if (bset.isEmpty()) {
                return aset.last().y;
            } else {
                Sample a = aset.last();
                Sample b = bset.first();
                return a.y + (b.y - a.y) * (x - a.x) / (b.x - a.x);
            }
        }
    }

}