package reuze.pending;
import java.util.ArrayList;
import java.util.List;

import com.software.reuze.gb_Axis;
import com.software.reuze.gb_Frustum;
import com.software.reuze.gb_Line3D;
import com.software.reuze.gb_Plane;
import com.software.reuze.gb_Vector3;


/*import gov.nasa.worldwind.View;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.*;

import javax.media.opengl.GL;
import javax.media.opengl.glu.*;
import java.util.*;*/

/**
 * Represents a geometric cylinder, most often used as a bounding volume. <code>Cylinder</code>s are immutable.
 *
 * @author Tom Gaskins
 * @version $Id$
 */
public class Cylinder {
    protected final gb_Vector3 bottomCenter; // point at center of cylinder base
    protected final gb_Vector3 topCenter; // point at center of cylinder top
    protected final gb_Vector3 axisUnitDirection; // axis as unit vector from bottomCenter to topCenter
    protected final double cylinderRadius;
    protected final double cylinderHeight;

    /**
     * Create a Cylinder from two points and a radius.
     *
     * @param bottomCenter   the center point of of the cylinder's base.
     * @param topCenter      the center point of the cylinders top.
     * @param cylinderRadius the cylinder's radius.
     *
     * @throws IllegalArgumentException if the radius is zero or the top or bottom point is null or they are
     *                                  coincident.
     */
    public Cylinder(gb_Vector3 bottomCenter, gb_Vector3 topCenter, double cylinderRadius)
    {
        if (bottomCenter == null || topCenter == null || bottomCenter.equals(topCenter))
        {
            String message =
                bottomCenter == null || topCenter == null ? "nullValue.EndPointIsNull" : "generic.EndPointsCoincident";
            throw new IllegalArgumentException(message);
        }

        if (cylinderRadius <= 0)
        {
            String message = "Geom.Cylinder.RadiusIsZeroOrNegative "+ cylinderRadius;
            throw new IllegalArgumentException(message);
        }

        // Convert the bottom center and top center points to points in four-dimensional homogeneous coordinates to
        // ensure that their w-coordinates are 1. Cylinder's intersection tests compute a dot product between these
        // points and each frustum plane, which depends on a w-coordinate of 1. We convert each point at construction to
        // avoid the additional overhead of converting them during every intersection test.
        this.bottomCenter = bottomCenter;
        this.topCenter = topCenter;
        this.cylinderHeight = this.bottomCenter.dst(this.topCenter);
        this.cylinderRadius = cylinderRadius;
        this.axisUnitDirection = this.topCenter.cpy().sub(this.bottomCenter).nor();
    }

    /**
     * Create a Cylinder from two points, a radius and an axis direction. Provided for use when unit axis is know and
     * computation of it can be avoided.
     *
     * @param bottomCenter   the center point of of the cylinder's base.
     * @param topCenter      the center point of the cylinders top.
     * @param cylinderRadius the cylinder's radius.
     * @param unitDirection  the unit-length axis of the cylinder.
     *
     * @throws IllegalArgumentException if the radius is zero or the top or bottom point is null or they are
     *                                  coincident.
     */
    public Cylinder(gb_Vector3 bottomCenter, gb_Vector3 topCenter, double cylinderRadius, gb_Vector3 unitDirection)
    {
        if (bottomCenter == null || topCenter == null || bottomCenter.equals(topCenter))
        {
            String message = 
                bottomCenter == null || topCenter == null ? "nullValue.EndPointIsNull" : "generic.EndPointsCoincident";
            throw new IllegalArgumentException(message);
        }

        if (cylinderRadius <= 0)
        {
            String message = "Geom.Cylinder.RadiusIsZeroOrNegative "+ cylinderRadius;
            throw new IllegalArgumentException(message);
        }

        // Convert the bottom center and top center points to points in four-dimensional homogeneous coordinates to
        // ensure that their w-coordinates are 1. Cylinder's intersection tests compute a dot product between these
        // points and each frustum plane, which depends on a w-coordinate of 1. We convert each point at construction to
        // avoid the additional overhead of converting them during every intersection test.
        this.bottomCenter = bottomCenter;
        this.topCenter = topCenter;
        this.cylinderHeight = this.bottomCenter.dst(this.topCenter);
        this.cylinderRadius = cylinderRadius;
        this.axisUnitDirection = unitDirection;
    }

    /**
     * Returns the unit-length axis of this cylinder.
     *
     * @return the unit-length axis of this cylinder.
     */
    public gb_Vector3 getAxisUnitDirection()
    {
        return axisUnitDirection;
    }

    /**
     * Returns the this cylinder's bottom-center point.
     *
     * @return this cylinder's bottom-center point.
     */
    public gb_Vector3 getBottomCenter()
    {
        return bottomCenter;
    }

    /**
     * Returns the this cylinder's top-center point.
     *
     * @return this cylinder's top-center point.
     */
    public gb_Vector3 getTopCenter()
    {
        return topCenter;
    }

    /**
     * Returns this cylinder's radius.
     *
     * @return this cylinder's radius.
     */
    public double getCylinderRadius()
    {
        return cylinderRadius;
    }

    /**
     * Returns this cylinder's height.
     *
     * @return this cylinder's height.
     */
    public double getCylinderHeight()
    {
        return cylinderHeight;
    }

    /**
     * Return this cylinder's center point.
     *
     * @return this cylinder's center point.
     */
    public gb_Vector3 getCenter()
    {
        gb_Vector3 b = this.bottomCenter;
        gb_Vector3 t = this.topCenter;
        return new gb_Vector3(
            (b.x + t.x) / 2.0,
            (b.y + t.y) / 2.0,
            (b.z + t.z) / 2.0);
    }

    /** {@inheritDoc} */
    public double getDiameter()
    {
        return 2 * this.getRadius();
    }

    /** {@inheritDoc} */
    public double getRadius()
    {
        // return the radius of the enclosing sphere
        double halfHeight = this.bottomCenter.dst(this.topCenter) / 2.0;
        return Math.sqrt(halfHeight * halfHeight + this.cylinderRadius * this.cylinderRadius);
    }

    /**
     * Return this cylinder's volume.
     *
     * @return this cylinder's volume.
     */
    public double getVolume()
    {
        return Math.PI * this.cylinderRadius * this.cylinderRadius * this.cylinderHeight;
    }

    /**
     * Compute a bounding cylinder for a collection of points.
     *
     * @param points the points to compute a bounding cylinder for.
     *
     * @return a cylinder bounding all the points. The axis of the cylinder is the longest principal axis of the
     *         collection. (See {@link WWMath#computePrincipalAxes(Iterable)}.
     *
     * @throws IllegalArgumentException if the point list is null or empty.
     * @see #computeVerticalBoundingCylinder(gov.nasa.worldwind.globes.Globe, double, Sector)
     */
    public static Cylinder computeBoundingCylinder(Iterable<? extends gb_Vector3> points)
    {
        if (points == null)
        {
            String message = "nullValue.PointListIsNull";
            throw new IllegalArgumentException(message);
        }

        gb_Vector3[] axes = gb_Axis.computePrincipalAxes(points);
        if (axes == null)
        {
            String message = "generic.ListIsEmpty";
            throw new IllegalArgumentException(message);
        }

        gb_Vector3 r = axes[0];
        gb_Vector3 s = axes[1];

        List<gb_Vector3> sPlanePoints = new ArrayList<gb_Vector3>();
        double minDotR = Double.MAX_VALUE;
        double maxDotR = -minDotR;

        for (gb_Vector3 p : points)
        {
            double pdr = p.dot(r);
            sPlanePoints.add(p.tmp2().sub(r.tmp().mul(p.dot(r))));

            if (pdr < minDotR)
                minDotR = pdr;
            if (pdr > maxDotR)
                maxDotR = pdr;
        }

        gb_Vector3 minPoint = sPlanePoints.get(0);
        gb_Vector3 maxPoint = minPoint;
        double minDotS = Double.MAX_VALUE;
        double maxDotS = -minDotS;
        for (gb_Vector3 p : sPlanePoints)
        {
            double d = p.dot(s);
            if (d < minDotS)
            {
                minPoint = p;
                minDotS = d;
            }
            if (d > maxDotS)
            {
                maxPoint = p;
                maxDotS = d;
            }
        }

        gb_Vector3 center = minPoint.cpy().add(maxPoint).div(2);
        float radius = center.dst(minPoint);

        for (gb_Vector3 h : sPlanePoints)
        {
            gb_Vector3 hq = h.tmp().sub(center);
            float d = hq.len();
            if (d > radius)
            {
                gb_Vector3 g = center.tmp().sub(hq.nor().mul(radius));
                center.set(g.add(h).div(2));
                radius = d;
            }
        }

        gb_Vector3 bottomCenter = center.cpy().add(r.tmp().mul((float)minDotR));
        gb_Vector3 topCenter = center.add((r.tmp().mul((float)maxDotR)));

        if (radius == 0)
            radius = 1;

        if (bottomCenter.equals(topCenter))
            topCenter.set(bottomCenter).add(new gb_Vector3(1, 0, 0));

        return new Cylinder(bottomCenter, topCenter, radius);
    }

    /** {@inheritDoc} */
/*TODO
    public Intersection[] intersect(gb_Line3D line)
    {
        if (line == null)
        {
            String message = "nullValue.LineIsNull";
            throw new IllegalArgumentException(message);
        }

        double[] tVals = new double[2];
        if (!intcyl(line.a, line.getDirection(), this.bottomCenter, this.axisUnitDirection,
            this.cylinderRadius, tVals))
            return null;

        if (!clipcyl(line.b, line.getDirection(), this.bottomCenter, this.topCenter,
            this.axisUnitDirection, tVals))
            return null;

        if (!Double.isInfinite(tVals[0]) && !Double.isInfinite(tVals[1]) && tVals[0] >= 0.0 && tVals[1] >= 0.0)
            return new Intersection[] {new Intersection(line.getPointAt(tVals[0]), false),
                new Intersection(line.getPointAt(tVals[1]), false)};
        if (!Double.isInfinite(tVals[0]) && tVals[0] >= 0.0)
            return new Intersection[] {new Intersection(line.getPointAt(tVals[0]), false)};
        if (!Double.isInfinite(tVals[1]) && tVals[1] >= 0.0)
            return new Intersection[] {new Intersection(line.getPointAt(tVals[1]), false)};
        return null;
    }

    public boolean intersects(gb_Line3D line)
    {
        if (line == null)
        {
            String message = "nullValue.LineIsNull";
            throw new IllegalArgumentException(message);
        }

        return intersect(line) != null;
    }
*/
    // Taken from "Graphics Gems IV", Section V.2, page 356.

    public static boolean intcyl(gb_Vector3 raybase, gb_Vector3 raycos, gb_Vector3 base, gb_Vector3 axis, double radius, double[] tVals/*out*/)
    {
        boolean hit; // True if ray intersects cyl
        gb_Vector3 RC; // Ray base to cylinder base
        double d; // Shortest distance between the ray and the cylinder
        double t, s; // Distances along the ray
        gb_Vector3 n, D, O;
        double ln;

        RC = raybase.cpy().sub(base);
        n = raycos.tmp().crs(axis);

        // Ray is parallel to the cylinder's axis.
        if ((ln = n.len()) == 0.0)
        {
            d = RC.dot(axis);
            D = RC.tmp2().sub(axis.tmp().mul((float)d));
            d = D.len();
            tVals[0] = Double.NEGATIVE_INFINITY;
            tVals[1] = Double.POSITIVE_INFINITY;
            // True if ray is in cylinder.
            return d <= radius;
        }

        n.nor();
        d = Math.abs(RC.dot(n)); // Shortest distance.
        hit = (d <= radius);

        // If ray hits cylinder.
        if (hit)
        {
            O = RC.crs(axis);
            t = -O.dot(n) / ln;
            O = n.crs(axis);
            O.nor();
            s = Math.abs(Math.sqrt(radius * radius - d * d) / raycos.dot(O));
            tVals[0] = t - s; // Entering distance.
            tVals[1] = t + s; // Exiting distance.
        }

        return hit;
    }

    // Taken from "Graphics Gems IV", Section V.2, page 356.

    public static boolean clipcyl(gb_Vector3 raybase, gb_Vector3 raycos, gb_Vector3 bot, gb_Vector3 top, gb_Vector3 axis, double[] tVals/*out*/)
    {
        double dc, dwb, dwt, tb, tt;
        double in, out; // Object intersection distances.

        in = tVals[0];
        out = tVals[1];

        dc = axis.dot(raycos);
        dwb = axis.dot(raybase) - axis.dot(bot);
        dwt = axis.dot(raybase) - axis.dot(top);

        // Ray is parallel to the cylinder end-caps.
        if (dc == 0.0)
        {
            if (dwb <= 0.0)
                return false;
            if (dwt >= 0.0)
                return false;
        }
        else
        {
            // Intersect the ray with the bottom end-cap.
            tb = -dwb / dc;
            // Intersect the ray with the top end-cap.
            tt = -dwt / dc;

            // Bottom is near cap, top is far cap.
            if (dc >= 0.0)
            {
                if (tb > out)
                    return false;
                if (tt < in)
                    return false;
                if (tb > in && tb < out)
                    in = tb;
                if (tt > in && tt < out)
                    out = tt;
            }
            // Bottom is far cap, top is near cap.
            else
            {
                if (tb < in)
                    return false;
                if (tt > out)
                    return false;
                if (tb > in && tb < out)
                    out = tb;
                if (tt > in && tt < out)
                    in = tt;
            }
        }

        tVals[0] = in;
        tVals[1] = out;
        return in < out;
    }

    protected double intersects(gb_Plane plane, double effectiveRadius)
    {
        // Test the distance from the first cylinder end-point. Assumes that bottomCenter's w-coordinate is 1.
        double dq1 = plane.position.dot(this.bottomCenter);
        boolean bq1 = dq1 <= -effectiveRadius;

        // Test the distance from the top of the cylinder. Assumes that topCenter's w-coordinate is 1.
        double dq2 = plane.position.dot(this.topCenter);
        boolean bq2 = dq2 <= -effectiveRadius;

        if (bq1 && bq2) // both beyond effective radius; cylinder is on negative side of plane
            return -1;

        if (bq1 == bq2) // both within effective radius; can't draw any conclusions
            return 0;

        return 1; // Cylinder almost certainly intersects
    }

    protected double intersectsAt(gb_Plane plane, double effectiveRadius, gb_Vector3[] endpoints)
    {
        // Test the distance from the first end-point. Assumes that the first end-point's w-coordinate is 1.
        double dq1 = plane.position.dot(endpoints[0]);
        boolean bq1 = dq1 <= -effectiveRadius;

        // Test the distance from the possibly reduced second cylinder end-point. Assumes that the second end-point's
        // w-coordinate is 1.
        double dq2 = plane.position.dot(endpoints[1]);
        boolean bq2 = dq2 <= -effectiveRadius;

        if (bq1 && bq2) // endpoints more distant from plane than effective radius; cylinder is on neg. side of plane
            return -1;

        if (bq1 == bq2) // endpoints less distant from plane than effective radius; can't draw any conclusions
            return 0;

        // Compute and return the endpoints of the cylinder on the positive side of the plane.
        float t = (float) ((effectiveRadius + dq1) / plane.getNormal().dot(endpoints[0].tmp().sub(endpoints[1])));

        gb_Vector3 newEndPoint = endpoints[0].tmp().add(endpoints[1].tmp2().sub(endpoints[0]).tmp3().mul(t));
        if (bq1) // Truncate the lower end of the cylinder
            endpoints[0].set(newEndPoint);
        else // Truncate the upper end of the cylinder
            endpoints[1].set(newEndPoint);

        return t;
    }

    /** {@inheritDoc} */
    public double getEffectiveRadius(gb_Plane plane)
    {
        if (plane == null)
            return 0;

        // Determine the effective radius of the cylinder axis relative to the plane.
        double dot = plane.getNormal().dot(this.axisUnitDirection);
        double scale = 1d - dot * dot;
        if (scale <= 0)
            return 0;
        else
            return this.cylinderRadius * Math.sqrt(scale);
    }

    /** {@inheritDoc} */
    public boolean intersects(gb_Plane plane)
    {
        if (plane == null)
        {
            String message = "nullValue.PlaneIsNull";
            throw new IllegalArgumentException(message);
        }

        double effectiveRadius = this.getEffectiveRadius(plane);
        return this.intersects(plane, effectiveRadius) >= 0;
    }

    /** {@inheritDoc} */
    public boolean intersects(gb_Frustum frustum)
    {
        if (frustum == null)
        {
            String message = "nullValue.FrustumIsNull";
            throw new IllegalArgumentException(message);
        }

        double intersectionPoint;
        gb_Vector3[] endPoints = new gb_Vector3[] {this.bottomCenter, this.topCenter};

        double effectiveRadius = this.getEffectiveRadius(frustum.getNear());
        intersectionPoint = this.intersectsAt(frustum.getNear(), effectiveRadius, endPoints);
        if (intersectionPoint < 0)
            return false;

        // Near and far have the same effective radius.
        intersectionPoint = this.intersectsAt(frustum.getFar(), effectiveRadius, endPoints);
        if (intersectionPoint < 0)
            return false;

        effectiveRadius = this.getEffectiveRadius(frustum.getLeft());
        intersectionPoint = this.intersectsAt(frustum.getLeft(), effectiveRadius, endPoints);
        if (intersectionPoint < 0)
            return false;

        effectiveRadius = this.getEffectiveRadius(frustum.getRight());
        intersectionPoint = this.intersectsAt(frustum.getRight(), effectiveRadius, endPoints);
        if (intersectionPoint < 0)
            return false;

        effectiveRadius = this.getEffectiveRadius(frustum.getTop());
        intersectionPoint = this.intersectsAt(frustum.getTop(), effectiveRadius, endPoints);
        if (intersectionPoint < 0)
            return false;

        effectiveRadius = this.getEffectiveRadius(frustum.getBottom());
        intersectionPoint = this.intersectsAt(frustum.getBottom(), effectiveRadius, endPoints);
        return intersectionPoint >= 0;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof Cylinder))
            return false;

        Cylinder cylinder = (Cylinder) o;

        if (Double.compare(cylinder.cylinderHeight, cylinderHeight) != 0)
            return false;
        if (Double.compare(cylinder.cylinderRadius, cylinderRadius) != 0)
            return false;
        if (axisUnitDirection != null ? !axisUnitDirection.equals(cylinder.axisUnitDirection)
            : cylinder.axisUnitDirection != null)
            return false;
        if (bottomCenter != null ? !bottomCenter.equals(cylinder.bottomCenter) : cylinder.bottomCenter != null)
            return false;
        //noinspection RedundantIfStatement
        if (topCenter != null ? !topCenter.equals(cylinder.topCenter) : cylinder.topCenter != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = bottomCenter != null ? bottomCenter.hashCode() : 0;
        result = 31 * result + (topCenter != null ? topCenter.hashCode() : 0);
        result = 31 * result + (axisUnitDirection != null ? axisUnitDirection.hashCode() : 0);
        temp = cylinderRadius != +0.0d ? Double.doubleToLongBits(cylinderRadius) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = cylinderHeight != +0.0d ? Double.doubleToLongBits(cylinderHeight) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String toString()
    {
        return this.cylinderRadius + ", " + this.bottomCenter.toString() + ", " + this.topCenter.toString() + ", "
            + this.axisUnitDirection.toString();
    }
}