package com.software.reuze;
//package aima.core.logic.propositional.parsing.ast;

//import aima.core.logic.common.ParseTreeNode;
//import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public abstract class ml_a_ParseTreeSentence implements l_ParseTreeNode {

	public abstract Object accept(ml_i_SentenceVisit plv, Object arg);
}