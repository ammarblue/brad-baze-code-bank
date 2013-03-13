import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class midgui extends JFrame implements ActionListener{
	JPanel 
	public static void main(String args[]){
		midgui gui=new midgui();
		gui.setVisible(true);
	}
	public midgui(){
		super("TEST2");
		setSize(400,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		JPanel mainpanel=new JPanel();
		
		
	}
	public void actionPerformed(ActionEvent e){
		
	}
}
