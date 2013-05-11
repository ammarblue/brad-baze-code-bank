package com.software.reuze;


public interface dg_i_Property {
	//by convention, the name "saved" is reserved to get/restore saved state of a paused game object
	//by convention, local names come first PLANET_OBJ to speed local search in context where OBJ is only option
	public void set(String name, String value);
	public String gets(String name);
	public void set(String name, int value);
	public int geti(String name);
	public void set(String name, float value);
	public float getf(String name);
	public void set(String name, Object value);
	public Object geto(String name);
	public void set(String name, gb_Vector3 value);
	public gb_Vector3 getv3(String name);
	public boolean isName(String name);
	//used to give verbal commands to objects, string format verb[comma-separated list of modifiers]
	public float action(String command);
}
