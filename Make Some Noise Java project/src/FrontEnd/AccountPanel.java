package FrontEnd;

import java.util.Map;
import javax.swing.*;

import BackEnd.Accounts.CurrentSession;
import Exceptions.NotSignedInException;

public class AccountPanel extends JPanel {

	private Integer accountId;
	private AccountHeader header;
	private ProjectThumbnailList projectList;

	public AccountPanel(Map<String, String> accountInfo, CurrentSession currentSession) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String projectListText;
		try {
			accountId = currentSession.getSignedIn();
			projectListText = "Your projects:";
		} catch (NotSignedInException e){
			accountId = null;
			projectListText = "You are not signed in.";
		}

		header = new AccountHeader(accountInfo, currentSession);
		System.out.println("accPanel "+ accountId);
		projectList = new ProjectThumbnailList(accountId, currentSession.GetProjectsInAccount());

		add(header);
		add(new JLabel(projectListText));
		add(projectList);
	}

	public Integer getAccountId() {
		return accountId;
	}
}