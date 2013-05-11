package reuze.test;

import static org.junit.Assert.*;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.software.reuze.d_Date;


public class DateTest extends TestCase {

	@Test
	public void testIntegerConstructor() {
		d_Date date = new d_Date(10, 23, 1985);
		Assert.assertNotNull(date);
		Assert.assertEquals(date.month(), 10);
		Assert.assertEquals(date.day(), 23);
		Assert.assertEquals(date.year(), 1985);
	}

	@Test
	public void testStringConstructor() {
		d_Date date = new d_Date("10/23/1985");
		Assert.assertNotNull(date);
		Assert.assertEquals(date.month(), 10);
		Assert.assertEquals(date.day(), 23);
		Assert.assertEquals(date.year(), 1985);
	}
	
	@Test
	public void testParseException() {
		try {
		d_Date date = new d_Date("garbage");
		}catch (RuntimeException e) {return;}
		assertTrue(false);
	}
	
	@Test
	public void testValidationException() {
		try {
		d_Date date = new d_Date(23, 10, 1985); // Not a valid calendar date.
		}catch (RuntimeException e) {return;}
		assertTrue(false);
	}
	
	@Test
	public void testComparers() {
		d_Date earlier = new d_Date("10/23/1985");
		d_Date later = new d_Date("10/24/1985");
		Assert.assertTrue(earlier.isBefore(later));
		Assert.assertTrue(later.isAfter(earlier));
		
		d_Date eqEarlier = new d_Date("10/23/1985");
		Assert.assertEquals(eqEarlier,  earlier);
	}
}