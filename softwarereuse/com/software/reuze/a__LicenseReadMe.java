package com.software.reuze;

public final class a__LicenseReadMe {
	public static final String license=
"Software copied/created/collected/modified by bobcook47@hotmail.com to provide one-stop shopping for modules with canonical naming."+
"http://hg.postspectacular.com/toxiclibs/source/ Copyright (c) 2006-2011 Karsten Schmidt <info at postspectacular.com>"+
"Part of the Processing project - http://processing.org Copyright (c) 2011 Elie Zananiri Copyright (c) 2008-11 Andreas Schlegel"+
"http://code.google.com/p/ai4games2d/ p.k.lager"+
"http://code.google.com/p/aima-java/ Norvig and Russell's Artificial Intelligence - A Modern Approach"+
"http://algs4.cs.princeton.edu/home/ Copyright © 2002Ð2012 Robert Sedgewick and Kevin Wayne. All rights reserved."+
"http://www.jhlabs.com/ Copyright 2006 Jerry Huxtable"+
"http://code.google.com/p/straightedge/ by by Keith Woodward"+
"http://code.google.com/p/libgdx/ Licensed under the Apache License, Version 2.0"+
"This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License"+
"as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version."
;
/*	http://creativecommons.org/licenses/LGPL/2.1/

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General Public
	License along with this library; if not, write to the Free Software
	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
*/
/* You may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
/*
Classes are organized by naming not packages.
First, there is no agreed-upon taxonomy for package names.
Second, users that pick and choose modules from package hierarchies may encounter naming conflicts.
Any of these classes can be reused by others with a low-probability of name conflicts.
Classes are named from generic to specific, left-to-right, e.g. SearchBreadthFirst, sub-class names add on to parent's name
Imports must be fully qualified which makes it easy to write a program that will pull dependent classes from the library.
Be warned that this is a work in progress, so names may change.
a-z..._a-z..._ClassName
       i                interface
       im               interface, marker
       ie				interface, environment
       					same as e attribute but more general-purpose
       					multiple implementations can be used simultaneously
       					and can be switched at runtime
       f                factory
       						must have a create(String arg) method, arg is a property list
       						type=ChoiceName must be implemented, returns an interface
       						tostring should return comma-separated list of choiceName[propName,...]
       g				parameterized class, generic
       a				abstract class
       						base class that factors part of the implementation for its children
       e				class designed to hide a technology choice
       					apps that use these modules must bind to different implementations
a			ai
aa				agent
ac				constraint satisfaction
al				learning
aln					neural net
ap				planning
apm					on maps
as				simulation
b			money
c			codes
cc				compression
ce				encryption/decryption
d			data structures
da				algorithms
daf					finite-state automata
dafd					deterministic
dafn					non-deterministic
dag				algorithms, graph
das					algorithms, search
dasg					algorithms, search, game
db				database
dg				data structures, for games
f			files
ff				format standard
g			geometry
ga				2D
gb				3D
gc				>=4D
i			image
ib				buffered
ibe					effects, static
ie				effects, static
ied				effects, dynamic
l			language-related
lc				concurrency
lev				events
le				exceptions
lp              patterns
m			math
mf				first-order logic
ml				propositional logic
mp				probability
mpb					Bayesian
mpt					temporal
ms				statistics
mv				vector
mva					2D
mvb					3D
mvc					4D
n			networking
p			physics
pf				fluids
pm				motion
pma				motion, 2D
pt			time
s			sound
v			vision
va				computer animation
vam				motion capture
vc				color
vg				computer graphics
vgu					user interface elements
w			world-wide web

Classes
	constructors
		if constants are appropriate, such as for Complex, add a String constructor, new Complex("3+4i")
		include a copy method, but not clone
		copy should set member variables, gives caller option of new on each arg
	member variables, only use public/private
	methods
    getters/setters, have the advantage of hiding type of member variable
    				 can be overloaded to avoid conversions by users
    toString, use StringBuilder, main purpose is print/println, format as property list, unless a single value
    			     x=58 y=59 list values should be comma-separated, quoted if value contains blanks or commas
    parse should be overloaded with a String and a Scanner argument and should be inverse of toString
    since code can reside in or out of source control systems, use the following convention
    label changes with /*XXXMMYY* /  XXX are your initials, MM is month, YY is year
    add comment at the end of class /*XXXMMYY ... with a longer description of change
 */
}
