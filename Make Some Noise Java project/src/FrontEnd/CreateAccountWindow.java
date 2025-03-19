package FrontEnd;

import javax.swing.Box;
import javax.swing.BoxLayout;

import java.util.Arrays;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.WindowConstants;

import BackEnd.Accounts.CurrentSession;

public class CreateAccountWindow extends JFrame {

	public CreateAccountWindow(CurrentSession currentSession) {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));                           // using BorderLayout layout managers
		setSize(300, 400);                                       // width and height
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		add(Box.createVerticalStrut(50));

		// Add labels and input fields
		JLabel signInLabel = new JLabel("Create account:");
		add(signInLabel);

		add(Box.createVerticalStrut(20));

		JLabel userNameFieldLabel = new JLabel("Username:");
		add(userNameFieldLabel);

		JTextField userNameField = new JTextField();
		userNameField.setMaximumSize(new Dimension(350, 75));
		add(userNameField);

		add(Box.createVerticalStrut(20));

		JLabel emailFieldLabel = new JLabel("Email:");
		add(emailFieldLabel);

		JTextField emailField = new JTextField();
		emailField.setMaximumSize(new Dimension(350, 75));
		add(emailField);

		add(Box.createVerticalStrut(20));

		JLabel passwordFieldLabel = new JLabel("Password:");
		add(passwordFieldLabel);

		JPasswordField passwordField = new JPasswordField();
		passwordField.setMaximumSize(new Dimension(350, 75));
		add(passwordField);

		add(Box.createVerticalStrut(20));

		// add button for server account checks
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				char[] passwordChars = passwordField.getPassword();
				String password = new String(passwordChars);
				// Securely wipe the password
				Arrays.fill(passwordChars, '\0');
				currentSession.CreateNewAccount(userNameField.getText(), password, emailField.getText());
				setVisible(false);
				dispose();
			}
		});
		add(submitButton);

		add(Box.createVerticalGlue());

		// add "sign into existing account" button and label
		JLabel createAccountLabel = new JLabel("Already have an account?");
		add(createAccountLabel);

		JButton createAccountButton = new JButton("Sign in");
		createAccountButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				new SignInWindow(currentSession);
				setVisible(false);
				dispose();
			}
		});
		add(createAccountButton);

		setVisible(true);
	}
}