// SOS (Simple Operating System)
// Java version 1.1 (January 2001)
//
// Author: Charles Crowley
// email: crowley@cs.unm.edu
// web page: www.cs.unm.edu/~crowley
// phone: 505-277-5446
//
// SIM.java
//
//   The class is the main program for the simulator.
//   It handles the simulation GUI and starts the simulation running.
//

//--------------------------------------------------------------------
// Imports
//--------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.Border;

//--------------------------------------------------------------------
// The SOS virtual machine simulator
//--------------------------------------------------------------------
public class SIM extends Applet {

	// --------------------------------------------------------------------
	// Tracing constants
	// --------------------------------------------------------------------
	static final int TraceApp = 0x1;
	static final int TraceHWDisk = 0x2;
	static final int TraceHWTimer = 0x4;
	static final int TraceHWSim = 0x8;
	static final int TraceHW = 0xE;
	static final int TraceSIM = 0x10;
	static final int TraceSOSDisk = 0x20;
	static final int TraceSOSPM = 0x40;
	static final int TraceSOSStart = 0x80;
	static final int TraceSOSSyscall = 0x100;
	static final int TraceSOS = 0x1E0;
	static final int TraceAlways = -1;

	// ------------------------------------------------------------------
	// Memory layout of the (simulated) physical memory
	// ------------------------------------------------------------------

	// Interrupt Vectors
	static final int SyscallIntVector = 0;
	static final int TimerIntVector = 1;
	static final int DiskIntIntVector = 2;
	static final int ProgErrIntIntVector = 3;

	// Disk registers
	static final int DiskControlRegister = 10;
	static final int DiskAddressRegister = 11;
	static final int DiskStatusRegister = 12;

	// Disk buffers
	static final int SystemDiskBuffer = 300;
	static final int EndSystemDiskBuffer = 427;

	// Message buffers
	static final int MessageBufferArea = 500; // 50 buffers of 8 words each
	static final int EndMessageBufferArea = 899;

	// Process memory areas
	static final int Process1Memory = 1000;
	static final int Process2Memory = 2000;
	static final int Process3Memory = 3000;
	static final int Process4Memory = 4000;
	static final int Process5Memory = 5000;
	static final int Process6Memory = 6000;
	static final int Process7Memory = 7000;
	static final int Process8Memory = 8000;

	// --------------------------------------------------------------------
	// ***** Public Class data *****
	// --------------------------------------------------------------------
	static public SIM TheSIM;
	static public SOSData sosData;
	static public HWSimulation hw;
	public PrintStream pout;
	public SIMPauser sosPauser = new SIMPauser();
	public Vector threadList = new Vector();//TODO change to Queue
	public JTabbedPane proc_tabs;

	// --------------------------------------------------------------------
	// ***** Private Class data *****
	// --------------------------------------------------------------------
	private long startTime;
	private boolean isApplet = true;
	private boolean sosStarted = false;
	private DefaultListModel proc0_model;
	private JList proc0_trace;
	private JScrollPane proc0_scroll;
	private int trace_mask = SIM.TraceApp | SIM.TraceSOSStart;

	// --------------------------------------------------------------------
	// ***** Trace function *****
	// --------------------------------------------------------------------
	static public void Trace(int trace_type, String msg) {
		if ((trace_type & SIM.TheSIM.trace_mask) == 0)
			// we are not recording this type of trace message
			return;
		int eIndex=Thread.currentThread().toString().indexOf(",4,file");
		TheSIM.TraceMessage(Thread.currentThread().toString().substring(7,eIndex)
				+ "@" + (System.currentTimeMillis() - SIM.TheSIM.startTime)
				+ ": " + msg);
		if (!SIM.TheSIM.isApplet)
			SIM.TheSIM.pout.println(msg);
		String msg1 = "@" + (System.currentTimeMillis() - SIM.TheSIM.startTime)
				+ ": " + msg;
		int cur_proc = SIM.sosData.current_process;
		if (cur_proc > 0)
			SIM.hw.ProcessTrace[cur_proc].addElement(msg1);
		else
			SIM.TheSIM.proc0_model.addElement(msg1);
	}

	// --------------------------------------------------------------------------
	// init function
	// --------------------------------------------------------------------------
	public void init() {
		// Get the starting time
		startTime = System.currentTimeMillis();

		// This is only necessary for applets
		if (isApplet) {
			TheSIM = this;
			this.resize(700, 400);
		}

		// Create a file to write tracing output to
		if (!isApplet) {
			try {
				FileOutputStream fout = new FileOutputStream("sim.out");
				SIM.TheSIM.pout = new PrintStream(fout, true);
			} catch (IOException e) {
				System.out
						.println("Could not create output print stream for sim.out");
			}
		}

		// allocate the global data and the SOS handlers
		sosData = new SOSData();

		// Initialize the simulated hardware
		hw = new HWSimulation();

		// Disply the welcome messages
		Trace(TraceAlways, "Welcome to the SOS simulator");
		Trace(TraceAlways,
				"Java version is " + System.getProperty("java.version"));
		Trace(TraceAlways,
				"Java vendor is " + System.getProperty("java.vendor"));
		if (!isApplet) {
			Trace(TraceAlways,
					"Java home is " + System.getProperty("java.home"));
			Trace(TraceAlways,
					"Java CLASSPATH is "
							+ System.getProperty("java.class.path"));
		}
	}

	// --------------------------------------------------------------------------
	// main function
	// --------------------------------------------------------------------------

	public static void main(String[] args) {

		// Create the simulation object
		TheSIM = new SIM();
		SIM.TheSIM.isApplet = false;
		
		JFrame TheFrame = new JFrame("SIM");

		// Handle window closing
		TheFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Set up the frame
		TheFrame.getContentPane().add(TheSIM, BorderLayout.CENTER);
		TheFrame.setSize(800, 500);
		TheSIM.init();
		TheSIM.start();
		TheFrame.setVisible(true);
	}

	// --------------------------------------------------------------------------
	// GUI components
	// --------------------------------------------------------------------------

	// Top row of buttons and panel
	JPanel top_buttons;
	JButton bstart;
	JButton bpause;
	JButton bresume;
	JButton bexit;

	// Application choice radio buttons and panel
	JPanel app_options;
	ButtonGroup app_group;
	JRadioButton rb_2gui;
	JRadioButton rb_msg;
	JRadioButton rb_disk;

	// Trace options checkboxes and panel
	JPanel trace_options;
	JCheckBox cb_app;
	JCheckBox cb_hw;
	JCheckBox cb_sim;
	JCheckBox cb_syscall;
	JCheckBox cb_pm;
	JCheckBox cb_disk;

	// Main trace list
	DefaultListModel trace_list_model;
	JList trace_list;
	JScrollPane trace_scroll;

	// Middle row of buttons and panel
	JPanel MiddleButtons;
	JButton bnext;
	JButton bp0;
	JButton bp1;
	JButton bp2;
	JButton bp3;
	JButton bp4;

	// --------------------------------------------------------------------------
	// The SIM constructor
	// --------------------------------------------------------------------------

	public SIM() {

		// Make the app options radio buttons
		app_options = new JPanel();
		app_options.setBorder(BorderFactory.createLineBorder(Color.black));
		app_group = new ButtonGroup();
		add(app_options);

		rb_2gui = new JRadioButton("2 GUI Apps");
		app_group.add(rb_2gui);
		rb_2gui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sosData.initialProcessNumber = 1;
			}
		});
		app_options.add(rb_2gui);

		rb_msg = new JRadioButton("Msg App");
		app_group.add(rb_msg);
		rb_msg.setSelected(true);
		rb_2gui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sosData.initialProcessNumber = 2;
			}
		});
		app_options.add(rb_msg);

		rb_disk = new JRadioButton("Disk App");
		app_group.add(rb_disk);
		rb_2gui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sosData.initialProcessNumber = 4;
			}
		});
		app_options.add(rb_disk);

		// Make the trace option check boxes
		trace_options = new JPanel();
		trace_options.setBorder(BorderFactory.createLineBorder(Color.black));
		add(trace_options);

		cb_app = new JCheckBox("App");
		if ((trace_mask & TraceApp) == TraceApp)
			cb_app.setSelected(true);
		cb_app.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					trace_mask |= TraceApp;
				else
					trace_mask &= ~TraceApp;
			}
		});
		trace_options.add(cb_app);

		cb_hw = new JCheckBox("HW");
		if ((trace_mask & TraceHW) == TraceHW)
			cb_hw.setSelected(true);
		cb_hw.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					trace_mask |= TraceHW;
				else
					trace_mask &= ~TraceHW;
			}
		});
		trace_options.add(cb_hw);

		cb_sim = new JCheckBox("SIM");
		if ((trace_mask & TraceSIM) == TraceSIM)
			cb_sim.setSelected(true);
		cb_sim.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					trace_mask |= TraceSIM;
				else
					trace_mask &= ~TraceSIM;
			}
		});
		trace_options.add(cb_sim);

		cb_syscall = new JCheckBox("Syscall");
		if ((trace_mask & TraceSOSSyscall) == TraceSOSSyscall)
			cb_syscall.setSelected(true);
		cb_syscall.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					trace_mask |= TraceSOSSyscall;
				else
					trace_mask &= ~TraceSOSSyscall;
			}
		});
		trace_options.add(cb_syscall);

		cb_pm = new JCheckBox("PM");
		if ((trace_mask & TraceSOSPM) == TraceSOSPM)
			cb_pm.setSelected(true);
		cb_pm.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					trace_mask |= TraceSOSPM;
				else
					trace_mask &= ~TraceSOSPM;
			}
		});
		trace_options.add(cb_pm);

		cb_disk = new JCheckBox("Disk");
		if ((trace_mask & TraceSOSDisk) == TraceSOSDisk)
			cb_disk.setSelected(true);
		cb_disk.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					trace_mask |= TraceSOSDisk;
				else
					trace_mask &= ~TraceSOSDisk;
			}
		});
		trace_options.add(cb_disk);

		// Make the top buttons panel
		top_buttons = new JPanel();
		top_buttons.setBorder(BorderFactory.createLineBorder(Color.black));
		add(top_buttons);

		bstart = new JButton("Start SOS");
		bstart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sosStarted) {
					// Don't allow starting SOS twice
					SIM.Trace(TraceAlways,
							"SOS already started");
				} else {
					// Start with a thread that executes the initialization code
					Thread t = new Thread(new SOSStart());
					threadList.addElement(t);
					t.setPriority(Thread.NORM_PRIORITY + 1);
					SIM.Trace(TraceSIM, "Starting SOS");
					t.start();
					SIM.Trace(TraceSIM, "SOS Started");
					sosStarted = true;
				}
			}
		});
		top_buttons.add(bstart);

		bpause = new JButton("Pause SOS");
		bpause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!sosPauser.isPaused()) {
					SIM.Trace(TraceAlways, "Pausing SOS");
					sosPauser.pause();
					hw.theDiskThread.suspend();
					hw.hwTimerThread.suspend();
				}
			}
		});
		top_buttons.add(bpause);

		bresume = new JButton("Resume SOS");
		bresume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sosPauser.isPaused()) {
					SIM.Trace(TraceAlways, "Resuming SOS");
					sosPauser.unpause();
					hw.theDiskThread.resume();
					hw.hwTimerThread.resume();
				}
			}
		});
		top_buttons.add(bresume);

		bexit = new JButton("Exit Simulator");
		bexit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isApplet) {
					Trace(TraceAlways,
							"*** Unload the page to exit the applet ***");
					System.exit(0);
				} else {
					System.exit(0);
				}
			}
		});
		top_buttons.add(bexit);

		// Make the main trace list
		trace_list_model = new DefaultListModel();
		trace_list = new JList(trace_list_model);
		trace_scroll = new JScrollPane(trace_list);
		trace_scroll.setBorder(BorderFactory.createLineBorder(Color.black));
	    trace_scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {  
	            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	        }
	    });
		add(trace_scroll);

		// Make the tabbed pne for the process traces
		proc_tabs = new JTabbedPane();
		add(proc_tabs);

		// Make the proc0 tab
		// JPanel p = new JPanel();
		proc0_model = new DefaultListModel();
		proc0_trace = new JList(proc0_model);
		proc0_scroll = new JScrollPane(proc0_trace);
	    proc0_scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {  
	            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	        }
	    });
		// p.add(proc0_scroll);
		proc_tabs.addTab("Proc0", null, proc0_scroll, "Trace for process 0");
	}

	synchronized public void TraceMessage(String msg) {
		trace_list_model.addElement(msg);
	}
}
