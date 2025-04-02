package FrontEnd;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import BackEnd.Accounts.CurrentSession;

/* 
 * Class to represent the header of an account panel, showing a user's basic account information
 */
public class AccountHeader extends JPanel {

	private JTextField username;
	private JTextField email;
	
	public AccountHeader(HashMap<String, String> accountInfo, CurrentSession session) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setMaximumSize(new Dimension(10000, 400));

		int columnSize = 12;
		if (accountInfo == null) {
			username = new JTextField("Guest", columnSize);
			email = new JTextField("No email", columnSize);
		} else {
			username = new JTextField(accountInfo.get("username"), columnSize);
			email = new JTextField(accountInfo.get("email"), columnSize);
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

		username.setEditable(false);
		email.setEditable(false);

		add(usernameFeatures);
		add(emailFeatures);
	}

}
