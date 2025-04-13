package FrontEnd;

import BackEnd.Accounts.Sharing;
import ServerEnd.BasicDatabaseActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SearchPanel extends JPanel {

	private SearchBar searchBar = new SearchBar();
	private ProjectThumbnailList searchResults = new ProjectThumbnailList(new ArrayList<>());
	//Sharing.SearchByTitle()

	public SearchPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(new JLabel("Search"));
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchResults.addProjectList(Sharing.SearchByTitle(searchBar.getText()));
			}
		});
		add(searchBar);
		this.add(searchButton);
		add(searchResults);
	}
}