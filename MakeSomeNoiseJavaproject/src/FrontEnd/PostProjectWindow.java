package FrontEnd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;

import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Project;
import Exceptions.NotSignedInException;

/**
 * A graphical window that allows a user to post a {@link BackEnd.Accounts.Project} after choosing a title, tags, and sharing settings. <P> Usually spawned by a {@link MakeSomeNoiseWindow}
 * @author Ryan Shipp
 */
public class PostProjectWindow extends JFrame {
	
	private JTextField titleField;
	private JTextField tagsField;
	private JCheckBox postPubliclyBox;
	
	/**
	 * Creates a new PostProjectWindow
	 * @param session the user session that the project will be posted to
	 * @param proj the project to be posted
	 */
	public PostProjectWindow(CurrentSession session, Project proj) {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setSize(300, 400);

		HashMap<String, String> accInfo = session.GetAccountInfo();
		add(new JLabel("Post project to "+accInfo.get("username")));

		add(new JLabel("Title:"));

		titleField = new JTextField(proj.title, 30);
		add(titleField);

		add(new JLabel("Tags"));

		tagsField = new JTextField(30);
		add(tagsField);
		
		postPubliclyBox = new JCheckBox("Make this project searchable to other users");
		add(postPubliclyBox);

		JButton submitButton = new JButton();
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitForm(session, proj);
			}
		});
		add(submitButton);

	}

	/**
	 * Submits the form, publishing the project to the server with the given title and tags
	 */
	public void submitForm(CurrentSession cs, Project p) {
		try {
			cs.SaveProject(p);
		} catch (NotSignedInException x){
			System.out.println("Must be signed in to save.");
		}

		CurrentSession.ChangeTags(p, parseTags());
		CurrentSession.ChangeTitle(p, this.titleField.getText());
		cs.ChangeStatus(p, postPubliclyBox.isSelected() ? "public" : "private");
	}

	/*
	 * Parse the given tags, seperating them by semicolons and trimming leading and trailing whitespace
	 */
	public List<String> parseTags() {
		String tags = tagsField.getText();

		return Arrays.asList(tags.split("\\s*;\\s*"));

	}
}
