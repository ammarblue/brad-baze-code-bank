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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

import com.software.reuze.da_PageFileLeafNode;
import com.software.reuze.da_PageFileLeafNodeNone;
import com.software.reuze.da_PageFileMemory;
import com.software.reuze.da_PageFileNode;
import com.software.reuze.da_PageFilePersistent;
import com.software.reuze.da_a_PageFile;
import com.software.reuze.le_ExceptionPageFile;


/**
 * <br>
 * Implementation of a R-Tree after the algorithms of Antonio Guttman. With nearest neighbour search
 * after algorithm of Cheung & Fu <br>
 *
 * @author Wolfgang Baer - WBaer@gmx.de
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public class da_TreeR implements Serializable {

    private da_a_PageFile file;

    /**
     * Creates an empty R-Tree with a memory-mapped pagefile (PageFileMemory) and an empty root node
     *
     * @param dimension -
     *            dimension of the data to store
     * @param maxLoad -
     *            maximum load of a node
     * @throws le_ExceptionTreeR
     */
    public da_TreeR( int dimension, int maxLoad ) throws le_ExceptionTreeR {
        this.file = new da_PageFileMemory();

        try {
            file.initialize( dimension, maxLoad + 1 );
            da_PageFileNode rootNode = new da_PageFileLeafNode( 0, this.file );
            file.writeNode( rootNode );
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException in constructor occured" );
        }
    }

    /**
     * Creates an empty R-Tree with a persistent pagefile (PageFilePersistent) and an empty root
     * node.
     *
     * @param dimension -
     *            dimension of the data to store
     * @param maxLoad -
     *            maximum load of a node
     * @param fileName -
     *            name of the rtree file
     * @throws le_ExceptionTreeR
     */
    public da_TreeR( int dimension, int maxLoad, String fileName ) throws le_ExceptionTreeR {
        try {
            this.file = new da_PageFilePersistent( fileName );
            this.file.initialize( dimension, maxLoad + 1 );

            da_PageFileNode rootNode = new da_PageFileLeafNode( 0, this.file );
            file.writeNode( rootNode );
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException in constructor occured" );
        }
    }

    /**
     * Creates an R-Tree from an EXISTING persistent pagefile (PageFilePersistent).
     *
     * @param fileName -
     *            name of the existing rtree file
     * @throws le_ExceptionTreeR
     */
    public da_TreeR( String fileName ) throws le_ExceptionTreeR {

        this.file = new da_PageFilePersistent( fileName );
        try {
            this.file.initialize( -999, -999 );
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException in constructor occured" );
        }
    }

    /**
     * Searches all entries in the R-Tree whose HyperBoundingBoxes intersect with the given.
     *
     * @param box -
     *            given test GeometryHyperspaceBoundingBox
     * @return Object[] - Array with retrieved Objects
     * @throws le_ExceptionTreeR
     */
    public Object[] intersects( gc_HyperspaceBoundingBox box )
                            throws le_ExceptionTreeR {
        if ( box.getDimension() != file.getDimension() )
            throw new IllegalArgumentException( "GeometryHyperspaceBoundingBox has wrong dimension " + box.getDimension() + " != "
                                                + file.getDimension() );

        Vector<Object> v = new Vector<Object>( 100 );
        // calls the real search method
        try {
            intersectsSearch( file.readNode( 0 ), v, box );
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException RTree.search() - readNode()" );
        }

        return v.toArray();
    }

    /**
     * Searches all entries in the R-Tree whose HyperBoundingBoxes contain the given.
     *
     * @param box -
     *            given test GeometryHyperspaceBoundingBox
     * @return Object[] - Array with retrieved Objects
     * @throws le_ExceptionTreeR
     */
    public Object[] contains( gc_HyperspaceBoundingBox box )
                            throws le_ExceptionTreeR {
        if ( box.getDimension() != file.getDimension() )
            throw new IllegalArgumentException( "GeometryHyperspaceBoundingBox has wrong dimension" );

        Vector<Object> v = new Vector<Object>( 100 );
        try {
            containsSearch( file.readNode( 0 ), v, box );
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException RTree.search() - readNode() " );
        }

        return v.toArray();
    }

    // private method for contains search
    private void containsSearch( da_PageFileNode node1, Vector<Object> v, gc_HyperspaceBoundingBox box ) {
        if ( node1 instanceof da_PageFileLeafNode ) {
            da_PageFileLeafNode node = (da_PageFileLeafNode) node1;

            for ( int i = 0; i < node.getUsedSpace(); i++ ) {
                // if box is contained put into Vector
                if ( node.hyperBBs[i].contains( box ) )
                    v.addElement( node.getData( i ) );
            }
            return;
        }
        da_PageFileLeafNodeNone node = (da_PageFileLeafNodeNone) node1;

        // node is no Leafnode - so search all entries for overlapping
        for ( int i = 0; i < node.getUsedSpace(); i++ ) {
            if ( node.hyperBBs[i].contains( box ) )
                containsSearch( (da_PageFileNode) node.getData( i ), v, box );
        }
    }

    // private method for intersects search
    private void intersectsSearch( da_PageFileNode node1, Vector<Object> v, gc_HyperspaceBoundingBox box ) {
        if ( node1 instanceof da_PageFileLeafNode ) {
            da_PageFileLeafNode node = (da_PageFileLeafNode) node1;

            for ( int i = 0; i < node.getUsedSpace(); i++ ) {
                if ( node.hyperBBs[i].overlaps( box ) )
                    v.addElement( node.getData( i ) );
            }
            return;
        }

        da_PageFileLeafNodeNone node = (da_PageFileLeafNodeNone) node1;

        for ( int i = 0; i < node.getUsedSpace(); i++ ) {
            if ( node.hyperBBs[i].overlaps( box ) )
                intersectsSearch( (da_PageFileNode) node.getData( i ), v, box );
        }
    }

    /**
     * Inserts the given Object associated with the given GeometryHyperspaceBoundingBox object into the R-Tree.
     *
     * @param obj -
     *            Object to insert
     * @param box -
     *            associated GeometryHyperspaceBoundingBox
     * @return boolean - true if successfull
     * @throws le_ExceptionTreeR
     */
    public boolean insert( Object obj, gc_HyperspaceBoundingBox box )
                            throws le_ExceptionTreeR {

        try {
            da_PageFileNode[] newNodes = new da_PageFileNode[] { null, null };
            // Find position for new record
            da_PageFileLeafNode node;
            node = chooseLeaf( file.readNode( 0 ), box );

            // Add record to leaf node

            if ( node.getUsedSpace() < ( file.getCapacity() - 1 ) ) {
                node.insertData( obj, box );
                file.writeNode( node );
            } else {
                // invoke SplitNode
                node.insertData( obj, box );
                file.writeNode( node );
                newNodes = splitNode( node );
            }

            if ( newNodes[0] != null ) {
                adjustTree( newNodes[0], newNodes[1] );
            } else {
                adjustTree( node, null );
            }
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException occured" );
        }

        return true;
    }

    // algorithm to split a full node
    private da_PageFileNode[] splitNode( da_PageFileNode node )
                            throws le_ExceptionPageFile {

        // new node
        da_PageFileNode newNode = null;
        // temp help node
        da_PageFileNode helpNode = null;

        // compute the start entries
        int[] seeds = pickSeeds( node );

        if ( node instanceof da_PageFileLeafNode ) {
            newNode = new da_PageFileLeafNode( this.file );
            helpNode = new da_PageFileLeafNode( node.getPageNumber(), this.file );
        } else {
            newNode = new da_PageFileLeafNodeNone( -1, this.file );
            helpNode = new da_PageFileLeafNodeNone( node.getPageNumber(), this.file );
        }

        // write the new node to pagefile
        file.writeNode( newNode );

        node.counter = 0;
        node.unionMinBB = gc_HyperspaceBoundingBox.getNullHyperBoundingBox( file.getDimension() );

        // insert the start entries
        helpNode.insertData( node.getData( seeds[0] ), node.getHyperBoundingBox( seeds[0] ) );
        newNode.insertData( node.getData( seeds[1] ), node.getHyperBoundingBox( seeds[1] ) );

        // mark the inserted entries - first build a marker array
        boolean[] marker = new boolean[file.getCapacity()];
        for ( int i = 0; i < file.getCapacity(); i++ )
            marker[i] = false;

        // mark them
        marker[seeds[0]] = true;
        marker[seeds[1]] = true;

        int doneCounter = file.getCapacity() - 2;

        // do until all entries are put into one of the groups or until
        // one group has so less entries that the remainder must be given to that group
        while ( doneCounter > 0 ) {
            int[] entry;
            entry = pickNext( node, marker, helpNode, newNode );
            doneCounter--;
            if ( entry[0] == 1 )
                helpNode.insertData( node.getData( entry[1] ), node.getHyperBoundingBox( entry[1] ) );
            else
                newNode.insertData( node.getData( entry[1] ), node.getHyperBoundingBox( entry[1] ) );

            if ( ( file.getMinimum() - helpNode.getUsedSpace() ) == doneCounter ) {

                for ( int i = 0; i < file.getCapacity(); i++ )
                    if ( marker[i] == false )
                        helpNode.insertData( node.getData( i ), node.getHyperBoundingBox( i ) );
                break;
            }

            if ( ( file.getMinimum() - newNode.getUsedSpace() ) == doneCounter ) {

                for ( int i = 0; i < file.getCapacity(); i++ )
                    if ( marker[i] == false )
                        newNode.insertData( node.getData( i ), node.getHyperBoundingBox( i ) );
                break;
            }
        }

        // put the entries from the temp node to current node
        for ( int x = 0; x < helpNode.getUsedSpace(); x++ )
            node.insertData( helpNode.getData( x ), helpNode.getHyperBoundingBox( x ) );

        file.writeNode( node );
        file.writeNode( newNode );

        return new da_PageFileNode[] { node, newNode };
    }

    // picks the first to entries for the new nodes - returns the index of the entries
    private int[] pickSeeds( da_PageFileNode node ) {

        double max = 0.0;
        int e1 = 0;
        int e2 = 0;

        // walks through all combinations and takes
        // the combination with the largest area enlargement
        for ( int i = 0; i < file.getCapacity(); i++ )
            for ( int j = 0; j < file.getCapacity(); j++ ) {
                if ( i != j ) {
                    double d = ( node.getHyperBoundingBox( i ) ).unionBoundingBox( node.getHyperBoundingBox( j ) ).getArea()
                               - node.getHyperBoundingBox( i ).getArea() - node.getHyperBoundingBox( j ).getArea();
                    if ( d > max ) {
                        max = d;
                        e1 = i;
                        e2 = j;
                    }
                }
            }

        return new int[] { e1, e2 };
    }

    // int[0] = group, int[1] = entry
    private int[] pickNext( da_PageFileNode node, boolean[] marker, da_PageFileNode group1, da_PageFileNode group2 ) {

        double d0 = 0;
        double d1 = 0;
        double diff = -1;
        double max = -1;
        int entry = 99;
        int group = 99;

        for ( int i = 0; i < file.getCapacity(); i++ ) {
            if ( marker[i] == false ) {
                d0 = group1.getUnionMinBB().unionBoundingBox( node.getHyperBoundingBox( i ) ).getArea()
                     - group1.getUnionMinBB().getArea();

                d1 = group2.getUnionMinBB().unionBoundingBox( node.getHyperBoundingBox( i ) ).getArea()
                     - group2.getUnionMinBB().getArea();
                diff = Math.abs( d0 - d1 );
                if ( diff > max ) {
                    if ( d0 < d1 )
                        group = 1;
                    else
                        group = 2;
                    max = diff;
                    entry = i;
                }
                if ( diff == max ) {
                    if ( d0 < d1 )
                        group = 1;
                    else
                        group = 2;
                    max = diff;
                    entry = i;
                }
            }
        }

        marker[entry] = true;
        return new int[] { group, entry };
    }

    // searches the leafnode with LeastEnlargment criterium for insert
    private da_PageFileLeafNode chooseLeaf( da_PageFileNode node, gc_HyperspaceBoundingBox box ) {

        if ( node instanceof da_PageFileLeafNode ) {
            return (da_PageFileLeafNode) node;
        }
        da_PageFileLeafNodeNone node1 = (da_PageFileLeafNodeNone) node;
        int least = node1.getLeastEnlargement( box );
        return chooseLeaf( (da_PageFileNode) node1.getData( least ), box );

    }

    /**
     * Queries the nearest neighbour to given search HyperPoint
     *
     * @param point -
     *            search point
     * @return double[] - Place 0 = Distance, Place 1 = data number (must be cast to int)
     * @throws le_ExceptionTreeR
     */
    public double[] nearestNeighbour( gc_HyperspacePoint point )
                            throws le_ExceptionTreeR {
        try {
            return nearestNeighbour( file.readNode( 0 ), point, new double[] { Double.POSITIVE_INFINITY, -1.0 } );
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException - nearestNeighbour - readNode(0)" );
        }
    }

    // private method for nearest neighbour query
    private double[] nearestNeighbour( da_PageFileNode node, gc_HyperspacePoint point, double[] temp ) {

        if ( node instanceof da_PageFileLeafNode ) {
            // if mindist this < tempDist
            for ( int i = 0; i < node.getUsedSpace(); i++ ) {
                double dist = node.getHyperBoundingBox( i ).minDist( point );
                if ( dist < temp[0] ) {
                    // then this = nearest Neighbour - update tempDist
                    temp[1] = ( (da_PageFileLeafNode) node ).data[i];
                    temp[0] = dist;
                }
            }

        } else {
            // inner class ABL
            class ABL implements Comparable {

                da_PageFileNode node;

                double minDist;

                /**
                 * @param node
                 * @param minDist
                 */
                public ABL( da_PageFileNode node, double minDist ) {
                    this.node = node;
                    this.minDist = minDist;
                }

                public int compareTo( Object obj ) {
                    ABL help = (ABL) obj;
                    if ( this.minDist < help.minDist )
                        return -1;

                    if ( this.minDist > help.minDist )
                        return 1;
                    return 0;
                }
            }

            // generate ActiveBranchList of node
            ABL[] abl = new ABL[node.getUsedSpace()];

            for ( int i = 0; i < node.getUsedSpace(); i++ ) {
                da_PageFileNode help = (da_PageFileNode) node.getData( i );
                abl[i] = new ABL( help, help.getUnionMinBB().minDist( point ) );
            }

            // sort activebranchlist
            Arrays.sort( abl );

            for ( int i = 0; i < abl.length; i++ ) {
                // apply heuristic 3
                if ( abl[i].minDist <= temp[0] ) {
                    temp = nearestNeighbour( abl[i].node, point, temp );
                }
            }
        }

        return temp;
    }

    /**
     * Closes the rtree.
     *
     * @throws le_ExceptionTreeR -
     *             if an error occures.
     */
    public void close()
                            throws le_ExceptionTreeR {
        try {
            file.close();
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException - close()" );
        }
    }

    /**
     * Deletes an entry from the RTree.
     *
     * @param box -
     *            GeometryHyperspaceBoundingBox of the entry to deleted
     * @param objID -
     *            Integer value of Object-ID to be deleted
     * @return boolean - true if successfull
     * @throws le_ExceptionTreeR
     */
    public boolean delete( gc_HyperspaceBoundingBox box, int objID )
                            throws le_ExceptionTreeR {

        Vector<Object> v = new Vector<Object>( 100 );
        try {
            findLeaf( file.readNode( 0 ), box, objID, v );
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException - delete()" );
        }

        if ( v.size() < 1 )
            return false;

        if ( v.size() == 1 ) {

            da_PageFileLeafNode leaf = (da_PageFileLeafNode) v.elementAt( 0 );

            for ( int i = 0; i < leaf.getUsedSpace(); i++ ) {
                if ( leaf.getHyperBoundingBox( i ).equals( box ) && leaf.data[i] == objID ) {
                    leaf.deleteData( i );

                    try {
                        file.writeNode( leaf );
                    } catch ( le_ExceptionPageFile e ) {
                        e.fillInStackTrace();
                        throw new le_ExceptionTreeR( "PageFileException - delete()" );
                    }
                }
            }

            Stack<Object> stack = new Stack<Object>();
            try {
                condenseTree( leaf, stack );
            } catch ( le_ExceptionPageFile e ) {
                e.fillInStackTrace();
                throw new le_ExceptionTreeR( "PageFileException - condenseTree()" );
            }

            while ( !stack.empty() ) {

                da_PageFileNode node = (da_PageFileNode) stack.pop();

                if ( node instanceof da_PageFileLeafNode ) {
                    for ( int i = 0; i < node.getUsedSpace(); i++ )
                        this.insert( ( (da_PageFileLeafNode) node ).getData( i ), ( (da_PageFileLeafNode) node ).getHyperBoundingBox( i ) );
                } else {
                    for ( int i = 0; i < node.getUsedSpace(); i++ )
                        stack.push( ( (da_PageFileLeafNodeNone) node ).getData( i ) );
                }

                try {
                    file.deleteNode( node.pageNumber );
                } catch ( le_ExceptionPageFile e ) {
                    e.fillInStackTrace();
                    throw new le_ExceptionTreeR( "PageFileException - delete() - deleteNode(0)" );
                }
            }
        }

        return true;
    }

    /**
     * Deletes all entries from the R-Tree with given GeometryHyperspaceBoundingBox
     *
     * @param box -
     *            GeometryHyperspaceBoundingBox
     * @return boolean - true if successfull
     * @throws le_ExceptionTreeR
     */
    public boolean delete( gc_HyperspaceBoundingBox box )
                            throws le_ExceptionTreeR {

        Vector<Object> v = new Vector<Object>( 100 );
        try {
            findLeaf( file.readNode( 0 ), box, v );
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException - delete()" );
        }

        if ( v.size() < 1 )
            return false;

        da_PageFileLeafNode leaf;

        for ( Enumeration en = v.elements(); en.hasMoreElements(); ) {

            leaf = (da_PageFileLeafNode) en.nextElement();

            for ( int i = 0; i < leaf.getUsedSpace(); i++ ) {
                if ( leaf.getHyperBoundingBox( i ).equals( box ) ) {
                    leaf.deleteData( i );

                    try {
                        file.writeNode( leaf );
                    } catch ( le_ExceptionPageFile e ) {
                        e.fillInStackTrace();
                        throw new le_ExceptionTreeR( "PageFileException - delete()" );
                    }
                }
            }

            Stack<Object> stack = new Stack<Object>();
            try {
                condenseTree( leaf, stack );
            } catch ( le_ExceptionPageFile e ) {
                e.fillInStackTrace();
                throw new le_ExceptionTreeR( "PageFileException - condenseTree()" );
            }

            while ( !stack.empty() ) {

                da_PageFileNode node = (da_PageFileNode) stack.pop();

                if ( node instanceof da_PageFileLeafNode ) {
                    for ( int i = 0; i < node.getUsedSpace(); i++ )
                        this.insert( ( (da_PageFileLeafNode) node ).getData( i ), ( (da_PageFileLeafNode) node ).getHyperBoundingBox( i ) );
                } else {
                    for ( int i = 0; i < node.getUsedSpace(); i++ )
                        stack.push( ( (da_PageFileLeafNodeNone) node ).getData( i ) );
                }

                try {
                    file.deleteNode( node.pageNumber );
                } catch ( le_ExceptionPageFile e ) {
                    e.fillInStackTrace();
                    throw new le_ExceptionTreeR( "PageFileException - delete() - deleteNode(0)" );
                }
            }
        }

        return true;
    }

    /**
     * Retrieves all entries with the given GeometryHyperspaceBoundingBox.
     *
     * @param box -
     *            GeometryHyperspaceBoundingBox
     * @return Object[] - array with retrieved objects
     * @throws le_ExceptionTreeR
     */
    public Object[] find( gc_HyperspaceBoundingBox box )
                            throws le_ExceptionTreeR {
        if ( box.getDimension() != file.getDimension() )
            throw new IllegalArgumentException( "GeometryHyperspaceBoundingBox has wrong dimension" );

        Vector<Object> v = new Vector<Object>( 100 );
        // ruft die eigentliche suche auf
        try {
            findSearch( file.readNode( 0 ), v, box );
        } catch ( le_ExceptionPageFile e ) {
            e.fillInStackTrace();
            throw new le_ExceptionTreeR( "PageFileException RTree.search() - readNode()" );
        }

        return v.toArray();
    }

    // Fï¿½hrt die eigentliche Suche durch - Aufruf von search(GeometryHyperspaceBoundingBox box)
    private void findSearch( da_PageFileNode node1, Vector<Object> v, gc_HyperspaceBoundingBox box ) {
        if ( node1 instanceof da_PageFileLeafNode ) {
            da_PageFileLeafNode node = (da_PageFileLeafNode) node1;

            for ( int i = 0; i < node.getUsedSpace(); i++ ) {
                // wenn eintraege enthalten diese in Vechtor aufnehmen;
                if ( node.hyperBBs[i].equals( box ) )
                    v.addElement( node.getData( i ) );
            }
            return;
        }

        da_PageFileLeafNodeNone node = (da_PageFileLeafNodeNone) node1;

        // node ist kein PageFileLeafNode
        // alle eintrraege auf ï¿½berlappung durchsuchen
        for ( int i = 0; i < node.getUsedSpace(); i++ ) {
            // wenn enthalten rekursiv search mit diesem node aufrufen
            if ( node.hyperBBs[i].contains( box ) ) {
                findSearch( (da_PageFileNode) node.getData( i ), v, box );
            }
        }

    }

    // Retrieves all leaf nodes regardless of the id
    private void findLeaf( da_PageFileNode node, gc_HyperspaceBoundingBox box, Vector<Object> v ) {
        if ( node instanceof da_PageFileLeafNode ) {
            for ( int i = 0; i < node.getUsedSpace(); i++ ) {
                if ( node.getHyperBoundingBox( i ).equals( box ) )
                    v.addElement( node );
            }
        } else {
            for ( int i = 0; i < node.getUsedSpace(); i++ ) {
                if ( node.getHyperBoundingBox( i ).overlaps( box ) )
                    findLeaf( (da_PageFileNode) node.getData( i ), box, v );
            }
        }
    }

    // Retrieves all leaf nodes with correct box and id
    private void findLeaf( da_PageFileNode node, gc_HyperspaceBoundingBox box, int objID, Vector<Object> v ) {
        if ( node instanceof da_PageFileLeafNode ) {
            for ( int i = 0; i < node.getUsedSpace(); i++ ) {
                if ( ( (da_PageFileLeafNode) node ).data[i] == objID && node.getHyperBoundingBox( i ).equals( box ) )
                    v.addElement( node );
            }
        } else {
            for ( int i = 0; i < node.getUsedSpace(); i++ ) {
                if ( node.getHyperBoundingBox( i ).overlaps( box ) )
                    findLeaf( (da_PageFileNode) node.getData( i ), box, objID, v );
            }
        }
    }

    // condenses the tree after remove of some entries
    private void condenseTree( da_PageFileNode n, Stack<Object> stack )
                            throws le_ExceptionPageFile {
        if ( !n.isRoot() ) {

            da_PageFileNode p = n.getParent();
            if ( n.getUsedSpace() < file.getMinimum() ) {
                p.deleteData( n.place );
                stack.push( n );
            } else {
                p.hyperBBs[n.place] = n.getUnionMinBB();
                p.updateNodeBoundingBox();
            }

            file.writeNode( p );

            condenseTree( p, stack );
        } else {
            if ( n.getUsedSpace() == 1 && ( n instanceof da_PageFileLeafNodeNone ) ) {

                da_PageFileNode kind = (da_PageFileNode) n.getData( 0 );
                da_PageFileNode newRoot = null;
                if ( kind instanceof da_PageFileLeafNode ) {
                    newRoot = new da_PageFileLeafNode( 0, this.file );
                    for ( int i = 0; i < kind.getUsedSpace(); i++ )
                        newRoot.insertData( kind.getData( i ), kind.getHyperBoundingBox( i ) );
                } else {
                    newRoot = new da_PageFileLeafNodeNone( 0, this.file );
                    for ( int i = 0; i < kind.getUsedSpace(); i++ )
                        newRoot.insertData( kind.getData( i ), kind.getHyperBoundingBox( i ) );
                }

                file.writeNode( newRoot );
            }
        }
    }

    // adjustes the Tree with the correct bounding boxes and
    // propagates needed splits upwards
    private void adjustTree( da_PageFileNode n1, da_PageFileNode n2 )
                            throws le_ExceptionPageFile {
        // if n2 = null - only adjust boundingboxes
        // if n2 != null a split occured - maybe propagate split

        if ( n1.isRoot() ) {

            // if n2 != null we need a new Root node - Root Split
            if ( ( n2 != null ) && n1.isRoot() ) {

                // PageFileNode must be written from page number 0 (root number) to other
                n1.setPageNumber( -1 );
                int pagenumber;

                pagenumber = file.writeNode( n1 );

                for ( int x = 0; x < n1.getUsedSpace(); x++ ) {
                    Object obj = n1.getData( x );

                    if ( obj instanceof da_PageFileNode ) {
                        da_PageFileNode node = (da_PageFileNode) obj;
                        node.parentNode = pagenumber;
                        file.writeNode( node );
                    }

                    obj = null;
                }

                da_PageFileLeafNodeNone newRoot = new da_PageFileLeafNodeNone( 0, this.file );

                newRoot.insertData( n1, n1.getUnionMinBB() );
                newRoot.insertData( n2, n2.getUnionMinBB() );
                newRoot.parentNode = 0;

                file.writeNode( newRoot );
            }

            return;
        }

        // adjust the bounding boxes in the parents for PageFileNode n1
        da_PageFileLeafNodeNone p = (da_PageFileLeafNodeNone) n1.getParent();
        p.hyperBBs[n1.place] = n1.getUnionMinBB();
        p.unionMinBB = ( p.getUnionMinBB() ).unionBoundingBox( n1.getUnionMinBB() );

        file.writeNode( p );

        // propagate adjustment upwards
        if ( n2 == null ) {
            adjustTree( p, null );
        } else {
            // as there occured a split - the second node has to be inserted
            da_PageFileNode[] newNodes = new da_PageFileNode[] { null, null };
            if ( p.getUsedSpace() < ( file.getCapacity() - 1 ) ) {
                // new split must happen
                p.insertData( n2, n2.getUnionMinBB() );
                file.writeNode( p );
                newNodes[0] = p;
            } else {
                p.insertData( n2, n2.getUnionMinBB() );
                file.writeNode( p );
                newNodes = splitNode( p );
            }

            adjustTree( newNodes[0], newNodes[1] );
        }
    }
}
