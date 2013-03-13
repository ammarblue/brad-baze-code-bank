import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GAME_FRAME extends JFrame{
	public static final int SCALE = 3;
	public static final int WIDTH = 282*SCALE;
	public static final int HEIGHT = 188*SCALE;
	public static final String NAME = "FINAL TESTAMENT - ALPHA";
	public static final JPanel displayPanel = new JPanel();
	
	
	public GAME_FRAME(){
		LOAD.LOAD_ALL(this);
		setTitle(NAME);
		setMinimumSize(new Dimension(WIDTH, HEIGHT ));
		setMaximumSize(new Dimension(WIDTH , HEIGHT));
		setPreferredSize(new Dimension(WIDTH , HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(displayPanel);
		displayPanel.setLayout(null);
		displayPanel.setBounds(0, 0, GAME_FRAME.WIDTH, GAME_FRAME.HEIGHT);
		displayPanel.setFocusable(true);
		displayPanel.setVisible(true);
		STAR_ANIMATION.ANIMATE_STARS();
		displayPanel.add(new WELCOME_PANEL());///FIRST PANEL TO DISPLAY
		//displayPanel.add(new PLAYER_CREATION_PANEL());
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);	
	}
}
