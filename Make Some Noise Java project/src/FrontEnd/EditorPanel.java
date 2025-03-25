package FrontEnd;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

class EditorPanel extends JPanel {

	private MakeSomeNoiseWindow parentWindow;

	private NoisePanel noisePanel;
	private JButton signInButton;
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
        renderButton.setPreferredSize(new Dimension(130, 40));
		add(renderButton, BorderLayout.WEST);

        // Create JButton for sign-in
        signInButton = new JButton("Sign In");
        // signInButton.setBounds(800, 20, 100, 25);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                signInButtonPushed();
            }
        });
        signInButton.setPreferredSize(new Dimension(100, 25));
        add(signInButton, BorderLayout.NORTH);

        // Add a LayerPanelList, the visual list of noise layers that the user has created in this project
        LayerPanelList layers = new LayerPanelList();
        layers.setPreferredSize(new Dimension(450, 100));
        add(layers, BorderLayout.EAST);
	}

	public void renderNoiseButtonPushed() {
		noisePanel.setNoiseRaster(null);
	}

	public void signInButtonPushed() {
		parentWindow.createSignInWindow();
	}
}
