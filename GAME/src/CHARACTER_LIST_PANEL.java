import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class CHARACTER_LIST_PANEL extends JPanel {
	static Timer timer = new Timer();
	static JButton newChar;
	static JButton loadChar;
	static JButton delete;
	static JButton delete2;
	static JButton delete3;
	static JButton delete4;
	static JButton delete5;
	static JButton delete6;
	static JButton play;
	static JButton back;
	static JButton charButton1;
	static JButton charButton2;
	static JButton charButton3;
	static JButton charButton4;
	static JButton charButton5;
	static JButton charButton6;
	static JPanel charPanel1;
	static JPanel charPanel2;
	static JPanel charPanel3;
	static JPanel charPanel4;
	static JPanel charPanel5;
	static JPanel charPanel6;
	static CHARACTER selectedCharacter;
	static int highlightY = -CHARACTER_LIST_IMAGES.CLI[10].getHeight(null)*GAME_FRAME.SCALE;
	static int selectedCharIndex = -1;
	public static void moveToPCPanel(){
	TimerTask moveToPCPanel = new TimerTask(){
		public void run() {
			GAME_FRAME.displayPanel.removeAll();
			///**** CHANGE THIS TO LOAD NEXT PANEL
			GAME_FRAME.displayPanel.add(new PLAYER_CREATION_PANEL());
		}
	};
	timer.schedule(moveToPCPanel, 1500L);
	}
	TimerTask moveToWPanel = new TimerTask(){
		public void run() {
			GAME_FRAME.displayPanel.removeAll();
			///**** CHANGE THIS TO LOAD NEXT PANEL
			GAME_FRAME.displayPanel.add(new WELCOME_PANEL());
		}
	};
	public CHARACTER_LIST_PANEL(){
		setLayout(null);
		setBounds(0, 0, GAME_FRAME.WIDTH, GAME_FRAME.HEIGHT);
		RENDER.RENDER(this);
		
		//newChar
		newChar = new JButton();
		newChar.setSize(CHARACTER_LIST_IMAGES.CLTI[4].getWidth(null),CHARACTER_LIST_IMAGES.CLTI[4].getHeight(null));
		newChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[4]));
		newChar.setDisabledIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[4]));
		newChar.setBorder(BorderFactory.createEmptyBorder());
		newChar.setContentAreaFilled(false);
		newChar.addMouseListener(new java.awt.event.MouseAdapter() {
			int temp = 0;
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	newChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[5]));
		    	temp = 1;
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	newChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[4]));
		    	temp = 0;
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	newChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[6]));
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	if(temp == 1){
		    		newChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[5]));
		    	}else{
		    		newChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[4]));
		    	}
		    }
		    
		});
		newChar.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	setAllEnabled(false);
            	TRANSITION1.ANIMATE_TRANSITIION1(new PLAYER_CREATION_PANEL());
            	moveToPCPanel();
            	
            }
        });
		newChar.setLocation(-1*GAME_FRAME.SCALE, 1*GAME_FRAME.SCALE);
		
		//play
		play = new JButton();
		play.setSize(CHARACTER_LIST_IMAGES.CLI[1].getWidth(null),CHARACTER_LIST_IMAGES.CLI[1].getHeight(null));
		play.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[1]));
		play.setDisabledIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[1]));
		play.setBorder(BorderFactory.createEmptyBorder());
		play.setContentAreaFilled(false);
		play.addMouseListener(new java.awt.event.MouseAdapter() {
			int temp = 0;
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	play.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[2]));
		    	temp = 1;
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	play.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[1]));
		    	temp = 0;
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	play.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[3]));
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	if(temp == 1){
		    		play.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[2]));
		    	}else{
		    		play.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[1]));
		    	}
		    }
		    
		});
		play.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	
            }
        });
		play.setLocation((GAME_FRAME.WIDTH/2)- (CHARACTER_LIST_IMAGES.CLI[1].getWidth(null)/2), 150*GAME_FRAME.SCALE);
		add(play);
		//back
		back = new JButton();
		back.setSize(CHARACTER_LIST_IMAGES.CLI[4].getWidth(null),CHARACTER_LIST_IMAGES.CLI[4].getHeight(null));
		back.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[4]));
		back.setDisabledIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[4]));
		back.setBorder(BorderFactory.createEmptyBorder());
		back.setContentAreaFilled(false);
		back.addMouseListener(new java.awt.event.MouseAdapter() {
			int temp = 0;
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	back.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[5]));
		    	temp = 1;
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	back.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[4]));
		    	temp = 0;
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	back.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[6]));
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	if(temp == 1){
		    		back.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[5]));
		    	}else{
		    		back.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[4]));
		    	}
		    }
		    
		});
		back.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	setAllEnabled(false);
            	TRANSITION1.ANIMATE_TRANSITIION1(new WELCOME_PANEL());
            	timer.schedule(moveToWPanel, 1500L);
            }
        });
		back.setLocation((GAME_FRAME.WIDTH/2)- (CHARACTER_LIST_IMAGES.CLI[1].getWidth(null)/2)-98*GAME_FRAME.SCALE, 150*GAME_FRAME.SCALE);
		add(back);
		
		//loadChar
		loadChar = new JButton();
		loadChar.setSize(CHARACTER_LIST_IMAGES.CLTI[1].getWidth(null),CHARACTER_LIST_IMAGES.CLTI[1].getHeight(null));
		loadChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[1]));
		loadChar.setDisabledIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[1]));
		loadChar.setBorder(BorderFactory.createEmptyBorder());
		loadChar.setContentAreaFilled(false);
		loadChar.addMouseListener(new java.awt.event.MouseAdapter() {
			int temp = 0;
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	loadChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[2]));
		    	temp = 1;
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	loadChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[1]));
		    	temp = 0;
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	loadChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[3]));
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	if(temp == 1){
		    		loadChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[2]));
		    	}else{
		    		loadChar.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLTI[1]));
		    	}
		    }
		    
		});
		loadChar.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	
            }
        });
		loadChar.setLocation(28*GAME_FRAME.SCALE, 1*GAME_FRAME.SCALE);
		//delete
		delete = new JButton();
		delete.setSize(CHARACTER_LIST_IMAGES.CLI[11].getWidth(null),CHARACTER_LIST_IMAGES.CLI[11].getHeight(null));
		delete.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[11]));
		delete.setDisabledIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[11]));
		delete.setBorder(BorderFactory.createEmptyBorder());
		delete.setContentAreaFilled(false);
		delete.setFocusable(false);
		delete.addMouseListener(new java.awt.event.MouseAdapter() {
			int temp = 0;
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	delete.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[12]));
		    	temp = 1;
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	delete.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[11]));
		    	temp = 0;
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	delete.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[13]));
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	if(temp == 1){
		    		delete.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[12]));
		    	}else{
		    		delete.setIcon(new ImageIcon(CHARACTER_LIST_IMAGES.CLI[11]));
		    	}
		    }
		});
		delete.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	CHARACTER_LIST.characters.remove(selectedCharIndex);
            	setCharPanels();
        		highlightSelectedCharacter();
            }
        });
		delete.setLocation(GAME_FRAME.SCALE*47 ,GAME_FRAME.SCALE*8);
		
		//charButton1
		charButton1 = new JButton();
		charButton1.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charButton1.setBorder(BorderFactory.createEmptyBorder());
		charButton1.setHorizontalAlignment(SwingConstants.LEFT);
		charButton1.setContentAreaFilled(false);
		charButton1.setFocusable(false);
		charButton1.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	
		    } 
		});
		charButton1.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	selectedCharIndex = 0;
            	selectedCharacter = CHARACTER_LIST.characters.get(selectedCharIndex);	
            	highlightSelectedCharacter();
            }
        });
		charButton1.setLocation(GAME_FRAME.SCALE*3,0);

		
		//charButton2
		charButton2 = new JButton();
		charButton2.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charButton2.setBorder(BorderFactory.createEmptyBorder());
		charButton2.setHorizontalAlignment(SwingConstants.LEFT);
		charButton2.setContentAreaFilled(false);
		charButton2.setFocusable(false);
		charButton2.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	
		    } 
		});
		charButton2.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	selectedCharIndex = 1;
            	selectedCharacter = CHARACTER_LIST.characters.get(selectedCharIndex);	
            	highlightSelectedCharacter();
            }
        });
		charButton2.setLocation(GAME_FRAME.SCALE*3,0);

		
		//charButton3
		charButton3 = new JButton();
		charButton3.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charButton3.setBorder(BorderFactory.createEmptyBorder());
		charButton3.setHorizontalAlignment(SwingConstants.LEFT);
		charButton3.setContentAreaFilled(false);
		charButton3.setFocusable(false);
		charButton3.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	
		    } 
		});
		charButton3.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	selectedCharIndex = 2;
            	selectedCharacter = CHARACTER_LIST.characters.get(selectedCharIndex);	
            	highlightSelectedCharacter();
            }
        });
		charButton3.setLocation(GAME_FRAME.SCALE*3,0);
		//charButton4
		charButton4 = new JButton();
		charButton4.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charButton4.setBorder(BorderFactory.createEmptyBorder());
		charButton4.setHorizontalAlignment(SwingConstants.LEFT);
		charButton4.setContentAreaFilled(false);
		charButton4.setFocusable(false);
		charButton4.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	
		    } 
		});
		charButton4.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	selectedCharIndex = 3;
            	selectedCharacter = CHARACTER_LIST.characters.get(selectedCharIndex);	
            	highlightSelectedCharacter();
            }
        });
		charButton4.setLocation(GAME_FRAME.SCALE*3,0);
		//charButton5
		charButton5 = new JButton();
		charButton5.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charButton5.setBorder(BorderFactory.createEmptyBorder());
		charButton5.setHorizontalAlignment(SwingConstants.LEFT);
		charButton5.setContentAreaFilled(false);
		charButton5.setFocusable(false);
		charButton5.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	
		    } 
		});
		charButton5.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	selectedCharIndex = 4;
            	selectedCharacter = CHARACTER_LIST.characters.get(selectedCharIndex);	
            	highlightSelectedCharacter();
            }
        });
		charButton5.setLocation(GAME_FRAME.SCALE*3,0);
		//charButton6
		charButton6 = new JButton();
		charButton6.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charButton6.setBorder(BorderFactory.createEmptyBorder());
		charButton6.setHorizontalAlignment(SwingConstants.LEFT);
		charButton6.setContentAreaFilled(false);
		charButton6.setFocusable(false);
		charButton6.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	
		    } 
		});
		charButton6.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {
            	selectedCharIndex = 5;
            	selectedCharacter = CHARACTER_LIST.characters.get(selectedCharIndex);	
            	highlightSelectedCharacter();
            }
        });
		charButton6.setLocation(GAME_FRAME.SCALE*3,0);
		
		
		charPanel1 = new JPanel();
		charPanel1.setLayout(null);
		charPanel1.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charPanel1.setLocation(207*GAME_FRAME.SCALE,35*GAME_FRAME.SCALE);
		charPanel1.setOpaque(false);
		charPanel1.setVisible(true);
		add(charPanel1);
		charPanel2 = new JPanel();
		charPanel2.setLayout(null);
		charPanel2.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charPanel2.setLocation(207*GAME_FRAME.SCALE,53*GAME_FRAME.SCALE);
		charPanel2.setOpaque(false);
		charPanel2.setVisible(false);
		add(charPanel2);
		charPanel3 = new JPanel();
		charPanel3.setLayout(null);
		charPanel3.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charPanel3.setLocation(207*GAME_FRAME.SCALE,71*GAME_FRAME.SCALE);
		charPanel3.setOpaque(false);
		charPanel3.setVisible(false);
		add(charPanel3);
		charPanel4 = new JPanel();
		charPanel4.setLayout(null);
		charPanel4.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charPanel4.setLocation(207*GAME_FRAME.SCALE,89*GAME_FRAME.SCALE);
		charPanel4.setOpaque(false);
		charPanel4.setVisible(false);
		add(charPanel4);
		charPanel5 = new JPanel();
		charPanel5.setLayout(null);
		charPanel5.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charPanel5.setLocation(207*GAME_FRAME.SCALE,107*GAME_FRAME.SCALE);
		charPanel5.setOpaque(false);
		charPanel5.setVisible(false);
		add(charPanel5);
		charPanel6 = new JPanel();
		charPanel6.setLayout(null);
		charPanel6.setSize(CHARACTER_LIST_IMAGES.CLI[10].getWidth(null),CHARACTER_LIST_IMAGES.CLI[10].getHeight(null));
		charPanel6.setLocation(207*GAME_FRAME.SCALE,125*GAME_FRAME.SCALE);
		charPanel6.setOpaque(false);
		charPanel6.setVisible(false);
		add(charPanel6);
		
		setCharPanels();
		highlightSelectedCharacter();
		setFocusable(true);
		setVisible(true);
	}
	public void setAllEnabled(boolean enabled){
		setEnabled2(enabled,CHARACTER_LIST_PANEL.this);
		setEnabled2(enabled,charPanel1);
		setEnabled2(enabled,charPanel2);
		setEnabled2(enabled,charPanel3);
		setEnabled2(enabled,charPanel4);
		setEnabled2(enabled,charPanel5);
		setEnabled2(enabled,charPanel6);
	}
	
	public void setEnabled2(boolean enabled, JPanel panel) {
		Component[] com = panel.getComponents();  
		for (int a = 0; a < com.length; a++) {  
		     com[a].setEnabled(enabled);  
		}  
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(PLAYER_CREATION_IMAGES.PCI[0], 0, 0, null);
		STAR_ANIMATION.RENDER_STARS(g2d);
		//g2d.drawImage(CHARACTER_LIST_IMAGES.CLTI[14], GAME_FRAME.SCALE *9, GAME_FRAME.SCALE *9, null);
		g2d.drawImage(PLAYER_CREATION_IMAGES.PCI[1], 182*GAME_FRAME.SCALE, -6*GAME_FRAME.SCALE, null);
		g2d.drawImage(CHARACTER_LIST_IMAGES.CLTI[0], 209*GAME_FRAME.SCALE+1, GAME_FRAME.SCALE * 24, null);
		g2d.drawImage(CHARACTER_LIST_IMAGES.CLI[10], 207*GAME_FRAME.SCALE, highlightY, null);
	}
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		TRANSITION1.RENDER_TRANSITION1(g2d);
	}
	public static void highlightSelectedCharacter(){
		try{
		if(selectedCharIndex == -1){
			highlightY = -CHARACTER_LIST_IMAGES.CLI[10].getHeight(null)*GAME_FRAME.SCALE;
		}else if(selectedCharIndex == 0){
			highlightY = GAME_FRAME.SCALE * 36;
			charPanel1.add(delete);
			charPanel1.setComponentZOrder(charButton1, 1);
			charPanel1.setComponentZOrder(delete, 0);
		}else if(selectedCharIndex == 1){
			highlightY = GAME_FRAME.SCALE * 54;
			charPanel2.add(delete);
			charPanel2.setComponentZOrder(charButton2, 1);
			charPanel2.setComponentZOrder(delete, 0);
		}else if(selectedCharIndex == 2){
			highlightY = GAME_FRAME.SCALE * 72;
			charPanel3.add(delete);
			charPanel3.setComponentZOrder(charButton3, 1);
			charPanel3.setComponentZOrder(delete, 0);
		}else if(selectedCharIndex == 3){
			highlightY = GAME_FRAME.SCALE * 90;
			charPanel4.add(delete);
			charPanel4.setComponentZOrder(charButton4, 1);
			charPanel4.setComponentZOrder(delete, 0);
		}else if(selectedCharIndex == 4){
			highlightY = GAME_FRAME.SCALE * 108;
			charPanel5.add(delete);
			charPanel5.setComponentZOrder(charButton5, 1);
			charPanel5.setComponentZOrder(delete, 0);
		}else if(selectedCharIndex == 5){
			highlightY = GAME_FRAME.SCALE * 126;
			charPanel6.add(delete);
			charPanel6.setComponentZOrder(charButton6, 1);
			charPanel6.setComponentZOrder(delete, 0);
		}
		}catch(Exception E){
			
		}
	}
	public static void setCharPanels(){
		int numOfChars = CHARACTER_LIST.characters.size();
		charPanel1.remove(charButton1);
		charPanel1.remove(delete);
		charPanel2.remove(charButton2);
		charPanel2.setVisible(false);
		charPanel3.remove(charButton3);
		charPanel3.setVisible(false);
		charPanel4.remove(charButton4);
		charPanel4.setVisible(false);
		charPanel5.remove(charButton5);
		charPanel5.setVisible(false);
		charPanel6.remove(charButton6);
		charPanel6.setVisible(false);
		selectedCharIndex = -1;
		
		if(numOfChars >= 0){
			charPanel1.add(newChar);
			charPanel1.add(loadChar);
			if(numOfChars >= 1){
				selectedCharIndex = 0;
				selectedCharacter = CHARACTER_LIST.characters.get(selectedCharIndex);
				charPanel1.remove(newChar);
				charPanel1.remove(loadChar);
				charButton1.setText("<html><div align='left'><font size='"+(GAME_FRAME.SCALE-1)+"' face= 'PF Tempesta Seven' color = 'yellow" +
						"'>"+CHARACTER_LIST.characters.get(0).getName()+"<br><font size='"+(GAME_FRAME.SCALE-2)+"'color = 'white'>Level " +
						""+CHARACTER_LIST.characters.get(0).getLevel()+" "+CHARACTER_LIST.characters.get(0).getRace()+"<br>" +
						""+CHARACTER_LIST.characters.get(0).getClass1()+"</html>");
				charPanel1.add(charButton1);
				charPanel2.setVisible(true);
				charPanel2.add(newChar);
				charPanel2.add(loadChar);
				if(numOfChars >= 2){
					charPanel2.remove(newChar);
					charPanel2.remove(loadChar);
					charButton2.setText("<html><div align='left'><font size='"+(GAME_FRAME.SCALE-1)+"' face= 'PF Tempesta Seven' color = 'yellow" +
							"'>"+CHARACTER_LIST.characters.get(1).getName()+"<br><font size='"+(GAME_FRAME.SCALE-2)+"'color = 'white'>Level " +
							""+CHARACTER_LIST.characters.get(1).getLevel()+" "+CHARACTER_LIST.characters.get(1).getRace()+"<br>" +
							""+CHARACTER_LIST.characters.get(1).getClass1()+"</html>");
					charPanel2.add(charButton2);
					charPanel3.setVisible(true);
					charPanel3.add(newChar);
					charPanel3.add(loadChar);
					if(numOfChars >= 3){
						charPanel3.remove(newChar);
						charPanel3.remove(loadChar);
						charButton3.setText("<html><div align='left'><font size='"+(GAME_FRAME.SCALE-1)+"' face= 'PF Tempesta Seven' color = 'yellow" +
								"'>"+CHARACTER_LIST.characters.get(2).getName()+"<br><font size='"+(GAME_FRAME.SCALE-2)+"'color = 'white'>Level " +
								""+CHARACTER_LIST.characters.get(2).getLevel()+" "+CHARACTER_LIST.characters.get(2).getRace()+"<br>" +
								""+CHARACTER_LIST.characters.get(2).getClass1()+"</html>");
						charPanel3.add(charButton3);
						charPanel4.setVisible(true);
						charPanel4.add(newChar);
						charPanel4.add(loadChar);
						if(numOfChars >= 4){
							charPanel4.remove(newChar);
							charPanel4.remove(loadChar);
							charButton4.setText("<html><div align='left'><font size='"+(GAME_FRAME.SCALE-1)+"' face= 'PF Tempesta Seven' color = 'yellow" +
									"'>"+CHARACTER_LIST.characters.get(3).getName()+"<br><font size='"+(GAME_FRAME.SCALE-2)+"'color = 'white'>Level " +
									""+CHARACTER_LIST.characters.get(3).getLevel()+" "+CHARACTER_LIST.characters.get(3).getRace()+"<br>" +
									""+CHARACTER_LIST.characters.get(3).getClass1()+"</html>");
							charPanel4.add(charButton4);
							charPanel5.setVisible(true);
							charPanel5.add(newChar);
							charPanel5.add(loadChar);
							if(numOfChars >= 5){
								charPanel5.remove(newChar);
								charPanel5.remove(loadChar);
								charButton5.setText("<html><div align='left'><font size='"+(GAME_FRAME.SCALE-1)+"' face= 'PF Tempesta Seven' color = 'yellow" +
										"'>"+CHARACTER_LIST.characters.get(4).getName()+"<br><font size='"+(GAME_FRAME.SCALE-2)+"'color = 'white'>Level " +
										""+CHARACTER_LIST.characters.get(4).getLevel()+" "+CHARACTER_LIST.characters.get(4).getRace()+"<br>" +
										""+CHARACTER_LIST.characters.get(4).getClass1()+"</html>");
								charPanel5.add(charButton5);
								charPanel6.setVisible(true);
								charPanel6.add(newChar);
								charPanel6.add(loadChar);
								if(numOfChars >= 6){
									charPanel6.remove(newChar);
									charPanel6.remove(loadChar);
									charButton6.setText("<html><div align='left'><font size='"+(GAME_FRAME.SCALE-1)+"' face= 'PF Tempesta Seven' color = 'yellow" +
											"'>"+CHARACTER_LIST.characters.get(5).getName()+"<br><font size='"+(GAME_FRAME.SCALE-2)+"'color = 'white'>Level " +
											""+CHARACTER_LIST.characters.get(5).getLevel()+" "+CHARACTER_LIST.characters.get(5).getRace()+"<br>" +
											""+CHARACTER_LIST.characters.get(5).getClass1()+"</html>");
									charPanel6.add(charButton6);
									
			
			
		}}}}}}}
		
	}

}
