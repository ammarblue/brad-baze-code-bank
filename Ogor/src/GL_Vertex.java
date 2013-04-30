/**
 * For each point in the model we store the texture coordinates and an index
 * value to an array of Vector that hold the XYZ coordinates.
 * 
 * @author Peter Lager
 * 
 */
public class GL_Vertex {
	public float s;
	public float t;
	public int idx;

	/**
	 * Parameter ctor
	 * 
	 * @param s
	 * @param t
	 * @param idx
	 */
	public GL_Vertex(float s, float t, int idx) {
		this.s = s;
		this.t = t;
		this.idx = idx;
	}

	public String toString() {
		return ("" + idx + "\t" + s + "\t" + t);
	}
}
