package FrontEnd;

import BackEnd.Accounts.Sharing;
import ServerEnd.BasicDatabaseActions;

import javax.swing.*;
import java.util.ArrayList;

public class SearchPanel extends JPanel {

	private SearchBar searchBar = new SearchBar();
	private ProjectThumbnailList searchResults = new ProjectThumbnailList(new ArrayList<>());
	//Sharing.SearchByTitle()

	public SearchPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(searchBar);
		add(searchResults);
	}
}