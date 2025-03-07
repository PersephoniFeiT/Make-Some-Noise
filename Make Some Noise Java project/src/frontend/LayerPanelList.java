/* package frontend;
import backend.Editor.NoiseLayer;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.event.*;


public class LayerPanelList extends JScrollPane {

	private class LayerPanel extends JPanel {

		private JTextField layerName;

		private JTextField seed;
		private JTextField frequency;
		private JTextField amplitude;
		private JTextField gain;
		private JTextField floor;
		private JTextField ceiling;

		public LayerPanel(NoiseLayer nl) {
			int columnNumber = 4;

			layerName 	= new JTextField("New Layer");

			seed 		= new JTextField(Integer.valueOf(nl.getSeed()).toString(), columnNumber);
			frequency 	= new JTextField(Double.valueOf(nl.getFreq()).toString(), columnNumber);
			amplitude 	= new JTextField(Double.valueOf(nl.getAmp()).toString(), columnNumber);
			gain 		= new JTextField(Double.valueOf(nl.getGain()).toString(), columnNumber);
			floor 		= new JTextField(Double.valueOf(nl.getFloor()).toString(), columnNumber);
			ceiling 	= new JTextField(Double.valueOf(nl.getCeiling()).toString(), columnNumber);
			
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// Draw the name on the JPanel
			g.drawImage(layerName, 0, 0, null);

			// Draw each layer parameter field 
			g.drawImage(seed, 10, 30, null);
			g.drawImage(frequency, 20, 30, null);
			g.drawImage(amplitude, 30, 30, null);
			g.drawImage(gain, 40, 30, null);
			g.drawImage(floor, 50, 30, null);
			g.drawImage(ceiling, 60, 30, null);
		}
	}

	private ArrayList<LayerPanel> layerPanels;

	private JButton addLayerButton;

	public LayerPanelList() {
		layerPanel = new ArrayList<LayerPanel>();

		listLabel = new JLabel("Layers");
		listLabel.setBound(40, 0, 30, 100);
		add(listLabel);
		
		addLayerButton = new JButton("Add Layer");
		addLayerButton.setBounds(0, 0, 30, 30);
		addLayerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				layerPanels.add(new LayerPanel(new PerlinNoiseLayer()));
			}
		});
		add(addLayerButton);
	}
} */