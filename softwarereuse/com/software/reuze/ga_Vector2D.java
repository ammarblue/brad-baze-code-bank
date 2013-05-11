package com.software.reuze;
import java.io.Serializable;
/**
 * 
 * @author Peter Lager
 *
 */
public class ga_Vector2D implements Serializable {


	/** Null vector (coordinates: 0, 0). */
	public static final ga_Vector2D ZERO   = new ga_Vector2D(0, 0);

	/** Null vector (coordinates: 1, 1). */
	public static final ga_Vector2D ONE   = new ga_Vector2D(1, 1);

	/** First canonical vector (coordinates: 1, 0). */
	public static final ga_Vector2D PLUS_I = new ga_Vector2D(1, 0);

	/** Opposite of the first canonical vector (coordinates: -1, 0). */
	public static final ga_Vector2D MINUS_I = new ga_Vector2D(-1, 0);

	/** Second canonical vector (coordinates: 0, 1). */
	public static final ga_Vector2D PLUS_J = new ga_Vector2D(0, 1);

	/** Opposite of the second canonical vector (coordinates: 0, -1). */
	public static final ga_Vector2D MINUS_J = new ga_Vector2D(0, -1);

	/** A vector with all coordinates set to NaN. */
	public static final ga_Vector2D NaN = new ga_Vector2D(Double.NaN, Double.NaN);

	/** A vector with all coordinates set to positive infinity. */
	public static final ga_Vector2D POSITIVE_INFINITY =
		new ga_Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

	/** A vector with all coordinates set to negative infinity. */
	public static final ga_Vector2D NEGATIVE_INFINITY =
		new ga_Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

	public static final int CLOCKWISE = 1;
	public static final int ANTI_CLOCKWISE = -1;

	public double x;
	public double y;


	/**
	 * Default to the zero vector
	 */
	public ga_Vector2D() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Create a vector based on parameter values.
	 * @param x
	 * @param y
	 */
	public ga_Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Copy constructor
	 * @param v the vector to copy
	 */
	public ga_Vector2D(final ga_Vector2D v){
		this.x = v.x;
		this.y = v.y;
	}

	public void set(final ga_Vector2D v){
		this.x = v.x;
		this.y = v.y;		
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Get the vector length squared
	 */
	public double lengthSq(){
		return x * x + y * y;
	}

	/**
	 * Get the vector length
	 */
	public double length(){
		return m_MathFast.sqrt(x * x + y * y);
	}

	/**
	 * Calculate the dot product between two un-normalized vectors.
	 * @param v the other vector
	 * @return the dot product
	 */
	public double dot(final ga_Vector2D v){
		return (x*v.x + y*v.y);
	}

	/**
	 * Calculate the dot product between two normalized vectors i.e. the cosine of the angle between them
	 * @param v
	 * @return
	 */
	public double dotNorm(final ga_Vector2D v){
		double denom = m_MathFast.sqrt(x * x + y * y) * m_MathFast.sqrt(v.x * v.x + v.y * v.y);
		return (x*v.x + y*v.y)/denom;
	}

	/**
	 * Calculate the angle between this and another vector.
	 * @param v the other vector
	 * @return the angle between in radians
	 */
	public double angleBetween(final ga_Vector2D v){
		double denom = m_MathFast.sqrt(x * x + y * y) * m_MathFast.sqrt(v.x * v.x + v.y * v.y);
		if(denom > Double.MIN_VALUE){
			double a = m_MathFast.acos((x*v.x + y*v.y) / denom);
			if( a != a ) // angle is NaN
				return 0;
			else
				return a;
		}
		return 0;		
	}

	/**
	 * Determines whether vector v is clockwise of this vector. <br>
	 * @param v a vector
	 * @return positive (+1) if clockwise else negative (-1)
	 */
	public int sign(final ga_Vector2D v){
		if(y*v.x > x*v.y)
			return CLOCKWISE;
		else
			return ANTI_CLOCKWISE;
	}

	/**
	 * Get a vector perpendicular to this one.
	 * @return a perpendicular vector
	 */
	public ga_Vector2D getPerp(){
		return new ga_Vector2D(-y, x);
	}

	/**
	 * Get the distance squared between this and an other
	 * point.
	 * @param v the other point
	 * @return distance to other point squared
	 */
	public double distanceSq(final ga_Vector2D v){
		double dx = v.x - x;
		double dy = v.y - y;
		return dx*dx + dy*dy;
	}

	/**
	 * Get the distance between this and an other point.
	 * @param v the other point
	 * @return distance to other point
	 */
	public double distance(final ga_Vector2D v){
		double dx = v.x - x;
		double dy = v.y - y;
		return m_MathFast.sqrt(dx*dx + dy*dy);
	}

	/**
	 * Normalize this vector
	 */
	public void normalize(){
		double mag = m_MathFast.sqrt(x * x + y * y);
		if(mag < Double.MIN_VALUE){
			x = y = 0.0;
		}
		else {
			x /= mag;
			y /= mag;
		}
	}

	/**
	 * Truncate this vector so its length is no greater than
	 * the value provided.
	 * @param max maximum size for this vector
	 */
	public void truncate(double max){
		double mag = m_MathFast.sqrt(x * x + y * y);
		if(mag > Double.MIN_VALUE && mag > max){
			double f = max / mag;
			x *= f;
			y *= f;			
		}
	}

	/**
	 * Get a vector that is the reverse of this vector
	 * @return the reverse vector
	 */
	public ga_Vector2D getReverse(){
		return new ga_Vector2D(-x, -y);
	}

	/**
	 * Return the reflection vector about the norm
	 * @param norm
	 * @return the reflected vector
	 */
	public ga_Vector2D getReflect(final ga_Vector2D norm){
		double dot = this.dot(norm);
		double nx = x + (-2 * dot * norm.x);
		double ny = y + (-2 * dot * norm.y);
		return new ga_Vector2D(nx, ny);
	}

	/**
	 * Add a vector to this one
	 * @param v the vector to add
	 */
	public void add(final ga_Vector2D v){
		x += v.x;
		y += v.y;
	}

	/**
	 * Change the vector by the values specified
	 * @param dx
	 * @param dy
	 */
	public void add(double dx, double dy){
		x += dx;
		y += dy;
	}

	public void sub(final ga_Vector2D v){
		x -= v.x;
		y -= v.y;
	}

	/**
	 * Multiply the vector by a scalar
	 * @param d
	 */
	public void mult(double d){
		x *= d;
		y *= d;
	}

	/**
	 * Divide the vector by a scalar
	 * @param d
	 */
	public void div(double d){
		x /= d;
		y /= d;
	}

	/**
	 * Get a new vector that is the sum of 2 vectors.
	 * @param v0 first vector
	 * @param v1 second vector
	 * @return the sum of the 2 vectors
	 */
	public static ga_Vector2D add(final ga_Vector2D v0, final ga_Vector2D v1){
		return new ga_Vector2D(v0.x + v1.x, v0.y + v1.y);
	}

	/**
	 * Get a new vector that is the difference between the
	 * 2 vectors.
	 * @param v0 first vector
	 * @param v1 second vector
	 * @return the difference between the 2 vectors
	 */
	public static ga_Vector2D sub(final ga_Vector2D v0, final ga_Vector2D v1){
		return new ga_Vector2D(v0.x - v1.x, v0.y - v1.y);
	}

	/**
	 * Get a new vector that is the product of a vector and a scalar
	 * @param v the original vector
	 * @param d the multiplier
	 * @return the calculated vector
	 */
	public static ga_Vector2D mult(final ga_Vector2D v, double d){
		return new ga_Vector2D(v.x * d, v.y * d);
	}

	/**
	 * Get a new vector that is a vector divided by a scalar
	 * @param v the original vector
	 * @param d the divisor
	 * @return the calculated vector
	 */
	public static ga_Vector2D div(final ga_Vector2D v, double d){
		return new ga_Vector2D(v.x / d, v.y / d);
	}

	public static double distSq(final ga_Vector2D v0, final ga_Vector2D v1){
		double dx = v1.x - v0.x;
		double dy = v1.y - v0.y;
		return dx*dx + dy*dy;
	}

	public static double dist(final ga_Vector2D v0, final ga_Vector2D v1){
		double dx = v1.x - v0.x;
		double dy = v1.y - v0.y;
		return m_MathFast.sqrt(dx*dx + dy*dy);
	}

	/**
	 * Get a new vector that is the given vector normalized
	 * @param v the original vector
	 * @return the normalized vector
	 */
	public static ga_Vector2D normalize(final ga_Vector2D v){
		ga_Vector2D n;
		double mag = v.length();
		if(mag < Double.MIN_VALUE)
			n = new ga_Vector2D();
		else
			n = new ga_Vector2D(v.x / mag, v.y / mag);
		return n;
	}
	
	/**
	 * Calculate the angle between two vectors.
	 * @param v0 first vector
	 * @param v1 second vector
	 * @return the angle between in radians
	 */
	public static double angleBetween(ga_Vector2D v0, ga_Vector2D v1){
		double denom = m_MathFast.sqrt(v0.x * v0.x + v0.y * v0.y) * m_MathFast.sqrt(v1.x * v1.x + v1.y * v1.y);
		if(denom > Double.MIN_VALUE){
			double a = m_MathFast.acos((v0.x*v1.x + v0.y*v1.y) / denom);
			if( a != a ) // angle is NaN
				return 0;
			else
				return a;
		}
		return 0;		
	}

	/**
	 * Determines whether entity 2 is visible from entity 1.
	 * 
	 * @param posFirst position of first entity
	 * @param facingFirst direction first entity is facing
	 * @param posSecond position of second entity
	 * @param fov field of view (radians)
	 * @return
	 */
	public static boolean isSecondInFOVofFirst(final ga_Vector2D posFirst, final ga_Vector2D facingFirst, final ga_Vector2D posSecond, double fov){
		ga_Vector2D toTarget = ga_Vector2D.sub(posSecond, posFirst);
		double dd = toTarget.length() * facingFirst.length();
		double angle = facingFirst.dot(toTarget) / dd;
		return angle >= m_MathFast.cos(fov / 2);
	}@Override
	public boolean equals(Object o) {
		ga_Vector2D v=(ga_Vector2D)o;
		return v.x==x && v.y==y;
	}
	/**
	 * Get the coordinates as an array.
	 * 
	 * @return
	 */
	public double[] toArray(){
		return new double[] {x, y};
	}

	@Override
	public String toString() {
		return "[ " + x + ", " + y + " ]";
	}

}
