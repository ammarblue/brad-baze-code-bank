package com.software.reuze;

public interface ff_i_XMLInputStates {
	public void open (String name);
	public void attribute (String name, String value, String element);
	public void text (String text);
	public void close (String element);
}
