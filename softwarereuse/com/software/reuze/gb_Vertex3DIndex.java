package com.software.reuze;

public class gb_Vertex3DIndex {
	public int vertex, texture, normal;
	public static final char INPUT_DELIMITER=' ';
	public gb_Vertex3DIndex(int v, int t, int n) {
		vertex=v; texture=t; normal=n;
	}
	public gb_Vertex3DIndex(gb_Vertex3DIndex x) {
		vertex=x.vertex; texture=x.texture; normal=x.normal;
	}
	public void set(int v, int t, int n) {
		vertex=v; texture=t; normal=n;
	}
	public boolean equals(Object o) {
		if (o==null) return false;
		gb_Vertex3DIndex v=(gb_Vertex3DIndex)o;
		return v.vertex==vertex && v.texture==texture && v.normal==normal;
	}
	public String toString() {
		return vertex+","+texture+","+normal;
	}
}
