package com.software.reuze;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package straightedge.geom.path;

import java.util.ArrayList;
//import straightedge.geom.KPoint;


/**
 *
 * @author Keith
 */
public class ga_PathData {

	public enum Result {
		NO_RESULT   {
			public String getMessage() { return "No results."; }
			public boolean isError() { return true; }
		},
		SUCCESS   {
			public String getMessage() { return "Success, path found."; }
			public boolean isError() { return false; }
		},
		ERROR1   {
			public String getMessage() { return "Error, startToEndDist is greater than maxSearchDistStartToEnd."; }
			public boolean isError() { return true; }
		},
		ERROR2   {
			public String getMessage() { return "Error, start node can not be connected to obstacle nodes or end node. Increase maxTempNodeConnectionDist or check that start node is not inside an obstacle."; }
			public boolean isError() { return true; }
		},
		ERROR3   {
			public String getMessage() { return "Error, end node can not be connected to obstacle nodes or start node. Increase maxTempNodeConnectionDist or check that end node is not inside an obstacle."; }
			public boolean isError() { return true; }
		},
		ERROR4   {
			public String getMessage() { return "Error, no path found. Could be due to obstacles fencing in the start or end node, or because maxTempNodeConnectionDist or maxSearchDistStartToEnd are not large enough."; }
			public boolean isError() { return true; }
		};

		// Do arithmetic op represented by this constant
		public abstract boolean isError();
		public abstract String getMessage();
		public String toString(){
			return getMessage();
		}
	}

	Result result;
	public ArrayList<ga_Vector2> points;
	public ArrayList<ga_PathNode> nodes;

	public ga_PathData(){
		reset();
	}

	public ga_PathData(Result result){
		initLists();
		if (result.isError() == false){
			throw new IllegalArgumentException("This constructor can only be used for error results. result.isError() == "+result.isError());
		}
		this.result = result;
	}

	public ga_PathData(ArrayList<ga_Vector2> points, ArrayList<ga_PathNode> nodes){
		setSuccess(points, nodes);
	}

	public void reset(){
		this.result = Result.NO_RESULT;
		initLists();
	}
	public void initLists(){
		points = new ArrayList<ga_Vector2>();
		nodes = new ArrayList<ga_PathNode>();
	}

	public void setError(Result result){
		if (result.isError() == false){
			throw new IllegalArgumentException("Result must be an error. result.isError() == "+result.isError());
		}
		this.result = result;
		initLists();
	}
	
	public void setSuccess(ArrayList<ga_Vector2> points, ArrayList<ga_PathNode> nodes){
		result = Result.SUCCESS;
		initLists();
		this.points.addAll(points);
		this.nodes.addAll(nodes);
	}
	public boolean isError(){
		return result.isError();
	}
	public Result getResult(){
		return result;
	}

	public ArrayList<ga_PathNode> getNodes() {
		return nodes;
	}

	public ArrayList<ga_Vector2> getPoints() {
		return points;
	}

}
