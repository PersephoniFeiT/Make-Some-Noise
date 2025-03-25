package FrontEnd;

import BackEnd.Editor.Simplex2NoiseLayer;
import BackEnd.Editor.NoiseLayer;
import BackEnd.Editor.LayerManger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.awt.Dimension;
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
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

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

			setMaximumSize(new Dimension(1000, 50));
		}
	}

	private class Header extends JPanel {

		public Header() {
			setLayout(new FlowLayout());

			listLabel = new JLabel("Layers");
			listLabel.setBounds(40, 0, 30, 100);
			add(listLabel);
		
			addLayerButton = new JButton("+");
			addLayerButton.setBounds(0, 0, 50, 50);
			addLayerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				addLayer();
			}
		});
		add(addLayerButton);
		}
	}

	private ArrayList<LayerPanel> layerPanels;
	private LayerManger layerManager = new LayerManger();
	private JLabel listLabel;
	private JButton addLayerButton;

	public LayerPanelList() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		layerPanels = new ArrayList<LayerPanel>();

		add(new Header());
	}

	public void addLayer() {
		Simplex2NoiseLayer newLayer = new Simplex2NoiseLayer(0.0, 0.0, 0.0, 0.0);
		LayerPanel lp = new LayerPanel(newLayer);
		layerPanels.add(lp);
		add(lp);
		revalidate();
		repaint();
		// layerManager.add(newLayer);
	}

	public LayerManger getManager() {
		return layerManager;
	}
} 
