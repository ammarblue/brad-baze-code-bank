package com.software.reuze;
/*
  * Copyright John E. Lloyd, 2003. All rights reserved. Permission
  * to use, copy, and modify, without fee, is granted for non-commercial 
  * and research purposes, provided that this copyright notice appears 
  * in all copies.
  *
  * This  software is distributed "as is", without any warranty, including 
  * any implied warranty of merchantability or fitness for a particular
  * use. The authors assume no responsibility for, and shall not be liable
  * for, any special, indirect, or consequential damages, or any damages
  * whatsoever, arising out of or in connection with the use of this
  * software.
  */

import java.util.*;

import com.software.reuze.gb_HalfEdge;
import com.software.reuze.gb_Vertex;


/**
 * Basic triangular face used to form the hull.
 *
 * <p>The information stored for each face consists of a planar
 * normal, a planar offset, and a doubly-linked list of three <a
 * href=HalfEdge>HalfEdges</a> which surround the face in a
 * counter-clockwise direction.
 *
 * @author John E. Lloyd, Fall 2004 */
class gb_Face
{
	gb_HalfEdge he0;
	private gb_Vector3 normal;
	float area;
	private gb_Vector3 centroid;
	float planeOffset;
	int index;
	int numVerts;

	gb_Face next;

	static final int VISIBLE = 1;
	static final int NON_CONVEX = 2;
	static final int DELETED = 3;

	int mark = VISIBLE;

	gb_Vertex outside;

	public void computeCentroid (gb_Vector3 centroid)
	 {
	   centroid.set(gb_Vector3.ZERO);
	   gb_HalfEdge he = he0;
	   do
	    { centroid.add (he.head().pnt);
	      he = he.next;
	    }
	   while (he != he0);
	   centroid.mul (1/(float)numVerts);
	 }

	public void computeNormal (gb_Vector3 normal, double minArea)
	 {
	   computeNormal(normal);

	   if (area < minArea)
	    { 
	      System.out.println ("area=" + area);
	      // make the normal more robust by removing
	      // components parallel to the longest edge

	      gb_HalfEdge hedgeMax = null;
	      double lenSqrMax = 0;
	      gb_HalfEdge hedge = he0;
	      do
	       { double lenSqr = hedge.lengthSquared();
		 if (lenSqr > lenSqrMax)
		  { hedgeMax = hedge;
		    lenSqrMax = lenSqr;
		  }
		 hedge = hedge.next;
	       }
	      while (hedge != he0);

	      gb_Vector3 p2 = hedgeMax.head().pnt;
	      gb_Vector3 p1 = hedgeMax.tail().pnt;
	      double lenMax = Math.sqrt(lenSqrMax);
	      double ux = (p2.x - p1.x)/lenMax;
	      double uy = (p2.y - p1.y)/lenMax;
	      double uz = (p2.z - p1.z)/lenMax;	   
	      double dot = normal.x*ux + normal.y*uy + normal.z*uz;
	      normal.x -= dot*ux;
	      normal.y -= dot*uy;
	      normal.z -= dot*uz;

	      normal.nor();	      
	    }
	 }

	public void computeNormal (gb_Vector3 normal)
	 {
	   gb_HalfEdge he1 = he0.next;
	   gb_HalfEdge he2 = he1.next;

	   gb_Vector3 p0 = he0.head().pnt;
	   gb_Vector3 p2 = he1.head().pnt;

	   double d2x = p2.x - p0.x;
	   double d2y = p2.y - p0.y;
	   double d2z = p2.z - p0.z;

	   normal.set(gb_Vector3.ZERO);

	   numVerts = 2;

	   while (he2 != he0)
	    { 
	      double d1x = d2x;
	      double d1y = d2y;
	      double d1z = d2z;

	      p2 = he2.head().pnt;
	      d2x = p2.x - p0.x;
	      d2y = p2.y - p0.y;
	      d2z = p2.z - p0.z;

	      normal.x += d1y*d2z - d1z*d2y;
	      normal.y += d1z*d2x - d1x*d2z;
	      normal.z += d1x*d2y - d1y*d2x;

	      he1 = he2;
	      he2 = he2.next;
	      numVerts++;
	    }
	   area = normal.len();
	   normal.mul (1/area);
	 }

	private boolean computeNormalAndCentroid() 
	 {
	   computeNormal (normal);
	   computeCentroid (centroid);
	   planeOffset = normal.dot(centroid);
	   int numv = 0;
	   gb_HalfEdge he = he0;
	   do
	    { numv++;
	      he = he.next;
	    }
	   while (he != he0);
	   if (numv == numVerts) return true;
	   System.out.println (
          "face " + getVertexString() + " numVerts=" + numVerts + " should be " + numv);
	    normal=null; centroid=null;
	    return false;
	 }

	private void computeNormalAndCentroid(double minArea)
	 {
	   computeNormal (normal, minArea);
	   computeCentroid (centroid);
	   planeOffset = normal.dot(centroid);
	 }

	public static gb_Face createTriangle (gb_Vertex v0, gb_Vertex v1, gb_Vertex v2)
	 {
	   return createTriangle (v0, v1, v2, 0);
	 }

	/**
	 * Constructs a triangule Face from vertices v0, v1, and v2.
	 *
	 * @param v0 first vertex
	 * @param v1 second vertex
	 * @param v2 third vertex
	 */
	public static gb_Face createTriangle (gb_Vertex v0, gb_Vertex v1, gb_Vertex v2,
					   double minArea)
	 {
	   gb_Face face = new gb_Face();
	   gb_HalfEdge he0 = new gb_HalfEdge (v0, face);
	   gb_HalfEdge he1 = new gb_HalfEdge (v1, face);
	   gb_HalfEdge he2 = new gb_HalfEdge (v2, face);

	   he0.prev = he2;
	   he0.next = he1;
	   he1.prev = he0;
	   he1.next = he2;
	   he2.prev = he1;
	   he2.next = he0;

	   face.he0 = he0;

	   // compute the normal and offset
	   face.computeNormalAndCentroid(minArea);
	   return face;
	 }

	public static gb_Face create (gb_Vertex[] vtxArray, int[] indices)
	 {
	   gb_Face face = new gb_Face();
	   gb_HalfEdge hePrev = null;
	   for (int i=0; i<indices.length; i++)
	    { gb_HalfEdge he = new gb_HalfEdge (vtxArray[indices[i]], face); 
	      if (hePrev != null)
	       { he.setPrev (hePrev);
		 hePrev.setNext (he);
	       }
	      else
	       { face.he0 = he; 
	       }
	      hePrev = he;
	    }
	   face.he0.setPrev (hePrev);
	   hePrev.setNext (face.he0);

	   // compute the normal and offset
	   return face.computeNormalAndCentroid()?face:null;	   
	 }

	public gb_Face ()
	 { 
	   normal = new gb_Vector3();
	   centroid = new gb_Vector3();
	   mark = VISIBLE;
	 }

	/**
	 * Gets the i-th half-edge associated with the face.
	 * 
	 * @param i the half-edge index, in the range 0-2.
	 * @return the half-edge
	 */
	public gb_HalfEdge getEdge(int i)
	 {
	   gb_HalfEdge he = he0;
	   while (i > 0)
	    { he = he.next;
	      i--;
	    }
	   while (i < 0)
	    { he = he.prev;
	      i++;
	    }
	   return he;
	 }

	public gb_HalfEdge getFirstEdge()
	 { return he0;
	 }
	
	/**
	 * Finds the half-edge within this face which has
	 * tail <code>vt</code> and head <code>vh</code>.
	 *
	 * @param vt tail point
	 * @param vh head point
	 * @return the half-edge, or null if none is found.
	 */
	public gb_HalfEdge findEdge (gb_Vertex vt, gb_Vertex vh)
	 {
	   gb_HalfEdge he = he0;
	   do
	    { if (he.head() == vh && he.tail() == vt)
	       { return he;
	       }
	      he = he.next;
	    }
	   while (he != he0);
	   return null;
	 }

	/**
	 * Computes the distance from a point p to the plane of
	 * this face.
	 *
	 * @param p the point
	 * @return distance from the point to the plane
	 */
	public float distanceToPlane (gb_Vector3 p)
	 {
	   return normal.x*p.x + normal.y*p.y + normal.z*p.z - planeOffset;
	 }

	/**
	 * Returns the normal of the plane associated with this face.
	 *
	 * @return the planar normal
	 */
	public gb_Vector3 getNormal ()
	 {
	   return normal;
	 }

	public gb_Vector3 getCentroid ()
	 {
	   return centroid;
	 }

	public int numVertices()
	 {
	   return numVerts;
	 }

	public String getVertexString ()
	 {
	   String s = null;
	   gb_HalfEdge he = he0;
	   do
	    { if (s == null)
	       { s = "" + he.head().index;
	       }
	      else
	       { s += " " + he.head().index;
	       }
	      he = he.next;
	    }
	   while (he != he0);
	   return s;
	 }

	public void getVertexIndices (int[] idxs)
	 {
	   gb_HalfEdge he = he0;
	   int i = 0;
	   do
	    { idxs[i++] = he.head().index;
	      he = he.next;
	    }
	   while (he != he0);
	 }

	private gb_Face connectHalfEdges (
	   gb_HalfEdge hedgePrev, gb_HalfEdge hedge)
	 {
	   gb_Face discardedFace = null;

	   if (hedgePrev.oppositeFace() == hedge.oppositeFace())
	    { // then there is a redundant edge that we can get rid off

	      gb_Face oppFace = hedge.oppositeFace();
	      gb_HalfEdge hedgeOpp;

	      if (hedgePrev == he0)
	       { he0 = hedge; 
	       }
	      if (oppFace.numVertices() == 3)
	       { // then we can get rid of the opposite face altogether
		 hedgeOpp = hedge.getOpposite().prev.getOpposite();

		 oppFace.mark = DELETED;
		 discardedFace = oppFace;
	       }
	      else
	       { hedgeOpp = hedge.getOpposite().next;

		 if (oppFace.he0 == hedgeOpp.prev)
		  { oppFace.he0 = hedgeOpp; 
		  }
		 hedgeOpp.prev = hedgeOpp.prev.prev;
		 hedgeOpp.prev.next = hedgeOpp;
	       }
	      hedge.prev = hedgePrev.prev;
	      hedge.prev.next = hedge;

	      hedge.opposite = hedgeOpp;
	      hedgeOpp.opposite = hedge;

	      // oppFace was modified, so need to recompute
	      if (!oppFace.computeNormalAndCentroid()) oppFace=null;
	    }
	   else
	    { hedgePrev.next = hedge;
	      hedge.prev = hedgePrev;
	    }
	   return discardedFace;
	 }

	boolean checkConsistency()
	 {
	   // do a sanity check on the face
	   gb_HalfEdge hedge = he0; 
	   double maxd = 0;
	   int numv = 0;

	   if (numVerts < 3)
	    { System.out.println(
		    "degenerate face: " + getVertexString());
	      return false;
	    }
	   do
	    { gb_HalfEdge hedgeOpp = hedge.getOpposite();
	      if (hedgeOpp == null)
	       { System.out.println(
		    "face " + getVertexString() + ": " +
		    "unreflected half edge " + hedge.getVertexString());
	        return false;
	       }
	      else if (hedgeOpp.getOpposite() != hedge)
	       { System.out.println(
		    "face " + getVertexString() + ": " +
		    "opposite half edge " + hedgeOpp.getVertexString() +
		    " has opposite " +
		    hedgeOpp.getOpposite().getVertexString());
	        return false;
	       }
	      if (hedgeOpp.head() != hedge.tail() ||
		  hedge.head() != hedgeOpp.tail())
	       { System.out.println(
		    "face " + getVertexString() + ": " +
		    "half edge " + hedge.getVertexString() +
		    " reflected by " + hedgeOpp.getVertexString());
	        return false;
	       }
	      gb_Face oppFace = hedgeOpp.face;
	      if (oppFace == null)
	       { System.out.println(
		    "face " + getVertexString() + ": " +
		    "no face on half edge " + hedgeOpp.getVertexString());
	        return false;
	       }
	      else if (oppFace.mark == DELETED)
	       { System.out.println(
		    "face " + getVertexString() + ": " +
		    "opposite face " + oppFace.getVertexString() + 
		    " not on hull");
	        return false;
	       }
	      double d = Math.abs(distanceToPlane(hedge.head().pnt));
	      if (d > maxd)
	       { maxd = d;
	       }
	      numv++;
	      hedge = hedge.next;
	    }
	   while (hedge != he0);

	   if (numv != numVerts)
	    {  System.out.println(
             "face " + getVertexString() + " numVerts=" + numVerts + " should be " + numv);
	       return false;
	    }
       return true;
	 }

	public int mergeAdjacentFace (gb_HalfEdge hedgeAdj,
				      gb_Face[] discarded)
	 {
	   gb_Face oppFace = hedgeAdj.oppositeFace();
	   int numDiscarded = 0;

	   discarded[numDiscarded++] = oppFace;
	   oppFace.mark = DELETED;

	   gb_HalfEdge hedgeOpp = hedgeAdj.getOpposite();

	   gb_HalfEdge hedgeAdjPrev = hedgeAdj.prev;
	   gb_HalfEdge hedgeAdjNext = hedgeAdj.next;
	   gb_HalfEdge hedgeOppPrev = hedgeOpp.prev;
	   gb_HalfEdge hedgeOppNext = hedgeOpp.next;

	   while (hedgeAdjPrev.oppositeFace() == oppFace)
	    { hedgeAdjPrev = hedgeAdjPrev.prev;
	      hedgeOppNext = hedgeOppNext.next;
	    }
	   
	   while (hedgeAdjNext.oppositeFace() == oppFace)
	    { hedgeOppPrev = hedgeOppPrev.prev;
	      hedgeAdjNext = hedgeAdjNext.next;
	    }

	   gb_HalfEdge hedge;

	   for (hedge=hedgeOppNext; hedge!=hedgeOppPrev.next; hedge=hedge.next)
	    { hedge.face = this;
	    }

	   if (hedgeAdj == he0)
	    { he0 = hedgeAdjNext; 
	    }
	   
	   // handle the half edges at the head
	   gb_Face discardedFace;

	   discardedFace = connectHalfEdges (hedgeOppPrev, hedgeAdjNext);
	   if (discardedFace != null)
	    { discarded[numDiscarded++] = discardedFace; 
	    }

	   // handle the half edges at the tail
	   discardedFace = connectHalfEdges (hedgeAdjPrev, hedgeOppNext);
	   if (discardedFace != null)
	    { discarded[numDiscarded++] = discardedFace; 
	    }

	   computeNormalAndCentroid ();
	   checkConsistency();

	   return numDiscarded;
	 }

	private double areaSquared (gb_HalfEdge hedge0, gb_HalfEdge hedge1)
	 {
	   // return the squared area of the triangle defined
	   // by the half edge hedge0 and the point at the
	   // head of hedge1.

		gb_Vector3 p0 = hedge0.tail().pnt;
		gb_Vector3 p1 = hedge0.head().pnt;
		gb_Vector3 p2 = hedge1.head().pnt;

	   double dx1 = p1.x - p0.x;
	   double dy1 = p1.y - p0.y;
	   double dz1 = p1.z - p0.z;

	   double dx2 = p2.x - p0.x;
	   double dy2 = p2.y - p0.y;
	   double dz2 = p2.z - p0.z;

	   double x = dy1*dz2 - dz1*dy2;
	   double y = dz1*dx2 - dx1*dz2;
	   double z = dx1*dy2 - dy1*dx2;

	   return x*x + y*y + z*z;	   
	 }

	public void triangulate (gb_FaceList newFaces, double minArea)
	 {
	   gb_HalfEdge hedge;

	   if (numVertices() < 4)
	    { return; 
	    }

	   gb_Vertex v0 = he0.head();
	   gb_Face prevFace = null;

	   hedge = he0.next;
	   gb_HalfEdge oppPrev = hedge.opposite;
	   gb_Face face0 = null;

	   for (hedge=hedge.next; hedge!=he0.prev; hedge=hedge.next)	   
	    { gb_Face face =
		 createTriangle (v0, hedge.prev.head(), hedge.head(), minArea);
	      face.he0.next.setOpposite (oppPrev);
	      face.he0.prev.setOpposite (hedge.opposite);
	      oppPrev = face.he0;
	      newFaces.add (face);
	      if (face0 == null)
	       { face0 = face; 
	       }
	    }
	   hedge = new gb_HalfEdge (he0.prev.prev.head(), this);
	   hedge.setOpposite (oppPrev);

	   hedge.prev = he0;
	   hedge.prev.next = hedge;

	   hedge.next = he0.prev;
	   hedge.next.prev = hedge;

	   computeNormalAndCentroid (minArea);
	   checkConsistency();

	   for (gb_Face face=face0; face!=null; face=face.next)
	    { face.checkConsistency(); 
	    }

	 }
}



