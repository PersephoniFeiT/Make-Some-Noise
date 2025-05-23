package FrontEnd;

import BackEnd.Editor.Simplex2NoiseLayer;
import BackEnd.Editor.Simplex3NoiseLayer;
import BackEnd.Editor.BlendMode;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.Iterator;

/**
 * A visual list of layers to show the properties of each {@link BackEnd.Editor.NoiseLayer} in a {@link BackEnd.Accounts.Project}. 
 * @author Ryan Shipp
 */
public class LayerPanelList extends JScrollPane {

	private ContentPanel contents;
	private EditorPanel hostEditorPanel;

	/**
	 * Creates a new LayerPanelList populated with the layers in an existing project
	 * @param p the project that this LayerPanelList will be associated with
	 * @param ep the editor panel that this LayerPanelList will sit in
	 */
	public LayerPanelList(Project p, EditorPanel ep) {
		hostEditorPanel = ep;
		contents = new ContentPanel(p, ep);
		setViewportView(contents);
	}

	/**
	 * A panel that shows the properties for a single layer.
	 * <P>
	 * A single panel contains a checkbox for if the layer should be included, a drop-down menu for layer type, and text boxes for parameter editing
	 */
	private class LayerPanel extends JPanel {

		private JTextField layerName;

		private JComboBox<String> layerType;

		private JCheckBox layerIsVisible;

		private LabeledTextField seed;
		private LabeledTextField freq;
		private LabeledTextField amp;
		private LabeledTextField gain;
		private JComboBox<String> blendMode;
		private LabeledTextField floor;
		private LabeledTextField ceiling;

		private NoiseLayer noiseLayer;
		private Project project;
		private EditorPanel hostEditorPanel;

		/**
		 * Component to represent a text field with a label above it
		 */
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
		
	    /**
	 	 * LayerPanel is a graphical panel that allows a user to edit a {@link BackEnd.Editor.NoiseLayer}
    	 * @author Ryan Shipp
    	 * @author Fei Triolo - {@link BlendMode} integration
	 	 */
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
							Double.parseDouble(gain.text.getText()),
							Double.parseDouble(amp.text.getText()),
							Double.parseDouble(freq.text.getText()),
							BlendMode.fromInt(blendMode.getSelectedIndex())
						);
					} else if (choice.equals("Perlin Noise")) {
						noiseLayer = new PerlinNoiseLayer();
					} else if (choice.equals("Random Noise")) {
						noiseLayer = new RandomNoiseLayer(
							Integer.parseInt(seed.text.getText()),
							Double.parseDouble(floor.text.getText()),
							Double.parseDouble(ceiling.text.getText()),
							Double.parseDouble(gain.text.getText()),
							Double.parseDouble(amp.text.getText()),
							Double.parseDouble(freq.text.getText()),
							BlendMode.fromInt(blendMode.getSelectedIndex())
						);
					} else if (choice.equals("Simplex3 Noise")) {
						noiseLayer = new Simplex3NoiseLayer(
							Integer.parseInt(seed.text.getText()),
							Double.parseDouble(floor.text.getText()),
							Double.parseDouble(ceiling.text.getText()),
							Double.parseDouble(gain.text.getText()),
							Double.parseDouble(amp.text.getText()),
							Double.parseDouble(freq.text.getText()),
							BlendMode.fromInt(blendMode.getSelectedIndex())
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
			String[] blendModeOptions = {"Multiply","Divide","Add","Subtract"};
			seed =	new LabeledTextField("Seed", ""+nl.getSeed(), columnNumber);
			freq =	new LabeledTextField("Freq", ""+nl.getFreq(), columnNumber);
			amp =	new LabeledTextField("Amp", ""+nl.getAmp(), columnNumber);
			blendMode = new JComboBox<>(blendModeOptions);
			blendMode.setSelectedIndex(0);
			gain =	new LabeledTextField("Gain", ""+nl.getGain(), columnNumber);
			floor =	new LabeledTextField("Floor", ""+nl.getFloor(), columnNumber);
			ceiling =	new LabeledTextField("Ceil", ""+nl.getCeiling(), columnNumber);

			// Add listeners to each field so their NoiseLayer objects can be updated with them
			seed.text.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { updateIfValid(); }
				public void removeUpdate(DocumentEvent e) { updateIfValid(); }
				public void changedUpdate(DocumentEvent e) { updateIfValid(); }

				private void updateIfValid() {
					try {
						Double.parseDouble(seed.text.getText()); // test only
						updateLayer();
					} catch (NumberFormatException ignored) {}
				}
			});

			freq.text.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { updateIfValid(); }
				public void removeUpdate(DocumentEvent e) { updateIfValid(); }
				public void changedUpdate(DocumentEvent e) { updateIfValid(); }

				private void updateIfValid() {
					try {
						Double.parseDouble(seed.text.getText()); // test only
						updateLayer();
					} catch (NumberFormatException ignored) {
						// Don't call updateLayer yet — user may still be typing
					}
				}
			});

			amp.text.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { updateIfValid(); }
				public void removeUpdate(DocumentEvent e) { updateIfValid(); }
				public void changedUpdate(DocumentEvent e) { updateIfValid(); }

				private void updateIfValid() {
					try {
						Double.parseDouble(seed.text.getText()); // test only
						updateLayer();
					} catch (NumberFormatException ignored) {
						// Don't call updateLayer yet — user may still be typing
					}
				}
			});

			gain.text.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { updateIfValid(); }
				public void removeUpdate(DocumentEvent e) { updateIfValid(); }
				public void changedUpdate(DocumentEvent e) { updateIfValid(); }

				private void updateIfValid() {
					try {
						Double.parseDouble(seed.text.getText()); // test only
						updateLayer();
					} catch (NumberFormatException ignored) {
						// Don't call updateLayer yet — user may still be typing
					}
				}
			});

			floor.text.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { updateIfValid(); }
				public void removeUpdate(DocumentEvent e) { updateIfValid(); }
				public void changedUpdate(DocumentEvent e) { updateIfValid(); }

				private void updateIfValid() {
					try {
						Double.parseDouble(seed.text.getText()); // test only
						updateLayer();
					} catch (NumberFormatException ignored) {
						// Don't call updateLayer yet — user may still be typing
					}
				}
			});

			ceiling.text.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) { updateIfValid(); }
				public void removeUpdate(DocumentEvent e) { updateIfValid(); }
				public void changedUpdate(DocumentEvent e) { updateIfValid(); }

				private void updateIfValid() {
					try {
						Double.parseDouble(seed.text.getText()); // test only
						updateLayer();
					} catch (NumberFormatException ignored) {
						// Don't call updateLayer yet — user may still be typing
					}
				}
			});

			blendMode.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					String selectedItem = (String) blendMode.getSelectedItem();
					project.setBlendMode(selectedItem);
					updateLayer();
				}
			});

			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					project.removeLayer(noiseLayer);
					contents.remove(LayerPanel.this);
					hostEditorPanel.renderNoise();
					hostEditorPanel.revalidate();
					hostEditorPanel.repaint();
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
			add(blendMode);
			add(deleteButton);
			
			setMaximumSize(new Dimension(10000, 60));

			hostEditorPanel.renderNoise();
		}
	
		/**
		 * Change the layer object to match any changes that have been made to its LayerPanel
		 */
		private void updateLayer() {

			try {
				freq.setBackground(Color.white);
				this.noiseLayer.setFreq(Double.parseDouble(freq.text.getText()));
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
				double g = Double.parseDouble(gain.text.getText());
				if(g < -0.999){
					g = -0.999;
				}
				if(g > 0.999){
					g = 0.999;
				}
				noiseLayer.setGain(g);
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

			int mode = blendMode.getSelectedIndex();
			noiseLayer.setBlendMode(BlendMode.fromInt(mode));

			this.hostEditorPanel.renderNoise();
		}
	}

	/**
	 * ContentPanel is the rigid panel that is made scrollable by placing it in the LayerPanelList
	 * @author Ryan Shipp
	 */
	private class ContentPanel extends JPanel {

		private Project project;
		private EditorPanel hostEditorPanel;

		/**
		 * Header is the collection of compoenents that always lies at the top of the Panel, including the label and "Add New Layer" button
		 */
		private class Header extends JPanel {
			/**
			 * Create a new header object to be added into a ContentPanel
			 */
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

		/**
		 * Create a new content panel and populate it with a header and any existing layers
		 * @param p the project that this ContentPanel represents, which can be an empty project or an existing project with layers
		 * @param host the EditorPanel that this LayerPanelList resides in
		 */
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

		/**
		 * Add a new layer to the LayerPanelList and to its project. 
		 * By default, this is a Random Noise Layer with seed=123, floor=0.0, ceiling=1.0, gain=0, amp=1.0, freq=1.0, and BlendMode=Multiply
		 */
		public void addLayer() {
			RandomNoiseLayer newLayer = new RandomNoiseLayer(123, 0.0, 1.0, 0, 1.0, 1.0, BlendMode.MULTIPLY);
			LayerPanel lp = new LayerPanel(newLayer, project, hostEditorPanel);
			project.addLayer(newLayer);
			this.add(lp);
			lp.updateLayer();
			revalidate();
			repaint();
			hostEditorPanel.renderNoise();
		}

		/**
		 * Add a noise layer that has already been constructed to the LayerPanelList and its project
		 * @param nl the noise layer to be added
		 */
		public void addLayer(NoiseLayer nl) {
			LayerPanel lp = new LayerPanel(nl, project, hostEditorPanel);
			this.add(lp);
			lp.updateLayer();
			revalidate();
			repaint();
			hostEditorPanel.renderNoise();
		}

		/**
		 * Remove a LayerPanel from this LayerPanelList and from the project
		 * @param lp the LayerPanel to be removed
		 */
		public void removeLayer(LayerPanel lp) {
			this.remove(lp);
			project.removeLayer(lp.noiseLayer);
			revalidate();
			repaint();
		}
	}
} 
