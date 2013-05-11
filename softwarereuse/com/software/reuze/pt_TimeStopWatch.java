package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac Stopwatch.java
 *
 *
 *************************************************************************/

/**
 *  <i>Stopwatch</i>. This class is a data type for measuring
 *  the running time (wall clock) of a program.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/32class">Section 3.2</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 */



public class pt_TimeStopWatch { 
    private final long start;

   /**
     * Create a stopwatch object.
     */
    public pt_TimeStopWatch() {
        start = System.currentTimeMillis();
    } 

   /**
     * Return elapsed time (in seconds) since this object was created.
     */
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }
    public String toString() { return ""+elapsedTime(); }

}
