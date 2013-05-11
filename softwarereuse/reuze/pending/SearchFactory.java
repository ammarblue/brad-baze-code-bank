package reuze.pending;
import com.software.reuze.aa_TreeSearch;
import com.software.reuze.aa_TreeSearchBestFirstSearchRecursive;
import com.software.reuze.aa_TreeSearchNodeExpanderQueue;
import com.software.reuze.dag_GraphSearch;
import com.software.reuze.das_SearchProblemBreadthFirst;
import com.software.reuze.das_SearchProblemDepthFirst;
import com.software.reuze.das_SearchProblemHillClimbing;
import com.software.reuze.das_SearchProblemIterativeDeepening;
import com.software.reuze.das_SearchProblemPriorityUniformCost;
import com.software.reuze.das_TreeAstar;
import com.software.reuze.das_TreeAstarEvaluationFunction;
import com.software.reuze.das_TreeBestFirstGreedy;
import com.software.reuze.das_i_SearchProblem;
import com.software.reuze.m_i_HeuristicFunction;

/**
 * Useful factory for configuring search objects. Implemented as a singleton.
 * @author Ruediger Lunde
 */
public class SearchFactory {

	/** Search strategy: Depth first search. */
	public final static int DF_SEARCH = 0;
	/** Search strategy: Depth first search. */
	public final static int BF_SEARCH = 1;
	/** Search strategy: Iterative deepening search. */
	public final static int ID_SEARCH = 2;
	/** Search strategy: Uniform cost search. */
	public final static int UC_SEARCH = 3;
	/** Search strategy: Greedy best first search. */
	public final static int GBF_SEARCH = 4;
	/** Search strategy: A* search. */
	public final static int ASTAR_SEARCH = 5;
	/** Search strategy: Recursive best first search. */
	public final static int RBF_SEARCH = 6;
	/** Search strategy: Hill climbing search. */
	public final static int HILL_SEARCH = 7;

	/** Search mode: tree search. */
	public final static int TREE_SEARCH = 0;
	/** Search mode: graph search. */
	public final static int GRAPH_SEARCH = 1;

	/** Contains the only existing instance. */
	private static SearchFactory instance;

	/** Invisible constructor. */
	private SearchFactory() {
	};

	/** Provides access to the factory. Implemented with lazy instantiation. */
	public static SearchFactory getInstance() {
		if (instance == null)
			instance = new SearchFactory();
		return instance;
	}

	/**
	 * Returns the names of all search strategies, which are supported by this
	 * factory. The indices correspond to the parameter values of method
	 * {@link #createSearch(int, int, m_i_HeuristicFunction)}.
	 */
	public String[] getSearchStrategyNames() {
		return new String[] { "Depth First", "Breadth First",
				"Iterative Deepening", "Uniform Cost", "Greedy Best First",
				"A*", "Recursive Best First", "Hill Climbing" };
	}

	/**
	 * Returns the names of all search modes, which are supported by this
	 * factory. The indices correspond to the parameter values of method
	 * {@link #createSearch(int, int, m_i_HeuristicFunction)}.
	 */
	public String[] getSearchModeNames() {
		return new String[] { "Tree Search", "Graph Search" };
	}

	/**
	 * Creates a search instance.
	 * 
	 * @param strategy
	 *            search strategy. See static constants.
	 * @param mode
	 *            search mode: {@link #TREE_SEARCH} or {@link #GRAPH_SEARCH}
	 * 
	 */
	public das_i_SearchProblem createSearch(int strategy, int mode, m_i_HeuristicFunction hf) {
		aa_TreeSearchNodeExpanderQueue qs = null;
		das_i_SearchProblem result = null;
		switch (mode) {
		case TREE_SEARCH:
			qs = new aa_TreeSearch();
			break;
		case GRAPH_SEARCH:
			qs = new dag_GraphSearch();
		}
		switch (strategy) {
		case DF_SEARCH:
			result = new das_SearchProblemDepthFirst(qs);
			break;
		case BF_SEARCH:
			result = new das_SearchProblemBreadthFirst(qs);
			break;
		case ID_SEARCH:
			result = new das_SearchProblemIterativeDeepening();
			break;
		case UC_SEARCH:
			result = new das_SearchProblemPriorityUniformCost(qs);
			break;
		case GBF_SEARCH:
			result = new das_TreeBestFirstGreedy(qs, hf);
			break;
		case ASTAR_SEARCH:
			result = new das_TreeAstar(qs, hf);
			break;
		case RBF_SEARCH:
			result = new aa_TreeSearchBestFirstSearchRecursive(new das_TreeAstarEvaluationFunction(
					hf));
			break;
		case HILL_SEARCH:
			result = new das_SearchProblemHillClimbing(hf);
			break;
		}
		return result;
	}
}
