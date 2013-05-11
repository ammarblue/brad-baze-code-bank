package reuze.test;
//******************
//Craig Porter
//900506492

import com.software.reuze.ga_Geometry2D;
import com.software.reuze.ga_Vector2D;

import junit.framework.TestCase;

public class Geometry2DTest extends TestCase {
	private ga_Vector2D a;
	private ga_Vector2D b;
	private ga_Vector2D c;
	private ga_Vector2D p;
	
	public void testLineCircle() {
		assertTrue(ga_Geometry2D.line_circle(-2, 2, 2, -2, 0, 0, 1));
		assertFalse(ga_Geometry2D.line_circle(0, 2, 4, -2, 0, 0, 1));
	}
	
	public void testLineLine() {
		assertFalse(ga_Geometry2D.line_line(0, 0, 3, 0, 4, 2, 4, -2));
		assertTrue(ga_Geometry2D.line_line(0, 0, 3, 0, 2, 2, 2, -2));
	}
	
	public void testLineCircleP() {
		double[] result;
		result = ga_Geometry2D.line_circle_p(-2, 2, 2, 2, 0, 0, 1);
		assertEquals(0, result.length);
		result = ga_Geometry2D.line_circle_p(0, 0, 2, 0, 0, 0, 1);
		assertEquals(2, result.length);
		result = ga_Geometry2D.line_circle_p(-2, 0, 2, 0, 0, 0, 1);
		assertEquals(4, result.length);
	}
	
	public void testInsideTriangle() {
		a = new ga_Vector2D(-100, -100);
		b = new ga_Vector2D(0, 100);
		c = new ga_Vector2D(100, -100);
		p = new ga_Vector2D(1, 1);
		boolean bool = ga_Geometry2D.isInsideTriangle(a, b, c, p);
		assertTrue(bool);
		bool = ga_Geometry2D.isInsideTriangle(a.x,a.y, b.x,b.y, c.x,c.y, -99,-99);
		assertTrue(bool);
		bool = ga_Geometry2D.isInsideTriangle(a.x,a.y, b.x,b.y, c.x,c.y, 99,-99.99);
		assertTrue(bool);
	}
	
	public void testTangentsToCircle() {
		double[] result;
		result = ga_Geometry2D.tangents_to_circle(0, 0, 0, 0, 4);
		assertEquals(0, result.length);
		result = ga_Geometry2D.tangents_to_circle(4, 0, 0, 0, 4);
		assertEquals(2, result.length);
		assertTrue(result[0] == 4 && result[1] == 0);
		result = ga_Geometry2D.tangents_to_circle(5, 0, 0, 0, 4);
		assertEquals(4, result.length);
		assertTrue(result[0] == result[2] && result[1] == -result[3]);
	}
	
	
}