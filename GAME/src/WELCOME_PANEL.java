import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WELCOME_PANEL extends JPanel {
	static Timer timer = new Timer();
	float rayopacity = 0.5f;
	int titleframe = 2;
	JButton start;
	JButton facebook;
	JButton twitter;
	public static void moveToCLPanel(){
	TimerTask moveToCLPanel = new TimerTask(){
		public void run() {
			GAME_FRAME.displayPanel.removeAll();
			///**** CHANGE THIS TO LOAD NEXT PANEL
			GAME_FRAME.displayPanel.add(new CHARACTER_LIST_PANEL());
		}
	};
	timer.schedule(moveToCLPanel, 1500L);
	}
	public WELCOME_PANEL(){
		setLayout(null);
		setBounds(0, 0, GAME_FRAME.WIDTH, GAME_FRAME.HEIGHT);
		RENDER.RENDER(this);
		run_ray_opacity_animation();
		//STAR_ANIMATION.ANIMATE_STARS();
		run_title_animation();
		
		//start
		start = new JButton();
		start.setSize(WELCOME_IMAGES.WI[27].getWidth(null),WELCOME_IMAGES.WI[27].getHeight(null));
		start.setIcon(new ImageIcon(WELCOME_IMAGES.WI[27]));
		start.setDisabledIcon(start.getIcon());
		start.setBorder(BorderFactory.createEmptyBorder());
		start.setContentAreaFilled(false);
		start.addMouseListener(new java.awt.event.MouseAdapter() {
			int x = 0;
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	x = 1;
		    	start.setIcon(new ImageIcon(WELCOME_IMAGES.WI[28]));
		    	
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	x = 0;
		    	start.setIcon(new ImageIcon(WELCOME_IMAGES.WI[27]));
		    	
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	
		    		start.setIcon(new ImageIcon(WELCOME_IMAGES.WI[29]));
		    		
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	if(x == 1){
		    	start.setIcon(new ImageIcon(WELCOME_IMAGES.WI[28]));
		    	}else{
		    		start.setIcon(new ImageIcon(WELCOME_IMAGES.WI[27]));
		    	}
		    	
		    }
		    
		});
		start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	TRANSITION1.ANIMATE_TRANSITIION1(new WELCOME_PANEL());
            	moveToCLPanel();
            	setEnabled(false);
            }
        });
		start.setLocation(((GAME_FRAME.WIDTH / 2)-(WELCOME_IMAGES.WI[28].getWidth(null)/2)), 124*GAME_FRAME.SCALE);
		add(start);
		
		
		
		//twitter
		twitter = new JButton();
		twitter.setSize(WELCOME_IMAGES.WI[33].getWidth(null),WELCOME_IMAGES.WI[33].getHeight(null));
		twitter.setIcon(new ImageIcon(WELCOME_IMAGES.WI[33]));
		twitter.setDisabledIcon(new ImageIcon(WELCOME_IMAGES.WI[33]));
		twitter.setBorder(BorderFactory.createEmptyBorder());
		twitter.setContentAreaFilled(false);
		twitter.addMouseListener(new java.awt.event.MouseAdapter() {
			int x = 0;
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	x = 1;
		    	twitter.setIcon(new ImageIcon(WELCOME_IMAGES.WI[34]));
		    	}
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	x = 0;
		    	twitter.setIcon(new ImageIcon(WELCOME_IMAGES.WI[33]));
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	
		    		twitter.setIcon(new ImageIcon(WELCOME_IMAGES.WI[35]));
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	if(x == 1){
		    	twitter.setIcon(new ImageIcon(WELCOME_IMAGES.WI[34]));
		    	}else{
		    		twitter.setIcon(new ImageIcon(WELCOME_IMAGES.WI[33]));
		    	}
		    	  }
		    
		});
		twitter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	
            }
        });
		twitter.setLocation(252*GAME_FRAME.SCALE, 129*GAME_FRAME.SCALE);
		add(twitter);
		
		//facebook
		facebook = new JButton();
		facebook.setSize(WELCOME_IMAGES.WI[30].getWidth(null),WELCOME_IMAGES.WI[30].getHeight(null));
		facebook.setIcon(new ImageIcon(WELCOME_IMAGES.WI[30]));
		facebook.setDisabledIcon(new ImageIcon(WELCOME_IMAGES.WI[30]));
		facebook.setBorder(BorderFactory.createEmptyBorder());
		facebook.setContentAreaFilled(false);
		facebook.addMouseListener(new java.awt.event.MouseAdapter() {
			int x = 0;
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	x = 1;
		    	facebook.setIcon(new ImageIcon(WELCOME_IMAGES.WI[31]));
		    	}
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	x = 0;
		    	facebook.setIcon(new ImageIcon(WELCOME_IMAGES.WI[30]));
		    }
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	
		    		facebook.setIcon(new ImageIcon(WELCOME_IMAGES.WI[32]));
		    }
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
		    	if(x == 1){
		    	facebook.setIcon(new ImageIcon(WELCOME_IMAGES.WI[31]));
		    	}else{
		    		facebook.setIcon(new ImageIcon(WELCOME_IMAGES.WI[30]));
		    	}
		    	  }
		    
		});
		facebook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	
            }
        });
		facebook.setLocation(252*GAME_FRAME.SCALE, 152*GAME_FRAME.SCALE);
		add(facebook);
		
		
		setFocusable(true);
		setVisible(true);	
	}
	public void setEnabled(boolean enabled) {
	    super.setEnabled(enabled);
	    for (Component component : getComponents())
	        component.setEnabled(enabled);
	    
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(WELCOME_IMAGES.WI[0], 0, 0, null);
		STAR_ANIMATION.RENDER_STARS(g2d);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, rayopacity));
		g2d.drawImage(WELCOME_IMAGES.WI[36], 0, 0, null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		g2d.drawImage(WELCOME_IMAGES.WI[26], 0, 0, null);
		
		g2d.drawImage(WELCOME_IMAGES.WI[1], ((GAME_FRAME.WIDTH / 2)-(WELCOME_IMAGES.WI[1].getWidth(null)/2)), 53*GAME_FRAME.SCALE, null);
		g2d.drawImage(WELCOME_IMAGES.WI[titleframe], 56*GAME_FRAME.SCALE, 56*GAME_FRAME.SCALE, null);
		g2d.drawImage(WELCOME_IMAGES.WTI[0], 68*GAME_FRAME.SCALE, 108*GAME_FRAME.SCALE, null);
	
	}
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		TRANSITION1.RENDER_TRANSITION1(g2d);
	}
	

	public void run_ray_opacity_animation(){
		
		TimerTask task = new TimerTask(){
			int upDown = 0;//0 up, 1 down
			public void run() {
				if(upDown == 0){
					if(rayopacity < .99){
						rayopacity+= .01;
					}else{
						upDown = 1;
						
					}
				}
				if(upDown == 1){
					if(rayopacity > .55){
						rayopacity-= .01;
					}else{
						upDown = 0;
						
					}
				}
			}
		};
		timer.schedule(task, 0, 50);
	}
	public void run_title_animation(){
		final 
		TimerTask delay = new TimerTask(){
			public void run() {
				TimerTask task1 = new TimerTask(){
					public void run() {
						if(titleframe < 22){
							titleframe++;
						}else{
							titleframe = 2;
							this.cancel();
							
						}
						
					}
				};
				timer.schedule(task1, 0, 40);
				
			}
		};
		
		timer.schedule(delay, 2000 ,6000);
		
	}

}
