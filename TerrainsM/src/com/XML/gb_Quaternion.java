package com.XML;




import java.io.Serializable;

/**
 * A simple quaternion class. See http://en.wikipedia.org/wiki/Quaternion for more information.
 * 
 * @author badlogicgames@gmail.com
 * @author vesuvio
 * 
 */
public class gb_Quaternion implements Serializable {
	private static final long serialVersionUID = -7661875440774897168L;
	private static final float NORMALIZATION_TOLERANCE = 0.00001f;			
	private static gb_Quaternion tmp1 = new gb_Quaternion(0,0,0,0);
	private static gb_Quaternion tmp2 = new gb_Quaternion(0,0,0,0);	
	
	public float x;
	public float y;
	public float z;
	public float w;

	/**
	 * Constructor, sets the four components of the quaternion.
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 * @param w The w-component
	 */
	public gb_Quaternion (float x, float y, float z, float w) {
		this.set(x, y, z, w);
	}

	gb_Quaternion () {

	}

	/**
	 * Constructor, sets the quaternion components from the given quaternion.
	 * 
	 * @param quaternion The quaternion to copy.
	 */
	public gb_Quaternion (gb_Quaternion quaternion) {
		this.set(quaternion);
	}
	public gb_Quaternion (float w, gb_Vector3 p) {
		x=p.x; y=p.y; z=p.z; this.w=w;
	}

	/**
	 * Constructor, sets the quaternion from the given axis vector and the angle around that axis in degrees.
	 * 
	 * @param axis The axis
	 * @param angle The angle in degrees.
	 */
	public gb_Quaternion (gb_Vector3 axis, float angle) {
		this.set(axis, angle);
	}

	/**
	 * Sets the components of the quaternion
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 * @param w The w-component
	 * @return This quaternion for chaining
	 */
	public gb_Quaternion set (float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	/**
	 * Sets the quaternion components from the given quaternion.
	 * @param quaternion The quaternion.
	 * @return This quaternion for chaining.
	 */
	public gb_Quaternion set (gb_Quaternion quaternion) {
		return this.set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
	}

	/**
	 * Sets the quaternion components from the given axis and angle around that axis.
	 * 
	 * @param axis The axis
	 * @param angle The angle in degrees
	 * @return This quaternion for chaining.
	 */
	public gb_Quaternion set (gb_Vector3 axis, float angle) {
		float l_ang = (float)Math.toRadians(angle);
		float l_sin = (float)Math.sin(l_ang / 2);
		float l_cos = (float)Math.cos(l_ang / 2);
		return this.set(axis.x * l_sin, axis.y * l_sin, axis.z * l_sin, l_cos).nor();
	}

	/**
	 * @return a copy of this quaternion
	 */
	public gb_Quaternion cpy () {
		return new gb_Quaternion(this);
	}

	/**
	 * @return the euclidian length of this quaternion
	 */
	public float len () {
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString () {
		return "[" + x + "|" + y + "|" + z + "|" + w + "]";
	}

	/**
	 * Sets the quaternion to the given euler angles.
	 * @param yaw the yaw in degrees
	 * @param pitch the pitch in degress
	 * @param roll the roll in degess
	 * @return this quaternion
	 */
	public gb_Quaternion setEulerAngles (float yaw, float pitch, float roll) {
		yaw = (float)Math.toRadians(yaw);
		pitch = (float)Math.toRadians(pitch);
		roll = (float)Math.toRadians(roll);
		float num9 = roll * 0.5f;
		float num6 = (float)Math.sin(num9);
		float num5 = (float)Math.cos(num9);
		float num8 = pitch * 0.5f;
		float num4 = (float)Math.sin(num8);
		float num3 = (float)Math.cos(num8);
		float num7 = yaw * 0.5f;
		float num2 = (float)Math.sin(num7);
		float num = (float)Math.cos(num7);
		x = ((num * num4) * num5) + ((num2 * num3) * num6);
		y = ((num2 * num3) * num5) - ((num * num4) * num6);
		z = ((num * num3) * num6) - ((num2 * num4) * num5);
		w = ((num * num3) * num5) + ((num2 * num4) * num6);
		return this;
	}
	
	/**
     * Creates a Quaternion from a axis and a angle.
     * 
     * @param axis
     *            axis vector (will be normalized)
     * @param angle
     *            angle in radians.
     * 
     * @return new quaternion
     */
    public static gb_Quaternion createFromAxisAngle(final gb_Vector3 axis, float angle) {
        angle *= 0.5;
        float sin = m_MathUtils.sin(angle);
        float cos = m_MathUtils.cos(angle);
        gb_Quaternion q = new gb_Quaternion(cos, axis.cpy().norTo(sin));
        return q;
    }
    
	/**
	 * @return the length of this quaternion without square root
	 */
	public float len2() {
		return x * x + y * y + z * z + w * w;
	}

	/**
	 * Normalizes this quaternion to unit length
	 * @return the quaternion for chaining
	 */
	public gb_Quaternion nor() {
		float len = len2();
		if(len != 0.f &&
		   (Math.abs(len - 1.0f) > NORMALIZATION_TOLERANCE)) {
			len = (float) Math.sqrt(len);
			w /= len;
			x /= len;
			y /= len;
			z /= len;
		}
		return this;
	}
	public gb_Quaternion invert () {
		float d = len2();
		x = x / d;
		y = y / d;
		z = z / d;
		w = w / d;
		return this;
	}
	/**
	 * Conjugate the quaternion.
	 *
	 * @return This quaternion for chaining
	 */
	public gb_Quaternion conjugate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	public gb_Vector3 rotateVector (gb_Vector3 vec /*inout*/) {
		tmp1.x = -x;
		tmp1.y = -y;
		tmp1.z = -z;
		tmp1.w = w;

// inv.normalize();
		tmp2.set(this);
		tmp2.mul(vec);
		tmp2.mul(tmp1);

		vec.x = tmp2.x;
		vec.y = tmp2.y;
		vec.z = tmp2.z;
		return vec;
	}
	//TODO : this would better fit into the vector3 class
	/**
	 * Transforms the given vector using this quaternion
	 *
	 * @param v Vector to transform
	 */
	public gb_Vector3 transform(gb_Vector3 v) {
		tmp2.set(this);
		tmp2.conjugate();
		tmp2.mulLeft(tmp1.set(v.x, v.y, v.z, 0)).mulLeft(this);

		v.x = tmp2.x;
		v.y = tmp2.y;
		v.z = tmp2.z;
		return v;
	}
	public gb_Quaternion mul (gb_Vector3 v) {
		float tw = -x * v.x - y * v.y - z * v.z;
		float tx = w * v.x + y * v.z - z * v.y;
		float ty = w * v.y + z * v.x - x * v.z;
		float tz = w * v.z + x * v.y - y * v.x;

		w = tw;
		x = tx;
		y = ty;
		z = tz;
		return this;
	}
	/**
	 * Multiplies this quaternion with another one
	 *
	 * @param q Quaternion to multiply with
	 * @return This quaternion for chaining
	 */
	public gb_Quaternion mul(gb_Quaternion q) {
		float newX = w * q.x + x * q.w + y * q.z - z * q.y;
		float newY = w * q.y + y * q.w + z * q.x - x * q.z;
		float newZ = w * q.z + z * q.w + x * q.y - y * q.x;
		float newW = w * q.w - x * q.x - y * q.y - z * q.z;
		x = newX;
		y = newY;
		z = newZ;
		w = newW;
		return this;
	}

	/**
	 * Multiplies this quaternion with another one in the form of q * this
	 *
	 * @param q Quaternion to multiply with
	 * @return This quaternion for chaining
	 */
	public gb_Quaternion mulLeft(gb_Quaternion q) {
		float newX = q.w * x + q.x * w + q.y * z - q.z * y;
		float newY = q.w * y + q.y * w + q.z * x - q.x * z;
		float newZ = q.w * z + q.z * w + q.x * y - q.y * x;
		float newW = q.w * w - q.x * x - q.y * y - q.z * z;
		x = newX;
		y = newY;
		z = newZ;
		w = newW;
		return this;
	}

	//TODO : the matrix4 set(quaternion) doesn't set the last row+col of the matrix to 0,0,0,1 so... that's why there is this method
	/**
	 * Fills a 4x4 matrix with the rotation matrix represented by this quaternion.
	 *
	 * @param matrix Matrix to fill
	 */
	public void toFloats(float[] matrix) {
		float xx = x * x;
		float xy = x * y;
		float xz = x * z;
		float xw = x * w;
		float yy = y * y;
		float yz = y * z;
		float yw = y * w;
		float zz = z * z;
		float zw = z * w;
		// Set matrix from quaternion
		matrix[m_Matrix4.M00] = 1 - 2 * (yy + zz);
		matrix[m_Matrix4.M01] = 2 * (xy - zw);
		matrix[m_Matrix4.M02] = 2 * (xz + yw);
		matrix[m_Matrix4.M03] = 0;
		matrix[m_Matrix4.M10] = 2 * (xy + zw);
		matrix[m_Matrix4.M11] = 1 - 2 * (xx + zz);
		matrix[m_Matrix4.M12] = 2 * (yz - xw);
		matrix[m_Matrix4.M13] = 0;
		matrix[m_Matrix4.M20] = 2 * (xz - yw);
		matrix[m_Matrix4.M21] = 2 * (yz + xw);
		matrix[m_Matrix4.M22] = 1 - 2 * (xx + yy);
		matrix[m_Matrix4.M23] = 0;
		matrix[m_Matrix4.M30] = 0;
		matrix[m_Matrix4.M31] = 0;
		matrix[m_Matrix4.M32] = 0;
		matrix[m_Matrix4.M33] = 1;
	}

	/**
	 * Returns the identity quaternion x,y,z = 0 and w=1
	 *
	 * @return Identity quaternion
	 */
	public static gb_Quaternion idt() {
		return new gb_Quaternion(0, 0, 0, 1);
	}

	//TODO : the setFromAxis(v3,float) method should replace the set(v3,float) method
	/**
	 * Sets the quaternion components from the given axis and angle around that axis.
	 *
	 * @param axis The axis
	 * @param angle The angle in degrees
	 * @return This quaternion for chaining.
	 */
	public gb_Quaternion setFromAxis(gb_Vector3 axis, float angle) {
		return setFromAxis(axis.x, axis.y, axis.z, angle);
	}

	/**
	 * Sets the quaternion components from the given axis and angle around that axis.
	 *
	 * @param x X direction of the axis
	 * @param y Y direction of the axis
	 * @param z Z direction of the axis
	 * @param angle The angle in degrees
	 * @return This quaternion for chaining.
	 */
	public gb_Quaternion setFromAxis(float x, float y, float z, float angle) {
		float l_ang = angle * m_MathUtils.degreesToRadians;
		float l_sin = m_MathUtils.sin(l_ang / 2);
		float l_cos = m_MathUtils.cos(l_ang / 2);
		return this.set(x * l_sin, y * l_sin, z * l_sin, l_cos).nor();
	}
	
//	fromRotationMatrix(xAxis.x, yAxis.x, zAxis.x, xAxis.y, yAxis.y, zAxis.y,
//      xAxis.z, yAxis.z, zAxis.z);
	
//	final float m00, final float m01, final float m02, final float m10,
//  final float m11, final float m12, final float m20, final float m21, final float m22	
	
	public gb_Quaternion setFromMatrix(m_Matrix4 matrix) {
		return setFromAxes(matrix.val[m_Matrix4.M00], matrix.val[m_Matrix4.M01], matrix.val[m_Matrix4.M02], 
					   		 matrix.val[m_Matrix4.M10], matrix.val[m_Matrix4.M11], matrix.val[m_Matrix4.M12],
					   		 matrix.val[m_Matrix4.M20], matrix.val[m_Matrix4.M21], matrix.val[m_Matrix4.M22]);
	}
	
	/**
	 * <p>Sets the Quaternion from the given x-, y- and z-axis which have to be orthonormal.</p>
	 * 
	 * <p>Taken from Bones framework for JPCT, see http://www.aptalkarga.com/bones/ which 
	 * in turn took it from Graphics Gem code at ftp://ftp.cis.upenn.edu/pub/graphics/shoemake/quatut.ps.Z.</p>
	 * 
	 * @param xx x-axis x-coordinate
	 * @param xy x-axis y-coordinate
	 * @param xz x-axis z-coordinate
	 * @param yx y-axis x-coordinate
	 * @param yy y-axis y-coordinate
	 * @param yz y-axis z-coordinate
	 * @param zx z-axis x-coordinate
	 * @param zy z-axis y-coordinate
	 * @param zz z-axis z-coordinate
	 */
	public gb_Quaternion setFromAxes(float xx, float xy, float xz, 
											float yx, float yy, float yz, 
											float zx, float zy, float zz) {
      // the trace is the sum of the diagonal elements; see
      // http://mathworld.wolfram.com/MatrixTrace.html
		final float m00 = xx, m01 = yx, m02 = zx;
		final float m10 = xy, m11 = yy, m12 = zy;
		final float m20 = xz, m21 = yz, m22 = zz;
      final float t = m00 + m11 + m22;

      // we protect the division by s by ensuring that s>=1
      double x, y, z, w;
      if (t >= 0) { // |w| >= .5
          double s = Math.sqrt(t + 1); // |s|>=1 ...
          w = 0.5 * s;
          s = 0.5 / s; // so this division isn't bad
          x = (m21 - m12) * s;
          y = (m02 - m20) * s;
          z = (m10 - m01) * s;
      } else if ((m00 > m11) && (m00 > m22)) {
          double s = Math.sqrt(1.0 + m00 - m11 - m22); // |s|>=1
          x = s * 0.5; // |x| >= .5
          s = 0.5 / s;
          y = (m10 + m01) * s;
          z = (m02 + m20) * s;
          w = (m21 - m12) * s;
      } else if (m11 > m22) {
          double s = Math.sqrt(1.0 + m11 - m00 - m22); // |s|>=1
          y = s * 0.5; // |y| >= .5
          s = 0.5 / s;
          x = (m10 + m01) * s;
          z = (m21 + m12) * s;
          w = (m02 - m20) * s;
      } else {
          double s = Math.sqrt(1.0 + m22 - m00 - m11); // |s|>=1
          z = s * 0.5; // |z| >= .5
          s = 0.5 / s;
          x = (m02 + m20) * s;
          y = (m21 + m12) * s;
          w = (m10 - m01) * s;
      }

      return set((float)x, (float)y, (float)z, (float)w);
	}
	
	/**
	 * Spherical linear interpolation between this quaternion and the other
	 * quaternion, based on the alpha value in the range [0,1]. Taken
	 * from. Taken from Bones framework for JPCT, see http://www.aptalkarga.com/bones/ 
	 * @param end the end quaternion
	 * @param alpha alpha in the range [0,1]
	 * @return this quaternion for chaining
	 */
	public gb_Quaternion slerp(gb_Quaternion end, float alpha) {
      if (this.equals(end)) {         
         return this;
     }
      
     float result = dot(end);     

     if (result < 0.0) {
         // Negate the second quaternion and the result of the dot product
         end.mul(-1);
         result = -result;
     }

     // Set the first and second scale for the interpolation
     float scale0 = 1 - alpha;
     float scale1 = alpha;

     // Check if the angle between the 2 quaternions was big enough to
     // warrant such calculations
     if ((1 - result) > 0.1) {// Get the angle between the 2 quaternions,
         // and then store the sin() of that angle
         final double theta = Math.acos(result);
         final double invSinTheta = 1f / Math.sin(theta);

         // Calculate the scale for q1 and q2, according to the angle and
         // it's sine value
         scale0 = (float) (Math.sin((1 - alpha) * theta) * invSinTheta);
         scale1 = (float) (Math.sin((alpha * theta)) * invSinTheta);
     }

     // Calculate the x, y, z and w values for the quaternion by using a
     // special form of linear interpolation for quaternions.
     final float x = (scale0 * this.x) + (scale1 * end.x);
     final float y = (scale0 * this.y) + (scale1 * end.y);
     final float z = (scale0 * this.z) + (scale1 * end.z);
     final float w = (scale0 * this.w) + (scale1 * end.w);
     set(x, y, z, w);

     // Return the interpolated quaternion
     return this;
	}
	
   public boolean equals(final Object o) {
      if (this == o) {
          return true;
      }
      if (!(o instanceof gb_Quaternion)) {
          return false;
      }
      final gb_Quaternion comp = (gb_Quaternion) o;
      return this.x == comp.x && this.y == comp.y && this.z == comp.z && this.w == comp.w;

  }
   
   /**
    * Dot product between this and the other quaternion.
    * @param other the other quaternion.
    * @return this quaternion for chaining.
    */
   public float dot(gb_Quaternion other) {
   	return x * other.x + y * other.y + z * other.z + w * other.w;
   }
   
   /**
    * Multiplies the components of this quaternion with the
    * given scalar.
    * @param scalar the scalar.
    * @return this quaternion for chaining.
    */
   public gb_Quaternion mul(float scalar) {
   	this.x *= scalar;
   	this.y *= scalar;
   	this.z *= scalar;
   	this.w *= scalar;
   	return this;
   }
   /**
    * Constructs a quaternion that rotates the vector given by the "forward"
    * param into the direction given by the "dir" param.
    * 
    * @param dir
    * @param forward
    * @return quaternion
    */
   public static gb_Quaternion getAlignmentQuat(final gb_Vector3 dir, final gb_Vector3 forward) {
	   final gb_Vector3 target = dir.tmp().nor();
	   final gb_Vector3 axis = forward.tmp2().crs(target);
       float length = axis.len() + 0.0001f;
       float angle = (float) Math.atan2(length, forward.dot(target));
       return getAxisAngleQuat(axis, angle);
   }
   /**
    * Creates a Quaternion from a axis and a angle.
    * 
    * @param axis
    *            axis vector (will be normalized)
    * @param angle
    *            angle in radians.
    * 
    * @return new quaternion
    */
   public static gb_Quaternion getAxisAngleQuat(final gb_Vector3 axis, float angle) {
       angle *= 0.5;
       float sin = (float)Math.sin(angle);
       float cos = (float)Math.cos(angle);
       gb_Vector3 tmp=axis.tmp().norTo(sin);
       gb_Quaternion q = new gb_Quaternion(tmp.x, tmp.y, tmp.z, cos);
       return q;
   }
   /**
    * Converts the quat to a 4x4 rotation matrix (in row-major format). Assumes
    * the quat is currently normalized (if not, you'll need to call
    * {@link #normalize()} first).
    * 
    * @return result matrix
    */
   public m_Matrix4 toMatrix4x4() {
       return toMatrix4(new m_Matrix4());
   }

   public m_Matrix4 toMatrix4(m_Matrix4 result) {
       // Converts this quaternion to a rotation matrix.
       //
       // | 1 - 2(y^2 + z^2) 2(xy + wz) 2(xz - wy) 0 |
       // | 2(xy - wz) 1 - 2(x^2 + z^2) 2(yz + wx) 0 |
       // | 2(xz + wy) 2(yz - wx) 1 - 2(x^2 + y^2) 0 |
       // | 0 0 0 1 |

       float x2 = x + x;
       float y2 = y + y;
       float z2 = z + z;
       float xx = x * x2;
       float xy = x * y2;
       float xz = x * z2;
       float yy = y * y2;
       float yz = y * z2;
       float zz = z * z2;
       float wx = w * x2;
       float wy = w * y2;
       float wz = w * z2;

       return result.set(1 - (yy + zz), xy - wz, xz + wy, 0, xy + wz,
               1 - (xx + zz), yz - wx, 0, xz - wy, yz + wx, 1 - (xx + yy), 0,
               0, 0, 0, 1);
   }
   /**
    * Converts the quaternion into a float array consisting of: rotation angle
    * in radians, rotation axis x,y,z
    * 
    * @return 4-element float array
    */
   public float[] toAxisAngle() {
       float[] res = new float[4];
       float sa = (float) Math.sqrt(1.0f - w * w);
       if (sa < m_MathUtils.EPS) {
           sa = 1.0f;
       } else {
           sa = 1.0f / sa;
       }
       res[0] = (float) Math.acos(w) * 2.0f;
       res[1] = x * sa;
       res[2] = y * sa;
       res[3] = z * sa;
       return res;
   }
   /**
    * Creates a Quaternion from Euler angles.
    * 
    * @param pitch
    *            X-angle in radians.
    * @param yaw
    *            Y-angle in radians.
    * @param roll
    *            Z-angle in radians.
    * 
    * @return new quaternion
    */
   public static gb_Quaternion createFromEuler(float pitch, float yaw, float roll) {
       pitch *= 0.5;
       yaw *= 0.5;
       roll *= 0.5;
       float sinPitch = m_MathUtils.sin(pitch);
       float cosPitch = m_MathUtils.cos(pitch);
       float sinYaw = m_MathUtils.sin(yaw);
       float cosYaw = m_MathUtils.cos(yaw);
       float sinRoll = m_MathUtils.sin(roll);
       float cosRoll = m_MathUtils.cos(roll);
       float cosPitchCosYaw = cosPitch * cosYaw;
       float sinPitchSinYaw = sinPitch * sinYaw;

       gb_Quaternion q = new gb_Quaternion();

       q.x = sinRoll * cosPitchCosYaw - cosRoll * sinPitchSinYaw;
       q.y = cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw;
       q.z = cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw;
       q.w = cosRoll * cosPitchCosYaw + sinRoll * sinPitchSinYaw;

       // alternative solution from:
       // http://is.gd/6HdEB
       //
       // double c1 = Math.cos(yaw/2);
       // double s1 = Math.sin(yaw/2);
       // double c2 = Math.cos(pitch/2);
       // double s2 = Math.sin(pitch/2);
       // double c3 = Math.cos(roll/2);
       // double s3 = Math.sin(roll/2);
       // double c1c2 = c1*c2;
       // double s1s2 = s1*s2;
       // w =c1c2*c3 - s1s2*s3;
       // x =c1c2*s3 + s1s2*c3;
       // y =s1*c2*c3 + c1*s2*s3;
       // z =c1*s2*c3 - s1*c2*s3;

       return q;
   }
   
	public void computeW () {
		float t = 1.0f - x * x - y * y - z * z;

		if (t < 0.0f)
			w = 0;
		else
			w = -(float)Math.sqrt(t);
	}
	
    /**
     * Spherical interpolation to target quaternion (code ported from <a href=
     * "http://www.gamasutra.com/view/feature/3278/rotating_objects_using_quaternions.php"
     * >GamaSutra</a>)
     * 
     * @param target
     *            quaternion
     * @param t
     *            interpolation factor (0..1)
     * @return new interpolated quat
     */
    public gb_Quaternion interpolateTo(gb_Quaternion target, float t) {
        return cpy().interpolateToSelf(target, t);
    }

    /**
     * @param target
     * @param t
     * @param is
     * @return interpolated quaternion as new instance
     */
    public gb_Quaternion interpolateTo(gb_Quaternion target, float t,
            m_i_InterpolateValue is) {
        return cpy().interpolateToSelf(target, is.interpolate(0, 1, t));
    }

    /**
     * Spherical interpolation to target quaternion (code ported from <a href=
     * "http://www.gamasutra.com/view/feature/3278/rotating_objects_using_quaternions.php"
     * >GamaSutra</a>)
     * 
     * @param target
     *            quaternion
     * @param t
     *            interpolation factor (0..1)
     * @return new interpolated quat
     */
    public gb_Quaternion interpolateToSelf(gb_Quaternion target, double t) {
        double scale;
        double invscale;
        float dot = dot(target);
        double theta = Math.acos(dot);
        double sintheta = Math.sin(theta);
        if (sintheta > 0.001f) {
            scale = Math.sin(theta * (1.0 - t)) / sintheta;
            invscale = Math.sin(theta * t) / sintheta;
        } else {
            scale = 1 - t;
            invscale = t;
        }
        if (dot < 0) {
            w = (float) (scale * w - invscale * target.w);
            x = (float) (scale * x - invscale * target.x);
            y = (float) (scale * y - invscale * target.y);
            z = (float) (scale * z - invscale * target.z);
        } else {
            w = (float) (scale * w + invscale * target.w);
            x = (float) (scale * x + invscale * target.x);
            y = (float) (scale * y + invscale * target.y);
            z = (float) (scale * z + invscale * target.z);
        }
        return cpy().nor();
    }

    /**
     * Uses spherical interpolation to approach the target quaternion. The
     * interpolation factor is manipulated by the chosen
     * {@link InterpolateStrategy} first.
     * 
     * @param target
     * @param t
     * @param is
     * @return itself
     */
    public gb_Quaternion interpolateToSelf(gb_Quaternion target, float t,
            m_i_InterpolateValue is) {
        return interpolateToSelf(target, is.interpolate(0, 1, t));
    }
    public static void main(String args[]) {
    	gb_Quaternion a=new gb_Quaternion(0,1,1,0);
    	gb_Quaternion b=new gb_Quaternion(1,1,0,0);
    	for (float i=0; i<=1.04; i+=0.1f) {
    		System.out.println(a.interpolateTo(b, i));
    	}
    }
}
