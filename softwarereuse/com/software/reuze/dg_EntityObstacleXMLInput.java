package com.software.reuze;

import java.util.ArrayList;

import com.software.reuze.ff_i_XMLInputStates;




public class dg_EntityObstacleXMLInput extends ff_XMLReaderInputStream implements ff_i_XMLInputStates {
	public ArrayList<dg_EntityObstacle> obstacleList;
	dg_EntityObstacle current;
	public void open(String name) {
		System.out.println("open::"+name);
		if (name.equals("obstacles")) obstacleList = new ArrayList<dg_EntityObstacle>();
		if (name.equals("obstacle")) current=new dg_EntityObstacle();
	}

	public void attribute(String name, String value, String element) {
		System.out.println("attr::"+name+" "+value+" "+element);
		if (name.equals("name")) current.name=value;
		if (element.equals("pos") && name.equals("x")) current.pos.x=Double.parseDouble(value);
		if (element.equals("pos") && name.equals("y")) current.pos.y=Double.parseDouble(value);
		if (element.equals("radius")) current.colRadius=Double.parseDouble(value);
	}

	public void text(String text) {
		// TODO Auto-generated method stub
		
	}

	public void close(String element) {
		System.out.println("close::"+element);
		if (element.equals("obstacle")) {obstacleList.add(current); current=null;}
	}

}
