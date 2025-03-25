package FrontEnd;

import BackEnd.Editor.Simplex2NoiseLayer;
import BackEnd.Editor.NoiseLayer;
import BackEnd.Editor.LayerManger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;

public class LayerPanelList extends JScrollPane {

	private class LayerPanel extends JPanel {

		private JTextField layerName;
		
		private JTextField seed;
		private JTextField freq;
		private JTextField amp;
		private JTextField gain;
		private JTextField floor;
		private JTextField ceiling;

		private NoiseLayer noiseLayer;
		
		public LayerPanel(NoiseLayer nl) {
			setLayout(new FlowLayout());
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

			noiseLayer = nl;

			// Populate the LayerPanel with a title and attribute fields
			layerName = new JTextField("New Layer", 6);

			int columnNumber = 4;
			seed =	new JTextField(""+nl.getSeed(), columnNumber);
			freq =	new JTextField(""+nl.getFreq(), columnNumber);
			amp =	new JTextField(""+nl.getAmp(), columnNumber);
			gain =	new JTextField(""+nl.getGain(), columnNumber);
			floor =	new JTextField(""+nl.getFloor(), columnNumber);
			ceiling =	new JTextField(""+nl.getCeiling(), columnNumber);

			// Add listeners to each field so their NoiseLayer objects can be updated with them
			seed.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			freq.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			amp.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			gain.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			floor.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			ceiling.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});
			
			add(layerName);
			add(seed);
			add(freq);
			add(amp);
			add(gain);
			add(floor);
			add(ceiling);

			setMaximumSize(new Dimension(1000, 50));
		}
	
		private void updateLayer() {

			try {
				freq.setBackground(Color.white);
				noiseLayer.setFreq(Double.parseDouble(freq.getText()));
			} catch (NumberFormatException e) {
				freq.setBackground(Color.pink);
			}

			try {
				amp.setBackground(Color.white);
				noiseLayer.setAmp(Double.parseDouble(amp.getText()));
			} catch (NumberFormatException e) {
				amp.setBackground(Color.pink);
			}

			try {
				gain.setBackground(Color.white);
				noiseLayer.setGain(Double.parseDouble(gain.getText()));
			} catch (NumberFormatException e) {
				gain.setBackground(Color.pink);
			}

			try {
				floor.setBackground(Color.white);
				noiseLayer.setFloor(Double.parseDouble(floor.getText()));
			} catch (NumberFormatException e) {
				floor.setBackground(Color.pink);
			}

			try {
				ceiling.setBackground(Color.white);
				noiseLayer.setCeiling(Double.parseDouble(ceiling.getText()));
			} catch (NumberFormatException e) {
				ceiling.setBackground(Color.pink);
			}
		}
	}

	private class ContentPanel extends JPanel {

		private class Header extends JPanel {

			public Header() {
				setLayout(new FlowLayout());
	
				add(new JLabel("Layers"));
			
				JButton addLayerButton = new JButton("+");
				addLayerButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						addLayer();
					}
				});
				add(addLayerButton);
			}
		}
		
		public ContentPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			add(new Header());
		}

		public void addLayer() {
			Simplex2NoiseLayer newLayer = new Simplex2NoiseLayer(0.0, 0.0, 0.0, 0.0);
			LayerPanel lp = new LayerPanel(newLayer);
			contents.add(lp);
			revalidate();
			repaint();
			// layerManager.add(newLayer);
		}
	}

	private LayerManger layerManager = new LayerManger();
	private ContentPanel contents;

	public LayerPanelList() {
		contents = new ContentPanel();
		setViewportView(contents);
	}

	public LayerManger getManager() {
		return layerManager;
	}
} 
