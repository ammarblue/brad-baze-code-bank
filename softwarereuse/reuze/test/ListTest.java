package reuze.test;
//package aima.test.core.unit.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.ml_SentenceAtomicSymbol;


//import aima.core.logic.propositional.parsing.ast.Symbol;

/**
 * @author Ravi Mohan
 * 
 */
public class ListTest {

	@Test
	public void testListOfSymbolsClone() {
		ArrayList<ml_SentenceAtomicSymbol> l = new ArrayList<ml_SentenceAtomicSymbol>();
		l.add(new ml_SentenceAtomicSymbol("A"));
		l.add(new ml_SentenceAtomicSymbol("B"));
		l.add(new ml_SentenceAtomicSymbol("C"));
		List<ml_SentenceAtomicSymbol> l2 = new ArrayList<ml_SentenceAtomicSymbol>(l);
		l2.remove(new ml_SentenceAtomicSymbol("B"));
		Assert.assertEquals(3, l.size());
		Assert.assertEquals(2, l2.size());
	}

	@Test
	public void testListRemove() {
		List<Integer> one = new ArrayList<Integer>();
		one.add(new Integer(1));
		Assert.assertEquals(1, one.size());
		one.remove(0);
		Assert.assertEquals(0, one.size());
	}
}
