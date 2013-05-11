package reuze.pending;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import reuze.awt.dg_CardVisibleAWT;

import com.software.reuze.dg_Card;
import com.software.reuze.dg_CardPile;
import com.software.reuze.dg_CardSolitairePlay;



public class demoCardSolitaire extends JFrame implements MouseListener {
dg_CardSolitairePlay<dg_CardVisibleAWT> sol;
private int size = 3;
BufferedImage im;
@Override public void paint(Graphics g) {
	int w=this.getWidth();
	int h=this.getHeight();
	  g.clearRect(0, 0, w, h);
	  int i=0;
	  for (dg_CardPile<dg_CardVisibleAWT> p : sol) {
		  g.setColor(Color.black);
		  if (i<4) g.drawString("CDHS".substring(i,i+1), 10, i*50+40);
		  if (p.size()==0) g.drawRect(20, i*50+30, 30, 30);
		  else {
			  int j=0;
			  for (dg_CardVisibleAWT c : p) {
				  if (c.isProperty(dg_Card.FACEUP)) c.draw(g, 20+j*30, i*50+30, 1, true);
				  else c.draw(g, 20+j*30, i*50+30, 1, im, null);
				  j++;
			  }
		  }
		  i++;
	  }
	  if (pile>=0) g.drawRect(15, pile*50+25, (card+1)*30, 45);
	  g.drawString("DRAW 3 CARDS", 20, i*50+50);
	  g.drawString("MOVE TO ACES", 20+4*30, i*50+50);
	  g.drawString("NEW GAME", 20+8*30, i*50+50);
}
private void registerListeners() {
  addWindowListener( new WindowAdapter() {
    public void windowClosing(WindowEvent e) {
      close();
    }
  });
}

public void close() {
  System.exit(0);
}
public void set(String name, final int start[], final int showing[], int flags[], int seed) {
	this.setTitle(name+" + "+seed);
	sol.set(name, start, showing, flags, seed, new dg_CardVisibleAWT(0));
}
public demoCardSolitaire(String name, final int start[], final int showing[], int flags[], int seed) {
	super(name+" + "+seed);
    registerListeners();
    addMouseListener(this);
    pack();
    try {
		im=ImageIO.read(new File("./data/pencils.jpg"));
	} catch (IOException e) {
		e.printStackTrace();
		System.exit(0);
	}
	sol=new dg_CardSolitairePlay("S O L",start,faceup,flags,(int) (Math.random()*100000), new dg_CardVisibleAWT(0));
}

final static int start22[]= {0,0,0,0, 1,6,7,8,9,10,11};
final static int faceup22[]={0,0,0,0, 1,5,5,5,5, 5, 5};
final static int flags22[]= {1,1,1,1, 2,2,2,2,2, 2, 2};
final static int start[]= {0,0,0,0,24,0, 1, 2, 3, 4, 5, 6, 7};
final static int faceup[]={0,0,0,0, 0,0, 1, 1, 1, 1, 1, 1, 1};
final static int flags[]= {1,1,1,1, 4,8,18,18,18,18,18,18,18};
public static void main(String[] args) {
	
	demoCardSolitaire m=new demoCardSolitaire("S O L",start,faceup,flags,(int) (Math.random()*100000));
	  m.setSize(600, 800);
	  m.setVisible(true);
		System.out.println(m);
}

int pile=-1, card=-1;
public void mouseClicked(MouseEvent arg0) {
	System.out.println(arg0.getX()+" "+arg0.getY());
	int p=(arg0.getY()-30)/50;
	int c=(arg0.getX()-20)/30;
	System.out.println("pile="+p+" card="+c);
	if (p == sol.size()) {
		if (c<4) sol.deal3();
		if (c>=4 && c<8) sol.moveToAce();
		if (c>=8 && c<12) set("S O L",start,faceup,flags,(int) (Math.random()*100000));
		repaint();
	}
	if (p<0 || p>=sol.size()) return;
	dg_CardPile<dg_CardVisibleAWT> pi = sol.get(p);
	if (c<0 || (c!=0 && c>=pi.size())) return;
	if (pile>=0 && pile!=p) {
		int fromSlot=pile;
		int toSlot=p;
		int n=card;
		if (sol.checkAndMove(fromSlot, toSlot, n));
		pile=-1; card=-1;
	} else { pile=p; card=c; }
	repaint();
}

public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

public void mousePressed(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

public void mouseReleased(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}

}

