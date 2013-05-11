package reuze.app;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.software.reuze.lev_Observable;
import com.software.reuze.lev_i_Observer;

public class appTest {
	public class Counter {
		// The model.
		public int count;

		public Counter(int count) {
			this.count = count;
		}

		public int getCount() {
			return count;
		}

		public void incCount() {
			count++;
		}

		public void decCount() {
			count--;
		}
	}

	public class CounterObservable extends Counter {
		public lev_Observable o;

		public CounterObservable(int count) {
			super(count);
			o = new lev_Observable();
		}

		public void incCount() {
			count++;
			o.setChanged();
			o.notifyObservers(this);
		}

		public void decCount() {
			count--;
			o.setChanged();
			o.notifyObservers(this);
		}

		public void addObserver(lev_i_Observer ob) {
			o.addObserver(ob);
		}
	}

	public class CounterView extends Frame implements lev_i_Observer {
		// The view.
		private TextField tf = new TextField(10);
		// A reference to our associated model.
		private CounterObservable counter;

		public void update(lev_Observable o, Object arg) {
			tf.setText(counter.getCount() + "");
			tf.repaint();
		}

		public CounterView(String title, CounterObservable c) {
			super(title);
			counter = c;
			c.addObserver(this);
			Panel tfPanel = new Panel();
			tf.setText(counter.getCount() + "");
			tfPanel.add(tf);
			add("North", tfPanel);
			Panel buttonPanel = new Panel();
			Button incButton = new Button("Increment");
			incButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					counter.incCount();
					tf.setText(counter.getCount() + "");
				}
			});
			buttonPanel.add(incButton);
			Button decButton = new Button("Decrement");
			decButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					counter.decCount();
					tf.setText(counter.getCount() + "");
				}
			});
			buttonPanel.add(decButton);
			Button exitButton = new Button("Exit");
			exitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			buttonPanel.add(exitButton);
			add("South", buttonPanel);
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
		}
	}

	public static void main(String args[]) {
		appTest x = new appTest();
		CounterObservable counter = x.new CounterObservable(0);
		CounterView cv1 = x.new CounterView("CounterView1", counter);
		cv1.setSize(300, 100);
		cv1.setVisible(true);
		CounterView cv2 = x.new CounterView("CounterView2", counter);
		cv2.setSize(300, 100);
		cv2.setVisible(true);
	}
}
