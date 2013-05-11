package com.software.reuze;

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


//import straightedge.geom.KPoint;
//import straightedge.geom.KPolygon;

/**
 *
 * @author Keith
 */
public class ga_Cell {
	ga_CellArray cellArray;
	int row;
	int col;
	boolean discovered = false;
	ga_CellLinks botLeft;
	ga_CellLinks botRight;
	ga_CellLinks topRight;
	ga_CellLinks topLeft;
	ga_Vector2 center;

	public ga_Cell(ga_CellArray cellArray, int row, int col){
		this.cellArray = cellArray;
		this.row = row;
		this.col = col;
	}

	public ga_Vector2 getCenter() {
		return center;
	}

	public void setCenter(ga_Vector2 center) {
		this.center = center;
	}
	
	public ga_CellLinks getBotLeft() {
		return botLeft;
	}

	public ga_CellLinks getBotRight() {
		return botRight;
	}

	public ga_CellLinks getTopLeft() {
		return topLeft;
	}

	public ga_CellLinks getTopRight() {
		return topRight;
	}

	public void setBotLeft(ga_CellLinks botLeft) {
		this.botLeft = botLeft;
	}

	public void setBotRight(ga_CellLinks botRight) {
		this.botRight = botRight;
	}

	public void setTopLeft(ga_CellLinks topLeft) {
		this.topLeft = topLeft;
	}

	public void setTopRight(ga_CellLinks topRight) {
		this.topRight = topRight;
	}

	public boolean isDiscovered() {
		return discovered;
	}

	public ga_CellArray getCellArray() {
		return cellArray;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}

	public ga_Cell getCellUp(){
		int r = row+1;
		if (r < cellArray.getNumRows()){
			return cellArray.getCells()[r][col];
		}else{
			return null;
		}
	}
	public ga_Cell getCellDown(){
		int r = row-1;
		if (r >= 0){
			return cellArray.getCells()[r][col];
		}else{
			return null;
		}
	}
	public ga_Cell getCellLeft(){
		int c = col-1;
		if (c >= 0){
			return cellArray.getCells()[row][c];
		}else{
			return null;
		}
	}
	public ga_Cell getCellRight(){
		int c = col+1;
		if (c < cellArray.getNumCols()){
			return cellArray.getCells()[row][c];
		}else{
			return null;
		}
	}

	public ga_Cell getCellUpRight(){
		int r = row+1;
		int c = col+1;
		if (r < cellArray.getNumRows() && c < cellArray.getNumCols()){
			return cellArray.getCells()[r][c];
		}else{
			return null;
		}
	}
	public ga_Cell getCellUpLeft(){
		int r = row+1;
		int c = col-1;
		if (r < cellArray.getNumRows() && c >= 0){
			return cellArray.getCells()[r][c];
		}else{
			return null;
		}
	}
	public ga_Cell getCellDownRight(){
		int r = row-1;
		int c = col+1;
		if (r >= 0 && c < cellArray.getNumCols()){
			return cellArray.getCells()[r][c];
		}else{
			return null;
		}
	}
	public ga_Cell getCellDownLeft(){
		int r = row-1;
		int c = col-1;
		if (r >= 0 && c >= 0){
			return cellArray.getCells()[r][c];
		}else{
			return null;
		}
	}

	//	public boolean isBorder(){
//		for (int i = 0; i < neighbours.length; i++){
//			Cell neighbour = neighbours[i];
//			if (neighbour == null || neighbour.isDiscovered()){
//				return true;
//			}
//		}
//		Cell neighbour = this.getCellUpRight();
//		if (neighbour == null || neighbour.isDiscovered()){
//			return true;
//		}
//		neighbour = this.getCellUpLeft();
//		if (neighbour == null || neighbour.isDiscovered()){
//			return true;
//		}
//		neighbour = this.getCellDownLeft();
//		if (neighbour == null || neighbour.isDiscovered()){
//			return true;
//		}
//		neighbour = this.getCellDownRight();
//		if (neighbour == null || neighbour.isDiscovered()){
//			return true;
//		}
//
//		return false;
//	}


}
