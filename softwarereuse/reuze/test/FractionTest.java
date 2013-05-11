package reuze.test;

// JUnit test for class m_Fraction.java from the reuze package.
// Written by Travis Losser for Artificial Intelligence Fall 2012.
// Date: 8/20/2012
// Assignment P1
//
// Runs 21 different tests to test the results from the calculations and operations for fractions
// being performed by class m_Fraction.java.

import static org.junit.Assert.*;
import junit.framework.TestCase;
import org.junit.Test;

import com.software.reuze.m_Fraction;

public class FractionTest extends TestCase {
		final int NUMERATOR1 = 1;
		final int DENOMINATOR1 = 2;
		
		final int NUMERATOR2 = 21;
		final int DENOMINATOR2 = 4;
		
		final int NUMERATOR3 = 1;
		final int DENOMINATOR3 = 4;
		
		final int NUMERATOR4 = 2;
		final int DENOMINATOR4 = 3;

	@Test
	public void testGetNumerator() {
		m_Fraction fraction1 = new m_Fraction(NUMERATOR1, DENOMINATOR1);
		m_Fraction fraction2 = new m_Fraction(NUMERATOR2, DENOMINATOR2);
		m_Fraction fraction3 = new m_Fraction(NUMERATOR3, DENOMINATOR3);
		m_Fraction fraction4 = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		
		assertEquals("Numerator1", NUMERATOR1, fraction1.getNumerator());
		assertEquals("Numerator2", NUMERATOR2, fraction2.getNumerator());
		assertEquals("Numerator3", NUMERATOR3, fraction3.getNumerator());
		assertEquals("Numerator4", NUMERATOR4, fraction4.getNumerator());
		System.out.println("testGetNumerator");
		System.out.println("----------------");
		System.out.println("Numerators(1, 2, 3, 4) = " + fraction1.getNumerator() + ", " + fraction2.getNumerator() + ", " + fraction3.getNumerator() + ", " + fraction4.getNumerator());
		System.out.println();
	}

	@Test
	public void testGetDenominator() {
		m_Fraction fraction1 = new m_Fraction(NUMERATOR1, DENOMINATOR1);
		m_Fraction fraction2 = new m_Fraction(NUMERATOR2, DENOMINATOR2);
		m_Fraction fraction3 = new m_Fraction(NUMERATOR3, DENOMINATOR3);
		m_Fraction fraction4 = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		
		assertEquals("Denominator1", DENOMINATOR1, fraction1.getDenominator());
		assertEquals("Denominator2", DENOMINATOR2, fraction2.getDenominator());
		assertEquals("Denominator3", DENOMINATOR3, fraction3.getDenominator());
		assertEquals("Denominator4", DENOMINATOR4, fraction4.getDenominator());
		System.out.println("testGetDenominator");
		System.out.println("----------------");
		System.out.println("Denominators(1, 2, 3, 4) = " + fraction1.getDenominator() + ", " + fraction2.getDenominator() + ", " + fraction3.getDenominator() + ", " + fraction4.getDenominator());
		System.out.println();
	}

	@Test
	public void testSetM_Fraction() {
		m_Fraction fraction = new m_Fraction();
		m_Fraction fraction4 = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		
		fraction.set(fraction4);
		int isEqual = fraction.compareTo(fraction4);
		assertEquals("Result", 0, isEqual);
		System.out.println("testSetM_Fraction");
		System.out.println("----------------");
		if(isEqual == 0) {
			System.out.println(fraction.toString() + " = " + fraction4.toString());
		}
		System.out.println();
	}

	@Test
	public void testSetIntInt() {
		m_Fraction fraction = new m_Fraction();
		m_Fraction fraction3 = new m_Fraction(NUMERATOR3, DENOMINATOR3);
		
		fraction.set(NUMERATOR3, DENOMINATOR3);
		int isEqual = fraction.compareTo(fraction3);
		assertEquals("Result", 0, isEqual);
		System.out.println("testSetIntInt");
		System.out.println("----------------");
		if(isEqual == 0) {
			System.out.println(fraction.toString() + " = " + fraction3.toString());
		}
		System.out.println();
	}

	@Test
	public void testReduce() {
		m_Fraction fraction = new m_Fraction();
		m_Fraction f = new m_Fraction(1, 12);
		
		fraction.reduce(10, 120);
		int isEqual = fraction.compareTo(f);
		assertEquals("Result", 0, isEqual);
		System.out.println("testReduce");
		System.out.println("----------------");
		if(isEqual == 0) {
			System.out.println("Reduce Fraction = 10/120");
			System.out.println(fraction.toString() + " = " + f.toString());
		}
		System.out.println();
	}

	@Test
	public void testGcd() {
		int gcd1 = m_Fraction.gcd(42, 56);
		int gcd2 = m_Fraction.gcd(8, 12);
		int gcd3 = m_Fraction.gcd(54, 24);
		
		assertEquals("Result", 14, gcd1);
		assertEquals("Result", 4, gcd2);
		assertEquals("Result", 6, gcd3);
		System.out.println("testGcd");
		System.out.println("--------");
		System.out.println("gcd(42, 56) = " + gcd1);
		System.out.println("gcd(8, 12) = " + gcd2);
		System.out.println("gcd(54, 24) = " + gcd3);
		System.out.println();
	}

	@Test
	public void testNegate() {
		m_Fraction fraction = m_Fraction.constant("37/9");
		m_Fraction fraction2 = m_Fraction.constant("-2/3");
		fraction.negate();
		fraction2.negate();
		assertEquals("Result", "-37/9", fraction.toString());
		assertEquals("Result", "2/3", fraction2.toString());
		System.out.println("testNegate");
		System.out.println("--------");
		System.out.println("Original = 37/9, Negate = " + fraction.toString());
		System.out.println("Original = -2/3, Negate = " + fraction2.toString());
		System.out.println();
	}

	@Test
	public void testReciprocal() {
		m_Fraction fraction = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		fraction.reciprocal();
		assertEquals("Result", "3/2", fraction.toString());
		System.out.println("testReciprocal");
		System.out.println("--------------");
		System.out.println("Original = 2/3, Reciprocal = " + fraction.toString());
		System.out.println();
	}

	@Test
	public void testAddM_Fraction() {
		m_Fraction f1 = new m_Fraction(NUMERATOR1, DENOMINATOR1);
		m_Fraction f2 = new m_Fraction(NUMERATOR2, DENOMINATOR2);
		m_Fraction f3 = new m_Fraction(NUMERATOR3, DENOMINATOR3);
		m_Fraction f4 = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		
		m_Fraction fraction = f1.add(f2.add(f3.add(f4)));
		assertEquals("Result", "20/3", fraction.toString());
		System.out.println("testAddM_Fraction");
		System.out.println("-----------------");
		System.out.println("1/2 + 21/4 + 1/4 + 2/3 = " + fraction.toString());
		System.out.println();
	}

	@Test
	public void testAddInt() {
		m_Fraction f4 = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		int value = 5;
		m_Fraction fraction = f4.add(value);
		assertEquals("Result", "17/3", fraction.toString());
		System.out.println("testAddInt");
		System.out.println("-----------------");
		System.out.println("2/3 + 5 = " + fraction.toString());
		System.out.println();
	}

	@Test
	public void testSubtractM_Fraction() {
		m_Fraction f1 = new m_Fraction(NUMERATOR1, DENOMINATOR1);
		m_Fraction f2 = new m_Fraction(NUMERATOR2, DENOMINATOR2);
		
		m_Fraction fraction = f1.subtract(f2);
		assertEquals("Result", "-19/4", fraction.toString());
		System.out.println("testSubtractM_Fraction");
		System.out.println("-----------------");
		System.out.println("1/2 - 21/4 = " + fraction.toString());
		System.out.println();
	}

	@Test
	public void testSubtractInt() {
		m_Fraction f2 = new m_Fraction(NUMERATOR2, DENOMINATOR2);
		
		m_Fraction fraction = f2.subtract(5);
		assertEquals("Result", "1/4", fraction.toString());
		System.out.println("testSubtractInt");
		System.out.println("-----------------");
		System.out.println("21/4 - 5 = " + fraction.toString());
		System.out.println();
	}

	@Test
	public void testMultiplyM_Fraction() {
		m_Fraction f1 = new m_Fraction(NUMERATOR1, DENOMINATOR1);
		m_Fraction f2 = new m_Fraction(NUMERATOR2, DENOMINATOR2);
		m_Fraction f3 = new m_Fraction(NUMERATOR3, DENOMINATOR3);
		m_Fraction f4 = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		
		m_Fraction fraction = f1.multiply(f2.multiply(f3.multiply(f4)));
		assertEquals("Result", "7/16", fraction.toString());
		System.out.println("testMultiplyM_Fraction");
		System.out.println("-----------------");
		System.out.println("1/2 * 21/4 * 1/4 * 2/3 = " + fraction.toString());
		System.out.println();
	}

	@Test
	public void testMultiplyInt() {
		m_Fraction f1 = new m_Fraction(NUMERATOR1, DENOMINATOR1);
		int value = 9;
		
		m_Fraction fraction = f1.multiply(value);
		assertEquals("Result", "9/2", fraction.toString());
		System.out.println("testMultiplyInt");
		System.out.println("-----------------");
		System.out.println("1/2 * 9 = " + fraction.toString());
		System.out.println();
	}

	@Test
	public void testDivideM_Fraction() {
		m_Fraction f1 = new m_Fraction(NUMERATOR1, DENOMINATOR1);
		m_Fraction f4 = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		
		m_Fraction fraction = f4.divide(f1);
		assertEquals("Result", "4/3", fraction.toString());
		System.out.println("testDivideM_Fraction");
		System.out.println("-----------------");
		System.out.println("(2/3) / (1/2) = " + fraction.toString());
		System.out.println();
	}

	@Test
	public void testDivideInt() {
		m_Fraction f4 = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		int value = 10;
		
		m_Fraction fraction = f4.divide(value);
		assertEquals("Result16", "1/15", fraction.toString());
		System.out.println("testDivideInt");
		System.out.println("-----------------");
		System.out.println("(2/3) / 10 = " + fraction.toString());
		System.out.println();
	}

	@Test
	public void testCompareToM_Fraction() {
		m_Fraction f1 = new m_Fraction(NUMERATOR1, DENOMINATOR1);
		m_Fraction f2 = new m_Fraction(2, 4);
		m_Fraction f3 = new m_Fraction(NUMERATOR3, DENOMINATOR3);
		m_Fraction f4 = new m_Fraction(NUMERATOR4, DENOMINATOR4);
		
		int isLessThan = f1.compareTo(f4);
		int isGreaterThan = f1.compareTo(f3);
		int isEqual = f1.compareTo(f2);
		
		assertEquals("Result", -1, isLessThan);
		assertEquals("Result", 1, isGreaterThan);
		assertEquals("Result", 0, isEqual);
	}

	@Test
	public void testCompareToInt() {
		m_Fraction f1 = new m_Fraction(NUMERATOR1, DENOMINATOR1);
		m_Fraction f2 = new m_Fraction(NUMERATOR2, DENOMINATOR2);
		m_Fraction f3 = new m_Fraction(15, 5);
		int value = 3;
		
		int isLessThan = f1.compareTo(value);
		int isGreaterThan = f2.compareTo(value);
		int isEqual = f3.compareTo(value);
		assertTrue(f1.compareTo(0) != 0);
		assertEquals("Result", -1, isLessThan);
		assertEquals("Result", 1, isGreaterThan);
		assertEquals("Result", 0, isEqual);
		m_Fraction test = new m_Fraction(24,-48);
		f1 = new m_Fraction(1, -2);
		assertTrue(f1.compareTo(test) == 0);
	}

	@Test
	public void testEqualsObject() {
		m_Fraction f1 = new m_Fraction(2, 3);
		m_Fraction f2 = new m_Fraction(2, 3);
		
		boolean isEqual = f1.equals(f2);
		assertEquals("Result", true, isEqual);
	}

	@Test
	public void testEqualsInt() {
		m_Fraction f1 = new m_Fraction(15, 5);
		int value = 3;
		
		boolean isEqual = f1.equals(value);
		assertEquals("Result", true, isEqual);
	}
	
	@Test
	public void testDivideByZero() {
		try {
		m_Fraction f1 = new m_Fraction(4, 0);
		} catch (ArithmeticException e) {assertTrue(true); return; }
		assertTrue(false);
	}
}
