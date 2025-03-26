package FrontEnd;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class SearchBar extends JTextField {
	
	public SearchBar() {
		setMaximumSize(new Dimension(10000, 10));
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
}
