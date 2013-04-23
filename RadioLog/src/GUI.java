import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame implements ActionListener {
	JTabbedPane TPL;
	JPanel PContact, PRight,PCallsign;
	JLabel callsign, feq, time, date,logo;
	JTextField CallSign, Freq, Time, DATE;
	JButton Save, TimeStamp;
	JTable Tdata;
	DefaultTableModel tm;
	GridBagConstraints Pcon, Mcon, Ccon;
	String[] colName = { "Call Sign", "Date", "Time", "Freq", "Mode" };
	SQLConnect SQL = new SQLConnect();

	public GUI() {
		super("Radio Log");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setBackground(Color.WHITE);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		Mcon = new GridBagConstraints();
		Mcon.fill = GridBagConstraints.BOTH;
		add(mainPanel);

		TPL=new JTabbedPane();
		mainPanel.add(TPL);
		
		try {
			BufferedImage myPicture = ImageIO.read(new File("ARRL.jpg"));
			logo=new JLabel(new ImageIcon(myPicture));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Image Load Error");
		}
		add(logo);
		
		PContact = new JPanel();
		PContact.setLayout(new GridBagLayout());
		Mcon.gridx = 0;
		Mcon.gridy = 0;
		Mcon.insets = new Insets(10, 5, 0, 20);
		TPL.add("Contact",PContact);

		PCallsign=new JPanel();
		PCallsign.setLayout(new GridBagLayout());
		Mcon.gridx=0;
		Mcon.gridy=0;
		Mcon.insets=new Insets(10,5,0,20);
		TPL.add("Call Signs",PCallsign);
		
		PRight = new JPanel();
		PRight.setLayout(new BorderLayout());
		Mcon.gridx = 1;
		Mcon.gridy = 0;
		Mcon.insets = new Insets(10, 20, 0, 5);
		mainPanel.add(PRight, Mcon);

		Pcon = new GridBagConstraints();
		Pcon.fill = GridBagConstraints.HORIZONTAL;

		callsign = new JLabel("CallSign");
		Pcon.insets = new Insets(5, 0, 0, 5);
		Pcon.gridx = 0;
		Pcon.gridy = 0;
		PContact.add(callsign, Pcon);

		CallSign = new JTextField();
		Pcon.insets = new Insets(5, 0, 0, 0);
		Pcon.gridx = 1;
		Pcon.gridy = 0;
		PContact.add(CallSign, Pcon);

		feq = new JLabel("Freq");
		Pcon.insets = new Insets(5, 0, 0, 0);
		Pcon.gridx = 0;
		Pcon.gridy = 1;
		PContact.add(feq, Pcon);

		Freq = new JTextField();
		Pcon.insets = new Insets(5, 0, 0, 0);
		Pcon.gridx = 1;
		Pcon.gridy = 1;
		PContact.add(Freq, Pcon);

		time = new JLabel("Time");
		Pcon.insets = new Insets(5, 0, 0, 0);
		Pcon.gridx = 0;
		Pcon.gridy = 2;
		PContact.add(time, Pcon);

		Time = new JTextField();
		Pcon.insets = new Insets(5, 0, 0, 0);
		Pcon.gridx = 1;
		Pcon.gridy = 2;
		PContact.add(Time, Pcon);

		date = new JLabel("Date");
		Pcon.insets = new Insets(5, 0, 0, 0);
		Pcon.gridx = 0;
		Pcon.gridy = 3;
		PContact.add(date, Pcon);

		DATE = new JTextField();
		Pcon.insets = new Insets(5, 0, 0, 0);
		Pcon.gridx = 1;
		Pcon.gridy = 3;
		PContact.add(DATE, Pcon);

		TimeStamp = new JButton("Use Time Stamp");
		Pcon.insets = new Insets(5, 0, 0, 0);
		Pcon.gridx = 1;
		Pcon.gridy = 4;
		TimeStamp.addActionListener(this);
		PContact.add(TimeStamp, Pcon);

		Save = new JButton("Save");
		Pcon.insets = new Insets(20, 50, 0, 50);
		Pcon.gridx = 1;
		Pcon.gridy = 5;
		Save.addActionListener(this);
		PContact.add(Save, Pcon);

		Tdata = new JTable(new DefaultTableModel(SQL.getAllData(),colName));//TODO need way to refresh table
		tm=(DefaultTableModel) Tdata.getModel();
		JScrollPane STabel = new JScrollPane(Tdata);
		Tdata.setPreferredScrollableViewportSize(new Dimension(450, 10));
		PRight.add(STabel, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == Save) {
			if (!SQL.commit(this)) {
				System.exit(1);
			} else {
				CallSign.setText("");
				DATE.setText("");
				Time.setText("");
				Freq.setText("");
				tm.addRow(SQL.getData());
			}
		} else if (e.getSource() == TimeStamp) {
			Date dtemp = new Date();
			String stemp = "";
			stemp = stemp.concat(dtemp.getHours() + ":");
			stemp = stemp.concat(dtemp.getMinutes() + ":");
			stemp = stemp.concat(dtemp.getSeconds() + "");
			Time.setText(stemp);
			stemp = "";
			stemp = stemp.concat((dtemp.getYear() + 1900) + "-");
			stemp = stemp.concat((dtemp.getMonth() + 1) + "-");
			stemp = stemp.concat(dtemp.getDate() + "");
			DATE.setText(stemp);
		}
	}

	public long getTime() {
		return new Date().getTime();
	}

	public Date getDate() {
		return new Date();
	}
}
