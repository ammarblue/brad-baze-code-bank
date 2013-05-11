package com.software.reuze;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class dg_Card implements Comparable, Comparator<dg_Card> {
  public enum Number {Ace,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten,Jack,Queen,King,Joker};
  public enum Suit {Clubs, Diamonds, Hearts, Spades, Jokers};
  public static final String names[]=
  {"JK","AC","2C","3C","4C","5C","6C","7C","8C","9C","TC","JC","QC","KC",
	  "AD","2D","3D","4D","5D","6D","7D","8D","9D","TD","JD","QD","KD",
	  "AH","2H","3H","4H","5H","6H","7H","8H","9H","TH","JH","QH","KH",
	  "AS","2S","3S","4S","5S","6S","7S","8S","9S","TS","JS","QS","KS"
  };
public static final int FACEUP=0;
public static final int N_SUITS=4;
protected int value;  //16-bit properties, 16-bit value
protected static final Number valueName[]={Number.Ace,Number.Two,Number.Three,Number.Four,Number.Five,Number.Six,Number.Seven,Number.Eight,Number.Nine,Number.Ten,Number.Jack,Number.Queen,Number.King};
/* 0-joker, 1-13 club 14-26 diamond 27-39 heart 40-52 spade 1-ace ... 13-king */
public dg_Card(int value) {
	assert(value>=0 && value<=52);
	this.value = value;
}
public dg_Card(Number n, Suit s) {
	assert(!(n!=Number.Joker ^ s!=Suit.Jokers));
	if (s==Suit.Jokers) value=0;
	else value = (byte) (s.ordinal()*13+n.ordinal()+1);
}
public dg_Card(dg_Card c) {
  copy(c);
}
public void copy(dg_Card c) {
	value = c.value;
}
@Override public Object clone() {
	return new dg_Card(this);
}
public static Object create(dg_Card x) {
	dg_Card c=new dg_Card(0);
	c.value=x.value;
	return c;
}
public void set(int number /*1-13*/, int suit /*0-3*/) {
	value = suit*13+number;
}
public void set(int value) {
	assert(value>=0 && value<=52);
	this.value = value;
}
public boolean isProperty(int prop) {
	return (value&(1<<(prop+16)))!=0;
}
public void setProperty(int prop, boolean tru) {
	if (tru) value |= (1<<(prop+16));
	else value &= ~(1<<(prop+16));
}
public int getPoints() {
	if ((value&0xffff)==0) return 0;
	return ((value&0xffff)-1)%13+1;
}
@Override
public String toString() {
	return names[value&0xffff];
}
public static int countOnes(int x) {
	int j=0;
	for (int i=0; i<32; i++) {
		if (x==0) break;
		j += (x&1);
		x >>= 1;
	}
	return j;
	
}
public static String toString(String format, int properties) {
	int n = countOnes(properties);
		String s=n+",";
		if (n > 0) {
		  for (int i=0, j=0; i<=14; i++)
			if ((properties & (1<<i)) !=0) {s += ((j==0)?"":",")+i; j++;}
		  s+=",";
		}
	return s;
}
public static dg_Card constant(String s) {
	s=s.trim();
	for (int i=0; i<names.length; i++)
		if (s.equalsIgnoreCase(names[i])) {
			return new dg_Card(i);
		}
	return null;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (!(obj instanceof dg_Card))
		return false;
	final dg_Card other = (dg_Card) obj;
	return (value&0xffff) == (other.value&0xffff);
}
public boolean isSuit(Suit s) {
	return getSuit()==s;
}
public Suit getSuit() {
	if ((value&0xffff)==0) return Suit.Jokers;
	int c=((value&0xffff)-1)/13;
	return c==0?Suit.Clubs :c==1?Suit.Diamonds :c==2?Suit.Hearts:Suit.Spades;
}
public boolean isRed() {
	Suit s=getSuit();
	return (s==Suit.Diamonds) || (s==Suit.Hearts);
}
public Number getNumber() {
	if ((value&0xffff)==0) return Number.Joker;
	int c=((value&0xffff)-1)%13;
	return valueName[c];
}
public String toString(String format) {
	if (format.charAt(0)=='l') return getNumber()+" of "+getSuit();
	return toString()+","+toString("set", (value>>16)&0xffff);
}
public boolean parse(Scanner s) {
	Pattern p=s.delimiter();
	s.useDelimiter("\\W+");
	String st=s.next();
	dg_Card x=constant(st);
	if (x==null) return false;
	int n=s.nextInt();
	for (;n-->0&&s.hasNext();) {
		int i;
		st=s.next();
		i=Integer.parseInt(st);
		x.setProperty(i, true);
	}
	copy(x);
	s.useDelimiter(p);
	return true;
}
public static int parseProp(Scanner s) {
	Pattern p=s.delimiter();
	s.useDelimiter("\\W+");
	int n=s.nextInt(),i,x=0;
	for (;n-->0&&s.hasNext();) {
		i=s.nextInt();
		x |= (1<<i);
	}
	s.useDelimiter(p);
	return x;
}
public static String toString(int card) {
	if (card==0) return names[0];
	return names[card].substring(0,1);
	
}
public int compareTo(Object arg0) {
	if (arg0==null) return -1;
	if (!(arg0 instanceof dg_Card)) return -1;
	dg_Card c= (dg_Card)arg0;
	if ((c.value&0xffff) == (value&0xffff)) return 0;
	return (c.value&0xffff) < (value&0xffff) ? 1 : -1;
}
public int compare(dg_Card arg0, dg_Card arg1) {
	if (arg0==null && arg1==null) return 0;
	if (arg0==null) return 1;
	if (arg1==null) return -1;
	int i=arg0.getPoints(), j=arg1.getPoints();
	if (i==j) return 0;
	return (i<j)? -1 : 1;
}
public static void main(String[] args) {
  dg_Card c=new dg_Card(23);
  System.out.println(c);
  c = new dg_Card(Number.Five, Suit.Hearts);
  System.out.println(c);
  System.out.println(c.toString("long"));
  System.out.println(constant("9D"));
  System.out.println(c.parse(new Scanner("TH,3,5,7,11,")));
  System.out.println(c.toString("set"));
}
}
