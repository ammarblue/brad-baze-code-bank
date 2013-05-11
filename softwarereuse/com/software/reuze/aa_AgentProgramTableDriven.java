package com.software.reuze;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.software.reuze.aa_ActionDynamicNoOp;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_AgentProgram;
import com.software.reuze.aa_i_Percept;
import com.software.reuze.d_Table;


/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.7, page 47.<br>
 * <br>
 * 
 * <pre>
 * function TABLE-DRIVEN-AGENT(percept) returns an action
 *   persistent: percepts, a sequence, initially empty
 *               table, a table of actions, indexed by percept sequences, initially fully specified
 *           
 *   append percept to end of percepts
 *   action <- LOOKUP(percepts, table)
 *   return action
 * </pre>
 * 
 * Figure 2.7 The TABLE-DRIVEN-AGENT program is invoked for each new percept and
 * returns an action each time. It retains the complete percept sequence in
 * memory.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * 
 */
public class aa_AgentProgramTableDriven implements aa_i_AgentProgram {
	private List<aa_i_Percept> percepts = new ArrayList<aa_i_Percept>();

	private d_Table<List<aa_i_Percept>, String, aa_i_Action> table;

	private static final String ACTION = "action";

	// persistent: percepts, a sequence, initially empty
	// table, a table of actions, indexed by percept sequences, initially fully
	// specified
	/**
	 * Constructs a TableDrivenAgentProgram with a table of actions, indexed by
	 * percept sequences.
	 * 
	 * @param perceptSequenceActions
	 *            a table of actions, indexed by percept sequences
	 */
	public aa_AgentProgramTableDriven(
			Map<List<aa_i_Percept>, aa_i_Action> perceptSequenceActions) {

		List<List<aa_i_Percept>> rowHeaders = new ArrayList<List<aa_i_Percept>>(
				perceptSequenceActions.keySet());

		List<String> colHeaders = new ArrayList<String>();
		colHeaders.add(ACTION);

		table = new d_Table<List<aa_i_Percept>, String, aa_i_Action>(rowHeaders, colHeaders);

		for (List<aa_i_Percept> row : rowHeaders) {
			table.set(row, ACTION, perceptSequenceActions.get(row));
		}
	}

	//
	// START-AgentProgram

	// function TABLE-DRIVEN-AGENT(percept) returns an action
	public aa_i_Action execute(aa_i_Percept percept) {
		// append percept to end of percepts
		percepts.add(percept);

		// action <- LOOKUP(percepts, table)
		// return action
		return lookupCurrentAction();
	}

	// END-AgentProgram
	//

	//
	// PRIVATE METHODS
	//
	private aa_i_Action lookupCurrentAction() {
		aa_i_Action action = null;

		action = table.get(percepts, ACTION);
		if (null == action) {
			action = aa_ActionDynamicNoOp.NO_OP;
		}

		return action;
	}
}
