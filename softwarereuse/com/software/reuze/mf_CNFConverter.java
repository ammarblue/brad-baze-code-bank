package com.software.reuze;
//package aima.core.logic.fol;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.FOSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;*/

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 345.<br>
 * <br>
 * Every sentence of first-order logic can be converted into an inferentially
 * equivalent CNF sentence.<br>
 * <br>
 * <b>Note:</b> Transformation rules extracted from 346 and 347, which are
 * essentially the INSEADO method outlined in: <a
 * href="http://logic.stanford.edu/classes/cs157/2008/lectures/lecture09.pdf"
 * >INSEADO Rules</a>
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class mf_CNFConverter {

	private mf_Parser parser = null;
	private mf_VisitorSubstitution substVisitor;

	public mf_CNFConverter(mf_Parser parser) {
		this.parser = parser;

		this.substVisitor = new mf_VisitorSubstitution();
	}

	/**
	 * Returns the specified sentence as a list of clauses, where each clause is
	 * a disjunction of literals.
	 * 
	 * @param aSentence
	 *            a sentence in first order logic (predicate calculus)
	 * 
	 * @return the specified sentence as a list of clauses, where each clause is
	 *         a disjunction of literals.
	 */
	public mf_CNF convertToCNF(mf_i_Sentence aSentence) {
		// I)mplications Out:
		mf_i_Sentence implicationsOut = (mf_i_Sentence) aSentence.accept(
				new ImplicationsOut(), null);

		// N)egations In:
		mf_i_Sentence negationsIn = (mf_i_Sentence) implicationsOut.accept(
				new NegationsIn(), null);

		// S)tandardize variables:
		// For sentences like:
		// (FORALL x P(x)) V (EXISTS x Q(x)),
		// which use the same variable name twice, change the name of one of the
		// variables.
		mf_i_Sentence saQuantifiers = (mf_i_Sentence) negationsIn.accept(
				new StandardizeQuantiferVariables(substVisitor),
				new LinkedHashSet<mf_NodeTermVariable>());

		// Remove explicit quantifiers, by skolemizing existentials
		// and dropping universals:
		// E)xistentials Out
		// A)lls Out:
		mf_i_Sentence andsAndOrs = (mf_i_Sentence) saQuantifiers.accept(
				new RemoveQuantifiers(parser), new LinkedHashSet<mf_NodeTermVariable>());

		// D)istribution
		// V over ^:
		mf_i_Sentence orDistributedOverAnd = (mf_i_Sentence) andsAndOrs.accept(
				new DistributeOrOverAnd(), null);

		// O)perators Out
		return (new CNFConstructor()).construct(orDistributedOverAnd);
	}
}

class ImplicationsOut implements mf_Visitor {
	public ImplicationsOut() {

	}

	public Object visitPredicate(mf_Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		return function;
	}

	public Object visitNotSentence(mf_SentenceNot notSentence, Object arg) {
		mf_i_Sentence negated = notSentence.getNegated();

		return new mf_SentenceNot((mf_i_Sentence) negated.accept(this, arg));
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		mf_i_Sentence alpha = (mf_i_Sentence) sentence.getFirst().accept(this, arg);
		mf_i_Sentence beta = (mf_i_Sentence) sentence.getSecond().accept(this, arg);

		// Eliminate <=>, bi-conditional elimination,
		// replace (alpha <=> beta) with (~alpha V beta) ^ (alpha V ~beta).
		if (mf_SymbolsConnectors.isBICOND(sentence.getConnector())) {
			mf_i_Sentence first = new mf_SentenceConnected(mf_SymbolsConnectors.OR,
					new mf_SentenceNot(alpha), beta);
			mf_i_Sentence second = new mf_SentenceConnected(mf_SymbolsConnectors.OR, alpha,
					new mf_SentenceNot(beta));

			return new mf_SentenceConnected(mf_SymbolsConnectors.AND, first, second);
		}

		// Eliminate =>, implication elimination,
		// replacing (alpha => beta) with (~alpha V beta)
		if (mf_SymbolsConnectors.isIMPLIES(sentence.getConnector())) {
			return new mf_SentenceConnected(mf_SymbolsConnectors.OR, new mf_SentenceNot(alpha),
					beta);
		}

		return new mf_SentenceConnected(sentence.getConnector(), alpha, beta);
	}

	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {

		return new mf_SentenceQuantified(sentence.getQuantifier(),
				sentence.getVariables(), (mf_i_Sentence) sentence.getQuantified()
						.accept(this, arg));
	}
}

class NegationsIn implements mf_Visitor {
	public NegationsIn() {

	}

	public Object visitPredicate(mf_Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		return function;
	}

	public Object visitNotSentence(mf_SentenceNot notSentence, Object arg) {
		// CNF requires NOT (~) to appear only in literals, so we 'move ~
		// inwards' by repeated application of the following equivalences:
		mf_i_Sentence negated = notSentence.getNegated();

		// ~(~alpha) equivalent to alpha (double negation elimination)
		if (negated instanceof mf_SentenceNot) {
			return ((mf_SentenceNot) negated).getNegated().accept(this, arg);
		}

		if (negated instanceof mf_SentenceConnected) {
			mf_SentenceConnected negConnected = (mf_SentenceConnected) negated;
			mf_i_Sentence alpha = negConnected.getFirst();
			mf_i_Sentence beta = negConnected.getSecond();
			// ~(alpha ^ beta) equivalent to (~alpha V ~beta) (De Morgan)
			if (mf_SymbolsConnectors.isAND(negConnected.getConnector())) {
				// I need to ensure the ~s are moved in deeper
				mf_i_Sentence notAlpha = (mf_i_Sentence) (new mf_SentenceNot(alpha)).accept(
						this, arg);
				mf_i_Sentence notBeta = (mf_i_Sentence) (new mf_SentenceNot(beta)).accept(
						this, arg);
				return new mf_SentenceConnected(mf_SymbolsConnectors.OR, notAlpha, notBeta);
			}

			// ~(alpha V beta) equivalent to (~alpha ^ ~beta) (De Morgan)
			if (mf_SymbolsConnectors.isOR(negConnected.getConnector())) {
				// I need to ensure the ~s are moved in deeper
				mf_i_Sentence notAlpha = (mf_i_Sentence) (new mf_SentenceNot(alpha)).accept(
						this, arg);
				mf_i_Sentence notBeta = (mf_i_Sentence) (new mf_SentenceNot(beta)).accept(
						this, arg);
				return new mf_SentenceConnected(mf_SymbolsConnectors.AND, notAlpha, notBeta);
			}
		}

		// in addition, rules for negated quantifiers:
		if (negated instanceof mf_SentenceQuantified) {
			mf_SentenceQuantified negQuantified = (mf_SentenceQuantified) negated;
			// I need to ensure the ~ is moved in deeper
			mf_i_Sentence notP = (mf_i_Sentence) (new mf_SentenceNot(
					negQuantified.getQuantified())).accept(this, arg);

			// ~FORALL x p becomes EXISTS x ~p
			if (mf_Quantifiers.isFORALL(negQuantified.getQuantifier())) {
				return new mf_SentenceQuantified(mf_Quantifiers.EXISTS,
						negQuantified.getVariables(), notP);
			}

			// ~EXISTS x p becomes FORALL x ~p
			if (mf_Quantifiers.isEXISTS(negQuantified.getQuantifier())) {
				return new mf_SentenceQuantified(mf_Quantifiers.FORALL,
						negQuantified.getVariables(), notP);
			}
		}

		return new mf_SentenceNot((mf_i_Sentence) negated.accept(this, arg));
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		return new mf_SentenceConnected(sentence.getConnector(),
				(mf_i_Sentence) sentence.getFirst().accept(this, arg),
				(mf_i_Sentence) sentence.getSecond().accept(this, arg));
	}

	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {

		return new mf_SentenceQuantified(sentence.getQuantifier(),
				sentence.getVariables(), (mf_i_Sentence) sentence.getQuantified()
						.accept(this, arg));
	}
}

class StandardizeQuantiferVariables implements mf_Visitor {
	// Just use a localized indexical here.
	private mf_StandardizeApartIndexical quantifiedIndexical = new mf_StandardizeApartIndexical() {
		private int index = 0;

		public String getPrefix() {
			return "q";
		}

		public int getNextIndex() {
			return index++;
		}
	};

	private mf_VisitorSubstitution substVisitor = null;

	public StandardizeQuantiferVariables(mf_VisitorSubstitution substVisitor) {
		this.substVisitor = substVisitor;
	}

	public Object visitPredicate(mf_Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		return function;
	}

	public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
		return new mf_SentenceNot((mf_i_Sentence) sentence.getNegated().accept(this,
				arg));
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		return new mf_SentenceConnected(sentence.getConnector(),
				(mf_i_Sentence) sentence.getFirst().accept(this, arg),
				(mf_i_Sentence) sentence.getSecond().accept(this, arg));
	}

	@SuppressWarnings("unchecked")
	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {
		Set<mf_NodeTermVariable> seenSoFar = (Set<mf_NodeTermVariable>) arg;

		// Keep track of what I have to subst locally and
		// what my renamed variables will be.
		Map<mf_NodeTermVariable, mf_i_NodeTerm> localSubst = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
		List<mf_NodeTermVariable> replVariables = new ArrayList<mf_NodeTermVariable>();
		for (mf_NodeTermVariable v : sentence.getVariables()) {
			// If local variable has be renamed already
			// then I need to come up with own name
			if (seenSoFar.contains(v)) {
				mf_NodeTermVariable sV = new mf_NodeTermVariable(quantifiedIndexical.getPrefix()
						+ quantifiedIndexical.getNextIndex());
				localSubst.put(v, sV);
				// Replacement variables should contain new name for variable
				replVariables.add(sV);
			} else {
				// Not already replaced, this name is good
				replVariables.add(v);
			}
		}

		// Apply the local subst
		mf_i_Sentence subst = substVisitor.subst(localSubst,
				sentence.getQuantified());

		// Ensure all my existing and replaced variable
		// names are tracked
		seenSoFar.addAll(replVariables);

		mf_i_Sentence sQuantified = (mf_i_Sentence) subst.accept(this, arg);

		return new mf_SentenceQuantified(sentence.getQuantifier(), replVariables,
				sQuantified);
	}
}

class RemoveQuantifiers implements mf_Visitor {

	private mf_Parser parser = null;
	private mf_VisitorSubstitution substVisitor = null;

	public RemoveQuantifiers(mf_Parser parser) {
		this.parser = parser;

		substVisitor = new mf_VisitorSubstitution();
	}

	public Object visitPredicate(mf_Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		return function;
	}

	public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
		return new mf_SentenceNot((mf_i_Sentence) sentence.getNegated().accept(this,
				arg));
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		return new mf_SentenceConnected(sentence.getConnector(),
				(mf_i_Sentence) sentence.getFirst().accept(this, arg),
				(mf_i_Sentence) sentence.getSecond().accept(this, arg));
	}

	@SuppressWarnings("unchecked")
	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {
		mf_i_Sentence quantified = sentence.getQuantified();
		Set<mf_NodeTermVariable> universalScope = (Set<mf_NodeTermVariable>) arg;

		// Skolemize: Skolemization is the process of removing existential
		// quantifiers by elimination. This is done by introducing Skolem
		// functions. The general rule is that the arguments of the Skolem
		// function are all the universally quantified variables in whose
		// scope the existential quantifier appears.
		if (mf_Quantifiers.isEXISTS(sentence.getQuantifier())) {
			Map<mf_NodeTermVariable, mf_i_NodeTerm> skolemSubst = new LinkedHashMap<mf_NodeTermVariable, mf_i_NodeTerm>();
			for (mf_NodeTermVariable eVar : sentence.getVariables()) {
				if (universalScope.size() > 0) {
					// Replace with a Skolem Function
					String skolemFunctionName = parser.getFOLDomain()
							.addSkolemFunction();
					skolemSubst.put(eVar, new mf_NodeTermFunction(skolemFunctionName,
							new ArrayList<mf_i_NodeTerm>(universalScope)));
				} else {
					// Replace with a Skolem Constant
					String skolemConstantName = parser.getFOLDomain()
							.addSkolemConstant();
					skolemSubst.put(eVar, new mf_SymbolConstant(skolemConstantName));
				}
			}

			mf_i_Sentence skolemized = substVisitor.subst(skolemSubst, quantified);
			return skolemized.accept(this, arg);
		}

		// Drop universal quantifiers.
		if (mf_Quantifiers.isFORALL(sentence.getQuantifier())) {
			// Add to the universal scope so that
			// existential skolemization may be done correctly
			universalScope.addAll(sentence.getVariables());

			mf_i_Sentence droppedUniversal = (mf_i_Sentence) quantified.accept(this, arg);

			// Enusre my scope is removed before moving back up
			// the call stack when returning
			universalScope.removeAll(sentence.getVariables());

			return droppedUniversal;
		}

		// Should not reach here as have already
		// handled the two quantifiers.
		throw new IllegalStateException("Unhandled Quantifier:"
				+ sentence.getQuantifier());
	}
}

class DistributeOrOverAnd implements mf_Visitor {

	public DistributeOrOverAnd() {

	}

	public Object visitPredicate(mf_Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		return function;
	}

	public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
		return new mf_SentenceNot((mf_i_Sentence) sentence.getNegated().accept(this,
				arg));
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		// Distribute V over ^:

		// This will cause flattening out of nested ^s and Vs
		mf_i_Sentence alpha = (mf_i_Sentence) sentence.getFirst().accept(this, arg);
		mf_i_Sentence beta = (mf_i_Sentence) sentence.getSecond().accept(this, arg);

		// (alpha V (beta ^ gamma)) equivalent to
		// ((alpha V beta) ^ (alpha V gamma))
		if (mf_SymbolsConnectors.isOR(sentence.getConnector())
				&& mf_SentenceConnected.class.isInstance(beta)) {
			mf_SentenceConnected betaAndGamma = (mf_SentenceConnected) beta;
			if (mf_SymbolsConnectors.isAND(betaAndGamma.getConnector())) {
				beta = betaAndGamma.getFirst();
				mf_i_Sentence gamma = betaAndGamma.getSecond();
				return new mf_SentenceConnected(mf_SymbolsConnectors.AND,
						(mf_i_Sentence) (new mf_SentenceConnected(mf_SymbolsConnectors.OR, alpha,
								beta)).accept(this, arg),
						(mf_i_Sentence) (new mf_SentenceConnected(mf_SymbolsConnectors.OR, alpha,
								gamma)).accept(this, arg));
			}
		}

		// ((alpha ^ gamma) V beta) equivalent to
		// ((alpha V beta) ^ (gamma V beta))
		if (mf_SymbolsConnectors.isOR(sentence.getConnector())
				&& mf_SentenceConnected.class.isInstance(alpha)) {
			mf_SentenceConnected alphaAndGamma = (mf_SentenceConnected) alpha;
			if (mf_SymbolsConnectors.isAND(alphaAndGamma.getConnector())) {
				alpha = alphaAndGamma.getFirst();
				mf_i_Sentence gamma = alphaAndGamma.getSecond();
				return new mf_SentenceConnected(mf_SymbolsConnectors.AND,
						(mf_i_Sentence) (new mf_SentenceConnected(mf_SymbolsConnectors.OR, alpha,
								beta)).accept(this, arg),
						(mf_i_Sentence) (new mf_SentenceConnected(mf_SymbolsConnectors.OR, gamma,
								beta)).accept(this, arg));
			}
		}

		return new mf_SentenceConnected(sentence.getConnector(), alpha, beta);
	}

	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {
		// This should not be called as should have already
		// removed all of the quantifiers.
		throw new IllegalStateException(
				"All quantified sentences should have already been removed.");
	}
}

class CNFConstructor implements mf_Visitor {
	public CNFConstructor() {

	}

	public mf_CNF construct(mf_i_Sentence orDistributedOverAnd) {
		ArgData ad = new ArgData();

		orDistributedOverAnd.accept(this, ad);

		return new mf_CNF(ad.clauses);
	}

	public Object visitPredicate(mf_Predicate p, Object arg) {
		ArgData ad = (ArgData) arg;
		if (ad.negated) {
			ad.clauses.get(ad.clauses.size() - 1).addNegativeLiteral(p);
		} else {
			ad.clauses.get(ad.clauses.size() - 1).addPositiveLiteral(p);
		}
		return p;
	}

	public Object visitTermEquality(mf_SentenceAtomicTermEquality equality, Object arg) {
		ArgData ad = (ArgData) arg;
		if (ad.negated) {
			ad.clauses.get(ad.clauses.size() - 1).addNegativeLiteral(equality);
		} else {
			ad.clauses.get(ad.clauses.size() - 1).addPositiveLiteral(equality);
		}
		return equality;
	}

	public Object visitVariable(mf_NodeTermVariable variable, Object arg) {
		// This should not be called
		throw new IllegalStateException("visitVariable() should not be called.");
	}

	public Object visitConstant(mf_SymbolConstant constant, Object arg) {
		// This should not be called
		throw new IllegalStateException("visitConstant() should not be called.");
	}

	public Object visitFunction(mf_NodeTermFunction function, Object arg) {
		// This should not be called
		throw new IllegalStateException("visitFunction() should not be called.");
	}

	public Object visitNotSentence(mf_SentenceNot sentence, Object arg) {
		ArgData ad = (ArgData) arg;
		// Indicate that the enclosed predicate is negated
		ad.negated = true;
		sentence.getNegated().accept(this, arg);
		ad.negated = false;

		return sentence;
	}

	public Object visitConnectedSentence(mf_SentenceConnected sentence, Object arg) {
		ArgData ad = (ArgData) arg;
		mf_i_Sentence first = sentence.getFirst();
		mf_i_Sentence second = sentence.getSecond();

		first.accept(this, arg);
		if (mf_SymbolsConnectors.isAND(sentence.getConnector())) {
			ad.clauses.add(new mf_Clause());
		}
		second.accept(this, arg);

		return sentence;
	}

	public Object visitQuantifiedSentence(mf_SentenceQuantified sentence,
			Object arg) {
		// This should not be called as should have already
		// removed all of the quantifiers.
		throw new IllegalStateException(
				"All quantified sentences should have already been removed.");
	}

	class ArgData {
		public List<mf_Clause> clauses = new ArrayList<mf_Clause>();
		public boolean negated = false;

		public ArgData() {
			clauses.add(new mf_Clause());
		}
	}
}