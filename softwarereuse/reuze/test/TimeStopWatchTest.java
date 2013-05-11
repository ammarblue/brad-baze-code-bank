package reuze.test;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.pt_TimeStopWatch;

public class TimeStopWatchTest extends TestCase {
	
	pt_TimeStopWatch tsw;

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testPt_TimeStopWatch() {
		tsw = new pt_TimeStopWatch();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assert(tsw != null);
	}

	@Test
	public void testElapsedTime() {
		assert(tsw.elapsedTime() > 1);
	}

	@Test
	public void testToString() {
		
		assert(tsw.toString() instanceof String);
		
	}

}