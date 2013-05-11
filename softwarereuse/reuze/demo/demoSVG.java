package reuze.demo;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

import reuze.awt.ff_SVGDrawAWT;

import com.software.reuze.ff_SVGReader;

public class demoSVG extends JFrame {

	  public demoSVG() {
	    super("Test");
	    registerListeners();
	    pack();
	  }
	  
	  static ArrayList<Object> objects=null;
	  static boolean done=false;
	  @Override public void paint(Graphics g) {
		  if (!done) return;
		  g.translate(50,50);
		  ff_SVGDrawAWT.paintObjects((Graphics2D)g, objects);
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
	  
	  public static void main(String args[]) {
		  demoSVG m=new demoSVG();
		  m.setSize(600, 400);
		  m.setVisible(true);
		  	try {
				objects=ff_SVGReader.parse(new Scanner(new FileInputStream("./data/bot1.svg")));
			} catch (FileNotFoundException e) {
				System.exit(0);
			}
		  done=true;
		  m.repaint();
	  }
}
