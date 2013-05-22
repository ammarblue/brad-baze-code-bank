/*
 * GateDialog.java - Dialog class of the jQuantum computer simulator
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

import java.util.Properties;
import javax.swing.*;

/**
 * This class enables to input parameter values for quantum gates to be added to
 * a quantum circuit.
 * 
 * @author Andreas de Vries
 * @version 1.4
 */
public class GateDialog extends javax.swing.JDialog {
	private static final long serialVersionUID = 1495862803;
	private Properties bundle;
	private String gate;
	/** The size of the <i>x</i>-register. */
	private int xRegisterSize;
	/** The size of the <i>y</i>-register. */
	private int yRegisterSize;
	private int[] qubits;
	/** Flag indicating whether the <i>y</i>-register is chosen to be modofied. */
	public boolean yRegisterChosen = false;
	/** Flag indicating whether the cancel button has been pressed. */
	public boolean cancelButtonClicked = false;

	/** Creates new form InitDialog */
	public GateDialog(java.awt.Frame parent, boolean modal, Properties bundle,
			String gate, int xRegisterSize, int yRegisterSize) {
		super(parent, modal);
		this.bundle = bundle;
		this.gate = gate;
		this.xRegisterSize = xRegisterSize;
		this.yRegisterSize = yRegisterSize;

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

		xRadioButton.setText("<html><i>x</i>-"
				+ bundle.getProperty("Register.text") + "</html>");
		yRadioButton.setText("<html><i>y</i>-"
				+ bundle.getProperty("Register.text") + "</html>");

		if (yRegisterSize == 0) {
			yRadioButton.setEnabled(false);
		}

		if (gate.equalsIgnoreCase("Hadamard")) {
			qubits = new int[1];
		} else if (gate.equalsIgnoreCase("cNOT")) {
			qubits = new int[2];
		} else if (gate.equalsIgnoreCase("X") || gate.equalsIgnoreCase("Y")
				|| gate.equalsIgnoreCase("Z") || gate.equalsIgnoreCase("S")
				|| gate.equalsIgnoreCase("invS") || gate.equalsIgnoreCase("T")) {
			qubits = new int[1];
		} else if (gate.equalsIgnoreCase("sqrt-X")) {
			qubits = new int[1];
		} else if (gate.equalsIgnoreCase("Toffoli")) {
			qubits = new int[3];
		} else if (gate.equalsIgnoreCase("QFT")
				|| gate.equalsIgnoreCase("invQFT")) {
			qubits = new int[0];
		} else if (gate.equalsIgnoreCase("Grover")) {
			qubits = new int[2];
		}

		for (int i = 0; i < qubits.length; i++) {
			qubits[i] = i + 1;
		}

		if (gate.equalsIgnoreCase("Grover")) {
			qubits[1] = org.mathIT.quantum.Register.groverSteps(xRegisterSize);
		}

		if (qubits.length > 0) {
			innerInputPanel.setBorder(new javax.swing.border.EtchedBorder());
		}
		innerInputPanel.setLayout(new java.awt.GridLayout(qubits.length, 2));
		qubitPanel = new javax.swing.JPanel[qubits.length];
		qubitLabel = new javax.swing.JLabel();
		qubitLabel.setText(bundle.getProperty("qubitIndex.text") + ": ");
		qubitTextField = new JTextField[qubits.length];

		for (int i = 0; i < qubits.length; i++) {

			if (i < qubits.length - 1) {
				if (gate.equalsIgnoreCase("Grover")) {
					innerInputPanel.add(new javax.swing.JLabel(bundle
							.getProperty("GateDialog.Grover.needle.text")
							+ ": ", SwingConstants.TRAILING));
				} else {
					innerInputPanel.add(new javax.swing.JLabel(bundle
							.getProperty("controlQubit.text") + ": ",
							SwingConstants.TRAILING));
				}
			} else {
				if (gate.equalsIgnoreCase("Grover")) {
					innerInputPanel.add(new javax.swing.JLabel(bundle
							.getProperty("GateDialog.Grover.number.text")
							+ ": ", SwingConstants.TRAILING));
				} else {
					innerInputPanel.add(new javax.swing.JLabel(bundle
							.getProperty("targetQubit.text") + ": ",
							SwingConstants.TRAILING));
				}
			}

			qubitTextField[i] = new javax.swing.JTextField();
			qubitTextField[i].setColumns(3);
			qubitTextField[i].setText("" + qubits[i]);

			qubitTextField[i]
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							qubitTextFieldActionPerformed(evt);
						}
					});

			qubitPanel[i] = new javax.swing.JPanel();
			qubitPanel[i].add(qubitTextField[i]);

			innerInputPanel.add(qubitPanel[i]);
		}

		if (gate.equalsIgnoreCase("Grover")) {
			questionLabel.setText(bundle
					.getProperty("GateDialog.Grover.questionLabel.text"));
		} else {
			questionLabel.setText(bundle
					.getProperty("GateDialog.questionLabel.text"));
		}
		okButton.setText(bundle.getProperty("okButton.text"));
		cancelButton.setText(bundle.getProperty("cancelButton.text"));

		pack();

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setLocation((screenSize.width - getSize().width) / 2,
				(screenSize.height - getSize().height) / 2);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		buttonGroup = new javax.swing.ButtonGroup();
		headPanel = new javax.swing.JPanel();
		questionLabel = new javax.swing.JLabel();
		inputPanel = new javax.swing.JPanel();
		innerInputPanel = new javax.swing.JPanel();
		radioPanel = new javax.swing.JPanel();
		xRadioButton = new javax.swing.JRadioButton();
		yRadioButton = new javax.swing.JRadioButton();
		buttonPanel = new javax.swing.JPanel();
		okButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});

		questionLabel.setText("Please input the qubit(s) being acted on:");
		headPanel.add(questionLabel);

		getContentPane().add(headPanel, java.awt.BorderLayout.NORTH);

		inputPanel.setLayout(new java.awt.BorderLayout());

		inputPanel.add(innerInputPanel, java.awt.BorderLayout.CENTER);

		radioPanel.setLayout(new java.awt.BorderLayout());

		radioPanel.setBorder(new javax.swing.border.EtchedBorder());
		xRadioButton.setSelected(true);
		xRadioButton.setText("<html><i>x</i>-Register</html>");
		buttonGroup.add(xRadioButton);
		xRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		xRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				xRadioButtonActionPerformed(evt);
			}
		});

		radioPanel.add(xRadioButton, java.awt.BorderLayout.NORTH);

		yRadioButton.setText("<html><i>y</i>-Register</html>");
		buttonGroup.add(yRadioButton);
		yRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		yRadioButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				yRadioButtonActionPerformed(evt);
			}
		});

		radioPanel.add(yRadioButton, java.awt.BorderLayout.SOUTH);

		inputPanel.add(radioPanel, java.awt.BorderLayout.SOUTH);

		getContentPane().add(inputPanel, java.awt.BorderLayout.CENTER);

		okButton.setText(" OK ");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(okButton);

		cancelButton.setText(" Cancel ");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(cancelButton);

		getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

		pack();
	}// GEN-END:initComponents

	private void yRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_yRadioButtonActionPerformed
		yRegisterChosen = yRadioButton.isSelected();
	}// GEN-LAST:event_yRadioButtonActionPerformed

	private void xRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_xRadioButtonActionPerformed
		yRegisterChosen = yRadioButton.isSelected();
	}// GEN-LAST:event_xRadioButtonActionPerformed

	private void qubitTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
		setValues();
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed
		cancelButtonClicked = true;
		setVisible(false);
		removeAll();
		dispose();
	}// GEN-LAST:event_cancelButtonActionPerformed

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okButtonActionPerformed
		setValues();
	}// GEN-LAST:event_okButtonActionPerformed

	/**
	 * Returns an array of the qubit values.
	 * 
	 * @return an array of the qubit values
	 */
	public int[] getQubits() {
		return qubits;
	}

	private void setValues() {
		int j = 0;
		try {
			String text = bundle.getProperty("GateDialog.errorMessage_2");
			String title = bundle.getProperty("errorMessage.title.text");
			if (gate.equalsIgnoreCase("Hadamard")) {
				if (xRadioButton.isSelected() && xRegisterSize < 1) {
					JOptionPane.showMessageDialog(this, "x-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (yRadioButton.isSelected() && yRegisterSize < 1) {
					JOptionPane.showMessageDialog(this, "y-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else if (gate.equalsIgnoreCase("cNOT")) {
				if (xRadioButton.isSelected() && xRegisterSize < 2) {
					JOptionPane.showMessageDialog(this, "x-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (yRadioButton.isSelected() && yRegisterSize < 2) {
					JOptionPane.showMessageDialog(this, "y-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else if (gate.equalsIgnoreCase("X") || gate.equalsIgnoreCase("Y")
					|| gate.equalsIgnoreCase("Z") || gate.equalsIgnoreCase("S")
					|| gate.equalsIgnoreCase("T")) {
				if (xRadioButton.isSelected() && xRegisterSize < 1) {
					JOptionPane.showMessageDialog(this, "x-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (yRadioButton.isSelected() && yRegisterSize < 1) {
					JOptionPane.showMessageDialog(this, "y-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else if (gate.equalsIgnoreCase("Toffoli")) {
				if (xRadioButton.isSelected() && xRegisterSize < 3) {
					JOptionPane.showMessageDialog(this, "x-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (yRadioButton.isSelected() && yRegisterSize < 3) {
					JOptionPane.showMessageDialog(this, "y-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else if (gate.equalsIgnoreCase("QFT")) {
				if (xRadioButton.isSelected() && xRegisterSize < 1) {
					JOptionPane.showMessageDialog(this, "x-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (yRadioButton.isSelected() && yRegisterSize < 1) {
					JOptionPane.showMessageDialog(this, "y-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else if (gate.equalsIgnoreCase("invQFT")) {
				if (xRadioButton.isSelected() && xRegisterSize < 1) {
					JOptionPane.showMessageDialog(this, "x-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (yRadioButton.isSelected() && yRegisterSize < 1) {
					JOptionPane.showMessageDialog(this, "y-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else if (gate.equalsIgnoreCase("Grover")) {
				if (xRegisterSize < 1) {
					JOptionPane.showMessageDialog(this, "x-" + text + "!",
							title, JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			while (j < qubits.length) {
				qubits[j] = Integer.parseInt(qubitTextField[j].getText());
				j++;
			}
			boolean error = false;
			int iMax = xRegisterSize;
			if (iMax > qubits.length)
				iMax = qubits.length;
			for (int i = 0; i < iMax; i++) {
				error |= gate.equalsIgnoreCase("Grover") ? qubits[i] >= (1 << xRegisterSize)
						: qubits[i] > xRegisterSize;
			}
			error = xRadioButton.isSelected() && error;
			if (error) {
				JOptionPane.showMessageDialog(this, "x-" + text + "!", title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			error = false;
			iMax = yRegisterSize;
			if (iMax > qubits.length)
				iMax = qubits.length;
			for (int i = 0; i < qubits.length; i++) {
				error |= qubits[i] > yRegisterSize;
			}
			error = yRadioButton.isSelected() && error;
			if (error) {
				JOptionPane.showMessageDialog(this, "y-" + text + "!", title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			cancelButtonClicked = false;
			setVisible(false);
		} catch (Exception e) {
			String message = bundle.getProperty("GateDialog.errorMessage_1")
					+ " " + (j + 1);
			JOptionPane.showMessageDialog(null, message);
		}
	}

	/** Closes the dialog */
	private void closeDialog(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_closeDialog
		cancelButtonClicked = true;
		setVisible(false);
		removeAll();
		dispose();
	}// GEN-LAST:event_closeDialog

	/*
	 * public static void main(String args[]) {
	 * java.awt.EventQueue.invokeLater(new Runnable() { public void run() {
	 * String language = "ru"; //java.util.Locale.getDefault().getLanguage();
	 * java.util.Properties bundle = new java.util.Properties(); try {
	 * System.out.print("Try to load " + "/jquantum/resources/Bundle_" +
	 * language + ".xml ...");
	 * bundle.loadFromXML(getClass().getResourceAsStream(
	 * "/jquantum/resources/Bundle_" + language + ".xml"));
	 * System.out.println(" successfully!"); } catch(Exception e) { try {
	 * bundle.loadFromXML(getClass().getResourceAsStream(
	 * "/jquantum/resources/Bundle_en.xml"));
	 * System.out.println(" but loaded /jquantum/Bundle_en.xml!"); } catch
	 * (Exception e2) { e2.printStackTrace(); } } new GateDialog(new
	 * javax.swing.JFrame(), true, bundle, "Grover", 8, 0).setVisible(true);
	 * System.exit(0); } }); } //
	 */

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.ButtonGroup buttonGroup;
	private javax.swing.JPanel buttonPanel;
	private javax.swing.JButton cancelButton;
	private javax.swing.JPanel headPanel;
	private javax.swing.JPanel innerInputPanel;
	private javax.swing.JPanel inputPanel;
	private javax.swing.JButton okButton;
	private javax.swing.JLabel questionLabel;
	private javax.swing.JPanel radioPanel;
	private javax.swing.JRadioButton xRadioButton;
	private javax.swing.JRadioButton yRadioButton;
	// End of variables declaration//GEN-END:variables
	private JLabel qubitLabel;
	private JPanel[] qubitPanel;
	private JTextField[] qubitTextField;

}
