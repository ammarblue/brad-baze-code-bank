/**
 * A very basic class to represent a 3D vector/scalar.
 * 
 * I have not used PVector since it has an additional attribute which means it
 * requires 16 bytes rather than the 12 bytes needed for this class. Might not
 * seem much but a MD2 model can require many thousands of these.
 * 
 * @author Peter Lager
 * 
 */
public class Vector3 {

	/** The x component of the vector. */
	public float x;

	/** The y component of the vector. */
	public float y;

	/** The z component of the vector. */
	public float z;

	/**
	 * Constructor for an empty vector: x, y, and z are set to 0.
	 */
	public Vector3() {
	}

	/**
	 * Constructor for a 3D vector.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the y coordinate.
	 */
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void subtract(Vector3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public void scale(float f) {
		x *= f;
		y *= f;
		z *= f;
	}

	/**
	 * get a string representing the vector.
	 */
	public String toString() {
		return "[ " + x + ", " + y + ", " + z + " ]";
	}

}
