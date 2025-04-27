package FrontEnd;

import javax.swing.Box;
import javax.swing.BoxLayout;

import java.awt.Color;
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

import Exceptions.ExceptionHandler;
import Exceptions.IncorrectPasswordException;
import Exceptions.InvalidInputException;
import Exceptions.NoSuchAccountException;

/**
 * A window to that allows a user to sign into an existing account. It is normally launched by a {@link MakeSomeNoiseWindow}, and may launch a {@link CreateAccountWindow}
 * @author Ryan Shipp
 */
public class SignInWindow extends JFrame {

	/**
	 * Creates and shows a new sign-in window
	 * @param hostWindow
	 */
	public SignInWindow(MakeSomeNoiseWindow hostWindow) {
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

				// Reset any error messages
				userNameFieldLabel.setText("Username:");
				userNameFieldLabel.setForeground(Color.BLACK);
				passwordFieldLabel.setText("Password:");
				passwordFieldLabel.setForeground(Color.BLACK);

				char[] passwordChars = passwordField.getPassword();
				String password = new String(passwordChars);
				// Securely wipe the password
				Arrays.fill(passwordChars, '\0');
				try {
          			hostWindow.signIn(userNameField.getText(), password);
					setVisible(false);
					dispose();
				} catch (NoSuchAccountException e) {
					userNameFieldLabel.setText("Unrecognized username, try again");
					userNameFieldLabel.setForeground(Color.RED);
				} catch (IncorrectPasswordException e) {
					passwordFieldLabel.setText("Incorrect password, try again");
					passwordFieldLabel.setForeground(Color.RED);
				} catch (InvalidInputException e) {
				} catch (Exception e) {
					ExceptionHandler.handleException(e);
				}
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
				new CreateAccountWindow(hostWindow);
				setVisible(false);
				dispose();

			}
		});
		add(createAccountButton);

		setVisible(true);
	}
}