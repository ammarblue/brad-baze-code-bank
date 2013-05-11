package reuze.test;
//package aima.test.core.unit.search.uninformed;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.software.reuze.aa_EnvironmentMap;
import com.software.reuze.aa_SimpleProblemSolvingMap;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Agent;
import com.software.reuze.aa_i_EnvironmentState;
import com.software.reuze.aa_i_EnvironmentView;
import com.software.reuze.d_MapExtendable;
import com.software.reuze.das_Bidirectional;


/**
 * @author Ciaran O'Reilly
 * 
 */
public class BidirectionalSearchTest {

	StringBuffer envChanges;

	das_Bidirectional bidirectionalSearch;

	@Before
	public void setUp() {

		envChanges = new StringBuffer();

		bidirectionalSearch = new das_Bidirectional();
	}

	//
	// Test IG(A)
	@Test
	public void test_A_StartingAtGoal() {
		d_MapExtendable aMap = new d_MapExtendable();

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "A" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(A):aa_i_Action[name==NoOp]:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=2:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test IG(A)<->(B)<->(C)
	@Test
	public void test_ABC_StartingAtGoal() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "A" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(A):aa_i_Action[name==NoOp]:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=2:METRIC[nodesExpanded]=2:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<->G(B)
	@Test
	public void test_AB_BothWaysPath() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "B" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(B):aa_i_Action[name==moveTo, location==B]:METRIC[pathCost]=5.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=2:METRIC[nodesExpanded]=2:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<->(B)<->G(C)
	@Test
	public void test_ABC_BothWaysPath() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "C" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(C):aa_i_Action[name==moveTo, location==B]:aa_i_Action[name==moveTo, location==C]:METRIC[pathCost]=10.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=2:METRIC[nodesExpanded]=4:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<->(B)<->(C)<->(D)
	@Test
	public void test_ABCD_BothWaysPath() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "D" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(D):aa_i_Action[name==moveTo, location==B]:aa_i_Action[name==moveTo, location==C]:aa_i_Action[name==moveTo, location==D]:METRIC[pathCost]=15.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=2:METRIC[nodesExpanded]=4:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)->G(B)
	@Test
	public void test_AB_OriginalOnlyPath() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addUnidirectionalLink("A", "B", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "B" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(B):aa_i_Action[name==moveTo, location==B]:METRIC[pathCost]=5.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=2:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)->(B)->G(C)
	@Test
	public void test_ABC_OriginalOnlyPath() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addUnidirectionalLink("A", "B", 5.0);
		aMap.addUnidirectionalLink("B", "C", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "C" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(C):aa_i_Action[name==moveTo, location==B]:aa_i_Action[name==moveTo, location==C]:METRIC[pathCost]=10.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=4:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)->(B)->(C)<->(D)<->G(E)
	@Test
	public void test_ABCDE_OriginalOnlyPath() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addUnidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "E" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(E):aa_i_Action[name==moveTo, location==B]:aa_i_Action[name==moveTo, location==C]:aa_i_Action[name==moveTo, location==D]:aa_i_Action[name==moveTo, location==E]:METRIC[pathCost]=20.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=1:METRIC[nodesExpanded]=6:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<-G(B)
	@Test
	public void test_AB_ReverseOnlyPath() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addUnidirectionalLink("B", "A", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "B" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(B):aa_i_Action[name==NoOp]:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=3:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(das_Bidirectional.SearchOutcome.PATH_NOT_FOUND,
				bidirectionalSearch.getSearchOutcome());
	}

	//
	// Test I(A)<-(B)<-G(C)
	@Test
	public void test_ABC_ReverseOnlyPath() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addUnidirectionalLink("B", "A", 5.0);
		aMap.addUnidirectionalLink("C", "B", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "C" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(C):aa_i_Action[name==NoOp]:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=4:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(das_Bidirectional.SearchOutcome.PATH_NOT_FOUND,
				bidirectionalSearch.getSearchOutcome());
	}

	// Test I(A)<->(B)<->(C)<-(D)<-G(E)
	@Test
	public void test_ABCDE_ReverseOnlyPath() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addUnidirectionalLink("D", "C", 5.0);
		aMap.addUnidirectionalLink("E", "D", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "E" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(E):aa_i_Action[name==NoOp]:METRIC[pathCost]=0.0:METRIC[maxQueueSize]=2:METRIC[queueSize]=0:METRIC[nodesExpanded]=8:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(das_Bidirectional.SearchOutcome.PATH_NOT_FOUND,
				bidirectionalSearch.getSearchOutcome());
	}

	/**
	 * <code>
	 * Test I(A)<->(B)<->(C)<->(D)<->(E)<->(F)<->(G)<->G(H)
	 *              |                                    +
	 *              --------------------------------------
	 * </code>
	 */
	@Test
	public void test_ABCDEF_OriginalFirst() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);
		aMap.addBidirectionalLink("E", "F", 5.0);
		aMap.addBidirectionalLink("F", "G", 5.0);
		aMap.addBidirectionalLink("G", "H", 5.0);
		aMap.addUnidirectionalLink("B", "H", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "H" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(H):aa_i_Action[name==moveTo, location==B]:aa_i_Action[name==moveTo, location==H]:METRIC[pathCost]=10.0:METRIC[maxQueueSize]=3:METRIC[queueSize]=3:METRIC[nodesExpanded]=8:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_FROM_ORIGINAL_PROBLEM,
				bidirectionalSearch.getSearchOutcome());
	}

	/**
	 * <code>
	 * Test I(A)<->(B)<->(C)<->(D)<->(E)<->G(F)
	 *        +                       |
	 *        -------------------------
	 * </code>
	 */
	@Test
	public void test_ABCDEF_ReverseFirstButNotFromOriginal() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);
		aMap.addBidirectionalLink("E", "F", 5.0);
		aMap.addUnidirectionalLink("E", "A", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "F" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(F):aa_i_Action[name==moveTo, location==B]:aa_i_Action[name==moveTo, location==C]:aa_i_Action[name==moveTo, location==D]:aa_i_Action[name==moveTo, location==E]:aa_i_Action[name==moveTo, location==F]:METRIC[pathCost]=25.0:METRIC[maxQueueSize]=3:METRIC[queueSize]=3:METRIC[nodesExpanded]=8:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	/**
	 * <code>
	 *                          -------------
	 *                          +           +
	 * Test I(A)<->(B)<->(C)<->(D)<->(E)<-G(F)
	 *        +                       +
	 *        -------------------------
	 * </code>
	 */
	@Test
	public void test_ABCDEF_MoreComplexReverseFirstButNotFromOriginal() {
		d_MapExtendable aMap = new d_MapExtendable();
		aMap.addBidirectionalLink("A", "B", 5.0);
		aMap.addBidirectionalLink("B", "C", 5.0);
		aMap.addBidirectionalLink("C", "D", 5.0);
		aMap.addBidirectionalLink("D", "E", 5.0);
		aMap.addUnidirectionalLink("F", "E", 5.0);
		aMap.addBidirectionalLink("E", "A", 5.0);
		aMap.addBidirectionalLink("D", "F", 5.0);

		aa_EnvironmentMap me = new aa_EnvironmentMap(aMap);
		aa_SimpleProblemSolvingMap ma = new aa_SimpleProblemSolvingMap(me.getMap(), me, bidirectionalSearch,
				new String[] { "F" });
		me.addAgent(ma, "A");
		me.addEnvironmentView(new BDSEnvironmentView());
		me.stepUntilDone();

		Assert.assertEquals(
				"CurrentLocation=In(A), Goal=In(F):aa_i_Action[name==moveTo, location==B]:aa_i_Action[name==moveTo, location==C]:aa_i_Action[name==moveTo, location==D]:aa_i_Action[name==moveTo, location==F]:METRIC[pathCost]=20.0:METRIC[maxQueueSize]=4:METRIC[queueSize]=3:METRIC[nodesExpanded]=8:aa_i_Action[name==NoOp]:",
				envChanges.toString());

		Assert.assertEquals(
				das_Bidirectional.SearchOutcome.PATH_FOUND_BETWEEN_PROBLEMS,
				bidirectionalSearch.getSearchOutcome());
	}

	class BDSEnvironmentView implements aa_i_EnvironmentView {
		public void notify(String msg) {
			envChanges.append(msg).append(":");
		}

		public void agentAdded(aa_i_Agent agent, aa_i_EnvironmentState state) {
			// Nothing.
		}

		public void agentActed(aa_i_Agent agent, aa_i_Action action,
				aa_i_EnvironmentState state) {
			envChanges.append(action).append(":");
		}
	}
}
