
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class midterm1 extends JFrame implements ActionListener{
	JPanel p1,p2,p3;
	JButton b1,b2,b3,b4;
	public static void main(String args[]){
		midterm1 gui=new midterm1();
		gui.setVisible(true);
	}
	public midterm1(){
		super("TEST");
		setSize(300,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		JPanel mainpanel=new JPanel();
		mainpanel.setLayout(new GridLayout(1,3));
		p1=new JPanel();
		p1.setBackground(Color.LIGHT_GRAY);
		mainpanel.add(p1);
		p2=new JPanel();
		p2.setBackground(Color.LIGHT_GRAY);
		mainpanel.add(p2);
		p3=new JPanel();
		p3.setBackground(Color.LIGHT_GRAY);
		mainpanel.add(p3);
		add(mainpanel,BorderLayout.CENTER);
		JPanel buttons=new JPanel();
		buttons.setBackground(Color.LIGHT_GRAY);
		buttons.setLayout(new FlowLayout());
		b1=new JButton("B1");
		b1.setBackground(Color.YELLOW);
		b1.addActionListener(this);
		buttons.add(b1);
		b2=new JButton("B2");
		b2.setBackground(Color.CYAN);
		b2.addActionListener(this);
		buttons.add(b2);
		b3=new JButton("B3");
		b3.setBackground(Color.MAGENTA);
		b3.addActionListener(this);
		buttons.add(b3);
		b4=new JButton("Clear");
		b4.setBackground(Color.WHITE);
		b4.addActionListener(this);
		buttons.add(b4);
		add(buttons,BorderLayout.SOUTH);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==b1){
			p1.setBackground(Color.BLUE);
		}else if(e.getSource()==b2){
			p2.setBackground(Color.BLUE);
		}else if(e.getSource()==b3){
			p3.setBackground(Color.BLUE);
		}else if(e.getSource()==b4){
			p1.setBackground(Color.LIGHT_GRAY);
			p2.setBackground(Color.LIGHT_GRAY);
			p3.setBackground(Color.LIGHT_GRAY);
		}else{
			System.out.println("ERROR");
		}
	}
}