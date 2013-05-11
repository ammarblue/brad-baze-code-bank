package reuze.app;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import reuze.awt.vgu_CountPanel;
import reuze.awt.vgu_EditorPanel;
import reuze.awt.vgu_EffectPanel;
import reuze.awt.vgu_GradientPanel;
import reuze.awt.vgu_ImagePanel;
import reuze.awt.vgu_NumericPanel;
import reuze.awt.vgu_OptionsPanel;
import reuze.awt.vgu_PercentagePanel;
import reuze.awt.vgu_RangedNumericPanel;
import reuze.awt.vgu_ScaledNumericPanel;
import reuze.awt.vgu_SpawnPanel;
import reuze.awt.vgu_SpriteAWT;

import com.software.reuze.dg_ParticleEmitter;
import com.software.reuze.ied_ParticleEffect;
import com.software.reuze.vg_i_Sprite;
import com.software.reuze.dg_ParticleEmitter.NumericValue;

public class appParticleEditor extends JFrame {
	Effects lwjglCanvas;
	JPanel rowsPanel;
	JPanel editRowsPanel;
	vgu_EffectPanel effectPanel;
	private JSplitPane splitPane;
	dg_ParticleEmitter.NumericValue pixelsPerMeter;
	dg_ParticleEmitter.NumericValue zoomLevel;
	public vg_i_Sprite awt;
	vgu_SpriteAWT awt2;
	float pixelsPerMeterPrev;
	float zoomLevelPrev;

	public ied_ParticleEffect effect = new ied_ParticleEffect();
	final HashMap<dg_ParticleEmitter, ParticleData> particleData = new HashMap();

	public appParticleEditor() {
		super("Particle Editor");

		lwjglCanvas = new Effects();
		lwjglCanvas.setBackground(Color.DARK_GRAY);
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent event) {
				System.exit(0);
			}
		});
		awt2 = new vgu_SpriteAWT();
		awt = awt2;
		effect.setSprite(awt);
		initializeComponents();
		pixelsPerMeter = new dg_ParticleEmitter.NumericValue();
		pixelsPerMeter.setValue(55);
		zoomLevel = new dg_ParticleEmitter.NumericValue();
		zoomLevel.setValue(1);
		setSize(950, 950);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public appParticleEditor(vg_i_Sprite awt) {
		super("Particle Editor");
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent event) {
				System.exit(0);
			}
		});
		this.awt = awt;
		effect.setSprite(awt);
		initializeComponents();
		pixelsPerMeter = new dg_ParticleEmitter.NumericValue();
		pixelsPerMeter.setValue(55);
		zoomLevel = new dg_ParticleEmitter.NumericValue();
		zoomLevel.setValue(1);
		setSize(950, 950);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public class Effects extends Canvas implements KeyListener {

		public void paint(Graphics g) {
			effect.setPosition(this.getWidth() / 2, this.getHeight() / 2);
			awt2.g = (Graphics2D) g;
			effect.draw(awt2);
			effect.update(0.04f);
		}

		public Effects() {
			addKeyListener(this);
		}

		public Dimension getMinimumSize() {
			return new Dimension(325, 400);
		}

		public Dimension getPreferredSize() {
			return new Dimension(325, 550);
		}

		public Dimension getMaximumSize() {
			return new Dimension(325, 550);
		}

		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		public void keyTyped(KeyEvent e) {
			repaint();
		}

	}

	public void reloadRows() {
		/*
		 * EventQueue.invokeLater(new Runnable() {
		 * 
		 * public void run () {
		 */
		editRowsPanel.removeAll();
		addEditorRow(new vgu_NumericPanel("Pixels per meter", pixelsPerMeter));
		addEditorRow(new vgu_NumericPanel("Zoom level", zoomLevel));

		rowsPanel.removeAll();
		dg_ParticleEmitter emitter = getEmitter();
		addRow(new vgu_ImagePanel(appParticleEditor.this));
		addRow(new vgu_RangedNumericPanel("Delay", emitter.getDelay()));
		addRow(new vgu_RangedNumericPanel("Duration", emitter.getDuration()));
		addRow(new vgu_CountPanel(appParticleEditor.this));
		addRow(new vgu_ScaledNumericPanel("Emission", "Duration",
				emitter.getEmission()));
		addRow(new vgu_ScaledNumericPanel("Life", "Duration", emitter.getLife()));
		addRow(new vgu_ScaledNumericPanel("Life Offset", "Duration",
				emitter.getLifeOffset()));
		addRow(new vgu_RangedNumericPanel("X Offset", emitter.getXOffsetValue()));
		addRow(new vgu_RangedNumericPanel("Y Offset", emitter.getYOffsetValue()));
		addRow(new vgu_SpawnPanel(emitter.getSpawnShape(),
				appParticleEditor.this));
		addRow(new vgu_ScaledNumericPanel("Spawn Width", "Duration",
				emitter.getSpawnWidth()));
		addRow(new vgu_ScaledNumericPanel("Spawn Height", "Duration",
				emitter.getSpawnHeight()));
		addRow(new vgu_ScaledNumericPanel("Size", "Life", emitter.getScale()));
		addRow(new vgu_ScaledNumericPanel("Velocity", "Life",
				emitter.getVelocity()));
		addRow(new vgu_ScaledNumericPanel("Angle", "Life", emitter.getAngle()));
		addRow(new vgu_ScaledNumericPanel("Rotation", "Life",
				emitter.getRotation()));
		addRow(new vgu_ScaledNumericPanel("Wind", "Life", emitter.getWind()));
		addRow(new vgu_ScaledNumericPanel("Gravity", "Life",
				emitter.getGravity()));
		addRow(new vgu_GradientPanel("Tint", emitter.getTint()));
		addRow(new vgu_PercentagePanel("Transparency", "Life",
				emitter.getTransparency()));
		addRow(new vgu_OptionsPanel(appParticleEditor.this));
		for (Component component : rowsPanel.getComponents())
			if (component instanceof vgu_EditorPanel)
				((vgu_EditorPanel) component).update(appParticleEditor.this);
		rowsPanel.repaint();
		rowsPanel.validate();
		/*
		 * } });
		 */
	}

	void addEditorRow(JPanel row) {
		row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
				java.awt.Color.black));
		editRowsPanel.add(row, new GridBagConstraints(0, -1, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	void addRow(JPanel row) {
		row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
				java.awt.Color.black));
		rowsPanel.add(row, new GridBagConstraints(0, -1, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public void setVisible(String name, boolean visible) {
		for (Component component : rowsPanel.getComponents())
			if (component instanceof vgu_EditorPanel
					&& ((vgu_EditorPanel) component).getName().equals(name))
				component.setVisible(visible);
	}

	public dg_ParticleEmitter getEmitter() {
		return effect.getEmitters().get(effectPanel.editIndex);
	}

	public ImageIcon getIcon(dg_ParticleEmitter emitter) {
		ParticleData data = particleData.get(emitter);
		if (data == null)
			particleData.put(emitter, data = new ParticleData());
		String imagePath = emitter.getImagePath();
		if (data.icon == null && imagePath != null) {
			try {
				URL url;
				File file = new File(imagePath);
				if (file.exists())
					url = file.toURI().toURL();
				else {
					url = appParticleEditor.class.getResource(imagePath);
					if (url == null)
						return null;
				}
				data.icon = new ImageIcon(url);
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			}
		}
		return data.icon;
	}

	public void setIcon(dg_ParticleEmitter emitters, ImageIcon icon) {
		ParticleData data = particleData.get(emitters);
		if (data == null)
			particleData.put(emitters, data = new ParticleData());
		data.icon = icon;
	}

	public void setEnabled(dg_ParticleEmitter emitter, boolean enabled) {
		ParticleData data = particleData.get(emitter);
		if (data == null)
			particleData.put(emitter, data = new ParticleData());
		data.enabled = enabled;
		emitter.setPause(!enabled);
		if (enabled)
			emitter.reset();
	}

	public boolean isEnabled(dg_ParticleEmitter emitter) {
		ParticleData data = particleData.get(emitter);
		if (data == null)
			return true;
		return data.enabled;
	}

	private void initializeComponents() {
		splitPane = new JSplitPane();
		splitPane.setUI(new BasicSplitPaneUI() {
			public void paint(Graphics g, JComponent jc) {
			}
		});
		splitPane.setDividerSize(4);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		{
			JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			rightSplit.setUI(new BasicSplitPaneUI() {
				public void paint(Graphics g, JComponent jc) {
				}
			});
			rightSplit.setDividerSize(4);
			splitPane.add(rightSplit, JSplitPane.RIGHT);

			{
				JPanel propertiesPanel = new JPanel(new GridBagLayout());
				rightSplit.add(propertiesPanel, JSplitPane.TOP);
				propertiesPanel.setBorder(new CompoundBorder(BorderFactory
						.createEmptyBorder(3, 0, 6, 6), BorderFactory
						.createTitledBorder("Editor Properties")));
				{
					JScrollPane scroll = new JScrollPane();
					propertiesPanel.add(scroll, new GridBagConstraints(0, 0, 1,
							1, 1, 1, GridBagConstraints.NORTH,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
							0));
					scroll.setBorder(BorderFactory
							.createEmptyBorder(0, 0, 0, 0));
					{
						editRowsPanel = new JPanel(new GridBagLayout());
						scroll.setViewportView(editRowsPanel);
						scroll.getVerticalScrollBar().setUnitIncrement(70);
					}
				}
			}

			{
				JPanel propertiesPanel = new JPanel(new GridBagLayout());
				rightSplit.add(propertiesPanel, JSplitPane.BOTTOM);
				propertiesPanel.setBorder(new CompoundBorder(BorderFactory
						.createEmptyBorder(3, 0, 6, 6), BorderFactory
						.createTitledBorder("Emitter Properties")));
				{
					JScrollPane scroll = new JScrollPane();
					propertiesPanel.add(scroll, new GridBagConstraints(0, 0, 1,
							1, 1, 1, GridBagConstraints.NORTH,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
							0));
					scroll.setBorder(BorderFactory
							.createEmptyBorder(0, 0, 0, 0));
					{
						rowsPanel = new JPanel(new GridBagLayout());
						scroll.setViewportView(rowsPanel);
						scroll.getVerticalScrollBar().setUnitIncrement(70);
					}
				}
			}
			rightSplit.setDividerLocation(200);

		}
		{
			JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			leftSplit.setUI(new BasicSplitPaneUI() {
				public void paint(Graphics g, JComponent jc) {
				}
			});
			leftSplit.setDividerSize(4);
			splitPane.add(leftSplit, JSplitPane.LEFT);
			{
				JPanel spacer = new JPanel(new BorderLayout());
				leftSplit.add(spacer, JSplitPane.TOP);
				if (lwjglCanvas != null)
					spacer.add(lwjglCanvas);
				spacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
			}
			{
				JPanel emittersPanel = new JPanel(new BorderLayout());
				leftSplit.add(emittersPanel, JSplitPane.BOTTOM);
				emittersPanel.setBorder(new CompoundBorder(BorderFactory
						.createEmptyBorder(0, 6, 6, 0), BorderFactory
						.createTitledBorder("Effect Emitters")));
				{
					effectPanel = new vgu_EffectPanel(this);
					emittersPanel.add(effectPanel);
				}
			}
			leftSplit.setDividerLocation(550);
		}
		splitPane.setDividerLocation(325);
	}

	static class ParticleData {
		public ImageIcon icon;
		public String imagePath;
		public boolean enabled = true;
	}

	public static void main(String[] args) {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (Throwable ignored) {
				}
				break;
			}
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new appParticleEditor();
			}
		});
	}

}
