package com.software.reuze;

import java.util.HashMap;

public class dg_NameSpace {
	final HashMap<Integer,Object> map;
	final HashMap<String,Integer> names;
	static int id;
	public dg_NameSpace() {
		map=new HashMap<Integer,Object>();
		names=new HashMap<String,Integer>();
	}
	public int add(String name) {
		Integer x=names.get(name);
		if (x!=null) return x;
		names.put(name, id++);
		return id-1;
	}
	public int id(String name) {
		final Integer i = names.get(name);
		return i==null?-1:i;
	}
	public boolean contains(String name) {
		return names.containsKey(name);
	}
	public void clear() {
		map.clear();
		names.clear();
	}
	public int put(String space, String name, int value) {
		Integer x=names.get(space);
		if (x==null) throw new RuntimeException(space+" not found");
		Integer y=names.get(name);
		if (y==null) y=add(name);
		Integer z=(Integer)map.put(x<<20+y, (float)value);
		return z==null?Integer.MIN_VALUE:z.intValue();
	}
	public float put(String space, String name, float value) {
		Integer x=names.get(space);
		if (x==null) throw new RuntimeException(space+" not found");
		Integer y=names.get(name);
		if (y==null) y=add(name);
		Float z=(Float)map.put(x<<20+y, value);
		return z==null?Float.MIN_VALUE:z;
	}
	public int put(String name, int value) {
		Integer y=names.get(name);
		if (y==null) y=add(name);
		Integer z=(Integer)map.put(0<<20+y, (float)value);
		return z==null?Integer.MIN_VALUE:z;
	}
	public float put(String name, float value) {
		Integer y=names.get(name);
		if (y==null) y=add(name);
		Float z=(Float)map.put(0<<20+y, value);
		return z==null?Float.MIN_VALUE:z;
	}
	public float getFloat(String space, String name) {
		Integer x=names.get(space);
		if (x==null) throw new RuntimeException(space+" not found");
		Integer y=names.get(name);
		if (y==null) throw new RuntimeException(name+" not found");
		Float z=(Float)map.get(x<<20+y);
		return z==null?Float.MIN_VALUE:z;
	}
	public float getFloat(String name) {
		Integer y=names.get(name);
		if (y==null) throw new RuntimeException(name+" not found");
		Float z=(Float)map.get(0<<20+y);
		return z==null?Float.MIN_VALUE:z;
	}
	public int getInt(String space, String name) {
		Integer x=names.get(space);
		if (x==null) throw new RuntimeException(space+" not found");
		Integer y=names.get(name);
		if (y==null) throw new RuntimeException(name+" not found");
		Integer z=(Integer)map.get(x<<20+y);
		return z==null?Integer.MIN_VALUE:z;
	}
	public int getInt(String name) {
		Integer y=names.get(name);
		if (y==null) throw new RuntimeException(name+" not found");
		Integer z=(Integer)map.get(0<<20+y);
		return z==null?Integer.MIN_VALUE:z;
	}
}
