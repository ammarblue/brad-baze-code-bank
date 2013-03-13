//Brad Bazemore
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
public class GUI extends JFrame implements MouseListener,ActionListener,MouseMotionListener{
	JPanel paint;
	JPanel buttons;
	JButton Red,Green,Blue,Black,Erase,Draw,Clear,Save;
	Paint P;
	int c=0,s=0,x1,y1,x2,y2;
	public GUI(){
		super("PAINT");
		setSize(600,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		
		buttons=new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.setBackground(Color.BLACK);
		mainPanel.add(buttons,BorderLayout.SOUTH);
		
		paint=new JPanel();
		paint.setLayout(new BorderLayout());
		paint.setBackground(Color.WHITE);
		mainPanel.add(paint,BorderLayout.CENTER);

		Draw=new JButton("Draw");
		Draw.setBackground(Color.LIGHT_GRAY);
		Draw.addActionListener(this);
		buttons.add(Draw);
		
		Red=new JButton("RED");
		Red.setBackground(Color.RED);
		Red.addActionListener(this);
		buttons.add(Red);
		
		Blue=new JButton("BLUE");
		Blue.setBackground(Color.BLUE);
		Blue.addActionListener(this);
		buttons.add(Blue);
		
		Green=new JButton("GREEN");
		Green.setBackground(Color.GREEN);
		Green.addActionListener(this);
		buttons.add(Green);
		
		Black=new JButton("Black");
		Black.setBackground(Color.GRAY);
		Black.addActionListener(this);
		buttons.add(Black);
		
		Erase=new JButton("ERASE");
		Erase.setBackground(Color.LIGHT_GRAY);
		Erase.addActionListener(this);
		buttons.add(Erase);
		
		Clear=new JButton("CLEAR");
		Clear.setBackground(Color.LIGHT_GRAY);
		Clear.addActionListener(this);
		buttons.add(Clear);
		
		Save=new JButton("SAVE");
		Save.setBackground(Color.LIGHT_GRAY);
		Save.addActionListener(this);
		buttons.add(Save);
		
		P=new Paint(this);
		paint.add(P);
		P.addMouseListener(this);
		P.addMouseMotionListener(this);
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		x2=e.getX();
		y2=e.getY();
	}
	public void mouseReleased(MouseEvent e) {}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Clear){
			System.out.println("Clear");
			s=2;
			P.repaint();
		}else if(e.getSource()==Erase){
			System.out.println("Erase");
			s=1;
		}else if(e.getSource()==Draw){
			System.out.println("Draw");
			s=0;
		}else if(e.getSource()==Save){
			System.out.println("Save");
			String file=JOptionPane.showInputDialog("Enter File Name");
		}else if(e.getSource()==Red){
			System.out.println("Red");
			c=1;
			P.repaint();
		}else if(e.getSource()==Blue){
			System.out.println("Blue");
			c=2;
			P.repaint();
		}else if(e.getSource()==Green){
			System.out.println("Green");
			c=3;
			P.repaint();
		}else if(e.getSource()==Black){
			System.out.println("Black");
			c=0;
			P.repaint();
		}
	}
	public void mouseDragged(MouseEvent e) {
		x1=e.getX();
		y1=e.getY();
		P.repaint();
	}
	public void mouseMoved(MouseEvent e) {}
}
class Paint extends JPanel{
	GUI G;
	Paint(GUI GIN){
		G=GIN;
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
	}
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		if(G.c==0){
			g.setColor(Color.BLACK);
		}else if(G.c==1){
			g.setColor(Color.RED);
		}else if(G.c==2){
			g.setColor(Color.BLUE);
		}else if(G.c==3){
			g.setColor(Color.GREEN);
		}
		if(G.s==0){
			g.drawLine(G.x1, G.y1, G.x2, G.y2);
			G.x2=G.x1;
			G.y2=G.y1;
		}else if(G.s==1){
			g.clearRect(G.x1, G.y1, 20, 20);
		}else if(G.s==2){
			g.clearRect(0, 0, 600, 500);
			G.s=0;
		}
	}
}
class WriteImage {	
    public void save(Component g2,String filename){
    	
    }
}
