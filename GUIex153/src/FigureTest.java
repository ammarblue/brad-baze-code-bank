import javax.swing.*;
import java.awt.*;
public class FigureTest extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public FigureTest(){
		setTitle("Arcs");
		add(new ArcsPanel());
	}
	public static void main(String[] args){
		FigureTest frame=new FigureTest();
		frame.setSize(250,300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
class ArcsPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void paintComponet(Graphics g){
		super.paintComponent(g);
		int xCenter=getWidth()/2;
		int yCenter=getHeight()/2;
		int radius=(int)(Math.min(getWidth(), getHeight())*0.4);
		int x=xCenter-radius;
		int y=yCenter-radius;
		g.fillArc(x, y, 2*radius, 2*radius, 0, 30);
		g.fillArc(x, y, 2*radius, 2*radius, 90, 30);
		g.fillArc(x, y, 2*radius, 2*radius, 180, 30);
		g.fillArc(x, y, 2*radius, 2*radius, 270, 30);
	}
}
