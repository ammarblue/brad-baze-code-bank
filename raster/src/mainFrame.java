import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class mainFrame extends JFrame {
	public  final String NAME = "PM2.5 Raster - Brad Bazemore & Calin Gribble";
	public  final int WIDTH = 800;
	public  final int HEIGHT = 600;
	public  Image map_image = new ImageIcon(
			mainFrame.class.getResource("map.png")).getImage();
	public  JPanel displayPanel;
	public  JPanel datePanel;
	public  JPanel idwControlPanel;
	public  final JSlider slider1 = new JSlider(JSlider.VERTICAL);
	public  final JSlider slider2 = new JSlider();
	public  final JLabel legend = new JLabel();
	public  final JLabel displayDate = new JLabel();
	public  final JLabel idwSearchLbl = new JLabel();
	public  final JLabel idwSearchDirectionLbl = new JLabel();
	public  final JLabel longitudeLbl = new JLabel();
	public  final JTextField longitudeTxt = new JTextField();
	public  final JLabel latitudeLbl = new JLabel();
	public  final JTextField latitudeTxt = new JTextField();
	public  final JLabel timeLbl = new JLabel();
	public  final JTextField timeTxt = new JTextField();
	
	public  final JLabel weightLbl = new JLabel();
	public  final JTextField weightTxt = new JTextField();
	public  final JLabel neighborsLbl = new JLabel();
	public  final JTextField neighborsTxt = new JTextField();
	
	public  final JButton idwCalculate = new JButton();
	public  final JButton animate = new JButton();
	public  final JLabel idwResult = new JLabel();
	
	public day[] year = new day[365];
	
	public double mouseLongitude;
	public double mouseLatitude;
	public int mouseX;
	public int mouseY;
	public double roundLat;
	public double roundLong;
	
	public boolean animating = false;
	double temp = 0;

	
	public IDW test=new IDW(year);
	
	
	public mainFrame() {

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

				if (counter == 0) {
					tempX = foo;
				}
				if (counter == 1) {
					tempY = foo;
				}
				if (counter == 2) {
					tempDay = foo - 1;
				}
				if (counter == 3) {
					point tempPoint = new point(tempX, tempY, tempDay, foo);
					try {
						year[(int) tempDay].points.add(tempPoint);
					} catch (Exception e) {
						day day = new day();
						year[(int) tempDay] = day;
						year[(int) tempDay].points.add(tempPoint);
					}
					counter = -1;
				}
				counter++;
			}
		}
		
		DataRead dr=new DataRead(year);
		dr.Write();
		dr.ReadforError();
		setTitle(NAME);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		displayPanel = new JPanel() {

			public void paintComponent(Graphics g) {

				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				// g2d.drawImage(map_image.getScaledInstance(map_image.getWidth(null)
				// *(slider1.getValue()/50),
				// map_image.getHeight(null)*(slider1.getValue()/50),
				// Image.SCALE_FAST),0,0,null);
				g2d.drawImage(
						map_image,
						(displayPanel.getWidth() / 2)
								- ((int) (((double) map_image.getWidth(null)) * ((double) slider1
										.getValue() / 30.0)) / 2),
						(displayPanel.getHeight() / 2)
								- ((int) (((double) map_image.getHeight(null)) * ((double) slider1
										.getValue() / 30.0)) / 2),
						(int) (((double) map_image.getWidth(null)) * ((double) slider1
								.getValue() / 30.0)),
						(int) (((double) map_image.getHeight(null)) * ((double) slider1
								.getValue() / 30.0)), null);
				// g2d.drawImage(map_image, 0, 0,
				// map_image.getWidth(null),map_image.getHeight(null),null);
				
				// System.out.println(slider1.getValue());

				for (int i = 0; i < year[slider2.getValue()].points.size(); i++) {
					g2d.setColor(year[slider2.getValue()].points.get(i).color);
					// g2d.fillRect((int)(year[slider2.getValue()].points.get(i).x
					// * 8.8000) + (int)(139.2 * 8.8000),
					// (int)(year[slider2.getValue()].points.get(i).y * -8.8000)
					// + (int)(61.5 * 8.8000) , 2, 2);
					g2d.fillRect(
							longToX(year[slider2.getValue()].points.get(i).x),
							latToY(year[slider2.getValue()].points.get(i).y),
							(2 * (slider1.getValue() / 40)) + 1,
							(2 * (slider1.getValue() / 40)) + 1);

				}

				g2d.setColor(new Color(160, 160, 160));
				g2d.drawLine(0, displayPanel.getHeight() / 2,
						displayPanel.getWidth(), displayPanel.getHeight() / 2);
				g2d.drawLine(displayPanel.getWidth() / 2, 0,
						displayPanel.getWidth() / 2, displayPanel.getHeight());
				g2d.setColor(new Color(51, 0, 102));
				double factor = 1e5; // = 1 * 10^5 = 100000.
				roundLat = Math.round(mouseLatitude * factor) / factor;
				roundLong = Math.round(mouseLongitude * factor) / factor;
				
					g2d.drawString("LATITUDE: "+roundLat + " N, " + "LONGITUDE:" + roundLong
						+ " W", mouseX, mouseY);
					g2d.drawString("MOUSE X:" + mouseX + ", MOUSE Y:" + mouseY,
						mouseX, mouseY + 20);
				
				//Draw Legend
				g2d.setColor(Color.GRAY);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .60f));
				g2d.fillRect(15, displayPanel.getHeight() - 155, 120, 140);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
				g2d.setColor(Color.BLACK);
				g2d.drawString("Legend", 20, displayPanel.getHeight() - 140);
				g2d.setColor(new Color(250, 253, 40));
				g2d.fillRect(20, displayPanel.getHeight() - 135, 30, 15);
				g2d.setColor(Color.BLACK);
				g2d.drawString("    0 <   x   < 5", 54, displayPanel.getHeight() - 123);
				g2d.setColor(new Color(253, 220, 40));
				g2d.fillRect(20, displayPanel.getHeight() - 115, 30, 15);
				g2d.setColor(Color.BLACK);
				g2d.drawString("  5 =<   x   < 10", 53, displayPanel.getHeight() - 103);
				g2d.setColor(new Color(253, 195, 40));
				g2d.fillRect(20, displayPanel.getHeight() - 95, 30, 15);
				g2d.setColor(Color.BLACK);
				g2d.drawString("10 =<   x   < 15", 52, displayPanel.getHeight() - 83);
				g2d.setColor(new Color(253, 153, 40));
				g2d.fillRect(20, displayPanel.getHeight() - 75, 30, 15);
				g2d.setColor(Color.BLACK);
				g2d.drawString("15 =<   x   < 20", 52, displayPanel.getHeight() - 63);
				g2d.setColor(new Color(253, 75, 40));
				g2d.fillRect(20, displayPanel.getHeight() - 55, 30, 15);
				g2d.setColor(Color.BLACK);
				g2d.drawString("20 =<   x   < 25", 52, displayPanel.getHeight() - 43);
				g2d.setColor(new Color(253, 40, 40));
				g2d.fillRect(20, displayPanel.getHeight() - 35, 30, 15);
				g2d.setColor(Color.BLACK);
				g2d.drawString("25 =<   x ", 52, displayPanel.getHeight() - 23);
				
				
			}
			
		};
		displayPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseLatitude = 37.765392 - ((((mouseY) - displayPanel
						.getHeight() / 2) / (((320 - 74) / (48.936935 - 26.056783))
						* slider1.getValue() / 40)));

				// mouseLongitude =
				// -96.199824+((((mouseX)-displayPanel.getWidth()/2)/(((382-148)/(123.2-96.199824))*slider1.getValue()/40)));
				mouseLongitude = -96.199824
						+ ((((mouseX) - displayPanel.getWidth() / 2) / (((634 - 135) / (124.49707 - 67.126465))
								* slider1.getValue() / 40)));
				
				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// mouseLatitude = 38.065392 -
				// ((((mouseY)-displayPanel.getHeight()/2)/(((359-89)/(48.135797-22.037042))*slider1.getValue()/40)));
				mouseLatitude = 37.765392 - ((((mouseY) - displayPanel
						.getHeight() / 2) / (((320 - 74) / (48.936935 - 26.056783))
						* slider1.getValue() / 40)));

				// mouseLongitude =
				// -96.199824+((((mouseX)-displayPanel.getWidth()/2)/(((382-148)/(123.2-96.199824))*slider1.getValue()/40)));
				mouseLongitude = -96.199824
						+ ((((mouseX) - displayPanel.getWidth() / 2) / (((634 - 135) / (124.49707 - 67.126465))
								* slider1.getValue() / 40)));
				
				mouseX = e.getX();
				mouseY = e.getY();
				

			}
		});
		displayPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				mouseX = e.getX();
				mouseY = e.getY();
				latitudeTxt.setText(""+roundLat);
				longitudeTxt.setText(""+roundLong);
				timeTxt.setText(""+slider2.getValue());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				mouseX = -10000;
				mouseY = -10000;
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			
		});
		

		displayPanel.setBackground(new Color(54, 149, 229));
		displayPanel.setLayout(null);
		render.render(displayPanel);
		//displayPanel.setBounds(10, 10, this.getWidth() - 250,this.getHeight() - 200);
		displayPanel.setFocusable(true);
		displayPanel.setVisible(true);
		displayPanel.setBorder(BorderFactory.createLoweredBevelBorder());

		slider1.setMajorTickSpacing(20);
		slider1.setMinorTickSpacing(5);
		slider1.setPaintTicks(true);
		slider1.setLocation(10, 20);
		slider1.setSize(60, 200);
		slider1.setOpaque(false);
		slider1.setVisible(true);
		slider1.setMinimum(1);
		slider1.setValue(40);
		// slider1.setForeground(Color.WHITE);
		displayPanel.add(slider1);

		legend.setBackground(Color.WHITE);
		legend.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		legend.setOpaque(true);
		legend.setLocation(15, displayPanel.getHeight() - 155);
		legend.setSize(120, 140);
		displayPanel.add(legend);

		datePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				slider2.setSize(this.getWidth() - 230, 60);
				String dt = "01/01/2009"; // Start date
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Calendar c = Calendar.getInstance();
				try {
					c.setTime(sdf.parse(dt));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				c.add(Calendar.DATE, slider2.getValue()); // number of days to
															// add
				dt = sdf.format(c.getTime());
				displayDate.setText(dt);
				
				if(animating){
					temp++;
					if(temp % 10 == 0){
					if(slider2.getValue() < 364){
						slider2.setValue((int)(slider2.getValue() + 1));
					}else{
						slider2.setValue(0);
					}
					}
				}else{
					temp = 0;
				}
			}
		};
		// datePanel.setBackground(new Color(54,149,229));
		datePanel.setLayout(null);
		render.render(datePanel);
		//datePanel.setBounds(10, this.getHeight() - 180, this.getWidth() - 35,
				//132);
		datePanel.setFocusable(true);
		datePanel.setVisible(true);
		datePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		slider2.setMajorTickSpacing(30);
		slider2.setMaximum(364);
		slider2.setMinorTickSpacing(5);
		slider2.setPaintTicks(true);
		slider2.setLocation(210, 30);
		slider2.setSize(this.getWidth() - 230, 60);
		slider2.setOpaque(false);
		slider2.setVisible(true);
		slider2.setValue(0);
		slider2.setForeground(Color.BLACK);
		datePanel.add(slider2);

		displayDate.setLocation(40, 29);
		displayDate.setSize(200, 60);
		displayDate.setFont(new Font("Arial", Font.BOLD, 24));
		displayDate.setText("YO");
		displayDate.setVisible(true);
		datePanel.add(displayDate);
		
		idwControlPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				animate.setLocation(10, this.getHeight() - 30);
			}
		};
		
		idwControlPanel.setLayout(null);
		render.render(idwControlPanel);
		idwSearchLbl.setLocation(10, 10);
		idwSearchLbl.setSize(200, 14);
		idwSearchLbl.setFont(new Font("Arial", Font.BOLD, 14));
		idwSearchLbl.setText("IDW Search");
		idwSearchLbl.setVisible(true);
		idwControlPanel.add(idwSearchLbl);
		
		idwSearchDirectionLbl.setLocation(10, 26);
		idwSearchDirectionLbl.setSize(200, 37);
		idwSearchDirectionLbl.setFont(new Font("Arial", Font.PLAIN, 10));
		idwSearchDirectionLbl.setText("<HTML><font color= 'green'>Simply click points on<br>the map to fill in this<br>information.");
		idwSearchDirectionLbl.setVisible(true);
		idwControlPanel.add(idwSearchDirectionLbl);
		
		latitudeLbl.setLocation(10, 68);
		latitudeLbl.setSize(200, 14);
		latitudeLbl.setFont(new Font("Arial", Font.BOLD, 12));
		latitudeLbl.setText("Latitude:");
		latitudeLbl.setVisible(true);
		idwControlPanel.add(latitudeLbl);
		
		latitudeTxt.setLocation(10, 84);
		latitudeTxt.setSize(115, 18);
		latitudeTxt.setFont(new Font("Arial", Font.PLAIN, 12));
		latitudeTxt.setVisible(true);
		idwControlPanel.add(latitudeTxt);
		
		
		longitudeLbl.setLocation(10, 102);
		longitudeLbl.setSize(200, 14);
		longitudeLbl.setFont(new Font("Arial", Font.BOLD, 12));
		longitudeLbl.setText("Longitude:");
		longitudeLbl.setVisible(true);
		idwControlPanel.add(longitudeLbl);
		
		longitudeTxt.setLocation(10, 118);
		longitudeTxt.setSize(115, 18);
		longitudeTxt.setFont(new Font("Arial", Font.PLAIN, 12));
		longitudeTxt.setVisible(true);
		idwControlPanel.add(longitudeTxt);
		
		timeLbl.setLocation(10, 136);
		timeLbl.setSize(200, 14);
		timeLbl.setFont(new Font("Arial", Font.BOLD, 12));
		timeLbl.setText("Day (t):");
		timeLbl.setVisible(true);
		idwControlPanel.add(timeLbl);
		
		timeTxt.setLocation(10, 152);
		timeTxt.setSize(115, 18);
		timeTxt.setFont(new Font("Arial", Font.PLAIN, 12));
		timeTxt.setVisible(true);
		idwControlPanel.add(timeTxt);
		
		weightLbl.setLocation(10, 170);
		weightLbl.setSize(200, 14);
		weightLbl.setFont(new Font("Arial", Font.BOLD, 12));
		weightLbl.setText("Weight Value (p):");
		weightLbl.setVisible(true);
		idwControlPanel.add(weightLbl);
		
		weightTxt.setLocation(10, 186);
		weightTxt.setSize(115, 18);
		weightTxt.setFont(new Font("Arial", Font.PLAIN, 12));
		weightTxt.setVisible(true);
		weightTxt.setText(""+2);
		idwControlPanel.add(weightTxt);
		
		neighborsLbl.setLocation(10, 204);
		neighborsLbl.setSize(200, 14);
		neighborsLbl.setFont(new Font("Arial", Font.BOLD, 12));
		neighborsLbl.setText("Neighbors (n):");
		neighborsLbl.setVisible(true);
		idwControlPanel.add(neighborsLbl);
		
		neighborsTxt.setLocation(10, 220);
		neighborsTxt.setSize(115, 18);
		neighborsTxt.setFont(new Font("Arial", Font.PLAIN, 12));
		neighborsTxt.setVisible(true);
		neighborsTxt.setText(""+5);
		idwControlPanel.add(neighborsTxt);
		
		idwCalculate.setLocation(10, 242);
		idwCalculate.setSize(115, 22);
		idwCalculate.setFont(new Font("Arial", Font.BOLD, 12));
		idwCalculate.setText("CALCULATE");
		idwCalculate.setVisible(true);
		idwCalculate.setFocusable(false);
		idwCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				try{
					double factor = 1e5; // = 1 * 10^5 = 100000.
					//Math.round(mouseLatitude * factor) / factor
					idwResult.setText("(v) = "+ Math.round((test.idw(Double.parseDouble(latitudeTxt.getText()), Double.parseDouble(longitudeTxt.getText()), Integer.parseInt(timeTxt.getText()), Integer.parseInt(weightTxt.getText()), Integer.parseInt(neighborsTxt.getText())))* factor) / factor);
				}catch(Exception e9){
					idwResult.setText("Invalid Input");
				}
			}
	     });      
		idwControlPanel.add(idwCalculate);
		
		idwResult.setLocation(10, 264);
		idwResult.setSize(115, 22);
		idwResult.setForeground(Color.RED);
		idwResult.setFont(new Font("Arial", Font.PLAIN, 13));
		idwResult.setText("Result");
		idwResult.setVisible(true);
		idwControlPanel.add(idwResult);
		
		
		
		animate.setSize(115, 22);
		animate.setFont(new Font("Arial", Font.BOLD, 12));
		animate.setText("ANIMATE");
		animate.setVisible(true);
		animate.setFocusable(false);
		animate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
	           if(animating){
	        	   animating = false;
	        	   animate.setText("ANIMATE");
	           }else{
	        	   animating = true;
	        	   animate.setText("STOP");
	           }
	        }
	     });      
		idwControlPanel.add(animate);

		
		
		
		idwControlPanel.setFocusable(true);
		idwControlPanel.setVisible(true);
		idwControlPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		
		add(displayPanel);
		add(datePanel);
		add(idwControlPanel);
		
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
		pack();
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		displayPanel.setBounds(10, 10, this.getWidth() - 180,
				this.getHeight() - 200);
		datePanel.setBounds(10, this.getHeight() - 180, this.getWidth() - 35,
				132);
		idwControlPanel.setBounds(this.getWidth() - 160, 10, 135,
				this.getHeight() - 201);
		
		
	}

	public int longToX(double longitude) {
		int value = 0;

		// value = (int)(((96.199824 +
		// longitude)*(((382-148)/(123.2-96.199824))*slider1.getValue()/40)) +
		// displayPanel.getWidth()/2);
		value = (int) (((96.199824 + longitude) * (((634 - 135) / (124.49707 - 67.126465))
				* slider1.getValue() / 40)) + displayPanel.getWidth() / 2);
		return value;
	}

	public int latToY(double latitude) {
		int value = 0;

		// value = (int)(((38.065392 -
		// latitude)*(((359-89)/(48.135797-22.037042))*slider1.getValue()/40)) +
		// displayPanel.getHeight()/2);
		value = (int) (((37.765392 - latitude) * (((320 - 74) / (48.936935 - 26.056783))
				* slider1.getValue() / 40)) + displayPanel.getHeight() / 2);

		return value;
	}

}
