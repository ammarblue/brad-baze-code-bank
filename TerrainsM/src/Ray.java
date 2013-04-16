import processing.core.PVector;


class Ray {
	/**
	 * 
	 */
	private final terrainmarch Ray;
	public PVector origin;
	public PVector direction;

	Ray(terrainmarch terrainmarch, PVector o, PVector d) {
		Ray = terrainmarch;
		origin = o;
		direction = d;
	}
}