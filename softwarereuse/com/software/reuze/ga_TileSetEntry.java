package com.software.reuze;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 ******************************************************************************/

/** Contains a Tiled Map tile set
 * @author David Fraska */
public class ga_TileSetEntry {
	public int firstgid;
	public int tileWidth;
	public int tileHeight;
	public int margin = 0, spacing = 0;
	public String imageName;
	public String name;

	/** Contains a Tiled Map tile set */
	protected ga_TileSetEntry () {
	}

	/** Copy constructor
	 * @param set The set to be copied */
	protected ga_TileSetEntry (ga_TileSetEntry set) {
		this.firstgid = set.firstgid;
		this.tileWidth = set.tileWidth;
		this.tileHeight = set.tileHeight;
		this.margin = set.margin;
		this.spacing = set.spacing;
		this.imageName = set.imageName;
		this.name = set.name;
	}
}
