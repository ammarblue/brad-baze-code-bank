package reuze.test;

import static org.junit.Assert.*;
import junit.framework.TestCase;
import org.junit.Test;

import com.software.reuze.ga_CellArray;

public class CellArrayTest extends TestCase {
	
	ga_CellArray myCellArray = new ga_CellArray(5.0f,5.0f);

	@Test
	public void testGetCellWidthAndHeight() {
		assertEquals(5.0f,myCellArray.getCellWidthAndHeight(),.0001);
	}

	@Test
	public void testGetHeight() {
		assertEquals(5.0f,myCellArray.getHeight(),.0001);
	}

	@Test
	public void testGetNumCols() {
		assertEquals(1,myCellArray.getNumCols());
	}

	@Test
	public void testGetNumRows() {
		assertEquals(1,myCellArray.getNumRows());
	}

	@Test
	public void testGetWidth() {
		assertEquals(5.0f,myCellArray.getWidth(),.0001);
	}

}