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

public class PostProjectWindow extends JFrame {
	
	private JTextField titleField;
	private JTextField tagsField;
	private JCheckBox postPubliclyBox;
	
	public PostProjectWindow(CurrentSession session, Project proj) {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setSize(300, 400);

		HashMap<String, String> accInfo = session.GetAccountInfo();
		add(new JLabel("Post project to "+accInfo.get("username")));

		add(new JLabel("Title:"));

		titleField = new JTextField(30);
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

	// Method to submit the form, publishing the project to the server with the given title and tags
	public void submitForm(CurrentSession cs, Project p) {
		cs.SaveProject(p);

		Integer ID = p.getID();
		CurrentSession.ChangeTags(ID, parseTags());
		CurrentSession.ChangeTitle(ID, this.titleField.getText());
		cs.ChangeStatus(ID, postPubliclyBox.isSelected() ? "public" : "private");
	}

	// Method to parse the given tags, seperating them by semicolons and trimming leading and trailing whitespace
	public List<String> parseTags() {
		String tags = tagsField.getText();

		return Arrays.asList(tags.split("\\s*;\\s*"));

	}
}
