package com.software.reuze;
import com.software.reuze.gb_Ray;
import com.software.reuze.gb_Sphere;
import com.software.reuze.gb_Triangle;
import com.software.reuze.gb_TriangleMesh;
import com.software.reuze.gb_i_Mesh;

public class gb_AABB3 {

    //@XmlElement(required = true)
    public gb_Vector3 position,  //center of the box
    					extent;     //edgeWidth/2 in X Y Z direction

    //@XmlTransient
    protected gb_Vector3 min, max;

    public gb_AABB3() {
        position =  new gb_Vector3();
        extent = new gb_Vector3();
    }

    /**
     * Creates an independent copy of the passed in box
     * 
     * @param box
     */
    public gb_AABB3(gb_AABB3 box) {
        this(box.position.cpy(), box.extent.cpy());  //RPC1012
    }

    /**
     * Creates a new box of the given size at the world origin.
     * 
     * @param extent
     */
    public gb_AABB3(float extent) {
        this(new gb_Vector3(), extent);
    }

    /**
     * Creates a new instance from center point and uniform extent in all
     * directions.
     * 
     * @param pos
     * @param extent
     */
    public gb_AABB3(final gb_Vector3 pos, float extent) {
        position=pos.cpy();
        this.extent=new gb_Vector3(extent, extent, extent);
    }

    /**
     * Creates a new instance from center point and extent
     * 
     * @param pos
     * @param extent
     *            box dimensions (the box will be double the extent in width)
     */
    public gb_AABB3(final gb_Vector3 pos, final gb_Vector3 extent) {
    	position=pos.cpy();
        setExtent(extent.cpy());  //RPC1012
    }

    public boolean contains(final gb_Vector3 p) {
        return isInAABBMinMax(p);
    }

    public gb_AABB3 copy() {
        return new gb_AABB3(this);
    }

    /**
     * Returns the current box size as new Vector3 instance (updating this vector
     * will NOT update the box size! Use {@link #setExtent(gb_Vector3)} for
     * those purposes)
     * 
     * @return box size
     */
    public final gb_Vector3 getExtent() {
        return extent.cpy();
    }

    public final gb_Vector3 getMax() {
        if (max==null) updateBounds();   //RPC1012
        return max.cpy();
    }

    public final gb_Vector3 getMin() {
    	if (min==null) updateBounds();
        return min.cpy();
    }

    public gb_Vector3 getNormalForPoint(final gb_Vector3 v) {
        gb_Vector3 p = v.tmp2().sub(position);
        gb_Vector3 pabs = extent.tmp3().sub(p.tmp().abs());
        gb_Vector3 psign = p.tmp().signum();
        gb_Vector3 normal;
        float minDist = pabs.x;
        if (pabs.y < minDist) {
            minDist = pabs.y;
            normal = gb_Vector3.Y.cpy().mul(psign.y);
        } else normal = gb_Vector3.X.cpy().mul(psign.x);
        if (pabs.z < minDist) {
            normal = gb_Vector3.Z.cpy().mul(psign.z);
        }
        return normal;
    }

    /**
     * Adjusts the box size and position such that it includes the given point.
     * 
     * @param p
     *            point to include
     * @return itself
     */
    public gb_AABB3 includePoint(final gb_Vector3 p) {
    	if (min==null) getMin();
        min.min(p);
        max.max(p);
        position.set(min.tmp().interpolate(max, 0.5f));
        extent.set(max.tmp().sub(min).mul(0.5f));
        return this;
    }

    /**
     * Checks if the box intersects the passed in one.
     * 
     * @param box
     *            box to check
     * @return true, if boxes overlap
     */
    public boolean intersectsBox(gb_AABB3 box) {
        gb_Vector3 t = box.position.tmp().sub(position);
        return m_MathUtils.abs(t.x) <= (extent.x + box.extent.x)
                && m_MathUtils.abs(t.y) <= (extent.y + box.extent.y)
                && m_MathUtils.abs(t.z) <= (extent.z + box.extent.z);
    }

    /**
     * Calculates intersection with the given ray between a certain distance
     * interval.
     * 
     * Ray-box intersection is using IEEE numerical properties to ensure the
     * test is both robust and efficient, as described in:
     * 
     * Amy Williams, Steve Barrus, R. Keith Morley, and Peter Shirley: "An
     * Efficient and Robust Ray-Box Intersection Algorithm" Journal of graphics
     * tools, 10(1):49-54, 2005
     * 
     * @param ray
     *            incident ray
     * @param minDist
     * @param maxDist
     * @return intersection point on the bounding box (only the first is
     *         returned) or null if no intersection
     */
    public gb_Vector3 intersectsRay(gb_Ray ray, float minDist, float maxDist) {
        gb_Vector3 invDir = ray.direction.tmp().reciprocal();
        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;
        gb_Vector3 bbox = signDirX ? getMax() : getMin();   //RPC1012
        float tmin = (bbox.x - ray.origin.x) * invDir.x;
        bbox = signDirX ? min : max;
        float tmax = (bbox.x - ray.origin.x) * invDir.x;
        bbox = signDirY ? max : min;
        float tymin = (bbox.y - ray.origin.y) * invDir.y;
        bbox = signDirY ? min : max;
        float tymax = (bbox.y - ray.origin.y) * invDir.y;
        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
        bbox = signDirZ ? max : min;
        float tzmin = (bbox.z - ray.origin.z) * invDir.z;
        bbox = signDirZ ? min : max;
        float tzmax = (bbox.z - ray.origin.z) * invDir.z;
        if ((tmin > tzmax) || (tzmin > tmax)) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if ((tmin < maxDist) && (tmax > minDist)) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }

    public boolean intersectsSphere(gb_Sphere s) {
        return intersectsSphere(s.center, s.radius);
    }

    /**
     * @param c
     *            sphere center
     * @param r
     *            sphere radius
     * @return true, if AABB intersects with sphere
     */
    public boolean intersectsSphere(gb_Vector3 c, float r) {
        float s, d = 0;
        // find the square of the distance
        // from the sphere to the box
        if (c.x < getMin().x) {    //RPC1012
            s = c.x - min.x;
            d = s * s;
        } else if (c.x > max.x) {
            s = c.x - max.x;
            d += s * s;
        }

        if (c.y < min.y) {
            s = c.y - min.y;
            d += s * s;
        } else if (c.y > max.y) {
            s = c.y - max.y;
            d += s * s;
        }

        if (c.z < min.z) {
            s = c.z - min.z;
            d += s * s;
        } else if (c.z > max.z) {
            s = c.z - max.z;
            d += s * s;
        }

        return d <= r * r;
    }

    public boolean intersectsTriangle(gb_Triangle tri) {
        // use separating axis theorem to test overlap between triangle and box
        // need to test for overlap in these directions:
        //
        // 1) the {x,y,z}-directions (actually, since we use the AABB of the
        // triangle
        // we do not even need to test these)
        // 2) normal of the triangle
        // 3) cross-product(edge from tri, {x,y,z}-direction)
        // this gives 3x3=9 more tests
        gb_Vector3 v0, v1, v2;
        gb_Vector3 normal, e0, e1, e2, f;

        // move everything so that the box center is in (0,0,0)
        v0 = tri.a.cpy().sub(position);
        v1 = tri.b.cpy().sub(position);
        v2 = tri.c.cpy().sub(position);

        // compute triangle edges
        e0 = v1.sub(v0);
        e1 = v2.sub(v1);
        e2 = v0.sub(v2);

        // test the 9 tests first (this was faster)
        f = e0.tmp().abs();
        if (testAxis(e0.z, -e0.y, f.z, f.y, v0.y, v0.z, v2.y, v2.z, extent.y,
                extent.z)) {
            return false;
        }
        if (testAxis(-e0.z, e0.x, f.z, f.x, v0.x, v0.z, v2.x, v2.z, extent.x,
                extent.z)) {
            return false;
        }
        if (testAxis(e0.y, -e0.x, f.y, f.x, v1.x, v1.y, v2.x, v2.y, extent.x,
                extent.y)) {
            return false;
        }

        f = e1.tmp().abs();
        if (testAxis(e1.z, -e1.y, f.z, f.y, v0.y, v0.z, v2.y, v2.z, extent.y,
                extent.z)) {
            return false;
        }
        if (testAxis(-e1.z, e1.x, f.z, f.x, v0.x, v0.z, v2.x, v2.z, extent.x,
                extent.z)) {
            return false;
        }
        if (testAxis(e1.y, -e1.x, f.y, f.x, v0.x, v0.y, v1.x, v1.y, extent.x,
                extent.y)) {
            return false;
        }

        f = e2.tmp().abs();
        if (testAxis(e2.z, -e2.y, f.z, f.y, v0.y, v0.z, v1.y, v1.z, extent.y,
                extent.z)) {
            return false;
        }
        if (testAxis(-e2.z, e2.x, f.z, f.x, v0.x, v0.z, v1.x, v1.z, extent.x,
                extent.z)) {
            return false;
        }
        if (testAxis(e2.y, -e2.x, f.y, f.x, v1.x, v1.y, v2.x, v2.y, extent.x,
                extent.y)) {
            return false;
        }

        // first test overlap in the {x,y,z}-directions
        // find min, max of the triangle each direction, and test for overlap in
        // that direction -- this is equivalent to testing a minimal AABB around
        // the triangle against the AABB

        // test in X-direction
        if (m_MathUtils.min(v0.x, v1.x, v2.x) > extent.x
                || m_MathUtils.max(v0.x, v1.x, v2.x) < -extent.x) {
            return false;
        }

        // test in Y-direction
        if (m_MathUtils.min(v0.y, v1.y, v2.y) > extent.y
                || m_MathUtils.max(v0.y, v1.y, v2.y) < -extent.y) {
            return false;
        }

        // test in Z-direction
        if (m_MathUtils.min(v0.z, v1.z, v2.z) > extent.z
                || m_MathUtils.max(v0.z, v1.z, v2.z) < -extent.z) {
            return false;
        }

        // test if the box intersects the plane of the triangle
        // compute plane equation of triangle: normal*x+d=0
        normal = e0.crs(e1);
        float d = -normal.dot(v0);
        if (!planeBoxOverlap(normal, d, extent)) {
            return false;
        }
        return true;
    }

    private boolean planeBoxOverlap(gb_Vector3 normal, float d, gb_Vector3 maxbox) {
        gb_Vector3 vmin = new gb_Vector3();
        gb_Vector3 vmax = new gb_Vector3();

        if (normal.x > 0.0f) {
            vmin.x = -maxbox.x;
            vmax.x = maxbox.x;
        } else {
            vmin.x = maxbox.x;
            vmax.x = -maxbox.x;
        }

        if (normal.y > 0.0f) {
            vmin.y = -maxbox.y;
            vmax.y = maxbox.y;
        } else {
            vmin.y = maxbox.y;
            vmax.y = -maxbox.y;
        }

        if (normal.z > 0.0f) {
            vmin.z = -maxbox.z;
            vmax.z = maxbox.z;
        } else {
            vmin.z = maxbox.z;
            vmax.z = -maxbox.z;
        }
        if (normal.dot(vmin) + d > 0.0f) {
            return false;
        }
        if (normal.dot(vmax) + d >= 0.0f) {
            return true;
        }
        return false;
    }

    public gb_AABB3 set(gb_AABB3 box) {
        extent.set(box.extent);
        min=null; max=null;      //RPC1012
        return set(box.position);
    }

    /**
     * Updates the position of the box in space
     * 
     * @see gb_Vector3.geom.Vector3#set(float, float, float)
     */
    public gb_Vector3 set(float x, float y, float z) {
    	position.x = x;
    	position.y = y;
    	position.z = z;
        min=null; max=null;     //RPC1012
        return position;
    }

    /**
     * Updates the position of the box in space
     * @see gb_Vector3.geom.Vector3#set(gb_Vector3.geom.Vector3)
     */
    public gb_AABB3 set(final gb_Vector3 v) {
    	position.x = v.x;
    	position.y = v.y;
    	position.z = v.z;
        min=null; max=null;   //RPC1012
        return this;
    }

    /**
     * Updates the size of the box and calls {@link #updateBounds()} immediately
     * 
     * @param extent
     *            new box size
     * @return itself, for method chaining
     */
    public gb_AABB3 setExtent(final gb_Vector3 extent) {
        this.extent = extent.cpy();
        min=null; max=null; //RPC1012
        return this;
    }

    private boolean testAxis(float a, float b, float fa, float fb, float va,
            float vb, float wa, float wb, float ea, float eb) {
        float p0 = a * va + b * vb;
        float p2 = a * wa + b * wb;
        float min, max;
        if (p0 < p2) {
            min = p0;
            max = p2;
        } else {
            min = p2;
            max = p0;
        }
        float rad = fa * ea + fb * eb;
        return (min > rad || max < -rad);
    }

    public gb_i_Mesh toMesh() {
        return toMesh(null);
    }

    public gb_i_Mesh toMesh(gb_i_Mesh mesh) {
        if (mesh == null) {
            mesh = new gb_TriangleMesh("aabb", 8, 12);
        }
        if (min==null) updateBounds();
        gb_Vector3 a = new gb_Vector3(min.x, max.y, max.z);
        gb_Vector3 b = new gb_Vector3(max.x, max.y, max.z);
        gb_Vector3 c = new gb_Vector3(max.x, min.y, max.z);
        gb_Vector3 d = new gb_Vector3(min.x, min.y, max.z);
        gb_Vector3 e = new gb_Vector3(min.x, max.y, min.z);
        gb_Vector3 f = new gb_Vector3(max.x, max.y, min.z);
        gb_Vector3 g = new gb_Vector3(max.x, min.y, min.z);
        gb_Vector3 h = new gb_Vector3(min.x, min.y, min.z);
        // front
        mesh.addFace(a, b, d, null, null, null, null);
        mesh.addFace(b, c, d, null, null, null, null);
        // back
        mesh.addFace(f, e, g, null, null, null, null);
        mesh.addFace(e, h, g, null, null, null, null);
        // top
        mesh.addFace(e, f, a, null, null, null, null);
        mesh.addFace(f, b, a, null, null, null, null);
        // bottom
        mesh.addFace(g, h, d, null, null, null, null);
        mesh.addFace(g, d, c, null, null, null, null);
        // left
        mesh.addFace(e, a, h, null, null, null, null);
        mesh.addFace(a, d, h, null, null, null, null);
        // right
        mesh.addFace(b, f, g, null, null, null, null);
        mesh.addFace(b, g, c, null, null, null, null);
        return mesh;
    }

    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.Vector3#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<aabb3> pos: ").append(position).append(" ext: ")
                .append(extent);
        return sb.toString();
    }

    /**
     * Updates the min/max corner points of the box. may be called after moving
     * the box in space by manipulating the public x,y,z coordinates directly.
     * 
     * @return itself
     */
    public final gb_AABB3 updateBounds() {
            this.min = position.cpy().sub(extent);
            this.max = position.cpy().add(extent);
        return this;
    }
    /**
     * Creates a new instance from two vectors specifying opposite corners of
     * the box
     * 
     * @param min
     *            first corner point
     * @param max
     *            second corner point
     * @return new AABB with center at the half point between the 2 input
     *         vectors
     */
    public static gb_AABB3 fromMinMax(final gb_Vector3 min, final gb_Vector3 max) {
    	gb_Vector3 a = min.tmp().min(max);
    	gb_Vector3 b = max.tmp2().max(min).sub(a).mul(0.5f);
        //return new gb_AABB3(a.interpolate(b, 0.5f), b);
    	return new gb_AABB3(a.add(b), b);
    }
    /*
     * (non-Javadoc)
     * 
     * @see toxi.geom.ReadonlyVec3D#isInAABB(toxi.geom.AABB)
     */
    public boolean isInAABBMinMax(final gb_Vector3 p) {
        if (p.x < getMin().x || p.x > max.x) {
            return false;
        }
        if (p.y < min.y || p.y > max.y) {
            return false;
        }
        if (p.z < min.z || p.z > max.z) {
            return false;
        }
        return true;
    }

    public boolean isInAABB(final gb_Vector3 p) {
        float w = extent.x;
        if (p.x < position.x - w || p.x > position.x + w) {
            return false;
        }
        w = extent.y;
        if (p.y < position.y - w || p.y > position.y + w) {
            return false;
        }
        w = extent.z;
        if (p.z < position.z - w || p.z > position.z + w) {
            return false;
        }
        return true;
    }
    /**
     * Forcefully fits the vector in the given AABB.
     * 
     * @param box
     *            the box
     * 
     * @return itself
     */
    public gb_Vector3 constrain(gb_Vector3 v) {
        return v.constrain(getMin(), getMax());
    }
}
