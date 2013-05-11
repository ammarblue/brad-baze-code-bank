package com.software.reuze;
//$HeadURL$
//----------------------------------------
//RTree implementation.
//Copyright (C) 2002-2004 Wolfgang Baer - WBaer@gmx.de
//
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
//
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with this library; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//----------------------------------------

import java.io.Serializable;

/**
 * <p>
 * HyperBoundingBox implementing a bounding box object in the multidimensional space.
 * </p>
 *
 * @author Wolfgang Baer - WBaer@gmx.de
 */

public class gc_HyperspaceBoundingBox implements Serializable {

    private gc_HyperspacePoint pMin;

    private gc_HyperspacePoint pMax;

    /**
     * Constructor.<br>
     * Creates a HyperBoundingBox for given HyperPoints.
     *
     * @param pMin -
     *            min point
     * @param pMax -
     *            max point
     */
    public gc_HyperspaceBoundingBox( gc_HyperspacePoint pMin, gc_HyperspacePoint pMax ) {
        if ( pMin.getDimension() != pMax.getDimension() )
            throw new IllegalArgumentException( "HyperPoints need same dimension: " + pMin.getDimension() + " != "
                                                + pMax.getDimension() );

        this.pMin = pMin;
        this.pMax = pMax;
    }

    /**
     * Creates a null HyperBoundingBox with null HyperPoints. Mostly used internal.
     *
     * @param dimension -
     *            int for dimension of point
     * @return HyperBoundingBox
     */
    protected static gc_HyperspaceBoundingBox getNullHyperBoundingBox( int dimension ) {
        return new gc_HyperspaceBoundingBox( gc_HyperspacePoint.getNullHyperPoint( dimension ),
                                     gc_HyperspacePoint.getNullHyperPoint( dimension ) );
    }

    /**
     * Returns the minimum GeometryHyperspacePoint
     *
     * @return GeometryHyperspacePoint
     *
     */
    public gc_HyperspacePoint getPMin() {
        return pMin;
    }

    /**
     * Returns the maximum GeometryHyperspacePoint
     *
     * @return GeometryHyperspacePoint
     *
     */
    public gc_HyperspacePoint getPMax() {
        return pMax;
    }

    /**
     * Returns the dimension of this HyperBoundingBox.
     *
     * @return int
     */
    public int getDimension() {
        return pMin.getDimension();
    }

    /**
     * Tests if this and the given HyperBoundingBox overlaps.
     *
     * @param box -
     *            HyperBoundingBox to test
     * @return boolean
     */
    public boolean overlaps( gc_HyperspaceBoundingBox box ) {
        boolean intersect = true;

        for ( int i = 0; i < getDimension(); i++ ) {
            if ( ( pMin.getCoord( i ) > box.getPMax().getCoord( i ) )
                 || ( pMax.getCoord( i ) < box.getPMin().getCoord( i ) ) ) {
                intersect = false;
                break;
            }
        }

        return intersect;
    }

    /**
     * Tests if this contains the given HyperBoundingBox overlaps.
     *
     * @param box -
     *            HyperBoundingBox to test
     * @return boolean
     */
    public boolean contains( gc_HyperspaceBoundingBox box ) {
        boolean contains = true;

        for ( int i = 0; i < getDimension(); i++ ) {
            if ( ( pMin.getCoord( i ) > box.getPMin().getCoord( i ) )
                 || ( pMax.getCoord( i ) < box.getPMax().getCoord( i ) ) ) {
                contains = false;
                break;
            }
        }

        return contains;
    }

    /**
     * Computes the area (over all dimension) of this.
     *
     * @return double
     */
    public double getArea() {
        double area = 1;

        for ( int i = 0; i < pMin.getDimension(); i++ )
            area = area * ( pMax.getCoord( i ) - pMin.getCoord( i ) );

        return area;
    }

    /**
     * Computes the union of this with the given HyperBoundingBox.
     *
     * @param box -
     *            given HyperBoundingBox
     * @return HyperBoundingBox
     */
    public gc_HyperspaceBoundingBox unionBoundingBox( gc_HyperspaceBoundingBox box ) {

        if ( this.getDimension() != box.getDimension() )
            throw new IllegalArgumentException( "HyperBoundingBoxes need same dimension " + this.getDimension()
                                                + " != " + box.getDimension() );

        if ( this.equals( gc_HyperspaceBoundingBox.getNullHyperBoundingBox( this.getDimension() ) ) )
            return box;
        if ( box.equals( gc_HyperspaceBoundingBox.getNullHyperBoundingBox( this.getDimension() ) ) )
            return this;

        double[] min = new double[this.getDimension()];
        double[] max = new double[this.getDimension()];

        for ( int i = 0; i < this.getDimension(); i++ ) {

            if ( this.getPMin().getCoord( i ) <= box.getPMin().getCoord( i ) ) {
                min[i] = this.getPMin().getCoord( i );
            } else {
                min[i] = box.getPMin().getCoord( i );
            }
            if ( this.getPMax().getCoord( i ) >= box.getPMax().getCoord( i ) ) {
                max[i] = this.getPMax().getCoord( i );
            } else {
                max[i] = box.getPMax().getCoord( i );
            }
        }
        return new gc_HyperspaceBoundingBox( new gc_HyperspacePoint( min ), new gc_HyperspacePoint( max ) );
    }

    /**
     * Computes the minimal distance square of this to the given GeometryHyperspacePoint. After Roussopoulos
     * Nick: Nearest Neighbor Queries - MINDIST
     *
     * @param point -
     *            GeometryHyperspacePoint
     * @return double
     */
    public double minDist( gc_HyperspacePoint point ) {
        double min = 0;
        double ri = 0;

        for ( int i = 0; i < point.getDimension(); i++ ) {
            if ( point.getCoord( i ) < this.pMin.getCoord( i ) ) {
                ri = this.pMin.getCoord( i );
            } else {
                if ( point.getCoord( i ) > this.pMax.getCoord( i ) ) {
                    ri = this.pMax.getCoord( i );
                } else {
                    ri = point.getCoord( i );
                }
            }
            min = min + Math.pow( point.getCoord( i ) - ri, 2 );
        }
        return min;
    }

    /**
     * Deep copy.
     *
     * @see java.lang.Object#clone()
     */
    protected Object clone() {
        return new gc_HyperspaceBoundingBox( (gc_HyperspacePoint) pMin.clone(), (gc_HyperspacePoint) pMax.clone() );
    }

    /**
     * Builds a String representation of the HyperBoundingBox.
     *
     * @return String
     */
    public String toString() {
        return "BOX: P-Min (" + pMin.toString() + "), P-Max (" + pMax.toString() + ")";
    }

    /**
     * Implements equals
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals( Object obj ) {
        gc_HyperspaceBoundingBox box = (gc_HyperspaceBoundingBox) obj;
        return ( this.pMin.equals( box.pMin ) && this.pMax.equals( box.pMax ) );
    }
}
