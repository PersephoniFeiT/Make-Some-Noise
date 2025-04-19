package FrontEnd;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.image.*;
// import java.awt.event.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Project;
import BackEnd.Editor.GradientFunction;
import Exceptions.NotSignedInException;

class EditorPanel extends JPanel {

	private NoisePanel noisePanel;
    private int noisePanelWidth = 700;
    private int noisePanelHeight = 463;
	// private JButton renderButton;
    private LayerPanelList layers;
    private GradientPanel gradientPanel;
    private GradientFunction gradientFunction;
    
    private Project project;

	public EditorPanel(MakeSomeNoiseWindow parentWindow, Project p, CurrentSession currentSession) {
        setLayout(new BorderLayout());                            // using BorderLayout layout managers
        setSize(1000, 700);                                       // width and height

        project = p;

        JPanel editorHeader = new JPanel();
        editorHeader.setLayout(new BoxLayout(editorHeader, BoxLayout.Y_AXIS));

        JPanel projTitleEditing = new JPanel();
        projTitleEditing.setLayout(new FlowLayout());

        JTextField projectTitleField = new JTextField(p.title, 50);
        projectTitleField.setEditable(false);
        
        JButton updateProjectTitleButton = new JButton("Rename");
        updateProjectTitleButton.setMaximumSize(new Dimension(5, 5));
        updateProjectTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                if (projectTitleField.isEditable()) {
                    projectTitleField.setEditable(false);
                    CurrentSession.ChangeTitle(project, projectTitleField.getText());
                } else {
                    try {
                        currentSession.getSignedIn();
                        projectTitleField.setEditable(true);
                    } catch (NotSignedInException er) {
                        parentWindow.signInErrorMessage();
                    }
                }
            }
        });
        projTitleEditing.add(projectTitleField);
        projTitleEditing.add(updateProjectTitleButton);
        editorHeader.add(projTitleEditing);

        JPanel sharingInfo = new JPanel();
        sharingInfo.setLayout(new FlowLayout());

        JCheckBox isVisible = new JCheckBox("Share online");
        isVisible.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String v = (isVisible.isSelected()) ? "public" : "private";
                if (project.getID() != null) {
                    currentSession.ChangeStatus(project, v);
                }
            }
        });
        sharingInfo.add(isVisible);

        JTextField tagsList = new JTextField(String.join(", ", project.tags), 30);
        tagsList.setEditable(false);
        JButton editTagsButton = new JButton("Change");
        editTagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                if (tagsList.isEditable()) {
                    tagsList.setEditable(false);
                    List<String> tags = Arrays.stream(tagsList.getText().split("[^A-Za-z]+"))
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());
                    CurrentSession.ChangeTags(project, tags);
                } else {
                    try {
                        currentSession.getSignedIn();
                        tagsList.setEditable(true);
                    } catch (NotSignedInException er) {
                        parentWindow.signInErrorMessage();
                    }
                }
            }
        });
        sharingInfo.add(new JLabel("Tags: "));
        sharingInfo.add(tagsList);
        sharingInfo.add(editTagsButton);

        JButton deleteButton = new JButton("Delete project");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                try {
                    parentWindow.deleteProject(currentSession.getSignedIn(), project.getID());
                } catch (NotSignedInException ex) {
                    parentWindow.signInErrorMessage();
                }
            }
        });
        sharingInfo.add(deleteButton);

        editorHeader.add(sharingInfo);
        
        add(editorHeader, BorderLayout.NORTH);

        // Create a NoisePanel to display the generated noise pattern
        int width = noisePanelWidth;
        int height = noisePanelHeight;
        noisePanel = new NoisePanel(width, height);

        // noisePanel.setBounds(10, 10, width, height);
        noisePanel.setBorder(new LineBorder(Color.BLACK));
        noisePanel.setPreferredSize(new Dimension(width, height));
		add(noisePanel, BorderLayout.CENTER);

        gradientPanel = new GradientPanel(1000, 40, project.getColor1(), project.getColor2(), this);
        gradientFunction = new GradientFunction(project.getColor1(), project.getColor2());

        // Add a LayerPanelList, the visual list of noise layers that the user has created in this project
        layers = new LayerPanelList(project, this);
        layers.setPreferredSize(new Dimension(550, 100));
        add(layers, BorderLayout.EAST);
        
        add(gradientPanel, BorderLayout.SOUTH);


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
	}

    public void renderNoise() {
        int width = noisePanelWidth;
        int height = noisePanelHeight;
        double[][] values = BackEnd.Editor.LayerManager.multiplyLayers(width, height, project.getLayerList());
        
        gradientFunction.SetColor1(project.getColor1());
        gradientFunction.SetColor2(project.getColor2());
        BufferedImage newBitmap = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float val = (float) values[x][y];
                //int color = Color.HSBtoRGB(h, 0.5f, 1.0f);
                newBitmap.setRGB(x, y, gradientFunction.interpolate(val));
            }
        }
        noisePanel.setBitmap(newBitmap); // 👉 Add this method to NoisePanel
    }

    public void writeImage() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    try { 
                        noisePanel.writeToFile(fileChooser.getSelectedFile());
                    } catch (IOException ex) {
                        System.out.println("ERROR: Failed to save image");
                    }
                }
            }
        });
        fileChooser.showSaveDialog(this);
        
    }

    public Project getProject() {
        return project;
    }
}
