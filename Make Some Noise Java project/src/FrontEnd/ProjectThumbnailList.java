package FrontEnd;

import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Project;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

/* 
 * Class to represent a visual list of projects, each containing a thumbnail and basic project information
 */
public class ProjectThumbnailList extends JScrollPane {

	private ContentPanel contents;
	private Integer accountID = null;
	private MakeSomeNoiseWindow mainWindow;

	// constructor
	public ProjectThumbnailList(MakeSomeNoiseWindow mainWindow) {
		this.mainWindow = mainWindow;
		contents = new ContentPanel();
		setViewportView(contents);
	}

	// constructor
	public ProjectThumbnailList(MakeSomeNoiseWindow mainWindow, List<Integer> projectIDs) {
		this.mainWindow = mainWindow;
		contents = new ContentPanel(projectIDs);
		setViewportView(contents);
	}

	// constructor
	public ProjectThumbnailList(MakeSomeNoiseWindow mainWindow, Integer accountID, List<Integer> projectIDs) {
		this.mainWindow = mainWindow;
		this.accountID = accountID;
		contents = new ContentPanel(projectIDs);
		setViewportView(contents);
	}


	public void reloadThumbnails(){
		// Clear existing contents
		this.contents.removeAll();
		// Re-add thumbnails
		if (this.contents.projectIDs != null) {
			this.contents.projectIDs.forEach(i -> this.contents.addThumbnail(accountID, i));
		}
		// Refresh UI
		this.revalidate();
		this.repaint();
	}

	public void goToEditorPanel(String projectInfo){
		System.out.println(projectInfo);
		this.mainWindow.addEditorPanel(Project.fromJSONtoProject(projectInfo));
		this.mainWindow.goToEditorPanel();
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
			this.projectIDs = projectIDs; // <-- store for reloads
			setLayout(new FlowLayout());
			projectIDs.forEach(i -> this.addThumbnail(accountID, i));
		}

		public void addThumbnail(Integer accountID, Integer projectID) {
			add(new ProjectThumbnail(accountID, projectID));
		}

		public void removeThumbnail(Integer projectID) {
			this.projectIDs.remove(projectID);
		}

		//////////////////
		private class ProjectThumbnail extends JPanel {

			public ProjectThumbnail(Integer accountID, Integer projectID) {
				Map<String, String> projectInfo =  CurrentSession.GetProjectInfo(projectID);
				List<String> tags = CurrentSession.getProjectTags(projectID);


				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				setMaximumSize(new Dimension(300, 300));

				this.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(final MouseEvent e) {
						if (accountID == null){ // if it's null, it's not your project or you're on share
							//so download the file.
							ProjectThumbnailList.this.mainWindow.saveProjectLocal(Project.fromJSONtoProject(projectInfo.get("projectInfoStruct")));
						} else {
							ProjectThumbnailList.this.goToEditorPanel(projectInfo.get("projectInfoStruct"));
						}
					}
				});

				// TODO: add image stuff
				add(new JLabel(projectInfo.get("thumbnail")));
				
				add(new JLabel(projectInfo.get("title")));
				add(new JLabel("By " + projectInfo.get("username")));
				add(new JLabel("Created " + projectInfo.get("dateCreated")));
				
				// Construct a single string containing every tag on this project
				StringBuilder tagsString = new StringBuilder();
				for(int i=0; i < tags.size(); i++) {
					tagsString.append("#").append(tags.get(i)).append(", ");
				}
				add(new JLabel(tagsString.toString()));

				if (accountID != null) {
					JButton deleteButton = new JButton("DELETE");
					deleteButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							CurrentSession.DeleteProject(accountID, projectID);
							ContentPanel.this.removeThumbnail(projectID);
							ProjectThumbnailList.this.reloadThumbnails();
						}
					});
					this.add(deleteButton);
				}
				
			}
		}
	}

}
