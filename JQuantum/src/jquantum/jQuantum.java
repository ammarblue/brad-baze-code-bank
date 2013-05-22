/*
 * jQuantum.java - Main class of the jQuantum computer simulator
 *
 * Copyright (C) 2004-2008 Andreas de Vries
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses
 * or write to the Free Software Foundation,Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA 02110-1301  USA
 */

package jquantum;

import java.awt.Cursor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import jquantum.webstart.BrowserLoader;
import jquantum.webstart.WebStartFileContents;
import jquantum.webstart.WebStartFileHandler;
import jquantum.webstart.WebStartFileHandlerNotAvailableException;

import org.mathIT.quantum.Circuit;

/**
 * This class is the main class of the quantum computer simulator jQuantum. It
 * displays a GUI enabling the input of quantum circuits and visualizes the
 * quantum register during the execution of the resulting quantum algorithms.
 * 
 * @author Andreas de Vries
 * @version 2.3
 */
public class jQuantum extends javax.swing.JFrame {
	private static final long serialVersionUID = -1924726959;
	/** Flag indicating whether a web start is present. */
	private static boolean webStartEnvironment;
	/** Name and version of this program. Current value is {@value} . */
	public final String VERSION = "jQuantum 2.3.1";
	/** Copy right of this program. Current value is {@value} . */
	private final String COPYRIGHT = "&copy; 2004 - 2010 Andreas de Vries under GNU GPL"
			+ "<br> With the help of Dave Boden, Vadim Mirgorod, Axel Thuresson";
	/** The current language of this application. */
	private String language;
	/** Specifies whether the current language menu is initialized. */
	private boolean languageMenuInitialized;
	/** The language dependent resource bundle. */
	private Properties bundle;
	/**
	 * The current width of the circuit panel containing the circuit design
	 * panel and the quantum registers.
	 */
	private int circuitPanelWidth;
	/**
	 * The current height of the circuit panel containing the circuit design
	 * panel and the quantum registers.
	 */
	private int circuitPanelHeight;
	/** The current quantum circuit. */
	private Circuit circuit;

	/**
	 * The filename of the most recently opened file. This is used as a default
	 * value when the user asks to save the file.
	 */
	private String currentlyLoadedFileName;

	/**
	 * This preference stores the directory which has been opened recently for
	 * loading or saving quantum circuits.
	 */
	private final String pref_currentDirectory = "pref_currentDirectory";

	/**
	 * Flag indicating whether the length of a qubit state (as a complex vector)
	 * is encoded by brightness.
	 */
	private boolean lengthBrightening;

	/** Creates new frame jQuantum with current system locale language. */
	public jQuantum() {
		this(null);
	}

	/**
	 * Creates new frame jQuantum with language specified.
	 * 
	 * @param language
	 *            the specified language
	 */
	public jQuantum(String language) {
		webStartEnvironment = false;
		try {
			Class.forName("javax.jnlp.ServiceManager");
			webStartEnvironment = true;
		} catch (ClassNotFoundException ex) {
		}

		if (language == null) {
			this.language = getLocale().getLanguage();
		} else {
			this.language = language;
		}
		openFrame();
	}

	/**
	 * Returns the size of the <i>x</i>-register.
	 * 
	 * @return the size of the <i>x</i>-register
	 */
	public int getXRegisterSize() {
		return circuit.getXRegisterSize();
	}

	/**
	 * Returns the size of the <i>y</i>-register.
	 * 
	 * @return the size of the <i>y</i>-register
	 */
	public int getYRegisterSize() {
		return circuit.getYRegisterSize();
	}

	private void openFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			if (UIManager.getSystemLookAndFeelClassName().equals(
					"javax.swing.plaf.metal.MetalLookAndFeel")) {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			}
		} catch (Exception e) {
			// System.err.println( "Look & Feel Error: " + e.toString() );
		}

		initComponents();

		this.circuitPanelWidth = 810;
		this.circuitPanelHeight = 200;
		this.lengthBrightening = lengthBrighteningItem.isSelected();

		circuitScrollPane.setPreferredSize(new java.awt.Dimension(
				circuitPanelWidth, circuitPanelHeight));
		xScrollPane.setPreferredSize(new java.awt.Dimension(
				circuitPanelWidth - 20, circuitPanelHeight + 28));
		// xScrollPane
		yScrollPane.setPreferredSize(new java.awt.Dimension(
				circuitPanelWidth - 20, circuitPanelHeight - 90));

		deleteButton.setMnemonic(java.awt.event.KeyEvent.VK_BACK_SPACE);
		nextButton.setMnemonic(java.awt.event.KeyEvent.VK_RIGHT);
		prevButton.setMnemonic(java.awt.event.KeyEvent.VK_LEFT);
		goButton.setMnemonic(java.awt.event.KeyEvent.VK_UP);
		restartButton.setMnemonic(java.awt.event.KeyEvent.VK_DOWN);

		circuit = new Circuit();
		circuitPanel = new CircuitPanel(circuit);
		circuitPanel.setPreferredSize(new java.awt.Dimension(circuitPanelWidth,
				circuitPanelHeight - 20));
		circuitPanel.setBackground(new java.awt.Color(255, 255, 255));
		circuitPanel.setBorder(new javax.swing.border.EtchedBorder());
		circuitScrollPane.setViewportView(circuitPanel);

		xPanel = new RegisterPanel(circuitPanelWidth);
		xPanel.setBackground(new java.awt.Color(255, 255, 255));
		xPanel.setBorder(new javax.swing.border.TitledBorder("x-Register"));
		xScrollPane.setViewportView(xPanel);

		yPanel = new RegisterPanel(circuitPanelWidth);
		yPanel.setBackground(new java.awt.Color(255, 255, 255));
		yPanel.setBorder(new javax.swing.border.TitledBorder("y-Register"));
		yScrollPane.setViewportView(yPanel);

		// Language specific entries:
		bundle = new Properties();
		languageMenuInitialized = false;
		setBundle();

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor. <br/>
	 * IMPORTANT! <br/>
	 * Note thst all labels are overwritten by the texts in the resource
	 * bundles.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		controlPanel = new javax.swing.JPanel();
		iniButton = new javax.swing.JButton();
		jSeparator2 = new javax.swing.JSeparator();
		nextButton = new javax.swing.JButton();
		prevButton = new javax.swing.JButton();
		jSeparator4 = new javax.swing.JSeparator();
		goButton = new javax.swing.JButton();
		restartButton = new javax.swing.JButton();
		centerPanel = new javax.swing.JPanel();
		constructPanel = new javax.swing.JPanel();
		gateScrollPane = new javax.swing.JScrollPane();
		gatePanel = new javax.swing.JPanel();
		iniQubitButton = new javax.swing.JButton();
		jSeparator0 = new javax.swing.JSeparator();
		hadamardButton = new javax.swing.JButton();
		cNOTButton = new javax.swing.JButton();
		xButton = new javax.swing.JButton();
		yButton = new javax.swing.JButton();
		zButton = new javax.swing.JButton();
		sButton = new javax.swing.JButton();
		invSButton = new javax.swing.JButton();
		tButton = new javax.swing.JButton();
		sqrtXButton = new javax.swing.JButton();
		toffoliButton = new javax.swing.JButton();
		qftButton = new javax.swing.JButton();
		invQftButton = new javax.swing.JButton();
		U_fButton = new javax.swing.JButton();
		rotationButton = new javax.swing.JButton();
		groverButton = new javax.swing.JButton();
		jSeparator1 = new javax.swing.JSeparator();
		measureButton = new javax.swing.JButton();
		jSeparator3 = new javax.swing.JSeparator();
		deleteButton = new javax.swing.JButton();
		circuitScrollPane = new javax.swing.JScrollPane();
		registerPanel = new javax.swing.JPanel();
		xScrollPane = new javax.swing.JScrollPane();
		yScrollPane = new javax.swing.JScrollPane();
		jMenuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		if (webStartEnvironment) {
			loadExampleMenuItem = new javax.swing.JMenuItem();
		}
		loadMenuItem = new javax.swing.JMenuItem();
		saveMenuItem = new javax.swing.JMenuItem();
		jSeparator5 = new javax.swing.JSeparator();
		exitMenuItem = new javax.swing.JMenuItem();
		configMenu = new javax.swing.JMenu();
		lengthBrighteningItem = new javax.swing.JCheckBoxMenuItem();
		helpMenu = new javax.swing.JMenu();
		aboutMenuItem = new javax.swing.JMenuItem();
		helpMenuItem = new javax.swing.JMenuItem();
		colorMenuItem = new javax.swing.JMenuItem();

		setTitle("jQuantum");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		controlPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Control"));
		controlPanel.setPreferredSize(new java.awt.Dimension(80, 80));

		iniButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/initialize.gif"))); // NOI18N
		iniButton.setToolTipText("Initialize registers");
		iniButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				iniButtonActionPerformed(evt);
			}
		});
		controlPanel.add(iniButton);

		jSeparator2.setPreferredSize(new java.awt.Dimension(66, 2));
		controlPanel.add(jSeparator2);

		nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/next.gif"))); // NOI18N
		nextButton.setToolTipText("Do next step");
		nextButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				nextButtonActionPerformed(evt);
			}
		});
		controlPanel.add(nextButton);

		prevButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/prev.gif"))); // NOI18N
		prevButton.setToolTipText("Do previous step");
		prevButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				prevButtonActionPerformed(evt);
			}
		});
		controlPanel.add(prevButton);

		jSeparator4.setPreferredSize(new java.awt.Dimension(64, 2));
		controlPanel.add(jSeparator4);

		goButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/start.gif"))); // NOI18N
		goButton.setToolTipText("Run complete circuit");
		goButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				goButtonActionPerformed(evt);
			}
		});
		controlPanel.add(goButton);

		restartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/restart.png"))); // NOI18N
		restartButton.setToolTipText("Initialize circuit");
		restartButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				restartButtonActionPerformed(evt);
			}
		});
		controlPanel.add(restartButton);

		getContentPane().add(controlPanel, java.awt.BorderLayout.WEST);

		centerPanel.setLayout(new java.awt.BorderLayout());

		constructPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Circuit Design"));
		constructPanel.setLayout(new java.awt.BorderLayout());

		gateScrollPane
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		gateScrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		gatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		iniQubitButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/jquantum/icons/iniQubit.gif"))); // NOI18N
		iniQubitButton.setToolTipText("Initialize qubits");
		iniQubitButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		iniQubitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				iniQubitButtonActionPerformed(evt);
			}
		});
		gatePanel.add(iniQubitButton);

		jSeparator0.setOrientation(javax.swing.SwingConstants.VERTICAL);
		jSeparator0.setPreferredSize(new java.awt.Dimension(2, 32));
		gatePanel.add(jSeparator0);

		hadamardButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/jquantum/icons/hadamard.gif"))); // NOI18N
		hadamardButton.setToolTipText("Hadamard gate");
		hadamardButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		hadamardButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				hadamardButtonActionPerformed(evt);
			}
		});
		gatePanel.add(hadamardButton);

		cNOTButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/cNOT.gif"))); // NOI18N
		cNOTButton.setToolTipText("c-NOT gate");
		cNOTButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		cNOTButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cNOTButtonActionPerformed(evt);
			}
		});
		gatePanel.add(cNOTButton);

		xButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/X.png"))); // NOI18N
		xButton.setToolTipText("Pauli-X gate (= NOT)");
		xButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		xButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				xButtonActionPerformed(evt);
			}
		});
		gatePanel.add(xButton);

		yButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/Y.png"))); // NOI18N
		yButton.setToolTipText("Pauli-Y gate");
		yButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		yButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				yButtonActionPerformed(evt);
			}
		});
		gatePanel.add(yButton);

		zButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/Z.png"))); // NOI18N
		zButton.setToolTipText("Pauli-Z gate");
		zButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		zButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				zButtonActionPerformed(evt);
			}
		});
		gatePanel.add(zButton);

		sButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/S.png"))); // NOI18N
		sButton.setToolTipText("S gate");
		sButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		sButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sButtonActionPerformed(evt);
			}
		});
		gatePanel.add(sButton);

		invSButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/S_dagger.png"))); // NOI18N
		invSButton.setToolTipText("S* gate");
		invSButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		invSButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				invSButtonActionPerformed(evt);
			}
		});
		gatePanel.add(invSButton);

		tButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/T.png"))); // NOI18N
		tButton.setToolTipText("T gate");
		tButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		tButton.setIconTextGap(0);
		tButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tButtonActionPerformed(evt);
			}
		});
		gatePanel.add(tButton);

		sqrtXButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/sqrtX.png"))); // NOI18N
		sqrtXButton.setToolTipText("sqrt(NOT) gate");
		sqrtXButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		sqrtXButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sqrtXButtonActionPerformed(evt);
			}
		});
		gatePanel.add(sqrtXButton);

		toffoliButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/toffoli.gif"))); // NOI18N
		toffoliButton.setToolTipText("Toffoli gate");
		toffoliButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		toffoliButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				toffoliButtonActionPerformed(evt);
			}
		});
		gatePanel.add(toffoliButton);

		qftButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/qft.gif"))); // NOI18N
		qftButton.setToolTipText("quantum Fourier transform");
		qftButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		qftButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				qftButtonActionPerformed(evt);
			}
		});
		gatePanel.add(qftButton);

		invQftButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/invQft.gif"))); // NOI18N
		invQftButton.setToolTipText("inverse quantum Fourier transform");
		invQftButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		invQftButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				invQftButtonActionPerformed(evt);
			}
		});
		gatePanel.add(invQftButton);

		U_fButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/U_f.gif"))); // NOI18N
		U_fButton.setToolTipText("Function evaluation");
		U_fButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		U_fButton.setEnabled(false);
		U_fButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				U_fButtonActionPerformed(evt);
			}
		});
		gatePanel.add(U_fButton);

		rotationButton.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/jquantum/icons/rotation.gif"))); // NOI18N
		rotationButton.setToolTipText("Rotation operator");
		rotationButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		rotationButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				rotationButtonActionPerformed(evt);
			}
		});
		gatePanel.add(rotationButton);

		groverButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/grover.gif"))); // NOI18N
		groverButton.setToolTipText("Rotation operator");
		groverButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		groverButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				groverButtonActionPerformed(evt);
			}
		});
		gatePanel.add(groverButton);

		jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
		jSeparator1.setPreferredSize(new java.awt.Dimension(2, 32));
		gatePanel.add(jSeparator1);

		measureButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/measurement.gif"))); // NOI18N
		measureButton.setToolTipText("Measurement");
		measureButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		measureButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				measureButtonActionPerformed(evt);
			}
		});
		gatePanel.add(measureButton);

		jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
		jSeparator3.setPreferredSize(new java.awt.Dimension(2, 32));
		gatePanel.add(jSeparator3);

		deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/delete.gif"))); // NOI18N
		deleteButton.setMnemonic('V');
		deleteButton.setToolTipText("Delete last gate (also: 'ALT-BACKSPACE')");
		deleteButton.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		deleteButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteButtonActionPerformed(evt);
			}
		});
		gatePanel.add(deleteButton);

		gateScrollPane.setViewportView(gatePanel);

		constructPanel.add(gateScrollPane, java.awt.BorderLayout.NORTH);

		circuitScrollPane
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		constructPanel.add(circuitScrollPane, java.awt.BorderLayout.SOUTH);

		centerPanel.add(constructPanel, java.awt.BorderLayout.NORTH);

		registerPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Register States"));
		registerPanel.setLayout(new java.awt.BorderLayout());
		registerPanel.add(xScrollPane, java.awt.BorderLayout.CENTER);
		registerPanel.add(yScrollPane, java.awt.BorderLayout.SOUTH);

		centerPanel.add(registerPanel, java.awt.BorderLayout.CENTER);

		getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

		fileMenu.setText("File");

		if (webStartEnvironment) {
			loadExampleMenuItem.setText("Load Example Circuit");
			loadExampleMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							loadExampleMenuItemActionPerformed(evt);
						}
					});
		}
		if (webStartEnvironment) {
			fileMenu.add(loadExampleMenuItem);
		}

		loadMenuItem.setText("Load Circuit");
		loadMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(loadMenuItem);

		saveMenuItem.setText("Save Circuit");
		saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveMenuItem);
		fileMenu.add(jSeparator5);

		exitMenuItem.setText("Quit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(exitMenuItem);

		jMenuBar.add(fileMenu);

		configMenu.setText("Configuration");

		lengthBrighteningItem.setText("length coloring");
		lengthBrighteningItem
				.setToolTipText("Brightness of a qubit state depending on its length?");
		lengthBrighteningItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						lengthBrighteningItemActionPerformed(evt);
					}
				});
		configMenu.add(lengthBrighteningItem);

		jMenuBar.add(configMenu);

		helpMenu.setText("Help ");
		helpMenu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		helpMenu.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

		aboutMenuItem.setText("About");
		aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutMenuItemActionPerformed(evt);
			}
		});
		helpMenu.add(aboutMenuItem);

		helpMenuItem.setText("Help on jQuantum");
		helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				helpMenuItemActionPerformed(evt);
			}
		});
		helpMenu.add(helpMenuItem);

		colorMenuItem.setText("Coloring complex numbers");
		colorMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				colorMenuItemActionPerformed(evt);
			}
		});
		helpMenu.add(colorMenuItem);

		jMenuBar.add(helpMenu);

		setJMenuBar(jMenuBar);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void colorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_colorMenuItemActionPerformed
		new ColorInfoFrame(bundle).setVisible(true);
	}// GEN-LAST:event_colorMenuItemActionPerformed

	private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_aboutMenuItemActionPerformed
		String title;
		title = bundle.getProperty("jQuantum.info.title");
		String output = "<html><h3>";
		output += VERSION;
		output += "</h3>";
		output += "<p>";
		output += bundle.getProperty("jQuantum.info.text");
		output += "</p>";
		output += "<br>";
		output += COPYRIGHT;
		output += "</html>";
		// output = output.replaceAll("\n", ""); // "\n" destroys the HTML
		// format
		new InfoFrame(output, title, 350, 150);
	}// GEN-LAST:event_aboutMenuItemActionPerformed

	private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_helpMenuItemActionPerformed
		int width = 800, height = 550;
		String title;
		String output = "<html>";
		title = bundle.getProperty("jQuantum.help.title");
		output = "<html>";
		output += bundle.getProperty("jQuantum.help.text");
		output += "</html>";
		// output = output.replaceAll("\n", ""); // "\n" destroys the HTML
		// format
		new InfoFrame(output, title, width, height);
	}// GEN-LAST:event_helpMenuItemActionPerformed

	private void lengthBrighteningItemActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_lengthBrighteningItemActionPerformed
		lengthBrightening = lengthBrighteningItem.isSelected();
		xPanel.setLengthBrightening(lengthBrightening);
		yPanel.setLengthBrightening(lengthBrightening);
	}// GEN-LAST:event_lengthBrighteningItemActionPerformed

	private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveMenuItemActionPerformed
		byte[] bytesToSave;
		try {
			bytesToSave = prepareSaveFile();
		} catch (IOException ex) {
			System.err.print("Cannot create file contents: " + ex.getMessage());
			return;
		}

		boolean loadUsingFileChooser = true;
		// Try and get an InputStream either from the Java WebStart API
		// or from a file dialog box.
		if (webStartEnvironment) {
			loadUsingFileChooser = false;
			WebStartFileHandler webStart = new WebStartFileHandler();
			try {
				WebStartFileContents contents = new WebStartFileContents();
				contents.setFileName(currentlyLoadedFileName);
				contents.setInputStream(new ByteArrayInputStream(bytesToSave));
				webStart.saveFileContents(null, contents);
			} catch (IOException ioe) {
				System.err.println("Error loading file: " + ioe.getMessage());
			} catch (WebStartFileHandlerNotAvailableException wsfhnae) {
				// Try loading using the conventional file chooser.
				loadUsingFileChooser = true;
			}
		}

		if (loadUsingFileChooser) {
			String fileDirectoryHint = Preferences.userNodeForPackage(
					getClass()).get(pref_currentDirectory, null);
			JFileChooser fileChooser = new JFileChooser(fileDirectoryHint);
			if (currentlyLoadedFileName != null) {
				fileChooser.setSelectedFile(new File(currentlyLoadedFileName));
			}
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					FileOutputStream fileOutput = new FileOutputStream(file);
					fileOutput.write(bytesToSave);
					fileOutput.flush();
					fileOutput.close();
					Preferences.userNodeForPackage(getClass()).put(
							pref_currentDirectory, file.getParent());
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}// GEN-LAST:event_saveMenuItemActionPerformed

	private byte[] prepareSaveFile() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream output = new ObjectOutputStream(buffer);
		output.writeObject(new int[] { circuit.getXRegisterSize(),
				circuit.getYRegisterSize() });
		output.writeObject(circuit.getGates());
		output.flush();

		return buffer.toByteArray();
	}

	private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_loadMenuItemActionPerformed
		boolean loadUsingFileChooser = true;
		// Try and get an InputStream either from the Java WebStart API
		// or from a file dialog box.
		if (webStartEnvironment) {
			loadUsingFileChooser = false;
			WebStartFileHandler webStart = new WebStartFileHandler();
			try {
				WebStartFileContents contents = webStart.getFileContents(null);
				if (contents != null) {
					loadFile(contents.getFileName(), contents.getInputStream());
					currentlyLoadedFileName = contents.getFileName();
				}
			} catch (IOException ex) {
				System.err.println("Error loading file: " + ex.getMessage());
			} catch (WebStartFileHandlerNotAvailableException ex) {
				// Try loading using the more conventional file chooser.
				loadUsingFileChooser = true;
			}
		}

		if (loadUsingFileChooser) {
			// Not running in Java Web Start environment
			String fileDirectoryHint = Preferences.userNodeForPackage(
					getClass()).get(pref_currentDirectory, null);
			JFileChooser fileChooser = new JFileChooser(fileDirectoryHint);

			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					InputStream fileInput = new FileInputStream(file);
					loadFile(file.getName(), fileInput);
					currentlyLoadedFileName = file.getName();
					Preferences.userNodeForPackage(getClass()).put(
							pref_currentDirectory, file.getParent());
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}// GEN-LAST:event_loadMenuItemActionPerformed

	private void loadExampleMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {
		String examples = "http://jquantum.sourceforge.net/QuantumCircuits/";
		try {
			boolean shown = BrowserLoader.showInBrowser(new URL(examples));
			if (!shown) {
				System.err.println("Browser could not be pointed at: "
						+ examples);
			}
		} catch (MalformedURLException ex) {
			System.err.println("Cannot load examples URL: " + examples);
		}
	}

	@SuppressWarnings("unchecked")
	// reading files cannot be guaranteed by no compiler at all!
	private void loadFile(String fileName, InputStream fileInput) {
		assert fileName != null;
		assert fileInput != null;
		// open the file:
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(fileInput);
			try {
				circuit.initialize((int[]) input.readObject(),
						(ArrayList<org.mathIT.quantum.QuantumGate>) input
								.readObject());

				// if the circuit contains a Grover operator, do length
				// brightening:
				for (org.mathIT.quantum.QuantumGate g : circuit.getGates()) {
					if (g.getName().equals("Grover")) {
						lengthBrightening = true;
						lengthBrighteningItem.setSelected(lengthBrightening);
						break;
					}
				}

				xPanel.setLengthBrightening(lengthBrightening);
				yPanel.setLengthBrightening(lengthBrightening);
				xPanel.setQubitStates(circuit.getXRegister());
				yPanel.setScale(xPanel.getScale());
				yPanel.setQubitStates(circuit.getYRegister(),
						xPanel.getLeftBorder());
				U_fButton.setEnabled(circuit.getYRegister().getSize() > 0);
				circuitPanel.repaint();
			} catch (ClassNotFoundException cnf) {
				String title = bundle.getProperty("jQuantum.loadError.title");
				String message = "<html>" + fileName;
				message += bundle.getProperty("jQuantum.loadError.text");
				message += "<br><br>";
				message += bundle
						.getProperty("jQuantum.loadError.textsupplement");
				message = message.replaceAll("\n", "");

				JOptionPane.showMessageDialog(this, message, title,
						JOptionPane.ERROR_MESSAGE);
			} catch (ClassCastException cce) {
				String title = bundle.getProperty("jQuantum.loadError.title");
				String message = "<html>" + fileName;
				message += bundle.getProperty("jQuantum.loadError.text");
				message = message.replaceAll("\n", "");

				JOptionPane.showMessageDialog(this, message, title,
						JOptionPane.ERROR_MESSAGE);
			} catch (EOFException eof) {
				// do nothing, the stream will be closed in the finally clause
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} catch (IOException ioe) {
			// ioe.printStackTrace();
			String title = bundle.getProperty("jQuantum.loadError.title");
			String message = "<html>" + fileName + " ";
			message += bundle.getProperty("jQuantum.loadError.text");
			message = message.replaceAll("\n", "");

			JOptionPane.showMessageDialog(this, message, title,
					JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exitMenuItemActionPerformed
		dispose();
		try {
			System.exit(0);
		} catch (Exception e) {
			// this may happen if the invoking instance is an applet
		}
	}// GEN-LAST:event_exitMenuItemActionPerformed

	private void iniButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_iniButtonActionPerformed
		InitDialog dialog = new InitDialog(this, true, bundle);
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		int[] values = dialog.getValues();
		int xRegisterSize = values[0];
		int yRegisterSize = values[1];
		int initialState = values[2];
		if (xRegisterSize + yRegisterSize > 0) {
			// initialization successful?
			if (circuit.initialize(xRegisterSize, yRegisterSize, initialState)) {
				xPanel.setLengthBrightening(lengthBrightening);
				xPanel.setQubitStates(circuit.getXRegister());
				yPanel.setLengthBrightening(lengthBrightening);
				yPanel.setScale(xPanel.getScale());

				yPanel.setQubitStates(circuit.getYRegister(),
						xPanel.getLeftBorder());

				U_fButton.setEnabled(yRegisterSize > 0);
				circuitPanel.repaint();
			}
		}
		dialog.dispose();
	}// GEN-LAST:event_iniButtonActionPerformed

	private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_prevButtonActionPerformed
		getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
		if (circuit.getPreviousGate().getName().equals("Measurement")) {
			String message = bundle
					.getProperty("jQuantum.prevButton.errorText");
			String title = bundle.getProperty("errorMessage.title.text");
			JOptionPane.showMessageDialog(this, message, title,
					JOptionPane.ERROR_MESSAGE);
			initializeRegisters(true);
		} else {
			try {
				circuit.setPreviousStep();
				xPanel.setQubitStates(circuit.getXRegister());
				yPanel.setQubitStates(circuit.getYRegister(),
						xPanel.getLeftBorder());
				circuitPanel.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}// GEN-LAST:event_prevButtonActionPerformed

	private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_nextButtonActionPerformed
		getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			circuit.setNextStep();
			xPanel.setQubitStates(circuit.getXRegister());
			yPanel.setQubitStates(circuit.getYRegister(),
					xPanel.getLeftBorder());
			circuitPanel.repaint();
		} catch (java.nio.BufferOverflowException boe) {
			// beo.printStackTrace();
			JOptionPane
					.showMessageDialog(
							this,
							"<html>"
									+ bundle.getProperty("jQuantum.error.yRegisterTooSmall"),
							bundle.getProperty("errorMessage.title.text"),
							JOptionPane.ERROR_MESSAGE);
			initializeRegisters(true);
		} catch (Exception e) {
			e.printStackTrace();
			initializeRegisters(true);
		}
		getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}// GEN-LAST:event_nextButtonActionPerformed

	private void restartButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_restartButtonActionPerformed
		initializeRegisters(true);
	}// GEN-LAST:event_restartButtonActionPerformed

	private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_goButtonActionPerformed
		getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
		try {
			circuit.executeAll();
			circuitPanel.repaint();
		} catch (Exception e) {
			e.getMessage();
		}
		xPanel.setQubitStates(circuit.getXRegister());
		yPanel.setQubitStates(circuit.getYRegister(), xPanel.getLeftBorder());
		getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}// GEN-LAST:event_goButtonActionPerformed

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deleteButtonActionPerformed
		getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
		if (circuit != null) {
			if (circuit.size() > 0) {
				if (circuit.getNextGateNumber() == circuit.size()) {
					if (circuit.getPreviousGate().getName()
							.equals("Measurement")) {
						initializeRegisters(true);
					} else {
						circuit.setPreviousStep();
					}
				}
				circuit.remove(circuit.size() - 1);
				circuitPanel.repaint(); // the remove action cannot be noticed
										// by the circuit panel!
				xPanel.setQubitStates(circuit.getXRegister());
				yPanel.setQubitStates(circuit.getYRegister(),
						xPanel.getLeftBorder());
			}
		}
		getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}// GEN-LAST:event_deleteButtonActionPerformed

	private void iniQubitButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_iniQubitButtonActionPerformed
		getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
		if (circuit.getNumberOfWires() > 0) {
			int[] initialQubits = (circuit.size() > 0) ? circuit.get(0)
					.getQubits() : new int[circuit.getNumberOfWires()];
			InitialStateDialog dialog = new InitialStateDialog(this, true,
					bundle, initialQubits, circuit.getXRegisterSize());
			dialog.setVisible(true);

			if (dialog.cancelButtonClicked) {
				getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				return;
			}
			try {
				circuit.setInitialQubits(dialog.getQubits());
				initializeRegisters(true);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dialog.removeAll();
				dialog.dispose();
				circuitPanel.repaint();
			}
		}
		getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}// GEN-LAST:event_iniQubitButtonActionPerformed

	private void hadamardButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_hadamardButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "Hadamard",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int qubit = dialog.getQubits()[0];
			circuit.addHadamard(qubit, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_hadamardButtonActionPerformed

	private void cNOTButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cNOTButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "cNOT",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int[] qubits = dialog.getQubits();
			circuit.addCNOT(qubits, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_cNOTButtonActionPerformed

	private void xButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_xButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "X",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int qubit = dialog.getQubits()[0];
			circuit.addPauliX(qubit, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_xButtonActionPerformed

	private void yButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_yButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "Y",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int qubit = dialog.getQubits()[0];
			circuit.addPauliY(qubit, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_yButtonActionPerformed

	private void zButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_zButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "Z",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int qubit = dialog.getQubits()[0];
			circuit.addPauliZ(qubit, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_zButtonActionPerformed

	private void sButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "S",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int qubit = dialog.getQubits()[0];
			circuit.addSGate(qubit, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_sButtonActionPerformed

	private void invSButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_invSButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "invS",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int qubit = dialog.getQubits()[0];
			circuit.addInvSGate(qubit, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_invSButtonActionPerformed

	private void tButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "T",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int qubit = dialog.getQubits()[0];
			circuit.addTGate(qubit, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_tButtonActionPerformed

	private void sqrtXButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sqrtXButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "sqrt-X",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int qubit = dialog.getQubits()[0];
			circuit.addSqrtX(qubit, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_sqrtXButtonActionPerformed

	private void toffoliButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_toffoliButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "Toffoli",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int[] qubits = dialog.getQubits();
			circuit.addToffoli(qubits, dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_toffoliButtonActionPerformed

	private void invQftButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_invQftButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "invQFT",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			circuit.addInvQFT(dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_invQftButtonActionPerformed

	private void qftButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_qftButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "QFT",
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			circuit.addQFT(dialog.yRegisterChosen);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_qftButtonActionPerformed

	private void rotationButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_rotationButtonActionPerformed
		RotationDialog dialog = new RotationDialog(this, true, bundle);
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			circuit.addRotation(dialog.qubits, dialog.yRegisterChosen,
					dialog.axis, dialog.phiAsPartOfPi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_rotationButtonActionPerformed

	private void U_fButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_U_fButtonActionPerformed
		FunctionDialog dialog = new FunctionDialog(this, true, bundle);
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			circuit.addFunction(dialog.function);
			circuitPanel.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
		}
	}// GEN-LAST:event_U_fButtonActionPerformed

	private void groverButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_groverButtonActionPerformed
		GateDialog dialog = new GateDialog(this, true, bundle, "Grover",
				circuit.getXRegisterSize(), 0 // circuit.getYRegisterSize()
		);
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		try {
			int needle = dialog.getQubits()[0];
			for (int i = 0; i < dialog.getQubits()[1]; i++) {
				circuit.addGrover(needle);
			}
			// length brightening should be done by default:
			lengthBrightening = true;
			lengthBrighteningItem.setSelected(lengthBrightening);
			xPanel.setLengthBrightening(lengthBrightening);
			yPanel.setLengthBrightening(lengthBrightening);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_groverButtonActionPerformed

	private void measureButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_measureButtonActionPerformed
		MeasureDialog dialog = new MeasureDialog(this, true, bundle,
				circuit.getXRegisterSize(), circuit.getYRegisterSize());
		if (dialog.cancelButtonClicked)
			return;
		dialog.setVisible(true);

		if (dialog.cancelButtonClicked)
			return;
		int[] qubits;
		try {
			if (dialog.xRegisterChosen) {
				qubits = new int[circuit.getXRegisterSize()];
				for (int i = 0; i < circuit.getXRegisterSize(); i++) {
					qubits[i] = i + 1;
				}
				circuit.addMeasurement(qubits, dialog.yRegisterChosen);
			} else if (dialog.yRegisterChosen) {
				qubits = new int[circuit.getYRegisterSize()];
				for (int i = 0; i < circuit.getYRegisterSize(); i++) {
					qubits[i] = i + 1;
				}
				circuit.addMeasurement(qubits, dialog.yRegisterChosen);
			} else if (dialog.qubitChosen) {
				qubits = new int[1];
				qubits[0] = dialog.qubit;
				circuit.addMeasurement(qubits, dialog.yQubitChosen);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dialog.removeAll();
			dialog.dispose();
			circuitPanel.repaint();
		}
	}// GEN-LAST:event_measureButtonActionPerformed

	private void initializeRegisters(boolean setRegisterPanels) {
		circuit.initializeRegisters();
		circuitPanel.repaint();
		if (setRegisterPanels) {
			xPanel.setQubitStates(circuit.getXRegister());
			yPanel.setQubitStates(circuit.getYRegister(),
					xPanel.getLeftBorder());
		}
	}

	/**
	 * Sets the bundle according to the language. If it is not possible, it is
	 * set to English.
	 */
	private void setBundle() {
		java.io.InputStream is = getClass().getResourceAsStream(
				"/jquantum/resources/Bundle_" + language + ".xml");
		if (is == null) { // in case the language resource bundle is not
							// present...
			language = "en"; // English is default
			is = getClass().getResourceAsStream(
					"/jquantum/resources/Bundle_en.xml");
		}
		try {
			// System.out.print("Try to load " + "/jquantum/resources/Bundle_" +
			// language + ".xml ...");
			bundle.loadFromXML(is);
			if (!languageMenuInitialized) {
				initializeLanguageMenu();
			}
			// System.out.println(" successfully!");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		convertToLanguage();
		circuitPanel.setBundle(bundle);
		languageMenuInitialized = true;
	}

	/** Initializes the language menu. */
	private void initializeLanguageMenu() {
		languageMenu = new javax.swing.JMenu();
		languageMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/" + language + ".gif")));

		germanMenuItem = new javax.swing.JMenuItem();
		// The menu items de, en, ru, ...
		germanMenuItem.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/jquantum/icons/de.gif"))); // NOI18N
		germanMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				germanMenuItemActionPerformed();
			}
		});
		languageMenu.add(germanMenuItem);

		englishMenuItem = new javax.swing.JMenuItem();
		englishMenuItem.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/jquantum/icons/en.gif"))); // NOI18N
		englishMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				englishMenuItemActionPerformed();
			}
		});
		languageMenu.add(englishMenuItem);

		russianMenuItem = new javax.swing.JMenuItem();
		russianMenuItem.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/jquantum/icons/ru.gif"))); // NOI18N
		russianMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				russianMenuItemActionPerformed();
			}
		});
		languageMenu.add(russianMenuItem);

		swedishMenuItem = new javax.swing.JMenuItem();
		swedishMenuItem.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/jquantum/icons/sv.gif"))); // NOI18N
		swedishMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				swedishMenuItemActionPerformed();
			}
		});
		languageMenu.add(swedishMenuItem);

		jMenuBar.add(languageMenu);
	}

	private void germanMenuItemActionPerformed() {
		language = "de";
		languageMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/" + language + ".gif")));
		setBundle();
		convertToLanguage();
	}

	private void englishMenuItemActionPerformed() {
		language = "en";
		languageMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/" + language + ".gif")));
		setBundle();
		convertToLanguage();
	}

	private void russianMenuItemActionPerformed() {
		language = "ru";
		languageMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/" + language + ".gif")));
		setBundle();
		convertToLanguage();
	}

	private void swedishMenuItemActionPerformed() {
		language = "sv";
		languageMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/jquantum/icons/" + language + ".gif")));
		setBundle();
		convertToLanguage();
	}

	/** Converts all GUI-Texts to the bundle language. */
	private void convertToLanguage() {
		controlPanel.setBorder(new javax.swing.border.TitledBorder(bundle
				.getProperty("jQuantum.controlPanel.border.title")));
		iniButton.setToolTipText(bundle
				.getProperty("jQuantum.iniButton.toolTipText"));
		nextButton.setToolTipText(bundle
				.getProperty("jQuantum.nextButton.toolTipText"));
		prevButton.setToolTipText(bundle
				.getProperty("jQuantum.prevButton.toolTipText"));
		goButton.setToolTipText(bundle
				.getProperty("jQuantum.goButton.toolTipText"));
		restartButton.setToolTipText(bundle
				.getProperty("jQuantum.restartButton.toolTipText"));
		constructPanel.setBorder(new javax.swing.border.TitledBorder(bundle
				.getProperty("jQuantum.constructPanel.border.title")));
		iniQubitButton.setToolTipText(bundle
				.getProperty("jQuantum.iniQubitButton.toolTipText"));
		hadamardButton.setToolTipText(bundle
				.getProperty("jQuantum.hadamardButton.toolTipText"));
		xButton.setToolTipText(bundle
				.getProperty("jQuantum.xButton.toolTipText"));
		yButton.setToolTipText(bundle
				.getProperty("jQuantum.yButton.toolTipText"));
		zButton.setToolTipText(bundle
				.getProperty("jQuantum.zButton.toolTipText"));
		sButton.setToolTipText(bundle
				.getProperty("jQuantum.sButton.toolTipText"));
		invSButton.setToolTipText(bundle
				.getProperty("jQuantum.invSButton.toolTipText"));
		tButton.setToolTipText(bundle
				.getProperty("jQuantum.tButton.toolTipText"));
		sqrtXButton.setToolTipText(bundle
				.getProperty("jQuantum.sqrtXButton.toolTipText"));
		cNOTButton.setToolTipText(bundle
				.getProperty("jQuantum.cNOTButton.toolTipText"));
		toffoliButton.setToolTipText(bundle
				.getProperty("jQuantum.toffoliButton.toolTipText"));
		qftButton.setToolTipText(bundle
				.getProperty("jQuantum.qftButton.toolTipText"));
		invQftButton.setToolTipText(bundle
				.getProperty("jQuantum.invQFTButton.toolTipText"));
		U_fButton.setToolTipText(bundle
				.getProperty("jQuantum.U_fButton.toolTipText"));
		groverButton.setToolTipText(bundle
				.getProperty("jQuantum.groverButton.toolTipText"));
		rotationButton.setToolTipText(bundle
				.getProperty("jQuantum.rotationButton.toolTipText"));
		measureButton.setToolTipText(bundle
				.getProperty("jQuantum.measureButton.toolTipText"));
		deleteButton.setToolTipText(bundle
				.getProperty("jQuantum.deleteButton.toolTipText"));
		registerPanel.setBorder(new javax.swing.border.TitledBorder(bundle
				.getProperty("jQuantum.registerPanel.border.title")));
		xPanel.setBorder(new javax.swing.border.TitledBorder("x-"
				+ bundle.getProperty("Register.text")));
		yPanel.setBorder(new javax.swing.border.TitledBorder("y-"
				+ bundle.getProperty("Register.text")));

		fileMenu.setText(bundle.getProperty("jQuantum.fileMenu.text"));
		loadMenuItem.setText(bundle.getProperty("jQuantum.loadMenuItem.text"));
		if (webStartEnvironment) {
			loadExampleMenuItem.setText(bundle
					.getProperty("jQuantum.loadExampleMenuItem.text"));
		}
		saveMenuItem.setText(bundle.getProperty("jQuantum.saveMenuItem.text"));
		exitMenuItem.setText(bundle.getProperty("jQuantum.exitMenuItem.text"));
		configMenu.setText(bundle.getProperty("jQuantum.configMenu.text"));
		lengthBrighteningItem.setToolTipText(bundle
				.getProperty("jQuantum.lengthBrighteningItem.toolTipText"));
		lengthBrighteningItem.setText(bundle
				.getProperty("jQuantum.lengthBrighteningMenuItem.text"));
		helpMenu.setText(bundle.getProperty("jQuantum.helpMenu.text"));
		aboutMenuItem
				.setText(bundle.getProperty("jQuantum.aboutMenuItem.text"));
		helpMenuItem.setText(bundle.getProperty("jQuantum.helpMenuItem.text"));
		colorMenuItem
				.setText(bundle.getProperty("jQuantum.colorMenuItem.text"));
	}

	/**
	 * Paints the register panels.
	 * 
	 * @param g
	 *            the specified Graphics window
	 */
	@Override
	public void paint(java.awt.Graphics g) {
		xScrollPane.setSize(registerPanel.getWidth() - 20,
				registerPanel.getHeight() - yScrollPane.getHeight() - 35);
		super.paint(g);
	}

	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_exitForm
		dispose();
		try {
			System.exit(0);
		} catch (Exception e) {
			// this may happen if the invoking instance is an applet
		}
	}// GEN-LAST:event_exitForm

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		jQuantum jq = new jQuantum();
		if (args.length == 2 && args[0].equals("-open")) {
			String filestring = args[1];
			try {
				File f = new File(filestring);
				if (webStartEnvironment) {
					WebStartFileHandler handler = new WebStartFileHandler();
					try {
						WebStartFileContents contents = handler.openFile(f);
						jq.loadFile(contents.getFileName(),
								contents.getInputStream());
					} catch (WebStartFileHandlerNotAvailableException ex) {
						System.err.println("Could not open file: " + filestring
								+ " - " + ex.getMessage());
					} catch (IOException ex) {
						System.err.println("Could not open file: " + filestring
								+ " - " + ex.getMessage());
					}
				} else {
					String name = f.getName();
					InputStream in = new FileInputStream(f);
					jq.loadFile(name, in);
				}
			} catch (FileNotFoundException ex) {
				System.out.println("Couldn't find: " + filestring);
			}
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton U_fButton;
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JButton cNOTButton;
	private javax.swing.JPanel centerPanel;
	private javax.swing.JScrollPane circuitScrollPane;
	private javax.swing.JMenuItem colorMenuItem;
	private javax.swing.JMenu configMenu;
	private javax.swing.JPanel constructPanel;
	private javax.swing.JPanel controlPanel;
	private javax.swing.JButton deleteButton;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JPanel gatePanel;
	private javax.swing.JScrollPane gateScrollPane;
	private javax.swing.JButton goButton;
	private javax.swing.JButton groverButton;
	private javax.swing.JButton hadamardButton;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JMenuItem helpMenuItem;
	private javax.swing.JButton iniButton;
	private javax.swing.JButton iniQubitButton;
	private javax.swing.JButton invQftButton;
	private javax.swing.JButton invSButton;
	private javax.swing.JMenuBar jMenuBar;
	private javax.swing.JSeparator jSeparator0;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JSeparator jSeparator3;
	private javax.swing.JSeparator jSeparator4;
	private javax.swing.JSeparator jSeparator5;
	private javax.swing.JCheckBoxMenuItem lengthBrighteningItem;
	private javax.swing.JMenuItem loadExampleMenuItem;
	private javax.swing.JMenuItem loadMenuItem;
	private javax.swing.JButton measureButton;
	private javax.swing.JButton nextButton;
	private javax.swing.JButton prevButton;
	private javax.swing.JButton qftButton;
	private javax.swing.JPanel registerPanel;
	private javax.swing.JButton restartButton;
	private javax.swing.JButton rotationButton;
	private javax.swing.JButton sButton;
	private javax.swing.JMenuItem saveMenuItem;
	private javax.swing.JButton sqrtXButton;
	private javax.swing.JButton tButton;
	private javax.swing.JButton toffoliButton;
	private javax.swing.JButton xButton;
	private javax.swing.JScrollPane xScrollPane;
	private javax.swing.JButton yButton;
	private javax.swing.JScrollPane yScrollPane;
	private javax.swing.JButton zButton;
	// End of variables declaration//GEN-END:variables

	private javax.swing.JMenu languageMenu;
	private javax.swing.JMenuItem englishMenuItem;
	private javax.swing.JMenuItem germanMenuItem;
	private javax.swing.JMenuItem russianMenuItem;
	private javax.swing.JMenuItem swedishMenuItem;

	private CircuitPanel circuitPanel;
	private RegisterPanel xPanel;
	private RegisterPanel yPanel;
}
