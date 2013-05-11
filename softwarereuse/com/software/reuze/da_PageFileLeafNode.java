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

import com.software.reuze.mpb_i_Node;


/**
 * <p>
 * Implementation of a LeafNode. Inherits methods from the abstract class Node filling the defined
 * abstract methods with life.
 * </p>
 *
 * @author Wolfgang Baer - WBaer@gmx.de
 */
class da_PageFileLeafNode extends da_PageFileNode implements Serializable {

    protected int[] data;

    // protected Object[] data;

    /**
     * Constructor.
     *
     * @param pageNumber -
     *            number of this node in page file
     * @param file -
     *            the PageFile of this node
     */
    protected da_PageFileLeafNode( int pageNumber, da_a_PageFile file ) {
        super( pageNumber, file );
        data = new int[file.getCapacity()];

        for ( int i = 0; i < file.getCapacity(); i++ )
            data[i] = -1;
    }

    /**
     * Constructor.<br>
     * The page number in the pagefile will be assigned with the first save to a page file
     *
     * @param file -
     *            the PageFile of this node
     */
    protected da_PageFileLeafNode( da_a_PageFile file ) {
        super( -1, file );
        data = new int[file.getCapacity()];

        for ( int i = 0; i < file.getCapacity(); i++ )
            data[i] = -1;
    }

    /**
     * Return type is an Integer object
     *
     * @see mpb_i_Node#getData(int)
     */
    protected Object getData( int index ) {
        return new Integer( data[index] );
    }

    /**
     * @see mpb_i_Node#insertData(java.lang.Object, HyperBoundingBox)
     */
    protected void insertData( Object obj, gc_HyperspaceBoundingBox box ) {
        data[counter] = ( (Integer) obj ).intValue();
        hyperBBs[counter] = box;
        unionMinBB = unionMinBB.unionBoundingBox( box );
        counter = counter + 1;
    }

    /**
     * @see mpb_i_Node#insertData(java.lang.Object, HyperBoundingBox)
     */
    protected void deleteData( int index ) {
        if ( this.getUsedSpace() == 1 ) {
            // only one element is a special case.
            hyperBBs[0] = gc_HyperspaceBoundingBox.getNullHyperBoundingBox( file.getDimension() );
            data[0] = -1;
        } else {
            System.arraycopy( hyperBBs, index + 1, hyperBBs, index, counter - index - 1 );
            System.arraycopy( data, index + 1, data, index, counter - index - 1 );
            hyperBBs[counter - 1] = gc_HyperspaceBoundingBox.getNullHyperBoundingBox( file.getDimension() );
            data[counter - 1] = -1;
        }

        counter--;
        updateNodeBoundingBox();
    }

    /**
     * @see mpb_i_Node#clone()
     */
    protected Object clone() {

        da_PageFileLeafNode clone = new da_PageFileLeafNode( this.pageNumber, this.file );
        clone.counter = this.counter;
        clone.place = this.place;
        clone.unionMinBB = (gc_HyperspaceBoundingBox) this.unionMinBB.clone();
        clone.parentNode = this.parentNode;

        for ( int i = 0; i < file.getCapacity(); i++ )
            clone.hyperBBs[i] = (gc_HyperspaceBoundingBox) this.hyperBBs[i].clone();

        return clone;
    }
}
