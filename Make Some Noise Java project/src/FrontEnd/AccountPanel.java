package FrontEnd;

import java.util.HashMap;

import javax.swing.*;

public class AccountPanel extends JPanel {

	private AccountHeader header;
	private ProjectThumbnailList projectList;
	
	public AccountPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		header = new AccountHeader();
		projectList = new ProjectThumbnailList();

		add(header);
		add(projectList);
	}

	public AccountPanel(HashMap<String, String> accountInfo) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
}