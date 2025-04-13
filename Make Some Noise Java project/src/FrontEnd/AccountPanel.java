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

		// TODO: Make this work when I have a server connection
		// String projectList = accountInfo.get("projectList");
		// for(int i=0; i < projectList.length(), i++) {
		// 	projectList.add(projectList.get(i));
		// }

		add(header);
		add(projectList);
	}

	public Integer getAccountId() {
		return accountId;
	}
}