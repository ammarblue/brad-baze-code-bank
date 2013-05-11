package com.software.reuze;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.software.reuze.aa_ActionDynamicNoOp;
import com.software.reuze.aa_a_Agent;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_Percept;


/**
 * @author Ravi Mohan
 * 
 */
public class aa_AgentSearch extends aa_a_Agent {
	protected List<aa_i_Action> actionList;

	private Iterator<aa_i_Action> actionIterator;

	private d_Metrics searchMetrics;

	public aa_AgentSearch(a_Problem p, das_i_SearchProblem search) throws Exception {
		actionList = search.search(p);
		actionIterator = actionList.iterator();
		searchMetrics = search.getMetrics();
	}

	@Override
	public aa_i_Action execute(aa_i_Percept p) {
		if (actionIterator.hasNext()) {
			return actionIterator.next();
		} else {
			return aa_ActionDynamicNoOp.NO_OP;
		}
	}

	public boolean isDone() {
		return !actionIterator.hasNext();
	}

	public List<aa_i_Action> getActions() {
		return actionList;
	}

	public Properties getInstrumentation() {
		Properties retVal = new Properties();
		Iterator<String> iter = searchMetrics.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = searchMetrics.get(key);
			retVal.setProperty(key, value);
		}
		return retVal;
	}
}