package FrontEnd;

import javax.swing.*;

public class SearchPanel extends JPanel {

	private SearchBar searchBar = new SearchBar();
	private ProjectThumbnailList searchResults = new ProjectThumbnailList();
	
	public SearchPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(searchBar);
		add(searchResults);
	}
}