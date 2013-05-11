package com.software.reuze;
//package aima.core.logic.fol;

/**
 * @author Ravi Mohan
 * 
 */
public class mf_SymbolsConnectors {
	public static final String AND = "AND";

	public static final String OR = "OR";

	public static final String NOT = "NOT";

	public static final String IMPLIES = "=>";

	public static final String BICOND = "<=>";

	public static boolean isAND(String connector) {
		return AND.equals(connector);
	}

	public static boolean isOR(String connector) {
		return OR.equals(connector);
	}

	public static boolean isNOT(String connector) {
		return NOT.equals(connector);
	}

	public static boolean isIMPLIES(String connector) {
		return IMPLIES.equals(connector);
	}

	public static boolean isBICOND(String connector) {
		return BICOND.equals(connector);
	}
}