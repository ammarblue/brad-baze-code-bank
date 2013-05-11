package com.software.reuze;
//package aima.core.logic.fol.parsing.ast;

import java.util.List;


//import aima.core.logic.common.ParseTreeNode;
//import aima.core.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface mf_i_Node extends l_ParseTreeNode {
	String getSymbolicName();

	boolean isCompound();

	List<? extends mf_i_Node> getArgs();

	Object accept(mf_Visitor v, Object arg);

	mf_i_Node copy();
}
