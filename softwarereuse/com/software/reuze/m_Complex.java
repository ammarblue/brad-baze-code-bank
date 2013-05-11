package com.software.reuze;
import java.io.Serializable;
import java.util.Scanner;

//package com.example.android.notepad2;
// Complex.java
/*
 * Copyright (c) 2003 Jon S. Squire.  All Rights Reserved.
 * 11/15/09 fixed atan, added asin,acos Robert Cook
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the author or the names of contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. THE AUTHOR AND CONTRIBUTORS
 * SHALL NOT BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE
 * AS A RESULT OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE
 * SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL THE AUTHOR OR CONTRIBUTORS
 * OR SUCCEEDING LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA,
 * OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF THE AUTHOR
 * OR CONTRIBUTORS HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any human use medical device.
 */

/** A Complex consists of a real
 *  and imaginary part, called Cartesian coordinates.
 *
 *  The Complex class provides methods for arithmetic such as:
 *  add, subtract, multiply, divide, negate and invert.
 *  Also provided are complex functions sin, cos, tan, atan,
 *  sqrt, log, exp, pow, sinh, cosh, tanh, atanh.
 *
 *  Source code <a href="Complex.java">Complex.java</a>
 */

public strictfp class m_Complex extends Number implements Comparable<m_Complex>, Serializable
	{
		float x, y; // Cartesian representation of complex
		public static final char INPUT_DELIMITER=' ';
		/** static temporary vector **/
		public final static m_Complex tmp = new m_Complex();
		  /** static temporary vector **/
		public final static m_Complex tmp2 = new m_Complex();
		public final static m_Complex I = new m_Complex(0,1);
		public final static m_Complex MINUS_I = new m_Complex(0,-1);
		public final static m_Complex ONE_R = new m_Complex(0,-1);
		public final static m_Complex ZERO = new m_Complex(0,0);
		/** cartesian coordinates real and imaginary are NaN */
		public m_Complex(){x=Float.NaN; y=Float.NaN;}
		
		/** construct a copy of a Complex object */
		public m_Complex(m_Complex z){x=z.x; y=z.y;}
		
		/** real value, imaginary=0.0 */
		public m_Complex(float x){this.x=x; y=0f;}
		
		/** cartesian coordinates real and imaginary */
		public m_Complex(float x, float y){this.x=x; this.y=y;}
		public m_Complex set(m_Complex f) {
			  x = f.x;
			  y = f.y;
			  return this;
		}
		public m_Complex set(float x, float y) {
			  this.x = x;
			  this.y = y;
			  return this;
		}
		/** convert cartesian to polar */
		public m_Complex polar(){
			float r = (float)StrictMath.sqrt(this.x*this.x+this.y*this.y);
			y = (float)StrictMath.atan2(this.y,this.x);
			x=r;
			return this;
		}
		
		/** convert polar to cartesian */
		public m_Complex cartesian(){
			float r = this.x*(float)StrictMath.cos(this.y);
			y=this.x*(float)StrictMath.sin(this.y);
			x=r;
			return this;
		}
		
		/** the real part of the complex number */ 
		public float getReal(){return this.x;}
		
		/** extract the imaginary part of the complex number */
		public float getImaginary(){return this.y;}
		
		/** extract the magnitude of the complex number */ 
		public float magnitude(){
			return (float)StrictMath.sqrt(this.x*this.x+this.y*this.y);
		}
		
		/** extract the argument of the complex number */
		public float argument(){return (float)StrictMath.atan2(this.y,this.x);}
		// return phase
		public float phase() { return (float)StrictMath.atan2(y, x); }  // between -pi and pi

		/** add complex numbers */ 
		public m_Complex add(m_Complex z){
			this.x+=z.x; this.y+=z.y;
			return this;
		}
		
		/** add a float to a complex number */
		public m_Complex add(float d){
			this.x+=d;
			return this;
		}
		
		/** subtract z from the complex number */
		public m_Complex subtract(m_Complex z){
			this.x-=z.x; this.y-=z.y;
			return this;
		}
		
		/** subtract the float d from the complex number */
		public m_Complex subtract(float d){
			this.x-=d;
			return this;
		}
		
		/** negate the complex number */
		public m_Complex negate(){
			x=-this.x; y=-this.y;
			return this;
		}
		
		/** multiply complex numbers */ 
		public m_Complex multiply(m_Complex z){
			float r=this.x*z.x-this.y*z.y;
			y=this.x*z.y+this.y*z.x;
			x=r;
			return this;
		}
		
		/** multiply a complex number by a float */
		public m_Complex multiply(float d){
			this.x*=d; this.y*=d;
			return this;
		}
		
		/** divide the complex number by z */
		public m_Complex divide(m_Complex z){
			float r=z.x*z.x+z.y*z.y;
			float s=(this.x*z.x+this.y*z.y)/r;
			y=(this.y*z.x-this.x*z.y)/r;
			x=s;
			return this;
		}
		
		/** divide the complex number by the float d */
		public m_Complex divide(float d){
			this.x/=d;  this.y/=d;
			return this;
		}
		
		/** invert the complex number */
		public m_Complex reciprocal() {
			float r=this.x*this.x+this.y*this.y;
			this.x/=r; this.y/=-r;
			return this;
		}
		
		/** conjugate the complex number */
		public m_Complex conjugate(){y=-y; return this;}
		
		/** compute the absolute value of a complex number */
		public float abs(){return (float)StrictMath.sqrt(this.x*this.x+this.y*this.y);}
		
		/** compare complex numbers for equality */
		public boolean equals(Object o){
			if (o==null) return false;
			m_Complex z=(m_Complex)o;
			return Math.abs(z.x-this.x)<1.0E-8 && Math.abs(z.y-this.y)<1.0E-8;
		}

		/** compare complex numbers for equality */
		public int compareTo(m_Complex z){
			if (this.equals(z)) return 0;
			if (this.x>z.x && this.y>z.y) return 1;
			if (this.x<z.x && this.y<z.y) return -1;
			if (Math.abs(z.x-this.x)<1.0E-8) return 0;
			return (this.x>z.x)?1:-1;
		}
		
		/** convert a complex number to a String. 
		 *  Complex z = new Complex(1.0,2.0);
		 *  System.out.println("z="+z); */
		public String toString(){
			if (y<0.0) {
				if (x==0) return this.y+"i";
				return this.x+"r"+this.y+"i";
			}
			if (this.y==0) return this.x+"r";
			return this.x+"r+"+this.y+"i";
		}
		
		/** convert text representation to a Complex.  
		 *  input format  real_float r+ imaginary_float i */
		public static final m_Complex constant(String s){
			int to,from = s.indexOf('r');
			float x=0.0f;
			if(from>0) {
				x = Float.parseFloat(s.substring(0,from));
				to = s.indexOf('+',from);
				if (to<0) to = s.indexOf('-',from);
				if (to>0) from=to;
			} else from=0;
			to = s.indexOf('i',from);
			float y=0.0f;
			if (to>0) y = Float.parseFloat(s.substring(from,to));
			else if (from==0) throw new NumberFormatException();
			return new m_Complex(x,y);
		}
		  public m_Complex parse(Scanner s) {
			  set(constant(s.next()));
			  return this;
		  }
		/** compute e to the power of the complex number */
		public m_Complex exp(){float exp_x=(float)StrictMath.exp(this.x);
			x=exp_x*(float)StrictMath.cos(this.y);
			y=exp_x*(float)StrictMath.sin(this.y);
			return this;
		}
		
		/** compute the natural logarithm of the complex number */
		public m_Complex log(){
			float rpart=(float)StrictMath.sqrt(this.x*this.x+this.y*this.y);
			y=(float)StrictMath.atan2(this.y,this.x);
			if(y>(float)StrictMath.PI) y=y-2.0f*(float)StrictMath.PI;
		    x=(float)StrictMath.log(rpart);
		    return this;
		}
		
		/** compute the square root of the complex number */
		public m_Complex sqrt(){
			float r=(float)StrictMath.sqrt(this.x*this.x+this.y*this.y);
			float rpart=(float)StrictMath.sqrt(0.5*(r+this.x));
			float ipart=(float)StrictMath.sqrt(0.5*(r-this.x));
			if(this.y<0.0) ipart=-ipart;
			x=rpart;   y=ipart;
			return this;
		}
		
		/** compute the complex number raised to the power z */
		public m_Complex pow(m_Complex z){
			this.log().multiply(z);
			return this.exp();
		}
		
		/** compute the complex number raised to the power float d */
		public m_Complex pow(float d){
			this.log().multiply(d);
			return this.exp();
		}
		
		/** compute the sin of the complex number */
		public m_Complex sin(){
			float r=(float)StrictMath.sin(this.x)*cosh(this.y);
			y=(float)StrictMath.cos(this.x)*sinh(this.y);
			x=r;
			return this;
		}
		
		/** compute the cosine of the complex number */
		public m_Complex cos(){
			float r=(float)StrictMath.cos(this.x)*cosh(this.y);
			y=(float)-StrictMath.sin(this.x)*sinh(this.y);
			x=r;
			return this;
		}
		
		/** compute the tangent of the complex number */
		public m_Complex tan(){tmp.set(this); tmp.cos(); return (this.sin()).divide(tmp);}
		
		/** compute the arctangent of a complex number */
		/*public Complex atan(){Complex IM = new Complex(0.0,-1.0);
			Complex ZP = new Complex(1.0+this.y,this.x);
			Complex ZM = new Complex(1.0-this.y,this.x);
			return IM.multiply(((ZP.log()).subtract(ZM.log())).divide(2.0));}*/
		public m_Complex atan(){ /*fixed RPC*/
			tmp.x=this.x;  tmp.y=this.y-1.0f;
			tmp2.x=-this.x;  tmp2.y=-this.y-1.0f;
			this.set(MINUS_I);
			return this.multiply( tmp.divide(tmp2).log()).divide(2.0f);
		}

		public m_Complex asin(){ /*added RPC*/
			tmp.set(this);
			tmp.multiply(MINUS_I);  //iz
			tmp2.set(1.0f,0.0f);
			tmp.add( tmp2.subtract(this.multiply(this)).sqrt() ); //sqrt(1-z^2)+iz
			this.x=tmp.x; this.y=tmp.y;
			return log().multiply(I);
		}
		public m_Complex acos(){ /*added RPC*/
			tmp.set(this);
			tmp.add((new m_Complex(1.0f,0.0f)).subtract(this.multiply(this)).sqrt().multiply(MINUS_I)); //z+i*sqrt(1-z^2)+iz
			this.x=tmp.x; this.y=tmp.y;
			return log().multiply(I);
		}
		
		/** compute the hyperbolic sin of the complex number */
		public m_Complex sinh(){
			float r=sinh(this.x)*(float)StrictMath.cos(this.y);
			y=cosh(this.x)*(float)StrictMath.sin(this.y);
			x=r;
			return this;
		}
		
		/** compute the hyperbolic cosine of the complex number */
		public m_Complex cosh(){
			float r=cosh(this.x)*(float)StrictMath.cos(this.y);
			y=sinh(this.x)*(float)StrictMath.sin(this.y);
			x=r;
			return this;
		}
		
		/** compute the hyperbolic tangent of the complex number */
		public m_Complex tanh(){
			tmp.set(this);
			return this.sinh().divide(tmp.cosh());
		}
		
		/** compute the inverse hyperbolic tangent of a complex number */
		public m_Complex atanh(){
			tmp.set(this);
			this.add(1.0f).log().subtract(
					tmp.subtract(1.0f).negate().log()).divide(2.0f);
			return this;
		}
		
		
		// local - should be a good implementation in StrictMath
		private float sinh(float x){return (float) ((float)(
											 StrictMath.exp(x)-StrictMath.exp(-x))/2.0);}
		private float cosh(float x){return (float) ((
									 StrictMath.exp(x)+StrictMath.exp(-x))/2.0);}

		@Override
		public double doubleValue() {
			return x;
		}

		@Override
		public float floatValue() {
			return x;
		}

		@Override
		public int intValue() {
			return (int) x;
		}

		@Override
		public long longValue() {
			return (long) x;
		}
		public int hashCode() {
		    return (int) (Float.floatToRawIntBits(x) ^ Float.floatToRawIntBits(y));
		}
		public static void main(String args[]) {
			m_Complex c=new m_Complex();
			System.out.println(c);
			m_Complex a=new m_Complex(3, 4);
			m_Complex b=new m_Complex(-4, 8);
			assert a.getReal()==3 && a.getImaginary()==4;
			assert constant("3r").add(constant("-4i")).equals(constant("3r-4i"));
			assert a.add(b).equals(constant("-1r+12i"));
			assert a.conjugate().equals(constant("-1r-12i"));
			assert a.add(9.3f).equals(constant("8.3r-12i"));
			assert a.subtract(b).equals(constant("12.3r-20i"));
			assert a.subtract(8).equals(constant("4.3r-20i"));
			assert a.multiply(3).equals(constant("12.9r-60i"));
			assert a.divide(3).equals(constant("4.3r-20i"));
			assert constant("3r+4i").magnitude()==5;
			assert constant("3r+4i").negate().equals(constant("-3r-4i"));
			assert constant("3r+4i").reciprocal().equals(constant("0.12r-0.16i"));
			System.out.println(constant("3r+4i").phase()+"==0.927295218 phase");
			assert constant("3r+4i").multiply(constant("-4r+5i")).equals(constant("-32r-1i"));
			System.out.println(constant("3r+4i").divide(constant("-4r+5i"))+"==0.195122r-0.756098i divides");
			System.out.println(constant("3r+4i").exp()+"==-13.128783r-15.200784i exp");
			System.out.println(constant("3r+4i").log()+"==1.6094379r+0.927295i log");
			assert constant("3r+4i").sqrt().equals(constant("2r+1i"));
			assert constant("3r+4i").pow(3).equals(constant("-117r+44i"));
			System.out.println(constant("3r+4i").sin()+"==3.853738r-27.016813i sin");
			System.out.println(constant("3r+4i").cos()+"==-27.0349456r-3.85115i cos");
			System.out.println(constant("3r+4i").tan()+"==0.000187346r+0.999356i tan");
			System.out.println(constant("3r+4i").asin()+"==0.6339839r+2.305509i asin");
			System.out.println(constant("3r+4i").acos()+"==0.9368r-2.305509i acos");
			System.out.println(constant("3r+4i").atan()+"==1.4483r+0.158997i atan");
			System.out.println(constant("3r+4i").sinh()+"==-6.54812r-7.61923i sinh");
			System.out.println(constant("3r+4i").cosh()+"==-6.58066r-7.58155i cosh");
			System.out.println(constant("3r+4i").tanh()+"==1.0007r+0.004908i tanh");
			System.out.println(constant("3r+4i").atanh()+"==0.1175r+1.40992i atanh");
			System.out.println(a.parse(new Scanner("-3.51e+1r")));
		}
	}