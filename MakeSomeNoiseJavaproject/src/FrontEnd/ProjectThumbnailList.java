package FrontEnd;

/**
 * @author Maya Malavasi, Ryan Shipp
 * Class that arranges JPanels to form a visual representation of a list of projects. ProjectThumbnailList serves as a
 * container class for subclass {@ContentPanel}, which contains the order and arrangement of the thumbnails and in turn
 * contains subclass {@ProjectThumbnail}. Each project in the ProjectThumbnailList has its own ProjectThumbnail.
 */
import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

/* 
 * Class to represent a visual list of projects, each containing a thumbnail and basic project information
 */
public class ProjectThumbnailList extends JScrollPane {

	private ContentPanel contents;
	private Integer accountID;
	private MakeSomeNoiseWindow mainWindow;

	/**
	 * Constructs a new ProjectThumbnailList container panel
	 * @param mainWindow the main window/graphics controller Jpanel of the overall project
	 * @param accountID current account ID, null if not logged in
	 * @param projectIDs A List of Integer project IDs to display
	 */
	// constructor
	public ProjectThumbnailList(MakeSomeNoiseWindow mainWindow, Integer accountID, List<Integer> projectIDs) {
		this.mainWindow = mainWindow;
		this.accountID = accountID;
		contents = new ContentPanel(projectIDs);
		setViewportView(contents);
	}


	/** Reloads/repaints the current panel to update it. Works by removing all contents and re-adding the projects as
	 * new thumbnails.
	 */
	private void reloadThumbnails(){
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

	/** Indicates to the main window to switch to the editor panel, opening and editing the given selected project.
	 * @param projectInfo the project's JSON string
	 */
	private void goToEditorPanel(String projectInfo){
		this.mainWindow.addEditorPanel(Project.fromJSONtoProject(projectInfo));
		this.mainWindow.goToEditorPanel();
	}

	/** Add another project to the Thumbnail display list.
	 * @param projectID the Integer ID of the project to add
	 */
	public void addProject(Integer projectID) {
		contents.addThumbnail(accountID, projectID);
	}

	/** Add list of projects to the Thumbnail display list
	 * @param projectIDs List of Integer IDs of the projects to add
	 */
	public void addProjectList(List<Integer> projectIDs) {
		projectIDs.forEach(this::addProject);
	}


	/////////////////////
	/**
	 * Class that arranges and contains the ProjectThumbnail objects in a designated layout format.
	 */
	private class ContentPanel extends JPanel {
		List<Integer> projectIDs;

		//constructor
		/** Constructor specifies the layout as a JPanel FlowLayout, with an empty list of projects. */
		public ContentPanel() {
			setLayout(new FlowLayout());
		}

		/** Constructor specifies the layout as a JPanel FlowLayout, and adds a ProjectThumbnail for each project in the
		 * given list. 
		 * @param projectIDs the list of Integer project IDs to display
		 * */
		public ContentPanel(List<Integer> projectIDs) {
			super();
			this.projectIDs = projectIDs; // <-- store for reloads
			setLayout(new FlowLayout());
			projectIDs.forEach(i -> this.addThumbnail(accountID, i));
		}

		/** Creates a ProjectThumbnail display and adds it to the contents.
		 * @param accountID the ID of the current signed-in account. Null if guest.
		 * @param projectID the ID of the project to display */
		public void addThumbnail(Integer accountID, Integer projectID) {
			add(new ProjectThumbnail(accountID, projectID));
		}

		/** Removes a ProjectThumbnail display from the contents.
		 * @param projectID the ID of the project to display */
		public void removeThumbnail(Integer projectID) {
			this.projectIDs.remove(projectID);
		}

		//////////////////
		/** ProjectThumbnail is a template for any project thumbnail display. Each ProjectThumbnail contains a thumbnail
		 * image and basic project information, and will perform certain actions when clicked, depending on sign-in. Can
		 * be used for personal project lists, lists of shared projects, or any other functionality that requires a display of
		 * several projects. */
		private class ProjectThumbnail extends JPanel {

			/** Constructor collects the project information to display, the different text, link, and button components
			 * of the Thumbnail square object, and then displays them as a smaller panel.
			 * @param accountID the ID of the current signed-in account. Null if guest.
			 * @param projectID the ID of the project for the current thumbnail instance
			 * */
			public ProjectThumbnail(Integer accountID, Integer projectID) {
				Map<String, String> projectInfo =  CurrentSession.GetProjectInfo(projectID);
				List<String> tags = CurrentSession.getProjectTags(projectID);


				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				setMaximumSize(new Dimension(300, 300));

				this.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(final MouseEvent e) {
						if (accountID == null || !ProjectThumbnailList.this.mainWindow.getSignedIn().equals(CurrentSession.getProjectAccountID(projectID))){ // if it's null, it's not your project or you're on share
							//so download the file.
							ProjectThumbnailList.this.mainWindow.saveProjectLocal(Project.fromJSONtoProject(projectInfo.get("projectInfoStruct")));
						} else {
							ProjectThumbnailList.this.goToEditorPanel(projectInfo.get("projectInfoStruct"));
						}
					}
				});

				BufferedImage tn;
				try {
					tn = ImageIO.read(new File(projectInfo.get("thumbnail")));
					Image scaled = tn.getScaledInstance(90, 50, Image.SCALE_SMOOTH);
					JLabel picLabel = new JLabel(new ImageIcon(scaled),SwingConstants.CENTER);
					add(picLabel);
				} catch (Exception e){
					add(new JLabel("[Image could not be loaded]"));
				}

				add(new JLabel(projectInfo.get("title")));
				add(new JLabel("By " + projectInfo.get("username")));
				add(new JLabel("Created " + projectInfo.get("dateCreated")));
				
				// Construct a single string containing every tag on this project
				StringBuilder tagsString = new StringBuilder();
				for(int i=0; i < tags.size(); i++) {
					tagsString.append("#").append(tags.get(i));
					if (i != tags.size()-1)
						tagsString.append(", ");
				}
				add(new JLabel(tagsString.toString()));

				if (accountID != null || ProjectThumbnailList.this.mainWindow.isAdmin()
						|| (CurrentSession.getProjectAccountID(projectID).equals(ProjectThumbnailList.this.mainWindow.getSignedIn()))
					){
					JButton deleteButton = new JButton("DELETE");
					deleteButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							Integer accID = accountID;
							if (ProjectThumbnailList.this.mainWindow.isAdmin()) accID = CurrentSession.getProjectAccountID(projectID);
							CurrentSession.DeleteProject(accID, projectID);
							ContentPanel.this.removeThumbnail(projectID);
							ProjectThumbnailList.this.reloadThumbnails();
						}
					});
					this.add(deleteButton);
				}
				add(new JLabel("Status: " + projectInfo.get("status")));
				
			}
		}
	}

}
