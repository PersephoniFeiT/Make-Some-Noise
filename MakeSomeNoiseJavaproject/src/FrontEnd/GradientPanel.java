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

/**
 * JFrame panel for selecting and previewing color gradients with HSV colorpickers and gradient preview raster.
 * This frame contains a color picker button on the far left which selects the gradient starting color a horizontal gradient preview bar raster that renders the full gradient, and a second color picker on the right which represents the ending gradient color.
 * @package FrontEnd
 * @author Fei Triolo
 */
public class GradientPanel extends JPanel{
    /**
     * Internal ColorPicker ActionListener class which instantiates an HSV default colorpicker, updates the gradient preview bar, and calls renderNoise() from {@link EditorPanel} to apply the new gradient on confirmation.
     */
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

    /**
     * updates the preview raster using the {@link GradientFunction} to set the RGB value of accross x position normalized between 0 and 1
     */
    public void updateSample(){
        GradientFunction gradient = new GradientFunction(getColor1(), getColor2());
        source.getProject().setColor1(getColor1());
        source.getProject().setColor2(getColor2());
        for(int x = 0; x < gradientSample.getWidth(); x++){
            double location = (double)x/(double)gradientSample.getWidth();
            for(int y = 0; y < gradientSample.getHeight(); y++){
                gradientSample.setRGB(x, y, gradient.interpolate(location));
            }
        }
        repaint();
    }
    
    /**
     * Gets the hexcode value of the starting gradient color
     * @return the color selected on the left color picker button
     */
    public int getColor1(){
        return color1Button.getBackground().getRGB();
    }

    /**
     * Gets the hexcode value of the ending gradient color
     * @return the color selected on the right color picker button
     */
    public int getColor2(){
        return color2Button.getBackground().getRGB();
    }

    
}
