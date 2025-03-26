package FrontEnd;

import BackEnd.Accounts.Project;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.List;
import java.awt.Dimension;
import java.awt.FlowLayout;

/* 
 * Class to represent a visual list of projects, each containing a thumbnail and basic project information
 */
public class ProjectThumbnailList extends JScrollPane {
	
	private class ContentPanel extends JPanel {

		private class ProjectThumbnail extends JPanel {

			public ProjectThumbnail(String title, String username, String thumbnail, List<String> tags) {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				setMaximumSize(new Dimension(300, 300));

				// TODO: add image stuff
				add(new JLabel("[THIS IS AN IMAGE]"));
				
				add(new JLabel(title));
				add(new JLabel("By " + username));
				
				// Construct a single string containing every tag on this project
				String tagsString = "";
				Object[] tagsArray = tags.toArray();
				for(int i=0; i < tagsArray.length; i++) {
					tagsString = tagsString + "#" + tagsArray[i].toString() + "; ";
				}
				add(new JLabel(tagsString));
				
			}
		}

		public ContentPanel() {
			setLayout(new FlowLayout());
		}

		public void add(Project p) {
			add(new ProjectThumbnail(p.title, p.username, p.thumbnail, p.tags));
		}
	}

	private ContentPanel contents;
	
	public ProjectThumbnailList() {

		contents = new ContentPanel();

		setViewportView(contents);
	}

	public void addProject(Project p) {
		contents.add(p);
	}
}
