package com.software.reuze;
//$HeadURL$
// ----------------------------------------
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
 * Abstract class for common implementation and definition of abstract methods for both concrete
 * classes LeafNode and NoneLeafNode.
 * </p>
 *
 * @author Wolfgang Baer - WBaer@gmx.de
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
abstract class da_PageFileNode implements Serializable {

    protected transient da_a_PageFile file;

    protected int parentNode;

    protected int pageNumber;

    protected int counter;

    protected gc_HyperspaceBoundingBox unionMinBB;

    protected gc_HyperspaceBoundingBox[] hyperBBs;

    protected int place;

    /**
     * Constructor.
     *
     * @param pageNumber -
     *            number of this node in page file
     * @param pageFile -
     *            the PageFile of this node
     */
    protected da_PageFileNode( int pageNumber, da_a_PageFile pageFile ) {
        this.file = pageFile;
        this.pageNumber = pageNumber;
        parentNode = 0;
        hyperBBs = new gc_HyperspaceBoundingBox[file.getCapacity()];

        for ( int i = 0; i < file.getCapacity(); i++ )
            hyperBBs[i] = gc_HyperspaceBoundingBox.getNullHyperBoundingBox( file.getDimension() );

        unionMinBB = gc_HyperspaceBoundingBox.getNullHyperBoundingBox( file.getDimension() );
        counter = 0;
    }

    /**
     * Inserts the given data into the node
     *
     * @param obj -
     *            object to insert (Typ Integer oder AbstractNode)
     * @param box -
     *            the associated HyperBoundingBox
     */
    protected abstract void insertData( Object obj, gc_HyperspaceBoundingBox box );

    /**
     * Deletes a the entry with given index from node
     *
     * @param index -
     *            index of entry
     */
    protected abstract void deleteData( int index );

    /**
     * Fetches the data for given index from node
     *
     * @param index -
     *            index of data
     */
    protected abstract Object getData( int index );

    /**
     * Returns the parent node of this.
     *
     * @return Node
     */
    protected da_PageFileNode getParent() {

    	da_PageFileNode node = null;
        try {
            node = file.readNode( parentNode );
        } catch ( le_ExceptionPageFile e ) {
            // PageFileException: AbstractNode.getParent() - readNode
            e.printStackTrace();
        }

        return node;
    }

    /**
     * Returns the page number of this.
     *
     * @return int
     */
    protected int getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the page number of this to given number
     *
     * @param number -
     *            int
     */
    protected void setPageNumber( int number ) {
        this.pageNumber = number;
    }

    /**
     * Currently used space in the node
     *
     * @return int
     */
    protected int getUsedSpace() {
        return counter;
    }

    /**
     * Returns the HyperBoundingBox over all Entries currently in the node
     *
     * @return HyperBoundingBox
     */
    protected gc_HyperspaceBoundingBox getUnionMinBB() {
        return unionMinBB;
    }

    /**
     * Updates the HyperBoundingBox over all Entries currently in the node
     */
    protected void updateNodeBoundingBox() {
        this.unionMinBB = gc_HyperspaceBoundingBox.getNullHyperBoundingBox( file.getDimension() );
        for ( int i = 0; i < this.getUsedSpace(); i++ )
            this.unionMinBB = this.unionMinBB.unionBoundingBox( this.hyperBBs[i] );
    }

    /**
     * Returns an array of HyperBoundingBox objects of the entries of the node. The array may be
     * empty - for used place in the node see getUsedSpace.
     *
     * @return HyperBoundingBox[] - boxes of the entries
     * @see #getUsedSpace()
     */
    protected gc_HyperspaceBoundingBox[] getHyperBoundingBoxes() {
        return hyperBBs;
    }

    /**
     * Returns the HyperBoundingBox for entry with given index.
     *
     * @param index -
     *            index of entry
     * @return HyperBoundingBox
     */
    protected gc_HyperspaceBoundingBox getHyperBoundingBox( int index ) {
        return hyperBBs[index];
    }

    /**
     * Tests if this is the root node.
     *
     * @return boolean
     */
    protected boolean isRoot() {
        return pageNumber == 0;
    }

    /**
     * Deep copy without data entries (only HyperBoundingBox objects)
     *
     * @see java.lang.Object#clone()
     */
    @Override
    protected abstract Object clone();

    /**
     * String-Representation of Node
     *
     */
    @Override
    public String toString() {
        String str = "";

        if ( this instanceof da_PageFileLeafNode ) {
            str = "LeafNode: " + unionMinBB.toString();
        } else {
            str = "NoneLeafNode: " + unionMinBB.toString();
        }

        return str;
    }
}
