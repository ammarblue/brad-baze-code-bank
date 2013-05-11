package com.software.reuze;
//package aima.core.logic.fol.domain;

/**
 * @author Ravi Mohan
 * 
 */
public class mf_DomainExamples {
	public static mf_Domain crusadesDomain() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("John");
		domain.addConstant("Richard");
		domain.addConstant("England");
		domain.addConstant("Saladin");
		domain.addConstant("Crown");

		domain.addFunction("LeftLegOf");
		domain.addFunction("BrotherOf");
		domain.addFunction("EnemyOf");
		domain.addFunction("LegsOf");

		domain.addPredicate("King");
		return domain;
	}

	public static mf_Domain knowsDomain() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("John");
		domain.addConstant("Jane");
		domain.addConstant("Bill");
		domain.addConstant("Elizabeth");
		domain.addFunction("Mother");
		domain.addPredicate("Knows");
		return domain;
	}

	public static mf_Domain weaponsDomain() {

		mf_Domain domain = new mf_Domain();
		domain.addConstant("West");
		domain.addConstant("America");
		domain.addConstant("M1");
		domain.addConstant("Nono");
		domain.addPredicate("American");
		domain.addPredicate("Weapon");
		domain.addPredicate("Sells");
		domain.addPredicate("Hostile");
		domain.addPredicate("Criminal");
		domain.addPredicate("Missile");
		domain.addPredicate("Owns");
		domain.addPredicate("Enemy");

		return domain;
	}

	public static mf_Domain kingsDomain() {
		mf_Domain domain = new mf_Domain();
		domain.addConstant("John");
		domain.addConstant("Richard");
		domain.addPredicate("King");
		domain.addPredicate("Greedy");
		domain.addPredicate("Evil");
		return domain;
	}

	public static mf_Domain lovesAnimalDomain() {
		mf_Domain domain = new mf_Domain();
		domain.addPredicate("Animal");
		domain.addPredicate("Loves");
		domain.addPredicate("Kills");
		domain.addPredicate("Cat");
		domain.addConstant("Jack");
		domain.addConstant("Tuna");
		domain.addConstant("Curiosity");
		return domain;
	}

	public static mf_Domain ringOfThievesDomain() {
		mf_Domain domain = new mf_Domain();
		domain.addPredicate("Parent");
		domain.addPredicate("Caught");
		domain.addPredicate("Friend");
		domain.addPredicate("Skis");
		domain.addConstant("Mike");
		domain.addConstant("Joe");
		domain.addConstant("Janet");
		domain.addConstant("Nancy");
		domain.addConstant("Ernie");
		domain.addConstant("Bert");
		domain.addConstant("Red");
		domain.addConstant("Drew");
		return domain;
	}
}
