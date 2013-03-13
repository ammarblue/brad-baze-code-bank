import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GUI extends JFrame implements ActionListener{
	JButton RUN,STOP,TEST;
	JTextField NUM_BLOCKS;
	JLabel BL;
	JPanel TOWER_PANEL,BUTTONS;
	Animation T;
	Tower TOWER;
	GUI(){
		super("Tower of Hanoi");
		setSize(500,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel mainPanel=new JPanel();
		add(mainPanel);
		mainPanel.setLayout(new BorderLayout());
		
		BUTTONS=new JPanel();
		BUTTONS.setLayout(new FlowLayout());
		mainPanel.add(BUTTONS,BorderLayout.SOUTH);
		
		TOWER_PANEL=new JPanel();
		TOWER_PANEL.setLayout(new BorderLayout());
		mainPanel.add(TOWER_PANEL,BorderLayout.CENTER);
		
		BL=new JLabel("Number of Blocks");
		BUTTONS.add(BL);
		
		NUM_BLOCKS=new JTextField(10);
		NUM_BLOCKS.addActionListener(this);
		BUTTONS.add(NUM_BLOCKS);
		
		RUN=new JButton("RUN");
		RUN.addActionListener(this);
		BUTTONS.add(RUN);
		
		STOP=new JButton("STOP");
		STOP.addActionListener(this);
		BUTTONS.add(STOP);
		
		TEST=new JButton("TEST");
		TEST.addActionListener(this);
		BUTTONS.add(TEST);
		
		T=new Animation(this);
		TOWER=new Tower(this);
		
		TOWER_PANEL.add(T);

	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==RUN){
			TOWER.run(Integer.parseInt(NUM_BLOCKS.getText()));
		}else if(e.getSource()==STOP){
			System.exit(0);
		}if(e.getSource()==TEST){
			T.repaint();
		}
	}
}
class Animation extends JPanel{
	GUI gui;
	Animation(GUI in){
		gui=in;
	}
	 public void paintComponent(Graphics g){
		 super.paintComponents(g);
		 g.drawRect(50+(gui.TOWER.t1*150), 300-(gui.TOWER.h1*20), 60, 20);//1
		 g.drawRect(60, 280, 40, 20);//2
		 g.drawRect(70, 260, 20, 20);//3
	 }
}
