package com.software.reuze;

public interface da_i_Visitor {

    /**
     * Applies the procedure defined by an implementation of this interface to
     * the given node.
     * 
     * @param node
     */
    void visit(Object node, String command);
}
