package FrontEnd;

// import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
// import java.awt.event.*;

import BackEnd.Accounts.Project;

class EditorPanel extends JPanel {

	private NoisePanel noisePanel;
	// private JButton renderButton;
    private LayerPanelList layers;
    
    private Project project;

	public EditorPanel(MakeSomeNoiseWindow parentWindow, Project p) {
        setLayout(new BorderLayout());                            // using BorderLayout layout managers
        setSize(1000, 600);                                       // width and height

        project = p;

        // Create a NoisePanel to display the generated noise pattern
        int width = 800;
        int height = 494;
        noisePanel = new NoisePanel(width, height);

        // noisePanel.setBounds(10, 10, width, height);
        noisePanel.setBorder(new LineBorder(Color.BLACK));
        noisePanel.setPreferredSize(new Dimension(width, height));
		add(noisePanel, BorderLayout.CENTER);

        // Create JButton that will render the Noise pattern on the NoisePanel np
        // renderButton = new JButton("Show Noise");
        // // renderButton.setBounds(20, 410, 130, 40);                  // x axis, y axis, width, height
        // renderButton.addActionListener(new ActionListener() {
		// 	@Override
		// 	public void actionPerformed(ActionEvent evt) {
		// 		renderNoise();
		// 	}
        // });
        // renderButton.setMaximumSize(new Dimension(130, 40));
		// add(renderButton, BorderLayout.WEST);

        // Add a LayerPanelList, the visual list of noise layers that the user has created in this project
        layers = new LayerPanelList(project, this);
        layers.setPreferredSize(new Dimension(450, 100));
        add(layers, BorderLayout.EAST);
	}

	public void renderNoise() {
        int width = noisePanel.getWidth();
        int height = noisePanel.getHeight();
        double[][] values = BackEnd.Editor.LayerManager.multiplyLayers(width, height, project.getLayerList());
        for (int x=0; x < width; x++) {
            for (int y=0; y < height; y++) {
                noisePanel.setPixel(x, y, Color.HSBtoRGB((float)values[x][y], 0.5f, 1.0f));
            }
        }
	}

    public Project getProject() {
        return project;
    }
}
