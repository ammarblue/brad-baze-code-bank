// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// AppGUICounter.java
//
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class AppGUICounter extends Frame implements Runnable {
	int count = 0;
	int runFlag = 1;
	Label lab = new Label();
	Button onOff = new Button("Toggle");
	Button stop = new Button("Stop");
	TextField t = new TextField(10);
	Panel p = new Panel();

	public int GetMemoryCell(int address) {
		return 0;
	}

	public void SetMemoryCell(int address, int value) {
	}

	public AppGUICounter(String app_label) {
		p.add(t);
		// onOff.addActionListener(new OnOffL());
		p.add(onOff);
		// stop.addActionListener(new StopL());
		p.add(stop);
		lab.setText(app_label);
		add("North", lab);
		add("Center", p);
		setSize(230, 90);
		setVisible(true);
	}

	public void delay(int ticks) {
		int counter = 0;
		for (int i = 0; i < ticks; ++i) {
			for (int j = 0; j < 100000; ++j) {
				++counter;
			}
		}
	}

	public void run() {
		while (runFlag != 0) {
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
			}
			if (runFlag == 1)
				t.setText(Integer.toString(count++));
		}
		setVisible(false);
	}

	public boolean action(Event evt, Object arg) {
		if (evt.target.equals(onOff))
			runFlag = -runFlag;
		else if (evt.target.equals(stop))
			runFlag = 0;
		else
			return super.action(evt, arg);
		return true;
	}
	// class OnOffL implements ActionListener {
	// public void actionPerformed(ActionEvent e) {
	// runFlag = -runFlag;
	// }
	// }
	// class StopL implements ActionListener {
	// public void actionPerformed(ActionEvent e) {
	// runFlag = 0;
	// }
	// }
}
