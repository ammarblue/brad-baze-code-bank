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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Stack;


/**
 * <p>
 * A persistent implementation of a PageFile implemented based on a RandomAccessFile.
 * </p>
 * <p>
 * Structure of the File<br>
 * <br>
 * <br> -- Header --<br>
 * int pageFileVersion<br>
 * int dimension<br>
 * int capacity = maxLoad + 1 for Overflow<br>
 * int minimum<br>
 * <br>
 * <br> -- Body --<br>
 * a sequence of page one after another with:<br>
 * int typ - 1 PageFileLeafNode 2 PageFileLeafNodeNone<br>
 * int place - index of entry of this node in father node<br>
 * int counter - current used space in the node<br>
 * int parentNode - page number of father node<br>
 * int pageNumber - own page number<br> - for(i = 0; i < capacity; i++)<br>
 * int data Entry i - page number of childnode or object ID of data entry<br> - always dependend on
 * dimension = x<br>
 * double pMin x.Dimension - pMin of the common HyperBoundingBox<br>
 * double pMax x.Dimension - pMax of the common HyperBoundingBox<br> - for(i = 0; i < capacity;
 * i++)<br>
 * double pMin x.Dimension - pMin HyperBoundingBox for Entry i<br>
 * double pMax x.Dimension - pMax HyperBoundingBox for Entry i<br>
 * <br>
 * <br>
 * int entspr. 4 Bytes - double entspr. 8 Bytes<br>
 * <br>
 * <br>
 * PageSize = (4 * (5 + capacity)) + (capacity + 1) * (dimension * 16)<br>
 * <br>
 * </p>
 *
 * @author Wolfgang Baer - WBaer@gmx.de
 */
class da_PageFilePersistent extends da_a_PageFile {

    /** magic number */
    private static final int PAGEFILE_VERSION = 060676002;

    private static final int EMPTY_PAGE = -22;

    private RandomAccessFile file;

    private int pageSize;

    private String fileName;

    private byte[] buffer;

    private Stack<Integer> emptyPages;

    private boolean closed;

    /**
     * Constructor
     *
     * @param fileName
     */
    protected da_PageFilePersistent( String fileName ) {
        super();
        this.fileName = fileName;
        this.emptyPages = new Stack<Integer>();
        this.closed = false;
    }

    /**
     * Initializes the PersistentPageFile. Overrides initialize in PageFile.
     *
     * @param dimension -
     *            dimension of the data
     * @param capacity -
     *            capacity of a node
     * @throws le_ExceptionPageFile
     */
    protected void initialize( int dimension, int capacity )
                            throws le_ExceptionPageFile {
        super.initialize( dimension, capacity );

        File fileTest = new File( fileName );

        try {
            if ( dimension == -999 ) {
                // Initialize from existing file

                if ( !fileTest.exists() )
                    throw new le_ExceptionPageFile( "File does not exist" );

                file = new RandomAccessFile( fileTest, "rw" );
                // Test if it is a PersistentPageFile
                file.seek( 0 );
                if ( file.readInt() != PAGEFILE_VERSION )
                    throw new le_ExceptionPageFile( "Not a PersistentPageFile or wrong version" );

                // Reading header - Initializing PageFile
                this.dimension = file.readInt();
                this.capacity = file.readInt();
                this.minimum = file.readInt();
                this.pageSize = ( ( 4 * ( 5 + this.capacity ) ) + ( ( this.capacity + 1 ) * ( this.dimension * 16 ) ) );
                this.buffer = new byte[pageSize];

                // reading empty pages in Stack
                int i = 0;
                try {
                    while ( true ) {
                        file.seek( 16 + ( i * pageSize ) );
                        if ( EMPTY_PAGE == file.readInt() )
                            emptyPages.push( new Integer( i ) );
                        i++;
                    }
                } catch ( EOFException eof ) { // not an exception - wanted
                }
            } else {
                // new file
                file = new RandomAccessFile( fileTest, "rw" );
                file.setLength( 0 );
                this.pageSize = ( ( 4 * ( 5 + capacity ) ) + ( ( capacity + 1 ) * ( dimension * 16 ) ) );
                this.buffer = new byte[pageSize];

                // writing header
                file.seek( 0 );
                file.writeInt( PAGEFILE_VERSION );
                file.writeInt( this.dimension );
                file.writeInt( this.capacity );
                file.writeInt( this.minimum );

            }
        } catch ( IOException e ) {
            e.fillInStackTrace();
            throw new le_ExceptionPageFile( "IOException occured: \n " + e.getMessage() );
        }
    }

    /**
     * @see da_a_PageFile#readNode(int)
     */
    protected da_PageFileNode readNode( int pageFileNumber )
                            throws le_ExceptionPageFile {

        da_PageFileNode node = null;

        try {
            file.seek( 16 + ( pageFileNumber * pageSize ) );

            int read = file.read( buffer );

            if ( pageSize == read ) {

                DataInputStream ds = new DataInputStream( new ByteArrayInputStream( buffer ) );

                int type = ds.readInt();
                if ( type == 1 )
                    node = new da_PageFileLeafNode( -1, this );
                else
                    node = new da_PageFileLeafNodeNone( -1, this );

                node.place = ds.readInt();
                node.counter = ds.readInt();
                node.parentNode = ds.readInt();
                node.pageNumber = ds.readInt();

                if ( type == 1 ) {
                    for ( int i = 0; i < capacity; i++ )
                        ( (da_PageFileLeafNode) node ).data[i] = ds.readInt();
                } else {
                    for ( int i = 0; i < capacity; i++ )
                        ( (da_PageFileLeafNodeNone) node ).childNodes[i] = ds.readInt();
                }

                node.unionMinBB = readNextHyperBoundingBox( ds );

                for ( int i = 0; i < capacity; i++ )
                    node.hyperBBs[i] = readNextHyperBoundingBox( ds );

                ds.close();
            } else {
                throw new le_ExceptionPageFile( "Exception during read operation" );
            }

            return node;

        } catch ( IOException e ) {
            e.fillInStackTrace();
            throw new le_ExceptionPageFile( "PageFileException occured ! \n " + e.getMessage() );
        }
    }

    // reads the next HyperBoundingBox from the byte buffer
    private gc_HyperspaceBoundingBox readNextHyperBoundingBox( DataInputStream ds )
                            throws IOException {

        double[] point1, point2;
        point1 = new double[dimension];
        point2 = new double[dimension];

        for ( int i = 0; i < dimension; i++ )
            point1[i] = ds.readDouble();

        for ( int i = 0; i < dimension; i++ )
            point2[i] = ds.readDouble();

        return new gc_HyperspaceBoundingBox( new gc_HyperspacePoint( point1 ), new gc_HyperspacePoint( point2 ) );
    }

    /**
     * @see da_a_PageFile#writeNode(da_PageFileNode)
     */
    protected int writeNode( da_PageFileNode node )
                            throws le_ExceptionPageFile {
        try {

            if ( node.pageNumber < 0 ) {
                if ( !emptyPages.empty() )
                    node.setPageNumber( emptyPages.pop() );
                else
                    node.setPageNumber( (int) ( ( file.length() - 16 ) / pageSize ) );
            }

            ByteArrayOutputStream bs = new ByteArrayOutputStream( pageSize );
            DataOutputStream ds = new DataOutputStream( bs );

            int type;
            if ( node instanceof da_PageFileLeafNode )
                type = 1;
            else
                type = 2;

            ds.writeInt( type );

            ds.writeInt( node.place );
            ds.writeInt( node.counter );
            ds.writeInt( node.parentNode );
            ds.writeInt( node.pageNumber );

            if ( node instanceof da_PageFileLeafNode ) {
                for ( int i = 0; i < node.counter; i++ ) {
                    ds.writeInt( ( (da_PageFileLeafNode) node ).data[i] );
                }
                for ( int i = 0; i < ( capacity - node.counter ); i++ )
                    ds.writeInt( -1 );
            } else {
                for ( int i = 0; i < node.counter; i++ ) {
                    ds.writeInt( ( (da_PageFileLeafNodeNone) node ).childNodes[i] );
                }

                for ( int i = 0; i < ( capacity - node.counter ); i++ )
                    ds.writeInt( -1 );
            }

            for ( int i = 0; i < dimension; i++ )
                ds.writeDouble( node.unionMinBB.getPMin().getCoord( i ) );

            for ( int i = 0; i < dimension; i++ )
                ds.writeDouble( node.unionMinBB.getPMax().getCoord( i ) );

            for ( int j = 0; j < node.counter; j++ ) {
                for ( int i = 0; i < dimension; i++ )
                    ds.writeDouble( node.hyperBBs[j].getPMin().getCoord( i ) );

                for ( int i = 0; i < dimension; i++ )
                    ds.writeDouble( node.hyperBBs[j].getPMax().getCoord( i ) );
            }

            for ( int j = 0; j < ( capacity - node.counter ); j++ ) {
                for ( int i = 0; i < ( dimension * 2 ); i++ )
                    ds.writeDouble( -1 );
            }

            ds.flush();
            bs.flush();

            file.seek( 16 + ( pageSize * node.pageNumber ) );

            file.write( bs.toByteArray() );

            ds.close();

            return node.pageNumber;

        } catch ( IOException e ) {
            e.fillInStackTrace();
            throw new le_ExceptionPageFile( "PageFileException occured ! \n " + e.getMessage() );
        }
    }

    /**
     * @see da_a_PageFile#deleteNode(int)
     */
    protected da_PageFileNode deleteNode( int pageNumber )
                            throws le_ExceptionPageFile {
        da_PageFileNode node = this.readNode( pageNumber );
        try {
            file.seek( 16 + ( pageSize * node.pageNumber ) );
            file.writeInt( EMPTY_PAGE );
        } catch ( IOException e ) {
            e.fillInStackTrace();
            throw new le_ExceptionPageFile( "PageFileException occured ! \n " + e.getMessage() );
        }

        emptyPages.push( new Integer( pageNumber ) );
        return node;
    }

    /**
     * @see da_a_PageFile#close()
     */
    protected void close()
                            throws le_ExceptionPageFile {
        try {
            file.close();
        } catch ( IOException e ) {
            e.fillInStackTrace();
            throw new le_ExceptionPageFile( "PageFileException during close()" );
        }

        closed = true;
    }

    protected void finalize()
                            throws Throwable {
        if ( !closed )
            file.close();

        super.finalize();
    }
}
