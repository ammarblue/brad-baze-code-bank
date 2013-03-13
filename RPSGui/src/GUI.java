import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
public class GUI extends JFrame implements ActionListener{
	JPanel ANIMATION,OPTIONS,BUTTONS,NAMES,PLAYS;
	JButton PLAY,STOP,SET;
	JRadioButton ON,OFF,ROCK,PAPER,SCISSORS;
	JLabel AI,LNAME,PNAME,COMPUTER;
	JTextField NAME;
	Animation A1,A2;
	ButtonGroup GBG,OBG;
	GAME G;
	BufferedImage R,P,S;
	GUI(){
		super("RPS");
		setSize(600,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		JPanel mainPanel=new JPanel();
		mainPanel.setBackground(Color.LIGHT_GRAY);
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel);
		G=new GAME(this);
		JOptionPane.showMessageDialog(null, "This is Rock Paper Scissors\n"+"To start the game enter your name\n"+"Then hit play","RPS",JOptionPane.PLAIN_MESSAGE);
		
		//create panels
		ANIMATION=new JPanel();
		OPTIONS=new JPanel();
		BUTTONS=new JPanel();
		NAMES=new JPanel();
		PLAYS=new JPanel();
		
		//create layout
		ANIMATION.setLayout(new GridLayout(1,2));
		ANIMATION.setVisible(false);
		OPTIONS.setLayout(new GridLayout(3,1));
		BUTTONS.setLayout(new FlowLayout());
		NAMES.setLayout(new GridLayout(1,2));
		PLAYS.setLayout(new GridLayout(4,1));
		
		//set backgrounds
		ANIMATION.setBackground(Color.LIGHT_GRAY);
		OPTIONS.setBackground(Color.LIGHT_GRAY);
		BUTTONS.setBackground(Color.LIGHT_GRAY);
		NAMES.setBackground(Color.LIGHT_GRAY);
		PLAYS.setBackground(Color.LIGHT_GRAY);
		
		//create borders
		ANIMATION.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		OPTIONS.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		BUTTONS.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		NAMES.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		PLAYS.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		
		//add to mainPanel
		mainPanel.add(ANIMATION,BorderLayout.CENTER);
		mainPanel.add(OPTIONS,BorderLayout.EAST);
		mainPanel.add(BUTTONS,BorderLayout.SOUTH);
		mainPanel.add(NAMES,BorderLayout.NORTH);
		mainPanel.add(PLAYS,BorderLayout.WEST);
		
		//create objs
		PLAY=new JButton("PLAY");
		PLAY.setBackground(Color.GREEN);
		STOP=new JButton("STOP");
		STOP.setBackground(Color.RED);
		SET=new JButton("GO!");
		SET.setVisible(false);
		SET.setBackground(Color.WHITE);
		NAME=new JTextField(20);
		ON=new JRadioButton("ON");
		ON.setBackground(Color.LIGHT_GRAY);
		OFF=new JRadioButton("OFF");
		OFF.setBackground(Color.LIGHT_GRAY);
		OFF.setSelected(true);
		AI=new JLabel("AI");
		LNAME=new JLabel("NAME");
		PNAME=new JLabel();
		COMPUTER=new JLabel("PLAYER 2: COMPUTER");
		ROCK=new JRadioButton("ROCK");
		ROCK.setBackground(Color.LIGHT_GRAY);
		PAPER=new JRadioButton("PAPER");
		PAPER.setBackground(Color.LIGHT_GRAY);
		SCISSORS=new JRadioButton("SCISSORS");
		SCISSORS.setBackground(Color.LIGHT_GRAY);
		
		//add action
		PLAY.addActionListener(this);
		STOP.addActionListener(this);
		SET.addActionListener(this);
		ON.addActionListener(this);
		OFF.addActionListener(this);
		ROCK.addActionListener(this);
		PAPER.addActionListener(this);
		SCISSORS.addActionListener(this);
		
		//place obj
		BUTTONS.add(LNAME);
		BUTTONS.add(NAME);
		BUTTONS.add(PLAY);
		BUTTONS.add(STOP);
		
		OPTIONS.add(AI);
		OPTIONS.add(ON);
		OPTIONS.add(OFF);
		
		NAMES.add(PNAME);
		NAMES.add(COMPUTER);
		
		PLAYS.add(ROCK);
		PLAYS.add(PAPER);
		PLAYS.add(SCISSORS);
		PLAYS.add(SET);
		
		//button group GBG
		GBG=new ButtonGroup();
		GBG.add(ROCK);
		GBG.add(PAPER);
		GBG.add(SCISSORS);
		
		//button group OBG
		OBG=new ButtonGroup();
		OBG.add(ON);
		OBG.add(OFF);
		
		//animation
		A1=new Animation(this);
		ANIMATION.add(A1);
		
		A2=new Animation(this);
		ANIMATION.add(A2);
		
		//bring in images
		try{
			R=ImageIO.read(new File("Rock.png"));
			P=ImageIO.read(new File("Paper.png"));
			S=ImageIO.read(new File("Scissors.png"));
		}catch(IOException ei){
			System.out.println("ERROR ON IMAGE LOAD");
		}
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==PLAY){
			if(NAME.getText().equals("")){
				JOptionPane.showMessageDialog(null, "Enter your name","RPS",JOptionPane.PLAIN_MESSAGE);
			}else{
				PNAME.setText("PLAYER 1: "+NAME.getText());
				JOptionPane.showMessageDialog(null, "Are you ready?","RPS",JOptionPane.PLAIN_MESSAGE);
				SET.setVisible(true);
			}
		}else if(e.getSource()==STOP){
			System.exit(0);
		}else if(e.getSource()==SET){
			int win=G.Comp();
			ANIMATION.setVisible(true);
			super.repaint();
			if(win==0){
				JOptionPane.showMessageDialog(null, "TIE!!","RPS",JOptionPane.PLAIN_MESSAGE);
				super.repaint();
			}else if(win==1){
				JOptionPane.showMessageDialog(null, PNAME.getText()+" WINS!!!","RPS",JOptionPane.PLAIN_MESSAGE);
				super.repaint();
			}else if(win==2){
				JOptionPane.showMessageDialog(null, "PLAYER 2: COMPUTER WINS!!!","RPS",JOptionPane.PLAIN_MESSAGE);
				super.repaint();
			}
		}else if(e.getSource()==PAPER){
			A1.STATE=1;
		}else if(e.getSource()==ROCK){
			A1.STATE=0;
		}else if(e.getSource()==SCISSORS){
			A1.STATE=2;
		}
	}
}
class Animation extends JPanel{
	GUI gui;
	int STATE;
	Animation(GUI in){
		gui=in;
	}
	 public void paintComponent(Graphics g){
		super.paintComponents(g);
		if(STATE==0){
			g.drawImage(gui.R,40,50,150,180,null);
		}else if(STATE==1){
			g.drawImage(gui.P,40,50,150,180,null);
		}else if(STATE==2){
			g.drawImage(gui.S,40,50,150,180,null);
		}
	}
}
