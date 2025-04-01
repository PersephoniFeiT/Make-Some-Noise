package FrontEnd;

import java.util.HashMap;

import javax.swing.*;

public class AccountPanel extends JPanel {

	private Integer accountId;
	private AccountHeader header;
	private ProjectThumbnailList projectList;
	
	public AccountPanel(Integer ID) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		accountId = ID;
		header = new AccountHeader();
		projectList = new ProjectThumbnailList();

		add(header);
		add(projectList);
	}

	public AccountPanel(Integer ID, HashMap<String, String> accountInfo) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		accountId = ID;
		header = new AccountHeader(accountInfo);
		projectList = new ProjectThumbnailList();

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