package FrontEnd;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.*;

class EditorPanel extends JFrame {

	private MakeSomeNoiseWindow parentWindow;

	private NoisePanel noisePanel;
	private JButton signInButton;
	private JButton renderButton;

	public EditorPanel(MakeSomeNoiseWindow parentWindow) {
		setLayout(new BorderLayout());                            // using BorderLayout layout managers
        setSize(1000, 600);                                       // width and height
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create a NoisePanel to display the generated noise pattern
        int width = 647;
        int height = 400;
        noisePanel = new NoisePanel(width, height);
        noisePanel.setBounds(10, 10, width, height);
        noisePanel.setBorder(new LineBorder(Color.BLACK));
		add(noisePanel);

        // Create JButton that will render the Noise pattern on the NoisePanel np
        renderButton = new JButton("Show Noise");
        renderButton.setBounds(20, 410, 130, 40);                  // x axis, y axis, width, height
        renderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				renderNoiseButtonPushed();
			}
        });
		add(renderButton);

        // Create JButton for sign-in
        JButton signInButton = new JButton("Sign In");
        signInButton.setBounds(800, 20, 100, 25);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                signInButtonPushed();
            }
        });
        add(signInButton);

        // Add a LayerPanelList, the visual list of noise layers that the user has created in this project
        // LayerPanelList layers = new LayerPanelList();
        // add(layers, BorderLayout.EAST);

	}

	public void renderNoiseButtonPushed() {
		noisePanel.setNoiseRaster(null);
	}

	public void signInButtonPushed() {
		parentWindow.createSignInWindow();
	}
}