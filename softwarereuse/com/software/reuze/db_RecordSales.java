package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac Transaction.java
 *  Execution:    java Transaction
 *  
 * Used on p. 79, 91, 119, 266, 311, 337, 340, 462.
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;


public class db_RecordSales implements Comparable<db_RecordSales> {
    private final String  who;      // customer
    private final d_Date    when;     // date
    private final double  amount;   // amount

    public db_RecordSales(String who, d_Date when, double amount) {
        this.who    = who;
        this.when   = when;
        this.amount = amount;
    }

    // create new transaction by parsing string of the form: name,
    // date, real number, separated by whitespace
    public db_RecordSales(String transaction) {
        String[] a = transaction.split("\\s+");
        who    = a[0];
        when   = new d_Date(a[1]);
        amount = Double.parseDouble(a[2]);
    }

    // accessor methods
    public String  who()    { return who;      }
    public d_Date    when()   { return when;     }
    public double  amount() { return amount;   }

    public String toString() {
        return String.format("%-10s %10s %8.2f", who, when, amount);
    }

    public int compareTo(db_RecordSales that) {
        if      (this.amount < that.amount) return -1;
        else if (this.amount > that.amount) return +1;
        else                                return  0;
    }    

    public int hashCode() {
        int hash = 17;
        hash = 31*hash + who.hashCode();
        hash = 31*hash + when.hashCode();
        hash = 31*hash + ((Double) amount).hashCode();
        return hash;
    }

    // ascending order of account number
    public static class WhoOrder implements Comparator<db_RecordSales> {
        public int compare(db_RecordSales v, db_RecordSales w) {
            return v.who.compareTo(w.who);
        }
    }

    // ascending order of time
    public static class WhenOrder implements Comparator<db_RecordSales> {
        public int compare(db_RecordSales v, db_RecordSales w) {
            return v.when.compareTo(w.when);
        }
    }

    // ascending order of ammount
    public static class HowMuchOrder implements Comparator<db_RecordSales> {
        public int compare(db_RecordSales v, db_RecordSales w) {
            if      (v.amount < w.amount) return -1;
            else if (v.amount > w.amount) return +1;
            else                          return  0;
        }
    }


    // test client
    public static void main(String[] args) {
        db_RecordSales[] a = new db_RecordSales[4];
        a[0] = new db_RecordSales("Turing   6/17/1990  644.08");
        a[1] = new db_RecordSales("Tarjan   3/26/2002  4121.85");
        a[2] = new db_RecordSales("Knuth    6/14/1999  288.34");
        a[3] = new db_RecordSales("Dijkstra 8/22/2007  2678.40");

         System.out.println("Unsorted");
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
        System.out.println();
        
        System.out.println("Sort by date");
        Arrays.sort(a, new db_RecordSales.WhenOrder());
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
        System.out.println();

        System.out.println("Sort by customer");
        Arrays.sort(a, new db_RecordSales.WhoOrder());
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
        System.out.println();

        System.out.println("Sort by amount");
        Arrays.sort(a, new db_RecordSales.HowMuchOrder());
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
        System.out.println();
    }

}
