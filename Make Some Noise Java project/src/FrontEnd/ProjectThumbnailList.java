package FrontEnd;

import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Project;
import Exceptions.Accounts.NotSignedInException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

/* 
 * Class to represent a visual list of projects, each containing a thumbnail and basic project information
 */
public class ProjectThumbnailList extends JScrollPane {

	private ContentPanel contents;
	private Integer accountID;

	// constructor
	public ProjectThumbnailList() {
		contents = new ContentPanel();
		this.accountID = null;
		setViewportView(contents);
	}

	// constructor
	public ProjectThumbnailList(List<Integer> projectIDs) {
		contents = new ContentPanel(projectIDs);
		this.accountID = null;
		setViewportView(contents);
	}

	// constructor
	public ProjectThumbnailList(Integer accountID, List<Integer> projectIDs) {
		this(projectIDs);
		this.accountID = accountID;
	}


	public void addProject(Integer projectID) {
		contents.addThumbnail(accountID, projectID);
	}

	public void addProjectList(List<Integer> projectIDs) {
		projectIDs.forEach(this::addProject);
	}


	/////////////////////
	private class ContentPanel extends JPanel {
		List<Integer> projectIDs;

		//constructor
		public ContentPanel() {
			setLayout(new FlowLayout());
		}

		public ContentPanel(List<Integer> projectIDs) {
			super();
			projectIDs.forEach(i -> this.addThumbnail(accountID, i));
		}

		public void addThumbnail(Integer accountID, Integer projectID) {
			add(new ProjectThumbnail(accountID, projectID));
		}

		//////////////////
		private class ProjectThumbnail extends JPanel {

			public ProjectThumbnail(Integer accountID, Integer projectID) {
				Map<String, String> projectInfo =  CurrentSession.GetProjectInfo(projectID);
				List<String> tags = CurrentSession.getProjectTags(projectID);


				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				setMaximumSize(new Dimension(300, 300));

				// TODO: add image stuff
				add(new JLabel("[THIS IS AN IMAGE]"));
				
				add(new JLabel(projectInfo.get("title")));
				add(new JLabel("By " + projectInfo.get("username")));
				add(new JLabel("Created " + projectInfo.get("dateCreated")));
				
				// Construct a single string containing every tag on this project
				StringBuilder tagsString = new StringBuilder();
				for(int i=0; i < tags.size(); i++) {
					tagsString.append("#").append(tags.get(i)).append(", ");
				}
				add(new JLabel(tagsString.toString()));

				System.out.println("accountID = " + accountID + ", projectID = " + projectID);
				if (accountID != null) {
					JButton deleteButton = new JButton("DELETE");
					deleteButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							CurrentSession.DeleteProject(accountID, projectID);
						}
					});
					add(deleteButton);
				}
				
			}
		}
	}

}
