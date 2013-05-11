package reuze.test;
//package aima.test.core.unit.search.framework;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.aa_TreeSearchNode;


/**
 * @author Ravi Mohan
 * 
 */
public class NodeTest {

	@Test
	public void testRootNode() {
		aa_TreeSearchNode node1 = new aa_TreeSearchNode("state1");
		Assert.assertTrue(node1.isRootNode());
		aa_TreeSearchNode node2 = new aa_TreeSearchNode("state2", node1, null, 1.0);
		Assert.assertTrue(node1.isRootNode());
		Assert.assertFalse(node2.isRootNode());
		Assert.assertEquals(node1, node2.getParent());
	}

	@Test
	public void testGetPathFromRoot() {
		aa_TreeSearchNode node1 = new aa_TreeSearchNode("state1");
		aa_TreeSearchNode node2 = new aa_TreeSearchNode("state2", node1, null, 1.0);
		aa_TreeSearchNode node3 = new aa_TreeSearchNode("state3", node2, null, 1.0);
		List<aa_TreeSearchNode> path = node3.getPathFromRoot();
		Assert.assertEquals(node1, path.get(0));
		Assert.assertEquals(node2, path.get(1));
		Assert.assertEquals(node3, path.get(2));
	}
}
