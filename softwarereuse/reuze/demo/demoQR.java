package reuze.demo;

import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import reuze.awt.ib_MatrixBitsToImage;

import com.software.reuze.c_BarFormat;
import com.software.reuze.c_QRWriter;
import com.software.reuze.d_MatrixBits;
import com.software.reuze.l_ExceptionBarcodeWriter;


public class demoQR extends JFrame {

	  public demoQR() {
	    super("Test");
	    registerListeners();
	    pack();
	  }
	  
	  @Override public void paint(Graphics g) {
		  BufferedImage buf=ib_MatrixBitsToImage.toBufferedImage(matrix);
		  g.drawImage(buf,50,50,null);
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
		    demoQR m=new demoQR();
			int bigEnough = 256;
		    c_QRWriter writer = new c_QRWriter();
		    try {
				m.matrix = writer.encode("http://www.google.com/", c_BarFormat.QR_CODE, bigEnough,
				    bigEnough, null);
			} catch (l_ExceptionBarcodeWriter e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  m.setSize(600, 400);
		  m.setVisible(true);
	  }
	d_MatrixBits matrix=null;
}
