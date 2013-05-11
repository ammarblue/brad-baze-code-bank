package com.software.reuze;
//package aima.core.learning.inductive;

//import aima.core.learning.framework.Attribute;
//import aima.core.learning.framework.Example;
//import aima.core.util.Util;
import java.util.HashMap;


/**
 * Represents a decision tree, figure 18.2, page 699, AIMAv3. The book 
 * describes a Boolean decision tree, where attributes are compared with 
 * examples to predict a decision. This structure has been implemented with 
 * the classes Attribute, Example, and DecisionTreeLeaf. Because decision
 * trees are not necessarily boolean (see Bshouty et al, "On Learning Decision
 * Trees with Large Output Domains", <a href="http://people.clarkson.edu/~ttamon/ps-dir/btw-alg98-dt.pdf">
 * http://people.clarkson.edu/~ttamon/ps-dir/btw-alg98-dt.pdf</a>), this class
 * has been implemented with generics; for a boolean decision tree, simply
 * instantiate DecisionTree&lt;Boolean&gt;.
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class al_DecisionTreeBoolean<E> {

    /**
     * Attribute to test at this node
     */
    private d_PropertyListPair attribute;
    /**
     * Outcomes branching from this node
     */
    private HashMap<E, al_DecisionTreeBoolean> branches = new HashMap<E, al_DecisionTreeBoolean>();

    /**
     * Constructor
     */
    public al_DecisionTreeBoolean() {
    }

    /**
     * Constructor
     * 
     */
    public al_DecisionTreeBoolean(d_PropertyListPair attribute) {
        this.attribute = attribute;
    }

    /**
     * Get this node's attribute, see framework.Attribute for usage
     * @return 
     */
    public d_PropertyListPair getAttribute() {
        return this.attribute;
    }

    /**
     * Set this node's attribute
     * @param attribute 
     */
    public void setAttribute(d_PropertyListPair attribute) {
        this.attribute = attribute;
    }

    /**
     * Get the branching nodes from this node
     * @return 
     */
    public HashMap<E, al_DecisionTreeBoolean> getBranches() {
        return this.branches;
    }

    /**
     * Add a leaf branching from this node
     * @param attributeValue
     * @param decision 
     */
    public <F> void addLeaf(E value, F decision) {
        this.branches.put(value, new al_DecisionTreeBooleanLeaf<F>(decision));
    }

    /**
     * Add a node branching from this node; every node is an instance of
     * DecisionTree
     * @param value
     * @param tree 
     */
    public void addBranch(E value, al_DecisionTreeBoolean tree) {
        this.branches.put(value, tree);
    }

    /**
     * Make prediction based on an example
     * @param e
     * @return 
     */
    public E predict(al_Example e) {
        // setup
        String attributeName = this.getAttribute().getName();
        E attributeValue = (E) e.get(attributeName).getValue();
        // match
        if (this.branches.containsKey(attributeValue)) {
            return (E) this.branches.get(attributeValue).predict(e); // continue down tree
        } else {
            throw new RuntimeException("No branch for value: " + attributeValue);
        }
    }

    /**
     * Return string representation
     * @return 
     */
    @Override
    public String toString() {
        return toString(0, new StringBuilder());
    }

    /**
     * Return formatted string representation
     * @param depth
     * @param buf
     * @return 
     */
    public String toString(int depth, StringBuilder s) {
        if (this.getAttribute().getName() != null) {
            s.append("[");
            s.append(this.getAttribute().getName());
            s.append("]");
            s.append("\n");
            for (E attributeValue : this.branches.keySet()) {
                m_MathUtil2.append(s, "  ", depth + 1);
                s.append("|- ");
                s.append(attributeValue);
                s.append(" -> ");
                al_DecisionTreeBoolean child = this.branches.get(attributeValue);
                if( child instanceof al_DecisionTreeBooleanLeaf){
                    s.append(child);
                    s.append("\n");
                }
                else{
                    s.append("\n");
                    m_MathUtil2.append(s, "  ", depth + 1);
                    s.append(child.toString(depth + 1, new StringBuilder()));
                }
            }
        }
        return s.toString();
    }
}
