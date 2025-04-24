package FrontEnd;

import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Sharing;
import ServerEnd.BasicDatabaseActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maya Malavasi, Ryan Shipp
 * SearchPanel is a JPanel window for the search functionality of our application. It contains a search bar, drop down
 * for the search type, and an area to display a ProjectThumbnailList of the resulting projects from the search.
 */
public class SearchPanel extends JPanel {

	private MakeSomeNoiseWindow mainWindow;
	private SearchBar searchBar = new SearchBar();
	private ProjectThumbnailList searchResults;
	private JComboBox<String> searchByDrop = new JComboBox<>(new String[]{"Title", "Username", "Tag"});
	private String searchBy = "title";
	//Sharing.SearchByTitle()

	/** Constructor creates the different components of the page (search bar, drop down menu, search button, and display
	 * area for thumbnails) and adds them to the panel page to display.
	 * @param mainWindow the main window/graphics controller Jpanel of the overall project
	 */
	public SearchPanel(MakeSomeNoiseWindow mainWindow) {
		this.mainWindow = mainWindow;

		Integer ID = null;
		if (this.mainWindow.isAdmin()) ID = this.mainWindow.getSignedIn();
		searchResults = new ProjectThumbnailList(this.mainWindow, ID, new ArrayList<>());

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(new JLabel("Search"));

		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Integer> searchByList;
				switch (searchBy){
					case "Title":
						searchByList = Sharing.SearchByTitle(searchBar.getText());
						break;
					case "Username":
						searchByList = Sharing.SearchByUsername(searchBar.getText());
						break;
					case "Tag":
						searchByList = Sharing.SearchByTag(searchBar.getText());
						break;
					case null:
						searchByList = new ArrayList<>();
						break;
					default:
						searchByList = Sharing.SearchByTitle(searchBar.getText());
						break;
				}
				// Remove old results
				remove(searchResults);

				// Add new results
				Integer ID = null;
				if (SearchPanel.this.mainWindow.isAdmin()) ID = SearchPanel.this.mainWindow.getSignedIn();
				searchResults = new ProjectThumbnailList(SearchPanel.this.mainWindow, ID, searchByList);
				add(searchResults);

				// Refresh UI
				revalidate();
				repaint();
			}
		});

		searchByDrop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) searchByDrop.getSelectedItem();
				searchBy = selectedItem;
			}
		});

		JPanel searchBarPanel = new JPanel();
		searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.X_AXIS));
		searchBar.setMaximumSize(new Dimension(200, 30));
		searchByDrop.setMaximumSize(new Dimension(120, 30));
		searchBarPanel.add(searchBar);
		searchBarPanel.add(searchByDrop);
		searchBarPanel.add(searchButton);

		this.add(searchBarPanel);
		add(searchResults);
	}
}