import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GUI extends JFrame implements ActionListener{
	int optionsState;
	String commands;
	JLabel L1,L2;
	JPanel text;
	JPanel buttons;
	JPanel options;
	JTextArea terminal;
	JTextField input;
	JTextArea timer;
	JButton run;
	JButton stop;
	JRadioButton PrimeNumber;
	JRadioButton MatrixAdd;
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
		
		//creates options panel
		options=new JPanel();
		options.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		options.setLayout(new GridLayout(2,2));
		mainPanel.add(options,BorderLayout.EAST);
		
		//creates buttons
		run=new JButton("RUN");
		run.setBackground(Color.GREEN);
		run.addActionListener(this);
		buttons.add(run);
		
		stop=new JButton("STOP");
		stop.setBackground(Color.RED);
		stop.addActionListener(this);
		buttons.add(stop);
		
		//creates radio boxes
		ButtonGroup G1=new ButtonGroup();
		
		PrimeNumber=new JRadioButton("Prime Number");
		G1.add(PrimeNumber);
		PrimeNumber.addActionListener(this);
		options.add(PrimeNumber);
		
		MatrixAdd=new JRadioButton("Matrix Add");
		G1.add(MatrixAdd);
		MatrixAdd.addActionListener(this);
		options.add(MatrixAdd);
		
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
			if(e.getSource()==MatrixAdd){optionsState=1;
			}else if(e.getSource()==PrimeNumber){optionsState=2;}
			commands=input.getText();
			M=new terminaLibrary();
			String out=M.read(commands,optionsState);
			if(out.compareTo("Date: ")==-32){
				timer.setText(out);
			}else{
				terminal.setText(out);
			}
		}else if(e.getSource()==stop){
			System.exit(ABORT);
		}
	}
}
