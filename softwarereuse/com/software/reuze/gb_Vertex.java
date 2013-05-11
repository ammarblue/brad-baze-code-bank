package com.software.reuze;

/**
 * Represents vertices of the hull, as well as the points from
 * which it is formed.
 *
 * @author John E. Lloyd, Fall 2004
 */
class gb_Vertex
{
	/**
	 * Spatial point associated with this vertex.
	 */
	gb_Vector3 pnt;

	/**
	 * Back index into an array.
	 */
	int index;

	/**
	 * List forward link.
	 */
 	gb_Vertex prev;

	/**
	 * List backward link.
	 */
 	gb_Vertex next;

	/**
	 * Current face that this vertex is outside of.
	 */
 	gb_Face face;

	/**
	 * Constructs a vertex and sets its coordinates to 0.
	 */
	public gb_Vertex()
	 { pnt = new gb_Vector3();
	 }

}
