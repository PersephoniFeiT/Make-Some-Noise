package FrontEnd;

import BackEnd.Editor.Simplex2NoiseLayer;
import javafx.scene.layout.Border;
import BackEnd.Editor.NoiseLayer;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
// import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.FlowLayout;
import java.awt.event.*;


public class LayerPanelList extends JPanel {

	private class LayerPanel extends JPanel {

		private JTextField layerName;

		private JTextField seed;
		private JTextField frequency;
		private JTextField amplitude;
		private JTextField gain;
		private JTextField floor;
		private JTextField ceiling;

		public LayerPanel(NoiseLayer nl) {
			setLayout(new FlowLayout());

			int columnNumber = 4;

			layerName 	= new JTextField("New Layer");

			seed 		= new JTextField(""+nl.getSeed(), columnNumber);
			frequency 	= new JTextField(""+nl.getFreq(), columnNumber);
			amplitude 	= new JTextField(""+nl.getAmp(), columnNumber);
			gain 		= new JTextField(""+nl.getGain(), columnNumber);
			floor 		= new JTextField(""+nl.getFloor(), columnNumber);
			ceiling 	= new JTextField(""+nl.getCeiling(), columnNumber);
			
			add(layerName);
			add(seed);
			add(frequency);
			add(amplitude);
			add(gain);
			add(floor);
			add(ceiling);
		}

		// @Override
		// protected void paintComponent(Graphics g) {
		// 	super.paintComponent(g);
			
		// 	// Draw the name on the JPanel
		// 	g.drawImage(layerName, 0, 0, null);

		// 	// Draw each layer parameter field 
		// 	g.drawImage(seed, 10, 30, null);
		// 	g.drawImage(frequency, 20, 30, null);
		// 	g.drawImage(amplitude, 30, 30, null);
		// 	g.drawImage(gain, 40, 30, null);
		// 	g.drawImage(floor, 50, 30, null);
		// 	g.drawImage(ceiling, 60, 30, null);
		// }
	}

	private ArrayList<LayerPanel> layerPanels;
	// private BackEnd.Editor.LayerManager layerManager = new LayerManager();

	private JLabel listLabel;
	private JButton addLayerButton;

	public LayerPanelList() {
		setLayout(new BorderLayout());

		layerPanels = new ArrayList<LayerPanel>();

		listLabel = new JLabel("Layers");
		listLabel.setBounds(40, 0, 30, 100);
		add(listLabel, BorderLayout.NORTH);
		
		addLayerButton = new JButton("+");
		addLayerButton.setBounds(0, 0, 50, 50);
		addLayerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				addLayer();
			}
		});
		add(addLayerButton, BorderLayout.NORTH);
	}

	public void addLayer() {
		Simplex2NoiseLayer newLayer = new Simplex2NoiseLayer(0.0, 0.0, 0.0, 0.0);
		LayerPanel lp = new LayerPanel(newLayer);
		layerPanels.add(lp);
		add(lp, BorderLayout.CENTER);
		// layerManager.add(newLayer);
	}
} 
