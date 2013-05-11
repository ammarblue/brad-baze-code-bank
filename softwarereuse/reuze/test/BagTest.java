package reuze.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.software.reuze.d_Bag;

/**
 * @author Justin Miller 08/22/2012
 *
 */
public class BagTest extends TestCase {

	@Test
	public void testAdd() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		Assert.assertTrue(biggestDBAG.size()==3);
		biggestDBAG.add(4);
		Assert.assertTrue(biggestDBAG.size()==4);
	}

	@Test
	public void testAddAllObjectArray() {
		d_Bag biggestDBAG = new d_Bag();
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(5);
		numbers.add(10);
		numbers.add(6);		
		biggestDBAG.addAll(numbers);
		Assert.assertTrue(biggestDBAG.containsAll(numbers));
	}

	@Test
	public void testGet() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		biggestDBAG.add(4);
		Assert.assertFalse(biggestDBAG.get(1).equals(5));
		Assert.assertTrue(biggestDBAG.get(0).equals(1));
	}

	@Test
	public void testRemoveInt() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		biggestDBAG.add(4);
		biggestDBAG.remove(0);
		Assert.assertTrue(biggestDBAG.size()==3);
		Assert.assertFalse(biggestDBAG.get(0).equals(1));
	}

	@Test
	public void testRemoveObject() {
		d_Bag biggestDBAG = new d_Bag();
		String s1 = new String("Hello");
		String s2 = new String("this");
		String s3 = new String("is");
		String s4 = new String("a");
		String s5 = new String("test");
		biggestDBAG.add(s1);
		biggestDBAG.add(s2);
		biggestDBAG.add(s3);
		biggestDBAG.add(s4);
		biggestDBAG.add(s5);
		biggestDBAG.remove(s3);
		Assert.assertFalse(biggestDBAG.contains(s3));
		Assert.assertTrue(biggestDBAG.contains(s4));
	}

	@Test
	public void testContains() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		biggestDBAG.add(4);
		Assert.assertTrue(biggestDBAG.contains(3));
	}

	@Test
	public void testIndexOf() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		biggestDBAG.add(4);
		Assert.assertTrue(biggestDBAG.indexOf(3) == 2);
	}

	@Test
	public void testClear() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		biggestDBAG.add(4);
		biggestDBAG.clear();
		Assert.assertTrue(biggestDBAG.isEmpty());
	}

	@Test
	public void testSize() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		biggestDBAG.add(4);
		Assert.assertTrue(biggestDBAG.size() == 4);
	}

	@Test
	public void testToArray() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		biggestDBAG.add(4);
		Assert.assertTrue(biggestDBAG.toArray().getClass().isArray());
	}


	@Test
	public void testRemoveAll() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		biggestDBAG.add(4);
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(2);
		numbers.add(3);
		biggestDBAG.removeAll(numbers);
		Assert.assertFalse(biggestDBAG.contains(2));
		Assert.assertTrue(biggestDBAG.contains(4));
	}

	@Test
	public void testContainsAll() {
		d_Bag biggestDBAG = new d_Bag();
		biggestDBAG.add(1);
		biggestDBAG.add(2);
		biggestDBAG.add(3);
		biggestDBAG.add(4);
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(2);
		numbers.add(3);
		biggestDBAG.removeAll(numbers);
		Assert.assertFalse(biggestDBAG.contains(3));
		Assert.assertFalse(biggestDBAG.contains(2));
		Assert.assertTrue(biggestDBAG.contains(1));
	}

	@Test
	public void testIsEmpty() {
		d_Bag biggestDBAG = new d_Bag();
		Assert.assertTrue(biggestDBAG.isEmpty());
	}

}