package com.software.reuze;
//package aima.core.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import aima.core.logic.fol.parsing.FOLVisitor;
import java.util.Arrays;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 */
public class mf_Predicate implements mf_i_SentenceAtomic {

    private String predicateName;
    private List<mf_i_NodeTerm> terms = new ArrayList<mf_i_NodeTerm>();
    private String stringRep = null;
    private int hashCode = 0;

    public mf_Predicate(String predicateName, List<mf_i_NodeTerm> terms) {
        this.predicateName = predicateName;
        this.terms.addAll(terms);
    }

    /**
     * Constructor
     * @param name
     * @param terms 
     */
    public mf_Predicate(String name, mf_i_NodeTerm... terms) {
        this.predicateName = name;
        this.terms.addAll(Arrays.asList(terms));
    }

    public String getPredicateName() {
        return predicateName;
    }

    public List<mf_i_NodeTerm> getTerms() {
        return Collections.unmodifiableList(terms);
    }

    //
    // START-AtomicSentence
    public String getSymbolicName() {
        return getPredicateName();
    }

    public boolean isCompound() {
        return true;
    }

    public List<mf_i_NodeTerm> getArgs() {
        return getTerms();
    }

    public Object accept(mf_Visitor v, Object arg) {
        return v.visitPredicate(this, arg);
    }

    public mf_Predicate copy() {
        List<mf_i_NodeTerm> copyTerms = new ArrayList<mf_i_NodeTerm>();
        for (mf_i_NodeTerm t : terms) {
            copyTerms.add(t.copy());
        }
        return new mf_Predicate(predicateName, copyTerms);
    }

    // END-AtomicSentence
    //
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof mf_Predicate)) {
            return false;
        }
        mf_Predicate p = (mf_Predicate) o;
        return p.getPredicateName().equals(getPredicateName())
                && p.getTerms().equals(getTerms());
    }

    @Override
    public int hashCode() {
        if (0 == hashCode) {
            hashCode = 17;
            hashCode = 37 * hashCode + predicateName.hashCode();
            for (mf_i_NodeTerm t : terms) {
                hashCode = 37 * hashCode + t.hashCode();
            }
        }
        return hashCode;
    }

    @Override
    public String toString() {
        if (null == stringRep) {
            StringBuilder sb = new StringBuilder();
            sb.append(predicateName);
            sb.append("(");

            boolean first = true;
            for (mf_i_NodeTerm t : terms) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append(t.toString());
            }

            sb.append(")");
            stringRep = sb.toString();
        }

        return stringRep;
    }
}