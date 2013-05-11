package com.software.reuze;
import java.util.Iterator;


public class gb_Vertex3DIndices implements Comparable<gb_Vertex3DIndices>, Iterable<gb_Vertex3DIndex>{
    public int[]        vData;   //indices into v t n arrays
    public int[]        tData;
    public int[]        nData;
    public static final char INPUT_DELIMITER=' ';
    public gb_Vertex3DIndices(int count, boolean textures, boolean normals) {
    	assert count>0;
    	vData=new int[count];
    	if (textures) tData=new int[count];
    	if (normals) nData=new int[count];
    }
    public boolean equals(Object o) {
    	if (o==null) return false;
    	return vData==((gb_Vertex3DIndices)o).vData && tData==((gb_Vertex3DIndices)o).tData
    			&& nData==((gb_Vertex3DIndices)o).nData;
    }
    public int hashCode() {
    	int j=0;
    	for (int i:vData) j ^= vData[i];
    	if (tData!=null) j ^= tData[0];
    	if (nData!=null) j ^= nData[0];
    	return j;
    }
	public int compareTo(gb_Vertex3DIndices vo) {
		if (equals(vo)) return 0;
		int j=Math.min(vData.length, vo.vData.length);
		for (int i=0; i<j; i++)
			if (vData[i]!=vo.vData[i]) return (vData[i]>vo.vData[i])?1:-1;
		if (vData.length == vo.vData.length) return 0;
		return (vData.length > vo.vData.length)?1:-1;
	}
	private StringBuilder sb=new StringBuilder(64);
	public String toString() {
		sb.delete(0,1000);
		sb.append(' ');
		sb.append(vData.length); sb.append(',');
		sb.append(tData!=null); sb.append(',');
		sb.append(nData!=null); sb.append(',');
		for (int i:vData) {sb.append(i); sb.append(',');}
		if (tData!=null) for (int i:tData) {sb.append(i); sb.append(',');}
		if (nData!=null) for (int i:nData) {sb.append(i); sb.append(',');}
		sb.append(' ');
		return sb.toString();
	}
	public int[] getvData() {
		if (tData!=null) assert tData.length==vData.length;
		if (nData!=null) assert nData.length==vData.length;
		return vData;
	}
	public void setvData(int[] vData) {
		assert vData!=null;
		this.vData = vData;
	}
	public int[] gettData() {
		if (tData!=null) assert tData.length==vData.length;
		if (nData!=null) assert nData.length==vData.length;
		return tData;
	}
	public void settData(int[] tData) {
		this.tData = tData;
	}
	public int[] getnData() {
		if (tData!=null) assert tData.length==vData.length;
		if (nData!=null) assert nData.length==vData.length;
		return nData;
	}
	public void setnData(int[] nData) {
		this.nData = nData;
	}
	private static gb_Vertex3DIndex tmp=new gb_Vertex3DIndex(-1, 0, 0);
	public gb_Vertex3DIndex getVertex3DIndex(int i) {
		tmp.set(vData[i], (tData!=null)?tData[i]:-1, (nData!=null)?nData[i]:-1);
		return tmp;
	}
	public static void main(String args[]) {
		gb_Vertex3DIndices vi=new gb_Vertex3DIndices(3,false,false);
		System.out.println(vi);
	}
	private class it implements Iterator<gb_Vertex3DIndex> {
        int i=0;
		public boolean hasNext() {
			return i<vData.length;
		}

		public gb_Vertex3DIndex next() {
			return getVertex3DIndex(i++);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	public Iterator<gb_Vertex3DIndex> iterator() {
		return new it();
	}
}
