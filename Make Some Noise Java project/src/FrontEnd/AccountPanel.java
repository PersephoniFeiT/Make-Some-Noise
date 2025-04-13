package FrontEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import BackEnd.Accounts.CurrentSession;
import Exceptions.Accounts.NotSignedInException;

public class AccountPanel extends JPanel {

	private Integer accountId;
	private AccountHeader header;
	private ProjectThumbnailList projectList;

	public AccountPanel(Integer ID, Map<String, String> accountInfo, CurrentSession currentSession) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		accountId = ID;
		header = new AccountHeader(accountInfo, currentSession);

		projectList = new ProjectThumbnailList(currentSession.GetProjectsInAccount());

		add(header);
		add(projectList);
	}

	public Integer getAccountId() {
		return accountId;
	}
}