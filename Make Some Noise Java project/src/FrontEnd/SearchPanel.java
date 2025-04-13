package FrontEnd;

import BackEnd.Accounts.Sharing;
import ServerEnd.BasicDatabaseActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchPanel extends JPanel {

	private SearchBar searchBar = new SearchBar();
	private ProjectThumbnailList searchResults = new ProjectThumbnailList(new ArrayList<>());
	private JComboBox<String> searchByDrop = new JComboBox<>(new String[]{"Title", "Username", "Tag"});
	private List<Integer> searchByList = new ArrayList<>();
	//Sharing.SearchByTitle()

	public SearchPanel() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(new JLabel("Search"));
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchResults.addProjectList(searchByList);
			}
		});

		searchByDrop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) searchByDrop.getSelectedItem();
				switch (selectedItem){
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
			}
		});


		add(searchBar);
		add(searchByDrop);
		this.add(searchButton);
		add(searchResults);
	}
}