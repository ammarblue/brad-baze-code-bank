import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class mainFrame extends JFrame{
	public static final String NAME = "PM2.5 Raster - Brad Bazemore & Calin Gribble";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static Image map_image = new ImageIcon(mainFrame.class.getResource("map.png")).getImage();
	public static JPanel displayPanel;
	public static JPanel datePanel;
	public static final JSlider slider1 = new JSlider(JSlider.VERTICAL);
	public static final JSlider slider2 = new JSlider();
	public static final JLabel legend = new JLabel();
	public static final JLabel displayDate = new JLabel();
	public day[] year = new day[365];
	
	
	
	public mainFrame(){
	
		int counter = 0;
		double tempX = 0;
		double tempY = 0;
		double tempDay = 0;
		double foo = 0;
		Scanner sc2 = null;
		sc2 = new Scanner(mainFrame.class.getResourceAsStream("data.txt"));
		    while (sc2.hasNextLine()) {
		            Scanner s2 = new Scanner(sc2.nextLine());
		        while (s2.hasNext()) {
		            String s = s2.next();
		            foo = Double.parseDouble(s);
		            
		            if(counter == 0){
		            	tempX = foo;
		            }
		            if(counter == 1){
		            	tempY = foo;
		            }
		            if(counter == 2){
		            	tempDay = foo-1;
		            }
		            if(counter == 3){
		            	point tempPoint = new point(tempX, tempY, tempDay, foo);
		            	try{
		            		year[(int)tempDay].points.add(tempPoint);
		            	}catch(Exception e){
		            		day day = new day();
		            		year[(int)tempDay] = day;
		            		year[(int)tempDay].points.add(tempPoint);
		            	}
		            	counter = -1;
		            	
		            }
		            
		            counter++;
		            
		        }
		    }
		setTitle(NAME);
		setMinimumSize(new Dimension(WIDTH, HEIGHT ));
		setMaximumSize(new Dimension(WIDTH , HEIGHT));
		setPreferredSize(new Dimension(WIDTH , HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		displayPanel = new JPanel(){
	
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				//g2d.drawImage(map_image.getScaledInstance(map_image.getWidth(null) *(slider1.getValue()/50), map_image.getHeight(null)*(slider1.getValue()/50), Image.SCALE_FAST),0,0,null);
				g2d.drawImage(map_image, (displayPanel.getWidth()/2)-((int)(((double)map_image.getWidth(null))*((double)slider1.getValue()/50.0))/2), (displayPanel.getHeight()/2)-((int)(((double)map_image.getHeight(null))*((double)slider1.getValue()/50.0))/2), (int)(((double)map_image.getWidth(null))*((double)slider1.getValue()/50.0)),(int)(((double)map_image.getHeight(null))*((double)slider1.getValue()/50.0)),null);
				legend.setLocation(15, displayPanel.getHeight() - 155);
				//System.out.println(slider1.getValue());
				for(int i = 0; i < year[slider2.getValue()].points.size(); i++){
					g2d.setColor(year[slider2.getValue()].points.get(i).color);
					g2d.fillRect((int)(year[slider2.getValue()].points.get(i).x * 5) + 850, (int)(year[slider2.getValue()].points.get(i).y * 5), 2, 2);
				}
				
			}
		};
		displayPanel.setBackground(new Color(54,149,229));
		displayPanel.setLayout(null);
		render.render(displayPanel);
		displayPanel.setBounds(10, 10, this.getWidth()- 250, this.getHeight()- 200);
		displayPanel.setFocusable(true);
		displayPanel.setVisible(true);
		displayPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		
	    slider1.setMajorTickSpacing(20);
	    slider1.setMinorTickSpacing(5);
	    slider1.setPaintTicks(true);
	    slider1.setLocation(10,20);
	    slider1.setSize(60, 200);
	    slider1.setOpaque(false);
        slider1.setVisible(true);
        slider1.setValue(25);
        //slider1.setForeground(Color.WHITE);
	    displayPanel.add(slider1);
	    
	    legend.setBackground(Color.WHITE);
	    legend.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    legend.setOpaque(true);
	    legend.setLocation(15, displayPanel.getHeight() - 155);
	    legend.setSize(120, 140);
	    displayPanel.add(legend);
	    
	    
		datePanel = new JPanel(){
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				slider2.setSize(this.getWidth() - 230, 60);
				String dt = "01/01/2009";  // Start date
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Calendar c = Calendar.getInstance();
				try {
					c.setTime(sdf.parse(dt));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				c.add(Calendar.DATE, slider2.getValue());  // number of days to add
				dt = sdf.format(c.getTime()); 
				displayDate.setText(dt);
			}
		};
		//datePanel.setBackground(new Color(54,149,229));
		datePanel.setLayout(null);
		render.render(datePanel);
		datePanel.setBounds(10, this.getHeight()- 180, this.getWidth()-35, 132);
		datePanel.setFocusable(true);
		datePanel.setVisible(true);
		datePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
	    slider2.setMajorTickSpacing(30);
	    slider2.setMaximum(364);
	    slider2.setMinorTickSpacing(5);
	    slider2.setPaintTicks(true);
	    slider2.setLocation(210,30);
	    slider2.setSize(this.getWidth() - 230, 60);
	    slider2.setOpaque(false);
        slider2.setVisible(true);
        slider2.setValue(0);
        slider2.setForeground(Color.BLACK);
	    datePanel.add(slider2);
	    
	    displayDate.setLocation(40, 29);
	    displayDate.setSize(200,60);
	    displayDate.setFont(new Font("Arial", Font.BOLD, 24));
	    displayDate.setText("YO");
	    displayDate.setVisible(true);
	    datePanel.add(displayDate);
	    
		add(displayPanel);
		add(datePanel);
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
		pack();
	}
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		displayPanel.setBounds(10, 10, this.getWidth()- 35, this.getHeight()- 200);
		datePanel.setBounds(10, this.getHeight()- 180, this.getWidth()-35, 132);
	}
	
}
