package com.software.reuze;

import com.software.reuze.d_MessageString;

/**
 * Objects of this class represent a rectangular region of the world. It
 * can also used to store the view-port or display area of the screen. <br>
 * Although the attributes are public this is for speed of reading and they
 * should only be changed using the methods provided in this class.
 * 
 * @author Peter Lager
 *
 */
public class dg_EntityDomainRectangular {

	public double lowX;
	public double highX;
	public double lowY;
	public double highY;
	
	public double width;
	public double height;
	
	/**
	 * Create a Domain object given the top-left and bottom-right coordinates.
	 * @param lowX
	 * @param lowY
	 * @param highX
	 * @param highY
	 */
	public dg_EntityDomainRectangular(double lowX, double lowY, double highX, double highY) {
		this.lowX = lowX;
		this.lowY = lowY;
		this.highX = highX;
		this.highY = highY;
		width = highX - lowX;
		height = highY - lowY;
	}
	
	/**
	 * Create a Domain that is a copy of another one.
	 * @param d domain to be copied
	 */
	public dg_EntityDomainRectangular(dg_EntityDomainRectangular d) {
		lowX = d.lowX;
		lowY = d.lowY;
		highX = d.highX;
		highY = d.highY;
		width = highX - lowX;
		height = highY - lowY;
	}
	
	/**
	 * Set the domain size.
	 * 
	 * @param lowX top-left x coordinate
	 * @param lowY top-left y coordinate
	 * @param width domain width
	 * @param height domain height
	 */
	public void setDomain_xywh(double lowX, double lowY, double width, double height) {
		this.lowX = lowX;
		this.lowY = lowY;
		this.width = width;
		this.height = height;
		highX = lowX + width;
		highY = lowY + height;
	}
	
	/**
	 * Centre the domain about the given world position.
	 * @param wx world x position
	 * @param wy world y position
	 */
	public void move_centre_xy_to(double wx, double wy){
		lowX = wx - width/2;
		lowY = wy - height/2;
		highX = lowX + width;
		highY = lowY + height;				
	}
	
	/**
	 * Centre the domain about the given horizontal position.
	 * @param wx world x position
	 */
	public void move_centre_x_to(double wx){
		lowX = wx - width/2;
		highX = lowX + width;
	}

	/**
	 * Centre the domain about the given vertical position.
	 * @param wy world y position
	 */
	public void move_centre_y_to(double wy){
		lowY = wy - height/2;
		highY = lowY + height;				
	}
	
	/**
	 * Centre the domain about the given position.
	 * @param wx world x centre position
	 * @param wy world y centre position
	 */
	public void move_centre_xy_by(double wx, double wy){
		lowX -= wx;
		lowY -= wy;
		highX = lowX + width;
		highY = lowY + height;				
	}
	
	/**
	 * Move the domain centre horizontally by the world distance given.
	 * @param wx world x centre position
	 */
	public void move_centre_x_by(double wx){
		lowX -= wx;
		highX = lowX + width;
	}

	/**
	 * Move the domain centre vertically by the world distance given.
	 * @param wy world y centre position
	 */
	public void move_centre_y_by(double wy){
		lowY -= wy;
		highY = lowY + height;				
	}
	
	/**
	 * See if this point is within the domain
	 * @param p the point to test
	 * @return true if the point is on or inside the boundary of this domain
	 */
	public boolean contains(ga_Vector2D p){
		return (p.x >= lowX && p.x <= highX && p.y >= lowY && p.y <= highY);
	}

	/**
	 * See if this point is within the domain
	 * @param px x position of point to test
	 * @param py  position of point to test
	 * @return true if the point is on or inside the boundary of this domain
	 */
	public boolean contains(double px, double py){
		return (px >= lowX && px <= highX && py >= lowY && py <= highY);		
	}
	
	/**
	 * Return the Domain as a String
	 */
	public String toString(){
		return d_MessageString.build("Position from {0},{1} to {2},{3}  Size {4} x {5}", lowX,lowY,highX,highY,width,height);
	}
}
