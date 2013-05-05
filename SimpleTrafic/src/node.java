import java.util.ArrayList;

import processing.core.PVector;


// nodes
public class node {
	/**
	 * 
	 */
	private simpleCity node;
	PVector pos = new PVector();
	ArrayList children = new ArrayList();

	node(simpleCity simpleCity, float x, float y) {
		node = simpleCity;
		pos = new PVector(x, y);
	}
}