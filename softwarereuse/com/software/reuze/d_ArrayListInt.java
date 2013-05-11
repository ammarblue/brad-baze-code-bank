package com.software.reuze;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.RandomAccess;

public class d_ArrayListInt implements Serializable,Iterable<Integer>,RandomAccess,Collection<Integer> {
	public int values[];
	private int size=0;
	public static final char INPUT_DELIMITER='[';
	public d_ArrayListInt() {values=new int[10];}
	public d_ArrayListInt(int initialCapacity) {values=new int[initialCapacity]; }
	public d_ArrayListInt(Collection<? extends Integer> c) {
		values=new int[c.size()];
		addAll(c);
		size=c.size();
	}
	public d_ArrayListInt(int... f) {
		values=new int[f.length];
		int i=0;
		for (int x:f) values[i++]=x;
		size=f.length;
	}
	public void set(int start, int... f) {
		int s=start+f.length;
		ensureCapacity(s);
		System.arraycopy(f, 0, values, start, f.length);
		if (s>size) size=s;
	}
	public boolean add(Integer arg0) {
		if (size==values.length) ensureCapacity(size+1);
		values[size++]=arg0;
		return true;
	}
	private int lastIncrease=0;
	public void ensureCapacity(int minCapacity) {
		if (minCapacity>=lastIncrease) {
			lastIncrease=Math.max(values.length/3, 100)+values.length;
			int f[]=new int[Math.max(minCapacity, lastIncrease)];
			System.arraycopy(values, 0, f, 0, size);
			lastIncrease=f.length;
			values=f;
		}
	}
	public int add(int arg0) {
		if (size==values.length) ensureCapacity(size+1);
		values[size]=arg0;
		return size++;
	}
	public int add(int... array) {
		if ((size+array.length)>values.length) ensureCapacity(size+array.length);
		int j=size;
		for (int i=0; i<array.length; i++) values[size++]=array[i];
		return j;
	}
	public boolean addAll(Collection<? extends Integer> arg0) {
		if ((size+arg0.size())>values.length) ensureCapacity(size+arg0.size());
		for (Integer x:arg0) values[size++]=x;
		return false;
	}

	public void clear() {
		size=0;
		lastIncrease=0;
	}

	public boolean contains(Object arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {
		return size==0;
	}

	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return size;
	}

	public Object[] toArray() {
		Integer x[]=new Integer[1];
		return (Object[])toArray(x);
	}

	public void toArray(int[] arg0) {
		if (arg0!=null && size<=arg0.length) {System.arraycopy(values,0,arg0,0,size);}
	}

	public boolean equals(Object o) {
		if (o==null) return false;
		d_ArrayListInt x=(d_ArrayListInt)o;
		if (x.size!=size) return false;
		for (int i=0; i<size; i++) if (values[i++]!=x.values[i]) return false;
		return true;
	}
	public int hashCode() {
		if (size==0) return 0;
		if (size==1) return values[0];
		return size ^ values[0] ^ values[size-1];
	}
	public <T> T[] toArray(T[] arg0) {
		Integer[] tmp;
		if (arg0!=null && size<=arg0.length) tmp=(Integer[])arg0;
		tmp=new Integer[size];
		for (int i=0; i<size; i++) tmp[i]=new Integer(values[i]);
		return (T[]) tmp;
	}
	public String toString() {
		trimToSize();
		return Arrays.toString(values);
	}
	public void trimToSize() {
		if (values.length>size) {
			int[] tmp=new int[size];
			for (int i=0; i<size; i++) tmp[i]=values[i];
			values=tmp;
			lastIncrease=size;
		}
	}
	public int get(int index) {
		if (index<0 || index>=size) throw new IndexOutOfBoundsException();
		return values[index];
	}
	public int set(int index, int element) {
		if (index<0 || index>=size) throw new IndexOutOfBoundsException();
		int tmp=values[index];
		values[index]=element;
		return tmp;
	}
	private class it implements Iterator<Integer> {
        int i=0;
		public boolean hasNext() {
			return i<size;
		}

		public Integer next() {
			return values[i++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	public Iterator<Integer> iterator() {
		return new it();
	}
	public static void main(String args[]) {
		d_ArrayListInt f=new d_ArrayListInt();
		f.add(3); f.add(4); f.add(0); f.set(2, 5);
		System.out.println(f.add(6,7,8));
		System.out.println(f);
		Integer x[]=new Integer[f.size()];
		for (int i=0; i<x.length; i++) x[i]=new Integer(0);
		System.out.println(Arrays.toString(f.toArray(x)));
		for (Integer ff:f) {System.out.print(ff); System.out.print(" ");}
		System.out.println();
		for (int i=0; i<10000; i++) f.add(0);
	}
}
