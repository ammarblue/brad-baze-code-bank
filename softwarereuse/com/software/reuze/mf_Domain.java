package com.software.reuze;
//package aima.core.logic.fol.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class mf_Domain {
	private Set<String> constants, functions, predicates;
	private int skolemConstantIndexical = 0;
	private int skolemFunctionIndexical = 0;
	private int answerLiteralIndexical = 0;
	private List<mf_i_DomainListener> listeners = new ArrayList<mf_i_DomainListener>();

	public mf_Domain() {
		this.constants = new HashSet<String>();
		this.functions = new HashSet<String>();
		this.predicates = new HashSet<String>();
	}

	public mf_Domain(mf_Domain toCopy) {
		this(toCopy.getConstants(), toCopy.getFunctions(), toCopy
				.getPredicates());
	}

	public mf_Domain(Set<String> constants, Set<String> functions,
			Set<String> predicates) {
		this.constants = new HashSet<String>(constants);
		this.functions = new HashSet<String>(functions);
		this.predicates = new HashSet<String>(predicates);
	}

	public Set<String> getConstants() {
		return constants;
	}

	public Set<String> getFunctions() {
		return functions;
	}

	public Set<String> getPredicates() {
		return predicates;
	}

	public void addConstant(String constant) {
		constants.add(constant);
	}

	public String addSkolemConstant() {

		String sc = null;
		do {
			sc = "SC" + (skolemConstantIndexical++);
		} while (constants.contains(sc) || functions.contains(sc)
				|| predicates.contains(sc));

		addConstant(sc);
		notifyFOLDomainListeners(new mf_EventDomainSkolemConstantAdded(this, sc));

		return sc;
	}

	public void addFunction(String function) {
		functions.add(function);
	}

	public String addSkolemFunction() {
		String sf = null;
		do {
			sf = "SF" + (skolemFunctionIndexical++);
		} while (constants.contains(sf) || functions.contains(sf)
				|| predicates.contains(sf));

		addFunction(sf);
		notifyFOLDomainListeners(new mf_EventDomainSkolemFunctionAdded(this, sf));

		return sf;
	}

	public void addPredicate(String predicate) {
		predicates.add(predicate);
	}

	public String addAnswerLiteral() {
		String al = null;
		do {
			al = "Answer" + (answerLiteralIndexical++);
		} while (constants.contains(al) || functions.contains(al)
				|| predicates.contains(al));

		addPredicate(al);
		notifyFOLDomainListeners(new mf_EventDomainAnswerLiteralAdded(this, al));

		return al;
	}

	public void addFOLDomainListener(mf_i_DomainListener listener) {
		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	public void removeFOLDomainListener(mf_i_DomainListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	//
	// PRIVATE METHODS
	//
	private void notifyFOLDomainListeners(mf_a_EventDomain event) {
		synchronized (listeners) {
			for (mf_i_DomainListener l : listeners) {
				event.notifyListener(l);
			}
		}
	}
}