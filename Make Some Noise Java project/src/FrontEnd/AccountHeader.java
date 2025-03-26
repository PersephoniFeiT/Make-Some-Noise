package FrontEnd;

import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* 
 * Class to represent the header of an account, showing a user's basic account information
 */
public class AccountHeader extends JPanel {

	private JLabel username;
	private JLabel email;

	public AccountHeader() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		username = new JLabel("Guest");
		email = new JLabel("No email");

		add(username);
		add(email);
	}
	
	public AccountHeader(HashMap<String, String> accountInfo) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		username = new JLabel(accountInfo.get("username"));
		email = new JLabel(accountInfo.get("email"));

		add(username);
		add(email);
	}

}
