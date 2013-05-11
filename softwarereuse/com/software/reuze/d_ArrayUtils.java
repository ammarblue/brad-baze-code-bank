package com.software.reuze;
/**
 * Copyright (c) 2007-2011, JAGaToo Project Group all rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of the 'Xith3D Project Group' nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
 * RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE
 */


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * Helper class for array operations.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public final class d_ArrayUtils
{
    /**
     * @return true, if both float arrays are either the same instance (or both null),
     * or the same length and all elements are equal.
     * 
     * @param a
     * @param b
     * @param precision, tolerance to use on comparison
     */
    public static final boolean equals( float[] a, float[] b, float precision )
    {
        if ( a == b )
            return ( true );
        
        if ( a == null )
            return ( false );
        
        if ( b == null )
            return ( false );
        
        if ( a.length != b.length )
            return ( false );
        
        for ( int i = 0; i < a.length; i++ )
        {
            if ( Math.abs(a[ i ]-b[ i ])>precision ) return ( false );
        }
        
        return ( true );
    }
    /**
     * @return true, if both double arrays are either the same instance (or both null),
     * or the same length and all elements are equal.
     * 
     * @param a
     * @param b
     * @param precision, tolerance to use on comparison
     */
    public static final boolean equals( double[] a, double[] b, double precision )
    {
        if ( a == b )
            return ( true );
        
        if ( a == null )
            return ( false );
        
        if ( b == null )
            return ( false );
        
        if ( a.length != b.length )
            return ( false );
        
        for ( int i = 0; i < a.length; i++ )
        {
            if ( Math.abs(a[ i ]-b[ i ])>precision ) return ( false );
        }
        
        return ( true );
    }
    
    public static final <T extends Number> int equalCount(ArrayList<T> a, ArrayList<T> b, float precision) {
    	if ( a == b ) return a.size();        
        if ( a == null ) return 0;        
        if ( b == null ) return 0;        
        if ( a.size() != b.size() ) return 0;
        int matches=0;
        for ( int i = 0; i < a.size(); i++ ) {
            if ( Math.abs(a.get(i).doubleValue()-b.get(i).doubleValue())<=precision ) matches++;
        }        
        return matches;
    }
        
    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * 
     * @return the array with the ensured length
     */
    public static final int[] ensureCapacity( int[] array, int minCapacity )
    {
        final int oldCapacity = array.length;
        
        if ( minCapacity > oldCapacity )
        {
            int[] oldArray = array;
            int newCapacity = ( oldCapacity * 3 ) / 2 + 1;
            if ( newCapacity < minCapacity )
                newCapacity = ( minCapacity * 3 ) / 2 + 1;
            array = new int[ newCapacity ];
            System.arraycopy( oldArray, 0, array, 0, oldCapacity );
        }
        
        return ( array );
    }
    
    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * @param paddValue the value to be written to appended elements
     * 
     * @return the array with the ensured length
     */
    public static final int[] ensureCapacity( int[] array, int minCapacity, int paddValue )
    {
        final int oldCapacity = array.length;
        
        if ( minCapacity > oldCapacity )
        {
            int[] oldArray = array;
            int newCapacity = ( oldCapacity * 3 ) / 2 + 1;
            if ( newCapacity < minCapacity )
                newCapacity = ( minCapacity * 3 ) / 2 + 1;
            array = new int[ newCapacity ];
            System.arraycopy( oldArray, 0, array, 0, oldCapacity );
            Arrays.fill( array, oldCapacity, newCapacity - 1, paddValue );
        }
        
        return ( array );
    }
    
    /**
     * Ensures, the given long array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * 
     * @return the array with the ensured length
     */
    public static final long[] ensureCapacity( long[] array, int minCapacity )
    {
        final int oldCapacity = array.length;
        
        if ( minCapacity > oldCapacity )
        {
            long[] oldArray = array;
            int newCapacity = ( oldCapacity * 3 ) / 2 + 1;
            if ( newCapacity < minCapacity )
                newCapacity = ( minCapacity * 3 ) / 2 + 1;
            array = new long[ newCapacity ];
            System.arraycopy( oldArray, 0, array, 0, oldCapacity );
        }
        
        return ( array );
    }
    
    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * @param paddValue the value to be written to appended elements
     * 
     * @return the array with the ensured length
     */
    public static final long[] ensureCapacity( long[] array, int minCapacity, int paddValue )
    {
        final int oldCapacity = array.length;
        
        if ( minCapacity > oldCapacity )
        {
            long[] oldArray = array;
            int newCapacity = ( oldCapacity * 3 ) / 2 + 1;
            if ( newCapacity < minCapacity )
                newCapacity = ( minCapacity * 3 ) / 2 + 1;
            array = new long[ newCapacity ];
            System.arraycopy( oldArray, 0, array, 0, oldCapacity );
            Arrays.fill( array, oldCapacity, newCapacity - 1, paddValue );
        }
        
        return ( array );
    }
    
    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * 
     * @return the array with the ensured length
     */
    public static final float[] ensureCapacity( float[] array, int minCapacity )
    {
        final int oldCapacity = array.length;
        
        if ( minCapacity > oldCapacity )
        {
            float[] oldArray = array;
            int newCapacity = ( oldCapacity * 3 ) / 2 + 1;
            if ( newCapacity < minCapacity )
                newCapacity = ( minCapacity * 3 ) / 2 + 1;
            array = new float[ newCapacity ];
            System.arraycopy( oldArray, 0, array, 0, oldCapacity );
        }
        
        return ( array );
    }
    
    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param minCapacity the desired (minimal) capacity
     * @param paddValue the value to be written to appended elements
     * 
     * @return the array with the ensured length
     */
    public static final float[] ensureCapacity( float[] array, int minCapacity, int paddValue )
    {
        final int oldCapacity = array.length;
        
        if ( minCapacity > oldCapacity )
        {
            float[] oldArray = array;
            int newCapacity = ( oldCapacity * 3 ) / 2 + 1;
            if ( newCapacity < minCapacity )
                newCapacity = ( minCapacity * 3 ) / 2 + 1;
            array = new float[ newCapacity ];
            System.arraycopy( oldArray, 0, array, 0, oldCapacity );
            Arrays.fill( array, oldCapacity, newCapacity - 1, paddValue );
        }
        
        return ( array );
    }
    
    /**
     * Ensures, the given int array has the desired length.<br>
     * <b>The ensured array is returned!</b>
     * 
     * @param array the input array
     * @param elementType
     * @param minCapacity the desired (minimal) capacity
     * 
     * @param <E>
     * 
     * @return the array with the ensured length
     */
    @SuppressWarnings( "unchecked" )
    public static final <E> E[] ensureCapacity( E[] array, Class<?> elementType, int minCapacity )
    {
        final int oldCapacity = array.length;
        
        if ( minCapacity > oldCapacity )
        {
            E[] oldArray = array;
            int newCapacity = ( oldCapacity * 3 ) / 2 + 1;
            if ( newCapacity < minCapacity )
                newCapacity = ( minCapacity * 3 ) / 2 + 1;
            array = (E[])Array.newInstance( elementType, newCapacity );
            System.arraycopy( oldArray, 0, array, 0, oldCapacity );
        }
        
        return ( array );
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( byte[] array, int start, int limit, byte element )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( array[ i ] == element )
                return ( true );
        }
        
        return ( false );
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( byte[] array, byte element )
    {
    	return indexOf(array, element)!=-1;
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( short[] array, int start, int limit, short element )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( array[ i ] == element )
                return ( true );
        }
        
        return ( false );
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( short[] array, short element )
    {
    	return indexOf(array, element)!=-1;
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( int[] array, int start, int limit, int element )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( array[ i ] == element )
                return ( true );
        }
        
        return ( false );
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( int[] array, int element )
    {
    	return indexOf(array, element)!=-1;
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( long[] array, int start, int limit, long element )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( array[ i ] == element )
                return ( true );
        }
        
        return ( false );
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( long[] array, long element )
    {
    	return indexOf(array, element)!=-1;
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( float[] array, int start, int limit, float element, float precision )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( Math.abs(array[ i ]-element)<=precision )
                return ( true );
        }
        
        return ( false );
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( float[] array, float element )
    {
    	return indexOf(array, element)!=-1;
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( double[] array, int start, int limit, double element, double precision )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( Math.abs(array[ i ]-element)<=precision )
                return ( true );
        }
        
        return ( false );
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( double[] array, double element )
    {
        return indexOf(array, element)!=-1;
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * @param strict if <code>true</code>, a == check is used to identify the element, otherwise, the equals method is used.
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( Object[] array, int start, int limit, Object element, boolean strict )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( strict || ( array[ i ] == null ) )
            {
                if ( array[ i ] == element )
                    return ( true );
            }
            else if ( array[ i ].equals( element ) )
            {
                return ( true );
            }
        }
        
        return ( false );
    }
    
    /**
     * Checks, if the specified array contains the specified element.
     * 
     * @param array the array to check
     * @param element the element to search
     * @param strict if <code>true</code>, a == check is used to identify the element, otherwise, the equals method is used.
     * 
     * @return <code>true</code>, if the array contains the specified element.
     */
    public static final boolean contains( Object[] array, Object element, boolean strict )
    {
        for ( int i = 0; i < array.length; i++ )
        {
            if ( strict || ( array[ i ] == null ) )
            {
                if ( array[ i ] == element )
                    return ( true );
            }
            else if ( array[ i ].equals( element ) )
            {
                return ( true );
            }
        }
        
        return ( false );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( byte[] array, int start, int limit, byte element )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( array[ i ] == element )
                return ( i );
        }
        
        return ( -1 );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( byte[] array, byte element )
    {
        return ( indexOf( array, 0, array.length - 1, element ) );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( short[] array, int start, int limit, short element )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( array[ i ] == element )
                return ( i );
        }
        
        return ( -1 );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( short[] array, short element )
    {
        return ( indexOf( array, 0, array.length - 1, element ) );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( int[] array, int start, int limit, int element )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( array[ i ] == element )
                return ( i );
        }
        
        return ( -1 );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( int[] array, int element )
    {
        return ( indexOf( array, 0, array.length - 1, element ) );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( long[] array, int start, int limit, long element )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( array[ i ] == element )
                return ( i );
        }
        
        return ( -1 );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( long[] array, long element )
    {
        return ( indexOf( array, 0, array.length - 1, element ) );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( float[] array, int start, int limit, float element, float precision )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( Math.abs(array[ i ]-element)<=precision )
                return ( i );
        }
        
        return ( -1 );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( float[] array, float element )
    {
        return ( indexOf( array, 0, array.length - 1, element, 0f ) );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( double[] array, int start, int limit, double element, double precision )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( Math.abs(array[ i ]-element)<=precision )
                return ( i );
        }
        
        return ( -1 );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param element the element to search
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( double[] array, double element )
    {
        return ( indexOf( array, 0, array.length - 1, element, 0d ) );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param start the index in the source array to start the search at
     * @param limit the index in the source array of the last element to be tested
     * @param element the element to search
     * @param strict if <code>true</code>, a == check is used to identify the element, otherwise, the equals method is used.
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( Object[] array, int start, int limit, Object element, boolean strict )
    {
        for ( int i = start; i <= limit; i++ )
        {
            if ( strict || ( array[ i ] == null ) )
            {
                if ( array[ i ] == element )
                    return ( i );
            }
            else if ( array[ i ].equals( element ) )
            {
                return ( i );
            }
        }
        
        return ( -1 );
    }
    
    /**
     * Searches the specified element inside the specified array.
     * 
     * @param array the array to check
     * @param element the element to search
     * @param strict if <code>true</code>, a == check is used to identify the element, otherwise, the equals method is used.
     * 
     * @return the element's index within the array or -1, if the array does not contain the specified element.
     */
    public static final int indexOf( Object[] array, Object element, boolean strict )
    {
        return ( indexOf( array, 0, array.length - 1, element, strict ) );
    }
    
    private d_ArrayUtils()
    {
    }
    /**
     * Reverses the item order of the supplied byte array.
     * 
     * @param array
     */
    public static void reverse(byte[] array) {
        int len = array.length - 1;
        int len2 = array.length / 2;
        for (int i = 0; i < len2; i++) {
            byte tmp = array[i];
            array[i] = array[len - i];
            array[len - i] = tmp;
        }
    }

    /**
     * Reverses the item order of the supplied char array.
     * 
     * @param array
     */
    public static void reverse(char[] array) {
        int len = array.length - 1;
        int len2 = array.length / 2;
        for (int i = 0; i < len2; i++) {
            char tmp = array[i];
            array[i] = array[len - i];
            array[len - i] = tmp;
        }
    }

    /**
     * Reverses the item order of the supplied float array.
     * 
     * @param array
     */
    public static void reverse(float[] array) {
        int len = array.length - 1;
        int len2 = array.length / 2;
        for (int i = 0; i < len2; i++) {
            float tmp = array[i];
            array[i] = array[len - i];
            array[len - i] = tmp;
        }
    }

    /**
     * Reverses the item order of the supplied int array.
     * 
     * @param array
     */
    public static void reverse(int[] array) {
        int len = array.length - 1;
        int len2 = array.length / 2;
        for (int i = 0; i < len2; i++) {
            int tmp = array[i];
            array[i] = array[len - i];
            array[len - i] = tmp;
        }
    }

    /**
     * Reverses the item order of the supplied short array.
     * 
     * @param array
     */
    public static void reverse(short[] array) {
        int len = array.length - 1;
        int len2 = array.length / 2;
        for (int i = 0; i < len2; i++) {
            short tmp = array[i];
            array[i] = array[len - i];
            array[len - i] = tmp;
        }
    }

    /**
     * Reverses the item order of the supplied array (generic types).
     * 
     * @param array
     */
    public static <T> void reverse(T[] array) {
        int len = array.length - 1;
        int len2 = array.length / 2;
        for (int i = 0; i < len2; i++) {
            T tmp = array[i];
            array[i] = array[len - i];
            array[len - i] = tmp;
        }
    }

    /**
     * Rearranges the array items in random order using the default
     * java.util.Random generator. Operation is in-place, no copy is created.
     * 
     * @param array
     */
    public static <T> void shuffle(T[] array) {
        shuffle(array, new Random());
    }

    /**
     * Rearranges the array items in random order using the given RNG. Operation
     * is in-place, no copy is created.
     * 
     * @param array
     * @param rnd
     */
    public static <T> void shuffle(T[] array, Random rnd) {
        int N = array.length;
        for (int i = 0; i < N; i++) {
            int r = i + rnd.nextInt(N - i); // between i and N-1
            T swap = array[i];
            array[i] = array[r];
            array[r] = swap;
        }
    }
    /**
     * Adds all array elements to the given collection of the same type.
     * 
     * @param collection
     *            existing collection
     * @param array
     *            array
     */
    public static <T> void addArrayToCollection(Collection<T> collection, T... array) {
        for (T o : array) {
            collection.add(o);
        }
    }

    /**
     * Converts the generic array into an {@link ArrayList} of the same type.
     * 
     * @param array
     * @return array list version
     */
    public static <T> ArrayList<T> arrayToList(T[] array) {
        ArrayList<T> list = new ArrayList<T>(array.length);
        for (T element : array) {
            list.add(element);
        }
        return list;
    }
}
