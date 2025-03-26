package FrontEnd;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

class EditorPanel extends JPanel {

	private NoisePanel noisePanel;
	private JButton renderButton;
    private LayerPanelList layers;

	public EditorPanel(MakeSomeNoiseWindow parentWindow) {
        setLayout(new BorderLayout());                            // using BorderLayout layout managers
        setSize(1000, 600);                                       // width and height

        // Create a NoisePanel to display the generated noise pattern
        int width = 647;
        int height = 400;
        noisePanel = new NoisePanel(width, height);
        // noisePanel.setBounds(10, 10, width, height);
        noisePanel.setBorder(new LineBorder(Color.BLACK));
        noisePanel.setPreferredSize(new Dimension(width, height));
		add(noisePanel, BorderLayout.CENTER);

        // Create JButton that will render the Noise pattern on the NoisePanel np
        renderButton = new JButton("Show Noise");
        // renderButton.setBounds(20, 410, 130, 40);                  // x axis, y axis, width, height
        renderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				renderNoiseButtonPushed();
			}
        });
        renderButton.setMaximumSize(new Dimension(130, 40));
		add(renderButton, BorderLayout.WEST);

        // Add a LayerPanelList, the visual list of noise layers that the user has created in this project
        layers = new LayerPanelList();
        layers.setPreferredSize(new Dimension(450, 100));
        add(layers, BorderLayout.EAST);
	}

	public void renderNoiseButtonPushed() {
        int width = 647;
        int height = 400;
        double[][] values = layers.getManager().multiplyLayers(width, height);

        for (int x=0; x < width; x++) {
            for (int y=0; y < height; y++) {
                // System.out.print(values[x][y]);
                noisePanel.setPixel(x, y, Color.HSBtoRGB((float)values[x][y], 0.5f, 1.0f));
            }
            // System.out.print("\n");
        }

        System.out.println(layers.getManager().toString());

	}
}
