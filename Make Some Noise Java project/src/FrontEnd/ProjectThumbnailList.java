package FrontEnd;

import BackEnd.Accounts.CurrentSession;
import BackEnd.Accounts.Project;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

	// constructor
	public ProjectThumbnailList(List<Integer> projectIDs) {
		contents = new ContentPanel();
		setViewportView(contents);
	}

	//////////////////
	public void addProject(Integer projectID) {
		contents.addThumbnail(CurrentSession.GetProjectInfo(projectID), CurrentSession.getProjectTags(projectID));
	}

	private class ContentPanel extends JPanel {
		List<Integer> projectIDs;

		//constructor
		public ContentPanel() {
			setLayout(new FlowLayout());
		}
		public void addThumbnail(Map<String, String> proj, List<String> tags) {
			add(new ProjectThumbnail(proj.get("title"), proj.get("username"), proj.get("dateCreated"), proj.get("thumbnail"), tags));
		}

		//////////////////
		private class ProjectThumbnail extends JPanel {

			public ProjectThumbnail(String title, String username, String dateCreated, String thumbnail, List<String> tags) {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				setMaximumSize(new Dimension(300, 300));

				// TODO: add image stuff
				add(new JLabel("[THIS IS AN IMAGE]"));
				
				add(new JLabel(title));
				add(new JLabel("By " + username));
				add(new JLabel("Created " + dateCreated));
				
				// Construct a single string containing every tag on this project
				StringBuilder tagsString = new StringBuilder();
				for(int i=0; i < tags.size(); i++) {
					tagsString.append("#").append(tags.get(i)).append(", ");
				}
				add(new JLabel(tagsString.toString()));
				
			}
		}
	}

}
