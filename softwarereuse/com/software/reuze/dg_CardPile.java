package com.software.reuze;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Scanner;
import java.util.regex.Pattern;

public class dg_CardPile<T extends dg_Card> implements List<T>, Iterable<T>, RandomAccess {
	protected ArrayList<T> cards;
	protected short properties;
	public dg_CardPile() {}
	public dg_CardPile(T type) {
		int n=52;
		cards = new ArrayList<T>(n);
		properties = 0;
		for (int i=0; i<n; i++) {
			T x = (T) type.clone();
			x.set(i+1);
			cards.add(x);
		}
	}
	public dg_CardPile(T type, int n) {
		n = n<=0?1:n;
		cards = new ArrayList<T>(n);
		properties = 0;
	}
	public boolean isProperty(int prop) {
		if (prop<0 || prop>15) return false;
		return (properties&(1<<prop)) != 0;
	}
	public void setProperty(int prop, boolean tru) {
		assert (prop>=0 && prop<16);
		if (tru) properties |= (1<<(prop));
		else properties &= ~(1<<(prop));
	}
	public void shuffle(int seed) {
		final Random r=new Random(seed);
		T t=(T) cards.get(0).clone();
		for (int i=0; i<500; i++) {
			int j,k;
			j = r.nextInt(52);
			do {k=r.nextInt(52); } while (j==k);
			t.copy(cards.get(j));
			cards.get(j).copy(cards.get(k));
			cards.get(k).copy(t);
		}
	}
	public boolean parse(Scanner s) {
		String st;
		Pattern p=s.delimiter();
		s.useDelimiter("\\W+");
		int n=s.nextInt(),i;
		assert(n>=0);
		cards.clear();
		properties = 0;
		if (n==0) {s.useDelimiter(p); return true;}
		cards.ensureCapacity(n);
		for (i=0; i<n; i++) {
			st=s.next();
			dg_Card x=T.constant(st);
			cards.add((T) T.create(x));
		}
		properties=(short)T.parseProp(s);
		s.useDelimiter(p);
		return true;
	}
	@Override
	public String toString() {
		return ""+cards.size()+cards.toString()+dg_Card.toString("set", properties);
	}
	
	public static void main(String args[]) {
		dg_CardPile p=new dg_CardPile(new dg_Card(0));
		System.out.println(p);
		p.shuffle(131);
		System.out.println(p);
		p.parse(new Scanner("4[AC, 2C, 3C, 4C]2,5,14,"));
		System.out.println(p);
	}

	public void clear() {
		cards.clear();
	}
	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	public T get(int arg0) {
		return cards.get(arg0);
	}
	public int indexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	public boolean isEmpty() {
		return cards.isEmpty();
	}
	public Iterator<T> iterator() {
		return cards.iterator();
	}
	public int lastIndexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	public ListIterator<T> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	public ListIterator<T> listIterator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean remove(Object arg0) {
		return cards.remove(arg0);
	}
	public T remove(int arg0) {
		return cards.remove(arg0);
	}
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	public T set(int arg0, T arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	public int size() {
		return cards.size();
	}
	public List<T> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean add(T o) {
		return cards.add(o);
	}
	public void add(int index, T element) {
		cards.add(index, element);
	}
	public boolean addAll(Collection<? extends T> c) {
		return cards.addAll(((dg_CardPile)c).cards);
	}
	public boolean addAll(int index, Collection<? extends T> c) {
		return cards.addAll(index, ((dg_CardPile)c).cards);
	}
}
