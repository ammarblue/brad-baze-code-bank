package com.software.reuze;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.RandomAccess;

public class d_ArrayListFloat implements Serializable,Iterable<Float>,RandomAccess,Collection<Float> {
	public float values[];
	private int size=0;
	public static final char INPUT_DELIMITER='[';
	public d_ArrayListFloat() {values=new float[10];}
	public d_ArrayListFloat(int initialCapacity) {values=new float[initialCapacity]; }
	public d_ArrayListFloat(Collection<? extends Float> c) {
		values=new float[c.size()];
		addAll(c);
		size=c.size();
	}
	public d_ArrayListFloat(float... f) {
		values=new float[f.length];
		int i=0;
		for (float x:f) values[i++]=x;
		size=f.length;
	}
	public void set(int start, float... f) {
		int s=start+f.length;
		ensureCapacity(s);
		System.arraycopy(f, 0, values, start, f.length);
		if (s>size) size=s;
	}
	public boolean add(Float arg0) {
		if (size==values.length) ensureCapacity(size+1);
		values[size++]=arg0;
		return true;
	}
	private int lastIncrease=0;
	public void ensureCapacity(int minCapacity) {
		if (minCapacity>=lastIncrease) {
			lastIncrease=Math.max(values.length/3, 100)+values.length;
			float f[]=new float[Math.max(minCapacity, lastIncrease)];
			System.arraycopy(values, 0, f, 0, size);
			lastIncrease=f.length;
			values=f;
		}
	}
	public int add(float arg0) {
		if (size==values.length) ensureCapacity(size+1);
		values[size]=arg0;
		return size++;
	}
	public int add(float... array) {
		if ((size+array.length)>values.length) ensureCapacity(size+array.length);
		int j=size;
		for (int i=0; i<array.length; i++) values[size++]=array[i];
		return j;
	}
	public boolean addAll(Collection<? extends Float> arg0) {
		if ((size+arg0.size())>values.length) ensureCapacity(size+arg0.size());
		for (Float x:arg0) values[size++]=x;
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
		Float x[]=new Float[1];
		return (Object[])toArray(x);
	}

	public void toArray(float[] arg0) {
		if (arg0!=null && size<=arg0.length) {System.arraycopy(values,0,arg0,0,size);}
	}

	public boolean equals(Object o) {
		if (o==null) return false;
		d_ArrayListFloat x=(d_ArrayListFloat)o;
		if (x.size!=size) return false;
		for (int i=0; i<size; i++) if (values[i++]!=x.values[i]) return false;
		return true;
	}
	public int hashCode() {
		if (size==0) return 0;
		if (size==1) return Float.floatToRawIntBits(values[0]);
		return size ^ Float.floatToRawIntBits(values[0]) ^ Float.floatToRawIntBits(values[size-1]);
	}
	public <T> T[] toArray(T[] arg0) {
		Float[] tmp;
		if (arg0!=null && size<=arg0.length) tmp=(Float[])arg0;
		tmp=new Float[size];
		for (int i=0; i<size; i++) tmp[i]=new Float(values[i]);
		return (T[]) tmp;
	}
	public String toString() {
		trimToSize();
		return Arrays.toString(values);
	}
	public void trimToSize() {
		if (values.length>size) {
			float[] tmp=new float[size];
			for (int i=0; i<size; i++) tmp[i]=values[i];
			values=tmp;
			lastIncrease=size;
		}
	}
	public float get(int index) {
		if (index<0 || index>=size) throw new IndexOutOfBoundsException();
		return values[index];
	}
	public float set(int index, float element) {
		if (index<0 || index>=size) throw new IndexOutOfBoundsException();
		float tmp=values[index];
		values[index]=element;
		return tmp;
	}
	private class it implements Iterator<Float> {
        int i=0;
    	private Float temp=new Float(0);
		public boolean hasNext() {
			return i<size;
		}

		public Float next() {
			return values[i++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	public Iterator<Float> iterator() {
		return new it();
	}
	public static void main(String args[]) {
		d_ArrayListFloat f=new d_ArrayListFloat();
		f.add(3); f.add(4); f.add(0); f.set(2, 5);
		System.out.println(f.add(6,7,8));
		System.out.println(f);
		Float x[]=new Float[f.size()];
		for (int i=0; i<x.length; i++) x[i]=new Float(0);
		System.out.println(Arrays.toString(f.toArray(x)));
		for (Float ff:f) {System.out.print(ff); System.out.print(" ");}
		System.out.println();
		for (int i=0; i<10000; i++) f.add(0);
	}
}
