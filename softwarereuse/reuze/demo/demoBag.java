package reuze.demo;

import com.software.reuze.d_Bag;

import processing.core.PApplet;

//coded by Ian Spurlock
//bag demo, for use as a collection of objects in a game such as a player's inventory, a store, a deck of cards, etc
//I know it's a bit basic, but it's extremely useful for many games (plus it gave me an opportunity to play with the graphics controls)

//CONTROLS
//the brown box represents the bag, items in the box are in the bag
//click on an item to move it in or out of the bag
//mousePressed() isn't working perfectly, you may have to click the item twice to activate it the first time

class item{
	int r,g,b;
	public item(int red,int green,int blue){
		r = red;
		g = green;
		b = blue;
	}
}

public class demoBag extends PApplet{
	
	//create the cards and bag(deck)
	d_Bag inven = new d_Bag(6);
	item a = new item(200,0,0);
	item b = new item(0,200,0);
	item c = new item(0,0,200);
	item d = new item(200,200,0);
	item e = new item(200,0,200);
	item f = new item(0,200,200);
	
	public void setup(){
		//put the items in the bag
		inven.add(a);
		inven.add(b);
		inven.add(c);
		inven.add(d);
		inven.add(e);
		inven.add(f);
		
		//set up the display window
		size(640,480,P2D);
		background(200,200,200);
	}
	
	public void mousePressed(){
		
		if(mouseX > 50 && mouseX < 100){
			if(mouseY > 50 && mouseY < 100 && inven.contains(a) == true){
				inven.remove(a);
			} else if(mouseY > 100 && mouseY < 150 && inven.contains(c) == true){
				inven.remove(c);
			} else if(mouseY > 150 && mouseY < 200 && inven.contains(e) == true){
				inven.remove(e);
			}
		}
		if(mouseX > 100 && mouseX < 150){
			if(mouseY > 50 && mouseY < 100 && inven.contains(b) == true){
				inven.remove(b);
			} else if(mouseY > 100 && mouseY < 150 && inven.contains(d) == true){
				inven.remove(d);
			} else if(mouseY > 150 && mouseY < 200 && inven.contains(f) == true){
				inven.remove(f);
			}
		}
		
		if(mouseX > 350 && mouseX < 400){
			if(mouseY > 50 && mouseY < 100 && inven.contains(a) == false){
				inven.add(a);
			} else if(mouseY > 100 && mouseY < 150 && inven.contains(c) == false){
				inven.add(c);
			} else if(mouseY > 150 && mouseY < 200 && inven.contains(e) == false){
				inven.add(e);
			}
		}
		if(mouseX > 400 && mouseX < 450){
			if(mouseY > 50 && mouseY < 100 && inven.contains(b) == false){
				inven.add(b);
			} else if(mouseY > 100 && mouseY < 150 && inven.contains(d) == false){
				inven.add(d);
			} else if(mouseY > 150 && mouseY < 200 && inven.contains(f) == false){
				inven.add(f);
			}
		}
	}
	
	public void draw(){
		background(200,200,200);
		fill(100,100,50);
		rect(45,45,110,160);
		
		if(inven.contains(a)==true){
			fill(a.r,a.g,a.b);
			ellipse(75,75,50,50);
		} else {
			fill(a.r,a.g,a.b);
			ellipse(375,75,50,50);
		}
		
		if(inven.contains(b)==true){
			fill(b.r,b.g,b.b);
			ellipse(125,75,50,50);
		} else {
			fill(b.r,b.g,b.b);
			ellipse(425,75,50,50);
		}
		
		if(inven.contains(c)==true){
			fill(c.r,c.g,c.b);
			ellipse(75,125,50,50);
		} else {
			fill(c.r,c.g,c.b);
			ellipse(375,125,50,50);
		}
		
		if(inven.contains(d)==true){
			fill(d.r,d.g,d.b);
			ellipse(125,125,50,50);
		} else {
			fill(d.r,d.g,d.b);
			ellipse(425,125,50,50);
		}
		
		if(inven.contains(e)==true){
			fill(e.r,e.g,e.b);
			ellipse(75,175,50,50);
		} else {
			fill(e.r,e.g,e.b);
			ellipse(375,175,50,50);
		}
		
		if(inven.contains(f)==true){
			fill(f.r,f.g,f.b);
			ellipse(125,175,50,50);
		} else {
			fill(f.r,f.g,f.b);
			ellipse(425,175,50,50);
		}
	}
	
}