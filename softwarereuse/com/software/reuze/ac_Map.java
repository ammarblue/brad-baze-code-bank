package com.software.reuze;

import java.util.ArrayList;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.1, Page 204.<br>
 * <br>
 * The principal states and territories of Australia. Coloring this map can be
 * viewed as a constraint satisfaction problem (CSP). The goal is to assign
 * colors to each region so that no neighboring regions have the same color.
 * 
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class ac_Map extends ac_CSP {
	public static final mf_NodeTermVariable NSW = new mf_NodeTermVariable("NSW");
	public static final mf_NodeTermVariable NT = new mf_NodeTermVariable("NT");
	public static final mf_NodeTermVariable Q = new mf_NodeTermVariable("Q");
	public static final mf_NodeTermVariable SA = new mf_NodeTermVariable("SA");
	public static final mf_NodeTermVariable T = new mf_NodeTermVariable("T");
	public static final mf_NodeTermVariable V = new mf_NodeTermVariable("V");
	public static final mf_NodeTermVariable WA = new mf_NodeTermVariable("WA");
	public static final String RED = "RED";
	public static final String GREEN = "GREEN";
	public static final String BLUE = "BLUE";

	/**
	 * Returns the principle states and territories of Australia as a list of
	 * variables.
	 * 
	 * @return the principle states and territories of Australia as a list of
	 *         variables.
	 */
	private static List<mf_NodeTermVariable> collectVariables() {
		List<mf_NodeTermVariable> variables = new ArrayList<mf_NodeTermVariable>();
		variables.add(NSW);
		variables.add(WA);
		variables.add(NT);
		variables.add(Q);
		variables.add(SA);
		variables.add(V);
		variables.add(T);
		return variables;
	}

	/**
	 * Constructs a map CSP for the principal states and territories of
	 * Australia, with the colors Red, Green, and Blue.
	 */
	public ac_Map() {
		super(collectVariables());

		ac_Domain colors = new ac_Domain(new Object[] { RED, GREEN, BLUE });

		for (mf_NodeTermVariable var : getVariables())
			setDomain(var, colors);

		addConstraint(new ac_ConstraintNotEqual(WA, NT));
		addConstraint(new ac_ConstraintNotEqual(WA, SA));
		addConstraint(new ac_ConstraintNotEqual(NT, SA));
		addConstraint(new ac_ConstraintNotEqual(NT, Q));
		addConstraint(new ac_ConstraintNotEqual(SA, Q));
		addConstraint(new ac_ConstraintNotEqual(SA, NSW));
		addConstraint(new ac_ConstraintNotEqual(SA, V));
		addConstraint(new ac_ConstraintNotEqual(Q, NSW));
		addConstraint(new ac_ConstraintNotEqual(NSW, V));
	}
}