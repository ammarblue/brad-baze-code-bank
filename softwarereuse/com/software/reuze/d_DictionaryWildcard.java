package com.software.reuze;
// WildcardDictionary - a dictionary with wildcard lookups
//
// Copyright (C) 1996 by Jef Poskanzer <jef@acme.com>.  All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// Visit the ACME Labs Java page for up-to-date versions of this and other
// fine Java utilities: http://www.acme.com/java/

//package Acme;


import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.regex.Pattern;

import com.software.reuze.d_Pair;

/// A dictionary with wildcard lookups.
// <P>
// The keys in this dictionary are wildcard patterns.  When you do a get(),
// the string you pass in is matched against all the patterns, and the
// first match is returned.
// <P>
// The wildcard matcher uses java.regex.matches.
// <P>
// <A HREF="/resources/classes/Acme/WildcardDictionary.java">Fetch the software.</A><BR>
// <A HREF="/resources/classes/Acme.tar.gz">Fetch the entire Acme package.</A>
// <P>
//TODO should be changed to use Map
public class d_DictionaryWildcard extends Dictionary implements Iterable /*RPC0812*/
    {

    private Vector<String> keys;
    private Vector elements;

    /// Constructor.
    public d_DictionaryWildcard()
	{
	keys = new Vector<String>();
	elements = new Vector();
	}

    /// Returns the number of elements contained within the dictionary.
    public int size()
	{
	return elements.size();
	}

    /// Returns true if the dictionary contains no elements.
    public boolean isEmpty()
	{
	return size() == 0;
	}

    /// Returns an enumeration of the dictionary's keys.
    public Enumeration keys()
	{
	return keys.elements();
	}

    /// Returns an enumeration of the elements. Use the Enumeration methods
    // on the returned object to fetch the elements sequentially.
    public Enumeration elements()
	{
	return elements.elements();
	}

    /// Gets the object associated with the specified key in the dictionary.
    // The key is assumed to be a String, which is matched against
    // the wildcard-pattern keys in the dictionary.
    // @param key the string to match
    // @returns the element for the key, or null if there's no match
    public synchronized Object get( Object key )
	{
	String sKey = (String) key;
	for ( int i = 0; i < keys.size(); ++i )
	    {
	    String thisKey = (String) keys.elementAt( i );
	    if ( thisKey.matches(sKey) )
		return elements.elementAt( i );
	    }
	return null;
	}
    private static d_Pair<Object, Object> p=new d_Pair<Object, Object>();
    /// Gets the object associated with the specified key in the dictionary.
    // The key is assumed to be a String, which is matched against
    // the wildcard-pattern keys in the dictionary.
    // @param key the string to match
    // @returns the matching key and its value, or null if there's no match
    public synchronized d_Pair<Object, Object> getPair( Object key )
	{
	String sKey = (String) key;
	for ( int i = 0; i < keys.size(); ++i )
	    {
	    String thisKey = (String) keys.elementAt( i );
	    if ( thisKey.matches(sKey) ) {
	    	p.a=thisKey; p.b=elements.elementAt(i);
	    	return p;
	    }
	    }
	return null;
	}

    /// Puts the specified element into the Dictionary, using the specified
    // key.  The element may be retrieved by doing a get() with the same
    // key.  The key and the element cannot be null.
    // @param key the specified key
    // @param value of the specified element
    // @return the old value of the key, or null if it did not have one.
    // @exception NullPointerException If the value of the specified
    // element is null.
    public synchronized Object put( Object key, Object element )
	{
	int i = keys.indexOf( key );
	if ( i != -1 )
	    {
	    Object oldElement = elements.elementAt( i );
	    elements.setElementAt( element, i );
	    return oldElement;
	    }
	else
	    {
	    keys.addElement( (String)key );
	    elements.addElement( element );
	    return null;
	    }
	}

    /// Removes the element corresponding to the key. Does nothing if the
    // key is not present.
    // @param key the key that needs to be removed
    // @return the value of key, or null if the key was not found.
    public synchronized Object remove( Object key )
	{
	int i = keys.indexOf( key );
	if ( i != -1 )
	    {
	    Object oldElement = elements.elementAt( i );
	    keys.removeElementAt( i );
	    elements.removeElementAt( i );
	    return oldElement;
	    }
	else
	    return null;
	}
    
    public static void main(String args[]) {
    	d_DictionaryWildcard d=new d_DictionaryWildcard();
    	d.put("apple", 3);
    	d.put("dog", 4);
    	d.put("cat", 5);
    	d.put("rat", 6);
    	d.put("orange", 7);
    	d.put("aardvark", 8);
    	for (Object o:d) System.out.println(o);
    	System.out.println(d.get("a\\p{Alpha}*"));
    	System.out.println(d.getPair("a\\p{Alpha}*"));
    }
	static public class DIterator implements Iterator<String> {
		private final d_DictionaryWildcard array;
		int index;

		public DIterator (d_DictionaryWildcard array) {
			this.array = array;
		}

		public boolean hasNext () {
			return index < array.size();
		}

		public String next () {
			if (index >= array.size()) throw new NoSuchElementException(""+index);
			return (String)array.keys.get(index++);
		}

		public void remove () {
			index--;
			array.keys.removeElementAt( index );
		    array.elements.removeElementAt( index );
		}

		public void reset () {
			index = 0;
		}
	}
	public Iterator iterator() {
		return new DIterator(this);
	}

}
/*RPC0812 added to library*/
