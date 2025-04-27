package FrontEnd;

import java.util.Map;
import javax.swing.*;

import BackEnd.Accounts.CurrentSession;
import Exceptions.NotSignedInException;

/**
 * @author Ryan Shipp
 * AccountPanel is one of the three main graphical interfaces a user sees on {@link MakeSomeNoseWindow}, and is used to manage and view account info. It contains a Header panel that shows the user's username, password, and email, and a Projects panel that lists the user's saved projects.
 */
public class AccountPanel extends JPanel {

	private Integer accountId;
	private AccountHeader header;
	private ProjectThumbnailList projectList;
	private MakeSomeNoiseWindow mainWindow;

	/**
	 * Create a new account panel
	 * @param mainWindow the MakeSomeNoiseWindow that this panel will appear in
	 * @param accountInfo map containing the user's username, email, and password should be gotten from BackEnd.Accounts.CurrentSession.GetAccountInfo(), or should be null if the user is not signed in.
	 * @param currentSession is the current user session for the application
	 */
	public AccountPanel(MakeSomeNoiseWindow mainWindow, Map<String, String> accountInfo, CurrentSession currentSession) {
		this.mainWindow = mainWindow;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String projectListText = "";
		try {
			accountId = currentSession.getSignedIn();
			projectListText = "Your projects:";
		} catch (NotSignedInException e){
			accountId = null;
			projectListText = "You are not signed in.";
		}
		header = new AccountHeader(accountInfo, currentSession);
		projectList = new ProjectThumbnailList(mainWindow, accountId, currentSession.GetProjectsInAccount());

		add(header);
		add(new JLabel(projectListText));
		add(projectList);
	}

	public Integer getAccountId() {
		return accountId;
	}
}