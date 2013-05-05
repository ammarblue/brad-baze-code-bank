import java.util.ArrayList;

import processing.core.PVector;

// nodes
public class node {
	/**
	 * 
	 */
	private final car_city_sim node;
	PVector pos = new PVector();
	ArrayList children = new ArrayList();

	node(car_city_sim car_city_sim, float x, float y) {
		node = car_city_sim;
		pos = new PVector(x, y);
	}
}