package frontend;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.awt.Color;
import java.awt.BorderLayout;
import backend.Accounts.CurrentSession;

public class ProjectEditorGUI extends JFrame {  
    
    public ProjectEditorGUI(CurrentSession currentSession) {  
        setLayout(new BorderLayout());                            // using BorderLayout layout managers
        setSize(1000, 600);                                       // width and height
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create a NoisePanel to display the generated noise pattern
        int width = 647;
        int height = 400;
        NoisePanel np = new NoisePanel(width, height);
        np.setBounds(10, 10, width, height);
        np.setBorder(new LineBorder(Color.BLACK));

        // Create JButton that will render the Noise pattern on the NoisePanel np
        JButton renderButton = new JButton("Show Noise"); 
        renderButton.setBounds(20, 410, 130, 40);                  // x axis, y axis, width, height  
        renderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                np.setNoiseRaster(null);
            }
        });

        // Create JButton for sign-in
        JButton signInButton = new JButton("Sign In");
        signInButton.setBounds(800, 20, 100, 25);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new SignInWindow(currentSession);
            }
        });
        add(signInButton);

        // Add a LayerPanelList, the visual list of noise layers that the user has created in this project
        // LayerPanelList layers = new LayerPanelList();
        // add(layers, BorderLayout.EAST);
        

        // add elements in JFrame
        add(renderButton);
        add(np);
        
        setVisible(true); 
    }  

    // public static void main(String args[]){
    //     new ProjectEditorGUI();
    // }
}  