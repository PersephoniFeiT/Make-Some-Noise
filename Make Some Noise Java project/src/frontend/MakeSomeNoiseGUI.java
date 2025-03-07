/* package frontend;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.event.*;
import java.awt.image.IndexColorModel;
import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MakeSomeNoiseGUI {  
    public static void main(String[] args) {  
        // create instance of JFrame
        JFrame f = new JFrame();
        f.setLayout(null);                                          // using no layout managers
        f.setSize(1000, 600);                                       // width and height
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create a NoisePanel to display the generated noise pattern
        int width = 10;
        int height = 10;
        byte ff = (byte) 0xff;
        byte[] r = { ff, 0, 0, ff, 0 };
        byte[] g = { 0, ff, 0, ff, 0 };
        byte[] b = { 0, 0, ff, ff, 0 };
        frontend.NoisePanel np = new NoisePanel(new IndexColorModel(3, 5, r, g, b), width, height);
        np.setBounds(10, 10, 600, 400);
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
        
        // add elements in JFrame
        f.add(renderButton);
        f.add(pingButton);
        f.add(pingLabel);
        f.add(np);
        
        f.setVisible(true); 
    }  
}  */