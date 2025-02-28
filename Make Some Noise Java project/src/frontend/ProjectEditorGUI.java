package frontend;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.awt.image.IndexColorModel;
import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.BorderLayout;

public class ProjectEditorGUI {  
    
    public ProjectEditorGUI() {  
        // create instance of JFrame
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());                                          // using no layout managers
        f.setSize(1000, 600);                                       // width and height
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create a NoisePanel to display the generated noise pattern
        int width = 647;
        int height = 400;
        NoisePanel np = new NoisePanel(width, height);
        np.setBounds(10, 10, width, height);
        np.setBorder(new LineBorder(Color.BLACK));

        // Create JButton that will render the Noise pattern on the NoisePanel np
        JButton renderButton = new JButton("Show Noise"); 
        renderButton.setBounds(100, 500, 130, 40);                  // x axis, y axis, width, height  
        renderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                np.setNoiseRaster(null);
            }
        });

        // Create JLabel to show the results the pingButton's pings
        JLabel pingLabel = new JLabel();
        pingLabel.setText("Lorem IPsum");
        pingLabel.setBounds(250, 450, 130, 40);

        // Create JButton that pings an IP
        JButton pingButton = new JButton("Ping Server");
        pingButton.setBounds(250, 500, 130, 40);
        pingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                pingLabel.setText("Waiting...");
                try {
                    if (InetAddress.getByName("8.8.8.8").isReachable(5000)) {
                        pingLabel.setText("Contact made");
                    } else {
                        pingLabel.setText("Connection Timeout");
                    }
                } catch (UnknownHostException e) {
                    pingLabel.setText("Error: unknown address");
                } catch (IOException e) {
                    pingLabel.setText("Error: IO Exception");
                }
            }
        });

        LayerPanelList layers = new LayerPanelList();
        f.add(layers, BorderLayout.EAST);
        

        // add elements in JFrame
        f.add(renderButton);
        f.add(pingButton);
        f.add(pingLabel);
        f.add(np);
        
        f.setVisible(true); 
    }  

    public static void main(String args[]){
        new ProjectEditorGUI();
    }
}  