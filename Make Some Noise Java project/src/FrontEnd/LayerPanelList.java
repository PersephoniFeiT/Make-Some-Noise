package FrontEnd;

import BackEnd.Editor.Simplex2NoiseLayer;
import BackEnd.Editor.Simplex3NoiseLayer;
import BackEnd.Editor.SimplexNoise;
import BackEnd.Editor.NoiseLayer;
import BackEnd.Editor.PerlinNoiseLayer;
import BackEnd.Editor.RandomNoiseLayer;
import BackEnd.Editor.LayerManger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

		private class LabeledTextField extends JPanel {
			private JLabel label;
			private JTextField text;

			public LabeledTextField(String l, String t, int col) {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

				label = new JLabel(l);
				text = new JTextField(t, col);

				add(label);
				add(text);
			}
		}

		private JTextField layerName;

		private JComboBox<String> layerType;
		
		private LabeledTextField seed;
		private LabeledTextField freq;
		private LabeledTextField amp;
		private LabeledTextField gain;
		private LabeledTextField floor;
		private LabeledTextField ceiling;

		private NoiseLayer noiseLayer;
		private LayerManger manager;
		
		public LayerPanel(NoiseLayer nl, LayerManger inManager) {
			setLayout(new FlowLayout());
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

			this.manager = inManager;
			noiseLayer = nl;

			// Populate the LayerPanel with a title and attribute fields
			layerName = new JTextField("New Layer", 6);

			String[] layerOptions = {"Simplex2 Noise", "Perlin Noise", "Random Noise", "Simplex3 Noise"};
			layerType = new JComboBox<String>(layerOptions);
			layerType.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					manager.removeLayer(noiseLayer);
					Object choice = layerType.getSelectedItem();
					if (choice.equals("Simplex2 Noise")) {
						noiseLayer = new Simplex2NoiseLayer(
							Double.parseDouble(floor.text.getText()),
							Double.parseDouble(ceiling.text.getText()),
							Double.parseDouble(amp.text.getText()),
							Double.parseDouble(freq.text.getText())
						);
					} else if (choice.equals("Perlin Noise")) {
						manager.addLayer(new PerlinNoiseLayer());
					} else if (choice.equals("Random Noise")) {
						noiseLayer = new RandomNoiseLayer(
							Integer.parseInt(seed.text.getText()),
							Double.parseDouble(floor.text.getText()),
							Double.parseDouble(ceiling.text.getText()),
							Double.parseDouble(amp.text.getText()),
							Double.parseDouble(freq.text.getText())
						);
					} else if (choice.equals("Simplex3 Noise")) {
						noiseLayer = new Simplex3NoiseLayer(
							Integer.parseInt(seed.text.getText()),
							Double.parseDouble(floor.text.getText()),
							Double.parseDouble(ceiling.text.getText()),
							Double.parseDouble(amp.text.getText()),
							Double.parseDouble(freq.text.getText())
						);
					}
					manager.addLayer(noiseLayer);
				}
			});

			int columnNumber = 3;
			seed =	new LabeledTextField("Seed", ""+nl.getSeed(), columnNumber);
			freq =	new LabeledTextField("Freq", ""+nl.getFreq(), columnNumber);
			amp =	new LabeledTextField("Amp", ""+nl.getAmp(), columnNumber);
			gain =	new LabeledTextField("Gain", ""+nl.getGain(), columnNumber);
			floor =	new LabeledTextField("Floor", ""+nl.getFloor(), columnNumber);
			ceiling =	new LabeledTextField("Ceil", ""+nl.getCeiling(), columnNumber);

			// Add listeners to each field so their NoiseLayer objects can be updated with them
			seed.text.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			freq.text.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			amp.text.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			gain.text.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			floor.text.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});

			ceiling.text.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					updateLayer();
				}
			});
			
			add(layerName);
			add(layerType);
			add(seed);
			add(freq);
			add(amp);
			add(gain);
			add(floor);
			add(ceiling);

			setMaximumSize(new Dimension(10000, 200));
		}
	
		private void updateLayer() {

			try {
				freq.setBackground(Color.white);
				noiseLayer.setFreq(Double.parseDouble(freq.text.getText()));
			} catch (NumberFormatException e) {
				freq.setBackground(Color.pink);
			}

			try {
				amp.setBackground(Color.white);
				noiseLayer.setAmp(Double.parseDouble(amp.text.getText()));
			} catch (NumberFormatException e) {
				amp.setBackground(Color.pink);
			}

			try {
				gain.setBackground(Color.white);
				noiseLayer.setGain(Double.parseDouble(gain.text.getText()));
			} catch (NumberFormatException e) {
				gain.setBackground(Color.pink);
			}

			try {
				floor.setBackground(Color.white);
				noiseLayer.setFloor(Double.parseDouble(floor.text.getText()));
			} catch (NumberFormatException e) {
				floor.setBackground(Color.pink);
			}

			try {
				ceiling.setBackground(Color.white);
				noiseLayer.setCeiling(Double.parseDouble(ceiling.text.getText()));
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
		
		private LayerManger layerManager = new LayerManger();

		public ContentPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			add(new Header());
		}

		public void addLayer() {
			Simplex2NoiseLayer newLayer = new Simplex2NoiseLayer(0.0, 0.0, 0.0, 0.0);
			LayerPanel lp = new LayerPanel(newLayer, layerManager);
			layerManager.addLayer(newLayer);
			contents.add(lp);
			revalidate();
			repaint();
		}

		public LayerManger getManager() {
			return layerManager;
		}
	}

	private ContentPanel contents;

	public LayerPanelList() {
		contents = new ContentPanel();
		setViewportView(contents);
	}

	public LayerManger getManager() {
		return contents.getManager();
	}
} 
