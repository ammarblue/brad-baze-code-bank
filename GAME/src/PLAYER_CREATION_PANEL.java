import java.awt.BasicStroke;
import javax.swing.text.AttributeSet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.PlainDocument;

public class PLAYER_CREATION_PANEL extends JPanel {
	Timer timer = new Timer();
	int base_stat_todraw = 0;
	int base_stat_cycles = 0;
	int stat_num_cycles = 0;
	Random rand = new Random();
	
	//race buttons
	JButton human;
	JButton klatu;
	JButton nhugg;
	JButton tenacian;
	JButton covert;
	JButton electrosarge;
	JButton mechatronist;
	JButton ranger;
	JLabel covert_lbl;
	JLabel electrosarge_lbl;
	JLabel mechatronist_lbl;
	JLabel ranger_lbl;
	JLabel race_Selected;
	
	JLabel power;
	JLabel agility;
	JLabel precision;
	JLabel resilience;
	JLabel fortitude;
	JLabel stat_description;
	
	int base_power = BASE_STATS.HUMAN_COVERT_BASE_POWER;
	int base_agility =  BASE_STATS.HUMAN_COVERT_BASE_AGILITY;
	int base_precision =  BASE_STATS.HUMAN_COVERT_BASE_PRECISION;
	int base_resilience =  BASE_STATS.HUMAN_COVERT_BASE_RESILIENCE;
	int base_fortitude =  BASE_STATS.HUMAN_COVERT_BASE_FORTITUDE;
	
	String race_Currently_Selected = "Human";
	String class_Currently_Selected = "Covert";
	
	String power_text = "POWER______________"+base_power;
	String agility_text = "AGILITY_____________"+base_agility;
	String precision_text = "PRECISION___________"+base_precision;
	String resilience_text = "RESILIENCE__________"+base_resilience;
	String fortitude_text = "FORTITUDE___________"+base_fortitude;
	
	int stat_num = 21;
	int stat_num_agi = 22;
	int stat_num_prf = 22;
	
	//customization panel
	JLabel customize1;
	JLabel customize2;
	JLabel customize3;
	JLabel customize4;
	
	int temp = 0;
	JButton customize1_LEFT;
	JButton customize2_LEFT;
	JButton customize3_LEFT;
	JButton customize4_LEFT;
	
	JButton customize1_RIGHT;
	JButton customize2_RIGHT;
	JButton customize3_RIGHT;
	JButton customize4_RIGHT;
	
	JLabel name_lbl;
	JTextField name;
	int nameFieldToDraw = 39;
	
	JButton accept;
	JButton back;
	
	
	TimerTask base_stat_animation = new TimerTask(){
		public void run() {
			if(base_stat_cycles < 12){
				if(base_stat_todraw == 3){
					base_stat_todraw = 0;
				}else{
					base_stat_todraw++;
				}
				base_stat_cycles++;
			}	
			
			
			
		}
	};
	public void run_base_stat_animation(){
		try{
			base_stat_todraw = 0;
			base_stat_cycles = 0;
			timer.schedule(base_stat_animation, 0, 100);
		}catch(Exception e1){
			
		}
	}
	TimerTask stat_num_animation = new TimerTask(){
		public void run() {
			if(stat_num >= 21){
			
			}else{
				stat_num++;
			}
			
			if(stat_num_agi >= 23){
				
			}else{
				stat_num_agi++;	
			}
			
			if(stat_num_prf >= 22){
				
			}else{
				stat_num_prf++;	
			}
		
			
		}
	};
	public void run_stat_num_animation(){
		try{
			stat_num = 0;
			stat_num_agi = 0;
			stat_num_prf = 0;
			timer.schedule(stat_num_animation, 0, 60);
		}catch(Exception e1){
			
		}
	}
	
	
	
	public PLAYER_CREATION_PANEL(){
				//PLAYER_CREATION_IMAGES.LOAD(this);
				generate_base_stats();
				RENDER.RENDER(this);
				//STAR_ANIMATION.ANIMATE_STARS();
				setLayout(null);
				setBounds(0, 0, GAME_FRAME.WIDTH , GAME_FRAME.HEIGHT);
				
				//customize1_LEFT
				customize1_LEFT = new JButton();
				customize1_LEFT.setSize(PLAYER_CREATION_IMAGES.PCI[32].getWidth(null),PLAYER_CREATION_IMAGES.PCI[32].getHeight(null));
				customize1_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				customize1_LEFT.setBorder(BorderFactory.createEmptyBorder());
				customize1_LEFT.setContentAreaFilled(false);
				
				customize1_LEFT.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	customize1_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[34]));
				    	temp = 1;
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	customize1_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				    	temp = 0;
				    }
				    public void mousePressed(java.awt.event.MouseEvent evt) {
				    	customize1_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[33]));
				    }
				    public void mouseReleased(java.awt.event.MouseEvent evt) {
				    	if(temp == 1){
				    		customize1_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[34]));
				    	}else{
				    		customize1_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				    	}
				    }
				    
				});
				customize1_LEFT.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	
		            }
		        });
				customize1_LEFT.setLocation(8*GAME_FRAME.SCALE, 21*GAME_FRAME.SCALE);
				add(customize1_LEFT);
				
				//customize2_LEFT
				customize2_LEFT = new JButton();
				customize2_LEFT.setSize(PLAYER_CREATION_IMAGES.PCI[32].getWidth(null),PLAYER_CREATION_IMAGES.PCI[32].getHeight(null));
				customize2_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				customize2_LEFT.setBorder(BorderFactory.createEmptyBorder());
				customize2_LEFT.setContentAreaFilled(false);
				
				customize2_LEFT.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	customize2_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[34]));
				    	temp = 1;
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	customize2_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				    	temp = 0;
				    }
				    public void mousePressed(java.awt.event.MouseEvent evt) {
				    	customize2_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[33]));
				    }
				    public void mouseReleased(java.awt.event.MouseEvent evt) {
				    	if(temp == 1){
				    		customize2_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[34]));
				    	}else{
				    		customize2_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				    	}
				    }
				    
				});
				customize2_LEFT.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	
		            }
		        });
				customize2_LEFT.setLocation(8*GAME_FRAME.SCALE, 37*GAME_FRAME.SCALE);
				add(customize2_LEFT);
				
				//customize3_LEFT
				customize3_LEFT = new JButton();
				customize3_LEFT.setSize(PLAYER_CREATION_IMAGES.PCI[32].getWidth(null),PLAYER_CREATION_IMAGES.PCI[32].getHeight(null));
				customize3_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				customize3_LEFT.setBorder(BorderFactory.createEmptyBorder());
				customize3_LEFT.setContentAreaFilled(false);
				
				customize3_LEFT.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	customize3_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[34]));
				    	temp = 1;
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	customize3_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				    	temp = 0;
				    }
				    public void mousePressed(java.awt.event.MouseEvent evt) {
				    	customize3_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[33]));
				    }
				    public void mouseReleased(java.awt.event.MouseEvent evt) {
				    	if(temp == 1){
				    		customize3_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[34]));
				    	}else{
				    		customize3_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				    	}
				    }
				    
				});
				customize3_LEFT.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	
		            }
		        });
				customize3_LEFT.setLocation(8*GAME_FRAME.SCALE, 53*GAME_FRAME.SCALE);
				add(customize3_LEFT);
				
				//customize4_LEFT
				customize4_LEFT = new JButton();
				customize4_LEFT.setSize(PLAYER_CREATION_IMAGES.PCI[32].getWidth(null),PLAYER_CREATION_IMAGES.PCI[32].getHeight(null));
				customize4_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				customize4_LEFT.setBorder(BorderFactory.createEmptyBorder());
				customize4_LEFT.setContentAreaFilled(false);
				
				customize4_LEFT.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	customize4_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[34]));
				    	temp = 1;
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	customize4_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				    	temp = 0;
				    }
				    public void mousePressed(java.awt.event.MouseEvent evt) {
				    	customize4_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[33]));
				    }
				    public void mouseReleased(java.awt.event.MouseEvent evt) {
				    	if(temp == 1){
				    		customize4_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[34]));
				    	}else{
				    		customize4_LEFT.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[32]));
				    	}
				    }
				    
				});
				customize4_LEFT.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	
		            }
		        });
				customize4_LEFT.setLocation(8*GAME_FRAME.SCALE, 69*GAME_FRAME.SCALE);
				add(customize4_LEFT);
				
				
				//customize Label 1
				customize1 = new JLabel();
				customize1.setSize(PLAYER_CREATION_IMAGES.PCTI[8].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[8].getHeight(null));
				customize1.setLocation(18*GAME_FRAME.SCALE, 23*GAME_FRAME.SCALE);
				customize1.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[8]));
				add(customize1);
				
				//customize Label 2
				customize2 = new JLabel();
				customize2.setSize(PLAYER_CREATION_IMAGES.PCTI[9].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[9].getHeight(null));
				customize2.setLocation(18*GAME_FRAME.SCALE, 39*GAME_FRAME.SCALE);
				customize2.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[9]));
				add(customize2);
				
				//customize Label 3
				customize3 = new JLabel();
				customize3.setSize(PLAYER_CREATION_IMAGES.PCTI[10].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[10].getHeight(null));
				customize3.setLocation(18*GAME_FRAME.SCALE, 55*GAME_FRAME.SCALE);
				customize3.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[10]));
				add(customize3);
				
				//customize Label 4
				customize4 = new JLabel();
				customize4.setSize(PLAYER_CREATION_IMAGES.PCTI[11].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[11].getHeight(null));
				customize4.setLocation(18*GAME_FRAME.SCALE, 71*GAME_FRAME.SCALE);
				customize4.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[11]));
				add(customize4);
				
				//customize1_RIGHT
				customize1_RIGHT = new JButton();
				customize1_RIGHT.setSize(PLAYER_CREATION_IMAGES.PCI[32].getWidth(null),PLAYER_CREATION_IMAGES.PCI[32].getHeight(null));
				customize1_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
				customize1_RIGHT.setBorder(BorderFactory.createEmptyBorder());
				customize1_RIGHT.setContentAreaFilled(false);				
				customize1_RIGHT.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseEntered(java.awt.event.MouseEvent evt) {
						customize1_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[34])));
						temp = 1;
					}
					public void mouseExited(java.awt.event.MouseEvent evt) {
						customize1_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
						temp = 0;
					}
					public void mousePressed(java.awt.event.MouseEvent evt) {
						customize1_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[33])));
					}
					public void mouseReleased(java.awt.event.MouseEvent evt) {
						if(temp == 1){
							customize1_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[34])));
						}else{
							customize1_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
						}
					}
						    
				});
				customize1_RIGHT.addActionListener(new ActionListener() {
							 
				public void actionPerformed(ActionEvent e)
					{
				            	
					}
				});
				customize1_RIGHT.setLocation(66*GAME_FRAME.SCALE, 21*GAME_FRAME.SCALE);
				add(customize1_RIGHT);
				
				//customize2_RIGHT
				customize2_RIGHT = new JButton();
				customize2_RIGHT.setSize(PLAYER_CREATION_IMAGES.PCI[32].getWidth(null),PLAYER_CREATION_IMAGES.PCI[32].getHeight(null));
				customize2_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
				customize2_RIGHT.setBorder(BorderFactory.createEmptyBorder());
				customize2_RIGHT.setContentAreaFilled(false);				
				customize2_RIGHT.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseEntered(java.awt.event.MouseEvent evt) {
						customize2_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[34])));
						temp = 1;
					}
					public void mouseExited(java.awt.event.MouseEvent evt) {
						customize2_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
						temp = 0;
					}
					public void mousePressed(java.awt.event.MouseEvent evt) {
						customize2_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[33])));
					}
					public void mouseReleased(java.awt.event.MouseEvent evt) {
						if(temp == 1){
							customize2_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[34])));
						}else{
							customize2_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
						}
					}
						    
				});
				customize2_RIGHT.addActionListener(new ActionListener() {
							 
				public void actionPerformed(ActionEvent e)
					{
				            	
					}
				});
				customize2_RIGHT.setLocation(66*GAME_FRAME.SCALE, 37*GAME_FRAME.SCALE);
				add(customize2_RIGHT);
				
				//customize3_RIGHT
				customize3_RIGHT = new JButton();
				customize3_RIGHT.setSize(PLAYER_CREATION_IMAGES.PCI[32].getWidth(null),PLAYER_CREATION_IMAGES.PCI[32].getHeight(null));
				customize3_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
				customize3_RIGHT.setBorder(BorderFactory.createEmptyBorder());
				customize3_RIGHT.setContentAreaFilled(false);				
				customize3_RIGHT.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseEntered(java.awt.event.MouseEvent evt) {
						customize3_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[34])));
						temp = 1;
					}
					public void mouseExited(java.awt.event.MouseEvent evt) {
						customize3_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
						temp = 0;
					}
					public void mousePressed(java.awt.event.MouseEvent evt) {
						customize3_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[33])));
					}
					public void mouseReleased(java.awt.event.MouseEvent evt) {
						if(temp == 1){
							customize3_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[34])));
						}else{
							customize3_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
						}
					}
						    
				});
				customize3_RIGHT.addActionListener(new ActionListener() {
							 
				public void actionPerformed(ActionEvent e)
					{
				            	
					}
				});
				customize3_RIGHT.setLocation(66*GAME_FRAME.SCALE, 53*GAME_FRAME.SCALE);
				add(customize3_RIGHT);
				
				//customize4_RIGHT
				customize4_RIGHT = new JButton();
				customize4_RIGHT.setSize(PLAYER_CREATION_IMAGES.PCI[32].getWidth(null),PLAYER_CREATION_IMAGES.PCI[32].getHeight(null));
				customize4_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
				customize4_RIGHT.setBorder(BorderFactory.createEmptyBorder());
				customize4_RIGHT.setContentAreaFilled(false);				
				customize4_RIGHT.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseEntered(java.awt.event.MouseEvent evt) {
						customize4_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[34])));
						temp = 1;
					}
					public void mouseExited(java.awt.event.MouseEvent evt) {
						customize4_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
						temp = 0;
					}
					public void mousePressed(java.awt.event.MouseEvent evt) {
						customize4_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[33])));
					}
					public void mouseReleased(java.awt.event.MouseEvent evt) {
						if(temp == 1){
							customize4_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[34])));
						}else{
							customize4_RIGHT.setIcon(new ImageIcon(MANIPULATE_IMAGE.FLIP(PLAYER_CREATION_IMAGES.PCI[32])));
						}
					}
						    
				});
				customize4_RIGHT.addActionListener(new ActionListener() {
							 
				public void actionPerformed(ActionEvent e)
					{
				            	
					}
				});
				customize4_RIGHT.setLocation(66*GAME_FRAME.SCALE, 69*GAME_FRAME.SCALE);
				add(customize4_RIGHT);
				
				
				
				
				
				//power
				power = new JLabel();
				power.setSize(66*GAME_FRAME.SCALE,6*GAME_FRAME.SCALE);
				power.setFont(new Font("PF Tempesta Seven",Font.PLAIN,(int)GAME_FRAME.SCALE*10/3));
				power.setForeground(new Color(204,102,0));
				power.setLocation(114*GAME_FRAME.SCALE, 35*GAME_FRAME.SCALE);
				power.setText("POWER.................."+base_power);
				power.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> DAMAGE! DAMAGE! <br>DAMAGE!</html>");
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> Mouse-over stats<br>for info...</html>");
				    }
				});
				add(power);
				
				//agility
				agility = new JLabel();
				agility.setSize(69*GAME_FRAME.SCALE,6*GAME_FRAME.SCALE);
				agility.setFont(new Font("PF Tempesta Seven",Font.PLAIN,(int)GAME_FRAME.SCALE*10/3));
				agility.setForeground(new Color(0,102,0));
				agility.setLocation(114*GAME_FRAME.SCALE, 30*GAME_FRAME.SCALE);
				agility.setText("AGILITY.................."+base_agility);
				agility.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> Movement speed and <br>attack rate.</html>");
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> Mouse-over stats<br>for info...</html>");
				    }
				});
				add(agility);
				
				//precision
				precision = new JLabel();
				//precision.setHorizontalAlignment(SwingConstants.CENTER);
				precision.setSize(66*GAME_FRAME.SCALE,6*GAME_FRAME.SCALE);
				precision.setFont(new Font("PF Tempesta Seven",Font.PLAIN,(int)GAME_FRAME.SCALE*10/3));
				precision.setForeground(new Color(153,51,255));
				precision.setLocation(114*GAME_FRAME.SCALE, 40*GAME_FRAME.SCALE);
				precision.setText("PRECISION............."+base_precision);
				precision.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> CRIT! and resource <br>regeneration.</html>");
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> Mouse-over stats<br>for info...</html>");
				    }
				});
				add(precision);
				
				//resilience
				resilience = new JLabel();
				resilience.setSize(66*GAME_FRAME.SCALE,6*GAME_FRAME.SCALE);
				resilience.setFont(new Font("PF Tempesta Seven",Font.PLAIN,(int)GAME_FRAME.SCALE*10/3));
				resilience.setForeground(new Color(255,255,51));
				resilience.setLocation(114*GAME_FRAME.SCALE, 45*GAME_FRAME.SCALE);
				resilience.setText("RESILIENCE..........."+base_resilience);
				resilience.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> Damage mitigation... <br>*absorb*</html>");
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> Mouse-over stats<br>for info...</html>");
				    }
				});
				add(resilience);
				
				//fortitude
				fortitude = new JLabel();
				fortitude.setSize(66*GAME_FRAME.SCALE,6*GAME_FRAME.SCALE);
				fortitude.setFont(new Font("PF Tempesta Seven",Font.PLAIN,(int)GAME_FRAME.SCALE*10/3));
				fortitude.setForeground(new Color(255,30,30));
				fortitude.setLocation(114*GAME_FRAME.SCALE, 50*GAME_FRAME.SCALE);
				fortitude.setText("FORTITUDE............"+base_fortitude);
				fortitude.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> Health!<br> </html>");
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	stat_description.setText("<html><center> Mouse-over stats<br>for info...</html>");
				    }
				});
				add(fortitude);
				
				//stat_description
				stat_description = new JLabel();
				stat_description.setSize(PLAYER_CREATION_IMAGES.PCTI[28].getWidth(null),12*GAME_FRAME.SCALE);
				stat_description.setHorizontalAlignment(SwingConstants.CENTER);
				stat_description.setFont(new Font("PF Tempesta Seven",Font.PLAIN,(int)GAME_FRAME.SCALE*10/3));
				stat_description.setForeground(Color.WHITE);
				//stat_description.setLocation(126*GAME_FRAME.SCALE, 68*GAME_FRAME.SCALE);
				stat_description.setLocation(112*GAME_FRAME.SCALE, 68*GAME_FRAME.SCALE);
				stat_description.setText("<html><center> <center>Mouse-over stats<br>for info...</html>");
				add(stat_description);
				
				
				
				//human
				human = new JButton();
				human.setSize(PLAYER_CREATION_IMAGES.PCI[12].getWidth(null),PLAYER_CREATION_IMAGES.PCI[12].getHeight(null));
				human.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[12]));
				human.setBorder(BorderFactory.createEmptyBorder());
				human.setContentAreaFilled(false);
				human.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	if(race_Currently_Selected == "Human"){
				    		
				    	}else{
				    		human.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[13]));
				    	}
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	if(race_Currently_Selected == "Human"){
				    		
				    	}else{
				    		human.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[12]));
				    	}
				    }
				});
				human.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	race_Currently_Selected = "Human";
		            	generate_base_stats();
		            	run_base_stat_animation();
		            	run_stat_num_animation();
		            	
		            	highlight_Selected_Race();
		            	enable_available_classes();
		            	display_custom_options();
		            	
		            }
		        });
				human.setLocation(213*GAME_FRAME.SCALE, 26*GAME_FRAME.SCALE);
				add(human);
				
				//klatu
				klatu = new JButton();
				klatu.setSize(PLAYER_CREATION_IMAGES.PCI[15].getWidth(null),PLAYER_CREATION_IMAGES.PCI[15].getHeight(null));
				klatu.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[15]));
				klatu.setBorder(BorderFactory.createEmptyBorder());
				klatu.setContentAreaFilled(false);
				klatu.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	if(race_Currently_Selected == "Kla'tu"){
				    		
				    	}else{
				    		klatu.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[16]));
				    	}
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	if(race_Currently_Selected == "Kla'tu"){
				    		
				    	}else{
				    		klatu.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[15]));
				    	}
				    }
				});
				klatu.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	race_Currently_Selected = "Kla'tu";
		            	generate_base_stats();
		            	run_base_stat_animation();
		            	run_stat_num_animation();
		            	
		            	highlight_Selected_Race();
		            	enable_available_classes();
		            	display_custom_options();
		            }
		        });
				klatu.setLocation(238*GAME_FRAME.SCALE, 26*GAME_FRAME.SCALE);
				add(klatu);
				
				//nhugg
				nhugg = new JButton();
				nhugg.setSize(PLAYER_CREATION_IMAGES.PCI[18].getWidth(null),PLAYER_CREATION_IMAGES.PCI[18].getHeight(null));
				nhugg.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[18]));
				nhugg.setBorder(BorderFactory.createEmptyBorder());
				nhugg.setContentAreaFilled(false);
				nhugg.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	if(race_Currently_Selected == "Nhugg"){
				    		
				    	}else{
				    		nhugg.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[19]));
				    	}
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	if(race_Currently_Selected == "Nhugg"){
				    		
				    	}else{
				    		nhugg.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[18]));
				    	}
				    }
				});
				nhugg.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	race_Currently_Selected = "Nhugg";
		            	generate_base_stats();
		            	run_base_stat_animation();
		            	run_stat_num_animation();
		            	
		            	highlight_Selected_Race();
		            	enable_available_classes();
		            	display_custom_options();
		            }
		        });
				nhugg.setLocation(213*GAME_FRAME.SCALE, 51*GAME_FRAME.SCALE);
				add(nhugg);
				
				//tenacian
				tenacian = new JButton();
				tenacian.setSize(PLAYER_CREATION_IMAGES.PCI[21].getWidth(null),PLAYER_CREATION_IMAGES.PCI[21].getHeight(null));
				tenacian.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[21]));
				tenacian.setBorder(BorderFactory.createEmptyBorder());
				tenacian.setContentAreaFilled(false);
				tenacian.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	if(race_Currently_Selected == "Tenacian"){
				    		
				    	}else{
				    		tenacian.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[22]));
				    	}
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	if(race_Currently_Selected == "Tenacian"){
				    		
				    	}else{
				    		tenacian.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[21]));
				    	}
				    }
				});
				tenacian.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	race_Currently_Selected = "Tenacian";
		            	generate_base_stats();
		            	run_base_stat_animation();
		            	run_stat_num_animation();
		            	
		            	highlight_Selected_Race();
		            	enable_available_classes();
		            	display_custom_options();
		            }
		        });
				tenacian.setLocation(238*GAME_FRAME.SCALE, 51*GAME_FRAME.SCALE);
				add(tenacian);
				
				race_Selected = new JLabel();
				race_Selected.setVisible(true);
				//race_Selected.setForeground(Color.WHITE);
				//race_Selected.setFont(new Font("PF Tempesta Seven",Font.PLAIN, 2*GAME_FRAME.SCALE+1));
				add(race_Selected,2,0);
				
				//COVERT
				covert = new JButton();
				covert.setSize(PLAYER_CREATION_IMAGES.PCI[5].getWidth(null),PLAYER_CREATION_IMAGES.PCI[5].getHeight(null));
				
				covert.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
				covert.setDisabledIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[35]));
				covert.setBorder(BorderFactory.createEmptyBorder());
				covert.setContentAreaFilled(false);
				covert.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Covert"){
				    		
				    	}else{
				    		covert.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[6]));
				    	}
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Covert"){
				    		
				    	}else{
				    		covert.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
				    	}
				    }
				    public void mousePressed(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Covert"){
				    		
				    	}else{
				    		covert.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[7]));
				    	}
				    }
				    
				});
				covert.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	class_Currently_Selected = "Covert";
		            	generate_base_stats();
		            	run_base_stat_animation();
		            	run_stat_num_animation();
		            	
		            	highlight_Selected_Class();
		            }
		        });
				covert.setLocation(207*GAME_FRAME.SCALE, 78*GAME_FRAME.SCALE);
				add(covert);
				
				covert_lbl = new JLabel();
				covert_lbl.setSize(PLAYER_CREATION_IMAGES.PCTI[17].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[17].getHeight(null));
				covert_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[17]));
				covert_lbl.setDisabledIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[18]));
				covert_lbl.setLocation(210*GAME_FRAME.SCALE, 82*GAME_FRAME.SCALE);
				add(covert_lbl,2,0);
				
				//electrosarge
				electrosarge = new JButton();
				electrosarge.setSize(PLAYER_CREATION_IMAGES.PCI[5].getWidth(null),PLAYER_CREATION_IMAGES.PCI[5].getHeight(null));
				electrosarge.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
				electrosarge.setDisabledIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[35]));
				electrosarge.setBorder(BorderFactory.createEmptyBorder());
				electrosarge.setContentAreaFilled(false);
				electrosarge.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Electrosarge"){
				    		
				    	}else{
				    		electrosarge.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[6]));
				    	}
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Electrosarge"){
				    		
				    	}else{
				    		electrosarge.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
				    	}
				    }
				    public void mousePressed(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Electrosarge"){
				    		
				    	}else{
				    		electrosarge.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[7]));
				    	}
				    }
				    
				});
				electrosarge.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	class_Currently_Selected = "Electrosarge";
		            	generate_base_stats();
		            	run_base_stat_animation();
		            	run_stat_num_animation();
		            	
		            	highlight_Selected_Class();
		            }
		        });
				electrosarge.setLocation(207*GAME_FRAME.SCALE, 90*GAME_FRAME.SCALE);
				add(electrosarge);
				
				electrosarge_lbl = new JLabel();
				electrosarge_lbl.setSize(PLAYER_CREATION_IMAGES.PCTI[20].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[20].getHeight(null));
				electrosarge_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[20]));
				electrosarge_lbl.setDisabledIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[21]));
				electrosarge_lbl.setLocation(210*GAME_FRAME.SCALE, 94 *GAME_FRAME.SCALE);
				add(electrosarge_lbl,2,0);
				
				//mechatronist
				mechatronist = new JButton();
				mechatronist.setSize(PLAYER_CREATION_IMAGES.PCI[5].getWidth(null),PLAYER_CREATION_IMAGES.PCI[5].getHeight(null));
				mechatronist.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
				mechatronist.setDisabledIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[35]));
				mechatronist.setBorder(BorderFactory.createEmptyBorder());
				mechatronist.setContentAreaFilled(false);
				mechatronist.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Mechatronist"){
				    		
				    	}else{
				    		mechatronist.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[6]));
				    	}
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Mechatronist"){
				    		
				    	}else{
				    		mechatronist.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
				    	}
				    }
				    public void mousePressed(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Mechatronist"){
				    		
				    	}else{
				    		mechatronist.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[7]));
				    	}
				    }
				    
				});
				mechatronist.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	class_Currently_Selected = "Mechatronist";
		            	generate_base_stats();
		            	run_base_stat_animation();
		            	run_stat_num_animation();
		            	
		            	highlight_Selected_Class();
		            	
		            }
		        });
				mechatronist.setLocation(207*GAME_FRAME.SCALE, 102*GAME_FRAME.SCALE);
				add(mechatronist);
				
				mechatronist_lbl = new JLabel();
				mechatronist_lbl.setSize(PLAYER_CREATION_IMAGES.PCTI[23].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[23].getHeight(null));
				mechatronist_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[23]));
				mechatronist_lbl.setDisabledIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[24]));
				mechatronist_lbl.setLocation(210*GAME_FRAME.SCALE, 106*GAME_FRAME.SCALE);
				add(mechatronist_lbl,2,0);
				
				//ranger
				ranger = new JButton();
				ranger.setSize(PLAYER_CREATION_IMAGES.PCI[5].getWidth(null),PLAYER_CREATION_IMAGES.PCI[5].getHeight(null));
				ranger.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
				ranger.setDisabledIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[35]));
				ranger.setBorder(BorderFactory.createEmptyBorder());
				ranger.setContentAreaFilled(false);
				ranger.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Ranger"){
				    		
				    	}else{
				    		ranger.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[6]));
				    	}
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Ranger"){
				    		
				    	}else{
				    		ranger.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
				    	}
				    }
				    public void mousePressed(java.awt.event.MouseEvent evt) {
				    	if(class_Currently_Selected == "Ranger"){
				    		
				    	}else{
				    		ranger.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[7]));
				    	}
				    }
				    
				});
				ranger.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {	
		            	class_Currently_Selected = "Ranger";
		            	generate_base_stats();
		            	run_base_stat_animation();
		            	run_stat_num_animation();
		            	
		            	highlight_Selected_Class();
		            	
		            }
		        });
				ranger.setLocation(207*GAME_FRAME.SCALE, 114*GAME_FRAME.SCALE);
				add(ranger);
				
				ranger_lbl = new JLabel();
				ranger_lbl.setSize(PLAYER_CREATION_IMAGES.PCTI[26].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[26].getHeight(null));
				ranger_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[26]));
				ranger_lbl.setDisabledIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[27]));
				ranger_lbl.setLocation(210*GAME_FRAME.SCALE, 118*GAME_FRAME.SCALE);
				add(ranger_lbl,2,0);
				
				//accept
				accept = new JButton();
				accept.setSize(PLAYER_CREATION_IMAGES.PCI[36].getWidth(null),PLAYER_CREATION_IMAGES.PCI[36].getHeight(null));
				accept.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[36]));
				accept.setDisabledIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[36]));
				accept.setBorder(BorderFactory.createEmptyBorder());
				accept.setContentAreaFilled(false);
				accept.addMouseListener(new java.awt.event.MouseAdapter() {
				    public void mouseEntered(java.awt.event.MouseEvent evt) {
				    	accept.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[37]));
				    	temp = 1;
				    }
				    public void mouseExited(java.awt.event.MouseEvent evt) {
				    	accept.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[36]));
				    	temp = 0;
				    }
				    public void mousePressed(java.awt.event.MouseEvent evt) {
				    	accept.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[38]));
				    }
				    public void mouseReleased(java.awt.event.MouseEvent evt) {
				    	if(temp == 1){
				    		accept.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[37]));
				    	}else{
				    		accept.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[36]));
				    	}
				    }
				    
				});
				accept.addActionListener(new ActionListener() {
					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	setEnabled(false);
		            	CHARACTER newChar = new CHARACTER(name.getText(),race_Currently_Selected,class_Currently_Selected);
		            	CHARACTER_LIST.characters.add(newChar);
		            	TRANSITION1.ANIMATE_TRANSITIION1(new WELCOME_PANEL());
		            	WELCOME_PANEL.moveToCLPanel();
		            }
		        });
				accept.setLocation((GAME_FRAME.WIDTH/2)- (PLAYER_CREATION_IMAGES.PCI[36].getWidth(null)/2), 150*GAME_FRAME.SCALE);
				add(accept);
				
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
		            	setEnabled(false);
		            	TRANSITION1.ANIMATE_TRANSITIION1(new WELCOME_PANEL());
		            	WELCOME_PANEL.moveToCLPanel();
		            }
		        });
				back.setLocation((GAME_FRAME.WIDTH/2)- (CHARACTER_LIST_IMAGES.CLI[1].getWidth(null)/2)-98*GAME_FRAME.SCALE, 150*GAME_FRAME.SCALE);
				add(back);
				
				//name label
				name_lbl = new JLabel();
				name_lbl.setSize(PLAYER_CREATION_IMAGES.PCTI[29].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[29].getHeight(null));
				name_lbl.setLocation(226*GAME_FRAME.SCALE+1, 130*GAME_FRAME.SCALE);
				name_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[29]));
				add(name_lbl);
				
				//name textfield
				name = new JTextField(){
					@Override 
					public void setBorder(Border border) {
			        // No border
					}
				};
				class FixedSizeDocument extends PlainDocument
				{
				   private int max = 10;
				   public FixedSizeDocument(int max)
				   {
				        this.max = max;
				   } 
				   @Override
				   public void insertString(int offs, String str, AttributeSet a)
				      throws BadLocationException
				   {
					   if (getLength()+str.length()>max)
				      {
				    	  str = str.substring(0, max - getLength());
				      }
				      super.insertString(offs, str, a);
				   }
				}
				name.setDocument(new FixedSizeDocument(10));
				name.setCaretColor(new Color(0,0,0,0));
				name.putClientProperty("caretAspectRatio", 0);
				name.setHorizontalAlignment(JTextField.CENTER);
				name.setForeground(Color.WHITE);
				name.setFont(new Font("PF Tempesta Seven", Font.PLAIN, (3*GAME_FRAME.SCALE)+1));
				name.setSize(PLAYER_CREATION_IMAGES.PCI[40].getWidth(null)-(GAME_FRAME.SCALE*4),PLAYER_CREATION_IMAGES.PCI[40].getHeight(null)-(GAME_FRAME.SCALE*1));
				name.setLocation(213*GAME_FRAME.SCALE, 136*GAME_FRAME.SCALE+1);
				name.setOpaque(false);
				name.setHighlighter(null);
				name.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent arg0) {
						// TODO Auto-generated method stub
						nameFieldToDraw = 40;
						name.setForeground(Color.WHITE);
					}

					@Override
					public void focusLost(FocusEvent arg0) {
						// TODO Auto-generated method stub
						nameFieldToDraw = 39;
						name.setForeground(new Color(120, 120, 120, 185));
					}
		        });
				add(name);
				
				
				
				
				highlight_Selected_Race();
				highlight_Selected_Class();
				enable_available_classes();
				setFocusable(true);
				setVisible(true);
				
			}
			public void setEnabled(boolean enabled) {
				JLabel lbl = new JLabel();
				transitionDisable();
			    super.setEnabled(enabled);
			    for (Component component : getComponents()){
			    	if(component.getClass() == lbl.getClass()){	
			    	}else{
			    		component.setEnabled(enabled);
			    	}
			    }
	}
	
	public void highlight_Selected_Race(){
		if(race_Currently_Selected == "Human"){
			human.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[14]));
			klatu.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[15]));
			nhugg.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[18]));
			tenacian.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[21]));
			race_Selected.setSize(PLAYER_CREATION_IMAGES.PCTI[0].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[0].getHeight(null));
			race_Selected.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[0]));
			//race_Selected.setText("Human");
			race_Selected.setLocation(217*GAME_FRAME.SCALE, 42*GAME_FRAME.SCALE);
			
		}else if(race_Currently_Selected == "Kla'tu"){
			human.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[12]));
			klatu.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[17]));
			nhugg.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[18]));
			tenacian.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[21]));
			race_Selected.setSize(PLAYER_CREATION_IMAGES.PCTI[1].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[1].getHeight(null));
			race_Selected.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[1]));
			//race_Selected.setText("Kla'tu");
			race_Selected.setLocation(243*GAME_FRAME.SCALE, 42*GAME_FRAME.SCALE);
		}else if(race_Currently_Selected == "Nhugg"){
			human.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[12]));
			klatu.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[15]));
			nhugg.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[20]));
			tenacian.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[21]));
			race_Selected.setSize(PLAYER_CREATION_IMAGES.PCTI[2].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[2].getHeight(null));
			race_Selected.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[2]));
			//race_Selected.setText("Nhugg");
			race_Selected.setLocation(218*GAME_FRAME.SCALE, 67*GAME_FRAME.SCALE);
		}else if(race_Currently_Selected == "Tenacian"){
			human.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[12]));
			klatu.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[15]));
			nhugg.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[18]));
			tenacian.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[23]));
			race_Selected.setSize(PLAYER_CREATION_IMAGES.PCTI[3].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[3].getHeight(null));
			race_Selected.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[3]));
			//race_Selected.setText("Tenacian");
			race_Selected.setLocation(239*GAME_FRAME.SCALE, 67*GAME_FRAME.SCALE);
		}
	}
	public void highlight_Selected_Class(){
		if(class_Currently_Selected == "Covert"){
			covert.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[8]));
			electrosarge.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			mechatronist.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			ranger.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			covert_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[16]));
			electrosarge_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[20]));
			mechatronist_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[23]));
			ranger_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[26]));
			
		}else if(class_Currently_Selected == "Electrosarge"){
			covert.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			electrosarge.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[9]));
			mechatronist.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			ranger.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			
			covert_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[17]));
			electrosarge_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[19]));
			mechatronist_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[23]));
			ranger_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[26]));
			
		}else if(class_Currently_Selected == "Mechatronist"){
			covert.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			electrosarge.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			mechatronist.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[10]));
			ranger.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			
			covert_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[17]));
			electrosarge_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[20]));
			mechatronist_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[22]));
			ranger_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[26]));
			
		}else if(class_Currently_Selected == "Ranger"){
			covert.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			electrosarge.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			mechatronist.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[5]));
			ranger.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCI[11]));
			
			covert_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[17]));
			electrosarge_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[20]));
			mechatronist_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[23]));
			ranger_lbl.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[25]));	
		}
		
	}
	public void enable_available_classes(){
		
		if(race_Currently_Selected == "Human"){	
			covert.setEnabled(true);
			electrosarge.setEnabled(true);
			mechatronist.setEnabled(true);
			ranger.setEnabled(true);
			covert_lbl.setEnabled(true);
			electrosarge_lbl.setEnabled(true);
			mechatronist_lbl.setEnabled(true);
			ranger_lbl.setEnabled(true);
			highlight_Selected_Class();
		}else if(race_Currently_Selected == "Kla'tu"){
			covert.setEnabled(true);
			electrosarge.setEnabled(true);
			mechatronist.setEnabled(false);
			ranger.setEnabled(true);
			covert_lbl.setEnabled(true);
			electrosarge_lbl.setEnabled(true);
			mechatronist_lbl.setEnabled(false);
			ranger_lbl.setEnabled(true);
			if(class_Currently_Selected == "Mechatronist"){
				class_Currently_Selected = "Covert";
			}
			
			highlight_Selected_Class();
		}else if(race_Currently_Selected == "Nhugg"){
			covert.setEnabled(true);
			electrosarge.setEnabled(false);
			mechatronist.setEnabled(true);
			ranger.setEnabled(false);
			covert_lbl.setEnabled(true);
			electrosarge_lbl.setEnabled(false);
			mechatronist_lbl.setEnabled(true);
			ranger_lbl.setEnabled(false);
			if(class_Currently_Selected == "Electrosarge" ||class_Currently_Selected == "Ranger"){
				class_Currently_Selected = "Covert";
			}
			highlight_Selected_Class();
		}else if(race_Currently_Selected == "Tenacian"){
			covert.setEnabled(false);
			electrosarge.setEnabled(true);
			mechatronist.setEnabled(false);
			ranger.setEnabled(true);
			covert_lbl.setEnabled(false);
			electrosarge_lbl.setEnabled(true);
			mechatronist_lbl.setEnabled(false);
			ranger_lbl.setEnabled(true);
			if(class_Currently_Selected == "Mechatronist" || class_Currently_Selected == "Covert"){
				class_Currently_Selected = "Electrosarge";
			}
			highlight_Selected_Class();
		}
		
	}
	public void generate_base_stats(){
		if(race_Currently_Selected == "Human"){	
			if(class_Currently_Selected == "Covert"){
				base_power = BASE_STATS.HUMAN_COVERT_BASE_POWER;
				base_agility =  BASE_STATS.HUMAN_COVERT_BASE_AGILITY;
				base_precision =  BASE_STATS.HUMAN_COVERT_BASE_PRECISION;
				base_resilience =  BASE_STATS.HUMAN_COVERT_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.HUMAN_COVERT_BASE_FORTITUDE;
			}else if(class_Currently_Selected == "Electrosarge"){
				base_power = BASE_STATS.HUMAN_ELECTROSARGE_BASE_POWER;
				base_agility =  BASE_STATS.HUMAN_ELECTROSARGE_BASE_AGILITY;
				base_precision =  BASE_STATS.HUMAN_ELECTROSARGE_BASE_PRECISION;
				base_resilience =  BASE_STATS.HUMAN_ELECTROSARGE_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.HUMAN_ELECTROSARGE_BASE_FORTITUDE;
			}else if(class_Currently_Selected == "Mechatronist"){
				base_power = BASE_STATS.HUMAN_MECHATRONIST_BASE_POWER;
				base_agility =  BASE_STATS.HUMAN_MECHATRONIST_BASE_AGILITY;
				base_precision =  BASE_STATS.HUMAN_MECHATRONIST_BASE_PRECISION;
				base_resilience =  BASE_STATS.HUMAN_MECHATRONIST_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.HUMAN_MECHATRONIST_BASE_FORTITUDE;
			}else if(class_Currently_Selected == "Ranger"){
				base_power = BASE_STATS.HUMAN_RANGER_BASE_POWER;
				base_agility =  BASE_STATS.HUMAN_RANGER_BASE_AGILITY;
				base_precision =  BASE_STATS.HUMAN_RANGER_BASE_PRECISION;
				base_resilience =  BASE_STATS.HUMAN_RANGER_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.HUMAN_RANGER_BASE_FORTITUDE;
			}
		}else if(race_Currently_Selected == "Kla'tu"){
			if(class_Currently_Selected == "Covert"){
				base_power = BASE_STATS.KLATU_COVERT_BASE_POWER;
				base_agility =  BASE_STATS.KLATU_COVERT_BASE_AGILITY;
				base_precision =  BASE_STATS.KLATU_COVERT_BASE_PRECISION;
				base_resilience =  BASE_STATS.KLATU_COVERT_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.KLATU_COVERT_BASE_FORTITUDE;
			}else if(class_Currently_Selected == "Electrosarge"){
				base_power = BASE_STATS.KLATU_ELECTROSARGE_BASE_POWER;
				base_agility =  BASE_STATS.KLATU_ELECTROSARGE_BASE_AGILITY;
				base_precision =  BASE_STATS.KLATU_ELECTROSARGE_BASE_PRECISION;
				base_resilience =  BASE_STATS.KLATU_ELECTROSARGE_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.KLATU_ELECTROSARGE_BASE_FORTITUDE;
			}else if(class_Currently_Selected == "Ranger"){
				base_power = BASE_STATS.KLATU_RANGER_BASE_POWER;
				base_agility =  BASE_STATS.KLATU_RANGER_BASE_AGILITY;
				base_precision =  BASE_STATS.KLATU_RANGER_BASE_PRECISION;
				base_resilience =  BASE_STATS.KLATU_RANGER_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.KLATU_RANGER_BASE_FORTITUDE;
			}
		}else if(race_Currently_Selected == "Nhugg"){
			if(class_Currently_Selected == "Covert"){
				base_power = BASE_STATS.NHUGG_COVERT_BASE_POWER;
				base_agility =  BASE_STATS.NHUGG_COVERT_BASE_AGILITY;
				base_precision =  BASE_STATS.NHUGG_COVERT_BASE_PRECISION;
				base_resilience =  BASE_STATS.NHUGG_COVERT_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.NHUGG_COVERT_BASE_FORTITUDE;
			}else if(class_Currently_Selected == "Mechatronist"){
				base_power = BASE_STATS.NHUGG_MECHATRONIST_BASE_POWER;
				base_agility =  BASE_STATS.NHUGG_MECHATRONIST_BASE_AGILITY;
				base_precision =  BASE_STATS.NHUGG_MECHATRONIST_BASE_PRECISION;
				base_resilience =  BASE_STATS.NHUGG_MECHATRONIST_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.NHUGG_MECHATRONIST_BASE_FORTITUDE;
			}
		}else if(race_Currently_Selected == "Tenacian"){
			if(class_Currently_Selected == "Electrosarge"){
				base_power = BASE_STATS.TENACIAN_ELECTROSARGE_BASE_POWER;
				base_agility =  BASE_STATS.TENACIAN_ELECTROSARGE_BASE_AGILITY;
				base_precision =  BASE_STATS.TENACIAN_ELECTROSARGE_BASE_PRECISION;
				base_resilience =  BASE_STATS.TENACIAN_ELECTROSARGE_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.TENACIAN_ELECTROSARGE_BASE_FORTITUDE;
			}else if(class_Currently_Selected == "Ranger"){
				base_power = BASE_STATS.TENACIAN_RANGER_BASE_POWER;
				base_agility =  BASE_STATS.TENACIAN_RANGER_BASE_AGILITY;
				base_precision =  BASE_STATS.TENACIAN_RANGER_BASE_PRECISION;
				base_resilience =  BASE_STATS.TENACIAN_RANGER_BASE_RESILIENCE;
				base_fortitude =  BASE_STATS.TENACIAN_RANGER_BASE_FORTITUDE;
			}
		}
		power_text = "POWER______________"+base_power;
		agility_text = "AGILITY_____________"+base_agility;
		precision_text = "PRECISION___________"+base_precision;
		resilience_text = "RESILIENCE__________"+base_resilience;
		fortitude_text = "FORTITUDE___________"+base_fortitude;
	}
	public void transitionDisable(){
		highlight_Selected_Class();
		name.setDisabledTextColor(name.getForeground());
		customize1_LEFT.setDisabledIcon(customize1_LEFT.getIcon());
		customize2_LEFT.setDisabledIcon(customize2_LEFT.getIcon());
		customize3_LEFT.setDisabledIcon(customize3_LEFT.getIcon());
		customize4_LEFT.setDisabledIcon(customize4_LEFT.getIcon());
		customize1.setDisabledIcon(customize1.getIcon());
		customize2.setDisabledIcon(customize2.getIcon());
		customize3.setDisabledIcon(customize3.getIcon());
		customize4.setDisabledIcon(customize4.getIcon());
		customize1_RIGHT.setDisabledIcon(customize1_RIGHT.getIcon());
		customize2_RIGHT.setDisabledIcon(customize2_RIGHT.getIcon());
		customize3_RIGHT.setDisabledIcon(customize3_RIGHT.getIcon());
		customize4_RIGHT.setDisabledIcon(customize4_RIGHT.getIcon());
		race_Selected.setDisabledIcon(race_Selected.getIcon());
		human.setDisabledIcon(human.getIcon());
		klatu.setDisabledIcon(klatu.getIcon());
		nhugg.setDisabledIcon(nhugg.getIcon());
		tenacian.setDisabledIcon(tenacian.getIcon());
		if(covert.isEnabled()){
			covert.setDisabledIcon(covert.getIcon());
			covert_lbl.setDisabledIcon(covert_lbl.getIcon());
		}else{
			covert.setDisabledIcon(covert.getDisabledIcon());
			covert_lbl.setDisabledIcon(covert_lbl.getDisabledIcon());
		}
		if(electrosarge.isEnabled()){
			electrosarge.setDisabledIcon(electrosarge.getIcon());
			electrosarge_lbl.setDisabledIcon(electrosarge_lbl.getIcon());
		}else{
			electrosarge.setDisabledIcon(electrosarge.getDisabledIcon());
			electrosarge_lbl.setDisabledIcon(electrosarge_lbl.getDisabledIcon());
		}
		if(mechatronist.isEnabled()){
			mechatronist.setDisabledIcon(mechatronist.getIcon());
			mechatronist_lbl.setDisabledIcon(mechatronist_lbl.getIcon());
		}else{
			mechatronist.setDisabledIcon(mechatronist.getDisabledIcon());
			mechatronist_lbl.setDisabledIcon(mechatronist_lbl.getDisabledIcon());
		}
		if(ranger.isEnabled()){
			ranger.setDisabledIcon(ranger.getIcon());
			ranger_lbl.setDisabledIcon(ranger_lbl.getIcon());
		}else{
			ranger.setDisabledIcon(ranger.getDisabledIcon());
			ranger_lbl.setDisabledIcon(ranger_lbl.getDisabledIcon());
		}
		
		
		
		
		name_lbl.setDisabledIcon(name_lbl.getIcon());
	}
	
	public void display_custom_options(){
		if(race_Currently_Selected == "Human"){	
			customize2.setSize(PLAYER_CREATION_IMAGES.PCTI[9].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[9].getHeight(null));
			customize2.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[9]));
			customize3.setSize(PLAYER_CREATION_IMAGES.PCTI[10].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[10].getHeight(null));
			customize3.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[10]));
			customize4.setVisible(true);
			customize4_LEFT.setVisible(true);
			customize4_RIGHT.setVisible(true);
			customize4.setSize(PLAYER_CREATION_IMAGES.PCTI[11].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[11].getHeight(null));
			customize4.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[11]));
		}else if(race_Currently_Selected == "Kla'tu"){
			customize2.setSize(PLAYER_CREATION_IMAGES.PCTI[12].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[12].getHeight(null));
			customize2.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[12]));
			customize3.setSize(PLAYER_CREATION_IMAGES.PCTI[13].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[13].getHeight(null));
			customize3.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[13]));
			customize4.setVisible(false);
			customize4_LEFT.setVisible(false);
			customize4_RIGHT.setVisible(false);
		}else if(race_Currently_Selected == "Nhugg"){
			customize2.setSize(PLAYER_CREATION_IMAGES.PCTI[9].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[9].getHeight(null));
			customize2.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[9]));
			customize3.setSize(PLAYER_CREATION_IMAGES.PCTI[10].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[10].getHeight(null));
			customize3.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[10]));
			customize4.setVisible(true);
			customize4_LEFT.setVisible(true);
			customize4_RIGHT.setVisible(true);
			customize4.setSize(PLAYER_CREATION_IMAGES.PCTI[11].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[11].getHeight(null));
			customize4.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[11]));
		}else if(race_Currently_Selected == "Tenacian"){
			customize2.setSize(PLAYER_CREATION_IMAGES.PCTI[14].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[14].getHeight(null));
			customize2.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[14]));
			customize3.setSize(PLAYER_CREATION_IMAGES.PCTI[15].getWidth(null),PLAYER_CREATION_IMAGES.PCTI[15].getHeight(null));
			customize3.setIcon(new ImageIcon(PLAYER_CREATION_IMAGES.PCTI[15]));
			customize4.setVisible(false);
			customize4_LEFT.setVisible(false);
			customize4_RIGHT.setVisible(false);
		}
	}
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		TRANSITION1.RENDER_TRANSITION1(g2d);
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(PLAYER_CREATION_IMAGES.PCI[0], 0, 0, null);
		STAR_ANIMATION.RENDER_STARS(g2d);
		g2d.drawImage(PLAYER_CREATION_IMAGES.PCI[1], 182*GAME_FRAME.SCALE, -6*GAME_FRAME.SCALE, null);
		//g2d.drawImage(PLAYER_CREATION_IMAGES.PCI[2], 92*GAME_FRAME.SCALE, -4*GAME_FRAME.SCALE, null);
		g2d.drawImage(PLAYER_CREATION_IMAGES.PCI[2], 80*GAME_FRAME.SCALE, -4*GAME_FRAME.SCALE, null);
		g2d.drawImage(PLAYER_CREATION_IMAGES.PCTI[4+base_stat_todraw], 110*GAME_FRAME.SCALE, 24*GAME_FRAME.SCALE, null);//base stat drawing
		//g2d.drawImage(PLAYER_CREATION_IMAGES.PCTI[28], 124*GAME_FRAME.SCALE, 67*GAME_FRAME.SCALE, null);
		g2d.drawImage(PLAYER_CREATION_IMAGES.PCTI[28], 112*GAME_FRAME.SCALE, 67*GAME_FRAME.SCALE, null);
		g2d.drawImage(PLAYER_CREATION_IMAGES.PCI[nameFieldToDraw], 211*GAME_FRAME.SCALE, 136*GAME_FRAME.SCALE, null);
		
		name.setCaretPosition(name.getText().length());
		
		try{	
		power.setText(power_text.substring(0, stat_num)+random_int(21-stat_num));
		agility.setText(agility_text.substring(0, stat_num_agi)+random_int(22 - stat_num_agi));
		precision.setText(precision_text.substring(0, stat_num_prf)+random_int(22 - stat_num_prf));
		resilience.setText(resilience_text.substring(0, stat_num_prf)+random_int(22 - stat_num_prf));
		fortitude.setText(fortitude_text.substring(0, stat_num_prf)+random_int(22 - stat_num_prf));
		
		}catch(Exception e){
			
		}
		generate_base_stats();
		
		
	
		}
		public String random_int(int length){
			String x;
			String y = "";
			if(length > 0){
			for (int i = 0; i < length; i++){
				x = y+rand.nextInt(9);
				y = x;
			}
			}else{
				y="";
			}
			return y;
		}
					
		
}
	





