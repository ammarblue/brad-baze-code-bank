package com.software.reuze;
//package aima.core.logic.propositional.visitors;

/*import aima.core.logic.propositional.parsing.AbstractPLVisitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;*/

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class mf_CNFTransformer extends ml_SentenceVisitOps {
	@Override
	public Object visitBinarySentence(ml_SentenceBinary bs, Object arg) {
		if (bs.isBiconditional()) {
			return transformBiConditionalSentence(bs);
		} else if (bs.isImplication()) {
			return transformImpliedSentence(bs);
		} else if (bs.isOrSentence()
				&& (bs.firstTermIsAndSentence() || bs.secondTermIsAndSentence())) {
			return distributeOrOverAnd(bs);
		} else {
			return super.visitBinarySentence(bs, arg);
		}
	}

	@Override
	public Object visitNotSentence(ml_SentenceComplexUnary us, Object arg) {
		return transformNotSentence(us);
	}

	/**
	 * Returns the specified sentence in conjunctive normal form.
	 * 
	 * @param s
	 *            a sentence of propositional logic
	 * 
	 * @return the specified sentence in conjunctive normal form.
	 */
	public ml_a_ParseTreeSentence transform(ml_a_ParseTreeSentence s) {
		ml_a_ParseTreeSentence toTransform = s;
		while (!(toTransform.equals(step(toTransform)))) {
			toTransform = step(toTransform);
		}

		return toTransform;
	}

	private ml_a_ParseTreeSentence step(ml_a_ParseTreeSentence s) {
		return (ml_a_ParseTreeSentence) s.accept(this, null);
	}

	private ml_a_ParseTreeSentence transformBiConditionalSentence(ml_SentenceBinary bs) {
		ml_a_ParseTreeSentence first = new ml_SentenceBinary("=>", (ml_a_ParseTreeSentence) bs.getFirst()
				.accept(this, null), (ml_a_ParseTreeSentence) bs.getSecond().accept(this,
				null));
		ml_a_ParseTreeSentence second = new ml_SentenceBinary("=>", (ml_a_ParseTreeSentence) bs.getSecond()
				.accept(this, null), (ml_a_ParseTreeSentence) bs.getFirst()
				.accept(this, null));
		return new ml_SentenceBinary("AND", first, second);
	}

	private ml_a_ParseTreeSentence transformImpliedSentence(ml_SentenceBinary bs) {
		ml_a_ParseTreeSentence first = new ml_SentenceComplexUnary((ml_a_ParseTreeSentence) bs.getFirst().accept(
				this, null));
		return new ml_SentenceBinary("OR", first, (ml_a_ParseTreeSentence) bs.getSecond()
				.accept(this, null));
	}

	private ml_a_ParseTreeSentence transformNotSentence(ml_SentenceComplexUnary us) {
		if (us.getNegated() instanceof ml_SentenceComplexUnary) {
			return (ml_a_ParseTreeSentence) ((ml_SentenceComplexUnary) us.getNegated()).getNegated()
					.accept(this, null);
		} else if (us.getNegated() instanceof ml_SentenceBinary) {
			ml_SentenceBinary bs = (ml_SentenceBinary) us.getNegated();
			if (bs.isAndSentence()) {
				ml_a_ParseTreeSentence first = new ml_SentenceComplexUnary((ml_a_ParseTreeSentence) bs.getFirst()
						.accept(this, null));
				ml_a_ParseTreeSentence second = new ml_SentenceComplexUnary((ml_a_ParseTreeSentence) bs.getSecond()
						.accept(this, null));
				return new ml_SentenceBinary("OR", first, second);
			} else if (bs.isOrSentence()) {
				ml_a_ParseTreeSentence first = new ml_SentenceComplexUnary((ml_a_ParseTreeSentence) bs.getFirst()
						.accept(this, null));
				ml_a_ParseTreeSentence second = new ml_SentenceComplexUnary((ml_a_ParseTreeSentence) bs.getSecond()
						.accept(this, null));
				return new ml_SentenceBinary("AND", first, second);
			} else {
				return (ml_a_ParseTreeSentence) super.visitNotSentence(us, null);
			}
		} else {
			return (ml_a_ParseTreeSentence) super.visitNotSentence(us, null);
		}
	}

	private ml_a_ParseTreeSentence distributeOrOverAnd(ml_SentenceBinary bs) {
		ml_SentenceBinary andTerm = bs.firstTermIsAndSentence() ? (ml_SentenceBinary) bs
				.getFirst() : (ml_SentenceBinary) bs.getSecond();
		ml_a_ParseTreeSentence otherterm = bs.firstTermIsAndSentence() ? bs.getSecond() : bs
				.getFirst();
		// (alpha or (beta and gamma) = ((alpha or beta) and (alpha or gamma))
		ml_a_ParseTreeSentence alpha = (ml_a_ParseTreeSentence) otherterm.accept(this, null);
		ml_a_ParseTreeSentence beta = (ml_a_ParseTreeSentence) andTerm.getFirst().accept(this, null);
		ml_a_ParseTreeSentence gamma = (ml_a_ParseTreeSentence) andTerm.getSecond().accept(this, null);
		ml_a_ParseTreeSentence distributed = new ml_SentenceBinary("AND", new ml_SentenceBinary(
				"OR", alpha, beta), new ml_SentenceBinary("OR", alpha, gamma));
		return distributed;
	}
}