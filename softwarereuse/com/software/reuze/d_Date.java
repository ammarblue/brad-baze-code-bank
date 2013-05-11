package com.software.reuze;
/*************************************************************************
 *  Compilation:  javac Date.java
 *  Execution:    java Date
 *
 *  An immutable data type for dates.
 *
 *************************************************************************/

public class d_Date implements Comparable<d_Date> {
    private static final int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private final int month;   // month (between 1 and 12)
    private final int day;     // day   (between 1 and DAYS[month]
    private final int year;    // year

    // do bounds-checking to ensure object represents a valid date
    public d_Date(int month, int day, int year) {
        if (!isValid(month, day, year)) throw new RuntimeException("Invalid date");
        this.month = month;
        this.day   = day;
        this.year  = year;
    }

    // create new data by parsing from string of the form mm/dd/yy
    public d_Date(String date) {
        String[] fields = date.split("/");
        if (fields.length != 3) {
            throw new RuntimeException("Date parse error");
        }
        month = Integer.parseInt(fields[0]);
        day   = Integer.parseInt(fields[1]);
        year  = Integer.parseInt(fields[2]);
        if (!isValid(month, day, year)) throw new RuntimeException("Invalid date");
    }

    public int month() { return month; }
    public int day()   { return day;   }
    public int year()  { return year;  }


    // is the given date valid?
    private static boolean isValid(int m, int d, int y) {
        if (m < 1 || m > 12)      return false;
        if (d < 1 || d > DAYS[m]) return false;
        if (m == 2 && d == 29 && !isLeapYear(y)) return false;
        return true;
    }

    // is y a leap year?
    private static boolean isLeapYear(int y) {
        if (y % 400 == 0) return true;
        if (y % 100 == 0) return false;
        return (y % 4 == 0);
    }

    // return the next Date
    public d_Date next() {
        if (isValid(month, day + 1, year))    return new d_Date(month, day + 1, year);
        else if (isValid(month + 1, 1, year)) return new d_Date(month + 1, 1, year);
        else                                  return new d_Date(1, 1, year + 1);
    }


    // is this Date after b?
    public boolean isAfter(d_Date b) {
        return compareTo(b) > 0;
    }

    // is this Date a before b?
    public boolean isBefore(d_Date b) {
        return compareTo(b) < 0;
    }

    // compare this Date to that one
    public int compareTo(d_Date that) {
        if (this.year  < that.year)  return -1;
        if (this.year  > that.year)  return +1;
        if (this.month < that.month) return -1;
        if (this.month > that.month) return +1;
        if (this.day   < that.day)   return -1;
        if (this.day   > that.day)   return +1;
        return 0;
    }

    // return a string representation of this date
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    // is this Date equal to x?
    public boolean equals(Object x) {
        if (x == this) return true;
        if (x == null) return false;
        if (x.getClass() != this.getClass()) return false;
        d_Date that = (d_Date) x;
        return (this.month == that.month) && (this.day == that.day) && (this.year == that.year);
    }



    // sample client for testing
    public static void main(String[] args) {
        d_Date today = new d_Date(2, 25, 2004);
        System.out.println(today);
        for (int i = 0; i < 10; i++) {
            today = today.next();
            System.out.println(today);
        }

        System.out.println(today.isAfter(today.next()));
        System.out.println(today.isAfter(today));
        System.out.println(today.next().isAfter(today));


        d_Date birthday = new d_Date(10, 16, 1971);
        System.out.println(birthday);
        for (int i = 0; i < 10; i++) {
            birthday = birthday.next();
            System.out.println(birthday);
        }
    }

}
