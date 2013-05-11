package com.software.reuze;

import java.util.List;

import com.software.reuze.ga_Point;


/**
 * Provides a general interface for maps.
 * 
 * @author Ruediger Lunde
 */
public interface d_i_Map {

	/** Returns a list of all locations. */
	public List<String> getLocations();

	/**
	 * Answers to the question: Where can I get, following one of the
	 * connections starting at the specified location?
	 */
	public List<String> getLocationsLinkedTo(String fromLocation);

	/**
	 * Returns the travel distance between the two specified locations if they
	 * are linked by a connection and null otherwise.
	 */
	public Double getDistance(String fromLocation, String toLocation);

	/**
	 * Returns the position of the specified location. The position is
	 * represented by two coordinates, e.g. latitude and longitude values.
	 */
	public ga_Point getPosition(String loc);

	/**
	 * Returns a location which is selected by random.
	 */
	public String randomlyGenerateDestination();
}
