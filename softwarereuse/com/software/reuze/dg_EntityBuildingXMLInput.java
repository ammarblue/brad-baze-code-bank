package com.software.reuze;

import java.util.ArrayList;

import com.software.reuze.ff_i_XMLInputStates;




public class dg_EntityBuildingXMLInput extends ff_XMLReaderInputStream implements ff_i_XMLInputStates {
	public ArrayList<dg_EntityBuilding> buildingList;
	dg_EntityBuilding current;
	ga_Vector2D currentV;
	ArrayList<ga_Vector2D> currentVs=new ArrayList<ga_Vector2D>();
	public void open(String name) {
		System.out.println("open::"+name);
		if (name.equals("buildings")) buildingList = new ArrayList<dg_EntityBuilding>();
		if (name.equals("building")) {current=new dg_EntityBuilding(); currentV=new ga_Vector2D(); currentVs.clear();}
	}

	public void attribute(String name, String value, String element) {
		System.out.println("attr::"+name+" "+value+" "+element);
		if (name.equals("name")) current.name=value;
		if (element.equals("pos") && name.equals("x")) current.pos.x=Double.parseDouble(value);
		if (element.equals("pos") && name.equals("y")) current.pos.y=Double.parseDouble(value);
		if (element.equals("point") && name.equals("x")) currentV.x=Double.parseDouble(value);
		if (element.equals("point") && name.equals("y")) currentV.y=Double.parseDouble(value);
	}

	public void text(String text) {
		// TODO Auto-generated method stub
		
	}

	public void close(String element) {
		System.out.println("close::"+element);
		if (element.equals("building")) {
			buildingList.add(current);
			ga_Vector2D[] v=new ga_Vector2D[currentVs.size()];
			currentVs.toArray(v);
			current.setContour(v); current=null;}
		if (element.equals("point")) {currentVs.add(currentV); currentV=new ga_Vector2D();}
	}

}
