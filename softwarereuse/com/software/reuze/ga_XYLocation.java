package com.software.reuze;

/**
 * Note: If looking at a rectangle - the coordinate (x=0, y=0) will be the top
 * left hand corner. This corresponds with Java's AWT coordinate system.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class ga_XYLocation {
	public enum Direction {
		North, South, East, West
	};

	int xCoOrdinate, yCoOrdinate;

	/**
	 * Constructs and initializes a location at the specified (<em>x</em>,
	 * <em>y</em>) location in the coordinate space.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public ga_XYLocation(int x, int y) {
		xCoOrdinate = x;
		yCoOrdinate = y;
	}

	/**
	 * Returns the X coordinate of the location in integer precision.
	 * 
	 * @return the X coordinate of the location in double precision.
	 */
	public int getXCoOrdinate() {
		return xCoOrdinate;
	}

	public int getYCoOrdinate() {
		return yCoOrdinate;
	}

	/**
	 * Returns the location one unit left of this location.
	 * 
	 * @return the location one unit left of this location.
	 */
	public ga_XYLocation west() {
		return new ga_XYLocation(xCoOrdinate - 1, yCoOrdinate);
	}

	/**
	 * Returns the location one unit right of this location.
	 * 
	 * @return the location one unit right of this location.
	 */
	public ga_XYLocation east() {
		return new ga_XYLocation(xCoOrdinate + 1, yCoOrdinate);
	}

	/**
	 * Returns the location one unit ahead of this location.
	 * 
	 * @return the location one unit ahead of this location.
	 */
	public ga_XYLocation north() {
		return new ga_XYLocation(xCoOrdinate, yCoOrdinate - 1);
	}

	/**
	 * Returns the location one unit behind, this location.
	 * 
	 * @return the location one unit behind this location.
	 */
	public ga_XYLocation south() {
		return new ga_XYLocation(xCoOrdinate, yCoOrdinate + 1);
	}

	/**
	 * Returns the location one unit left of this location.
	 * 
	 * @return the location one unit left of this location.
	 */
	public ga_XYLocation left() {
		return west();
	}

	/**
	 * Returns the location one unit right of this location.
	 * 
	 * @return the location one unit right of this location.
	 */
	public ga_XYLocation right() {
		return east();
	}

	/**
	 * Returns the location one unit above this location.
	 * 
	 * @return the location one unit above this location.
	 */
	public ga_XYLocation up() {
		return north();
	}

	/**
	 * Returns the location one unit below this location.
	 * 
	 * @return the location one unit below this location.
	 */
	public ga_XYLocation down() {
		return south();
	}

	/**
	 * Returns the location one unit from this location in the specified
	 * direction.
	 * 
	 * @return the location one unit from this location in the specified
	 *         direction.
	 */
	public ga_XYLocation locationAt(Direction direction) {
		if (direction.equals(Direction.North)) {
			return north();
		}
		if (direction.equals(Direction.South)) {
			return south();
		}
		if (direction.equals(Direction.East)) {
			return east();
		}
		if (direction.equals(Direction.West)) {
			return west();
		} else {
			throw new RuntimeException("Unknown direction " + direction);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (null == o || !(o instanceof ga_XYLocation)) {
			return super.equals(o);
		}
		ga_XYLocation anotherLoc = (ga_XYLocation) o;
		return ((anotherLoc.getXCoOrdinate() == xCoOrdinate) && (anotherLoc
				.getYCoOrdinate() == yCoOrdinate));
	}

	@Override
	public String toString() {
		return " ( " + xCoOrdinate + " , " + yCoOrdinate + " ) ";
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + xCoOrdinate;
		result = result + yCoOrdinate;
		return result;
	}
}