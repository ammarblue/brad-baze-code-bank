package reuze.demo;

import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import javax.swing.JFrame;

import reuze.awt.ga_TiledAWT;

import com.software.reuze.ff_TMXLoader;
import com.software.reuze.ga_TiledMap;

public class demoTMX extends JFrame {
	ga_TiledAWT ta;
	public demoTMX() {
		super("Test");
		registerListeners();
		pack();
	}

	@Override public void paint(Graphics g) {
		if (ta!=null) ta.paint(g);
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
		demoTMX m=new demoTMX();
		m.setSize(600, 400);
		m.setVisible(true);
		ga_TiledMap map=null;
		try {
			map=ff_TMXLoader.createMap(new InputStreamReader(new FileInputStream("./data/tiledmap/tilespacing-test.tmx")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(map);
		m.ta=new ga_TiledAWT(map);
		m.repaint();
	}
}
