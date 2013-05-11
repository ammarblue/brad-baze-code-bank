package reuze.test;
import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

import com.software.reuze.m_NoiseSimplex;

public class NoiseSimplexTest extends TestCase {
static m_NoiseSimplex v=new m_NoiseSimplex();

	@Test
	public void testNoiseDoubleDouble() {
		assertEquals(0.23526496123584156,v.noise(1, 2),0);
	}

	@Test
	public void testNoiseDoubleDoubleDouble() {
		assertEquals(0.0,v.noise(1, 2,3),0);
	}

	@Test
	public void testNoiseDoubleDoubleDoubleDouble() {
		assertEquals(0.13508598596204313,v.noise(1, 2,3,4),0);
	}

}
