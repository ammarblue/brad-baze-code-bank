package com.software.reuze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.software.reuze.ac_Domain;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Section 6.1, Page 202.<br>
 * <br>
 * A constraint satisfaction problem or CSP consists of three components, X, D,
 * and C:
 * <ul>
 * <li>X is a set of variables, {X1, ... ,Xn}.</li>
 * <li>D is a set of domains, {D1, ... ,Dn}, one for each variable.</li>
 * <li>C is a set of constraints that specify allowable combinations of values.</li>
 * </ul>
 * 
 * @author Ruediger Lunde
 */
public class ac_CSP {

	private List<mf_NodeTermVariable> variables;
	private List<ac_Domain> domains;
	private List<ac_i_Constraint> constraints;

	/** Lookup, which maps a variable to its index in the list of variables. */
	private Hashtable<mf_NodeTermVariable, Integer> varIndexHash;
	/**
	 * Constraint network. Maps variables to those constraints in which they
	 * participate.
	 */
	private Hashtable<mf_NodeTermVariable, List<ac_i_Constraint>> cnet;

	private ac_CSP() {
	}

	/** Creates a new CSP for a fixed set of variables. */
	public ac_CSP(List<mf_NodeTermVariable> vars) {
		variables = new ArrayList<mf_NodeTermVariable>(vars.size());
		domains = new ArrayList<ac_Domain>(vars.size());
		constraints = new ArrayList<ac_i_Constraint>();
		varIndexHash = new Hashtable<mf_NodeTermVariable, Integer>();
		cnet = new Hashtable<mf_NodeTermVariable, List<ac_i_Constraint>>();
		ac_Domain emptyDomain = new ac_Domain(new ArrayList<Object>(0));
		int index = 0;
		for (mf_NodeTermVariable var : vars) {
			variables.add(var);
			domains.add(emptyDomain);
			varIndexHash.put(var, index++);
			cnet.put(var, new ArrayList<ac_i_Constraint>());
		}
	}

	public List<mf_NodeTermVariable> getVariables() {
		return Collections.unmodifiableList(variables);
	}

	public int indexOf(mf_NodeTermVariable var) {
		return varIndexHash.get(var);
	}

	public ac_Domain getDomain(mf_NodeTermVariable var) {
		return domains.get(varIndexHash.get(var));
	}

	public void setDomain(mf_NodeTermVariable var, ac_Domain domain) {
		domains.set(indexOf(var), domain);
	}

	/**
	 * Replaces the domain of the specified variable by new domain, which
	 * contains all values of the old domain except the specified value.
	 */
	public void removeValueFromDomain(mf_NodeTermVariable var, Object value) {
		ac_Domain currDomain = getDomain(var);
		List<Object> values = new ArrayList<Object>(currDomain.size());
		for (Object v : currDomain)
			if (!v.equals(value))
				values.add(v);
		setDomain(var, new ac_Domain(values));
	}

	public List<ac_i_Constraint> getConstraints() {
		return constraints;
	}

	/**
	 * Returns all constraints in which the specified variable participates.
	 */
	public List<ac_i_Constraint> getConstraints(mf_NodeTermVariable var) {
		return cnet.get(var);
	}

	public void addConstraint(ac_i_Constraint constraint) {
		constraints.add(constraint);
		for (mf_NodeTermVariable var : constraint.getScope())
			cnet.get(var).add(constraint);
	}

	/**
	 * Returns for binary constraints the other variable from the scope.
	 * 
	 * @return a variable or null for non-binary constraints.
	 */
	public mf_NodeTermVariable getNeighbor(mf_NodeTermVariable var, ac_i_Constraint constraint) {
		List<mf_NodeTermVariable> scope = constraint.getScope();
		if (scope.size() == 2) {
			if (var == scope.get(0))
				return scope.get(1);
			else if (var == scope.get(1))
				return scope.get(0);
		}
		return null;
	}

	/**
	 * Returns a copy which contains a copy of the domains list and is in all
	 * other aspects a flat copy of this.
	 */
	public ac_CSP copyDomains() {
		ac_CSP result = new ac_CSP();
		result.variables = variables;
		result.domains = new ArrayList<ac_Domain>(domains.size());
		result.domains.addAll(domains);
		result.constraints = constraints;
		result.varIndexHash = varIndexHash;
		result.cnet = cnet;
		return result;
	}
}