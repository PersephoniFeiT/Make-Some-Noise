package FrontEnd;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.*;

import BackEnd.Accounts.CurrentSession;

/* 
 * Class to represent the header of an account panel, showing a user's basic account information
 */
public class AccountHeader extends JPanel {

	private JTextField username;
	private JTextField email;
	private JPasswordField passwordField;

	public AccountHeader(HashMap<String, String> accountInfo, CurrentSession session) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setMaximumSize(new Dimension(10000, 400));

		int columnSize = 12;
		if (accountInfo == null) {
			username = new JTextField("Guest", columnSize);
			email = new JTextField("No email", columnSize);
			passwordField = new JPasswordField("", columnSize);
		} else {
			username = new JTextField(accountInfo.get("username"), columnSize);
			email = new JTextField(accountInfo.get("email"), columnSize);
			passwordField = new JPasswordField("",columnSize);
		}

		JPanel usernameFeatures = new JPanel();
		usernameFeatures.setLayout(new FlowLayout());
		usernameFeatures.add(new JLabel("Username: "));
		usernameFeatures.add(username);
		JButton changeUsernameButton = new JButton("Change");
		changeUsernameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (username.isEditable()) {
					session.ChangeEmail(username.getText());
				}
				if (accountInfo != null) {
					username.setEditable(!username.isEditable());
				}
			}
		});
		usernameFeatures.add(changeUsernameButton);

		JPanel emailFeatures = new JPanel();
		emailFeatures.setLayout(new FlowLayout());
		emailFeatures.add(new JLabel("Email address: "));
		emailFeatures.add(email);
		JButton changeEmailButton = new JButton("Change");
		changeEmailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (email.isEditable()) {
					session.ChangeEmail(email.getText());
				}

				if (accountInfo != null) {
					email.setEditable(!email.isEditable());
				}
			}
		});
		emailFeatures.add(changeEmailButton);

		passwordField.setMaximumSize(new Dimension(350, 75));
		JPanel passwordFeatures = new JPanel();
		passwordFeatures.setLayout(new FlowLayout());
		passwordFeatures.add(new JLabel("Password: "));
		passwordFeatures.add(passwordField);
		JButton changePasswordButton = new JButton("Change");
		changePasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (passwordField.isEditable()) {
					char[] passwordChars = passwordField.getPassword();
					String password = new String(passwordChars);
					// Securely wipe the password
					Arrays.fill(passwordChars, '\0');
					session.ChangePassword(password);
				}

				if (accountInfo != null) {
					passwordField.setEditable(!passwordField.isEditable());
				}
			}
		});
		passwordFeatures.add(changePasswordButton);

		username.setEditable(false);
		email.setEditable(false);
		passwordField.setEditable(false);

		//////////////////////////////////////////////////////
		//get the list of projects in account


		add(usernameFeatures);
		add(emailFeatures);
		add(passwordFeatures);
		add(new JSeparator());
	}

}
