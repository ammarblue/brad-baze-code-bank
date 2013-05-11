package com.software.reuze;

import java.util.Comparator;
import java.util.List;

import com.software.reuze.aa_i_Action;
import com.software.reuze.d_QueuePriority;


/**
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class das_SearchProblemPriority implements das_i_SearchProblem {
	private final aa_TreeSearchNodeExpanderQueue search;
	private final Comparator<aa_TreeSearchNode> comparator;

	public das_SearchProblemPriority(aa_TreeSearchNodeExpanderQueue search, Comparator<aa_TreeSearchNode> comparator) {
		this.search = search;
		this.comparator = comparator;
		if (search instanceof dag_GraphSearch) {
			((dag_GraphSearch) search)
					.setReplaceFrontierNodeAtStateCostFunction(comparator);
		}
	}
	
	public List<aa_i_Action> search(a_Problem p) throws Exception {
		return search.search(p, new d_QueuePriority<aa_TreeSearchNode>(5, comparator));
	}

	public d_Metrics getMetrics() {
		return search.getMetrics();
	}
}