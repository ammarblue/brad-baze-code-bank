package com.software.reuze;
import java.util.ArrayList;


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
//package straightedge.geom;

/**
 *
 * @author Keith
 */
public class ga_PolygonMulti implements ga_i_PolygonHolder {
	public ArrayList<ga_Polygon> polygons;

	public ga_PolygonMulti(){
	}
	public ga_PolygonMulti(ga_Polygon polygon){
		this.polygons = new ArrayList<ga_Polygon>();
		polygons.add(polygon);
	}
	public ga_PolygonMulti(ArrayList<ga_Polygon> polygons){
		this.polygons = polygons;
	}

	public ga_PolygonMulti(ga_Polygon... polygonArray){
		polygons = new ArrayList<ga_Polygon>();
		for (int i = 0; i < polygonArray.length; i++){
			polygons.add(polygonArray[i]);
		}
	}

	public ga_PolygonMulti copy(){
		ArrayList<ga_Polygon> newPolygons = new ArrayList<ga_Polygon>();
		for (int i = 0; i < polygons.size(); i++){
			newPolygons.add(polygons.get(i).copy());
		}
		return new ga_PolygonMulti(newPolygons);
	}

	public void translate(float x, float y) {
		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon polygon = polygons.get(i);
			polygon.translate(x, y);
		}
	}
	public void translate(ga_Vector2 translation){
		translate(translation.x, translation.y);
	}

	public void translateTo(float x, float y){
		ga_Vector2 center = this.getExteriorPolygon().getCenter();
		float xIncrement = x - center.x;
		float yIncrement = y - center.y;
		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon polygon = polygons.get(i);
			polygon.translate(xIncrement, yIncrement);
		}
	}
	public void translateTo(ga_Vector2 newCentre){
		translateTo(newCentre.x, newCentre.y);
	}
	public void translateToOrigin(){
		translateTo(0, 0);
	}

	public void rotate(float angle) {
		ga_Vector2 center = this.getExteriorPolygon().getCenter();
		rotate(angle, center.x, center.y);
	}
	public void rotate(float angle, ga_Vector2 axle) {
		rotate(angle, axle.x, axle.y);
	}
	public void rotate(float angle, float x, float y) {
		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon polygon = polygons.get(i);
			polygon.rotate(angle, x, y);
		}
	}

	public void scale(float xMultiplier, float yMultiplier, float x, float y){
		for (int i = 0; i < polygons.size(); i++){
			ga_Polygon polygon = polygons.get(i);
			polygon.scale(xMultiplier, yMultiplier, x, y);
		}
	}
	public void scale(float multiplierX, float multiplierY){
		ga_Vector2 center = getExteriorPolygon().getCenter();
		scale(multiplierX, multiplierY, center.x, center.y);
	}
	public void scale(float multiplierX, float multiplierY, ga_Vector2 p){
		scale(multiplierX, multiplierY, p.x, p.y);
	}
	public void scale(float multiplier){
		ga_Vector2 center = getExteriorPolygon().getCenter();
		scale(multiplier, multiplier, center.x, center.y);
	}
	public void scale(float multiplier, ga_Vector2 p){
		scale(multiplier, multiplier, p.x, p.y);
	}

	public ga_Polygon getExteriorPolygon() {
		if (polygons.size() > 0){
			return polygons.get(0);
		}
		return null;
	}

	public ga_Polygon getPolygon(int index) {
		return polygons.get(index);
		
	}

	public ArrayList<ga_Polygon> getPolygons(){
		return polygons;
	}

	public ArrayList<ga_Polygon> getInteriorPolygonsCopy(){
		ArrayList<ga_Polygon> internalPolygons = new ArrayList<ga_Polygon>();
		for (int i = 1; i < polygons.size(); i++){
			internalPolygons.add(polygons.get(i));
		}
		return internalPolygons;
	}

	/**
	 * Needed by PolygonHolder.
	 * @return This Polygon.
	 */
	public ga_Polygon getPolygon(){
		return getExteriorPolygon();
	}

	public ga_AABB getAABB(){
		return getExteriorPolygon().getAABB();
	}

	public boolean intersects(float x, float y, float w, float h){
		return getExteriorPolygon().intersects(x, y, w, h);
	}
	public boolean intersects(double x, double y, double w, double h){
		return getExteriorPolygon().intersects((float)x, (float)y, (float)w, (float)h);
	}

	public boolean contains(ga_Vector2 p){
		return contains(p.x, p.y);
	}
	public boolean contains(double x, double y){ return contains((float)x, (float)y);}
	public boolean contains(float x, float y){
		if (getExteriorPolygon().contains(x, y) == true){
			int crossings = 0;
			for (int h = 1; h < polygons.size(); h++){
				ArrayList<ga_Vector2> points = polygons.get(h).getPoints();
				ga_Vector2 pointIBefore = (points.size() != 0 ? points.get(points.size() - 1) : null);
				for (int i = 0; i < points.size(); i++) {
					ga_Vector2 pointI = points.get(i);
					if (((pointIBefore.y <= y && y < pointI.y)
							|| (pointI.y <= y && y < pointIBefore.y))
							&& x < ((pointI.x - pointIBefore.x)/(pointI.y - pointIBefore.y)*(y - pointIBefore.y) + pointIBefore.x)) {
						crossings++;
					}
					pointIBefore = pointI;
				}
			}
			return (crossings % 2 != 1);
		}else{
			return false;
		}
	}
	public boolean contains(float x, float y, float w, float h){
		return getExteriorPolygon().contains(x, y, w, h);
	}
	public boolean contains(double x, double y, double w, double h){
		return getExteriorPolygon().contains((float)x, (float)y, (float)w, (float)h);
	}

//	Path2D.Double path = new Path2D.Double();
//				path.setWindingRule(Path2D.WIND_EVEN_ODD);
//				{
//					float x = 0;
//					float y = 0;
//					float d = 400;
//					path.moveTo(x, y);
//					path.lineTo(x+d, y+0);
//					path.lineTo(x+d, y+d);
//					path.lineTo(x+0, y+d);
//					path.closePath();
//				}
//				{
//					float x = 50;
//					float y = 50;
//					float d = 300;
//					path.moveTo(x, y);
//					path.lineTo(x+d, y+0);
//					path.lineTo(x+d, y+d);
//					path.lineTo(x+0, y+d);
//					path.closePath();
//				}
//				{
//					float x = 100;
//					float y = 100;
//					float d = 200;
//					path.moveTo(x, y);
//					path.lineTo(x+d, y+0);
//					path.lineTo(x+d, y+d);
//					path.lineTo(x+0, y+d);
//					path.closePath();
//				}
//				//PolygonConverter pc = new PolygonConverter();
//				if (path.contains(lastMousePointInWorldCoords.x, lastMousePointInWorldCoords.y)){
//					g.setColor(Color.MAGENTA);
//				}else{
//					g.setColor(Color.BLACK);
//				}
//
//				g.draw(path);




//	public PolygonMulti parentMultiPolygon;
//	public Polygon polygon;
//	// level 0 has the top polygon.
//	// level 1 has holes within the first polygon.
//	// level 2 has polygons within the level 1 holes.
//	// level 3 has holes within the level 2 polygons.
//	// etc
//	public int level;
//	public ArrayList<PolygonMulti> innerPolygons;
//
//
//	public PolygonMulti(){
//	}
//	public PolygonMulti(Polygon polygon){
//		this(null, polygon, 0, new ArrayList<PolygonMulti>());
//	}
//	public PolygonMulti(PolygonMulti parentMultiPolygon, Polygon polygon, int level){
//		this(parentMultiPolygon, polygon, level, new ArrayList<PolygonMulti>());
//	}
//	public PolygonMulti(PolygonMulti parentMultiPolygon, Polygon polygon, int level, ArrayList<PolygonMulti> innerPolygons){
//		this.parentMultiPolygon = parentMultiPolygon;
//		this.level = level;
//		this.polygon = polygon;
//		this.innerPolygons = innerPolygons;
//	}
//
//
//	public boolean isHole(){
//		// if level is odd, it is a hole.
//		return (level % 2 == 1);
//	}
//
//	public ArrayList<PolygonMulti> getInnerPolygons() {
//		return innerPolygons;
//	}
//
//	public void setInnerPolygons(ArrayList<PolygonMulti> innerPolygons) {
//		this.innerPolygons = innerPolygons;
//	}
//
//	public int getLevel() {
//		return level;
//	}
//
//	public void setLevel(int level) {
//		this.level = level;
//	}
//
//	public PolygonMulti getParentMultiPolygon() {
//		return parentMultiPolygon;
//	}
//
//	public void setParentMultiPolygon(PolygonMulti parentMultiPolygon) {
//		this.parentMultiPolygon = parentMultiPolygon;
//	}
//
//	public Polygon getPolygon() {
//		return polygon;
//	}
//
//	public void setPolygon(Polygon polygon) {
//		this.polygon = polygon;
//	}
	
}
