package reuze.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.software.reuze.ga_XYLocation;
import com.software.reuze.ga_XYLocation.Direction;


public class XYLocationTest extends TestCase {

	private int xValue = 1;
	private int yValue = 2;
	private ga_XYLocation defaultLocation = new ga_XYLocation(xValue, yValue);
	
	@Test
	public void testGetXCoOrdinate() {
		assertEquals(xValue, defaultLocation.getXCoOrdinate());
	}

	@Test
	public void testGetYCoOrdinate() {
		assertEquals(yValue, defaultLocation.getYCoOrdinate());
	}

	@Test
	public void testWest() {
		ga_XYLocation expected = new ga_XYLocation(xValue - 1, yValue);
		assertEquals(expected, defaultLocation.west());
	}

	@Test
	public void testEast() {
		ga_XYLocation expected = new ga_XYLocation(xValue + 1, yValue);
		assertEquals(expected, defaultLocation.east());
	}

	@Test
	public void testNorth() {
		ga_XYLocation expected = new ga_XYLocation(xValue, yValue - 1);
		assertEquals(expected, defaultLocation.north());
	}

	@Test
	public void testSouth() {
		ga_XYLocation expected = new ga_XYLocation(xValue, yValue + 1);
		assertEquals(expected, defaultLocation.south());
	}

	@Test
	public void testLeft() {
		ga_XYLocation expected = new ga_XYLocation(xValue - 1, yValue);
		assertEquals(expected, defaultLocation.left());
	}

	@Test
	public void testRight() {
		ga_XYLocation expected = new ga_XYLocation(xValue + 1, yValue);
		assertEquals(expected, defaultLocation.right());
	}

	@Test
	public void testUp() {
		ga_XYLocation expected = new ga_XYLocation(xValue, yValue - 1);
		assertEquals(expected, defaultLocation.up());
	}

	@Test
	public void testDown() {
		ga_XYLocation expected = new ga_XYLocation(xValue, yValue + 1);
		assertEquals(expected, defaultLocation.down());
	}

	@Test
	public void testLocationAt() {
		assertEquals(new ga_XYLocation(xValue - 1, yValue), defaultLocation.locationAt(Direction.West));
		assertEquals(new ga_XYLocation(xValue + 1, yValue), defaultLocation.locationAt(Direction.East));
		assertEquals(new ga_XYLocation(xValue, yValue - 1), defaultLocation.locationAt(Direction.North));
		assertEquals(new ga_XYLocation(xValue, yValue + 1), defaultLocation.locationAt(Direction.South));

	}

}