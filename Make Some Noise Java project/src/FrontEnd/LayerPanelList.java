package FrontEnd;

import BackEnd.Editor.Simplex2NoiseLayer;
import BackEnd.Editor.Simplex3NoiseLayer;
import BackEnd.Editor.NoiseLayer;
import BackEnd.Editor.PerlinNoiseLayer;
import BackEnd.Editor.RandomNoiseLayer;
import BackEnd.Accounts.Project;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import java.util.Iterator;

public class LayerPanelList extends JScrollPane {

	private ContentPanel contents;
	private EditorPanel hostEditorPanel;

	public LayerPanelList(Project p, EditorPanel ep) {
		contents = new ContentPanel(p, ep);
		hostEditorPanel = ep;
		setViewportView(contents);
	}

	private class LayerPanel extends JPanel {

		private JTextField layerName;

		private JComboBox<String> layerType;

		private JCheckBox layerIsVisible;

		private LabeledTextField seed;
		private LabeledTextField freq;
		private LabeledTextField amp;
		private LabeledTextField gain;
		private LabeledTextField floor;
		private LabeledTextField ceiling;

		private NoiseLayer noiseLayer;
		private Project project;
		private EditorPanel hostEditorPanel;

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
		
		public LayerPanel(NoiseLayer nl, Project proj, EditorPanel host) {
			setLayout(new FlowLayout());
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

			this.hostEditorPanel = host;
			project = proj;
			noiseLayer = nl;

			// Populate the LayerPanel with a title and attribute fields
			layerIsVisible = new JCheckBox();
			layerIsVisible.setSelected(true);
			layerIsVisible.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (layerIsVisible.isSelected()) {
						proj.addLayer(noiseLayer);
					} else {
						proj.removeLayer(noiseLayer);
					}
					updateLayer();
				}
			});

			layerName = new JTextField("New Layer", 6);

			String[] layerOptions = {"Random Noise", /*"Perlin Noise",*/ "Simplex2 Noise", "Simplex3 Noise"};
			layerType = new JComboBox<String>(layerOptions);
			layerType.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				
					proj.removeLayer(noiseLayer);

					Object choice = layerType.getSelectedItem();
					if (choice.equals("Simplex2 Noise")) {
						noiseLayer = new Simplex2NoiseLayer(
							Integer.parseInt(seed.text.getText()),
							Double.parseDouble(floor.text.getText()),
							Double.parseDouble(ceiling.text.getText()),
							Double.parseDouble(amp.text.getText()),
							Double.parseDouble(freq.text.getText())
						);
					} else if (choice.equals("Perlin Noise")) {
						noiseLayer = new PerlinNoiseLayer();
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

					if (layerIsVisible.isSelected()) {
						proj.addLayer(noiseLayer);
					}

					hostEditorPanel.renderNoise();
				}
			});
			JPanel layerNameAndType = new JPanel();
			layerNameAndType.setLayout(new BoxLayout(layerNameAndType, BoxLayout.Y_AXIS));
			layerNameAndType.add(layerName);
			layerNameAndType.add(layerType);

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
			
			add(layerIsVisible);
			add(layerNameAndType);
			add(seed);
			add(freq);
			add(amp);
			add(gain);
			add(floor);
			add(ceiling);

			setMaximumSize(new Dimension(10000, 60));

			hostEditorPanel.renderNoise();
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
				double a = Double.parseDouble(amp.text.getText());
				if (a < 0.0) {
					// amp.text.setText("0.0");
					amp.setBackground(Color.pink);
					a = 0.0;
				} else if (a > 1.0) {
					// amp.text.setText("1.0");
					amp.setBackground(Color.pink);
					a = 1.0;
				}
				noiseLayer.setAmp(a);
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
				double f = Double.parseDouble(floor.text.getText());

				if (f < 0.0) {
					// floor.text.setText("0.0");
					floor.setBackground(Color.pink);
					f = 0.0;
				} else if (f > noiseLayer.getCeiling()) {
					// floor.text.setText(ceiling.text.getText());
					floor.setBackground(Color.pink);
					f = noiseLayer.getCeiling();
				}

				noiseLayer.setFloor(f);
			} catch (NumberFormatException e) {
				floor.setBackground(Color.pink);
			}

			try {
				ceiling.setBackground(Color.white);
				double c = Double.parseDouble(ceiling.text.getText());

				if (c < noiseLayer.getFloor()) {
					c = noiseLayer.getFloor();
					ceiling.setBackground(Color.pink);
				} else if (c > 1.0) {
					c = 1.0;
					ceiling.setBackground(Color.pink);
				}

				noiseLayer.setCeiling(c);
			} catch (NumberFormatException e) {
				ceiling.setBackground(Color.pink);
			}

			this.hostEditorPanel.renderNoise();
		}
	}

	private class ContentPanel extends JPanel {

		private Project project;
		private EditorPanel hostEditorPanel;

		private class Header extends JPanel {

			public Header() {
				setLayout(new FlowLayout());
				setMaximumSize(new Dimension(1000, 50));
	
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


		public ContentPanel(Project p, EditorPanel host) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			add(new Header());

			this.hostEditorPanel = host;
			project = p;

			Iterator<NoiseLayer> layers = p.getLayerList().iterator();
			while(layers.hasNext()) {
				this.addLayer(layers.next());
			}
		}

		public void addLayer() {
			RandomNoiseLayer newLayer = new RandomNoiseLayer(123, 0.0, 1.0, 1.0, 1.0);
			LayerPanel lp = new LayerPanel(newLayer, project, hostEditorPanel);
			project.addLayer(newLayer);
			this.add(lp);
			lp.updateLayer();
			revalidate();
			repaint();
		}

		public void addLayer(NoiseLayer nl) {
			LayerPanel lp = new LayerPanel(nl, project, hostEditorPanel);
			this.add(lp);
			lp.updateLayer();
			revalidate();
			repaint();
		}
	}
} 
