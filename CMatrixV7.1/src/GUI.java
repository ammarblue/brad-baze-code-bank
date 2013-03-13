import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GUI extends JFrame implements ActionListener{
	int optionsState;
	int matrixOUT[][]=new int[100][100];
	String commands;
	JLabel L1,L2;
	JPanel text;
	JPanel buttons;
	JTextArea terminal;
	JTextField input;
	JTextArea timer;
	JButton run;
	JButton stop;
	terminaLibrary M;
	public GUI(){
		//set up main window
		super("OpenMP Java Test");
		long startTime=System.nanoTime();
		setSize(550,400);
		setBackground(Color.LIGHT_GRAY);
		setLocation(400,150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//create main panel 
		JPanel mainPanel=new JPanel();
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setBackground(Color.LIGHT_GRAY);
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel,BorderLayout.CENTER);
		
		//create the buttons panel
		buttons=new JPanel();
		buttons.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		buttons.setBackground(Color.darkGray);
		buttons.setLayout(new FlowLayout());
		mainPanel.add(buttons,BorderLayout.SOUTH);
		
		//creates text panel
		text=new JPanel();
		text.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		text.setBackground(Color.LIGHT_GRAY);
		text.setLayout(new BorderLayout());
		mainPanel.add(text,BorderLayout.CENTER);
		
		//creates buttons
		run=new JButton("RUN");
		run.setBackground(Color.GREEN);
		run.addActionListener(this);
		buttons.add(run);
		
		stop=new JButton("STOP");
		stop.setBackground(Color.RED);
		stop.addActionListener(this);
		buttons.add(stop);
		
		//create terminal and text input
		input=new JTextField("<#");
		input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		input.setBackground(Color.WHITE);
		text.add(input,BorderLayout.NORTH);
		
		terminal=new JTextArea();
		terminal.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		terminal.setBackground(Color.WHITE);
		terminal.setEditable(false);
		text.add(terminal,BorderLayout.CENTER);
		
		timer=new JTextArea();
		timer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		timer.setBackground(Color.LIGHT_GRAY);
		timer.setEditable(false);
		text.add(timer,BorderLayout.SOUTH);
		long endTime=System.nanoTime();
		String time=Long.toString(endTime-startTime);
		timer.setText("TIME FOR START UP: "+time+"ns");
	}
	//action listener
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==run){
			SysOut();
		}else if(e.getSource()==stop){
			System.exit(ABORT);
		}
	}
	//print method
	public void SysOut(){
		terminal.setText("");
		commands=input.getText();
		M=new terminaLibrary();
		String out=M.read(commands,optionsState)+"\nEND OF LINE";
		if(out.compareTo("Date: ")==-32){
			timer.setText(out);
		}else{
			terminal.setText(out);
		}
		input.setText("<#");
	}
	//takes the final matrix and copies it for printing 
	public String MatrixSwap(){
		String temp="";
		for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				temp=temp+"\n"+matrixOUT[i][j];
				System.out.println(matrixOUT[i][j]);
			}
		}
		return temp;
	}
}
