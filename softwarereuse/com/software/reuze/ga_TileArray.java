package com.software.reuze;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


/*
 * Copyright (c) 2008, Keith Woodward
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Keith Woodward nor the names
 *    of its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
//package straightedge.geom.util;

//import straightedge.geom.*;

/**
 * Used to store and retrieve 2D objects.
 * Contains a table that's used to
 * quickly find the nearest objects.
 *
 * If bloated == true, some objects are outside of the table's rectangular bounds
 * with corners in botLeft and topRight. These T's will be stored in the
 * appropriate edge tile's sharedObstacles.
 *
 *       7 8 9
 *   X   4 5 6
 *       1 2 3
 *              Z
 *           Y
 *
 * For example, if the rectangle has tiles 1,2,3,...9 as above and
 * object X is added (which is outside the rectangle), bloated will be set to true
 * and X will be stored in tile 4's shareObstacles.
 * Similarly, Y and Z will be stored in tile 3's sharedObstacles.
 *
 * @author Keith
 */
public class ga_TileArray<T extends ga_i_PolygonHolder>{
	public int numRows;
	public int numCols;
	public Tile[][] tiles; // rows, columns
	public float tileWidthAndHeight;
	// Euclidean coordinates are assumed with positive X axis to the right and positive y axis up.
	public ga_Vector2 botLeft;
	public ga_Vector2 topRight;
	// if bloated == true then an object has been added that lies outside of the rectangle bounded by botRight and topLeft.
	public boolean bloated;

	da_Tracker tracker = new da_Tracker();

	public ga_TileArray(ga_Vector2 botLeft, float tileWidthAndHeight, int numRows, int numCols){
		init(botLeft, tileWidthAndHeight, numRows, numCols);
	}

	protected void init(ga_Vector2 botLeft, float tileWidthAndHeight, int numRows, int numCols){
		this.numRows = numRows;
		this.numCols = numCols;
		tiles = new Tile[numRows][numCols];
		this.tileWidthAndHeight = tileWidthAndHeight;
		this.botLeft = botLeft.copy();
		topRight = new ga_Vector2(botLeft.x + numRows*tileWidthAndHeight, botLeft.y + numCols*tileWidthAndHeight);
		bloated = false;
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				tiles[i][j] = new Tile(this);
			}
		}
	}

	public ga_TileArray(ga_Vector2 botLeft, ga_Vector2 approxTopRight, float tileWidthAndHeight){
		float minX = botLeft.x;
		float minY = botLeft.y;
		float maxX = approxTopRight.x;
		float maxY = approxTopRight.y;
		this.numRows = (int)Math.ceil((maxX - minX)/tileWidthAndHeight);
		this.numCols = (int)Math.ceil((maxY - minY)/tileWidthAndHeight);
		init(botLeft, tileWidthAndHeight, numRows, numCols);
	}

	public ga_TileArray(ga_AABB aabb, float tileWidthAndHeight){
		this(aabb.p, aabb.p2, tileWidthAndHeight);
	}
	
	public ga_TileArray(Object[] polygonHolders, float tileWidthAndHeight){
		this(ga_AABB.getAABBEnclosingCenterAndRadius(polygonHolders), tileWidthAndHeight);
	}
	
	public ga_TileArray(Collection<ga_i_PolygonHolder> polygonHolders, float tileWidthAndHeight){
		this(ga_AABB.getAABBEnclosingCenterAndRadius(polygonHolders), tileWidthAndHeight);
	}

	public void add(T t){
		ga_Vector2 c = t.getPolygon().getCenter();
		float r = t.getPolygon().getRadius();
		boolean outsideBounds = false;

		float leftColIndex = ((c.x - r) - botLeft.x)/tileWidthAndHeight;
		float rightColIndex = ((c.x + r) - botLeft.x)/tileWidthAndHeight;
		float botRowIndex = ((c.y - r) - botLeft.y)/tileWidthAndHeight;
		float topRowIndex = ((c.y + r) - botLeft.y)/tileWidthAndHeight;
//		System.out.println(this.getClass().getSimpleName()+": c == "+c+botRowIndex+", botRowIndex == "+botRowIndex+", topRowIndex == "+topRowIndex+", leftColIndex == "+leftColIndex+", rightColIndex == "+rightColIndex);

		if (botRowIndex < 0){
			botRowIndex = 0;
			outsideBounds = true;
		}else if (botRowIndex >= getNumRows()){
			botRowIndex = getNumRows()-1;
			outsideBounds = true;
		}
		if (topRowIndex < 0){
			topRowIndex = 0;
			outsideBounds = true;
		}else if (topRowIndex >= getNumRows()){
			topRowIndex = getNumRows() - 1;
			outsideBounds = true;
		}
		if (leftColIndex < 0){
			leftColIndex = 0;
			outsideBounds = true;
		}else if (leftColIndex >= getNumCols()){
			leftColIndex = getNumCols()-1;
			outsideBounds = true;
		}
		if (rightColIndex < 0){
			rightColIndex = 0;
			outsideBounds = true;
		}else if (rightColIndex >= getNumCols()){
			rightColIndex = getNumCols() - 1;
			outsideBounds = true;
		}
		if (outsideBounds){
			bloated = true;
		}
		int leftColIndexInt = (int)leftColIndex;
		int rightColIndexInt = (int)rightColIndex;
		int botRowIndexInt = (int)botRowIndex;
		int topRowIndexInt = (int)topRowIndex;
		//System.out.println(this.getClass().getSimpleName()+": c == "+c+botRowIndex+", botRowIndex == "+botRowIndex+", topRowIndex == "+topRowIndex+", leftColIndex == "+leftColIndex+", rightColIndex == "+rightColIndex);
		if (leftColIndexInt == rightColIndexInt && botRowIndexInt == topRowIndexInt){
			// the obst fits in a single tile so just add it to the tile's contained obstacles.
			Tile tile = tiles[botRowIndexInt][leftColIndexInt];
			if (outsideBounds == false){
				tile.getContainedObstacles().add(t);
			}else{
				tile.getSharedObstacles().add(t);
			}
		}else{
			// the obst spans a few tiles so add it to each tiles' shared obstacles.
			for (int i = botRowIndexInt; i <= topRowIndexInt; i++){
				for (int j = leftColIndexInt; j <= rightColIndexInt; j++){
					Tile tile = tiles[i][j];
					tile.getSharedObstacles().add(t);
//					System.out.println(this.getClass().getSimpleName()+": added c == "+c+", i == "+i+", j == "+j);
				}
			}
		}
	}

	
	public boolean remove(T t){
		ga_Vector2 c = t.getPolygon().getCenter();
		float r = t.getPolygon().getRadius();
		float leftColIndex = ((c.x - r) - botLeft.x)/tileWidthAndHeight;
		float rightColIndex = ((c.x + r) - botLeft.x)/tileWidthAndHeight;
		float botRowIndex = ((c.y - r) - botLeft.y)/tileWidthAndHeight;
		float topRowIndex = ((c.y + r) - botLeft.y)/tileWidthAndHeight;

		if (botRowIndex < 0){
			botRowIndex = 0;
		}else if (botRowIndex >= getNumRows()){
			botRowIndex = getNumRows()-1;
		}
		if (topRowIndex < 0){
			topRowIndex = 0;
		}else if (topRowIndex >= getNumRows()){
			topRowIndex = getNumRows() - 1;
		}
		if (leftColIndex < 0){
			leftColIndex = 0;
		}else if (leftColIndex >= getNumCols()){
			leftColIndex = getNumCols()-1;
		}
		if (rightColIndex < 0){
			rightColIndex = 0;
		}else if (rightColIndex >= getNumCols()){
			rightColIndex = getNumCols() - 1;
		}

		int leftColIndexInt = (int)leftColIndex;
		int rightColIndexInt = (int)rightColIndex;
		int botRowIndexInt = (int)botRowIndex;
		int topRowIndexInt = (int)topRowIndex;

		boolean removed = false;
		if (leftColIndexInt == rightColIndexInt && botRowIndexInt == topRowIndexInt){
			// the obst fits in a single tile so just add it to the tile's contained obstacles.
			Tile tile = tiles[botRowIndexInt][leftColIndexInt];
			boolean justRemoved = tile.getContainedObstacles().remove(t);
			if (justRemoved == false){
				// need to remove it from containedObstacles too in case it is outside the bounds.
				justRemoved = tile.getSharedObstacles().remove(t);
			}
			if (justRemoved == true){
				removed = true;
			}
			//System.out.println("added to getContainedObstacles: "+obst.getInnerPolygon().getCenter());
		}else{
			// the obst spans a few tiles so add it to each tiles' shared obstacles.
			for (int i = botRowIndexInt; i <= topRowIndexInt; i++){
				for (int j = leftColIndexInt; j <= rightColIndexInt; j++){
					Tile tile = tiles[i][j];
					boolean justRemoved = tile.getSharedObstacles().remove(t);
					if (justRemoved == false){
						// need to remove it from containedObstacles too in case it is outside the bounds.
						justRemoved = tile.getContainedObstacles().remove(t);
					}
					if (justRemoved == true){
						removed = true;
					}
				}
			}
		}
		assert getAllWithin(c, r).contains(t) == false : "c == "+c+", r == "+r;
		return removed;
	}

	public ArrayList<T> getAllWithin(ga_Vector2 point, float radius){
		return getAllWithin(point.x, point.y, radius);
	}

	//	CodeTimer ct = new CodeTimer(this.getClass().getSimpleName()+": getAllWithin");
	public ArrayList<T> getAllWithin(float x, float y, float radius){
//		ct.click("create ArrayList");
		ArrayList<T> nearbyObstacles = new ArrayList<T>();
		float r = radius;

//		ct.click("index calcs");

		float leftColIndex = ((x - r) - botLeft.x)/tileWidthAndHeight;
		float rightColIndex = ((x + r) - botLeft.x)/tileWidthAndHeight;
		float botRowIndex = ((y - r) - botLeft.y)/tileWidthAndHeight;
		float topRowIndex = ((y + r) - botLeft.y)/tileWidthAndHeight;
//		System.out.println(this.getClass().getSimpleName()+": c == "+c+botRowIndex+", botRowIndex == "+botRowIndex+", topRowIndex == "+topRowIndex+", leftColIndex == "+leftColIndex+", rightColIndex == "+rightColIndex);

//		ct.click("index checks");
		if (botRowIndex < 0){
			botRowIndex = 0;
		}else if (botRowIndex >= getNumRows()){
			botRowIndex = getNumRows()-1;
		}
		if (topRowIndex < 0){
			topRowIndex = 0;
		}else if (topRowIndex >= getNumRows()){
			topRowIndex = getNumRows() - 1;
		}
		if (leftColIndex < 0){
			leftColIndex = 0;
		}else if (leftColIndex >= getNumCols()){
			leftColIndex = getNumCols()-1;
		}
		if (rightColIndex < 0){
			rightColIndex = 0;
		}else if (rightColIndex >= getNumCols()){
			rightColIndex = getNumCols() - 1;
		}

		int leftColIndexInt = (int)leftColIndex;
		int rightColIndexInt = (int)rightColIndex;
		int botRowIndexInt = (int)botRowIndex;
		int topRowIndexInt = (int)topRowIndex;

		//System.out.println("leftColIndex == "+leftColIndex+" rightColIndex == "+rightColIndex+" botRowIndex == "+botRowIndex+" topRowIndex == "+topRowIndex);
//		ct.click("adds");
		if (leftColIndexInt == rightColIndexInt && botRowIndexInt == topRowIndexInt){
			// the obst fits in a single tile so just add it to the tile's contained obstacles.
			Tile tile = tiles[botRowIndexInt][leftColIndexInt];
			for (int i = 0; i < tile.getSharedObstacles().size(); i++){
				T t = (T)tile.getSharedObstacles().get(i);
				ga_Polygon polygon = t.getPolygon();
				ga_Vector2 polygonCenter = polygon.getCenter();
				if (polygon.isTileArraySearchStatusAdded(tracker) == true){
					continue;
				}
				float radiusSumSq = (r + polygon.getRadius());
				radiusSumSq *= radiusSumSq;
				if (ga_Vector2.dst2(x,y,polygonCenter.x,polygonCenter.y) < radiusSumSq){
					nearbyObstacles.add(t);
					polygon.setTileArraySearchStatus(true, tracker);
				}
			}
			for (int i = 0; i < tile.getContainedObstacles().size(); i++){
				T t = (T)tile.getContainedObstacles().get(i);
				ga_Polygon polygon = t.getPolygon();
				ga_Vector2 polygonCenter = polygon.getCenter();
				float radiusSumSq = (r + polygon.getRadius());
				radiusSumSq *= radiusSumSq;
				if (ga_Vector2.dst2(x,y,polygonCenter.x,polygonCenter.y) < radiusSumSq){
					nearbyObstacles.add(t);
				}
			}
		}else{
			// the obst spans a few tiles so add it to each tiles' shared obstacles.
			for (int i = botRowIndexInt; i <= topRowIndexInt; i++){
				for (int j = leftColIndexInt; j <= rightColIndexInt; j++){
					Tile tile = tiles[i][j];
					d_Bag<T> sharedObstacles = tile.getSharedObstacles();
					for (int k = 0; k < sharedObstacles.size(); k++){
						T t = (T)sharedObstacles.get(k);
						ga_Polygon polygon = t.getPolygon();
						if (polygon.isTileArraySearchStatusAdded(tracker) == true){
							continue;
						}
						float radiusSumSq = (r + polygon.getRadius());
						radiusSumSq *= radiusSumSq;
						ga_Vector2 polygonCenter = polygon.getCenter();
						if (ga_Vector2.dst2(x,y,polygonCenter.x,polygonCenter.y) < radiusSumSq){
							nearbyObstacles.add(t);
							polygon.setTileArraySearchStatus(true, tracker);
						}
					}
					d_Bag<T> containedObstacles = tile.getContainedObstacles();
					for (int k = 0; k < containedObstacles.size(); k++){
						T t = (T)containedObstacles.get(k);
						ga_Polygon polygon = t.getPolygon();
						float radiusSumSq = (r + polygon.getRadius());
						radiusSumSq *= radiusSumSq;
						ga_Vector2 polygonCenter = polygon.getCenter();
						if (ga_Vector2.dst2(x,y,polygonCenter.x,polygonCenter.y) < radiusSumSq){
							nearbyObstacles.add(t);
						}
					}
				}
			}
		}
		tracker.incrementCounter();
//		ct.lastClick();
		return nearbyObstacles;
	}
//	public ArrayList<T> getAllWithin(Vector2 point, float radius){
////		ct.click("create ArrayList");
//		ArrayList<T> nearbyObstacles = new ArrayList<T>();
//		Vector2 c = point;
//		float r = radius;
//
////		ct.click("index calcs");
//
//		float leftColIndex = ((c.x - r) - botLeft.x)/tileWidthAndHeight;
//		float rightColIndex = ((c.x + r) - botLeft.x)/tileWidthAndHeight;
//		float botRowIndex = ((c.y - r) - botLeft.y)/tileWidthAndHeight;
//		float topRowIndex = ((c.y + r) - botLeft.y)/tileWidthAndHeight;
////		System.out.println(this.getClass().getSimpleName()+": c == "+c+botRowIndex+", botRowIndex == "+botRowIndex+", topRowIndex == "+topRowIndex+", leftColIndex == "+leftColIndex+", rightColIndex == "+rightColIndex);
//
////		ct.click("index checks");
//		if (botRowIndex < 0){
//			botRowIndex = 0;
//		}else if (botRowIndex >= getNumRows()){
//			botRowIndex = getNumRows()-1;
//		}
//		if (topRowIndex < 0){
//			topRowIndex = 0;
//		}else if (topRowIndex >= getNumRows()){
//			topRowIndex = getNumRows() - 1;
//		}
//		if (leftColIndex < 0){
//			leftColIndex = 0;
//		}else if (leftColIndex >= getNumCols()){
//			leftColIndex = getNumCols()-1;
//		}
//		if (rightColIndex < 0){
//			rightColIndex = 0;
//		}else if (rightColIndex >= getNumCols()){
//			rightColIndex = getNumCols() - 1;
//		}
//
//		int leftColIndexInt = (int)leftColIndex;
//		int rightColIndexInt = (int)rightColIndex;
//		int botRowIndexInt = (int)botRowIndex;
//		int topRowIndexInt = (int)topRowIndex;
//
//		//System.out.println("leftColIndex == "+leftColIndex+" rightColIndex == "+rightColIndex+" botRowIndex == "+botRowIndex+" topRowIndex == "+topRowIndex);
////		ct.click("adds");
//		if (leftColIndexInt == rightColIndexInt && botRowIndexInt == topRowIndexInt){
//			// the obst fits in a single tile so just add it to the tile's contained obstacles.
//			Tile tile = tiles[botRowIndexInt][leftColIndexInt];
//			for (int i = 0; i < tile.getSharedObstacles().size(); i++){
//				T t = (T)tile.getSharedObstacles().get(i);
//				Polygon polygon = t.getPolygon();
//				if (polygon.isTileArraySearchStatusAdded(tracker) == true){
//					continue;
//				}
//				float radiusSumSq = (r + polygon.getRadius());
//				radiusSumSq *= radiusSumSq;
//				if (c.dst2(polygon.getCenter()) < radiusSumSq){
//					nearbyObstacles.add(t);
//					polygon.setTileArraySearchStatus(true, tracker);
//				}
//			}
//			for (int i = 0; i < tile.getContainedObstacles().size(); i++){
//				T t = (T)tile.getContainedObstacles().get(i);
//				Polygon polygon = t.getPolygon();
//				float radiusSumSq = (r + polygon.getRadius());
//				radiusSumSq *= radiusSumSq;
//				if (c.dst2(polygon.getCenter()) < radiusSumSq){
//					nearbyObstacles.add(t);
//				}
//			}
//		}else{
//			// the obst spans a few tiles so add it to each tiles' shared obstacles.
//			for (int i = botRowIndexInt; i <= topRowIndexInt; i++){
//				for (int j = leftColIndexInt; j <= rightColIndexInt; j++){
//					Tile tile = tiles[i][j];
//					Bag<T> sharedObstacles = tile.getSharedObstacles();
//					for (int k = 0; k < sharedObstacles.size(); k++){
//						T t = (T)sharedObstacles.get(k);
//						Polygon polygon = t.getPolygon();
//						if (polygon.isTileArraySearchStatusAdded(tracker) == true){
//							continue;
//						}
//						float radiusSumSq = (r + polygon.getRadius());
//						radiusSumSq *= radiusSumSq;
//						if (c.dst2(polygon.getCenter()) < radiusSumSq){
//							nearbyObstacles.add(t);
//							polygon.setTileArraySearchStatus(true, tracker);
//						}
//					}
//					Bag<T> containedObstacles = tile.getContainedObstacles();
//					for (int k = 0; k < containedObstacles.size(); k++){
//						T t = (T)containedObstacles.get(k);
//						Polygon polygon = t.getPolygon();
//						float radiusSumSq = (r + polygon.getRadius());
//						radiusSumSq *= radiusSumSq;
//						if (c.dst2(polygon.getCenter()) < radiusSumSq){
//							nearbyObstacles.add(t);
//						}
//					}
//				}
//			}
//		}
//		tracker.incrementCounter();
////		ct.lastClick();
//		return nearbyObstacles;
//	}


////	CodeTimer ct = new CodeTimer(this.getClass().getSimpleName()+": getAllWithin");
//	public CIdentityHashSet<T> nearbySharedPolygonsSet = new CIdentityHashSet<T>();
//	public ArrayList<T> getAllWithin(Vector2 point, float radius){
////		ct.click("create ArrayList");
//		ArrayList<T> nearbyObstacles = new ArrayList<T>();
//		Vector2 c = point;
//		float r = radius;
//
////		ct.click("index calcs");
//
//		float leftColIndex = ((c.x - r) - botLeft.x)/tileWidthAndHeight;
//		float rightColIndex = ((c.x + r) - botLeft.x)/tileWidthAndHeight;
//		float botRowIndex = ((c.y - r) - botLeft.y)/tileWidthAndHeight;
//		float topRowIndex = ((c.y + r) - botLeft.y)/tileWidthAndHeight;
////		System.out.println(this.getClass().getSimpleName()+": c == "+c+botRowIndex+", botRowIndex == "+botRowIndex+", topRowIndex == "+topRowIndex+", leftColIndex == "+leftColIndex+", rightColIndex == "+rightColIndex);
//
////		ct.click("index checks");
//		if (botRowIndex < 0){
//			botRowIndex = 0;
//		}else if (botRowIndex >= getNumRows()){
//			botRowIndex = getNumRows()-1;
//		}
//		if (topRowIndex < 0){
//			topRowIndex = 0;
//		}else if (topRowIndex >= getNumRows()){
//			topRowIndex = getNumRows() - 1;
//		}
//		if (leftColIndex < 0){
//			leftColIndex = 0;
//		}else if (leftColIndex >= getNumCols()){
//			leftColIndex = getNumCols()-1;
//		}
//		if (rightColIndex < 0){
//			rightColIndex = 0;
//		}else if (rightColIndex >= getNumCols()){
//			rightColIndex = getNumCols() - 1;
//		}
//
//		int leftColIndexInt = (int)leftColIndex;
//		int rightColIndexInt = (int)rightColIndex;
//		int botRowIndexInt = (int)botRowIndex;
//		int topRowIndexInt = (int)topRowIndex;
//
//		//System.out.println("leftColIndex == "+leftColIndex+" rightColIndex == "+rightColIndex+" botRowIndex == "+botRowIndex+" topRowIndex == "+topRowIndex);
////		ct.click("adds");
//		if (leftColIndexInt == rightColIndexInt && botRowIndexInt == topRowIndexInt){
//			// the obst fits in a single tile so just add it to the tile's contained obstacles.
//			Tile tile = tiles[botRowIndexInt][leftColIndexInt];
//			for (int i = 0; i < tile.getSharedObstacles().size(); i++){
//				T t = (T)tile.getSharedObstacles().get(i);
//				Polygon polygon = t.getPolygon();
//				float radiusSumSq = (r + polygon.getRadius());
//				radiusSumSq *= radiusSumSq;
//				if (c.dst2(polygon.getCenter()) < radiusSumSq){
//					nearbySharedPolygonsSet.add(t);
//				}
//			}
//			for (int i = 0; i < tile.getContainedObstacles().size(); i++){
//				T t = (T)tile.getContainedObstacles().get(i);
//				Polygon polygon = t.getPolygon();
//				float radiusSumSq = (r + polygon.getRadius());
//				radiusSumSq *= radiusSumSq;
//				if (c.dst2(polygon.getCenter()) < radiusSumSq){
//					nearbyObstacles.add(t);
//				}
//			}
//		}else{
//			// the obst spans a few tiles so add it to each tiles' shared obstacles.
//			for (int i = botRowIndexInt; i <= topRowIndexInt; i++){
//				for (int j = leftColIndexInt; j <= rightColIndexInt; j++){
//					Tile tile = tiles[i][j];
//					Bag<T> sharedObstacles = tile.getSharedObstacles();
//					for (int k = 0; k < sharedObstacles.size(); k++){
//						T t = (T)sharedObstacles.get(k);
//						Polygon polygon = t.getPolygon();
//						float radiusSumSq = (r + polygon.getRadius());
//						radiusSumSq *= radiusSumSq;
//						if (c.dst2(polygon.getCenter()) < radiusSumSq){
//							nearbySharedPolygonsSet.add(t);
//						}
//					}
//					Bag<T> containedObstacles = tile.getContainedObstacles();
//					for (int k = 0; k < containedObstacles.size(); k++){
//						T t = (T)containedObstacles.get(k);
//						Polygon polygon = t.getPolygon();
//						float radiusSumSq = (r + polygon.getRadius());
//						radiusSumSq *= radiusSumSq;
//						if (c.dst2(polygon.getCenter()) < radiusSumSq){
//							nearbyObstacles.add(t);
//						}
//					}
//				}
//			}
//		}
////		ct.click("shared map adds");
//		Object[] array = nearbySharedPolygonsSet.getTable();
//		for (int i = 0; i < array.length; i++){
//			if (array[i] != null){
//				T t = (T)array[i];
//				nearbyObstacles.add(t);
//			}
//		}
////		ct.click("shared map clear");
//		nearbySharedPolygonsSet.clear();
////		ct.lastClick();
//		return nearbyObstacles;
//	}

	public void clear(){
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				tiles[i][j].getContainedObstacles().clear();
				tiles[i][j].getSharedObstacles().clear();
			}
		}
	}

	public Tile getTile(int row, int col){
		return tiles[row][col];
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public ga_Vector2 getBotLeft() {
		return botLeft;
	}

	public boolean isBloated() {
		return bloated;
	}

	public float getTileWidthAndHeight() {
		return tileWidthAndHeight;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public ga_Vector2 getTopRight() {
		return topRight;
	}

	public class Tile<T>{
		ga_TileArray tileArray;
		d_Bag<T> containedObstacles;
		d_Bag<T> sharedObstacles;
		public Tile(ga_TileArray tileArray){
			this.tileArray = tileArray;
			sharedObstacles = new d_Bag<T>();
			containedObstacles = new d_Bag<T>();
		}

		public d_Bag<T> getContainedObstacles() {
			return containedObstacles;
		}

		public d_Bag<T> getSharedObstacles() {
			return sharedObstacles;
		}
	}

	public String toString(){
		return super.toString()+", numRows == "+numRows+", numCols == "+numCols+", tileWidthAndHeight == "+tileWidthAndHeight+", botLeft == "+botLeft+", topRight == "+topRight+", bloated == "+bloated;
	}



	public static void main(String[] args){
		float w = 1000;
		float h = 1000;
		ga_Vector2 botLeft = new ga_Vector2(0, 0);
		ga_Vector2 topRight = new ga_Vector2(w, h);
		ga_TileArray<ga_Polygon> tileArray = new ga_TileArray<ga_Polygon>(botLeft, topRight, 100);
//		System.out.println(TileArray.class.getSimpleName()+": tileArray == "+tileArray);
		int numRects = 10;
		int numPolygons = 0;
		ArrayList<ga_Polygon> allPolygons = new ArrayList<ga_Polygon>();
		Random rand = new Random(0);
		for (int i = 0; i < numRects; i++){
			ArrayList<ga_Vector2> points = new ArrayList<ga_Vector2>();
			float width = 25;
			float height = 25;
			ga_Vector2 point = new ga_Vector2(w*0.05f + rand.nextFloat()*w*0.9f, h*0.05f + rand.nextFloat()*h*0.9f);
			points.add(new ga_Vector2(point.x, point.y));
			points.add(new ga_Vector2(point.x, point.y + height));
			points.add(new ga_Vector2(point.x + width, point.y + height));
			points.add(new ga_Vector2(point.x + width, point.y));
			ga_Polygon poly = new ga_Polygon(points);
			poly.rotate(rand.nextFloat());
			tileArray.add(poly);
			allPolygons.add(poly);
		}

		for (int i = 0; i < numPolygons; i++){
			//ArrayList<Vector2> points = new ArrayList<Vector2>();
			int numPoints = 3 + rand.nextInt(10);
			float radius = rand.nextFloat()*200;
			ga_Polygon poly = ga_Polygon.createRegularPolygon(numPoints, radius);
			ga_Vector2 point = new ga_Vector2(w*0.05f + rand.nextFloat()*w*0.9f, h*0.05f + rand.nextFloat()*h*0.9f);
			poly.translateTo(point);
			poly.rotate(rand.nextFloat());
			tileArray.add(poly);
			allPolygons.add(poly);
		}

		System.out.println(ga_TileArray.class.getSimpleName()+": tileArray == "+tileArray);

		ga_Vector2 c = new ga_Vector2(500, 500);
		float r = rand.nextFloat()*300;
		ArrayList<ga_Polygon> polygons = tileArray.getAllWithin(c, r);

		System.out.println("c == "+c+" r == "+r);
		System.out.println(""+polygons.size()+" polygons within:");
		for (ga_Polygon poly : polygons){
			System.out.println(""+poly.getCenter().dst(c)+",   "+poly.getCenter());
		}
		System.out.println(""+allPolygons.size()+" polygons in total:");
		for (ga_Polygon poly : allPolygons){
			System.out.println(""+poly.getCenter().dst(c)+",   "+poly.getCenter());
		}
	}

}
