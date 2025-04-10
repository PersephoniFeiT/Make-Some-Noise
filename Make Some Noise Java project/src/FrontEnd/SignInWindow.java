package FrontEnd;

import javax.swing.Box;
import javax.swing.BoxLayout;

// import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.WindowConstants;

import BackEnd.Accounts.CurrentSession;
import Exceptions.Accounts.ExceptionHandler;
import Exceptions.Accounts.IncorrectPasswordException;
import Exceptions.Accounts.InvalidInputException;
import Exceptions.Accounts.NoSuchAccountException;

public class SignInWindow extends JFrame {

	public SignInWindow(CurrentSession currentSession) {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));  	       // using BoxLayout layout managers
		setSize(300, 400);        				                               // width and height
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		add(Box.createVerticalStrut(50));

		// Add labels and input fields
		JLabel signInLabel = new JLabel("Sign In:");
		add(signInLabel);

		add(Box.createVerticalStrut(40));

		JLabel userNameFieldLabel = new JLabel("Username:");
		add(userNameFieldLabel);

		JTextField userNameField = new JTextField();
		userNameField.setMaximumSize(new Dimension(350, 75));
		add(userNameField);

		add(Box.createVerticalStrut(40));

		JLabel passwordFieldLabel = new JLabel("Password:");
		add(passwordFieldLabel);

		JPasswordField passwordField = new JPasswordField();
		passwordField.setMaximumSize(new Dimension(350, 75));
		add(passwordField);

		add(Box.createVerticalStrut(20));

		// add button for server account checks
		JButton submitButton = new JButton("Submit");
		// submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				char[] passwordChars = passwordField.getPassword();
				String password = new String(passwordChars);
				// Securely wipe the password
				Arrays.fill(passwordChars, '\0');
				try {
					currentSession.SignIn(userNameField.getText(), password);
				} catch (IncorrectPasswordException e) {
				} catch (NoSuchAccountException e) {
				} catch (InvalidInputException e) {
				} catch (Exception e) {
					ExceptionHandler.handleException(e);
				}
				setVisible(false);
				dispose();
			}
		});
		add(submitButton);

		add(Box.createVerticalGlue());

		// add "create new account" button and label
		JLabel createAccountLabel = new JLabel("Don't have an account?");
		// createAccountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(createAccountLabel);

		JButton createAccountButton = new JButton("Create account");
		// createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createAccountButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				new CreateAccountWindow(currentSession);
				setVisible(false);
				dispose();

			}
		});
		add(createAccountButton);

		setVisible(true);
	}
}