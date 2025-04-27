package FrontEnd;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import BackEnd.Accounts.CurrentSession;

/**
 * @author Ryan Shipp
 * AccountHeader is the graphical object that sits at the top of an {@link AccountPanel} to show and change a user's account data
 */
public class AccountHeader extends JPanel {

	private JTextField username;
	private JTextField email;
	private JPasswordField passwordField;

	/**
	 * Create a new header for an account, containing email, password, and username labels and the buttons to change them
	 * @param accountInfo map containing the user's username, email, and password should be gotten from BackEnd.Accounts.CurrentSession.GetAccountInfo(), or should be null if the user is not signed in.
	 * @param session is the current user session for the application
	 */
	public AccountHeader(Map<String, String> accountInfo, CurrentSession session) {
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

                assert accountInfo != null;
                username.setEditable(!accountInfo.isEmpty());

				if (username.isEditable()) {
					session.ChangeUsername(username.getText());
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

				assert accountInfo != null;
				email.setEditable(!accountInfo.isEmpty());

				if (email.isEditable()) {
					session.ChangeEmail(email.getText());
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

				assert accountInfo != null;
				passwordField.setEditable(!accountInfo.isEmpty());

				if (passwordField.isEditable()) {
					char[] passwordChars = passwordField.getPassword();
					String password = new String(passwordChars);
					// Securely wipe the password
					Arrays.fill(passwordChars, '\0');
					session.ChangePassword(password);
				}
			}
		});
		passwordFeatures.add(changePasswordButton);

		username.setEditable(false);
		email.setEditable(false);
		passwordField.setEditable(false);

		JButton deleteButton = new JButton("DELETE ACCOUNT");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				session.DeleteAccount();
			}
		});

		add(usernameFeatures);
		add(emailFeatures);
		add(passwordFeatures);

		assert accountInfo != null;
		if (!accountInfo.isEmpty() && !session.isAdmin()) add(deleteButton);
	}

}
