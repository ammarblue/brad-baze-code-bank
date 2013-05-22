/*
 * BigNumbers.java
 *
 * Copyright (C) 2007-2009 Andreas de Vries
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses
 * or write to the Free Software Foundation,Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.mathIT.numbers;

import java.math.*;
import static java.math.BigInteger.*;
//import org.mathIT.algebra.PolynomialZ;
import static org.mathIT.numbers.Numbers.factorial;

/**
 * This class provides basic analytical and number theoretic functions for big
 * numbers.
 * 
 * @author Andreas de Vries
 * @version 1.0
 */
public class BigNumbers {
	// Suppresses default constructor, ensuring non-instantiability.
	private BigNumbers() {
	}

	/**
	 * The BigInteger constant two.
	 * 
	 * @see java.math.BigInteger
	 */
	public static final BigInteger TWO = BigInteger.valueOf(2);
	/**
	 * The BigInteger constant three.
	 * 
	 * @see java.math.BigInteger
	 */
	public static final BigInteger THREE = BigInteger.valueOf(3);

	/** Maximum precision of BigDecimals, having the value {@value} . */
	public final static int PRECISION = 50; // 50;
	/**
	 * This math context is used throughout this class. It determines the
	 * maximum precision ("scale") of the BigDecimal numbers and the rounding
	 * mode and is initialized with the values PRECISION = {@value #PRECISION}
	 * and rounding mode = {@link java.math.RoundingMode RoundingMode.HALF_EVEN}.
	 */
	public static MathContext mathcontext = new MathContext(PRECISION,
			RoundingMode.HALF_EVEN);
	/**
	 * The number 0 as a BigDecimal. It equals {@link java.math.BigInteger#ZERO}
	 * .
	 */
	public static final BigDecimal ZERO_DOT = BigDecimal.ZERO;
	/**
	 * The number 1 as a BigDecimal. It equals {@link java.math.BigInteger#ONE}.
	 */
	public static final BigDecimal ONE_DOT = BigDecimal.ONE;
	/** The number 2 as a BigDecimal. */
	public static final BigDecimal TWO_DOT = BigDecimal.valueOf(2.);
	/**
	 * The number 10 as a BigDecimal. It equals {@link java.math.BigInteger#TEN}
	 * .
	 */
	public static final BigDecimal TEN_DOT = BigDecimal.TEN;
	/**
	 * The number 1/6 as a BigDecimal. It is computed with the initial value of
	 * {@link #mathcontext}.
	 */
	public static final BigDecimal ONE_SIXTH = ONE_DOT.divide(
			BigDecimal.valueOf(6), mathcontext);
	/**
	 * The number 1/3 as a BigDecimal. It is computed with the initial value of
	 * {@link #mathcontext}.
	 */
	public static final BigDecimal ONE_THIRD = ONE_DOT.divide(
			BigDecimal.valueOf(3), mathcontext);
	/** The number 1/2 as a BigDecimal. */
	public static final BigDecimal ONE_HALF = BigDecimal.valueOf(.5);
	/**
	 * Square root of 2.
	 * 
	 * @see Numbers#SQRT2
	 */
	public static final BigDecimal SQRT_TWO = new BigDecimal(
			"1.4142135623730950488016887242096980785696718753769480731766797379907324784621070388503875343276415727");
	/**
	 * Square root of 1/2.
	 * 
	 * @see Numbers#SQRT_1_2
	 */
	public static final BigDecimal SQRT_ONE_HALF = new BigDecimal(
			"0.70710678118654752440084436210484903928483593768847403658833986899536623923105351942519376716382078635298505586");
	/** 10th root of 2. */
	public static final BigDecimal ROOT_10_TWO = new BigDecimal(
			"1.07177346253629316421300632502334202290638460497755678");
	/**
	 * The constant <i>e</i>, the base of the natural logarithms. The constant
	 * is implemented to a precision of 10<sup>-100</sup>.
	 * 
	 * @see java.lang.Math#E
	 */
	// 0 1 2 3 4 5 6 7 8 9 100
	// 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
	public static final BigDecimal E = new BigDecimal(
			"2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274");
	/**
	 * The constant &#x03C0;, the ratio of the circumference of a circle to its
	 * diameter. The constant is implemented to a precision of
	 * 10<sup>-100</sup>.
	 * 
	 * @see java.lang.Math#PI
	 */
	// 0 1 2 3 4 5 6 7 8 9 100
	// 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
	public static final BigDecimal PI = new BigDecimal(
			"3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");
	/**
	 * Euler-Mascheroni constant &#947;. The constant is implemented to a
	 * precision of 10<sup>-101</sup>.
	 * 
	 * @see Numbers#GAMMA
	 */
	// 0 1 2 3 4 5 6 7 8 9 100
	// 12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901
	public static final BigDecimal GAMMA = new BigDecimal(
			"0.57721566490153286060651209008240243104215933519399235988057672348848677267776646709369470632917467495");
	/**
	 * The constant 2&#x03C0;/360, the ratio of 1 radians per degree. The
	 * constant is implemented to a precision of 10<sup>-102</sup>.
	 * 
	 * @see #PI
	 * @see Numbers#RADIANS
	 */
	// 0 1 2 3 4 5 6 7 8 9 100
	// 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
	public static final BigDecimal RADIANS = new BigDecimal(
			"0.0174532925199432957692369076848861271344287188854172545609719144017100911460344944368224156963450948");
	/**
	 * The constant &#x03C0;/4.
	 * 
	 * @see #PI
	 */
	public static final BigDecimal PI_4 = PI.multiply(new BigDecimal("0.25"));
	/**
	 * The constant &#x03C0;/2.
	 * 
	 * @see #PI
	 */
	public static final BigDecimal PI_2 = PI.multiply(new BigDecimal("0.5"));
	/**
	 * The constant 3&#x03C0;/4.
	 * 
	 * @see #PI
	 */
	public static final BigDecimal PI3_4 = PI.multiply(new BigDecimal("0.75"));
	/**
	 * The constant 5&#x03C0;/4.
	 * 
	 * @see #PI
	 */
	public static final BigDecimal PI5_4 = PI.multiply(new BigDecimal("1.25"));
	/**
	 * The constant 3&#x03C0;/2.
	 * 
	 * @see #PI
	 */
	public static final BigDecimal PI3_2 = PI.multiply(new BigDecimal("1.5"));
	/**
	 * The constant 7&#x03C0;/4.
	 * 
	 * @see #PI
	 */
	public static final BigDecimal PI7_4 = PI.multiply(new BigDecimal("1.75"));
	/**
	 * Ramanujan's constant e<sup>&#x03C0; &#x221A;163</sup>, up to an accuracy
	 * of 10<sup>-102</sup>. It is "almost" an integer number. It is listed up
	 * to 5000 digits at Simon Plouffe's web site at <a
	 * href="http://pi.lacim.uqam.ca/piDATA/ramanujan.txt"
	 * target="_top">pi.lacim.uqam.ca/piDATA/ramanujan.txt</a>.
	 */
	// 0 1 2 3 4 5 6 7 8 9 100
	// 123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012
	public static final BigDecimal RAMANUJAN = new BigDecimal(
			"262537412640768743.999999999999250072597198185688879353856337336990862707537410378210647910118607312951181346186064504193");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(3), also called
	 * Ap&eacute;ry's constant. See <a
	 * href="http://www.research.att.com/~njas/sequences/A002117"
	 * target="_top">http://www.research.att.com/~njas/sequences/A002117</a>, or
	 * M. Abramowitz and I. A. Stegun: <i>Handbook of Mathematical
	 * Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_3
	 */
	public static final BigDecimal ZETA_3 = new BigDecimal(
			"1.20205690315959428539973816151144999076498629234049888179227155534183820578631309018645587360933525814619915");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(5). See M. Abramowitz and
	 * I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_5
	 */
	public static final BigDecimal ZETA_5 = new BigDecimal(
			"1.03692775514336992633");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(7). See M. Abramowitz and
	 * I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_7
	 */
	public static final BigDecimal ZETA_7 = new BigDecimal(
			"1.00834927738192282684");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(9). See M. Abramowitz and
	 * I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_9
	 */
	public static final BigDecimal ZETA_9 = new BigDecimal(
			"1.00200839282608221442");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(11). See M. Abramowitz
	 * and I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_11
	 */
	public static final BigDecimal ZETA_11 = new BigDecimal(
			"1.00049418860411946456");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(13). See M. Abramowitz
	 * and I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_13
	 */
	public static final BigDecimal ZETA_13 = new BigDecimal(
			"1.00012271334757848915");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(15). See M. Abramowitz
	 * and I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_15
	 */
	public static final BigDecimal ZETA_15 = new BigDecimal(
			"1.00003058823630702049");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(17). See M. Abramowitz
	 * and I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_17
	 */
	public static final BigDecimal ZETA_17 = new BigDecimal(
			"1.00000763719763789976");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(19). See M. Abramowitz
	 * and I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_19
	 */
	public static final BigDecimal ZETA_19 = new BigDecimal(
			"1.00000190821271655394");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(21). See M. Abramowitz
	 * and I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_21
	 */
	public static final BigDecimal ZETA_21 = new BigDecimal(
			"1.00000047693298678781");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(23). See M. Abramowitz
	 * and I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_23
	 */
	public static final BigDecimal ZETA_23 = new BigDecimal(
			"1.00000011921992596531");
	/**
	 * The value of the Riemann Zeta function &#x03B6;(25). See M. Abramowitz
	 * and I. A. Stegun: <i>Handbook of Mathematical Functions</i>, p. 811.
	 * 
	 * @see Numbers#ZETA_25
	 */
	public static final BigDecimal ZETA_25 = new BigDecimal(
			"1.00000002980350351465");

	/**
	 * Returns the best rational approximation of a real number <i>x</i>, that
	 * is, the integers <i>p, q</i> such that <i>x</i> &#x2248 <i>p/q</i>. The
	 * algorithm computes the continued fraction coefficients corresponding to
	 * <i>x</i>, where the number of coefficients is bounded by
	 * <code>limit</code>. Usually, the value of <code>limit</code> should be
	 * about 40.
	 * 
	 * @param x
	 *            the number to be approximated
	 * @param limit
	 *            the maximum number of continued fraction coefficients being
	 *            considered
	 * @return a two-element array <code>y</code> where <code>y[0]</code> =
	 *         <i>p</i> and <code>y[1]</code> = <i>q</i> such that <i>x</i>
	 *         &#x2248 <i>p/q</i>
	 * @see #continuedFraction(BigDecimal,int)
	 */
	public static BigInteger[] bestRationalApproximation(BigDecimal x, int limit) {
		BigInteger[] cf = continuedFraction(x, limit);
		// q_{k-2} = q1, q_{k-1} = q2, q_{k} = q3:
		BigInteger q1 = ONE, q2 = ZERO, q3 = ONE; // <- k=0
		// p_{k-2} = p1, p_{k-1} = p2, p_{k} = p3:
		BigInteger p1 = ZERO, p2 = ONE, p3 = ZERO; // <- k=0
		for (BigInteger a : cf) {
			// p_{k} = a_{k} p_{k-1} + p_{k-2}:
			p3 = a.multiply(p2).add(p1);
			p1 = p2;
			p2 = p3;
			// q_{k} = a_{k} q_{k-1} + q_{k-2}:
			q3 = a.multiply(q2).add(q1);
			q1 = q2;
			q2 = q3;
		}
		if (x.signum() < 0)
			p3 = p3.negate();
		return new BigInteger[] { p3, q3 };
	}

	/**
	 * Returns a BigDecimal whose value is <i>x<sup>n</sup></i>, using the core
	 * algorithm defined in ANSI standard X3.274-1996 with rounding according to
	 * the context {@link #mathcontext}. The absolute value of the parameter
	 * <i>n</i> must be in the range 0 through 999999999, inclusive. The
	 * allowable exponent range of this method depends on the version of
	 * {@link java.math.BigDecimal#pow(int,MathContext)}. Especially, pow(
	 * {@link #ZERO_DOT}, 0) returns {@link #ONE_DOT}.
	 * 
	 * @param x
	 *            number to be raised to the power
	 * @param n
	 *            power to raise <i>x</i> to
	 * @return <i>x<sup>n</sup></i>
	 */
	public static BigDecimal pow(BigDecimal x, int n) {
		// if (x.equals(ZERO_DOT) && n == 0) return ONE_DOT;
		return x.pow(n, mathcontext);
	}

	/**
	 * Returns the <i>n</i>th root of <i>z</i>, with an accuracy of 10<sup>-
	 * {@value #PRECISION}/2</sup> <i>z</i>. The root is understood as the
	 * principal root <i>r</i> with the unique real number with the same sign as
	 * <i>z</i> such that <i>r<sup>n</sup> = z</i>. If <i>n</i> = 0, the value
	 * of <i>z</i> is returned, if <i>n</i> &lt; 0, the value {@link #ZERO_DOT
	 * 0.0} is returned.
	 * 
	 * @param n
	 *            the radical
	 * @param z
	 *            the radicand
	 * @return the principal <i>n</i>th root <i>r</i> of <i>z</i> such that
	 *         <i>r<sup>n</sup> = z</i>
	 */
	public static BigDecimal root(int n, BigInteger z) {
		return root(n, new BigDecimal(z));
	}

	/**
	 * Returns the <i>n</i>th root of <i>z</i>, with an accuracy of 10<sup>-
	 * {@value #PRECISION}/2</sup> <i>z</i>. The root is understood as the
	 * principal root <i>r</i> with the unique real number with the same sign as
	 * <i>z</i> such that <i>r<sup>n</sup> = z</i>.
	 * 
	 * @param n
	 *            the radical
	 * @param z
	 *            the radicand
	 * @return the principal <i>n</i>th root <i>r</i> of <i>z</i> such that
	 *         <i>r<sup>n</sup> = z</i>
	 * @throws IllegalArgumentException
	 *             if <i>n</i> = 0, or if <i>n</i> is even and <i>z</i> < 0
	 */
	public static BigDecimal root(int n, BigDecimal z) {
		if (n == 0) {
			throw new IllegalArgumentException("Zeroth root does not exist!");
		}
		if (n < 0)
			return ONE_DOT.divide(root(-n, z), mathcontext);

		byte sign = 1;
		if (z.signum() < 0) {
			if (n % 2 == 0) {
				throw new IllegalArgumentException("" + n
						+ "-th root of a negative number");
			}
			sign = -1;
			z = z.negate();
		}

		int scale = PRECISION;
		BigDecimal w, n1, n2, h;
		BigDecimal accuracy;

		if (z.compareTo(ONE_DOT) > 0) {
			accuracy = z.multiply(new BigDecimal("1e-" + scale / 2));
		} else {
			accuracy = new BigDecimal("1e-" + scale);
		}

		// initial value of w:
		w = pow(ROOT_10_TWO, 10 * z.toBigInteger().bitLength() / n).setScale(
				scale, BigDecimal.ROUND_HALF_EVEN);

		n1 = BigDecimal.valueOf(n - 1).divide(BigDecimal.valueOf(n), PRECISION,
				RoundingMode.HALF_EVEN); // (n-1)/n
		n2 = BigDecimal.valueOf(n);
		while (pow(w, n).subtract(z).abs().compareTo(accuracy) > 0) {
			// w = (n-1) w/n + z/(n w^(n-1)):
			w = w.multiply(n1).add(
					z.divide(pow(w, n - 1).multiply(n2), scale,
							RoundingMode.HALF_UP));
			w = w.setScale(scale + n, BigDecimal.ROUND_HALF_EVEN);
		}
		// round up to scale digits:
		if (sign < 0)
			w = w.negate();

		// compute h such that z = w^n (1+h):
		h = z.divide(w.pow(n), PRECISION, RoundingMode.HALF_EVEN).subtract(
				ONE_DOT);
		if (h.compareTo(ONE_DOT) < 0) { // accuracy < 1 always???
			// Taylor expansion of (1+h)^{1/n} to compute w (1+h)^{1/n} =
			// z^{1/n}:
			n2 = ONE_DOT.divide(n2, PRECISION, RoundingMode.HALF_EVEN);
			// factor f = 1 + h/n - 1/2n * (n-1)/n * h^2 + 1/6n * (n-1)/n * h^3:
			BigDecimal f = ONE_DOT.add(h.multiply(n2));
			f = f.subtract(ONE_HALF.multiply(n2).multiply(n1)
					.multiply(pow(h, 2)));
			f = f.add(ONE_SIXTH.multiply(n2).multiply(n1)
					.multiply(TWO_DOT.subtract(n1)).multiply(pow(h, 3)));
			w = w.multiply(f);
		}
		return w.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * Returns the square root of <i>z</i>, with an accuracy of 10<sup>-
	 * {@value #PRECISION}/2</sup> <i>z</i>. The root is understood as the
	 * principal root <i>r</i> with the unique real number with the same sign as
	 * <i>z</i> such that <i>r</i><sup>2</sup> = <i>z</i>.
	 * 
	 * @param z
	 *            the radicand
	 * @return the principal square root <i>r</i> of <i>z</i> such that
	 *         <i>r</i><sup>2</sup> = <i>z</i>
	 */
	public static BigDecimal sqrt(BigInteger z) {
		return sqrt(new BigDecimal(z));
	}

	/**
	 * Returns the square root of <i>z</i>, with an accuracy of 10<sup>-
	 * {@value #PRECISION}/2</sup> <i>z</i>. The root is understood as the
	 * principal root <i>r</i> with the unique real number with the same sign as
	 * <i>z</i> such that <i>r</i><sup>2</sup> = <i>z</i>.
	 * 
	 * @param z
	 *            the radicand
	 * @return the principal square root <i>r</i> of <i>z</i> such that
	 *         <i>r</i><sup>2</sup> = <i>z</i>
	 */
	public static BigDecimal sqrt(BigDecimal z) {
		return root(2, z);
	}

	/**
	 * Returns the value of <i>n</i> mod <i>m</i>, even for negative values.
	 * Here the usual periodic definition is used, i.e.,
	 * <p align="center">
	 * <i>n</i> mod <i>m</i> = <i>n</i> - &#x23A3;<i>n</i>/<i>m</i>&#x23A6;
	 * &nbsp; for <i>m</i> &#x2260; 0, &nbsp; and <i>n</i> mod 0 = <i>n</i>.
	 * </p>
	 * For instance, 5 mod 3 = 2, but -5 mod 3 = 1, 5 mod (-3) = -1, and -5 mod
	 * (-3) = -2. See R.L. Graham, D.E. Knuth, O. Patashnik: <i>Concrete
	 * Mathematics.</i> 2nd Edition. Addison-Wesley, Upper Saddle River, NJ
	 * 1994, &sect;3.4 (p.82)
	 * 
	 * @param n
	 *            the value to be computed
	 * @param m
	 *            the modulus
	 * @return the value <i>n</i> mod <i>m</i>
	 */
	public static BigInteger mod(BigInteger n, BigInteger m) {
		// if (m == 0) return n;
		// byte mPositive = 1;
		// if (m < 0) {mPositive = -1; m = -m; n = -n;}
		// return (n >= 0) ? n%m * mPositive : (m + n%m)%m * mPositive;
		if (m.equals(ZERO))
			return n;
		if (m.signum() > 0) {
			return n.mod(m);
		} else {
			n = n.negate();
			m = m.negate();
			return n.mod(m).negate();
		}
	}

	/**
	 * Returns the value of <i>n</i> mod <i>m</i>, even for negative values.
	 * Here the usual periodic definition is used, i.e., 5 mod 3 = 2, but -5 mod
	 * 3 = 1, 5 mod (-3) = -1, and -5 mod (-3) = -2. See R.L. Graham, D.E.
	 * Knuth, O. Patashnik: <i>Concrete Mathematics.</i> 2nd Edition.
	 * Addison-Wesley, Upper Saddle River, NJ 1994, &sect;3.4 (p.82)
	 * 
	 * @param n
	 *            the value to be computed
	 * @param m
	 *            the modulus
	 * @return the value <i>n</i> mod <i>m</i>
	 */
	public static BigDecimal mod(BigDecimal n, BigDecimal m) {
		if (m.compareTo(ZERO_DOT) == 0)
			return n;
		if (n.compareTo(ZERO_DOT) == 0)
			return n;

		BigDecimal mPositive = ONE_DOT;
		if (m.signum() < 0) {
			mPositive = mPositive.negate();
			m = m.negate();
			n = n.negate();
		}
		BigDecimal r = n.remainder(m);

		if (r.compareTo(ZERO_DOT) == 0) {
			return ZERO_DOT;
		} else if (n.signum() > 0) {
			return r.multiply(mPositive);
		} else {
			return m.add(r).multiply(mPositive);
		}
	}

	/**
	 * Returns the value of <i>m<sup>e</sup></i> mod <i>n</i> for a nonnegative
	 * integer exponent <i>e</i> and a positive modulus <i>n</i>.
	 * 
	 * @param m
	 *            the value to be raised to the power
	 * @param e
	 *            the exponent
	 * @param n
	 *            the modulus
	 * @return the value <i>m<sup>e</sup></i> mod <i>n</i>
	 * @throws ArithmeticException
	 *             if <i>e</i> &lt; 0 and gcd(<i>x</i>, <i>n</i>) &gt; 1
	 */
	public static long modPow(BigInteger m, long e, long n) {
		long x = m.mod(BigInteger.valueOf(n)).longValue();
		if (e < 0) {
			// find the multiplicative inverse of x mod n by the extended Euclid
			// algorithm:
			long[] euclid = Numbers.euclid(x, n);
			if (euclid[0] == 1) { // gcd(x,n) == 1
				x = euclid[1];
				e = -e;
			} else {
				throw new ArithmeticException("Negative exponent " + e
						+ " is not possible (" + m + " and " + n
						+ " are not relatively prime)");
			}
		}

		long y = 1;
		if (n > 0) {
			while (e > 0) {
				if ((e & 1L) == 1) { // <=> if (e % 2 == 1) {
					y = (y * x) % n;
				}
				x = (x * x) % n;
				e = e >> 1; // <=> e /= 2;
			}
			return y;
		} else {
			while (e > 0) {
				if ((e & 1L) == 1) { // <=> if (e % 2 == 1) {
					y = Numbers.mod(y * x, n);
				}
				x = Numbers.mod(x * x, n);
				e = e >> 1; // <=> e /= 2;
			}
			return y;
		}
	}

	/**
	 * Returns the value of <i>m<sup>e</sup></i> mod <i>n</i> for a nonnegative
	 * integer exponent <i>e</i> and a positive modulus <i>n</i>.
	 * 
	 * @param x
	 *            the value to be raised to the power
	 * @param e
	 *            the exponent
	 * @param n
	 *            the modulus
	 * @return the value <i>m<sup>e</sup></i> mod <i>n</i>
	 * @throws ArithmeticException
	 *             if <i>e</i> &lt; 0 and gcd(<i>x</i>, <i>n</i>) &gt; 1
	 */
	public static BigInteger modPow(BigInteger x, BigInteger e, BigInteger n) {
		if (e.signum() < 0) {
			// find the multiplicative inverse of x mod n by the extended Euclid
			// algorithm:
			BigInteger[] euclid = euclid(x, n);
			if (euclid[0].equals(ONE)) { // gcd(x,n) == 1
				x = euclid[1];
				e = e.negate();
			} else {
				throw new ArithmeticException("Negative exponent " + e
						+ " is not possible (" + x + " and " + n
						+ " are not relatively prime)");
			}
		}

		BigInteger y = ONE;
		while (e.signum() > 0) {
			if (e.mod(TWO).equals(ONE)) {
				y = mod(y.multiply(x), n);
			}
			x = mod(x.multiply(x), n);
			e = e.divide(TWO);
		}
		return y;
	}

	/**
	 * Returns the value of (<i>x<sup>e</sup></i>) mod <i>n</i>.
	 * 
	 * @param x
	 *            the value to be raised to the power
	 * @param e
	 *            the exponent
	 * @param n
	 *            the modulus
	 * @return the value of <i>x<sup>e</sup></i> mod <i>n</i>
	 */
	public static BigDecimal modPow(BigDecimal x, int e, BigDecimal n) {
		return mod(x.pow(e, mathcontext), n);
	}

	/**
	 * Returns the value of <i>x</i><sup>2</sup>.
	 * 
	 * @param x
	 *            the value to be squared
	 * @return the square of the input <i>x</i>, i.e., <i>x</i><sup>2</sup>
	 */
	public static BigInteger sqr(BigInteger x) {
		return x.multiply(x);
	}

	/**
	 * Returns an array of three integers <i>x</i>[0], <i>x</i>[1], <i>x</i>[2]
	 * as given by the extended Euclidian algorithm for integers <i>m</i> and
	 * <i>n</i>. The three integers satisfy the equations
	 * <p style="text-align:center">
	 * <i>x</i>[0] = gcd(<i>m</i>, <i>n</i>) = <i>x</i>[1] <i>m</i> +
	 * <i>x</i>[2] <i>n</i>.
	 * </p>
	 * This methods implements an iterative version of the extended Euclidian
	 * algorithm.
	 * 
	 * @param m
	 *            the first integer
	 * @param n
	 *            the second integer
	 * @return an array of three integers <i>x</i>[0], <i>x</i>[1], <i>x</i>[2]
	 *         such that <i>x</i>[0] = gcd(<i>m</i>, <i>n</i>) = <i>x</i>[1]
	 *         <i>m</i> + <i>x</i>[2] <i>n</i>
	 * @see Numbers#euclid(long,long)
	 */
	public static BigInteger[] euclid(BigInteger m, BigInteger n) {
		BigInteger x[] = { ZERO, ONE, ZERO };
		BigInteger u = ZERO, v = ONE, q, r, tmp;
		boolean mNegative = false, nNegative = false;

		if (m.signum() < 0) {
			m = m.negate();
			mNegative = true;
		}
		if (n.signum() < 0) {
			n = n.negate();
			nNegative = true;
		}

		while (n.signum() > 0) {
			// determine q and r such that m = qn + r:
			q = m.divide(n);
			r = m.mod(n);
			// replace m <- n and n <- r:
			m = n;
			n = r;
			// replace:
			tmp = u;
			u = x[1].subtract(q.multiply(u));
			x[1] = tmp;
			tmp = v;
			v = x[2].subtract(q.multiply(v));
			x[2] = tmp;
		}
		x[0] = m;
		if (mNegative)
			x[1] = x[1].negate();
		if (nNegative)
			x[2] = x[2].negate();
		return x;
	}

	/**
	 * Returns the least common multiple of <i>m</i> and <i>n</i>.
	 * 
	 * @param m
	 *            the first integer
	 * @param n
	 *            the second integer
	 * @return the least common multiple of <i>m</i> and <i>n</i>
	 */
	public static BigInteger lcm(BigInteger m, BigInteger n) {
		return m.multiply(n).divide(m.gcd(n));
	}

	/**
	 * Tests deterministically whether the given integer <i>n</i> is prime. This
	 * algorithm first uses the method
	 * {@link java.math.BigInteger#isProbablePrime(int) isProbablePrime} of the
	 * class {@link java.math.BigInteger} which yields false if the given number
	 * is not prime with certainty.
	 * 
	 * @param n
	 *            the integer to test
	 * @return true if and only if <i>n</i> is prime.
	 */
	public static boolean isPrime(BigInteger n) {
		if (n.equals(TWO))
			return true;
		if (n.equals(THREE))
			return true;
		if (n.signum() < 0 || n.mod(TWO).equals(ZERO)
				|| n.mod(THREE).equals(ZERO)) {
			return false;
		}

		if (!n.isProbablePrime(1000))
			return false; // definitely not prime!
		// System.out.println("is probably prime!");

		BigInteger FOUR = BigInteger.valueOf(4);
		boolean bigstep = false; // flag for wheel 2-4-2-4-2-...
		BigInteger i = BigInteger.valueOf(5);
		BigInteger iMax = sqrt(n).toBigInteger();

		while (i.compareTo(iMax) <= 0) {
			if (n.mod(i).equals(ZERO)) {
				return false;
			}
			// <=> flag = !(n.mod(i).equals(ZERO));
			i = bigstep ? i.add(FOUR) : i.add(TWO);
			bigstep = !bigstep;
		}
		return true;
	}

	/**
	 * Returns true if <i>n</i> is a strong probable prime to base <i>a</i>, and
	 * false if <i>n</i> is not prime. This algorithm is the core of the
	 * Miller-Rabin primality test, cf. R. Crandall &amp; C. Pomerance: <i>Prime
	 * Numbers. A Computational Perspective.</i> 2<sup>nd</sup> edition.
	 * Springer, New York 2005, &sec;3.5. The number <i>n</i> must be an odd
	 * number > 3, and 1 < <i>a</i> < n-1.
	 * 
	 * @param n
	 *            the number to be tested on strong probable primality
	 * @param a
	 *            the base of the strong probable primality test
	 * @return true if <i>n</i> is a strong probable prime to base <i>a</i>, and
	 *         false if <i>n</i> is not prime
	 * @throws IllegalArgumentException
	 *             if <i>n</i> &le; 3, or <i>a</i> &le; 1, or <i>a</i> &ge;
	 *             <i>n</i> - 1
	 * @see Numbers#isStrongProbablePrime(int,int)
	 */
	public static boolean isStrongProbablePrime(BigInteger n, BigInteger a) {
		if (n.compareTo(THREE) <= 0 || a.compareTo(ONE) <= 0
				|| a.compareTo(n.subtract(ONE)) >= 0) {
			throw new IllegalArgumentException("n=" + n + ", a=" + a);
		}

		// Determine s and t such that n-1 = t*2^s:
		int s = 0;
		BigInteger t = n.subtract(ONE);
		while (t.mod(TWO).signum() == 0) {
			t = t.divide(TWO);
			s++;
		}

		// Test the odd part t of n-1:
		BigInteger b = a.modPow(t, n);
		if (b.equals(ONE) || b.equals(n.subtract(ONE))) {
			return true;
		}

		// Test the power of 2 in n-1:
		for (int j = 1; j < s; j++) {
			b = b.multiply(b).mod(n);
			if (b.equals(n.subtract(ONE))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The AKS primality test, returns true if the integer <i>n</i> > 1 is
	 * prime. This test has a polynomial time complexity with respect to log
	 * <i>n</i>, proving that the decision problem whether a given integer is
	 * prime, is in the complexity class <b>P</b>.
	 * 
	 * @param n
	 *            an integer &gt; 1
	 * @return true if and only if <i>n</i> is prime
	 */
	/*
	 * public static boolean primalityTestAKS(BigInteger n) { if
	 * (n.mod(TWO).equals(ZERO)) return false;
	 * 
	 * if (isPower(n)) return false; // step 1 boolean rDivides = true; // flag
	 * needed to find r int lgn = n.bitLength(); int minOrder = (30 + lgn)*(30 +
	 * lgn)/12; // <- Ralf Meyer int r = 1; int a, k; int end; PolynomialZ
	 * poly1, poly2; BigInteger r_;
	 * 
	 * // Step 2: Determine the smallest r such that r does not divide n^k for
	 * all k <= minOrder while (rDivides) { r++; // ??? r += 2; r_ =
	 * BigInteger.valueOf(r); // return if gcd(n,r) > 1: if
	 * (n.gcd(r_).compareTo(ONE) > 0) { if (n.equals(r_)) { return true; } else
	 * { return false; } }
	 * 
	 * k = 0; rDivides = false; while (k <= minOrder && !rDivides) { k++; // r
	 * divides n^k - 1? rDivides = n.pow(k).subtract(ONE).mod(r_).equals(ZERO);
	 * } }
	 * 
	 * // r is always prime, i.e., phi(r) = r-1: end = (int)
	 * (Math.sqrt((r-1)/12.0) * lgn) + 8; //end = (int) (Math.sqrt(r-1) * lgn);
	 * 
	 * for (a = 1; a <= end; a++) { // poly1 = (x + a)^n mod (x^r - 1, n) poly1
	 * = new PolynomialZ(); poly1.put(ONE, ONE); poly1.put(ZERO,
	 * BigInteger.valueOf(a)); poly1 = poly1.modPow(n, r, n); // poly2 = x^n + a
	 * mod (x^r - 1, n) poly2 = new PolynomialZ();
	 * poly2.put(n.mod(BigInteger.valueOf(r)), ONE); poly2.put(ZERO,
	 * (BigInteger.valueOf(a)).mod(n)); if (!poly1.equals(poly2)) return false;
	 * } return true; }
	 */

	/**
	 * Returns the order ord(<i>m,n</i>) of <i>m</i> modulo <i>n</i>. More
	 * precisely, we have <!--
	 * 
	 * ord(m, n) = min_i { i > 0 : m^i = 1 mod n },
	 * 
	 * -->
	 * <p style="text-align:center"/>
	 * 
	 * ord(<i>m, n</i>) = min<sub><i>i</i></sub> { <i>i</i> > 0 :
	 * <i>m<sup>i</sup></i> = 1 mod <i>n</i> },
	 * 
	 * </p>
	 * The order is computed by Floyd's cycle finding algorithm.
	 * 
	 * @param m
	 *            the number of which the order is computed
	 * @param n
	 *            the modulus
	 * @return the order of <i>m</i> mod <i>n</i>
	 */
	public static BigInteger ord(BigInteger m, BigInteger n) {
		BigInteger i, x, y;
		m = m.mod(n);
		if (!m.gcd(n).equals(ONE)) {
			i = ZERO;
		} else {
			i = ONE;
			x = m;
			y = m.multiply(x).mod(n);
			while (!x.equals(y)) {
				// i++; x = (m*x) mod n; y = (m*y)^2 mod n:
				i = i.add(ONE);
				x = m.multiply(x).mod(n);
				y = m.multiply(y).mod(n);
				y = y.multiply(y).mod(n); // ?? y = m.multiply(y).mod(n);
			}
		}
		return i;
	}

	/**
	 * Tests whether there exist integers <i>m</i> and <i>k</i> such that <i>n =
	 * m<sup>k</sup></i>.
	 * 
	 * @param n
	 *            the number to be checked
	 * @return true if and only if there exist integers <i>m</i> and <i>k</i>
	 *         such that <i>n = m<sup>k</sup></i>
	 */
	public static boolean isPower(BigInteger n) {
		BigDecimal temp;
		BigDecimal nBD = new BigDecimal(n);
		int greatexp = n.bitLength(); // = [log_2(n)]
		int k = 2;

		while (k <= greatexp) {
			temp = pow(root(k, n).setScale(0, BigDecimal.ROUND_HALF_UP), k);
			if (nBD.compareTo(temp.setScale(0, BigDecimal.ROUND_HALF_UP)) == 0) {
				return true;
			}
			k++;
		}
		return false;
	}

	/**
	 * Returns an array containing coefficients of the continued fraction of the
	 * number <i>x</i>, with at most <code>limit</code> coefficients. Note that
	 * by the finite precision of <i>x</i> the higher continuous fraction
	 * coefficients get more and more imprecise. For instance, for the Euler
	 * number the coefficients are correct up to the limit 87, for
	 * Ap&eacute;ry's constant up to the limit 100, <a
	 * href="http://www.research.att.com/~njas/sequences/A013631"
	 * target="_top">http://www.research.att.com/~njas/sequences/A013631</a>,
	 * for &#x03C0; up to the limit 43, cf. <a
	 * href="http://www.research.att.com/~njas/sequences/A001203"
	 * target="_top">http://www.research.att.com/~njas/sequences/A001203</a>.
	 * 
	 * @param x
	 *            the number to be expanded as a continuous fraction
	 * @param limit
	 *            the maximum number of continuous fraction coefficients to be
	 *            computed
	 * @return an array of length <code>limit</code> containing the continuous
	 *         fraction coefficients
	 */
	public static BigInteger[] continuedFraction(BigDecimal x, int limit) {
		MathContext mc = mathcontext;
		if (x.scale() > PRECISION) {
			mc = new MathContext(x.scale());
		}
		double accuracy = 1e-50;
		BigDecimal precision = BigDecimal.valueOf(accuracy);

		if (x.signum() == 0)
			return new BigInteger[] { ZERO };
		if (x.signum() < 0)
			x = x.negate();
		BigInteger[] a = new BigInteger[limit];
		if (limit <= 0)
			return a;
		int i = 0;
		BigDecimal xi;
		if (x.compareTo(ONE_DOT) >= 0) {
			// xi = 1/x;
			xi = ONE_DOT.divide(x, mc);
		} else {
			xi = x;
			a[0] = ZERO;
			i++;
		}

		while (xi.compareTo(precision) > 0 && i < limit) {
			// a[i] = (long) (1/xi);
			a[i] = ONE_DOT.divide(xi, mc).toBigInteger();
			// xi = 1/xi - a[i] = 1/xi - (long) (1/xi);
			xi = ONE_DOT.divide(xi, mc).subtract(new BigDecimal(a[i]));
			i++;
		}

		BigInteger[] result = new BigInteger[i];
		for (int j = 0; j < result.length; j++) {
			result[j] = a[j];
		}
		return result;
	}

	/**
	 * Returns Euler's number e raised to the power of <i>x</i>. The value is
	 * computed up to an accuracy of 10<sup>-100</sup>.
	 * 
	 * @param x
	 *            the exponent
	 * @return the number <i>e<sup>x</sup></i>
	 * @see #E
	 */
	public static BigDecimal exp(BigDecimal x) {
		int n = 70; // yields an accuracy of 10^{-100}
		// Treating of very large numbers remains to be done!!!
		return exp(x, n);
	}

	/**
	 * Returns the exponential value e<sup><i>x</i></sup> of a number <i>x</i>,
	 * up to an approximation order of <i>n</i>. For <i>n</i> = 70, the value is
	 * computed up to an accuracy of 10<sup>-100</sup>. The range of the
	 * integral part of <i>x</i> is limited according to the method
	 * {@link java.math.BigDecimal#pow(int,MathContext)}.
	 * 
	 * @param x
	 *            the number
	 * @param n
	 *            the order of approximation
	 * @return the number e<i><sup>x</sup></i>, up to the approximation order
	 *         <i>n</i>
	 */
	public static BigDecimal exp(BigDecimal x, int n) {
		MathContext mc = new MathContext(100, RoundingMode.HALF_EVEN);
		BigDecimal[] xMod1 = x.divideAndRemainder(ONE_DOT, mc);
		xMod1[0] = E.pow(xMod1[0].intValue(), mc);
		x = xMod1[1];
		// Forster I, p.47 (a_k = 1/(n-k)!):
		BigDecimal y = ONE_DOT.divide(new BigDecimal(factorial(n)), mc);
		for (int k = n - 1; k >= 0; k--) {
			y = y.multiply(x).add(
					ONE_DOT.divide(new BigDecimal(factorial(k)), mc));
		}
		return y.multiply(xMod1[0]);
	}

	/**
	 * Returns the natural logarithm of a number <i>x</i>. The value is computed
	 * up to an accuracy of 10<sup>-{@link #PRECISION}</sup>.
	 * 
	 * @param x
	 *            the number
	 * @return the natural logarithm of the argument
	 */
	public static BigDecimal ln(BigInteger x) {
		return ln(new BigDecimal(x), PRECISION);
	}

	/**
	 * Returns the natural logarithm of a number <i>x</i>. The value is computed
	 * up to an accuracy of 10<sup>-{@link #PRECISION}</sup>.
	 * 
	 * @param x
	 *            the number
	 * @return the natural logarithm of the argument
	 */
	public static BigDecimal ln(BigDecimal x) {
		return ln(x, PRECISION);
	}

	/**
	 * Returns the natural logarithm of a number <i>x</i>, up to an
	 * approximation order of <i>n</i>. With <i>n</i>, the value is computed up
	 * to an accuracy of 10<sup>-n</sup>.
	 * 
	 * @param x
	 *            the number
	 * @param n
	 *            the order up to which the approximation is computed
	 * @return the natural logarithm of the the argument
	 */
	public static BigDecimal ln(BigDecimal x, int n) {
		int precision = 100;
		if (x.signum() <= 0)
			throw new IllegalArgumentException("ln("
					+ x.setScale(5, RoundingMode.HALF_EVEN) + ") not defined");
		// Determine maximum m such that e^m <= x (where m = 0 if x < 1):
		int m = 0;
		while (E.pow(m + 1).compareTo(x) < 0) {
			m++;
		}
		// x = x / e^m:
		x = x.divide(E.pow(m), precision, RoundingMode.HALF_EVEN);
		// y = (x-1) / (x+1):
		BigDecimal y = x.subtract(ONE_DOT).divide(x.add(ONE_DOT),
				RoundingMode.HALF_EVEN);
		BigDecimal ln = y;

		for (int k = 1; k <= n; k++) {
			// ln = ln + y^(2k+1) / (2k+1):
			ln = ln.add(y.pow(2 * k + 1).divide(BigDecimal.valueOf(2 * k + 1),
					RoundingMode.HALF_EVEN));
		}
		return ln.multiply(TWO_DOT).add(BigDecimal.valueOf(m));
	}

	// Horner-Schema:
	/*
	 * public static BigDecimal ln2(BigDecimal x, int n) { int precision = 100;
	 * if (x.signum() <= 0) throw new IllegalArgumentException("ln(" +
	 * x.setScale(5,RoundingMode.HALF_EVEN)+") not defined"); // Determine
	 * maximum m such that e^m <= x (where m = 0 if x < 1): int m = 0; while
	 * (E.pow(m+1).compareTo(x) < 0) { m++; } // x = x / e^m: //x =
	 * x.divide(E.pow(m), mathcontext); // <- does not modify the scale of x!! x
	 * = x.divide(E.pow(m), precision, RoundingMode.HALF_EVEN); // y = (x-1) /
	 * (x+1): BigDecimal y = x.subtract(ONE_DOT).divide(x.add(ONE_DOT),
	 * RoundingMode.HALF_EVEN); // Horner scheme: ln y = 2 y (sum (y^2)^k /
	 * (2k+1)): BigDecimal y2 = y.multiply(y, new MathContext(precision,
	 * RoundingMode.HALF_EVEN)); BigDecimal ln =
	 * TWO_DOT.divide(BigDecimal.valueOf(2*n+1), precision,
	 * RoundingMode.HALF_EVEN);
	 * 
	 * for (int k = n - 1; k >= 0; k--) { // ln = ln * y^2 + a_{n-k}: ln =
	 * ln.multiply(y2).add(TWO_DOT.divide(BigDecimal.valueOf(2*k+1), precision,
	 * RoundingMode.HALF_EVEN)); } return
	 * ln.multiply(y).add(BigDecimal.valueOf(m)); } //
	 */

	/**
	 * Returns the arc tangent of a value; the returned angle is in the range
	 * -&#x03C0;/2 through &#x03C0;/2. The precision of the returned value is at
	 * least 10<sup>-100</sup>.
	 * 
	 * @param x
	 *            a number
	 * @return the arc tangent of the argument
	 * @see #arctan(BigDecimal,int)
	 */
	public static BigDecimal arctan(BigDecimal x) {
		return arctan(x, 126);
	}

	/**
	 * Returns the trigonometric cosine of an angle <i>x</i>. The value is
	 * computed up to an accuracy of 10<sup>-100</sup>.
	 * 
	 * @param x
	 *            the angle in radians
	 * @return the cosine of the the argument
	 * @see #cos(BigDecimal,int)
	 */
	public static BigDecimal cos(BigDecimal x) {
		int order = 32; // 32 => precision of 100 decimal digits

		if (x.compareTo(ZERO_DOT) == 0) {
			return ONE_DOT;
		} else {
			// x = x % (2 pi):
			x = mod(x, PI.multiply(TWO_DOT));
			if (x.signum() < 0) {
				x = x.negate();
			}
			if (x.compareTo(PI_4) < 0) { // x < pi/4
				// cos(x,n):
				return cos(x, order);
			} else if (x.compareTo(PI3_4) < 0) { // x < 3pi/4
				// sin(pi/2 - x,n):
				return sin(PI_2.subtract(x), order);
			} else if (x.compareTo(PI5_4) <= 0) { // x <= 5pi/4
				// - cos(pi - x):
				return cos(PI.subtract(x), order).negate();
			} else if (x.compareTo(PI7_4) < 0) { // x < 7pi/4
				// - sin(x - 3pi/2):
				return cos(x.subtract(PI3_2), order).negate();
			} else { // x >= 7pi/4
				// cos(x - 2pi):
				return cos(x.subtract(PI.multiply(TWO_DOT)), order);
			}
		}
	}

	/**
	 * Returns the trigonometric sine of an angle <i>x</i>. The value is
	 * computed up to an accuracy of 10<sup>-100</sup>.
	 * 
	 * @param x
	 *            the angle in radians
	 * @return the sine of the the argument
	 * @see #sin(BigDecimal,int)
	 */
	public static BigDecimal sin(BigDecimal x) {
		int order = 32; // 32 => precision of 100 decimal digits

		if (x.compareTo(ZERO_DOT) == 0) {
			return x;
		} else {
			// x = x % (2 pi):
			x = mod(x, PI.multiply(TWO_DOT));
			BigDecimal sign = ONE_DOT;
			if (x.signum() < 0) {
				sign = sign.negate();
				x = x.negate();
			}
			if (x.compareTo(PI_4) < 0) { // x < pi/4
				// sign * sin(x,n):
				return sin(x, order).multiply(sign);
			} else if (x.compareTo(PI3_4) < 0) { // x < 3pi/4
				// sign * cos(pi/2 - x,n):
				return cos(PI_2.subtract(x), order).multiply(sign);
			} else if (x.compareTo(PI5_4) <= 0) { // x <= 5pi/4
				// sign * sin(pi - x):
				return sin(PI.subtract(x), order).multiply(sign);
			} else if (x.compareTo(PI7_4) < 0) { // x < 7pi/4
				// - sign * cos(x - 3pi/2):
				return cos(x.subtract(PI3_2), order).negate().multiply(sign);
			} else { // x >= 7pi/4
				// sign * sin(x - 2pi):
				return sin(x.subtract(PI.multiply(TWO_DOT)), order).multiply(
						sign);
			}
		}
	}

	/**
	 * Returns the arc tangent of a value up to an approximation order <i>n</i>;
	 * the returned angle is in the range -&#x03C0;/2 through &#x03C0;/2. For
	 * even <i>n</i> &#x2264; 130, the precision of the returned value is about
	 * 10<sup>-0.8<i>n</i></sup> in the worst case. The most imprecise values to
	 * be computed are <i>x</i><sub>1</sub> = &#x221A;2 - 1 and
	 * <i>x</i><sub>2</sub> = &#x221A;2 + 1 (= 1/<i>x</i><sub>1</sub>). For
	 * <i>n</i> = 126, the precision of the returned value is about
	 * 10<sup>-100</sup> for these argument values.
	 * 
	 * @param x
	 *            a number
	 * @param n
	 *            the number of iterations
	 * @return the arc tangent of the argument
	 */
	public static BigDecimal arctan(BigDecimal x, int n) {
		int precision = 100;
		// if (x.compareTo(ZERO_DOT) == 0) return ZERO_DOT;
		if (x.abs().compareTo(new BigDecimal("1E-" + (precision + 1))) < 0)
			return ZERO_DOT;
		if (x.signum() < 0)
			return arctan(x.negate(), n).negate();
		if (x.compareTo(ONE_DOT) == 0)
			return PI_4;

		/*
		 * The following 2 conditions cause 2 recursion calls if x > sqrt(2) - 1
		 * (<=> (x-1)/(x+1) > 1/(sqrt(2) - 1) = sqrt(2) + 1)
		 */
		if (x.compareTo(ONE_DOT) > 0) {
			// arctan x = pi/4 + arctan((x-1) / (x+1)) for x > 0:
			// x <- (x-1) / (x+1):
			x = x.subtract(ONE_DOT).divide(x.add(ONE_DOT), precision,
					RoundingMode.HALF_EVEN);
			return PI_4.add(arctan(x, n));
		}

		if (x.compareTo(SQRT_TWO.subtract(ONE_DOT)) > 0) {
			// arctan x = pi/4 - arctan((1-x) / (x+1)) for x > 0:
			// x <- (1-x) / (x+1):
			x = ONE_DOT.subtract(x).divide(x.add(ONE_DOT), precision,
					RoundingMode.HALF_EVEN);
			return PI_4.subtract(arctan(x, n));
		}

		BigDecimal atan;
		boolean plusSwitch = false;
		// Horner scheme: arctan x = x (sum (x^2)^k / (2k+1)):
		BigDecimal x2 = x.multiply(x, new MathContext(precision,
				RoundingMode.HALF_EVEN));
		atan = ONE_DOT.divide(BigDecimal.valueOf(2 * n + 1), precision,
				RoundingMode.HALF_EVEN);
		if (n % 2 != 0) {
			atan.negate();
			plusSwitch = !plusSwitch;
		}

		for (int k = n - 1; k >= 0; k--) {
			// atan = atan * x^2 + (-1)^(2[k/2]) a_{n-k}:
			if (plusSwitch) {
				atan = atan.multiply(x2).add(
						ONE_DOT.divide(BigDecimal.valueOf(2 * k + 1),
								precision, RoundingMode.HALF_EVEN));
			} else {
				atan = atan.multiply(x2).subtract(
						ONE_DOT.divide(BigDecimal.valueOf(2 * k + 1),
								precision, RoundingMode.HALF_EVEN));
			}
			plusSwitch = !plusSwitch;
		}
		return atan.multiply(x);
	}

	/**
	 * Returns the trigonometric sine of an angle <i>x</i> with |<i>x</i>|
	 * &#x2264; &#x03C0;/4, up to the approximation order <i>n</i>. For <i>n</i>
	 * = 32, the precision of the returned value is about 10<sup>-100</sup>.
	 * Note, however, that the precision depends on the scale of the argument
	 * <i>x</i>, i.e., a low scale may result in a low precision.
	 * 
	 * @param x
	 *            the angle in radians
	 * @param n
	 *            the number of iterations
	 * @return the sine of the argument
	 */
	public static BigDecimal cos(BigDecimal x, int n) {
		BigDecimal cos = ONE_DOT;

		for (int k = 1; k <= n; k++) {
			if (k % 2 == 0) {
				cos = cos.add(x.pow(2 * k).divide(
						new BigDecimal(factorial(2 * k)),
						RoundingMode.HALF_EVEN));
			} else {
				cos = cos.subtract(x.pow(2 * k).divide(
						new BigDecimal(factorial(2 * k)),
						RoundingMode.HALF_EVEN));
			}
		}
		return cos;
	}

	/**
	 * Returns the trigonometric sine of an angle <i>x</i> with |<i>x</i>|
	 * &#x2264; &#x03C0;/4, up to the approximation order <i>n</i>. For <i>n</i>
	 * = 32, the precision of the returned value is about 10<sup>-100</sup>.
	 * 
	 * @param x
	 *            the angle in radians
	 * @param n
	 *            the number of iterations
	 * @return the sine of the argument
	 */
	public static BigDecimal sin(BigDecimal x, int n) {
		BigDecimal sin = x;

		for (int k = 1; k <= n; k++) {
			if (k % 2 == 0) {
				sin = sin.add(x.pow(2 * k + 1).divide(
						new BigDecimal(factorial(2 * k + 1)),
						RoundingMode.HALF_EVEN));
			} else {
				sin = sin.subtract(x.pow(2 * k + 1).divide(
						new BigDecimal(factorial(2 * k + 1)),
						RoundingMode.HALF_EVEN));
			}
		}
		return sin;
	}

	/**
	 * Returns a binary string as an integer.
	 * 
	 * @param bin
	 *            the binary string to be represented in decimal form
	 * @return the BigInteger representing the binary string
	 * @throws NumberFormatException
	 *             if the string is not binary
	 */
	public static BigInteger binToDec(String bin) {
		BigInteger base = TWO;
		boolean negative = false;
		BigInteger a_i = ZERO;
		BigInteger n = ZERO;

		if (bin.substring(0, 1).equals("-")) {
			negative = true;
			bin = bin.substring(1);
		}
		if (bin.substring(bin.length() - 1, bin.length()).equals("-")) {
			negative = true;
			bin = bin.substring(0, bin.length() - 1);
		}

		for (int i = 0; i < bin.length(); i++) {
			a_i = new BigInteger(bin.substring(i, i + 1));
			if (a_i.compareTo(ONE) > 0) {
				throw new NumberFormatException("No binary number");
			}
			n = n.add(a_i.multiply(base.pow(bin.length() - i - 1)));
		}

		if (negative)
			n = n.negate();
		return n;
	}

	/**
	 * Returns <i>n</i> as a binary string.
	 * 
	 * @param n
	 *            the decimal value to be represented in binary form.
	 * @return the binary representation of <i>n</i>
	 * @see #decToBin(BigDecimal, int)
	 */
	public static String decToBin(BigInteger n) {
		final BigInteger base = TWO;
		boolean negative = false;
		String symbols = "";
		BigInteger q = n;
		int r = 0;

		if (n.compareTo(ZERO) == 0) {
			symbols = "0";
		} else {
			if (n.compareTo(ZERO) < 0) {
				q = q.negate();
				negative = true;
			}

			while (q.compareTo(ZERO) > 0) {
				r = q.mod(base).intValue();
				symbols = r + symbols;
				q = q.divide(base);
			}
		}
		if (negative)
			symbols = "-" + symbols;
		return symbols;
	}

	/**
	 * Returns <i>n</i> as a binary string of the specified minimum length.
	 * 
	 * @param n
	 *            the decimal value to be represented in binary form
	 * @param minimumLength
	 *            the minimum length of the returned binary string
	 * @return string representing the binary representation of n
	 */
	public static String decToBin(BigInteger n, int minimumLength) {
		String out = decToBin(n);
		// pad with zeros:
		for (int i = out.length(); i < minimumLength; i++) {
			out = "0" + out;
		}
		return out;
	}

	/**
	 * Returns <i>z</i> as a hexadecimal string with at most <code>limit</code>
	 * positions right of the hexadecimal point.
	 * 
	 * @param z
	 *            the decimal value to be represented in hexadecimal form.
	 * @param limit
	 *            the maximum position after the hexadecimal point.
	 * @return the binary representation of <i>z</i>
	 */
	public static String decToBin(BigDecimal z, int limit) {
		final BigDecimal base = TWO_DOT;
		boolean negative = false;
		String symbols = "";

		if (z.compareTo(ZERO_DOT) == 0) {
			symbols = "0";
		} else {
			if (z.compareTo(ZERO_DOT) < 0) {
				z = z.negate();
				negative = true;
			}
			if (z.compareTo(ONE_DOT) >= 0) {
				symbols = decToBin(z.toBigInteger()) + ".";
			}

			z = z.subtract(new BigDecimal(z.toBigInteger())); // z -= (int) z;
			z = z.multiply(base); // z *= base;
			int r;
			int counter = 0;
			while (z.compareTo(ZERO_DOT) > 0 && counter <= limit) {
				r = z.intValue(); // (int) z;
				symbols += r;
				z = z.subtract(new BigDecimal(z.toBigInteger())); // z -= (int)
																	// z;
				z = z.multiply(base); // z *= base;
				counter++;
			}
		}

		if (negative)
			symbols = "-" + symbols;
		return symbols;
	}

	/**
	 * Returns a binary string as a hexadecimal string.
	 * 
	 * @param bin
	 *            the binary string to be represented in hexadecimal form
	 * @return the hexadecimal string representing the binary string
	 * @throws NumberFormatException
	 *             if the string is not binary
	 */
	public static String binToHex(String bin) {
		String hex = "";
		// padd the binary string with 0's to represent full tetrads:
		if (bin.length() % 4 > 0) {
			int length = bin.length();
			for (int i = 0; i < 4 - length % 4; i++) {
				bin = "0" + bin;
			}
		}

		String substring;
		for (int i = 0; i < bin.length(); i += 4) {
			substring = bin.substring(i, i + 4);
			if (substring.equals("0000")) {
				hex += "0";
			} else if (substring.equals("0001")) {
				hex += "1";
			} else if (substring.equals("0010")) {
				hex += "2";
			} else if (substring.equals("0011")) {
				hex += "3";
			} else if (substring.equals("0100")) {
				hex += "4";
			} else if (substring.equals("0101")) {
				hex += "5";
			} else if (substring.equals("0110")) {
				hex += "6";
			} else if (substring.equals("0111")) {
				hex += "7";
			} else if (substring.equals("1000")) {
				hex += "8";
			} else if (substring.equals("1001")) {
				hex += "9";
			} else if (substring.equals("1010")) {
				hex += "A";
			} else if (substring.equals("1011")) {
				hex += "B";
			} else if (substring.equals("1100")) {
				hex += "C";
			} else if (substring.equals("1101")) {
				hex += "D";
			} else if (substring.equals("1110")) {
				hex += "E";
			} else if (substring.equals("1111")) {
				hex += "F";
			}
		}
		return hex;
	}

	/**
	 * Returns <i>n</i> as a hexadecimal string.
	 * 
	 * @param n
	 *            the decimal value to be represented in hexadecimal form.
	 * @return the hexadecimal representation of <i>n</i>
	 */
	public static String decToHex(BigInteger n) {
		final BigInteger base = new BigInteger("16");
		boolean negative = false;
		String symbols = "";
		BigInteger q = n;
		int r = 0;

		if (n.compareTo(ZERO) == 0) {
			symbols = "0";
		} else {
			if (n.compareTo(ZERO) < 0) {
				q = q.negate();
				negative = true;
			}

			while (q.compareTo(ZERO) > 0) {
				r = q.mod(base).intValue();
				if (r <= 9) {
					symbols = r + symbols;
				} else if (r == 10) {
					symbols = "A" + symbols;
				} else if (r == 11) {
					symbols = "B" + symbols;
				} else if (r == 12) {
					symbols = "C" + symbols;
				} else if (r == 13) {
					symbols = "D" + symbols;
				} else if (r == 14) {
					symbols = "E" + symbols;
				} else if (r == 15) {
					symbols = "F" + symbols;
				}
				q = q.divide(base); // q /= base;
			}
		}
		if (negative)
			symbols = "-" + symbols;
		return symbols;
	}

	/**
	 * Returns <i>z</i> as a hexadecimal string with at most 100 positions right
	 * of the hexadecimal point.
	 * 
	 * @param z
	 *            the decimal value to be represented in hexadecimal form.
	 * @return the hexadecimal representation of <i>z</i>
	 */
	public static String decToHex(BigDecimal z) {
		return decToHex(z, 100);
	}

	/**
	 * Returns <i>z</i> as a hexadecimal string with at most <code>limit</code>
	 * positions right of the hexadecimal point.
	 * 
	 * @param z
	 *            the decimal value to be represented in hexadecimal form.
	 * @param limit
	 *            the maximum position after the hexadecimal point.
	 * @return the hexadecimal representation of <i>z</i>
	 */
	public static String decToHex(BigDecimal z, int limit) {
		final BigDecimal base = new BigDecimal(16.);
		boolean negative = false;
		String symbols = "";

		if (z.compareTo(ZERO_DOT) == 0) {
			symbols = "0";
		} else {
			if (z.compareTo(ZERO_DOT) < 0) {
				z = z.negate();
				negative = true;
			}
			if (z.compareTo(ONE_DOT) >= 0) {
				symbols = decToHex(z.toBigInteger()) + ".";
			}

			z = z.subtract(new BigDecimal(z.toBigInteger())); // z -= (int) z;
			z = z.multiply(base); // z *= base;
			int r;
			int counter = 0;
			while (z.compareTo(ZERO_DOT) > 0 && counter <= limit) {
				r = z.intValue(); // (int) z;
				if (r <= 9) {
					symbols += r;
				} else if (r == 10) {
					symbols += "A";
				} else if (r == 11) {
					symbols += "B";
				} else if (r == 12) {
					symbols += "C";
				} else if (r == 13) {
					symbols += "D";
				} else if (r == 14) {
					symbols += "E";
				} else if (r == 15) {
					symbols += "F";
				}
				z = z.subtract(new BigDecimal(z.toBigInteger())); // z -= (int)
																	// z;
				z = z.multiply(base); // z *= base;
				counter++;
			}
		}

		if (negative)
			symbols = "-" + symbols;
		return symbols;
	}

	/**
	 * Returns the Gray code of an integer.
	 * 
	 * @param x
	 *            an integer
	 * @return the Gray code of <i>x</i> as a string
	 * @see #grayCode(BigInteger, int)
	 */
	public static String grayCode(BigInteger x) {
		x = x.xor(x.shiftRight(1));
		String code = "";
		for (int i = x.bitLength(); i >= 0; i--) {
			code += x.testBit(i) ? "1" : "0";
		}
		return code;
	}

	/**
	 * Returns the Gray code of an integer <i>x</i>, with a given minimum
	 * length. If the minimum length is greater than the bit length of the
	 * integer <i>x</i>, the Gray code string is padded with leading zeros.
	 * 
	 * @param x
	 *            an integer
	 * @param minimumLength
	 *            the minimum length of the returned Gray code string
	 * @return the Gray code of <i>x</i> as a string
	 * @see #grayCode(BigInteger)
	 */
	public static String grayCode(BigInteger x, int minimumLength) {
		x = x.xor(x.shiftRight(1));
		String out = "";
		int length = (x.bitLength() > minimumLength) ? x.bitLength()
				: minimumLength;
		for (int i = length - 1; i >= 0; i--) {
			out += x.testBit(i) ? "1" : "0";
		}
		return out;
	}

	/**
	 * Returns binary representation of the integer represented by a Gray code
	 * string; the string is padded with zeros if it is shorter than the
	 * specified minimum length.
	 * 
	 * @param grayCode
	 *            a Gray code string
	 * @param minimumLength
	 *            the minimum length of the binary string
	 * @return the binary representation of the integer represented by the Gray
	 *         code string
	 * @throws NumberFormatException
	 *             if the string does not represent a Gray code
	 * @see #grayCodeToBinary(String)
	 */
	public static String grayCodeToBinary(String grayCode, int minimumLength) {
		String out = decToBin(grayCodeToDecimal(grayCode));
		// pad with zeros:
		for (int i = out.length(); i < minimumLength; i++) {
			out = "0" + out;
		}
		return out;
	}

	/**
	 * Returns binary representation of the integer in which is represented by a
	 * Gray code string.
	 * 
	 * @param grayCode
	 *            a Gray code string
	 * @return the binary representation of the integer represented by the Gray
	 *         code string
	 * @throws NumberFormatException
	 *             if the string does not represent a Gray code
	 * @see #grayCodeToBinary(String, int)
	 */
	public static String grayCodeToBinary(String grayCode) {
		return decToBin(grayCodeToDecimal(grayCode));
	}

	/**
	 * Returns the integer represented by a Gray code string.
	 * 
	 * @param grayCode
	 *            a Gray code string
	 * @return the integer represented by the Gray code string
	 * @throws NumberFormatException
	 *             if the string does not represent a Gray code
	 */
	public static BigInteger grayCodeToDecimal(String grayCode) {
		BigInteger x = binToDec(grayCode), y = ZERO;
		for (int i = grayCode.length() - 1; i >= 0; i--) {
			// y = ((y & (1 << i+1)) >> 1) ^ (x & (1 << i)) + y:
			y = y.and(ONE.shiftLeft(i + 1)).shiftRight(1)
					.xor(x.and(ONE.shiftLeft(i))).add(y);
		}
		return y;
	}

	/** For test purposes... */
	/*
	 * public static void main (String[] args) {
	 * //System.out.println(ONE_DOT.divide(SQRT_TWO, 100,
	 * BigDecimal.ROUND_HALF_EVEN));
	 * 
	 * //System.out.println(RADIANS.doubleValue());
	 * 
	 * //BigDecimal root_10_2u = new
	 * BigDecimal("1.071773462536293164213006325023342022906384604977556784");
	 * //BigDecimal root_10_2b = new
	 * BigDecimal("1.071773462536293164213006325023342022906384604977556783");
	 * //System.out.println(root_10_2u.pow(10).setScale(55,
	 * BigDecimal.ROUND_HALF_UP));
	 * //System.out.println(root_10_2b.pow(10).setScale(55,
	 * BigDecimal.ROUND_HALF_UP)); //System.out.println(root_10_2u.scale());
	 * 
	 * //--- root: ----------- //BigDecimal x = TWO_DOT, y; //int n = 5; //for
	 * (int i=0; i <= 100; i += 10) { // y = x.multiply(TEN_DOT.pow(i)); //
	 * System.out.println(y.setScale(PRECISION,BigDecimal.ROUND_HALF_EVEN) +
	 * " =\n" +
	 * root(n,y).pow(n).setScale(PRECISION,BigDecimal.ROUND_HALF_EVEN)); //
	 * //System.out.println(y.setScale(PRECISION,BigDecimal.ROUND_HALF_EVEN) +
	 * " =\n" +
	 * root2(n,y).pow(n).setScale(PRECISION,BigDecimal.ROUND_HALF_EVEN)); //}
	 * //for (int i=0; i < 50; i += 10) { // y = x.divide(TEN_DOT.pow(i),
	 * mathcontext); //
	 * System.out.println(y.setScale(PRECISION,BigDecimal.ROUND_HALF_EVEN) +
	 * " =\n" +
	 * root(n,y).pow(n).setScale(PRECISION,BigDecimal.ROUND_HALF_EVEN)); //}
	 * //System.out.println(x.setScale(PRECISION,BigDecimal.ROUND_HALF_EVEN) +
	 * " =\n" +
	 * root2(n,x).pow(n).setScale(PRECISION,BigDecimal.ROUND_HALF_EVEN));
	 * 
	 * //--- continued fraction: ----------- //BigDecimal x =
	 * SQRT_TWO.subtract(ONE_DOT); ////BigDecimal x =
	 * ZETA_3.multiply(BigDecimal.valueOf(26)).divide(PI.pow(3), 100,
	 * BigDecimal.ROUND_HALF_EVEN); ////BigDecimal x = ZETA_3.divide(PI.pow(3),
	 * 100, BigDecimal.ROUND_HALF_EVEN); //System.out.println("scale=" +
	 * x.scale()); //int limit = 100; //System.out.print("x=["); //BigInteger[]
	 * cf = continuedFraction(x,limit); //for (int i=0; i < cf.length - 1; i++)
	 * { // System.out.print(cf[i] + ","); //}
	 * //System.out.println(cf[cf.length-1] + "]"); //for (int i=1; i < 20; i++)
	 * { // BigInteger[] bra = bestRationalApproximation(x,i); //
	 * System.out.println("x = " + bra[0] +"/"+bra[1]); //}
	 * 
	 * //--- AKS: ----------- // boolean prime; // BigInteger m = new
	 * BigInteger("1524157877488187891"); // needs about 260 sec = 4:20 min //
	 * //BigInteger m = new BigInteger("101"); // needs about 0.04 sec //
	 * //BigInteger m = new BigInteger("1234567891"); // needs about 14 sec //
	 * long time = System.currentTimeMillis(); // prime = isPrime(m); // time =
	 * System.currentTimeMillis() - time; // System.out.println(m + " prime? " +
	 * prime + " (running time: " + time + " ms)"); // time =
	 * System.currentTimeMillis(); // prime = primalityTestAKS(m); // time =
	 * System.currentTimeMillis() - time; // System.out.println(m + " prime? " +
	 * prime + " (running time AKS: " + time + " ms)"); // //
	 * modulo:------------ //BigDecimal n = BigDecimal.valueOf(5.); //BigDecimal
	 * m = BigDecimal.valueOf(3.);;
	 * //System.out.println("5 mod 3="+mod(n,m)+", -5 mod 3="+mod(n.negate(),m)+
	 * //
	 * ", 5 mod -3="+mod(n,m.negate())+", -5 mod -3 ="+mod(n.negate(),m.negate
	 * ())); // // Gray Code: ------------------------- // for (BigInteger i =
	 * BigInteger.valueOf(1024); i.compareTo(BigInteger.valueOf(1100)) < 0; i =
	 * i.add(ONE)) { // System.out.println(grayCode(i,5) + " = " +
	 * Numbers.grayCode(i.longValue(),5)); // } // for (BigInteger i = new
	 * BigInteger("0"); i.compareTo(new BigInteger("129")) < 0; i = i.add(ONE))
	 * { // System.out.println(i + " -> " + grayCode(i,5) + " -> " +
	 * grayCodeToBinary(grayCode(i,5),5)); // } // Binary:
	 * ------------------------- for (BigDecimal i = BigDecimal.valueOf(1.);
	 * i.compareTo(BigDecimal.valueOf(2.1)) < 0; i =
	 * i.add(BigDecimal.valueOf(.1))) { System.out.println(decToBin(i,10) +
	 * " = " + Numbers.decToHex(i.doubleValue(),10)); } //
	 * ------------------------------------ // BigInteger x =
	 * BigInteger.valueOf(5); // BigInteger e = BigInteger.valueOf(3); //
	 * BigInteger n = BigInteger.valueOf(7); // System.out.println(x + "^" + e +
	 * " mod " + n + " = "+modPow(x,e,n)); // x = BigInteger.valueOf(-5); // e =
	 * BigInteger.valueOf(3); // n = BigInteger.valueOf(7); //
	 * System.out.println(x + "^" + e + " mod " + n + " = "+modPow(x,e,n)); // x
	 * = BigInteger.valueOf(5); // e = BigInteger.valueOf(-3); // n =
	 * BigInteger.valueOf(7); // System.out.println(x + "^" + e + " mod " + n +
	 * " = "+modPow(x,e,n)); // x = BigInteger.valueOf(5); // e =
	 * BigInteger.valueOf(3); // n = BigInteger.valueOf(-7); //
	 * System.out.println(x + "^" + e + " mod " + n + " = "+modPow(x,e,n)); //
	 * BigDecimal x = PI; //BigDecimal.valueOf(5); // int e = 3; // BigDecimal n
	 * = BigDecimal.valueOf(7); // System.out.println(x + "^" + e + " mod " + n
	 * + " = "+modPow(x,e,n)); // x = BigDecimal.valueOf(-15); // e = 3; // n =
	 * BigDecimal.valueOf(7); // System.out.println(x + "^" + e + " mod " + n +
	 * " = "+modPow(x,e,n)); // x = BigDecimal.valueOf(5); // e = -3; // n =
	 * BigDecimal.valueOf(7); // System.out.println(x + "^" + e + " mod " + n +
	 * " = "+modPow(x,e,n)); // x = BigDecimal.valueOf(5); // e = 3; // n =
	 * BigDecimal.valueOf(-7); // System.out.println(x + "^" + e + " mod " + n +
	 * " = "+modPow(x,e,n)); //--- sin, cos, ln, exp: --- //BigDecimal x = PI_4;
	 * //System.out.println("ln 2 = " + ln(TWO_DOT,6).setScale(25,6)); // x =
	 * ONE_DOT; // System.out.println("Abweichung: " +
	 * arctan(x,100).subtract(PI_4).setScale(105,6)); // for(int order = 124;
	 * order <= 130; order += 2) { // x = SQRT_TWO.subtract(ONE_DOT); //
	 * System.out.println("x="+x.setScale(3,6) + ": " + //
	 * arctan(x,150).subtract(arctan(x,order)).setScale(105,6) +
	 * ", n="+order*.8); // //x = SQRT_TWO.add(ONE_DOT); //
	 * //System.out.println("x="+x.setScale(3,6) + ": " + // //
	 * arctan(x,100).subtract(arctan(x,order)).setScale(25,6) + ", n="+order);
	 * // } // // long zeit; // zeit = System.nanoTime(); //
	 * //System.out.println("x="+x + ": " + cos(x)); // for (x = new
	 * BigDecimal("0.51"); x.compareTo(new BigDecimal("0.43")) <= 0; x =
	 * x.add(new BigDecimal(".0025"))) { //
	 * //System.out.println("x="+x.doubleValue() + ": " + (sin(x).doubleValue()
	 * - Math.sin(x.doubleValue()))); //
	 * //System.out.println("x="+x.doubleValue() + ": " + (cos(x).doubleValue()
	 * - Math.cos(x.doubleValue()))); // System.out.println("x="+x + ": " +
	 * arctan(x,8).setScale(25,6)); // System.out.println("x="+x + ": " +
	 * Math.atan(x.doubleValue())); // //System.out.println("ln "+x + " = " +
	 * ln(x,100).setScale(50,6)); // //System.out.println("ln "+x + " = " +
	 * Math.log(x.doubleValue())); // //System.out.println("e^"+x + " = " +
	 * exp(x).setScale(50,6)); // //System.out.println("e^"+x + " = " +
	 * Math.exp(x.doubleValue())); // } // zeit = System.nanoTime() - zeit; //
	 * System.out.println("Laufzeit: " + zeit/1000000 + " ms"); //zeit =
	 * System.nanoTime(); //for (x = TEN_DOT; x.compareTo(new BigDecimal("11"))
	 * <= 0; x = x.add(new BigDecimal("0.1"))) { // System.out.println("ln "+x +
	 * " = " + ln2(x,100).setScale(50,6)); //} //zeit = System.nanoTime() -
	 * zeit; //System.out.println("Laufzeit: " + zeit/1000000 + " ms");
	 * 
	 * //long zeit; //zeit = System.nanoTime();
	 * //algorithmics.TP1.BigNumbers.sin(x,100); //zeit = System.nanoTime() -
	 * zeit; //System.out.println("rekursiv: " + zeit + " ns");
	 * 
	 * //zeit = System.nanoTime(); //sin(x,100); //zeit = System.nanoTime() -
	 * zeit; //System.out.println("iterativ: " + zeit + " ns");
	 * 
	 * //for(int n=45; n <= 35; n++) {
	 * //System.out.println("Abweichung(n="+n+"): " +
	 * sin(x,n).add(SQRT_ONE_HALF).setScale(105,6));
	 * //System.out.println("Abweichung(n="+n+"): " +
	 * cos(x,n).subtract(SQRT_ONE_HALF).setScale(105,6));
	 * //System.out.println("Abweichung(n="+n+"): " +
	 * arctan(x,n).subtract(PI.divide
	 * (BigDecimal.valueOf(6),6)).setScale(105,6));
	 * //System.out.println("  sin.scale() = " + sin(X,n).scale()); //}
	 * 
	 * 
	 * //System.out.println(test + " = " + decToHex(test,50) + "_{16}"); //
	 * 
	 * //boolean isPower; //BigInteger m = new
	 * BigInteger("1524157877488187881"); // needs about 1,4 sec //BigInteger m
	 * = new BigInteger("1234567891"); //BigInteger m = new BigInteger("48"); //
	 * needs about 130 ms //long time = System.currentTimeMillis(); //isPower =
	 * isPower(m); //time = System.currentTimeMillis() - time;
	 * //System.out.println(m + " isPower? " + isPower + " (running time: " +
	 * time + "ms)");
	 * 
	 * //BigInteger[] x = euclid(new BigInteger("21"), new BigInteger("35"));
	 * //or(BigInteger n : x) { // System.out.print(n + ", "); //}
	 * //System.out.println(); //System.out.println("ord(5,7)=" + ord(new
	 * BigInteger("5"), new BigInteger("7"))); //System.out.print("5 mod 3=" +
	 * mod(new BigInteger("5"), new BigInteger("3")));
	 * //System.out.print(", -5 mod 3=" + mod(new BigInteger("-5"), new
	 * BigInteger("3"))); //System.out.print(", 5 mod -3=" + mod(new
	 * BigInteger("5"), new BigInteger("-3")));
	 * //System.out.println(", -5 mod -3=" + mod(new BigInteger("-5"), new
	 * BigInteger("-3"))); //System.out.println("5 mod 0=" + mod(new
	 * BigInteger("5"), new BigInteger("0"))); } //
	 */
}