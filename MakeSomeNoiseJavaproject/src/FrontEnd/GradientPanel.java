package FrontEnd;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.colorchooser.*;
import java.awt.event.ActionEvent;

import BackEnd.Editor.GradientFunction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.image.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class GradientPanel extends JPanel{
    private class ColorPickerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            
            // Create color chooser with current button color
            JColorChooser colorChooser = new JColorChooser(button.getBackground());
            
            // Remove all panels except the one named "HSV" (HSB)
            AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
            for (AbstractColorChooserPanel panel : panels) {
                if (!panel.getDisplayName().equals("HSV")) {
                    colorChooser.removeChooserPanel(panel);
                }
            }
            
            // Show dialog
            JDialog dialog = JColorChooser.createDialog(
                button,
                "Choose HSV Color",
                true,
                colorChooser,
                ev -> { // OK Listener (triggers only on confirmation)
                    Color newColor = colorChooser.getColor();
                    button.setBackground(newColor);
                    updateSample(); // Expensive call
                    source.renderNoise();
                },
                null // Cancel Listener (no action needed)
            );

            dialog.setVisible(true);
        }
    }

    private BufferedImage gradientSample;
    private JButton color1Button;
    private JButton color2Button;
    private EditorPanel source;

    public GradientPanel(int width, int height, int color1, int color2, EditorPanel source){
        this.source = source;
        this.setPreferredSize(new Dimension(width + 10, height + 20));
        color1Button = new JButton();
        color1Button.setPreferredSize(new Dimension(30, 30));
        color1Button.setOpaque(true);
        color1Button.setBackground(new Color(color1));
        color1Button.addActionListener(new ColorPickerListener());
        color2Button = new JButton();
        color2Button.setPreferredSize(new Dimension(30, 30));
        color2Button.setOpaque(true);
        color2Button.setBackground(new Color(color2));
        color2Button.addActionListener(new ColorPickerListener());
        setLayout(new BorderLayout());
        JLabel panelTitle = new JLabel("Gradient Picker", SwingConstants.CENTER);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.add(color1Button, BorderLayout.WEST);
        topPanel.add(color2Button, BorderLayout.EAST);
        topPanel.add(panelTitle, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        gradientSample = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        updateSample();
        JPanel gradSamplePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (gradientSample != null) {
                    g.drawImage(gradientSample, 0, 0, this);
                }
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
            
        };
        JPanel sampleContainer = new JPanel();
        sampleContainer.setLayout(new BoxLayout(sampleContainer, BoxLayout.Y_AXIS));
        sampleContainer.add(Box.createVerticalGlue());
        sampleContainer.add(gradSamplePanel, BoxLayout.Y_AXIS);
        sampleContainer.add(Box.createVerticalGlue());
        add(sampleContainer, BorderLayout.SOUTH);
    }

    public void updateSample(){
        GradientFunction gradient = new GradientFunction(getColor1(), getColor2());
        for(int x = 0; x < gradientSample.getWidth(); x++){
            double location = (double)x/(double)gradientSample.getWidth();
            for(int y = 0; y < gradientSample.getHeight(); y++){
                gradientSample.setRGB(x, y, gradient.interpolate(location));
            }
        }
        repaint();
    }
    
    public int getColor1(){
        return color1Button.getBackground().getRGB();
    }

    public int getColor2(){
        return color2Button.getBackground().getRGB();
    }

    
}
