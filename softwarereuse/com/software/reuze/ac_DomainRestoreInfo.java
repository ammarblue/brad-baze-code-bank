package com.software.reuze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.software.reuze.d_Pair;


/**
 * Provides informations which might be useful for a caller of a constraint
 * propagation algorithm. It maintains old domains for variables and provides
 * means to restore the initial state of the CSP (before domain reduction
 * started). Additionally, a flag indicates whether an empty domain has been
 * found during propagation.
 * 
 * @author Ruediger Lunde
 * 
 */
public class ac_DomainRestoreInfo {
	private List<d_Pair<mf_NodeTermVariable, ac_Domain>> savedDomains;
	private HashSet<mf_NodeTermVariable> affectedVariables;
	private boolean emptyDomainObserved;

	public ac_DomainRestoreInfo() {
		savedDomains = new ArrayList<d_Pair<mf_NodeTermVariable, ac_Domain>>();
		affectedVariables = new HashSet<mf_NodeTermVariable>();
	}

	public void clear() {
		savedDomains.clear();
		affectedVariables.clear();
	}

	public boolean isEmpty() {
		return savedDomains.isEmpty();
	}

	/**
	 * Stores the specified domain for the specified variable if a domain has
	 * not yet been stored for the variable.
	 */
	public void storeDomainFor(mf_NodeTermVariable var, ac_Domain domain) {
		if (!affectedVariables.contains(var)) {
			savedDomains.add(new d_Pair<mf_NodeTermVariable, ac_Domain>(var, domain));
			affectedVariables.add(var);
		}
	}

	public void setEmptyDomainFound(boolean b) {
		emptyDomainObserved = b;
	}

	/**
	 * Can be called after all domain information has been collected to reduce
	 * storage consumption.
	 * 
	 * @return this object, after removing one hashtable.
	 */
	public ac_DomainRestoreInfo compactify() {
		affectedVariables = null;
		return this;
	}

	public boolean isEmptyDomainFound() {
		return emptyDomainObserved;
	}

	public List<d_Pair<mf_NodeTermVariable, ac_Domain>> getSavedDomains() {
		return savedDomains;
	}

	public void restoreDomains(ac_CSP csp) {
		for (d_Pair<mf_NodeTermVariable, ac_Domain> pair : getSavedDomains())
			csp.setDomain(pair.getFirst(), pair.getSecond());
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		for (d_Pair<mf_NodeTermVariable, ac_Domain> pair : savedDomains)
			result.append(pair.getFirst() + "=" + pair.getSecond() + " ");
		if (emptyDomainObserved)
			result.append("!");
		return result.toString();
	}
}
