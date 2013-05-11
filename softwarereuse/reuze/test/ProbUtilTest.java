package reuze.test;
//package aima.test.core.unit.probability.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.mp_DomainFiniteBoolean;
import com.software.reuze.mp_DomainTokensArbitrary;
import com.software.reuze.mp_ProbUtil;
import com.software.reuze.mp_RandomVariable;
import com.software.reuze.mp_i_RandomVariable;


/*import aima.core.probability.RandomVariable;
import aima.core.probability.domain.ArbitraryTokenDomain;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.RandVar;*/

public class ProbUtilTest {

	@Test
	public void test_indexOf() {
		mp_RandomVariable X = new mp_RandomVariable("X", new mp_DomainFiniteBoolean());
		mp_RandomVariable Y = new mp_RandomVariable("Y", new mp_DomainTokensArbitrary("A", "B", "C"));
		mp_RandomVariable Z = new mp_RandomVariable("Z", new mp_DomainFiniteBoolean());

		// An ordered X,Y,Z enumeration of values should look like:
		// 00: true, A, true
		// 01: true, A, false
		// 02: true, B, true
		// 03: true, B, false
		// 04: true, C, true
		// 05: true, C, false
		// 06: false, A, true
		// 07: false, A, false
		// 08: false, B, true
		// 09: false, B, false
		// 10: false, C, true
		// 11: false, C, false
		mp_i_RandomVariable[] vars = new mp_i_RandomVariable[] { X, Y, Z };
		Map<mp_i_RandomVariable, Object> event = new LinkedHashMap<mp_i_RandomVariable, Object>();

		event.put(X, Boolean.TRUE);
		event.put(Y, "A");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(0, mp_ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(1, mp_ProbUtil.indexOf(vars, event));
		event.put(Y, "B");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(2, mp_ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(3, mp_ProbUtil.indexOf(vars, event));
		event.put(Y, "C");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(4, mp_ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(5, mp_ProbUtil.indexOf(vars, event));
		//
		event.put(X, Boolean.FALSE);
		event.put(Y, "A");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(6, mp_ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(7, mp_ProbUtil.indexOf(vars, event));
		event.put(Y, "B");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(8, mp_ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(9, mp_ProbUtil.indexOf(vars, event));
		event.put(Y, "C");
		event.put(Z, Boolean.TRUE);
		Assert.assertEquals(10, mp_ProbUtil.indexOf(vars, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertEquals(11, mp_ProbUtil.indexOf(vars, event));
	}

	@Test
	public void test_indexesOfValue() {
		mp_RandomVariable X = new mp_RandomVariable("X", new mp_DomainFiniteBoolean());
		mp_RandomVariable Y = new mp_RandomVariable("Y", new mp_DomainTokensArbitrary("A", "B", "C"));
		mp_RandomVariable Z = new mp_RandomVariable("Z", new mp_DomainFiniteBoolean());

		// An ordered X,Y,Z enumeration of values should look like:
		// 00: true, A, true
		// 01: true, A, false
		// 02: true, B, true
		// 03: true, B, false
		// 04: true, C, true
		// 05: true, C, false
		// 06: false, A, true
		// 07: false, A, false
		// 08: false, B, true
		// 09: false, B, false
		// 10: false, C, true
		// 11: false, C, false
		mp_i_RandomVariable[] vars = new mp_i_RandomVariable[] { X, Y, Z };
		Map<mp_i_RandomVariable, Object> event = new LinkedHashMap<mp_i_RandomVariable, Object>();

		event.put(X, Boolean.TRUE);
		Assert.assertArrayEquals(new int[] { 0, 1, 2, 3, 4, 5 },
				mp_ProbUtil.indexesOfValue(vars, 0, event));
		event.put(X, Boolean.FALSE);
		Assert.assertArrayEquals(new int[] { 6, 7, 8, 9, 10, 11 },
				mp_ProbUtil.indexesOfValue(vars, 0, event));

		event.put(Y, "A");
		Assert.assertArrayEquals(new int[] { 0, 1, 6, 7 },
				mp_ProbUtil.indexesOfValue(vars, 1, event));
		event.put(Y, "B");
		Assert.assertArrayEquals(new int[] { 2, 3, 8, 9 },
				mp_ProbUtil.indexesOfValue(vars, 1, event));
		event.put(Y, "C");
		Assert.assertArrayEquals(new int[] { 4, 5, 10, 11 },
				mp_ProbUtil.indexesOfValue(vars, 1, event));

		event.put(Z, Boolean.TRUE);
		Assert.assertArrayEquals(new int[] { 0, 2, 4, 6, 8, 10 },
				mp_ProbUtil.indexesOfValue(vars, 2, event));
		event.put(Z, Boolean.FALSE);
		Assert.assertArrayEquals(new int[] { 1, 3, 5, 7, 9, 11 },
				mp_ProbUtil.indexesOfValue(vars, 2, event));
	}
}
