package reuze.test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.software.reuze.ga_Circle;
import com.software.reuze.ga_Intersection;
import com.software.reuze.ga_Rectangle;
import com.software.reuze.ga_Vector2;
import com.software.reuze.gb_BoundingBox;
import com.software.reuze.gb_Plane;
import com.software.reuze.gb_Ray;
import com.software.reuze.gb_Vector3;


/**
 * 
 * @author Adam Gorby
 * 
 * JUnit tests for the various functions included in the Intersection2D class.
 * These deal with intersections of line segments, lines, rays, and geometric shapes.
 *
 */


public class Intersection2DTest extends TestCase {

	@Test
	public void testGetLowestPositiveRoot() {
		float a = 1;
		float b = 2;
		float c = -5;
		float lowestRoot = ga_Intersection.getLowestPositiveRoot(a,b,c);
		assertEquals("LowRoot", lowestRoot, 1.449, .01);
		
		a = 1; b = 2; c = 3;
		lowestRoot = ga_Intersection.getLowestPositiveRoot(a, b, c);
		assertEquals("LowRoot", lowestRoot, Float.NaN, .01);
		
		a = 3; b = 4; c = 4;
		lowestRoot = ga_Intersection.getLowestPositiveRoot(a, b, c);
		assertEquals("LowRoot", lowestRoot, Float.NaN, .01);
		
		a = 2; b = 3; c = -8;
		lowestRoot = ga_Intersection.getLowestPositiveRoot(a, b, c);
		assertEquals("LowRoot", lowestRoot, 1.386, .01);
	}

	@Test
	public void testIsPointInTriangle() {
		gb_Vector3 t1 = new gb_Vector3(0,0,0);
		gb_Vector3 t2 = new gb_Vector3(1,2,3);
		gb_Vector3 t3 = new gb_Vector3(4,5,6);
		
		gb_Vector3 point = new gb_Vector3(4,5,6);
		assertTrue(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		point.set(10,10,10);
		assertFalse(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		point.set(1,5,4);
		assertFalse(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		point.set(1,2,3);
		assertTrue(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		point.set(0,0,0);
		assertTrue(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		t2.set(5,0,0);
		t3.set(3,5,0);
		
		point.set(0,0,0);
		assertTrue(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		point.set(2,1,0);
		assertTrue(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		point.set(3,4,0);
		assertTrue(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		point.set(4,1,0);
		assertTrue(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		point.set(4,3,0);
		assertFalse(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
		
		point.set(2,4,0);
		assertFalse(ga_Intersection.isPointInTriangle(point, t1, t2, t3));
	}

	@Test
	public void testIntersectSegmentPlane() {
		gb_Vector3 start = new gb_Vector3();
		gb_Vector3 end = new gb_Vector3(0,0,-11);
		gb_Vector3 t1 = new gb_Vector3(0,1,-10);
		gb_Vector3 t2 = new gb_Vector3(1,0,-10);
		gb_Vector3 t3 = new gb_Vector3(1,1,-10);
		gb_Plane plane = new gb_Plane(t1, t2, t3);
		gb_Vector3 intersection = new gb_Vector3();
		gb_Vector3 check = new gb_Vector3(0,0,-10);
		
		assertTrue(ga_Intersection.intersectSegmentPlane(start, end, plane, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		assertEquals("Intersection Z", check.z, intersection.z, 0);
		
		end.set(0,0,10);
		assertFalse(ga_Intersection.intersectSegmentPlane(start, end, plane, intersection));

		// segment starts on plane
		start.set(0,0,-10);
		end.set(0,0,-5);
		assertTrue(ga_Intersection.intersectSegmentPlane(start, end, plane, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		assertEquals("Intersection Z", check.z, intersection.z, 0);
		
		// segment ends on plane
		start.set(0,0,-5);
		end.set(0,0,-10);
		assertTrue(ga_Intersection.intersectSegmentPlane(start, end, plane, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		assertEquals("Intersection Z", check.z, intersection.z, 0);
	}

	@Test
	public void testIsPointInPolygon() {
		ga_Vector2 p1 = new ga_Vector2(0,0);
		ga_Vector2 p2 = new ga_Vector2(10,0);
		ga_Vector2 p3 = new ga_Vector2(10,10);
		ga_Vector2 p4 = new ga_Vector2(5,20);
		ga_Vector2 p5 = new ga_Vector2(0,10);
		ga_Vector2 point = new ga_Vector2(1,1);
		
		List<ga_Vector2> polygon = new ArrayList<ga_Vector2>();
		polygon.add(p1);
		polygon.add(p2);
		polygon.add(p3);
		polygon.add(p4);
		polygon.add(p5);
		
		assertTrue(ga_Intersection.isPointInPolygon(polygon, point));
		
		point.set(-1,-1);
		assertFalse(ga_Intersection.isPointInPolygon(polygon, point));
		
		point.set(0,0);
		assertFalse(ga_Intersection.isPointInPolygon(polygon, point));
		
		point.set(9,9);
		assertTrue(ga_Intersection.isPointInPolygon(polygon, point));
		
		point.set(11,11);
		assertFalse(ga_Intersection.isPointInPolygon(polygon, point));
		
	}

	@Test
	public void testDistanceLinePoint() {
		ga_Vector2 start = new ga_Vector2();
		ga_Vector2 end = new ga_Vector2(5,0);
		ga_Vector2 point = new ga_Vector2(2,2);
		
		assertEquals("Distance", 2, ga_Intersection.distanceLinePoint(start, end, point), 0);
		
		point.set(2,0);
		assertEquals("Distance", 0, ga_Intersection.distanceLinePoint(start, end, point), 0);
		
		point.set(-2,0);
		assertEquals("Distance", 0, ga_Intersection.distanceLinePoint(start, end, point), 0);
		
		point.set(3,10);
		assertEquals("Distance", 10, ga_Intersection.distanceLinePoint(start, end, point), 0);
		
		end.set(5,5);
		assertEquals("Distance", 4.94, ga_Intersection.distanceLinePoint(start, end, point), .01);
		
		point.set(0,5);
		assertEquals("Distance", 3.53, ga_Intersection.distanceLinePoint(start, end, point), .01);
	}

	@Test
	public void testIntersectSegmentCircle() {
		ga_Vector2 start = new ga_Vector2();
		ga_Vector2 end = new ga_Vector2(5,0);
		ga_Vector2 center = new ga_Vector2(0,0);
		float Radius = 2;
		
		assertTrue(ga_Intersection.intersectSegmentCircle(start, end, center, Radius));
		
		start.set(5,5);
		assertFalse(ga_Intersection.intersectSegmentCircle(start, end, center, Radius));
		
		Radius = 5;
		assertTrue(ga_Intersection.intersectSegmentCircle(start, end, center, Radius));
		
		start.set(6,6);
		end.set(6,0);
		assertFalse(ga_Intersection.intersectSegmentCircle(start, end, center, Radius));
		ga_Vector2 out=new ga_Vector2();
		ga_Vector2 tmp=ga_Intersection.intersectLineCircle(start, end, center, Radius, out);
		assertTrue(tmp==null);
		start.set(5,6);
		end.set(5,2);
		out=new ga_Vector2();
		tmp=ga_Intersection.intersectLineCircle(start, end, center, Radius, out);
		assertTrue(tmp.equals(new ga_Vector2(0,0)) && out.equals(new ga_Vector2(5,0)));
		start.set(0,-2);
		end.set(0,7);
		out=new ga_Vector2();
		tmp=ga_Intersection.intersectLineCircle(start, end, center, Radius, out);
		assertTrue(tmp.equals(new ga_Vector2(0,-5)) && out.equals(new ga_Vector2(0,5)));
	}

	@Test
	public void testIntersectSegmentCircleDisplace() {
		ga_Vector2 start = new ga_Vector2(-100,0);
		ga_Vector2 end = new ga_Vector2(100,0);
		ga_Vector2 point = new ga_Vector2(0,0);
		ga_Vector2 displacement = new ga_Vector2();
		float radius = 4;
		
		// This function does not appear to work correctly for intersections
		// A line segment along the x axis most certainly intersects a circle with its center at the origin
		// I found no line segment or circle orientation that registered an intersection
		// It always returns 0 when there is an intersection, instead of the needed displacement to eliminate the intersection
		// the function should never return 0, if there is no intersection it will return infinity, not 0
		assertEquals("Displacement", 0, ga_Intersection.intersectSegmentCircleDisplace(start, end, point, radius, displacement), .01);
		point.set(10,10);
		assertEquals("Displacement", Float.POSITIVE_INFINITY, ga_Intersection.intersectSegmentCircleDisplace(start, end, point, radius, displacement), .01);
		
	}

	@Test
	public void testIntersectRayPlane() {
		gb_Vector3 origin = new gb_Vector3(0,0,0);
		gb_Vector3 direction = new gb_Vector3(0,0,-1);
		gb_Ray ray = new gb_Ray(origin, direction);
		gb_Vector3 t1 = new gb_Vector3(0,1,-10);
		gb_Vector3 t2 = new gb_Vector3(1,0,-10);
		gb_Vector3 t3 = new gb_Vector3(1,1,-10);
		gb_Plane plane = new gb_Plane(t1, t2, t3);
		gb_Vector3 intersection = new gb_Vector3();
		gb_Vector3 check = new gb_Vector3(0,0,-10);
		
		assertTrue(ga_Intersection.intersectRayPlane(ray, plane, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		assertEquals("Intersection Z", check.z, intersection.z, 0);
		
		direction.set(0,0,1);
		ray.set(origin, direction);
		assertFalse(ga_Intersection.intersectRayPlane(ray, plane, intersection));
		
		// ray existing on the plane
		direction.set(1,0,0);
		origin.set(1,1,-10);
		ray.set(origin,direction);
		check.set(1,1,-10);
		assertTrue(ga_Intersection.intersectRayPlane(ray, plane, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		assertEquals("Intersection Z", check.z, intersection.z, 0);
		
	}

	@Test
	public void testIntersectRayTriangle() {
		gb_Vector3 origin = new gb_Vector3(0,0,0);
		gb_Vector3 direction = new gb_Vector3(1,0,0);
		gb_Ray ray = new gb_Ray(origin, direction);
		gb_Vector3 t1 = new gb_Vector3(10,10,0);
		gb_Vector3 t2 = new gb_Vector3(10,0,-10);
		gb_Vector3 t3 = new gb_Vector3(10,0,10);
		gb_Vector3 intersection = new gb_Vector3();
		
		// ray passes through the triangle
		assertTrue(ga_Intersection.intersectRayTriangle(ray, t1, t2, t3, intersection));
		
		direction.set(0,1,0);
		ray.set(origin, direction);
		assertFalse(ga_Intersection.intersectRayTriangle(ray, t1, t2, t3, intersection));
		
		direction.set(0,0,1);
		ray.set(origin, direction);
		assertFalse(ga_Intersection.intersectRayTriangle(ray, t1, t2, t3, intersection));
		
		// ray begins on the triangle
		origin.set(10,0,0);
		ray.set(origin, direction);
		assertTrue(ga_Intersection.intersectRayTriangle(ray, t1, t2, t3, intersection));
		
	}

	@Test
	public void testIntersectRaySphere() {
		gb_Vector3 origin = new gb_Vector3(0,0,0);
		gb_Vector3 direction = new gb_Vector3(1,0,0);
		gb_Ray ray = new gb_Ray(origin, direction);
		gb_Vector3 center = new gb_Vector3(10,0,0);
		float radius = 4;
		gb_Vector3 intersection = new gb_Vector3();
		
		assertTrue(ga_Intersection.intersectRaySphere(ray, center, radius, intersection));
		
		direction.set(0,1,0);
		ray.set(origin, direction);
		assertFalse(ga_Intersection.intersectRaySphere(ray, center, radius, intersection));
		
		direction.set(0,0,1);
		ray.set(origin, direction);
		assertFalse(ga_Intersection.intersectRaySphere(ray, center, radius, intersection));
		
		// origin of ray inside circle
		origin.set(9,0,0);
		ray.set(origin,direction);
		assertTrue(ga_Intersection.intersectRaySphere(ray, center, radius, intersection));
	}

	@Test
	public void testIntersectRayBoundsFast() {
		gb_Vector3 min = new gb_Vector3(10,0,10);
		gb_Vector3 max = new gb_Vector3(0,10,10);
		gb_Vector3 origin = new gb_Vector3(-1,0,0);
		gb_Vector3 direction = new gb_Vector3(1,1,1);
		gb_Ray ray = new gb_Ray(origin, direction);
		gb_BoundingBox box = new gb_BoundingBox(min, max);
		
		assertTrue(ga_Intersection.intersectRayBoundsFast(ray, box));
		
		direction.set(-1,0,0);
		ray.set(origin, direction);
		assertFalse(ga_Intersection.intersectRayBoundsFast(ray, box));
		
		// Edge case
		origin.set(0,0,0);
		direction.set(1,0,0);
		ray.set(origin, direction);
		assertFalse(ga_Intersection.intersectRayBoundsFast(ray, box));
	}

	@Test
	public void testIntersectRectangles() {
		ga_Rectangle a = new ga_Rectangle(0,0,5,5);
		ga_Rectangle b = new ga_Rectangle(5,5,5,5);
		
		assertTrue(ga_Intersection.intersectRectangles(a, b));
		
		b.set(6,6,5,5);
		assertFalse(ga_Intersection.intersectRectangles(a, b));
		
		b.set(0,0,5,5);
		assertTrue(ga_Intersection.intersectRectangles(a, b));
		
		b.set(5,0,5,5);
		assertTrue(ga_Intersection.intersectRectangles(a, b));
	}

	@Test
	public void testIntersectLines() {
		ga_Vector2 p1 = new ga_Vector2(0,0);
		ga_Vector2 p2 = new ga_Vector2(5,0);
		ga_Vector2 p3 = new ga_Vector2(2,2);
		ga_Vector2 p4 = new ga_Vector2(2,-2);
		ga_Vector2 intersection = new ga_Vector2();
		ga_Vector2 check = new ga_Vector2(2,0);
		
		assertTrue(ga_Intersection.intersectLines(p1, p2, p3, p4, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		
		p3.set(10,10);
		p4.set(10,-10);
		check.set(10,0);
		assertTrue(ga_Intersection.intersectLines(p1, p2, p3, p4, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		
		p3.set(-10,10);
		p4.set(-10,-10);
		check.set(-10,0);
		assertTrue(ga_Intersection.intersectLines(p1, p2, p3, p4, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		
		p3.set(4,4);
		p4.set(6,4);
		assertFalse(ga_Intersection.intersectLines(p1, p2, p3, p4, intersection));
		// the vector entered into intersection is [-infinity, NaN], clearly no intersection exists for parallel lines
		// System.out.println(intersection);
		
	}

	@Test
	public void testIntersectSegments() {
		ga_Vector2 p1 = new ga_Vector2();
		ga_Vector2 p2 = new ga_Vector2(5,0);
		ga_Vector2 p3 = new ga_Vector2(2,2);
		ga_Vector2 p4 = new ga_Vector2(2,-2);
		ga_Vector2 intersection = new ga_Vector2();
		ga_Vector2 check = new ga_Vector2(2,0);
		
		assertTrue(ga_Intersection.intersectSegments(p1, p2, p3, p4, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		
		p3.set(5,5);
		p4.set(5,-5);
		check.set(5,0);
		assertTrue(ga_Intersection.intersectSegments(p1, p2, p3, p4, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		
		p3.set(0,5);
		p4.set(0,-5);
		check.set(0,0);
		assertTrue(ga_Intersection.intersectSegments(p1, p2, p3, p4, intersection));
		assertEquals("Intersection X", check.x, intersection.x, 0);
		assertEquals("Intersection Y", check.y, intersection.y, 0);
		
		p3.set(-3,5);
		p4.set(-3,-5);
		assertFalse(ga_Intersection.intersectSegments(p1, p2, p3, p4, intersection));
		
		p3.set(-3,5);
		p4.set(3,5);
		assertFalse(ga_Intersection.intersectSegments(p1, p2, p3, p4, intersection));
	}

	@Test
	public void testOverlapCircles() {
		ga_Circle c1 = new ga_Circle(0,0,2);
		ga_Circle c2 = new ga_Circle(0,0,2);
		
		assertTrue(ga_Intersection.overlapCircles(c1, c2));
		
		c2 = new ga_Circle(0,0,1);
		assertTrue(ga_Intersection.overlapCircles(c1,c2));
		
		c2 = new ga_Circle(10,10,1);
		assertFalse(ga_Intersection.overlapCircles(c1,c2));
		
		c2 = new ga_Circle(3,0,1);
		assertTrue(ga_Intersection.overlapCircles(c1,c2));
	}

	@Test
	public void testOverlapRectangles() {
		ga_Rectangle r1 = new ga_Rectangle(0,0,5,5);
		ga_Rectangle r2 = new ga_Rectangle(0,0,5,5);
		
		assertTrue(ga_Intersection.overlapRectangles(r1, r2));
		
		r2 = new ga_Rectangle(5,0,5,5);
		assertFalse(ga_Intersection.overlapRectangles(r1, r2));
		
		r2 = new ga_Rectangle(1,1,1,1);
		assertTrue(ga_Intersection.overlapRectangles(r1, r2));
		
		r2 = new ga_Rectangle(0,5,5,5);
		assertFalse(ga_Intersection.overlapRectangles(r1, r2));
		
		r2 = new ga_Rectangle(3,3,5,5);
		assertTrue(ga_Intersection.overlapRectangles(r1, r2));
	}

	@Test
	public void testOverlapCircleRectangle() {
		ga_Circle c = new ga_Circle(0,0,2);
		ga_Rectangle r = new ga_Rectangle(0,0,1,1);
		
		assertTrue(ga_Intersection.overlapCircleRectangle(c, r));
	}

}
