package reuze.test;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

import com.software.reuze.m_Complex;

public class ComplexTest extends TestCase {

	@Test
	public void testHashCode() {
		m_Complex a=new m_Complex(5, 4);
		
		int a1 = Float.floatToRawIntBits(3);
		int a2 = Float.floatToRawIntBits(4);
		assert((a1 ^ a2) == a.hashCode());
	}//testHashCode

	@Test
	public void testIntValue() {
		m_Complex a=new m_Complex(5, 4);
		assert(5 == a.intValue());
	}//testIntValue

	@Test
	public void testLongValue() {
		m_Complex a=new m_Complex(5, 4);
		assert(4.0 == a.longValue());
	}//testLongValue

	@Test
	public void testFloatValue() {
		m_Complex a=new m_Complex(5, 4);
		assert(5 == a.intValue());
	}//testFloatValue

	@Test
	public void testDoubleValue() {
		m_Complex a=new m_Complex(5, 4);
		assert(5 == a.intValue());
	}//testDoubleValue

	@Test
	public void testPolar() {
		m_Complex a=new m_Complex(5, 4);
		float x = (float)StrictMath.sqrt(41);
		float y = (float)StrictMath.atan2(4,5);
		a.polar();
		assert(a.getReal() == x && a.getImaginary() == y);
	}//testPolar

	@Test
	public void testCartesian() {
		m_Complex a=new m_Complex(5, 4);
		float x = 5 * (float)StrictMath.cos(4);
		float y = 5 * (float)StrictMath.sin(4);
		a.cartesian();
		assert(a.getReal() == x && a.getImaginary() == y);
	}//testCartesian

	@Test
	public void testMagnitude() {
		m_Complex a=new m_Complex(5, 4);
		
		float x = (float)StrictMath.sqrt(41);
		assert(x == a.magnitude());
	}//testMagnitude

	@Test
	public void testArgument() {
		m_Complex a=new m_Complex(5, 4);
		float x = (float)StrictMath.atan2(4,5);
		assert(x == a.argument());
	}//testArgument

	@Test
	public void testPhase() {
		m_Complex a=new m_Complex(5, 4);
		float x = (float)StrictMath.atan2(4, 5);
		assert(x == a.phase());
	}//testPhase

	@Test
	public void testAddM_Complex() {
		m_Complex a = new m_Complex(5, 4);
		m_Complex b = new m_Complex(5, 4);
		m_Complex result = new m_Complex(10, 8);
		assert(result.equals(a.add(b)));
	}//testAddM_Complex

	@Test
	public void testAddFloat() {
		m_Complex a = new m_Complex(5, 4);
		m_Complex result = new m_Complex(4, 4);
		assert(result.equals(a.add(4)));
	}//testAddFloat

	@Test
	public void testSubtractM_Complex() {
		m_Complex a = new m_Complex(5, 4);
		m_Complex b = new m_Complex(3,2);
		m_Complex result = new m_Complex(2, 2);
		assert(result.equals(a.subtract(b)));
	}//testSubtractM_Complex

	@Test
	public void testSubtractFloat() {
		m_Complex a = new m_Complex(5, 4);
		m_Complex result = new m_Complex(4, 4);
		assert(result.equals(a.subtract(1)));
	}//testSubtractFloat

	@Test
	public void testNegate() {
		m_Complex a = new m_Complex(5, 4);
		m_Complex b = new m_Complex(0,0);
		assert(b.equals(a.negate()));
	}//testNegate

	@Test
	public void testMultiplyM_Complex() {
		m_Complex a = new m_Complex(5, 4);
		m_Complex b = new m_Complex(3, 2);
		m_Complex result = new m_Complex(7, 22);
		
		assert(result == a.multiply(b));
	}//testMultiplyM_Complex

	@Test
	public void testMultiplyFloat() {
		m_Complex a = new m_Complex(5, 4);
		m_Complex result = new m_Complex(40, 32);
		
		assert(result == a.multiply(8));
	}//testMultiplyFloat

	@Test
	public void testDivideM_Complex() {
		m_Complex a = new m_Complex(5, 4);
		m_Complex b = new m_Complex(3, 2);
		m_Complex result = new m_Complex((23/13), (2/13));
		
		assert(result == a.divide(b));
	}//testDivideM_Complex

	@Test
	public void testDivideFloat() {
		m_Complex a = new m_Complex(10, 14);
		m_Complex result = new m_Complex(5, 7);
		
		assert(result == a.divide(8));
	}//testDivideFloat

	@Test
	public void testReciprocal() {
		m_Complex a = new m_Complex(5,4);
		m_Complex b = new m_Complex((5/41),(4/41));
		assert(b.equals(a.reciprocal()));
	}//testReciprocal

	@Test
	public void testConjugate() {
		m_Complex a = new m_Complex(5,4);
		m_Complex b = new m_Complex(5, -4);
		assert(b.equals(a));
	}//testConjugate

	@Test
	public void testAbs() {
		m_Complex a = new m_Complex(5,4);
		assert(a.abs() == 41);
	}//testAbs

	@Test
	public void testEqualsObject() {
		m_Complex a = new m_Complex(5,4);
		m_Complex b = new m_Complex(5,4);
		assert(a.equals(b));
	}//testEqualsObject

	@Test
	public void testCompareTo() {
		m_Complex a = new m_Complex(5,4);
		m_Complex b = new m_Complex(4,3);
		m_Complex c = new m_Complex(6,5);
		m_Complex d = new m_Complex(5,18);
		
		assert(a.compareTo(a) == 0 && a.compareTo(b) == 1 && a.compareTo(c) == -1 && a.compareTo(d) == 0);
	}//testCompareTo

	@Test
	public void testConstant() {
		m_Complex a = new m_Complex(5,4);
		m_Complex b = a.constant("5r+4i");
		assert(a.equals(b));
	}//testConstant

	@Test
	public void testExp() {
		m_Complex a = new m_Complex(5,4);
		
		float exp_x=(float)StrictMath.exp(5);
		float x = exp_x * (float)StrictMath.cos(4);
		float y = exp_x * (float)StrictMath.sin(4);
		
		m_Complex b = new m_Complex(x,y);
		
		assert(a.equals(b));
	}//testExp

	@Test
	public void testLog() {
		m_Complex a = new m_Complex(4,3);
		float r = 5.0f;
		float y = (float)StrictMath.atan2(3,4);
		float x = (float)StrictMath.log(r);
		m_Complex b = new m_Complex(x,y);
		assert(a.equals(b));
	}//testLog

	@Test
	public void testSqrt() {
		m_Complex a = new m_Complex(4,3);
		float r = 5.0f;
		float x = (float)StrictMath.sqrt(4.5f);
		float y =(float)StrictMath.sqrt(4.0f);
		m_Complex b = new m_Complex(x,y);
		assert(a.equals(b));
	}//testSqrt

	@Test
	public void testPowM_Complex() {
		m_Complex a = new m_Complex(4,3);
		float r = 5.0f;
		float y = (float)StrictMath.atan2(3,4);
		float x = (float)StrictMath.log(r);
		
		x = (x * 2) - (y * 3);
		y = (x * 3) + (y * 2);
		
		float exp_x = (float)StrictMath.exp(x);
		x = exp_x*(float)StrictMath.cos(y);
		y = exp_x*(float)StrictMath.sin(y);
		
		m_Complex b = new m_Complex(x,y);
		m_Complex z = new m_Complex(2,3);
		
		assert(a.pow(z).equals(b));
	}//testPowM_Complex

	@Test
	public void testPowFloat() {
		m_Complex a = new m_Complex(4,3);
		float r = 5.0f;
		float y = (float)StrictMath.atan2(3,4);
		float x = (float)StrictMath.log(r);
		
		x = x * 4;
		y = y * 4;
		
		float exp_x = (float)StrictMath.exp(x);
		x = exp_x*(float)StrictMath.cos(y);
		y = exp_x*(float)StrictMath.sin(y);
		
		m_Complex b = new m_Complex(x,y);
		
		assert(a.pow(4.0f).equals(b));
	}//testPowFloat

	@Test
	public void testSin() {
		m_Complex a = new m_Complex(4,3);
		
		float x = (float)StrictMath.sin(4) * (float)StrictMath.cosh(3);
		float y = (float)StrictMath.cos(x) * (float)StrictMath.sinh(3);
		
		m_Complex b = new m_Complex(x,y);
		assert(a.equals(b));
	}//testSin

	@Test
	public void testCos() {
		m_Complex a = new m_Complex(4,3);
		
		float x = (float)StrictMath.cos(4) * (float)StrictMath.cosh(3);
		float y = (float)-StrictMath.sin(x) * (float)StrictMath.sinh(3);
		
		m_Complex b = new m_Complex(x,y);
		assert(a.equals(b));
	}//testCos

	@Test
	public void testTan() {
		m_Complex a = new m_Complex(4,3);
		
		m_Complex b = a.sin().divide(a.cos());
		assert(a.tan().equals(b));
	}//testTan

	@Test
	public void testAtan() {
		m_Complex a = new m_Complex(4,3);
		m_Complex b = new m_Complex(4,3);
		
		m_Complex temp = new m_Complex(4,2);
		m_Complex temp2 = new m_Complex(-4,-2);
		m_Complex x = new m_Complex(0,-1);
		
		a.set(x);
		a.multiply(temp.divide(temp2).log()).divide(2.0f);
		
		assert(a.equals(b.atan()));
	}//testAtan

	@Test
	public void testAsin() {
		m_Complex a = new m_Complex(4,3);
		m_Complex b = new m_Complex(4,3);
		m_Complex c = new m_Complex(4,3);
		m_Complex d = new m_Complex(0,-1);
		m_Complex e = new m_Complex(1.0f,0.0f);
		m_Complex f = new m_Complex(0,1);
		
		c.multiply(d);
		c.add(e.subtract(b.multiply(b)).sqrt());
		
		b.set(c.getReal(), c.getImaginary());
		
		b.log().multiply(f);
		
		assert(a.asin().equals(b));
	}//testAsin

	@Test
	public void testAcos() {
		m_Complex a = new m_Complex(4,3);
		m_Complex b = new m_Complex(4,3);
		m_Complex c = new m_Complex(4,3);
		m_Complex d = new m_Complex(0,-1);
		m_Complex f = new m_Complex(0,1);
		
		c.add((new m_Complex(1.0f,0.0f)).subtract(b.multiply(b)).sqrt().multiply(d));
		b.set(c.getReal(), c.getImaginary());
		b.log().multiply(f);
	}//testAcos

	@Test
	public void testSinh() {
		m_Complex a = new m_Complex(4,3);
		
		float x = (float)StrictMath.sinh(4) * (float)StrictMath.cos(3);
		float y = (float)StrictMath.cosh(x) * (float)StrictMath.sin(3);
		
		m_Complex b = new m_Complex(x,y);
		
		assert(a.sinh().equals(b));
	}//testSinh

	@Test
	public void testCosh() {
		m_Complex a = new m_Complex(4,3);
		
		float x = (float)StrictMath.cosh(4) * (float)StrictMath.cos(3);
		float y = (float)StrictMath.sinh(x) * (float)StrictMath.sin(3);
		
		m_Complex b = new m_Complex(x,y);
		
		assert(a.cosh().equals(b));
	}//testCosh

	@Test
	public void testTanh() {
		m_Complex a = new m_Complex(4,3);
		m_Complex b = new m_Complex(4,3);
		m_Complex c = new m_Complex(4,3);
		
		b.sin().divide(c.cosh());
		
		assert(a.tanh().equals(b));
	}//testTanh

	@Test
	public void testAtanh() {
		m_Complex a = new m_Complex(4,3);
		m_Complex b = new m_Complex(4,3);
		m_Complex c = new m_Complex(4,3);
		
		b.add(1.0f).log().subtract(c.subtract(1.0f).negate().log()).divide(2.0f);
		
		assert(a.atanh().equals(b));
	}//testAtanh

}//class ComplexTest
