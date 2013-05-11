package com.software.reuze;

import java.util.ArrayList;

import com.software.reuze.ff_i_XMLInputStates;




public class dg_EntityVehicleXMLInput extends ff_XMLReaderInputStream implements ff_i_XMLInputStates {
	public ArrayList<dg_EntityVehicle> vehicleList;
	dg_EntityVehicle current;
	public void open(String name) {
		System.out.println("open::"+name);
		if (name.equals("vehicles")) vehicleList = new ArrayList<dg_EntityVehicle>();
		if (name.equals("vehicle")) current=new dg_EntityVehicle();
	}

	public void attribute(String name, String value, String element) {
		System.out.println("attr::"+name+" "+value+" "+element);
		if (name.equals("name")) current.name=value;
		if (element.equals("pos") && name.equals("x")) current.pos.x=Double.parseDouble(value);
		if (element.equals("pos") && name.equals("y")) current.pos.y=Double.parseDouble(value);
		if (element.equals("velocity") && name.equals("x")) current.velocity.x=Double.parseDouble(value);
		if (element.equals("velocity") && name.equals("y")) current.velocity.y=Double.parseDouble(value);
		if (element.equals("heading") && name.equals("x")) current.heading.x=Double.parseDouble(value);
		if (element.equals("heading") && name.equals("y")) {
			current.heading.y=Double.parseDouble(value);
			current.side.set(current.heading.getPerp());
		}
		if (element.equals("collision_radius")) current.colRadius=Double.parseDouble(value);
		if (element.equals("max_speed")) current.maxSpeed=Double.parseDouble(value);
		if (element.equals("mass")) current.mass=Double.parseDouble(value);
		if (element.equals("max_turn_rate")) {
			current.maxTurnRate=Double.parseDouble(value);
			current.prevTurnRate=current.maxTurnRate;
			current.currTurnRate=current.maxTurnRate;
		}
		if (element.equals("max_force")) current.maxForce=Double.parseDouble(value);
	}

	public void text(String text) {
		// TODO Auto-generated method stub
		
	}

	public void close(String element) {
		System.out.println("close::"+element);
		if (element.equals("vehicle") || element.equals("mover")) {vehicleList.add(current); current=null;}
	}
}
