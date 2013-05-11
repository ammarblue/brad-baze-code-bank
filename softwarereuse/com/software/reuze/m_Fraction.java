package com.software.reuze;
/*
  File: Fraction.java

  Originally written by Doug Lea and released into the public domain.
  This may be used for any purposes whatsoever without acknowledgment.
  Thanks for the assistance and support of Sun Microsystems Labs,
  and everyone contributing, testing, and using this code.

  History:
  Date       Who                What
  7Jul1998  dl               Create public version
  11Oct1999 dl               add hashCode
*/
import java.io.Serializable;
import java.util.Scanner;
/**
 * A  class representing fractions as pairs of ints.
 * Fractions are always maintained in reduced form.
 * Denominator is always positive
 **/
public class m_Fraction extends Number implements Comparable<m_Fraction>, Serializable {
  public int numerator_;
  public int denominator_;
  public static final char INPUT_DELIMITER=' ';
  /** static temporary vector **/
  public final static m_Fraction tmp = new m_Fraction();
  /** static temporary vector **/
  public final static m_Fraction tmp2 = new m_Fraction();
  /** Return the numerator **/
  public int getNumerator() { return numerator_; }

  /** Return the denominator **/
  public int getDenominator() { return denominator_; }

  /** Create a Fraction equal in value to num / den **/
  public m_Fraction(int num, int den) {
    reduce(num, den);
  }
  public m_Fraction() {}
  /** Create a fraction with the same value as Fraction f **/
  public m_Fraction(m_Fraction f) {
    numerator_ = f.numerator_;
    denominator_ = f.denominator_;
  }
  public void set(m_Fraction f) {
	  numerator_ = f.numerator_;
	  denominator_ = f.denominator_;
  }
  public void set(int num, int den) {
	  numerator_ = num;
	  denominator_ = den;
  }
  public String toString() { 
    if (denominator_ == 1) return "" + numerator_;
    else if (numerator_==0) return "0";
    else return numerator_ + "/" + denominator_; 
  }
  public m_Fraction parse(Scanner s) {
	  String in=s.next();
	  int pos=in.indexOf('/');
	  if (pos<0) {
		  numerator_ = Integer.parseInt(in); denominator_ = 1;
		  return this;
	  }
	  int n = Integer.parseInt(in.substring(0,pos));
	  int d = Integer.parseInt(in.substring(pos+1));
	  return reduce(n, d);
  }
  public static final m_Fraction constant(String in) {
	  int pos=in.indexOf('/');
	  if (pos<0) return new m_Fraction(Integer.parseInt(in),1);
	  return new m_Fraction(Integer.parseInt(in.substring(0,pos)), Integer.parseInt(in.substring(pos+1)));
  }
  public m_Fraction reduce(int num, int den) {
	// normalize while constructing
	    if (den==0) throw new ArithmeticException("divide by 0");
	    boolean numNonnegative = (num >= 0);
	    boolean denNonnegative = (den >= 0);
	    int a = numNonnegative? num : -num;
	    int b = denNonnegative? den : -den;
	    int g = gcd(a, b);
	    numerator_ = (numNonnegative == denNonnegative)? (a / g) : (-a / g);
	    denominator_ = b / g;
	    return this;
  }
  /** 
   * Compute the nonnegative greatest common divisor of a and b.
   * (This is needed for normalizing Fractions, but can be
   * useful on its own.)
   **/
  public static int gcd(int a, int b) { 
    int x;
    int y;

    if (a < 0) a = -a;
    if (b < 0) b = -b;

    if (a >= b) { x = a; y = b; }
    else        { x = b; y = a; }

    while (y != 0) {
      int t = x % y;
      x = y;
      y = t;
    }
    return x;
  }

  /** return a Fraction representing the negated value of this Fraction **/
  public m_Fraction negate() {
    numerator_ = -numerator_;
    return this;
  }

  /** return a Fraction representing 1 / this Fraction **/
  public m_Fraction reciprocal() {
	int ad = denominator_;
    if (numerator_<0) {
    	denominator_ = -numerator_;
    	ad = -ad;
    } else denominator_ = numerator_;
    numerator_ = ad;
    return this;
  }


  /** return a Fraction representing this Fraction plus b **/
  public m_Fraction add(m_Fraction b) {
    int an = numerator_;
    int ad = denominator_;
    int bn = b.numerator_;
    int bd = b.denominator_;
    return reduce(an*bd+bn*ad, ad*bd);
  }

  /** return a Fraction representing this Fraction plus n **/
  public m_Fraction add(int n) {
    numerator_ += denominator_*n;
    return this;
  }

  /** return a Fraction representing this Fraction minus b **/
  public m_Fraction subtract(m_Fraction b) {
    int an = numerator_;
    int ad = denominator_;
    int bn = b.numerator_;
    int bd = b.denominator_;
    return reduce(an*bd-bn*ad, ad*bd);
  }

  /** return a Fraction representing this Fraction minus n **/
  public m_Fraction subtract(int n) {
    return add(-n);
  }


  /** return a Fraction representing this Fraction times b **/
  public m_Fraction multiply(m_Fraction b) {
    int an = numerator_;
    int ad = denominator_;
    int bn = b.numerator_;
    int bd = b.denominator_;
    return reduce(an*bn, ad*bd);
  }

  /** return a Fraction representing this Fraction times n **/
  public m_Fraction multiply(int n) {
    return reduce(numerator_ * n, denominator_);
  }

  /** return a Fraction representing this Fraction divided by b **/
  public m_Fraction divide(m_Fraction b) {
    int an = numerator_;
    int ad = denominator_;
    int bn = b.numerator_;
    int bd = b.denominator_;
    return reduce(an*bd, ad*bn);
  }

  /** return a Fraction representing this Fraction divided by n **/
  public m_Fraction divide(int n) {
    return reduce(numerator_, denominator_ * n);
  }

  /** return a number less, equal, or greater than zero
   * reflecting whether this Fraction is less, equal or greater than 
   * the value of Fraction other.
   **/
  public int compareTo(m_Fraction other) {
	if (other==null) return 1;
    m_Fraction b = (m_Fraction)(other);
    int an = numerator_;
    int ad = denominator_;
    int bn = b.numerator_;
    int bd = b.denominator_;
    int l = an*bd;
    int r = bn*ad;
    return (l < r)? -1 : ((l == r)? 0: 1);
  }

  /** return a number less, equal, or greater than zero
   * reflecting whether this Fraction is less, equal or greater than n.
   **/

  public int compareTo(int n) {
    if (numerator_==0) {
    	if (n==0) return 0;
    	return n>0?-1:1;
    }
    float r=(float)numerator_/(float)denominator_;
    if (r==n) return 0;
    return (r < n)? -1 : 1;
  }

  public boolean equals(Object other) {
	if (other==null) return false;
    return compareTo((m_Fraction)other) == 0;
  }

  public boolean equals(int n) {
    return compareTo(n) == 0;
  }

  public int hashCode() {
    return (int) (numerator_ ^ denominator_);
  }

@Override
public double doubleValue() {
	return ((double)(numerator_)) / ((double)(denominator_));
}

@Override
public float floatValue() {
	return ((float)(numerator_)) / ((float)(denominator_));
}

@Override
public int intValue() {
	return numerator_/denominator_;
}

@Override
public long longValue() {
	return numerator_/denominator_;
}

public static void main(String args[]) {
	m_Fraction f=new m_Fraction(1,-2);
	System.out.println(f);
	f.parse(new Scanner("24/21"));
	System.out.println(f);
	System.out.println(constant("-8/4"));
	System.out.println(constant("7"));
	System.out.println(f.add(constant("3/4")).negate().multiply(4).divide(constant("-1/7")));
	System.out.println(constant("4/4"));
	System.out.println(new m_Fraction().parse(new Scanner("-18/91")));
	System.out.println(constant("-8/0"));
}

}
